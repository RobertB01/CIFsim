//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.codegen.types;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.statements.seq.SeqSelect.SelectAlternative;
import org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration;
import org.eclipse.escet.chi.metamodel.chi.BoolType;
import org.eclipse.escet.chi.metamodel.chi.ChannelType;
import org.eclipse.escet.chi.metamodel.chi.DictType;
import org.eclipse.escet.chi.metamodel.chi.DistributionType;
import org.eclipse.escet.chi.metamodel.chi.EnumDeclaration;
import org.eclipse.escet.chi.metamodel.chi.EnumTypeReference;
import org.eclipse.escet.chi.metamodel.chi.FileType;
import org.eclipse.escet.chi.metamodel.chi.FunctionDeclaration;
import org.eclipse.escet.chi.metamodel.chi.FunctionType;
import org.eclipse.escet.chi.metamodel.chi.InstanceType;
import org.eclipse.escet.chi.metamodel.chi.IntType;
import org.eclipse.escet.chi.metamodel.chi.ListType;
import org.eclipse.escet.chi.metamodel.chi.MatrixType;
import org.eclipse.escet.chi.metamodel.chi.ModelDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ProcessType;
import org.eclipse.escet.chi.metamodel.chi.RealType;
import org.eclipse.escet.chi.metamodel.chi.SetType;
import org.eclipse.escet.chi.metamodel.chi.StringType;
import org.eclipse.escet.chi.metamodel.chi.TimerType;
import org.eclipse.escet.chi.metamodel.chi.TupleField;
import org.eclipse.escet.chi.metamodel.chi.TupleType;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.TypeReference;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;
import org.eclipse.escet.chi.metamodel.chi.VoidType;
import org.eclipse.escet.common.java.Assert;

/** Helper class for creating TypeID objects. */
public class TypeIDCreation {
    /** Private constructor to prevent instantiation. */
    private TypeIDCreation() {
        // Intentionally empty.
    }

    /**
     * Construct a representation of a Chi program type.
     *
     * @param tp Meta-model object to convert.
     * @param ctxt Code generator context.
     * @return The created representation of the Chi type.
     */
    public static TypeID createTypeID(Type tp, CodeGeneratorContext ctxt) {
        tp = dropTypeReferences(tp);
        if (tp instanceof BoolType) {
            return makeBooleanTypeID();
        } else if (tp instanceof IntType) {
            return makeIntTypeID();
        } else if (tp instanceof StringType) {
            return makeStringTypeID();
        } else if (tp instanceof RealType) {
            return new RealTypeID();
        } else if (tp instanceof InstanceType) {
            return new InstanceTypeID();
        } else if (tp instanceof EnumTypeReference) {
            EnumTypeReference etr = (EnumTypeReference)tp;
            return createEnumTypeID(etr.getType(), ctxt);
        }

        if (tp instanceof MatrixType) {
            return new MatrixTypeID((MatrixType)tp);
        } else if (tp instanceof ChannelType) {
            return makeChannelTypeID((ChannelType)tp, ctxt);
        } else if (tp instanceof TupleType) {
            return makeTupleTypeID((TupleType)tp, ctxt);
        } else if (tp instanceof FunctionType) {
            return makeFunctionTypeID((FunctionType)tp, ctxt);
        } else if (tp instanceof ProcessType) {
            return makeProcessTypeID((ProcessType)tp, ctxt);
        } else if (tp instanceof ListType) {
            return makeListTypeID((ListType)tp, ctxt);
        } else if (tp instanceof SetType) {
            return makeSetTypeID((SetType)tp, ctxt);
        } else if (tp instanceof DictType) {
            return makeDictTypeID((DictType)tp, ctxt);
        } else if (tp instanceof DistributionType) {
            return makeDistributionTypeID((DistributionType)tp, ctxt);
        } else if (tp instanceof TimerType) {
            return new TimerTypeID();
        } else if (tp instanceof FileType) {
            return new FileTypeID();
        }

        // void and unresolved type are not handled, as they should never
        // happen here (void channels are handled in the channel function).
        //
        Assert.fail("Unhandled type '" + tp.toString() + "' encountered in createTypeID.");
        return null;
    }

    /**
     * Construct a Type id for an enumeration declaration.
     *
     * @param decl Enumeration declaration to use.
     * @param ctxt Code generator context.
     * @return The created type id.
     */
    public static EnumTypeID createEnumTypeID(EnumDeclaration decl, CodeGeneratorContext ctxt) {
        return new EnumTypeID(decl, ctxt);
    }

    /**
     * Construct a type id for a boolean type.
     *
     * @return The created type id.
     */
    public static BooleanTypeID makeBooleanTypeID() {
        return new BooleanTypeID();
    }

    /**
     * Construct a type id for an integer type.
     *
     * @return The created type id.
     */
    public static IntTypeID makeIntTypeID() {
        return new IntTypeID();
    }

    /**
     * Construct a type id for a string type.
     *
     * @return The created type id.
     */
    public static StringTypeID makeStringTypeID() {
        return new StringTypeID();
    }

    /**
     * Construct a type ID for a process type.
     *
     * @param bd Process or model declaration.
     * @param ctxt Code generator context.
     * @return Type id of the process type.
     */
    public static TypeID createProcessTypeID(BehaviourDeclaration bd, CodeGeneratorContext ctxt) {
        Assert.check((bd instanceof ProcessDeclaration) || (bd instanceof ModelDeclaration));
        List<TypeID> args = list();
        for (VariableDeclaration vd: bd.getVariables()) {
            if (!vd.isParameter()) {
                continue;
            }
            args.add(createTypeID(vd.getType(), ctxt));
        }
        return new ProcessTypeID(args, ctxt);
    }

    /**
     * Make a type ID for a process definition.
     *
     * @param p Process definition type.
     * @param ctxt Code generator context.
     * @return Constructed type ID.
     */
    private static TypeID makeProcessTypeID(ProcessType p, CodeGeneratorContext ctxt) {
        List<TypeID> subs = list();
        for (Type arg: p.getParameterTypes()) {
            subs.add(createTypeID(arg, ctxt));
        }
        return new ProcessTypeID(subs, ctxt);
    }

    /**
     * Make a type ID for a distribution type.
     *
     * @param s Distribution type.
     * @param ctxt Code generator context.
     * @return Constructed type ID.
     */
    private static TypeID makeDistributionTypeID(DistributionType s, CodeGeneratorContext ctxt) {
        return new DistributionTypeID(createTypeID(s.getResultType(), ctxt));
    }

    /**
     * Make a type ID for a channel type.
     *
     * <p>
     * The runtime does not care what kind of data is transferred, as that is handled by the generated derived
     * {@link SelectAlternative} classes.
     * </p>
     *
     * @param c Channel type.
     * @param ctxt Code generator context.
     * @return Constructed type ID.
     */
    private static TypeID makeChannelTypeID(ChannelType c, CodeGeneratorContext ctxt) {
        Type tp = dropTypeReferences(c.getElementType());
        TypeID dataType;
        if (tp instanceof VoidType) {
            dataType = null;
        } else {
            dataType = createTypeID(tp, ctxt);
        }
        return new ChannelTypeID(dataType);
    }

    /**
     * Construct a function type id from its function declaration.
     *
     * @param fd Provided function declaration.
     * @param ctxt Code generator context.
     * @return The created function type id.
     */
    public static TypeID createFunctionTypeID(FunctionDeclaration fd, CodeGeneratorContext ctxt) {
        List<TypeID> subs = list();
        for (VariableDeclaration vd: fd.getVariables()) {
            if (!vd.isParameter()) {
                continue;
            }
            subs.add(createTypeID(vd.getType(), ctxt));
        }
        subs.add(createTypeID(fd.getReturnType(), ctxt));
        return new FunctionTypeID(subs, ctxt);
    }

    /**
     * Make a type ID for a function definition.
     *
     * @param f Function definition type.
     * @param ctxt Code generator context.
     * @return Constructed type ID.
     */
    private static TypeID makeFunctionTypeID(FunctionType f, CodeGeneratorContext ctxt) {
        List<TypeID> subs = list();
        for (Type arg: f.getParameterTypes()) {
            subs.add(createTypeID(arg, ctxt));
        }
        subs.add(createTypeID(f.getResultType(), ctxt));
        return makeFunctionTypeID(subs, ctxt);
    }

    /**
     * Make a type ID for a function definition.
     *
     * @param subs Sub-types (result is the last sub-type).
     * @param ctxt Code generator context.
     * @return Constructed type ID.
     */
    public static TypeID makeFunctionTypeID(List<TypeID> subs, CodeGeneratorContext ctxt) {
        return new FunctionTypeID(subs, ctxt);
    }

    /**
     * Make a type ID for a tuple definition.
     *
     * @param t Tuple definition type.
     * @param ctxt Code generator context.
     * @return Constructed type ID.
     */
    private static TypeID makeTupleTypeID(TupleType t, CodeGeneratorContext ctxt) {
        List<String> fieldNames = list();
        List<TypeID> subs = list();
        for (TupleField arg: t.getFields()) {
            if (arg.getName() == null) {
                fieldNames.add("");
            } else {
                fieldNames.add(arg.getName());
            }
            subs.add(createTypeID(arg.getType(), ctxt));
        }
        return createTupleTypeID(fieldNames, subs, ctxt);
    }

    /**
     * Make a type ID for a sequence of field types.
     *
     * @param fieldNames Names of the fields in the tuple.
     * @param subs Type Ids of the fields.
     * @param ctxt Code generator context.
     * @return Constructed type ID.
     */
    public static TypeID createTupleTypeID(List<String> fieldNames, List<TypeID> subs, CodeGeneratorContext ctxt) {
        return new TupleTypeID(fieldNames, subs, ctxt);
    }

    /**
     * Make a type ID for a list type.
     *
     * @param l List type.
     * @param ctxt Code generator context.
     * @return Constructed type ID.
     */
    private static TypeID makeListTypeID(ListType l, CodeGeneratorContext ctxt) {
        TypeID tid = createTypeID(l.getElementType(), ctxt);
        return makeListTypeID(tid, ctxt);
    }

    /**
     * Construct a list type Id for a list with the given element type and initial length.
     *
     * @param elmTid Type ID of the list elements.
     * @param ctxt Code generator context.
     * @return Constructed type ID of the list type.
     */
    public static TypeID makeListTypeID(TypeID elmTid, CodeGeneratorContext ctxt) {
        return new ListTypeID(elmTid, ctxt);
    }

    /**
     * Make a type ID for a set type.
     *
     * @param st set type.
     * @param ctxt Code generator context.
     * @return Constructed type ID.
     */
    private static TypeID makeSetTypeID(SetType st, CodeGeneratorContext ctxt) {
        TypeID eTid = createTypeID(st.getElementType(), ctxt);
        return makeSetTypeID(eTid, ctxt);
    }

    /**
     * Make a type ID for a set with the given element type ID.
     *
     * @param elmTid Element type id.
     * @param ctxt Code generator context.
     * @return Type id of a set for the given element type.
     */
    public static TypeID makeSetTypeID(TypeID elmTid, CodeGeneratorContext ctxt) {
        return new SetTypeID(elmTid, ctxt);
    }

    /**
     * Make a type ID for a dictionary type.
     *
     * @param dtp Dictionary type.
     * @param ctxt Code generator context.
     * @return Constructed type ID.
     */
    private static TypeID makeDictTypeID(DictType dtp, CodeGeneratorContext ctxt) {
        TypeID kTid = createTypeID(dtp.getKeyType(), ctxt);
        TypeID vTid = createTypeID(dtp.getValueType(), ctxt);
        return new DictionaryTypeID(kTid, vTid, ctxt);
    }

    /**
     * Simplify a type by removing type declaration references from the 'head' of the type. It uses a simple protection
     * mechanism to detect cycles (although they should never happen).
     *
     * @param tp Type to simplify.
     * @return Simplified type.
     */
    public static Type dropTypeReferences(Type tp) {
        int count = 0;
        while (tp instanceof TypeReference) {
            Assert.check(count < 10000); // Safety net for infinite recursion.
            tp = ((TypeReference)tp).getType().getType();
            count++;
        }
        return tp;
    }
}
