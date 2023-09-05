//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
//
// See the NOTICE file(s) distributed with this work for additional
// information regarding copyright ownership.
//
// This program and the accompanying materials are made available
// under the terms of the MIT License which is available at
// https://opensource.org/licenses/MIT
//
// SPDX-License-Identifier: MIT
//////////////////////////////////////////////////////////////////////////////

package org.eclipse.escet.cif.typechecker.postchk;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.RegistryFactory;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.annotations.AnnotatedObject;
import org.eclipse.escet.cif.metamodel.cif.annotations.Annotation;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProblemReporter;
import org.eclipse.escet.cif.typechecker.annotations.AnnotationProvider;
import org.eclipse.escet.cif.typechecker.annotations.DoNothingAnnotationProvider;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

/**
 * CIF annotations additional type checking, for the 'post' type checking phase, using {@link AnnotationProvider
 * annotation providers}.
 */
public class CifAnnotationsPostChecker extends CifWalker {
    /** The post check environment to use. */
    private final CifPostCheckEnv env;

    /** Problem reporter for {@link AnnotationProvider annotation providers} to use to report problems. */
    private final ProblemReporter reporter = new ProblemReporter();

    /** Per annotation name, the annotation provider. */
    private final Map<String, AnnotationProvider> annotationProviders = map();

    /**
     * Constructor for the {@link CifAnnotationsPostChecker} class.
     *
     * @param env The post check environment to use.
     */
    public CifAnnotationsPostChecker(CifPostCheckEnv env) {
        this.env = env;
    }

    /**
     * Perform additional type checking on annotations using annotation providers.
     *
     * @param spec The specification to check. The specification must not include any component
     *     definitions/instantiations.
     */
    public void check(Specification spec) {
        // Find all annotations, and type check them using their corresponding annotation providers.
        walkSpecification(spec);

        // Let the annotation providers for the found annotations perform additional global type checking on the entire
        // specification.
        for (AnnotationProvider provider: annotationProviders.values()) {
            provider.checkGlobal(spec, reporter);
        }
    }

    @Override
    protected void preprocessAnnotatedObject(AnnotatedObject annotatedObj) {
        // Type check each annotation.
        for (Annotation annotation: annotatedObj.getAnnotations()) {
            // Get the annotation name.
            String annotationName = annotation.getName();

            // Get the annotation provider.
            AnnotationProvider provider = annotationProviders.get(annotationName);
            if (provider == null) {
                provider = getProvider(annotationName, annotation.getPosition());
                if (provider == null) {
                    // Could not find an annotation provider, or could not load it. Don't try again for next annotation
                    // with the same name.
                    provider = new DoNothingAnnotationProvider(annotationName);
                }
                annotationProviders.put(annotationName, provider);
            }

            // Let the provider check the annotation.
            provider.checkAnnotation(annotation, reporter);
        }
    }

    /**
     * Get the annotation provider for annotations with the given name.
     *
     * @param annotationName The annotation name.
     * @param position The position of the annotation.
     * @return The annotation provider, or {@code null} if not found, or if it could not be loaded.
     */
    private AnnotationProvider getProvider(String annotationName, Position position) {
        // Use extension registry to find registered providers.
        IExtensionRegistry registry = RegistryFactory.getRegistry();
        String extensionPointId = "org.eclipse.escet.cif.annotations";
        IConfigurationElement[] extensions = registry.getConfigurationElementsFor(extensionPointId);

        // Get provider for the requested annotation name.
        List<AnnotationProvider> providers = listc(1);
        boolean anyIssue = false;
        for (IConfigurationElement extension: extensions) {
            // Skip non-provider extensions.
            if (!"provider".equals(extension.getName())) {
                continue;
            }

            // Skip providers for other annotations.
            if (!annotationName.equals(extension.getAttribute("annotationName"))) {
                continue;
            }

            // Get contributor name.
            String contributorName = extension.getContributor().getName();

            // Get OSGi bundle name.
            String pluginName = extension.getAttribute("plugin");
            if (pluginName == null) {
                anyIssue = true;
                env.addProblem(ErrMsg.ANNO_PROVIDER_ERROR, position, annotationName,
                        fmt("annotation provider does not specify a plugin (contributed by \"%s\").", contributorName));
                // Non-fatal problem.
                continue;
            }

            // Get class name.
            String className = extension.getAttribute("class");
            if (className == null) {
                anyIssue = true;
                env.addProblem(ErrMsg.ANNO_PROVIDER_ERROR, position, annotationName,
                        fmt("annotation provider does not specify a class (contributed by \"%s\").", contributorName));
                // Non-fatal problem.
                continue;
            }

            // Get OSGi bundle.
            Bundle bundle = Platform.getBundle(pluginName);
            if (bundle == null) {
                anyIssue = true;
                env.addProblem(ErrMsg.ANNO_PROVIDER_ERROR, position, annotationName,
                        fmt("annotation provider plugin \"%s\" not found (contributed by \"%s\").", pluginName,
                                contributorName));
                // Non-fatal problem.
                continue;
            }

            // Check bundle state.
            int state = bundle.getState();
            boolean stateOk = state == Bundle.RESOLVED || state == Bundle.STARTING || state == Bundle.ACTIVE;
            if (!stateOk) {
                anyIssue = true;
                env.addProblem(ErrMsg.ANNO_PROVIDER_ERROR, position, annotationName,
                        fmt("annotation provider plugin \"%s\" is in a wrong state (state %d, contributed by \"%s\").",
                                pluginName, state, contributorName));
                // Non-fatal problem.
                continue;
            }

            // Get class loader from bundle.
            BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
            if (bundleWiring == null) {
                anyIssue = true;
                env.addProblem(ErrMsg.ANNO_PROVIDER_ERROR, position, annotationName,
                        fmt("annotation provider plugin \"%s\" has no bundle wiring (contributed by \"%s\").",
                                pluginName, contributorName));
                // Non-fatal problem.
                continue;
            }

            ClassLoader classLoader = bundleWiring.getClassLoader();
            if (classLoader == null) {
                anyIssue = true;
                env.addProblem(ErrMsg.ANNO_PROVIDER_ERROR, position, annotationName,
                        fmt("annotation provider plugin \"%s\" has no class loader (contributed by \"%s\").",
                                pluginName, contributorName));
                // Non-fatal problem.
                continue;
            }

            // Get class.
            Class<?> cls;
            try {
                cls = classLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                anyIssue = true;
                env.addProblem(ErrMsg.ANNO_PROVIDER_ERROR, position, annotationName,
                        fmt("annotation provider plugin \"%s\" is missing annotation provider class \"%s\" "
                                + "(contributed by \"%s\").", pluginName, className, contributorName));
                // Non-fatal problem.
                continue;
            }

            // Get provider.
            AnnotationProvider provider;
            try {
                provider = (AnnotationProvider)cls.getDeclaredConstructor(String.class).newInstance(annotationName);
            } catch (ReflectiveOperationException e) {
                anyIssue = true;
                env.addProblem(ErrMsg.ANNO_PROVIDER_ERROR, position, annotationName,
                        fmt("annotation provider plugin \"%s\" has an annotation provider class \"%s\" "
                                + "that could not be instantiated (contributed by \"%s\").", pluginName, className,
                                contributorName));
                // Non-fatal problem.
                continue;
            }

            // Add provider.
            providers.add(provider);
        }

        // If any issues, we are not sure how many providers there are. The problems have already been reported.
        if (anyIssue) {
            return null;
        }

        // Expected exactly one provider.
        if (providers.size() == 1) {
            return providers.get(0);
        }

        // Not exactly one provider.
        if (providers.isEmpty()) {
            env.addProblem(ErrMsg.ANNO_UNREGISTERED_NAME, position, annotationName);
            // Non-fatal problem.
        } else {
            String names = providers.stream().map(p -> fmt("\"%s\"", p.getClass().getName())).sorted(Strings.SORTER)
                    .collect(Collectors.joining(", "));
            env.addProblem(ErrMsg.ANNO_PROVIDER_ERROR, position, annotationName, fmt(
                    "multiple annotation providers are registered for the annotation in the current environment: %s.",
                    names));
            // Non-fatal problem.
        }
        return null;
    }

    /** Problem reporter for {@link AnnotationProvider annotation providers} to use to report problems. */
    private class ProblemReporter implements AnnotationProblemReporter {
        @Override
        public void reportProblem(String annotationName, String message, Position position,
                SemanticProblemSeverity severity)
        {
            ErrMsg msg = switch (severity) {
                case ERROR -> ErrMsg.ANNO_SPECIFIC_ERR;
                case WARNING -> ErrMsg.ANNO_SPECIFIC_WARN;
                default -> throw new AssertionError("Unknown severity: " + severity);
            };
            env.addProblem(msg, position, annotationName, message);
            // Non-fatal problem.
        }
    }
}
