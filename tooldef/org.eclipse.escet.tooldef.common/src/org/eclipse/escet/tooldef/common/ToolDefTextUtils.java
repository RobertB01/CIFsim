//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.tooldef.common;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.tooldef.metamodel.tooldef.JavaTool;
import org.eclipse.escet.tooldef.metamodel.tooldef.Script;
import org.eclipse.escet.tooldef.metamodel.tooldef.Tool;
import org.eclipse.escet.tooldef.metamodel.tooldef.ToolParameter;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.TypeParam;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.BoolExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.CastExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.DoubleExpression;
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
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolArgument;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolParamExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolRef;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.TupleExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.VariableExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.BoolType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.DoubleType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.IntType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.LongType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ObjectType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.SetType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.StringType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TupleType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeParamRef;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TypeRef;

/** ToolDef textual formatting utility methods. */
public class ToolDefTextUtils {
    /** Constructor for the {@link ToolDefTextUtils} class. */
    private ToolDefTextUtils() {
        // Static class.
    }

    /**
     * Is the given ToolDef object a named object. Named objects include objects with a 'name' feature and scoped
     * statements.
     *
     * @param obj The ToolDef object.
     * @param allowImplicit Whether to allow implicitly named objects.
     * @return {@code true} if it is a named object, {@code false} otherwise.
     */
    public static boolean isNamedObj(PositionObject obj, boolean allowImplicit) {
        // Objects with an optional name.
        if (obj instanceof Script) {
            return ((Script)obj).getName() != null;
        }

        // Objects with a mandatory name.
        if (obj instanceof Tool) {
            return true;
        } else if (obj instanceof ToolParameter) {
            return true;
        } else if (obj instanceof TypeDecl) {
            return true;
        } else if (obj instanceof TypeParam) {
            return true;
        } else if (obj instanceof Variable) {
            return true;
        }

        // Scoped statements.
        if (obj instanceof Script) {
            return allowImplicit;
        } else if (obj instanceof ForStatement) {
            return allowImplicit;
        } else if (obj instanceof IfStatement) {
            return allowImplicit;
        } else if (obj instanceof WhileStatement) {
            return allowImplicit;
        }

        // Others.
        return false;
    }

    /**
     * Returns the name of a ToolDef declaration.
     *
     * @param obj The ToolDef declaration. Must be a named object.
     * @return The name.
     */
    public static String getName(PositionObject obj) {
        // Objects with an optional name.
        if (obj instanceof Script) {
            String name = ((Script)obj).getName();
            Assert.notNull(name);
            return name;
        }

        // Objects with a mandatory name.
        if (obj instanceof Tool) {
            return ((Tool)obj).getName();
        } else if (obj instanceof ToolParameter) {
            return ((ToolParameter)obj).getName();
        } else if (obj instanceof TypeDecl) {
            return ((TypeDecl)obj).getName();
        } else if (obj instanceof TypeParam) {
            return ((TypeParam)obj).getName();
        } else if (obj instanceof Variable) {
            return ((Variable)obj).getName();
        }

        // Scoped statements.
        if (obj instanceof Script) {
            return "(script)";
        } else if (obj instanceof ForStatement) {
            return "(for)";
        } else if (obj instanceof IfStatement) {
            return "(if)";
        } else if (obj instanceof WhileStatement) {
            return "(while)";
        }

        // Others.
        throw new RuntimeException("Unexpected obj: " + obj);
    }

    /**
     * Returns the absolute name of a ToolDef declaration.
     *
     * @param obj The ToolDef declaration. Must be a named object.
     * @return The absolute name.
     */
    public static String getAbsName(PositionObject obj) {
        String name = getName(obj);
        PositionObject ancestor = (PositionObject)obj.eContainer();

        while (true) {
            // Script.
            if (ancestor instanceof Script) {
                // Root script, not imported script.
                if (((Script)ancestor).getName() == null) {
                    break;
                }
            }

            // Scopes.
            if (isNamedObj(ancestor, true)) {
                String ancestorName = getName(ancestor);
                name = ancestorName + "." + name;
            }

            // Move on to next level.
            ancestor = (PositionObject)ancestor.eContainer();
        }

        return name;
    }

    /**
     * Returns a text describing the ToolDef declaration. It includes an indication of what kind of declaration it is,
     * and the name of the declaration. For the top level script, {@code "the top level scope of the script"} is
     * returned.
     *
     * @param obj The ToolDef declaration. Must be a named object.
     * @return The description, starting with lower case text.
     */
    public static String getDescr(PositionObject obj) {
        // Explicitly named.
        if (isNamedObj(obj, false)) {
            return fmt("%s \"%s\"", getKindText(obj), getName(obj));
        }

        // Implicitly named.
        if (obj.eContainer() == null) {
            Assert.check(obj instanceof Script);
            Assert.check(((Script)obj).getName() == null);
            return "the top level scope of the script";
        }
        return getKindText(obj);
    }

    /**
     * Returns a text describing the ToolDef declaration. It includes an indication of what kind of declaration it is,
     * and the absolute name of the declaration. For the top level script, {@code "the top level scope of the script"}
     * is returned.
     *
     * @param obj The ToolDef declaration. Must be a named object.
     * @return The description, starting with lower case text.
     */
    public static String getAbsDescr(PositionObject obj) {
        // Explicitly named.
        if (isNamedObj(obj, false)) {
            return fmt("%s \"%s\"", getKindText(obj), getAbsName(obj));
        }

        // Implicitly named.
        if (obj.eContainer() == null) {
            Assert.check(obj instanceof Script);
            Assert.check(((Script)obj).getName() == null);
            return "the top level scope of the script";
        }
        return getKindText(obj);
    }

    /**
     * Returns a text describing the tool reference. It includes an indication of what kind of tool it is, and the
     * absolute name of the tool.
     *
     * @param toolRef The tool reference.
     * @return The description, starting with lower or upper case text.
     */
    public static String getAbsDescr(ToolRef toolRef) {
        // Java method.
        Tool tool = toolRef.getTool();
        if (tool instanceof JavaTool) {
            return fmt("Java method \"%s\"", ((JavaTool)tool).getMethodName());
        }

        // ToolDef tool.
        return fmt("%s%s \"%s\"", toolRef.isBuiltin() ? "built-in " : "", getKindText(tool), getAbsName(tool));
    }

    /**
     * Returns a textual description of the kind of ToolDef declaration that is given.
     *
     * @param obj The ToolDef declaration for which to return the textual description. Must be a named object.
     * @return The textual description, in lower case.
     */
    public static String getKindText(PositionObject obj) {
        // Objects with an optional name.
        if (obj instanceof Script) {
            if (((Script)obj).getName() == null) {
                return "script";
            } else {
                return "imported script";
            }
        }

        // Objects with a mandatory name.
        if (obj instanceof Tool) {
            return "tool";
        } else if (obj instanceof ToolParameter) {
            return "tool parameter";
        } else if (obj instanceof TypeDecl) {
            return "type declaration";
        } else if (obj instanceof TypeParam) {
            return "type parameter";
        } else if (obj instanceof Variable) {
            return "variable";
        }

        // Scoped statements.
        if (obj instanceof ForStatement) {
            return "for statement";
        } else if (obj instanceof IfStatement) {
            return "if statement";
        } else if (obj instanceof WhileStatement) {
            return "while statement";
        }

        // Others.
        throw new RuntimeException("Unexpected obj: " + obj);
    }

    /**
     * Returns the associativity of the binary operator.
     *
     * @param op The binary operator.
     * @return The associativity of the binary operator.
     */
    public static Associativity getAssociativity(String op) {
        switch (op) {
            case "+":
                return Associativity.LEFT;
            case "and":
                return Associativity.LEFT;
            case "or":
                return Associativity.LEFT;
            case "/":
                return Associativity.LEFT;
            case "==":
                return Associativity.LEFT;
            case ">=":
                return Associativity.LEFT;
            case ">":
                return Associativity.LEFT;
            case "div":
                return Associativity.LEFT;
            case "<=":
                return Associativity.LEFT;
            case "<":
                return Associativity.LEFT;
            case "mod":
                return Associativity.LEFT;
            case "*":
                return Associativity.LEFT;
            case "-":
                return Associativity.LEFT;
            case "!=":
                return Associativity.LEFT;
        }
        throw new RuntimeException("Unknown binop: " + op);
    }

    /**
     * Returns the binding strength of the expression. The binding strength defines the order in which sub-expressions
     * are applied in an expression. Expressions with higher binding strength are applied before expressions with lower
     * binding strength.
     *
     * <p>
     * Only supports fully type checked expressions.
     * </p>
     *
     * @param expr The expression for which to return the binding strength.
     * @return The binding strength of the expression.
     */
    public static int getBindingStrength(Expression expr) {
        // 0: or
        // 1: and
        // 2: <, <=, ==, !=, >=, >
        // 3: - (binary), + (binary)
        // 4: *, /, div, mod
        // 5: - (unary), + (unary), not
        // 6: l[x], l[x:y]
        // 7: true, false, 5, 6.0, null, "a", <int>x, [1], {1}, {1:2}, (1, 2),
        // f(x), a

        if (expr instanceof ToolInvokeExpression) {
            ToolInvokeExpression invoke = (ToolInvokeExpression)expr;
            ToolRef tool = invoke.getTool();
            if (tool.isBuiltin()) {
                String name = invoke.getTool().getName();
                switch (name) {
                    case "or":
                        return 0;

                    case "and":
                        return 1;

                    case "<":
                    case "<=":
                    case "==":
                    case "!=":
                    case ">=":
                    case ">":
                        return 2;

                    case "*":
                    case "/":
                    case "div":
                    case "mod":
                        return 4;

                    case "not":
                        return 5;

                    case "-":
                    case "+": {
                        int argCnt = invoke.getArguments().size();
                        if (argCnt == 2) {
                            return 3;
                        }
                        if (argCnt == 1) {
                            return 5;
                        }
                        throw new RuntimeException("arg count: " + argCnt);
                    }
                }
            }
        }

        if (expr instanceof ProjectionExpression) {
            return 6;
        } else if (expr instanceof SliceExpression) {
            return 6;
        }

        if (expr instanceof BoolExpression) {
            return 7;
        } else if (expr instanceof NumberExpression) {
            return 7;
        } else if (expr instanceof DoubleExpression) {
            return 7;
        } else if (expr instanceof NullExpression) {
            return 7;
        } else if (expr instanceof StringExpression) {
            return 7;
        } else if (expr instanceof CastExpression) {
            return 7;
        } else if (expr instanceof ListExpression) {
            return 7;
        } else if (expr instanceof SetExpression) {
            return 7;
        } else if (expr instanceof MapExpression) {
            return 7;
        } else if (expr instanceof TupleExpression) {
            return 7;
        } else if (expr instanceof ToolInvokeExpression) {
            return 7;
        } else if (expr instanceof ToolParamExpression) {
            return 7;
        } else if (expr instanceof VariableExpression) {
            return 7;
        }

        throw new RuntimeException("Unknown expr: " + expr);
    }

    /**
     * Converts ToolDef expressions to a textual representation derived from the ToolDef ASCII syntax.
     *
     * <p>
     * Only supports fully type checked expressions. References are converted to their absolute names.
     * </p>
     *
     * <p>
     * Expressions are converted to string, and joined using {@code ", "}.
     * </p>
     *
     * @param exprs The ToolDef expressions to convert.
     * @return The textual representation of the ToolDef expressions.
     * @see #exprToStr
     */
    public static String exprsToStr(List<Expression> exprs) {
        List<String> txts = listc(exprs.size());
        for (Expression expr: exprs) {
            txts.add(exprToStr(expr));
        }
        return StringUtils.join(txts, ", ");
    }

    /**
     * Converts a ToolDef expression to a textual representation derived from the ToolDef ASCII syntax.
     *
     * <p>
     * Only supports fully type checked expressions. References are converted to their absolute names.
     * </p>
     *
     * @param expr The ToolDef expression to convert.
     * @return The textual representation of the ToolDef expression.
     */
    public static String exprToStr(Expression expr) {
        if (expr instanceof BoolExpression) {
            BoolExpression bexpr = (BoolExpression)expr;
            return bexpr.isValue() ? "true" : "false";
        } else if (expr instanceof CastExpression) {
            CastExpression cexpr = (CastExpression)expr;
            String childTxt = exprToStr(cexpr.getChild());

            int castStrength = getBindingStrength(expr);
            int childStrength = getBindingStrength(cexpr.getChild());
            if (castStrength > childStrength) {
                childTxt = "(" + childTxt + ")";
            }
            return "<" + typeToStr(cexpr.getType()) + ">" + childTxt;
        } else if (expr instanceof DoubleExpression) {
            DoubleExpression dexpr = (DoubleExpression)expr;
            return dexpr.getValue();
        } else if (expr instanceof ListExpression) {
            ListExpression lexpr = (ListExpression)expr;
            return "[" + exprsToStr(lexpr.getElements()) + "]";
        } else if (expr instanceof MapExpression) {
            MapExpression mexpr = (MapExpression)expr;
            StringBuilder txt = new StringBuilder();
            txt.append("{");
            boolean first = true;
            for (MapEntry entry: mexpr.getEntries()) {
                if (first) {
                    first = false;
                } else {
                    txt.append(", ");
                }
                txt.append(exprToStr(entry.getKey()));
                txt.append(": ");
                txt.append(exprToStr(entry.getValue()));
            }
            txt.append("}");
            return txt.toString();
        } else if (expr instanceof NullExpression) {
            return "null";
        } else if (expr instanceof NumberExpression) {
            NumberExpression dexpr = (NumberExpression)expr;
            return dexpr.getValue();
        } else if (expr instanceof ProjectionExpression) {
            ProjectionExpression pexpr = (ProjectionExpression)expr;
            String childTxt = exprToStr(pexpr.getChild());

            int projStrength = getBindingStrength(expr);
            int childStrength = getBindingStrength(pexpr.getChild());
            if (projStrength > childStrength) {
                childTxt = "(" + childTxt + ")";
            }
            return childTxt + "[" + exprToStr(pexpr.getIndex()) + "]";
        } else if (expr instanceof SetExpression) {
            SetExpression sexpr = (SetExpression)expr;
            return "{" + exprsToStr(sexpr.getElements()) + "}";
        } else if (expr instanceof SliceExpression) {
            SliceExpression sexpr = (SliceExpression)expr;
            String childTxt = exprToStr(sexpr.getChild());

            int sliceStrength = getBindingStrength(expr);
            int childStrength = getBindingStrength(sexpr.getChild());
            if (sliceStrength > childStrength) {
                childTxt = "(" + childTxt + ")";
            }

            StringBuilder txt = new StringBuilder();
            txt.append(childTxt);
            txt.append("[");
            if (sexpr.getBegin() != null) {
                txt.append(exprToStr(sexpr.getBegin()));
            }
            txt.append(":");
            if (sexpr.getEnd() != null) {
                txt.append(exprToStr(sexpr.getEnd()));
            }
            txt.append("]");
            return txt.toString();
        } else if (expr instanceof StringExpression) {
            StringExpression sexpr = (StringExpression)expr;
            return "\"" + Strings.escape(sexpr.getValue()) + "\"";
        } else if (expr instanceof ToolInvokeExpression) {
            ToolInvokeExpression invoke = (ToolInvokeExpression)expr;
            ToolRef tool = invoke.getTool();
            if (tool.isBuiltin()) {
                // Detect binary/unary operators.
                String name = invoke.getTool().getName();
                boolean binop = false;
                boolean unop = false;
                switch (name) {
                    case "or":
                    case "and":
                    case "<":
                    case "<=":
                    case "==":
                    case "!=":
                    case ">=":
                    case ">":
                    case "*":
                    case "/":
                    case "div":
                    case "mod":
                        binop = true;
                        break;

                    case "not":
                        unop = true;
                        break;

                    case "-":
                    case "+": {
                        int argCnt = invoke.getArguments().size();
                        if (argCnt == 1) {
                            unop = true;
                        }
                        if (argCnt == 2) {
                            binop = true;
                        }
                        break;
                    }
                }

                // Handle unary operators.
                if (unop) {
                    Expression child = invoke.getArguments().get(0).getValue();
                    String childTxt = exprToStr(child);

                    int opStrength = getBindingStrength(expr);
                    int childStrength = getBindingStrength(child);
                    if (opStrength > childStrength) {
                        childTxt = "(" + childTxt + ")";
                    } else if (StringUtils.isAlpha(StringUtils.right(name, 1))) {
                        // No parentheses, so add a space, if the operator ends
                        // with a letter.
                        name += " ";
                    }

                    return name + childTxt;
                }

                // Handle binary operators.
                if (binop) {
                    Expression left = invoke.getArguments().get(0).getValue();
                    Expression right = invoke.getArguments().get(1).getValue();

                    int opStrength = getBindingStrength(expr);
                    int leftStrength = getBindingStrength(left);
                    int rightStrength = getBindingStrength(right);

                    String leftTxt = exprToStr(left);
                    if (opStrength > leftStrength
                            || (opStrength == leftStrength && getAssociativity(name) != Associativity.LEFT))
                    {
                        // Local operator has higher binding strength, or they
                        // are equal and it is not left-associative.
                        leftTxt = "(" + leftTxt + ")";
                    }

                    String rightTxt = exprToStr(right);
                    if (opStrength > rightStrength
                            || (opStrength == rightStrength && getAssociativity(name) != Associativity.RIGHT))
                    {
                        // Local operator has higher binding strength, or they
                        // are equal and it is not right-associative.
                        rightTxt = "(" + rightTxt + ")";
                    }

                    return leftTxt + " " + name + " " + rightTxt;
                }

                // No an operator. Handle built-in tools as user defined tools.
            }

            // Handle non-operator invocations.
            StringBuilder txt = new StringBuilder();
            if (tool.isBuiltin()) {
                txt.append(tool.getName());
            } else {
                txt.append(getAbsName(tool.getTool()));
            }
            txt.append("(");
            for (int i = 0; i < invoke.getArguments().size(); i++) {
                if (i > 0) {
                    txt.append(", ");
                }
                ToolArgument arg = invoke.getArguments().get(i);
                if (arg.getName() != null) {
                    txt.append(arg.getName());
                    txt.append("=");
                }
                txt.append(exprToStr(arg.getValue()));
            }
            txt.append(")");
            return txt.toString();
        } else if (expr instanceof ToolParamExpression) {
            ToolParamExpression tpexpr = (ToolParamExpression)expr;
            return getAbsName(tpexpr.getParam());
        } else if (expr instanceof TupleExpression) {
            TupleExpression texpr = (TupleExpression)expr;
            return "(" + exprsToStr(texpr.getElements()) + ")";
        } else if (expr instanceof VariableExpression) {
            VariableExpression vexpr = (VariableExpression)expr;
            return getAbsName(vexpr.getVariable());
        } else {
            // Unknown or unsupported expression.
            throw new RuntimeException("Unknown/unsupported expr: " + expr);
        }
    }

    /**
     * Converts a ToolDef type to a textual representation derived from the ToolDef ASCII syntax. The normalized ToolDef
     * type, normalized at all levels, is used.
     *
     * <p>
     * References that are still present are converted to their absolute names.
     * </p>
     *
     * @param type The ToolDef type to convert.
     * @return The textual representation of the ToolDef type.
     */
    public static String typeToStr(ToolDefType type) {
        return typeToStr(type, true);
    }

    /**
     * Converts a ToolDef type to a textual representation derived from the ToolDef ASCII syntax.
     *
     * <p>
     * References are converted to their absolute names.
     * </p>
     *
     * @param type The ToolDef type to convert.
     * @param normalize Whether to create a textual representation of the normalized type ({@code true}) or of the exact
     *     type as given ({@code false}). If normalized, the type is normalized at all levels.
     * @return The textual representation of the ToolDef type.
     */
    public static String typeToStr(ToolDefType type, boolean normalize) {
        // Normalize the type, if requested.
        if (normalize) {
            type = ToolDefTypeUtils.normalizeType(type);
        }

        // Simple types.
        if (type instanceof BoolType) {
            return type.isNullable() ? "bool?" : "bool";
        } else if (type instanceof DoubleType) {
            return type.isNullable() ? "double?" : "double";
        } else if (type instanceof IntType) {
            return type.isNullable() ? "int?" : "int";
        } else if (type instanceof LongType) {
            return type.isNullable() ? "long?" : "long";
        } else if (type instanceof ObjectType) {
            return type.isNullable() ? "object?" : "object";
        } else if (type instanceof StringType) {
            return type.isNullable() ? "string?" : "string";
        }

        // Container types.
        if (type instanceof ListType) {
            String rslt = type.isNullable() ? "list? " : "list ";
            ListType ltype = (ListType)type;
            return rslt + typeToStr(ltype.getElemType(), normalize);
        } else if (type instanceof SetType) {
            String rslt = type.isNullable() ? "set? " : "set ";
            SetType stype = (SetType)type;
            return rslt + typeToStr(stype.getElemType(), normalize);
        } else if (type instanceof MapType) {
            String rslt = type.isNullable() ? "map?(" : "map(";
            MapType mtype = (MapType)type;
            return rslt + typeToStr(mtype.getKeyType(), normalize) + ":" + typeToStr(mtype.getValueType(), normalize)
                    + ")";
        } else if (type instanceof TupleType) {
            String rslt = type.isNullable() ? "tuple?(" : "tuple(";
            TupleType ttype = (TupleType)type;
            List<String> frslts = listc(ttype.getFields().size());
            for (ToolDefType t: ttype.getFields()) {
                frslts.add(typeToStr(t, normalize));
            }
            return rslt + StringUtils.join(frslts, ", ") + ")";
        }

        // Reference types.
        if (type instanceof TypeParamRef) {
            Assert.check(!type.isNullable());
            return getAbsName(((TypeParamRef)type).getType());
        } else if (type instanceof TypeRef) {
            Assert.check(!type.isNullable());
            return getAbsName(((TypeRef)type).getType());
        }

        // Unknown/unsupported types.
        throw new RuntimeException("Unknown/unsupported type: " + type);
    }
}
