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

package org.eclipse.escet.tooldef.typechecker;

import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.tooldef.common.ToolDefTextUtils.typeToStr;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.isSubType;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.isSuperType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newBoolType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newDoubleType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newIntType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newListType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newLongType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newMapExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newMapType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newObjectType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newSetExpression;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newSetType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newStringType;
import static org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors.newTupleType;
import static org.eclipse.escet.tooldef.typechecker.TypesChecker.NON_NULLABLE_INT_HINT;
import static org.eclipse.escet.tooldef.typechecker.TypesChecker.NON_NULLABLE_INT_TYPE;

import java.util.List;

import org.eclipse.escet.common.emf.EMFHelper;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Numbers;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.tooldef.common.ToolDefTypeUtils;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.BoolExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.CastExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.DoubleExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.EmptySetMapExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ListExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapEntry;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.MapExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NullExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.NumberExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SetExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.SliceExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.StringExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolParamExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.TupleExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.UnresolvedRefExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.VariableExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.BoolType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.DoubleType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.IntType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.LongType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.SetType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.StringType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TupleType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeParamRef;

/** Type checker for ToolDef expressions. */
public class ExprsChecker {
    /** Constructor for the {@link ExprsChecker} class. */
    private ExprsChecker() {
        // Static class.
    }

    /**
     * Type check a ToolDef expression.
     *
     * <p>
     * May replace the expression in its parent.
     * </p>
     *
     * @param expr The ToolDef expression.
     * @param ctxt The type checker context.
     * @param hints The type hints of the environment of the expression, i.e. the expected or allowed types of the
     *     expression. Ignored if they don't fit.
     */
    public static void tcheck(Expression expr, CheckerContext ctxt, TypeHints hints) {
        if (expr instanceof BoolExpression) {
            tcheck((BoolExpression)expr);
        } else if (expr instanceof CastExpression) {
            tcheck((CastExpression)expr, ctxt);
        } else if (expr instanceof DoubleExpression) {
            tcheck((DoubleExpression)expr, ctxt);
        } else if (expr instanceof EmptySetMapExpression) {
            tcheck((EmptySetMapExpression)expr, ctxt, hints);
        } else if (expr instanceof ListExpression) {
            tcheck((ListExpression)expr, ctxt, hints);
        } else if (expr instanceof MapExpression) {
            tcheck((MapExpression)expr, ctxt, hints);
        } else if (expr instanceof NullExpression) {
            tcheck((NullExpression)expr, hints);
        } else if (expr instanceof NumberExpression) {
            tcheck((NumberExpression)expr, ctxt);
        } else if (expr instanceof ProjectionExpression) {
            tcheck((ProjectionExpression)expr, ctxt);
        } else if (expr instanceof SetExpression) {
            tcheck((SetExpression)expr, ctxt, hints);
        } else if (expr instanceof SliceExpression) {
            tcheck((SliceExpression)expr, ctxt, hints);
        } else if (expr instanceof StringExpression) {
            tcheck((StringExpression)expr);
        } else if (expr instanceof ToolInvokeExpression) {
            tcheck((ToolInvokeExpression)expr, ctxt, hints);
        } else if (expr instanceof ToolParamExpression) {
            // Should not be present in AST.
            throw new RuntimeException("Already resolved ref? " + expr);
        } else if (expr instanceof TupleExpression) {
            tcheck((TupleExpression)expr, ctxt, hints);
        } else if (expr instanceof UnresolvedRefExpression) {
            tcheck((UnresolvedRefExpression)expr, ctxt);
        } else if (expr instanceof VariableExpression) {
            // Should not be present in AST.
            throw new RuntimeException("Already resolved ref? " + expr);
        } else {
            throw new RuntimeException("Unknown expr: " + expr);
        }
    }

    /**
     * Type check a boolean expression.
     *
     * @param expr The expression.
     */
    private static void tcheck(BoolExpression expr) {
        // No types smaller than 'bool' that fit, so no need to check hints.
        expr.setType(newBoolType(false, null));
    }

    /**
     * Type check a cast expression.
     *
     * @param expr The expression.
     * @param ctxt The type checker context.
     */
    private static void tcheck(CastExpression expr, CheckerContext ctxt) {
        // Type check target type.
        TypesChecker.tcheck(expr.getType(), ctxt);
        ToolDefType targetType = expr.getType();
        targetType = ToolDefTypeUtils.normalizeType(targetType);

        // Type check child, using target type as hint. Ignore environment
        // hints.
        TypeHints childHints = new TypeHints();
        childHints.add(targetType);
        tcheck(expr.getChild(), ctxt, childHints);

        // Check child type against target type.
        ToolDefType childType = expr.getChild().getType();
        childType = ToolDefTypeUtils.normalizeType(childType);
        if (isSuperType(targetType, childType)) {
            // Allow widening to super type (targetType >= childType, will
            // always succeed). Includes 't' to 't?'.
        } else if (isSubType(targetType, childType)) {
            // Allow narrowing to sub type (targetType < childType due to equal
            // types being handled by previous case, might succeed). Includes
            // 't?' to 't'.
        } else if (!childType.isNullable() && !targetType.isNullable() && childType instanceof StringType
                && (targetType instanceof BoolType || targetType instanceof IntType || targetType instanceof LongType
                        || targetType instanceof DoubleType))
        {
            // Allow string to bool/int/long/double casting.
        } else {
            // Not allowed.
            ctxt.addProblem(Message.INVALID_CAST, expr.getPosition(), typeToStr(childType), typeToStr(targetType));
            throw new SemanticException();
        }
    }

    /**
     * Type check a double expression.
     *
     * @param expr The expression.
     * @param ctxt The type checker context.
     */
    private static void tcheck(DoubleExpression expr, CheckerContext ctxt) {
        // No types smaller than 'double' that fit, so no need to check hints.
        expr.setType(newDoubleType(false, null));

        // Check for overflow.
        double value;
        try {
            value = Double.parseDouble(expr.getValue());
        } catch (NumberFormatException e) {
            // Should never happen for results from the ToolDef scanner.
            throw new RuntimeException(e);
        }

        if (Double.isInfinite(value)) {
            ctxt.addProblem(Message.VALUE_OVERFLOW, expr.getPosition(), "Real", expr.getValue());
            throw new SemanticException();
        }

        Assert.check(!Double.isNaN(value));
        Assert.check(value >= 0.0);
    }

    /**
     * Type check an empty set/map expression.
     *
     * @param expr The expression.
     * @param ctxt The type checker context.
     * @param hints The type hints of the environment of the expression, i.e. the expected or allowed types of the
     *     expression. Ignored if they don't fit.
     */
    private static void tcheck(EmptySetMapExpression expr, CheckerContext ctxt, TypeHints hints) {
        // Try to derive expr/type from type hints. Use first match.
        for (ToolDefType hint: hints) {
            if (hint instanceof SetType) {
                SetType type = deepclone((SetType)hint);
                type.setNullable(false);
                Expression rslt = newSetExpression(null, expr.getPosition(), type);
                EMFHelper.updateParentContainment(expr, rslt);
                return;
            } else if (hint instanceof MapType) {
                MapType type = deepclone((MapType)hint);
                type.setNullable(false);
                Expression rslt = newMapExpression(null, expr.getPosition(), type);
                EMFHelper.updateParentContainment(expr, rslt);
                return;
            }
        }

        // No match in hints. Not enough information.
        ctxt.addProblem(Message.EXPR_UNKNOWN_TYPE, expr.getPosition(), "{}");
        throw new SemanticException();
    }

    /**
     * Type check a list expression.
     *
     * @param expr The expression.
     * @param ctxt The type checker context.
     * @param hints The type hints of the environment of the expression, i.e. the expected or allowed types of the
     *     expression. Ignored if they don't fit.
     */
    private static void tcheck(ListExpression expr, CheckerContext ctxt, TypeHints hints) {
        // Get type hints for elements.
        TypeHints elemHints = new TypeHints();
        for (ToolDefType hint: hints) {
            if (hint instanceof ListType) {
                elemHints.add(((ListType)hint).getElemType());
            }
        }

        // Type check the elements.
        int size = expr.getElements().size();
        for (int i = 0; i < size; i++) {
            Expression elem = expr.getElements().get(i);
            tcheck(elem, ctxt, elemHints);
        }

        // Get element type.
        ToolDefType elemType = null;
        boolean unique = false;
        if (size == 0) {
            // Use first element type hint, if available.
            for (ToolDefType elemHint: elemHints) {
                elemType = elemHint;
                break;
            }

            if (elemType == null) {
                ctxt.addProblem(Message.EXPR_UNKNOWN_TYPE, expr.getPosition(), "[]");
                throw new SemanticException();
            }
        } else {
            for (Expression elem: expr.getElements()) {
                if (elemType == null) {
                    elemType = elem.getType();
                } else {
                    elemType = ToolDefTypeUtils.mergeTypes(elemType, elem.getType());
                    unique = true;
                }
            }
        }

        // Create and set the list type.
        if (!unique) {
            elemType = deepclone(elemType);
        }
        expr.setType(newListType(elemType, false, null));
    }

    /**
     * Type check a non-empty map expression.
     *
     * @param expr The expression.
     * @param ctxt The type checker context.
     * @param hints The type hints of the environment of the expression, i.e. the expected or allowed types of the
     *     expression. Ignored if they don't fit.
     */
    private static void tcheck(MapExpression expr, CheckerContext ctxt, TypeHints hints) {
        // Get type hints for keys/values.
        TypeHints keyHints = new TypeHints();
        TypeHints valueHints = new TypeHints();
        for (ToolDefType hint: hints) {
            if (hint instanceof MapType) {
                keyHints.add(((MapType)hint).getKeyType());
                valueHints.add(((MapType)hint).getValueType());
            }
        }

        // Type check the entries.
        for (MapEntry entry: expr.getEntries()) {
            tcheck(entry.getKey(), ctxt, keyHints);
            tcheck(entry.getValue(), ctxt, valueHints);
        }

        // Get key/value types.
        ToolDefType keyType = null;
        ToolDefType valueType = null;
        boolean unique = false;
        for (MapEntry entry: expr.getEntries()) {
            if (keyType == null) {
                keyType = entry.getKey().getType();
                valueType = entry.getValue().getType();
            } else {
                keyType = ToolDefTypeUtils.mergeTypes(keyType, entry.getKey().getType());
                valueType = ToolDefTypeUtils.mergeTypes(valueType, entry.getValue().getType());
                unique = true;
            }
        }

        // Create and set the map type.
        if (!unique) {
            keyType = deepclone(keyType);
        }
        if (!unique) {
            valueType = deepclone(valueType);
        }
        expr.setType(newMapType(keyType, false, null, valueType));
    }

    /**
     * Type check a null expression.
     *
     * @param expr The expression.
     * @param hints The type hints of the environment of the expression, i.e. the expected or allowed types of the
     *     expression. Ignored if they don't fit.
     */
    private static void tcheck(NullExpression expr, TypeHints hints) {
        // Use most narrow nullable hint. We only use definitely nullable
        // types here.
        for (ToolDefType hint: hints) {
            if (hint.isNullable()) {
                expr.setType(deepclone(hint));
                return;
            }
        }

        // Use most narrow hint, and make it nullable.
        for (ToolDefType hint: hints) {
            // Skip type parameter references, as such references can't be
            // made nullable. Whether they are nullable depends entirely on
            // the type that is given for the type parameter.
            if (hint instanceof TypeParamRef) {
                continue;
            }

            // Make a nullable copy.
            ToolDefType t = deepclone(hint);
            t.setNullable(true);
            expr.setType(t);
            return;
        }

        // No hint. Use nullable object type.
        expr.setType(newObjectType(true, null));
    }

    /**
     * Type check a number expression.
     *
     * @param expr The expression.
     * @param ctxt The type checker context.
     */
    private static void tcheck(NumberExpression expr, CheckerContext ctxt) {
        // Try 'int', the most narrow type that fits.
        try {
            Integer.parseInt(expr.getValue());
            expr.setType(newIntType(false, null));
            return;
        } catch (NumberFormatException e) {
            // Ignore overflow.
        }

        // Try 'long', the second most narrow type that fits.
        try {
            Long.parseLong(expr.getValue());
            expr.setType(newLongType(false, null));
            return;
        } catch (NumberFormatException e) {
            // Ignore overflow.
        }

        // Overflow.
        ctxt.addProblem(Message.VALUE_OVERFLOW, expr.getPosition(), "Integer/long",
                Numbers.formatNumber(expr.getValue()));
        throw new SemanticException();
    }

    /**
     * Type check a projection expression.
     *
     * @param expr The expression.
     * @param ctxt The type checker context.
     */
    private static void tcheck(ProjectionExpression expr, CheckerContext ctxt) {
        // Type check the child, without hint.
        tcheck(expr.getChild(), ctxt, TypeHints.NO_HINTS);
        ToolDefType childType = expr.getChild().getType();
        childType = ToolDefTypeUtils.normalizeType(childType);

        // Check child type.
        if (childType.isNullable() || (!(childType instanceof ListType) && !(childType instanceof MapType)
                && !(childType instanceof StringType) && !(childType instanceof TupleType)))
        {
            ctxt.addProblem(Message.PROJ_CHILD_TYPE, expr.getPosition(), typeToStr(childType));
            throw new SemanticException();
        }

        // Get type hints for the index.
        TypeHints indexHints = new TypeHints();
        if (childType instanceof ListType) {
            indexHints.add(NON_NULLABLE_INT_TYPE);
        } else if (childType instanceof MapType) {
            indexHints.add(((MapType)childType).getKeyType());
        } else if (childType instanceof StringType) {
            indexHints.add(NON_NULLABLE_INT_TYPE);
        } else if (childType instanceof TupleType) {
            indexHints.add(NON_NULLABLE_INT_TYPE);
        } else {
            throw new RuntimeException("Unexpected child type: " + childType);
        }

        // Type check the index.
        tcheck(expr.getIndex(), ctxt, indexHints);
        Expression index = expr.getIndex();
        ToolDefType indexType = index.getType();
        indexType = ToolDefTypeUtils.normalizeType(indexType);

        // Check index type, and get result type.
        ToolDefType resultType;
        if (childType instanceof ListType) {
            if (!(indexType instanceof IntType) || indexType.isNullable()) {
                ctxt.addProblem(Message.PROJ_INDEX_TYPE, expr.getPosition(), typeToStr(childType), typeToStr(indexType),
                        "int");
                throw new SemanticException();
            }

            resultType = deepclone(((ListType)childType).getElemType());
        } else if (childType instanceof MapType) {
            ToolDefType keyType = ((MapType)childType).getKeyType();
            if (!isSubType(indexType, keyType)) {
                ctxt.addProblem(Message.PROJ_INDEX_TYPE, expr.getPosition(), typeToStr(childType), typeToStr(indexType),
                        typeToStr(keyType));
                throw new SemanticException();
            }

            resultType = deepclone(((MapType)childType).getValueType());
        } else if (childType instanceof StringType) {
            if (!(indexType instanceof IntType) || indexType.isNullable()) {
                ctxt.addProblem(Message.PROJ_INDEX_TYPE, expr.getPosition(), typeToStr(childType), typeToStr(indexType),
                        "int");
                throw new SemanticException();
            }

            resultType = deepclone(childType);
        } else if (childType instanceof TupleType) {
            TupleType ttype = (TupleType)childType;

            // Check index type.
            if (!(indexType instanceof IntType) || indexType.isNullable()) {
                ctxt.addProblem(Message.PROJ_INDEX_TYPE, expr.getPosition(), typeToStr(childType), typeToStr(indexType),
                        "int");
                throw new SemanticException();
            }

            // Check integer literal.
            if (!(index instanceof NumberExpression)) {
                ctxt.addProblem(Message.PROJ_TUPLE_NON_LIT, expr.getPosition());
                throw new SemanticException();
            }

            // Evaluate integer literal. It has already been type checked, so
            // the parsing/eval can't fail.
            int idx = Integer.parseInt(((NumberExpression)index).getValue());

            // Check index value against field range.
            if (idx < 0 || idx >= ttype.getFields().size()) {
                ctxt.addProblem(Message.PROJ_TUPLE_BOUND, expr.getPosition(), typeToStr(childType),
                        Integer.toString(idx));
                throw new SemanticException();
            }

            // Get result type.
            resultType = deepclone(ttype.getFields().get(idx));
        } else {
            throw new RuntimeException("Unexpected child type: " + childType);
        }

        // Set the type of the projection expression.
        expr.setType(resultType);
    }

    /**
     * Type check a non-empty set expression.
     *
     * @param expr The expression.
     * @param ctxt The type checker context.
     * @param hints The type hints of the environment of the expression, i.e. the expected or allowed types of the
     *     expression. Ignored if they don't fit.
     */
    private static void tcheck(SetExpression expr, CheckerContext ctxt, TypeHints hints) {
        Assert.check(!expr.getElements().isEmpty());

        // Get type hints for elements.
        TypeHints elemHints = new TypeHints();
        for (ToolDefType hint: hints) {
            if (hint instanceof SetType) {
                elemHints.add(((SetType)hint).getElemType());
            }
        }

        // Type check the elements.
        int size = expr.getElements().size();
        for (int i = 0; i < size; i++) {
            Expression elem = expr.getElements().get(i);
            tcheck(elem, ctxt, elemHints);
        }

        // Get element type.
        ToolDefType elemType = null;
        boolean unique = false;
        for (Expression elem: expr.getElements()) {
            if (elemType == null) {
                elemType = elem.getType();
            } else {
                elemType = ToolDefTypeUtils.mergeTypes(elemType, elem.getType());
                unique = true;
            }
        }

        // Create and set the set type.
        if (!unique) {
            elemType = deepclone(elemType);
        }
        expr.setType(newSetType(elemType, false, null));
    }

    /**
     * Type check a slice expression.
     *
     * @param expr The expression.
     * @param ctxt The type checker context.
     * @param hints The type hints of the environment of the expression, i.e. the expected or allowed types of the
     *     expression. Ignored if they don't fit.
     */
    private static void tcheck(SliceExpression expr, CheckerContext ctxt, TypeHints hints) {
        // Type check child. The result type is the same as the child type, so
        // pass along the hints of the environment.
        tcheck(expr.getChild(), ctxt, hints);
        ToolDefType childType = expr.getChild().getType();
        childType = ToolDefTypeUtils.normalizeType(childType);

        // Check child type.
        if (childType.isNullable() || (!(childType instanceof ListType) && !(childType instanceof StringType))) {
            ctxt.addProblem(Message.SLICE_CHILD_TYPE, expr.getPosition(), typeToStr(childType));
            throw new SemanticException();
        }

        // Type check begin.
        if (expr.getBegin() != null) {
            tcheck(expr.getBegin(), ctxt, NON_NULLABLE_INT_HINT);
            ToolDefType beginType = expr.getBegin().getType();
            beginType = ToolDefTypeUtils.normalizeType(beginType);

            if (!(beginType instanceof IntType) || beginType.isNullable()) {
                ctxt.addProblem(Message.SLICE_IDX_NON_INT, expr.getBegin().getPosition(), "begin",
                        typeToStr(beginType));
                throw new SemanticException();
            }
        }

        // Type check end.
        if (expr.getEnd() != null) {
            tcheck(expr.getEnd(), ctxt, NON_NULLABLE_INT_HINT);
            ToolDefType endType = expr.getEnd().getType();
            endType = ToolDefTypeUtils.normalizeType(endType);

            if (!(endType instanceof IntType) || endType.isNullable()) {
                ctxt.addProblem(Message.SLICE_IDX_NON_INT, expr.getEnd().getPosition(), "end", typeToStr(endType));
                throw new SemanticException();
            }
        }

        // Set type of the slice expression.
        expr.setType(deepclone(childType));
    }

    /**
     * Type check a string expression.
     *
     * @param expr The expression.
     */
    private static void tcheck(StringExpression expr) {
        // No types smaller than 'string', so no need to check hints.
        expr.setType(newStringType(false, null));
    }

    /**
     * Type check a tool invocation expression.
     *
     * @param expr The expression.
     * @param ctxt The type checker context.
     * @param hints The type hints of the environment of the expression, i.e. the expected or allowed types of the
     *     expression. Ignored if they don't fit.
     */
    private static void tcheck(ToolInvokeExpression expr, CheckerContext ctxt, TypeHints hints) {
        ToolInvokeChecker.tcheck(expr, ctxt, hints, true);
    }

    /**
     * Type check a tuple expression.
     *
     * @param expr The expression.
     * @param ctxt The type checker context.
     * @param hints The type hints of the environment of the expression, i.e. the expected or allowed types of the
     *     expression. Ignored if they don't fit.
     */
    private static void tcheck(TupleExpression expr, CheckerContext ctxt, TypeHints hints) {
        // Get type hints for elements.
        int size = expr.getElements().size();
        TypeHints[] elemHints = new TypeHints[size];
        for (int i = 0; i < size; i++) {
            elemHints[i] = new TypeHints();
        }
        for (ToolDefType hint: hints) {
            if (hint instanceof TupleType) {
                List<ToolDefType> fields = ((TupleType)hint).getFields();
                int cnt = Math.min(elemHints.length, fields.size());
                for (int i = 0; i < cnt; i++) {
                    elemHints[i].add(fields.get(i));
                }
            }
        }

        // Type check the elements.
        for (int i = 0; i < size; i++) {
            Expression elem = expr.getElements().get(i);
            tcheck(elem, ctxt, elemHints[i]);
        }

        // Get element types.
        List<ToolDefType> elemTypes = listc(size);
        for (Expression elem: expr.getElements()) {
            elemTypes.add(deepclone(elem.getType()));
        }

        // Create and set the tuple type.
        expr.setType(newTupleType(elemTypes, false, null));
    }

    /**
     * Type check an unresolved reference expression.
     *
     * @param expr The expression.
     * @param ctxt The type checker context.
     */
    private static void tcheck(UnresolvedRefExpression expr, CheckerContext ctxt) {
        // Resolve and replace. Type hints ignored.
        Expression resolved = ctxt.resolveValue(expr.getName(), expr.getPosition());
        EMFHelper.updateParentContainment(expr, resolved);
    }
}
