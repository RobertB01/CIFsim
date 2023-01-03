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

package org.eclipse.escet.chi.codegen;

import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createProcessTypeID;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.chi.codegen.classes.SelectAlternativeClass;
import org.eclipse.escet.chi.codegen.java.JavaClass;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.types.TypeID;
import org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Context of the code generator. */
public class CodeGeneratorContext {
    /** Name of the specification class. */
    public final String specName;

    /** Generated Java classes. */
    public List<JavaFile> files = list();

    /** Names at the global scope of the code generator. */
    private Map<PositionObject, String> globalScope = null;

    /** Names at the model, proc, or func definition scope. */
    private Map<PositionObject, String> defScope = null;

    /** Names in a select alternative scope of the code generator. */
    private Map<PositionObject, String> selectAltScope = null;

    /** The currently active scope. */
    private ActiveScope activeScope = ActiveScope.NONE;

    /** Unique number in the specification. */
    private int specUniqueNumber = 1;

    /** Unique number in a declaration. */
    private int declUniqueNumber = -1;

    /** Mapping of generated classes for types (functions, processes, tuples, etc). */
    private Map<TypeID, String> classNames = map();

    /** Mapping of 'enumerate' use to its implementation name. */
    private Map<TypeID, String> enumerateNames = map();

    /** Generated parameter read functions (for reading values from the command-line). */
    public Set<TypeID> readParamFuncs = set();

    /**
     * Map of process variable types to process definitions. Map is used for generating process variable enumerations in
     * the specification.
     *
     * <p>
     * Note that it may contain 'useless' variable types (with a type signature different from all available process
     * definitions). In particular, such a type may be used as formal parameter of a process definition.
     * </p>
     */
    public Map<TypeID, ProcessTypeDefinition> processTypes = map();

    /**
     * Context of the code generation process.
     *
     * @param specName Name of the specification.
     */
    public CodeGeneratorContext(String specName) {
        this.specName = specName;
    }

    /**
     * Construct a new java class, and add it to the set of generated classes.
     *
     * @param name Name of the new class (should be unique).
     * @param isAbstract Class in an abstract class.
     * @param baseclassName Name of the base-class of the new class.
     * @param interfaces Interfaces that the new class implements.
     * @return The (empty) constructed java class object.
     */
    public JavaClass addJavaClass(String name, boolean isAbstract, String baseclassName, List<String> interfaces) {
        JavaClass jc = new JavaClass(null, isAbstract, name, baseclassName, interfaces);
        addClass(jc);
        return jc;
    }

    /**
     * Add a java class to the list of generated classes.
     *
     * @param newFile Java class to add.
     */
    public void addClass(JavaFile newFile) {
        // Verify that the new class name is unique.
        for (JavaFile jc: files) {
            Assert.check(jc.getFQclassname() != newFile.getFQclassname());
        }
        files.add(newFile);
    }

    /**
     * Add the (class-)name of a type.
     *
     * @param tid Type id to add.
     * @param name Name of the type.
     */
    public void addTypeName(TypeID tid, String name) {
        classNames.put(tid, name);
    }

    /**
     * Is the (class-)name of a type already available.
     *
     * @param tid Type id to query.
     * @return A class-name of the given type ID is already available.
     */
    public boolean hasTypeName(TypeID tid) {
        return classNames.containsKey(tid);
    }

    /**
     * Retrieve the (class-)name of a type.
     *
     * @param tid Type id to retrieve.
     * @return Class name of the given type ID.
     */
    public String getTypeName(TypeID tid) {
        Assert.check(classNames.containsKey(tid));
        return classNames.get(tid);
    }

    /**
     * Get the name of the enumerate class for the given source type, if it exists.
     *
     * @param tid Source type ID
     * @return Name of the class with the enumerate functions if it exists, {@code null} otherwise.
     */
    public String getEnumerateName(TypeID tid) {
        return enumerateNames.get(tid);
    }

    /**
     * Add the enumeration class name for the given type.
     *
     * @param tid Source type ID.
     * @param name Name of the enumeration class name of the given type.
     */
    public void addEnumerateName(TypeID tid, String name) {
        Assert.check(!enumerateNames.containsKey(tid));
        enumerateNames.put(tid, name);
    }

    /**
     * Add an entry for a process type if it does not exist.
     *
     * @param tid Process type.
     */
    public void addProcessType(TypeID tid) {
        ProcessTypeDefinition ptd = processTypes.get(tid);
        if (ptd == null) {
            ptd = new ProcessTypeDefinition(tid);
            processTypes.put(tid, ptd);
        }
    }

    /**
     * Add a process definition with this process type.
     *
     * @param pd Process definition to add.
     */
    public void addProcessDefinition(ProcessDeclaration pd) {
        // This call creates an entry in 'processTypes' if it does not exist.
        TypeID bdTid = createProcessTypeID(pd, this);

        ProcessTypeDefinition ptd = processTypes.get(bdTid);
        ptd.addProcess(pd);
    }

    /** Generate process type enumerations to the provided specification class. */
    public void generateProcessTypes() {
        for (Entry<TypeID, ProcessTypeDefinition> entry: processTypes.entrySet()) {
            entry.getValue().generate(this);
        }
    }

    /**
     * Construct a unique name.
     *
     * @param prefix Name prefix to use.
     * @return The unique name.
     */
    public String makeUniqueName(String prefix) {
        specUniqueNumber++;
        return fmt("%s_%d", prefix, specUniqueNumber);
    }

    /** Start processing a new declaration. */
    public void startNewDeclaration() {
        declUniqueNumber = 10; // 10 makes it easier to recognize problems.
    }

    /** Declaration has been processed completely. */
    public void stopNewDeclaration() {
        declUniqueNumber = -1;
    }

    /**
     * Get a unique number within a declaration.
     *
     * <p>
     * Note that calling this function will fail unless {@link #startNewDeclaration()} has not been called last. The
     * {@link #stopNewDeclaration()} function disables access to the value again.
     * </p>
     *
     * @return A unique number within the declaration.
     */
    public int getUniqueDeclNumber() {
        Assert.check(declUniqueNumber >= 0);
        declUniqueNumber++;
        return declUniqueNumber;
    }

    /**
     * Open a new scope in the code generation process.
     *
     * @param newScope Type of new scope. Should be an immediate sub-scope of the current one.
     */
    public void openScope(ActiveScope newScope) {
        Assert.check(newScope != ActiveScope.NONE && activeScope == newScope.getEnclosing());
        activeScope = newScope;
        switch (activeScope) {
            case DEFINITION:
                defScope = map();
                break;
            case GLOBALS:
                globalScope = map();
                break;
            case SELECT_ALT:
                selectAltScope = map();
                break;
            default:
                Assert.fail("Unknown scope type encountered while opening");
        }
    }

    /** Close the current scope. */
    public void closeScope() {
        switch (activeScope) {
            case DEFINITION:
                defScope = null;
                break;
            case GLOBALS:
                globalScope = null;
                break;
            case SELECT_ALT:
                selectAltScope = null;
                break;
            default:
                Assert.fail("Unknown scope type encountered while closing");
        }
        activeScope = activeScope.getEnclosing();
    }

    /** Available active scopes. */
    public enum ActiveScope {
        /** No scope. */
        NONE,

        /** Global scope. */
        GLOBALS,

        /** Model, process, or function. */
        DEFINITION,

        /** Select alternative class. */
        SELECT_ALT;

        /**
         * Get the enclosing scope of the given one.
         *
         * @return The scope surrounding the given one.
         */
        public ActiveScope getEnclosing() {
            switch (this) {
                case DEFINITION:
                    return GLOBALS;
                case GLOBALS:
                    return NONE;
                case NONE:
                    return NONE;
                case SELECT_ALT:
                    return DEFINITION;
            }
            Assert.fail("Unknow kind of current scope.");
            return NONE; // Never reached.
        }
    }

    /**
     * Add a Chi definition to the scope.
     *
     * @param def Definition to add.
     * @param name Name of the definition in Java.
     */
    public void addDefinition(PositionObject def, String name) {
        switch (activeScope) {
            case DEFINITION:
                Assert.check(!defScope.containsKey(def));
                defScope.put(def, name);
                break;
            case GLOBALS:
                Assert.check(!globalScope.containsKey(def));
                globalScope.put(def, name);
                break;
            case SELECT_ALT:
                Assert.check(!selectAltScope.containsKey(def));
                selectAltScope.put(def, name);
                break;
            default:
                Assert.fail("Unknown scope type encountered while adding");
        }
    }

    /**
     * Query the name of a definition.
     *
     * @param def Definition to search.
     * @return Java name of the definition.
     */
    public String getDefinition(PositionObject def) {
        switch (activeScope) {
            case SELECT_ALT: {
                String name = selectAltScope.get(def);
                if (name != null) {
                    return name;
                }
            }
            //$FALL-THROUGH$
            case DEFINITION: {
                String name = defScope.get(def);
                if (name != null) {
                    if (activeScope == ActiveScope.SELECT_ALT) {
                        return SelectAlternativeClass.PROCESS_VAR + "." + name;
                    }
                    return name;
                }
            }
            //$FALL-THROUGH$
            case GLOBALS: {
                String name = globalScope.get(def);
                return name;
            }
            default:
                Assert.fail("Unknown scope type encountered while getting");
                return null;
        }
    }
}
