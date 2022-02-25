//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.common.CifCollectUtils;
import org.eclipse.escet.cif.common.CifEnumUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.AlgParameter;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.ComponentInst;
import org.eclipse.escet.cif.metamodel.cif.ComponentParameter;
import org.eclipse.escet.cif.metamodel.cif.EventParameter;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.LocationParameter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.expressions.CastExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.java.Assert;

/**
 * In-place transformation that anonymizes the names of all named objects.
 *
 * <p>
 * Precondition: Specifications with automaton to string casts are not supported.
 * </p>
 */
public class AnonymizeNames extends CifWalker implements CifToCifTransformation {
    /**
     * Mapping from name prefixes to the next free number for that prefix. Mapping starts empty, and entries are added
     * as needed.
     */
    private Map<String, Integer> nextFreeNumbers;

    /** Mapping from enumeration declarations to their representatives. */
    private Map<EnumDecl, EnumDecl> enumRepresentatives;

    /** Mapping of enumeration literals of representative enumerations to their new names. */
    private Map<EnumLiteral, String> enumLitRepresentativesNewNames;

    @Override
    public void transform(Specification spec) {
        // Clear next free number administration.
        nextFreeNumbers = map();

        // Get enumeration representatives.
        List<EnumDecl> enumDecls = list();
        CifCollectUtils.collectEnumDecls(spec, enumDecls);
        enumRepresentatives = CifEnumUtils.getEnumDeclReprs(enumDecls);

        // Assign new names to enumeration literals of enumeration representatives.
        enumLitRepresentativesNewNames = map();
        for (EnumDecl enumDecl: enumRepresentatives.values()) {
            for (EnumLiteral lit: enumDecl.getLiterals()) {
                enumLitRepresentativesNewNames.put(lit, getName("lit"));
            }
        }

        // Perform the transformation by walking over the specification.
        walkSpecification(spec);
    }

    @Override
    protected void preprocessCastExpression(CastExpression castExpr) {
        // Automaton to string casts are not supported, to prevent the resulting specification from becoming invalid.
        // For instance, the renamed locations of the automaton could be compared to string literals.
        CifType childType = CifTypeUtils.normalizeType(castExpr.getChild().getType());
        CifType resultType = CifTypeUtils.normalizeType(castExpr.getType());
        if (CifTypeUtils.hasComponentLikeType(childType) && resultType instanceof StringType) {
            throw new CifToCifPreconditionException("Anonymizing names for a CIF specification with automaton to "
                    + "string casts is currently not supported.");
        }
    }

    /**
     * Returns a new name for an object to anonymize.
     *
     * @param prefix The prefix for the name.
     * @return The new name.
     */
    private String getName(String prefix) {
        Integer nextNr = nextFreeNumbers.get(prefix);
        if (nextNr == null) {
            nextNr = 1;
        }
        nextFreeNumbers.put(prefix, nextNr + 1);
        return prefix + nextNr;
    }

    @Override
    protected void preprocessAutomaton(Automaton automaton) {
        if (automaton.eContainer() instanceof ComponentDef) {
            // Body of an automaton definition.
            automaton.setName(getName("autdef"));
        } else {
            // Automaton.
            automaton.setName(getName("aut"));
        }
    }

    @Override
    protected void preprocessGroup(Group group) {
        if (group instanceof Specification) {
            // Specifications must have 'specification' as their name.
            Assert.check("specification".equals(group.getName()));
        } else if (group.eContainer() instanceof ComponentDef) {
            // Body of a group definition.
            group.setName(getName("grpdef"));
        } else {
            // Group.
            group.setName(getName("grp"));
        }
    }

    @Override
    protected void preprocessComponentInst(ComponentInst componentInst) {
        componentInst.setName(getName("inst"));
    }

    @Override
    protected void preprocessAlgVariable(AlgVariable algVar) {
        if (algVar.eContainer() instanceof AlgParameter) {
            // Algebraic parameter.
            algVar.setName(getName("aparam"));
        } else {
            // Algebraic variable.
            algVar.setName(getName("alg"));
        }
    }

    @Override
    protected void preprocessConstant(Constant constant) {
        constant.setName(getName("const"));
    }

    @Override
    protected void preprocessContVariable(ContVariable contVar) {
        contVar.setName(getName("cont"));
    }

    @Override
    protected void preprocessDiscVariable(DiscVariable discVar) {
        if (discVar.eContainer() instanceof FunctionParameter) {
            // Function parameter.
            discVar.setName(getName("fparam"));
        } else {
            // Discrete variable.
            discVar.setName(getName("disc"));
        }
    }

    @Override
    protected void preprocessEnumDecl(EnumDecl enumDecl) {
        enumDecl.setName(getName("enum"));
    }

    @Override
    protected void preprocessEvent(Event event) {
        // Give name based on being a parameter or an event.
        if (event.eContainer() instanceof EventParameter) {
            // Event parameter.
            event.setName(getName("eparam"));
        } else {
            // Event.
            event.setName(getName("evt"));
        }

        // Prefix event name based on its supervisory kind. Don't prefix events without a supervisory kind, to prevent
        // introducing such prefixes for specifications without (un)controllable events.
        if (event.getControllable() != null) {
            String prefix = event.getControllable() ? "c_" : "u_";
            event.setName(prefix + event.getName());
        }
    }

    @Override
    protected void preprocessFunction(Function function) {
        function.setName(getName("func"));
    }

    @Override
    protected void preprocessInputVariable(InputVariable inputVar) {
        inputVar.setName(getName("input"));
    }

    @Override
    protected void preprocessTypeDecl(TypeDecl typeDecl) {
        typeDecl.setName(getName("type"));
    }

    @Override
    protected void preprocessLocation(Location location) {
        if (location.eContainer() instanceof LocationParameter) {
            // Location parameter.
            Assert.notNull(location.getName());
            location.setName(getName("lparam"));
        } else {
            // Location.
            if (location.getName() != null) {
                location.setName(getName("loc"));
            }
        }
    }

    @Override
    protected void preprocessComponentParameter(ComponentParameter compParam) {
        compParam.setName(getName("cparam"));
    }

    @Override
    protected void preprocessEnumLiteral(EnumLiteral enumLit) {
        // Compatible enumerations will need to remain compatible.
        // We use the new names obtained previously for the literals of enumeration representatives.

        // Get enumeration representative.
        EnumDecl enumDecl = (EnumDecl)enumLit.eContainer();
        EnumDecl enumRepresentative = enumRepresentatives.get(enumDecl);

        // Get literal representative.
        int index = enumDecl.getLiterals().indexOf(enumLit);
        EnumLiteral litRepresentative = enumRepresentative.getLiterals().get(index);

        // Assign new name.
        enumLit.setName(enumLitRepresentativesNewNames.get(litRepresentative));
    }

    @Override
    protected void preprocessField(Field field) {
        // Can globally number the fields, as tuple type compatibility is based on field types, not field names.
        if (field.getName() != null) {
            field.setName(getName("field"));
        }
    }
}
