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

package org.eclipse.escet.chi.typecheck;

import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newBoolType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newChannelType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newDictType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newDistributionType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newEnumTypeReference;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newFileType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newFunctionType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newInstanceType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newIntNumber;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newIntType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newListType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newMatrixType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newModelType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newProcessType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newRealType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newSetType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newStringType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTimerType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTupleField;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTupleType;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newTypeReference;
import static org.eclipse.escet.chi.metamodel.java.ChiConstructors.newVoidType;
import static org.eclipse.escet.chi.typecheck.CheckExpression.evalExpression;
import static org.eclipse.escet.chi.typecheck.CheckExpression.transExpression;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.position.common.PositionUtils.copyPosition;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.chi.metamodel.chi.BoolType;
import org.eclipse.escet.chi.metamodel.chi.ChannelOps;
import org.eclipse.escet.chi.metamodel.chi.ChannelType;
import org.eclipse.escet.chi.metamodel.chi.DictType;
import org.eclipse.escet.chi.metamodel.chi.DistributionType;
import org.eclipse.escet.chi.metamodel.chi.EnumDeclaration;
import org.eclipse.escet.chi.metamodel.chi.EnumTypeReference;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.FileType;
import org.eclipse.escet.chi.metamodel.chi.FunctionType;
import org.eclipse.escet.chi.metamodel.chi.InstanceType;
import org.eclipse.escet.chi.metamodel.chi.IntType;
import org.eclipse.escet.chi.metamodel.chi.ListType;
import org.eclipse.escet.chi.metamodel.chi.MatrixType;
import org.eclipse.escet.chi.metamodel.chi.ModelType;
import org.eclipse.escet.chi.metamodel.chi.ProcessType;
import org.eclipse.escet.chi.metamodel.chi.RealType;
import org.eclipse.escet.chi.metamodel.chi.SetType;
import org.eclipse.escet.chi.metamodel.chi.StringType;
import org.eclipse.escet.chi.metamodel.chi.TimerType;
import org.eclipse.escet.chi.metamodel.chi.TupleField;
import org.eclipse.escet.chi.metamodel.chi.TupleType;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.TypeDeclaration;
import org.eclipse.escet.chi.metamodel.chi.TypeReference;
import org.eclipse.escet.chi.metamodel.chi.UnresolvedType;
import org.eclipse.escet.chi.metamodel.chi.VoidType;
import org.eclipse.escet.chi.typecheck.CheckContext.ContextItem;
import org.eclipse.escet.chi.typecheck.symbols.SymbolEntry;
import org.eclipse.escet.chi.typecheck.symbols.TypeSymbolEntry;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Perform type checking on types. */
public abstract class CheckType {
    /**
     * Fully copy a type.
     *
     * @param t Type to copy.
     * @return A copy of the entire type tree.
     */
    public static Type copyType(Type t) {
        return deepclone(t);
    }

    /**
     * Fully copy a list of types.
     *
     * @param tlist Types to copy.
     * @return A deep-cloned copy of the list.
     */
    public static List<Type> copyTypes(List<Type> tlist) {
        List<Type> newList = listc(tlist.size());
        for (Type t: tlist) {
            newList.add(copyType(t));
        }
        return newList;
    }

    /**
     * Decide whether two lists of types are matching.
     *
     * @param provs List of provided types.
     * @param reqs List of required types.
     * @return Whether the provided types pairwise fit in the required types.
     */
    public static boolean matchTypeList(List<Type> provs, List<Type> reqs) {
        if (provs.size() != reqs.size()) {
            return false;
        }
        for (int i = 0; i < provs.size(); i++) {
            if (!matchType(provs.get(i), reqs.get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Decide whether a provided type matches with the required type.
     *
     * @param prov Provided type.
     * @param req Required type.
     * @return Whether the provided type fits in the required type.
     */
    public static boolean matchType(Type prov, Type req) {
        for (;;) {
            Assert.check(prov != null);
            Assert.check(req != null);
            Assert.check(!(prov instanceof UnresolvedType) && !(req instanceof UnresolvedType));

            prov = dropReferences(prov);
            req = dropReferences(req);

            if (prov == req || (prov instanceof VoidType && req instanceof VoidType)
                    || (prov instanceof FileType && req instanceof FileType)
                    || (prov instanceof BoolType && req instanceof BoolType)
                    || (prov instanceof IntType && req instanceof IntType)
                    || (prov instanceof TimerType && req instanceof TimerType)
                    || (prov instanceof StringType && req instanceof StringType)
                    || (prov instanceof RealType && req instanceof RealType)
                    || (prov instanceof InstanceType && req instanceof InstanceType))
            {
                return true;
            } else if (prov instanceof ListType && req instanceof ListType) {
                prov = ((ListType)prov).getElementType();
                req = ((ListType)req).getElementType();
            } else if (prov instanceof SetType && req instanceof SetType) {
                prov = ((SetType)prov).getElementType();
                req = ((SetType)req).getElementType();
            } else if (prov instanceof DistributionType && req instanceof DistributionType) {
                prov = ((DistributionType)prov).getResultType();
                req = ((DistributionType)req).getResultType();
            } else if (prov instanceof ChannelType && req instanceof ChannelType) {
                ChannelType cprov = (ChannelType)prov;
                ChannelType creq = (ChannelType)req;

                switch (creq.getOps()) {
                    case RECEIVE:
                        if (cprov.getOps() == ChannelOps.SEND) {
                            return false;
                        }
                        break;

                    case SEND:
                        if (cprov.getOps() == ChannelOps.RECEIVE) {
                            return false;
                        }
                        break;

                    case SEND_RECEIVE:
                        if (cprov.getOps() != ChannelOps.SEND_RECEIVE) {
                            return false;
                        }
                        break;

                    default:
                        Assert.fail("Unknown channel direction encountered.");
                        break;
                }
                // else, left is always at least as large as right

                prov = cprov.getElementType();
                req = creq.getElementType();
            } else if (prov instanceof DictType && req instanceof DictType) {
                DictType d1 = (DictType)prov;
                DictType d2 = (DictType)req;
                if (!matchType(d1.getKeyType(), d2.getKeyType())) {
                    return false;
                }
                prov = d1.getValueType();
                req = d2.getValueType();
            } else if (prov instanceof FunctionType && req instanceof FunctionType) {
                FunctionType f1 = (FunctionType)prov;
                FunctionType f2 = (FunctionType)req;
                if (!matchTypeList(f1.getParameterTypes(), f2.getParameterTypes())) {
                    return false;
                }
                prov = f1.getResultType();
                req = f2.getResultType();
            } else if (prov instanceof ModelType && req instanceof ModelType) {
                ModelType procProv = (ModelType)prov;
                ModelType procReq = (ModelType)req;

                if (!matchTypeList(procProv.getParameterTypes(), procReq.getParameterTypes())) {
                    return false;
                }
                if (procProv.getExitType() == null && procReq.getExitType() == null) {
                    return true;
                }
                if (procProv.getExitType() == null || procReq.getExitType() == null) {
                    return false;
                }
                return matchType(procProv.getExitType(), procReq.getExitType());
            } else if (prov instanceof ProcessType && req instanceof ProcessType) {
                ProcessType procProv = (ProcessType)prov;
                ProcessType procReq = (ProcessType)req;

                if (!matchTypeList(procProv.getParameterTypes(), procReq.getParameterTypes())) {
                    return false;
                }
                if (procProv.getExitType() == null && procReq.getExitType() == null) {
                    return true;
                }
                if (procProv.getExitType() == null || procReq.getExitType() == null) {
                    return false;
                }
                return matchType(procProv.getExitType(), procReq.getExitType());
            } else if (prov instanceof TupleType && req instanceof TupleType) {
                List<TupleField> f1 = ((TupleType)prov).getFields();
                List<TupleField> f2 = ((TupleType)req).getFields();
                if (f1.size() != f2.size()) {
                    return false;
                }
                for (int i = 0; i < f1.size(); i++) {
                    if (!matchType(f1.get(i).getType(), f2.get(i).getType())) {
                        return false;
                    }
                }
                return true;
            } else if (prov instanceof MatrixType && req instanceof MatrixType) {
                MatrixType m1 = (MatrixType)prov;
                MatrixType m2 = (MatrixType)req;
                if (evalExpression(m1.getColumnSize(), null) != evalExpression(m2.getColumnSize(), null)) {
                    return false;
                }
                return evalExpression(m1.getRowSize(), null) == evalExpression(m2.getRowSize(), null);
            } else if (prov instanceof EnumTypeReference && req instanceof EnumTypeReference) {
                EnumTypeReference ert1 = (EnumTypeReference)prov;
                EnumTypeReference ert2 = (EnumTypeReference)req;
                return ert1.getType() == ert2.getType();
            } else {
                return false;
            }
        }
    }

    /**
     * Get the biggest common type for each pair of types in both lists.
     *
     * @param tl1 First list.
     * @param tl2 Second list.
     * @return The list of pair-wise common types of both lists, or {@code null} if no common type can be derived.
     */
    private static List<Type> smallestTypeList(List<Type> tl1, List<Type> tl2) {
        if (tl1.size() != tl2.size()) {
            return null;
        }

        List<Type> resList = listc(tl1.size());
        for (int i = 0; i < tl1.size(); i++) {
            Type t = smallestType(tl1.get(i), tl2.get(i));
            if (t == null) {
                return null;
            }
            resList.add(t);
        }
        return resList;
    }

    /**
     * Get the biggest common type.
     *
     * @param t1 First type to compare.
     * @param t2 Second type to compare.
     * @return The smallest type of the provided arguments, or {@code null} if there is no smallest type.
     */
    public static Type smallestType(Type t1, Type t2) {
        Assert.check(t1 != null);
        Assert.check(t2 != null);

        t1 = dropReferences(t1);
        t2 = dropReferences(t2);

        if (t1 instanceof VoidType && t2 instanceof VoidType) {
            return newVoidType(null);
        } else if (t1 instanceof FileType && t2 instanceof FileType) {
            return newFileType(null);
        } else if (t1 instanceof BoolType && t2 instanceof BoolType) {
            return newBoolType(null);
        } else if (t1 instanceof IntType && t2 instanceof IntType) {
            return newIntType(null);
        } else if (t1 instanceof TimerType && t2 instanceof TimerType) {
            return newTimerType(null);
        } else if (t1 instanceof StringType && t2 instanceof StringType) {
            return newStringType(null);
        } else if (t1 instanceof RealType && t2 instanceof RealType) {
            return newRealType(null);
        } else if (t1 instanceof InstanceType && t2 instanceof InstanceType) {
            return newInstanceType(null);
        } else if (t1 instanceof ListType && t2 instanceof ListType) {
            ListType x1 = (ListType)t1;
            ListType x2 = (ListType)t2;
            t1 = smallestType(x1.getElementType(), x2.getElementType());
            if (t1 == null) {
                return null;
            }
            return newListType(t1, null, null);
        } else if (t1 instanceof SetType && t2 instanceof SetType) {
            SetType x1 = (SetType)t1;
            SetType x2 = (SetType)t2;
            t1 = smallestType(x1.getElementType(), x2.getElementType());
            if (t1 == null) {
                return null;
            }
            return newSetType(t1, null);
        } else if (t1 instanceof DistributionType && t2 instanceof DistributionType) {
            DistributionType x1 = (DistributionType)t1;
            DistributionType x2 = (DistributionType)t2;
            t1 = smallestType(x1.getResultType(), x2.getResultType());
            if (t1 == null) {
                return null;
            }
            return newDistributionType(null, t1);
        } else if (t1 instanceof ChannelType && t2 instanceof ChannelType) {
            ChannelType ct1 = (ChannelType)t1;
            ChannelType ct2 = (ChannelType)t2;

            t1 = smallestType(ct1.getElementType(), ct2.getElementType());
            if (t1 == null) {
                return null;
            }

            ChannelOps op1 = ct1.getOps();
            ChannelOps op2 = ct2.getOps();

            if (op1 == ChannelOps.SEND_RECEIVE) {
                return newChannelType(t1, op2, null);
            } else if (op2 == ChannelOps.SEND_RECEIVE) {
                return newChannelType(t1, op1, null);
            } else if (op1.equals(op2)) {
                return newChannelType(t1, op1, null);
            }
            return null;
        } else if (t1 instanceof DictType && t2 instanceof DictType) {
            DictType d1 = (DictType)t1;
            DictType d2 = (DictType)t2;
            t1 = smallestType(d1.getKeyType(), d2.getKeyType());
            t2 = smallestType(d1.getValueType(), d2.getValueType());
            if (t1 == null || t2 == null) {
                return null;
            }
            return newDictType(t1, null, t2);
        } else if (t1 instanceof FunctionType && t2 instanceof FunctionType) {
            FunctionType f1 = (FunctionType)t1;
            FunctionType f2 = (FunctionType)t2;
            List<Type> pl = smallestTypeList(f1.getParameterTypes(), f2.getParameterTypes());
            t1 = smallestType(f1.getResultType(), f2.getResultType());
            if (pl == null || t1 == null) {
                return null;
            }
            return newFunctionType(pl, null, t1);
        } else if (t1 instanceof ProcessType && t2 instanceof ProcessType) {
            ProcessType pt1 = (ProcessType)t1;
            ProcessType pt2 = (ProcessType)t2;
            List<Type> pl = smallestTypeList(pt1.getParameterTypes(), pt2.getParameterTypes());
            if (pl == null) {
                return null;
            }
            if (pt1.getExitType() == null && pt2.getExitType() == null) {
                return newProcessType(null, pl, null);
            }
            if (pt1.getExitType() == null || pt2.getExitType() == null) {
                return null; // One is 'null' and one is not.
            }
            Type rtp = smallestType(pt1.getExitType(), pt2.getExitType());
            if (rtp == null) {
                return null;
            }
            return newProcessType(rtp, pl, null);
        } else if (t1 instanceof ModelType && t2 instanceof ModelType) {
            ModelType pt1 = (ModelType)t1;
            ModelType pt2 = (ModelType)t2;
            List<Type> pl = smallestTypeList(pt1.getParameterTypes(), pt2.getParameterTypes());
            if (pl == null) {
                return null;
            }
            if (pt1.getExitType() == null && pt2.getExitType() == null) {
                return newModelType(null, pl, null);
            }
            if (pt1.getExitType() == null || pt2.getExitType() == null) {
                return null; // One is 'null' and one is not.
            }
            Type rtp = smallestType(pt1.getExitType(), pt2.getExitType());
            if (rtp == null) {
                return null;
            }
            return newModelType(rtp, pl, null);
        } else if (t1 instanceof TupleType && t2 instanceof TupleType) {
            List<TupleField> f1 = ((TupleType)t1).getFields();
            List<TupleField> f2 = ((TupleType)t2).getFields();
            if (f1.size() != f2.size()) {
                return null;
            }

            List<TupleField> resFields = list();
            for (int i = 0; i < f1.size(); i++) {
                Type t = smallestType(f1.get(i).getType(), f2.get(i).getType());
                if (t == null) {
                    return null;
                }
                resFields.add(newTupleField(null, null, t));
            }
            return newTupleType(resFields, null);
        } else if (t1 instanceof MatrixType && t2 instanceof MatrixType) {
            MatrixType m1 = (MatrixType)t1;
            MatrixType m2 = (MatrixType)t2;

            int c1 = evalExpression(m1.getColumnSize(), null);
            int c2 = evalExpression(m2.getColumnSize(), null);
            if (c1 != c2) {
                return null;
            }

            int r1 = evalExpression(m1.getRowSize(), null);
            int r2 = evalExpression(m2.getRowSize(), null);
            if (r1 != r2) {
                return null;
            }

            Expression c, r;
            c = newIntNumber(null, newIntType(), Integer.toString(c1));
            r = newIntNumber(null, newIntType(), Integer.toString(r1));
            return newMatrixType(c, null, r);
        } else if (t1 instanceof EnumTypeReference && t2 instanceof EnumTypeReference) {
            EnumTypeReference ert1 = (EnumTypeReference)t1;
            EnumTypeReference ert2 = (EnumTypeReference)t2;
            if (ert1.getType() != ert2.getType()) {
                return null;
            }

            return newEnumTypeReference(null, ert1.getType());
        }
        // Types are structurally different.
        return null;
    }

    /**
     * Remove the {@link TypeReference} objects from the top of the type.
     *
     * @param t Type to clean up.
     * @return Cleaned type.
     */
    public static Type dropReferences(Type t) {
        for (;;) {
            Assert.check(t != null);
            if (t instanceof TypeReference) {
                t = ((TypeReference)t).getType().getType();
                continue;
            }
            return t;
        }
    }

    /**
     * Is the provided type a numeric data type?
     *
     * @param t Type to test.
     * @return Whether the type is numeric.
     */
    public static boolean isNumericType(Type t) {
        t = dropReferences(t);
        return t instanceof IntType || t instanceof RealType;
    }

    /**
     * Construct a fresh bool distribution type.
     *
     * @return A fresh bool distribution type.
     */
    public static Type newBoolDist() {
        return newDistributionType(null, newBoolType());
    }

    /**
     * Construct a fresh integer distribution type.
     *
     * @return A fresh integer distribution type.
     */
    public static Type newIntDist() {
        return newDistributionType(null, newIntType());
    }

    /**
     * Construct a fresh real distribution type.
     *
     * @return A fresh real distribution type.
     */
    public static Type newRealDist() {
        return newDistributionType(null, newRealType());
    }

    /**
     * Construct a list of types.
     *
     * @param elems Types to merge into a list.
     * @return The list of types.
     */
    public static List<Type> tlist(Type... elems) {
        List<Type> rslt = listc(elems.length);
        for (Type elem: elems) {
            rslt.add(elem);
        }
        return rslt;
    }

    /**
     * Construct an anonymous tuple from a list types.
     *
     * @param elems Types to merge into a tuple type.
     * @return The tuple type.
     */
    public static TupleType tuplet(Type... elems) {
        List<TupleField> rslt = listc(elems.length);
        for (Type elem: elems) {
            rslt.add(newTupleField("", null, elem));
        }
        return newTupleType(rslt, null);
    }

    /**
     * Public access point to checking a type. Note that 'void' is never returned.
     *
     * @param t Type to check.
     * @param ctxt Type check context.
     * @return Type-checked copy of the input type.
     */
    public static Type transNonvoidType(Type t, CheckContext ctxt) {
        ctxt = ctxt.add(ContextItem.NO_VOID);
        return transType(t, ctxt);
    }

    /**
     * Type-check a type.
     *
     * @param t Unchecked type.
     * @param ctxt Type check context.
     * @return Type-checked copy of the type.
     */
    public static Type transType(Type t, CheckContext ctxt) {
        if (t instanceof DictType) {
            return transDictType((DictType)t, ctxt);
        } else if (t instanceof VoidType) {
            return transVoidType((VoidType)t, ctxt);
        } else if (t instanceof FileType) {
            return transFileType((FileType)t);
        } else if (t instanceof SetType) {
            return transSetType((SetType)t, ctxt);
        } else if (t instanceof BoolType) {
            return transBoolType((BoolType)t);
        } else if (t instanceof DistributionType) {
            return transDistributionType((DistributionType)t, ctxt);
        } else if (t instanceof IntType) {
            return transIntType((IntType)t);
        } else if (t instanceof TimerType) {
            return transTimerType((TimerType)t);
        } else if (t instanceof StringType) {
            return transStringType((StringType)t);
        } else if (t instanceof RealType) {
            return transRealType((RealType)t);
        } else if (t instanceof InstanceType) {
            return transInstanceType((InstanceType)t);
        } else if (t instanceof ChannelType) {
            return transChannelType((ChannelType)t, ctxt);
        } else if (t instanceof UnresolvedType) {
            return transUnresolvedType((UnresolvedType)t, ctxt);
        } else if (t instanceof FunctionType) {
            return transFunctionType((FunctionType)t, ctxt);
        } else if (t instanceof ProcessType) {
            return transProcessType((ProcessType)t, ctxt);
        } else if (t instanceof ModelType) {
            return transModelType((ModelType)t, ctxt);
        } else if (t instanceof TupleType) {
            return transTupleType((TupleType)t, ctxt);
        } else if (t instanceof MatrixType) {
            return transMatrixType((MatrixType)t, ctxt);
        } else if (t instanceof ListType) {
            return transListType((ListType)t, ctxt);
        }

        // TypeReference never happens in an untyped model.
        Assert.fail("Unknown type encountered.");
        return null;
    }

    /**
     * Type check a dictionary type.
     *
     * @param t Dictionary type to check.
     * @param ctxt Type check context.
     * @return Type checked type.
     */
    private static Type transDictType(DictType t, CheckContext ctxt) {
        Type kt = transNonvoidType(t.getKeyType(), ctxt);

        boolean cond = !(dropReferences(kt) instanceof TimerType);
        ctxt.checkThrowError(cond, Message.DICT_OF_TIMERS, t.getPosition());

        Type vt = transNonvoidType(t.getValueType(), ctxt);
        return newDictType(kt, copyPosition(t), vt);
    }

    /**
     * Type check a void type.
     *
     * @param t Void type to check.
     * @param ctxt Type check context.
     * @return Type checked type.
     */
    private static Type transVoidType(VoidType t, CheckContext ctxt) {
        if (ctxt.contains(ContextItem.NO_VOID)) {
            ctxt.throwError(Message.VOID_NOT_ALLOWED, t.getPosition());
        }
        return newVoidType(copyPosition(t));
    }

    /**
     * Type check a file type.
     *
     * @param t File type to check.
     * @return Type checked type.
     */
    private static Type transFileType(FileType t) {
        return newFileType(copyPosition(t));
    }

    /**
     * Type check a set type.
     *
     * @param t Set type to check.
     * @param ctxt Type check context.
     * @return Type checked type.
     */
    private static Type transSetType(SetType t, CheckContext ctxt) {
        Type et = transNonvoidType(t.getElementType(), ctxt);

        boolean cond = !(dropReferences(et) instanceof TimerType);
        ctxt.checkThrowError(cond, Message.SET_OF_TIMERS, t.getPosition());

        return newSetType(et, copyPosition(t));
    }

    /**
     * Type check a boolean type.
     *
     * @param t Boolean type to check.
     * @return Type checked type.
     */
    private static Type transBoolType(BoolType t) {
        return newBoolType(copyPosition(t));
    }

    /**
     * Type check a distribution type.
     *
     * @param t Distribution type to check.
     * @param ctxt Type check context.
     * @return Type checked type.
     */
    private static Type transDistributionType(DistributionType t, CheckContext ctxt) {
        Type et = transNonvoidType(t.getResultType(), ctxt);
        Type et2 = dropReferences(et);
        boolean cond = (et2 instanceof BoolType || et2 instanceof IntType || et2 instanceof RealType);
        ctxt.checkThrowError(cond, Message.WRONG_DIST_TYPE, et.getPosition(), toString(et2));

        return newDistributionType(copyPosition(t), et);
    }

    /**
     * Type check an integer type.
     *
     * @param t Integer type to check.
     * @return Type checked type.
     */
    private static Type transIntType(IntType t) {
        return newIntType(copyPosition(t));
    }

    /**
     * Type check a timer type.
     *
     * @param t Timer type to check.
     * @return Type checked type.
     */
    private static Type transTimerType(TimerType t) {
        return newTimerType(copyPosition(t));
    }

    /**
     * Type check a string type.
     *
     * @param t String type to check.
     * @return Type checked type.
     */
    private static Type transStringType(StringType t) {
        return newStringType(copyPosition(t));
    }

    /**
     * Type check a real type.
     *
     * @param t Real type to check.
     * @return Type checked type.
     */
    private static Type transRealType(RealType t) {
        return newRealType(copyPosition(t));
    }

    /**
     * Type check a instance type.
     *
     * @param t Instance type to check.
     * @return Type checked type.
     */
    private static Type transInstanceType(InstanceType t) {
        return newInstanceType(copyPosition(t));
    }

    /**
     * Type check a channel type.
     *
     * @param t Channel type to check.
     * @param ctxt Type check context.
     * @return Type checked type.
     */
    private static Type transChannelType(ChannelType t, CheckContext ctxt) {
        ctxt = ctxt.remove(ContextItem.NO_VOID);
        Type elmType = transType(t.getElementType(), ctxt);
        return newChannelType(elmType, t.getOps(), copyPosition(t));
    }

    /**
     * Type check a type reference.
     *
     * @param t Type reference to check.
     * @param ctxt Type check context.
     * @return The resolved type reference.
     */
    private static Type transUnresolvedType(UnresolvedType t, CheckContext ctxt) {
        SymbolEntry se = ctxt.getSymbol(t.getName());
        if (se != null) {
            // A full check is requested, so below a reference to the
            // complete object can be returned.
            se.fullTypeCheck();
            se.setUsed();
        }

        if (se instanceof TypeSymbolEntry) {
            // Try to get the symbol as a type definition.
            TypeSymbolEntry tse = (TypeSymbolEntry)se;
            TypeDeclaration td = tse.getTypeDeclaration();
            if (td != null) {
                if (ctxt.contains(ContextItem.NO_VOID)) {
                    // The only way to introduce a void type here is a type
                    // definition like 'type x = void'.
                    Type tp = dropReferences(td.getType());
                    if (tp instanceof VoidType) {
                        ctxt.throwError(Message.VOID_NOT_ALLOWED, t.getPosition());
                    }
                }
                return newTypeReference(copyPosition(t), td);
            }
            // Try to get the symbol as an enumeration type.
            EnumDeclaration ed = tse.getEnumTypeDeclaration();
            if (ed != null) {
                return newEnumTypeReference(copyPosition(t), ed);
            }
        }
        ctxt.throwError(Message.NO_TYPE_NAME, t.getPosition(), t.getName());
        return null; // Never reached.
    }

    /**
     * Type check a process type.
     *
     * @param t Process type to check.
     * @param ctxt Type check context.
     * @return Type checked type.
     */
    private static Type transProcessType(ProcessType t, CheckContext ctxt) {
        List<Type> pt = list();
        for (Type tp: t.getParameterTypes()) {
            pt.add(transNonvoidType(tp, ctxt));
        }
        Type rType = t.getExitType();
        if (rType != null) {
            ctxt = ctxt.remove(ContextItem.NO_VOID);
            rType = transType(rType, ctxt);
        }
        return newProcessType(rType, pt, copyPosition(t));
    }

    /**
     * Type check a model type.
     *
     * @param t Model type to check.
     * @param ctxt Type check context.
     * @return Type checked type.
     */
    private static Type transModelType(ModelType t, CheckContext ctxt) {
        List<Type> pt = list();
        for (Type tp: t.getParameterTypes()) {
            pt.add(transNonvoidType(tp, ctxt));
        }
        Type rType = t.getExitType();
        if (rType != null) {
            ctxt = ctxt.remove(ContextItem.NO_VOID);
            rType = transType(rType, ctxt);
        }
        return newModelType(rType, pt, copyPosition(t));
    }

    /**
     * Type check a function type.
     *
     * @param t Function type to check.
     * @param ctxt Type check context.
     * @return Type checked type.
     */
    private static Type transFunctionType(FunctionType t, CheckContext ctxt) {
        List<Type> pt = list();
        for (Type tp: t.getParameterTypes()) {
            pt.add(transNonvoidType(tp, ctxt));
        }
        Type rt = transNonvoidType(t.getResultType(), ctxt);
        return newFunctionType(pt, copyPosition(t), rt);
    }

    /**
     * Type check a tuple type.
     *
     * @param t Tuple type to check.
     * @param ctxt Type check context.
     * @return Type checked type.
     */
    private static Type transTupleType(TupleType t, CheckContext ctxt) {
        // Tuple type should be at least 2 fields long.
        ctxt.checkThrowError(t.getFields().size() >= 2, Message.TUPLE_TYPE_WRONG_SIZE, t.getPosition());

        // Check uniqueness of names, and valid-ness of the type of the fields.
        Map<String, TupleField> names = map();
        List<TupleField> tf = list();
        for (TupleField fld: t.getFields()) {
            String name = fld.getName();
            if (name.isEmpty()) {
                continue;
            }

            if (names.containsKey(name)) {
                ctxt.addError(Message.DUPLICATE_FIELD, fld.getPosition(), name);
                ctxt.addError(Message.DUPLICATE_FIELD, names.get(name).getPosition(), name);
                throw new SemanticException();
            }
            names.put(name, fld);

            Type tt = transNonvoidType(fld.getType(), ctxt);
            tf.add(newTupleField(fld.getName(), copyPosition(fld), tt));
        }
        return newTupleType(tf, copyPosition(t));
    }

    /**
     * Type check a matrix type.
     *
     * @param t Matrix type to check.
     * @param ctxt Type check context.
     * @return Type checked type.
     */
    private static Type transMatrixType(MatrixType t, CheckContext ctxt) {
        CheckContext exprCtxt = ctxt.add(ContextItem.NO_SAMPLE, ContextItem.NO_REAL_TIMER_CAST, ContextItem.NO_TIME);
        // Check column expression.
        Expression cs = transExpression(t.getColumnSize(), exprCtxt);
        boolean cond = cs.getType() instanceof IntType;
        Position pos = cs.getPosition();
        ctxt.checkThrowError(cond, Message.COLUMN_EXPR_WRONG_TYPE, pos, toString(cs.getType()));

        int value = evalExpression(cs, ctxt);
        cond = evalExpression(cs, ctxt) > 0;
        ctxt.checkThrowError(value > 0, Message.COLUMN_EXPR_WRONG_VALUE, cs.getPosition(), String.valueOf(value));

        // Check row expression.
        Expression rs = transExpression(t.getRowSize(), exprCtxt);
        cond = rs.getType() instanceof IntType;
        pos = rs.getPosition();
        ctxt.checkThrowError(cond, Message.ROW_EXPR_WRONG_TYPE, pos, toString(rs.getType()));

        value = evalExpression(rs, ctxt);
        ctxt.checkThrowError(value > 0, Message.ROW_EXPR_WRONG_VALUE, rs.getPosition(), String.valueOf(value));

        return newMatrixType(cs, copyPosition(t), rs);
    }

    /**
     * Type check a list type.
     *
     * @param t List type to check.
     * @param ctxt Type check context.
     * @return Type checked type.
     */
    private static Type transListType(ListType t, CheckContext ctxt) {
        CheckContext exprCtxt = ctxt.add(ContextItem.NO_SAMPLE, ContextItem.NO_REAL_TIMER_CAST, ContextItem.NO_TIME);
        Type tp = transNonvoidType(t.getElementType(), ctxt);
        Expression length;
        if (t.getInitialLength() == null) {
            length = null;
        } else {
            length = transExpression(t.getInitialLength(), exprCtxt);
        }
        return newListType(tp, length, copyPosition(t));
    }

    /**
     * Can values of the given type be converted to text?
     *
     * @param tp Type of the value.
     * @return Whether the value can be usefully converted to text.
     */
    public static boolean isPrintable(Type tp) {
        tp = dropReferences(tp);

        if (tp instanceof DictType) {
            DictType dt = (DictType)tp;
            return isPrintable(dt.getKeyType()) && isPrintable(dt.getValueType());
        } else if (tp instanceof VoidType) {
            return false;
        } else if (tp instanceof FileType) {
            return false;
        } else if (tp instanceof SetType) {
            SetType st = (SetType)tp;
            return isPrintable(st.getElementType());
        } else if (tp instanceof BoolType) {
            return true;
        } else if (tp instanceof DistributionType) {
            return false;
        } else if (tp instanceof IntType) {
            return true;
        } else if (tp instanceof TimerType) {
            return false;
        } else if (tp instanceof StringType) {
            return true;
        } else if (tp instanceof RealType) {
            return true;
        } else if (tp instanceof InstanceType) {
            return false;
        } else if (tp instanceof ChannelType) {
            return false;
        } else if (tp instanceof FunctionType) {
            return false;
        } else if (tp instanceof ProcessType) {
            return false;
        } else if (tp instanceof ModelType) {
            return false;
        } else if (tp instanceof TupleType) {
            TupleType pp = (TupleType)tp;
            for (TupleField tf: pp.getFields()) {
                if (!isPrintable(tf.getType())) {
                    return false;
                }
            }
            return true;
        } else if (tp instanceof MatrixType) {
            return true;
        } else if (tp instanceof ListType) {
            ListType lt = (ListType)tp;
            return isPrintable(lt.getElementType());
        } else if (tp instanceof EnumTypeReference) {
            return true;
        }

        Assert.fail("Unexpected type " + tp.toString() + " in isPrintable");
        return false;
    }

    /**
     * Construct a string representation of a type.
     *
     * @param tp Type to convert.
     * @return String representation of the provided type.
     */
    public static String toString(Type tp) {
        tp = dropReferences(tp);

        if (tp instanceof DictType) {
            DictType dt = (DictType)tp;
            return "dict(" + toString(dt.getKeyType()) + " : " + toString(dt.getValueType()) + ")";
        } else if (tp instanceof VoidType) {
            return "void";
        } else if (tp instanceof FileType) {
            return "file";
        } else if (tp instanceof SetType) {
            SetType st = (SetType)tp;
            return "set " + toString(st.getElementType());
        } else if (tp instanceof BoolType) {
            return "bool";
        } else if (tp instanceof DistributionType) {
            DistributionType dt = (DistributionType)tp;
            return "dist " + toString(dt.getResultType());
        } else if (tp instanceof IntType) {
            return "int";
        } else if (tp instanceof TimerType) {
            return "timer";
        } else if (tp instanceof StringType) {
            return "string";
        } else if (tp instanceof RealType) {
            return "real";
        } else if (tp instanceof InstanceType) {
            return "inst";
        } else if (tp instanceof ChannelType) {
            ChannelType ct = (ChannelType)tp;
            switch (ct.getOps()) {
                case RECEIVE:
                    return "chan? " + toString(ct.getElementType());

                case SEND:
                    return "chan! " + toString(ct.getElementType());

                case SEND_RECEIVE:
                    return "chan!? " + toString(ct.getElementType());

                default:
                    Assert.fail("Unexpected channel direction found");
                    return null;
            }
        } else if (tp instanceof FunctionType) {
            FunctionType ft = (FunctionType)tp;
            String str = "";
            for (Type at: ft.getParameterTypes()) {
                str = str.isEmpty() ? "(" : str + ", ";
                str += toString(at);
            }
            return "func " + toString(ft.getResultType()) + " " + str + ")";
        } else if (tp instanceof ProcessType) {
            ProcessType pt = (ProcessType)tp;
            String str = "(";
            if (pt.getExitType() != null) {
                str = toString(pt.getExitType()) + " (";
            }
            boolean first = true;
            for (Type at: pt.getParameterTypes()) {
                if (!first) {
                    str += ", ";
                }
                first = false;
                str += toString(at);
            }
            return "proc " + str + ")";
        } else if (tp instanceof ModelType) {
            ModelType pt = (ModelType)tp;
            String str = "(";
            if (pt.getExitType() != null) {
                str = toString(pt.getExitType()) + " (";
            }
            boolean first = true;
            for (Type at: pt.getParameterTypes()) {
                if (!first) {
                    str += ", ";
                }
                first = false;
                str += toString(at);
            }
            return "model " + str + ")";
        } else if (tp instanceof TupleType) {
            String str = "";
            TupleType pp = (TupleType)tp;
            for (TupleField tf: pp.getFields()) {
                str = str.isEmpty() ? "tuple(" : str + ", ";
                str += toString(tf.getType());
            }
            return str + ")";
        } else if (tp instanceof MatrixType) {
            MatrixType mt = (MatrixType)tp;
            return "matrix(" + String.valueOf(evalExpression(mt.getRowSize(), null)) + ", "
                    + String.valueOf(evalExpression(mt.getColumnSize(), null)) + ")";
        } else if (tp instanceof ListType) {
            ListType lt = (ListType)tp;
            return "list " + toString(lt.getElementType());
        }

        if (tp instanceof EnumTypeReference) {
            EnumTypeReference etr = (EnumTypeReference)tp;
            return "enum " + etr.getType().getName();
        }

        Assert.fail("Unexpected type " + tp.toString());
        return null; // Never reached
    }
}
