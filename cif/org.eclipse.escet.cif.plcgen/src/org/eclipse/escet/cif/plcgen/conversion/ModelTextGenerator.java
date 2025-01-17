//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.conversion;

import static org.eclipse.escet.common.java.Strings.duplicate;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.plcgen.model.expressions.PlcArrayLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBoolLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcEnumLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcFuncAppl;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcFuncBlockAppl;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcNamedValue;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcRealLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcStructLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression.PlcArrayProjection;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression.PlcProjection;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression.PlcStructProjection;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.ExprBinding;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcFuncNotation;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcParamDirection;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcParameterDescription;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;
import org.eclipse.escet.cif.plcgen.model.functions.PlcPlainFuncDescription;
import org.eclipse.escet.cif.plcgen.model.statements.PlcAssignmentStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcCommentBlock;
import org.eclipse.escet.cif.plcgen.model.statements.PlcCommentLine;
import org.eclipse.escet.cif.plcgen.model.statements.PlcFuncApplStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcReturnStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcSelectionStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcSelectionStatement.PlcSelectChoice;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcEnumType;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;

/** Conversion of expression and statement trees to text. */
public class ModelTextGenerator {
    /**
     * Convert an expression in a non-binding infix operator context to text.
     *
     * <p>
     * A non-binding infix operator context allows omitting parentheses around the expression without changing the
     * computation. This typically happens for expressions surrounded by statements, or in lists of values, for example
     * in array or struct literals.
     * </p>
     *
     * @param expr Expression to convert.
     * @return The constructed text.
     */
    public String toString(PlcExpression expr) {
        return toString(expr, FuncApplPreference.PREFER_INFIX);
    }

    /**
     * Convert an expression to text.
     *
     * @param expr Expression to convert.
     * @param funcApplPreference Notation preference for function application.
     * @return The constructed text.
     */
    public String toString(PlcExpression expr, FuncApplPreference funcApplPreference) {
        StringBuilder textBuilder = new StringBuilder();
        toText(expr, textBuilder, funcApplPreference);
        return textBuilder.toString();
    }

    /**
     * Convert an expression to text.
     *
     * @param expr Expression to convert.
     * @param textBuilder Storage of produced text, extended in-place.
     * @param funcApplPreference Notation preference for function application.
     */
    private void toText(PlcExpression expr, StringBuilder textBuilder, FuncApplPreference funcApplPreference) {
        toText(expr, textBuilder, ExprBinding.NO_PRIORITY, false, false, funcApplPreference);
    }

    /**
     * Convert an expression to text.
     *
     * @param expr Expression to convert.
     * @param textBuilder Storage of produced text, extended in-place.
     * @param parentBinding Infix binding strength of the parent.
     * @param atParentLeft Whether the expression is at the far left of the parent expression.
     * @param atParentRight Whether the expression is at the far right of the parent expression.
     * @param funcApplPreference Notation preference for function application.
     */
    private void toText(PlcExpression expr, StringBuilder textBuilder, ExprBinding parentBinding, boolean atParentLeft,
            boolean atParentRight, FuncApplPreference funcApplPreference)
    {
        // Dispatch to the right sub-class.
        if (expr instanceof PlcBoolLiteral boolLit) {
            toText(boolLit, textBuilder);
        } else if (expr instanceof PlcIntLiteral intLit) {
            toText(intLit, textBuilder);
        } else if (expr instanceof PlcRealLiteral realLit) {
            toText(realLit, textBuilder);
        } else if (expr instanceof PlcEnumLiteral enumLit) {
            toText(enumLit, textBuilder);
        } else if (expr instanceof PlcArrayLiteral arrayLit) {
            toText(arrayLit, textBuilder, funcApplPreference);
        } else if (expr instanceof PlcStructLiteral structLit) {
            toText(structLit, textBuilder, funcApplPreference);
        } else if (expr instanceof PlcFuncAppl funcAppl) {
            toText(funcAppl, textBuilder, parentBinding, atParentLeft, atParentRight, funcApplPreference);
        } else if (expr instanceof PlcVarExpression varExpr) {
            toText(varExpr, textBuilder, funcApplPreference);
        } else {
            throw new AssertionError("Unexpected PLC expression \"" + expr + "\" found.");
        }
    }

    /**
     * Convert a literal value to text.
     *
     * <p>
     * This function does a best effort in recognizing literal forms and returning a literal value. However if the
     * expression is not recognized as literal value, it may produce text that represents a computation to derive
     * the value.
     * </p>
     *
     * @param expr Literal value to convert.
     * @return The string representation of the value.
     */
    public String literalToString(PlcExpression expr) {
        if (PlcElementaryType.isIntType(expr.type)) {
            // Negative integer literals are expressed as computations. Try to recognize them and convert them back to
            // their literal value.
            Integer value = tryGetIntValue(expr);
            if (value != null) {
                return Integer.toString(value);
            }
        } else if (PlcElementaryType.isRealType(expr.type)) {
            // Negative real literals are expressed as computations. Try to recognize them and convert them back to
            // their literal value.
            String value = tryGetRealValue(expr);
            if (value != null) {
                return value;
            }
        }

        // Unrecognized integer expression or an expression of another type. Fallback to normal text conversion for
        // a best effort. Note that boolean, real, and enumeration literals do get converted correctly in this way.
        return toString(expr);
    }

    /**
     * Try to get a numerical integer value.
     *
     * <p>
     * CIF encodes negative integer literals as a computation. This function recognizes a superset of those expressions
     * and converts the value back to a literal value.
     * </p>
     *
     * @param expr The expression with integer type to parse.
     * @return A numerical integer value if the computation form was recognized, else {@code null}.
     */
    private Integer tryGetIntValue(PlcExpression expr) {
        Assert.check(PlcElementaryType.isIntType(expr.type));

        if (expr instanceof PlcIntLiteral intLit) {
            // Non-negative integer literals are simply an integer literal object.
            return intLit.value;

        } else if (expr instanceof PlcFuncAppl fnAppl) {
            PlcBasicFuncDescription funcDesc = fnAppl.function;
            if (funcDesc.operation == PlcFuncOperation.NEGATE_OP) {
                // All negative integer literals except -2147483648 are encoded as a negated positive integer literal.
                Integer v = tryGetIntValue(fnAppl.arguments.get(funcDesc.parameters[0].name).value);
                return (v == null) ? null : -v;

            } else if (funcDesc.operation == PlcFuncOperation.SUBTRACT_OP) {
                // -2147483648 is encoded as -2147483647 - 1.
                Integer left = tryGetIntValue(fnAppl.arguments.get(funcDesc.parameters[0].name).value);
                Integer right = tryGetIntValue(fnAppl.arguments.get(funcDesc.parameters[1].name).value);
                if (left != null && right != null) {
                    return left - right;
                }
            }
        }
        return null; // Expression form is not recognized as an integer literal.
    }

    /**
     * Try to get a real value.
     *
     * @param expr The expression with real type to parse.
     * @return A real value if the computation form was recognized, else {@code null}.
     */
    private String tryGetRealValue(PlcExpression expr) {
        Assert.check(PlcElementaryType.isRealType(expr.type));

        if (expr instanceof PlcRealLiteral realLit) {
            // Non-negative real literals are simply a real literal object.
            return realLit.value;

        } else if (expr instanceof PlcFuncAppl fnAppl) {
            PlcBasicFuncDescription funcDesc = fnAppl.function;
            if (funcDesc.operation == PlcFuncOperation.NEGATE_OP) {
                // All negative real literals are encoded as a negated positive real literal.
                String v = tryGetRealValue(fnAppl.arguments.get(funcDesc.parameters[0].name).value);
                if (v != null) {
                    // Avoid creating "--number".
                    v = v.startsWith("-") ? v.substring(1) : ("-" + v);
                }
                return v;
            }
        }
        return null; // Expression form is not recognized as a real literal.
    }

    /**
     * Convert a boolean literal expression to text.
     *
     * @param boolLit Expression to convert.
     * @param textBuilder Storage of produced text, extended in-place.
     */
    private void toText(PlcBoolLiteral boolLit, StringBuilder textBuilder) {
        textBuilder.append(boolLit.value ? "TRUE" : "FALSE");
    }

    /**
     * Convert an integer literal expression to text.
     *
     * @param intLit Expression to convert.
     * @param textBuilder Storage of produced text, extended in-place.
     */
    private void toText(PlcIntLiteral intLit, StringBuilder textBuilder) {
        textBuilder.append(String.valueOf(intLit.value));
    }

    /**
     * Convert a real literal expression to text.
     *
     * @param realLit Expression to convert.
     * @param textBuilder Storage of produced text, extended in-place.
     */
    private void toText(PlcRealLiteral realLit, StringBuilder textBuilder) {
        String rslt = realLit.value;
        int idx = rslt.indexOf('.');
        if (idx == -1) {
            idx = rslt.indexOf('e');
            if (idx == -1) {
                idx = rslt.indexOf('E');
                if (idx == -1) { // Not needed for CIF real literals, but our PLC model allows more freedom.
                    idx = rslt.length();
                }
            }
            rslt = rslt.substring(0, idx) + ".0" + rslt.substring(idx);
        }
        textBuilder.append(rslt);
    }

    /**
     * Convert an enumeration literal expression to text.
     *
     * @param enumLit Expression to convert.
     * @param textBuilder Storage of produced text, extended in-place.
     */
    private void toText(PlcEnumLiteral enumLit, StringBuilder textBuilder) {
        textBuilder.append(enumLit.value);
    }

    /**
     * Convert an array literal expression to text.
     *
     * @param arrayLit Expression to convert.
     * @param textBuilder Storage of produced text, extended in-place.
     * @param funcApplPreference Notation preference for function application.
     */
    private void toText(PlcArrayLiteral arrayLit, StringBuilder textBuilder, FuncApplPreference funcApplPreference) {
        textBuilder.append("[");
        boolean first = true;
        for (PlcExpression value: arrayLit.values) {
            if (!first) {
                textBuilder.append(", ");
            }
            first = false;
            toText(value, textBuilder, funcApplPreference);
        }
        textBuilder.append("]");
    }

    /**
     * Convert a struct literal expression to text.
     *
     * @param structLit Expression to convert.
     * @param textBuilder Storage of produced text, extended in-place.
     * @param funcApplPreference Notation preference for function application.
     */
    private void toText(PlcStructLiteral structLit, StringBuilder textBuilder, FuncApplPreference funcApplPreference) {
        textBuilder.append("("); // Struct brackets are always needed.
        boolean first = true;
        for (PlcNamedValue namedValue: structLit.values) {
            if (!first) {
                textBuilder.append(", ");
            }
            first = false;
            textBuilder.append(namedValue.name);
            textBuilder.append(" := ");
            toText(namedValue.value, textBuilder, funcApplPreference);
        }
        textBuilder.append(")"); // Struct brackets are always needed.
    }

    /**
     * Convert a function application expression to text.
     *
     * @param funcAppl Expression to convert.
     * @param textBuilder Storage of produced text, extended in-place.
     * @param parentBinding Infix binding strength of the parent.
     * @param atParentLeft Whether the expression is at the far left of the parent expression.
     * @param atParentRight Whether the expression is at the far right of the parent expression.
     * @param funcApplPreference Preference in function application notation.
     */
    private void toText(PlcFuncAppl funcAppl, StringBuilder textBuilder, ExprBinding parentBinding,
            boolean atParentLeft, boolean atParentRight, FuncApplPreference funcApplPreference)
    {
        PlcBasicFuncDescription basicDescr = funcAppl.function;
        Map<String, PlcNamedValue> arguments = funcAppl.arguments;

        // Decide what notation forms are allowed. Check there is at least one form available.
        boolean allArgumentsSupplied = basicDescr.parameters.length == arguments.size();
        boolean infixNotationAllowed = basicDescr.notations.contains(PlcFuncNotation.INFIX)
                && (funcApplPreference != FuncApplPreference.OUTER_PREFIX) && allArgumentsSupplied;
        boolean informalNotationAllowed = basicDescr.notations.contains(PlcFuncNotation.INFORMAL)
                && allArgumentsSupplied;
        boolean formalNotationAllowed = basicDescr.notations.contains(PlcFuncNotation.FORMAL);
        Assert.check(infixNotationAllowed || informalNotationAllowed || formalNotationAllowed);

        // Prefer infix.
        if (infixNotationAllowed) {
            if (!(basicDescr instanceof PlcPlainFuncDescription plainFunc)) {
                throw new AssertionError("Function allows infix notation but does not supply infix notation data.");
            }

            // Obtain the layout information for the infix function application.
            PlcBasicFuncDescription.ExprBinding infixBinding = plainFunc.infixBinding;
            String infixFuncName = plainFunc.infixFuncName;
            Assert.notNull(infixFuncName);

            int lastArgumentIndex = arguments.size() - 1;
            boolean isUnaryOperation = (lastArgumentIndex == 0);

            // Decide whether parentheses are needed around the call.
            boolean needsParentheses = infixBinding.needsParentheses(parentBinding, atParentLeft, atParentRight);
            // S7-300 doesn't accept nested infix unary "-" operations like "--1". Wrap parentheses around each level.
            needsParentheses |= isUnaryOperation;

            textBuilder.append(needsParentheses ? "(" : "");

            // Handle infix notation with a single argument as a special case.
            if (isUnaryOperation) {
                // Single parameter infix notation is literally pre-pended to make it a prefix.
                textBuilder.append(infixFuncName);
            }

            // Write the remaining expressions, and possibly the closing parenthesis.
            String infixString = " " + infixFuncName + " ";
            int argNumber = 0;
            for (PlcParameterDescription param: basicDescr.parameters) {
                PlcNamedValue argNamedValue = arguments.get(param.name);
                Assert.notNull(argNamedValue);

                textBuilder.append(argNumber > 0 ? infixString : "");
                toText(argNamedValue.value, textBuilder, infixBinding, argNumber == 0, argNumber == lastArgumentIndex,
                        FuncApplPreference.PREFER_INFIX);
                argNumber++;
            }
            textBuilder.append(needsParentheses ? ")" : "");

        } else if (informalNotationAllowed || formalNotationAllowed) {
            // Generate prefix notation.
            //
            // Obtain the type extension if relevant.
            String typeExtension = "";
            if (basicDescr.typeExtension.testFunction.test(funcAppl.type)) {
                typeExtension = "_" + getTypeName(funcAppl.type);
            }

            // Obtain the prefix function name.
            String prefixFuncName;
            if (funcAppl instanceof PlcFuncBlockAppl blockAppl) {
                prefixFuncName = blockAppl.variable.varName; // As defined by the IEC standard.
                // Some PLC systems must also have the prefix function name.
                if (!basicDescr.prefixFuncName.isEmpty()) {
                    prefixFuncName = prefixFuncName + "." + basicDescr.prefixFuncName;
                }
            } else {
                prefixFuncName = basicDescr.prefixFuncName + typeExtension;
            }
            Assert.notNull(prefixFuncName);

            // Construct the function application.
            textBuilder.append(prefixFuncName);
            textBuilder.append("(");

            int argNumber = 0;
            boolean useFormalSyntax = !informalNotationAllowed; // Prefer informal syntax above formal syntax.
            for (PlcParameterDescription param: basicDescr.parameters) {
                PlcNamedValue argNamedValue = arguments.get(param.name);
                if (argNamedValue == null) {
                    continue;
                }

                textBuilder.append(argNumber > 0 ? ", " : "");
                if (useFormalSyntax) {
                    textBuilder.append(param.name);
                    textBuilder.append(param.direction == PlcParamDirection.OUTPUT_ONLY ? " => " : " := ");
                }
                toText(argNamedValue.value, textBuilder, FuncApplPreference.PREFER_INFIX);
                argNumber++;
            }
            textBuilder.append(")");
        } else {
            throw new AssertionError("Failed to convert the function application to text.");
        }
    }

    /**
     * Extract the name of the type from a type if possible.
     *
     * @param type Type to examine.
     * @return The name of the examined type.
     * @throws AssertionError If no name is available in the type.
     */
    private String getTypeName(PlcType type) {
        if (type instanceof PlcElementaryType elemType) {
            return elemType.name;
        } else if (type instanceof PlcEnumType enumType) {
            return enumType.typeName;
        } else if (type instanceof PlcStructType structType) {
            return structType.typeName;
        }
        throw new AssertionError("Cannot extract a type name from \"" + type + "\".");
    }

    /**
     * Convert a variable expression to text.
     *
     * @param varExpr Expression to convert.
     * @param textBuilder Storage of produced text, extended in-place.
     * @param funcApplPreference Notation preference for function application.
     */
    private void toText(PlcVarExpression varExpr, StringBuilder textBuilder, FuncApplPreference funcApplPreference) {
        textBuilder.append(varExpr.variable.targetText);
        for (PlcProjection proj: varExpr.projections) {
            toText(proj, textBuilder, funcApplPreference);
        }
    }

    /**
     * Convert a projection to text.
     *
     * @param proj The projection to convert.
     * @param textBuilder Storage of produced text, extended in-place.
     * @param funcApplPreference Notation preference for function application.
     */
    private void toText(PlcProjection proj, StringBuilder textBuilder, FuncApplPreference funcApplPreference) {
        if (proj instanceof PlcStructProjection structProj) {
            textBuilder.append(".");
            textBuilder.append(structProj.fieldName);
        } else if (proj instanceof PlcArrayProjection arrayProj) {
            textBuilder.append('[');
            toText(arrayProj.indexExpression, textBuilder, funcApplPreference);
            textBuilder.append(']');
        } else {
            throw new AssertionError("Unexpected PLC projection \"" + proj + "\" found.");
        }
    }

    /**
     * Convert a sequence of PLC statements to text. Without {@code fixCodeBlock} a sequence without proper statement
     * will crash the application.
     *
     * @param plcStats Statements to convert.
     * @param pouName Name of the surrounding POU.
     * @param fixCodeBlock If set, make sequences without proper statement correct by adding an empty statement.
     * @return The generated output.
     */
    public String toString(List<PlcStatement> plcStats, String pouName, boolean fixCodeBlock) {
        CodeBox boxBuilder = new MemoryCodeBox();
        toText(plcStats, boxBuilder, pouName, fixCodeBlock);
        return boxBuilder.toString();
    }

    /**
     * Convert a sequence of PLC statements to text. Without {@code fixCodeBlock} a sequence without proper statement
     * will crash the application.
     *
     * @param plcStats StatementS to convert.
     * @param boxBuilder Storage of produced text, extended in-place.
     * @param pouName Name of the surrounding POU.
     * @param fixCodeBlock If set, make sequences without proper statement correct by adding an empty statement.
     */
    public void toText(List<PlcStatement> plcStats, CodeBox boxBuilder, String pouName, boolean fixCodeBlock) {
        // Generate the output while checking if the sequence contains at least one proper statement.
        boolean foundStat = false;
        for (PlcStatement plcStat: plcStats) {
            toText(plcStat, boxBuilder, pouName);

            foundStat |= plcStat.isProperPlcStatement();
        }

        if (!foundStat) {
            if (fixCodeBlock) {
                toText(new PlcCommentLine("Nothing to do.", true), boxBuilder, pouName);
            } else {
                throw new AssertionError("Code block does not have a proper PLC statement.");
            }
        }
    }

    /**
     * Convert a PLC statement to text.
     *
     * @param plcStat Statement to convert.
     * @param pouName Name of the surrounding POU.
     * @return The generated text.
     */
    public String toString(PlcStatement plcStat, String pouName) {
        CodeBox boxBuilder = new MemoryCodeBox();
        toText(plcStat, boxBuilder, pouName);
        return boxBuilder.toString();
    }

    /**
     * Convert a PLC statement to text.
     *
     * @param plcStat Statement to convert.
     * @param boxBuilder Storage of produced text, extended in-place.
     * @param pouName Name of the surrounding POU.
     */
    public void toText(PlcStatement plcStat, CodeBox boxBuilder, String pouName) {
        if (plcStat instanceof PlcAssignmentStatement asgStat) {
            toText(asgStat, boxBuilder, pouName);
        } else if (plcStat instanceof PlcCommentLine cmtLine) {
            toText(cmtLine, boxBuilder, pouName);
        } else if (plcStat instanceof PlcCommentBlock cmtBlock) {
            toText(cmtBlock, boxBuilder, pouName);
        } else if (plcStat instanceof PlcReturnStatement retStat) {
            toText(retStat, boxBuilder, pouName);
        } else if (plcStat instanceof PlcSelectionStatement selStat) {
            toText(selStat, boxBuilder, pouName);
        } else if (plcStat instanceof PlcFuncApplStatement funcApplStat) {
            toText(funcApplStat, boxBuilder, pouName);
        } else {
            throw new AssertionError("Unexpected PLC statement \"" + plcStat + "\" found.");
        }
    }

    /**
     * Convert a PLC assignment statement to text.
     *
     * @param asgStat Assignment statement to convert.
     * @param boxBuilder Storage of produced text, extended in-place.
     * @param pouName Name of the surrounding POU.
     */
    private void toText(PlcAssignmentStatement asgStat, CodeBox boxBuilder, String pouName) {
        boxBuilder.add("%s := %s;", toString(asgStat.lhs), toString(asgStat.value));
    }

    /**
     * Convert a PLC comment to text.
     *
     * @param cmtLine Comment line to convert.
     * @param boxBuilder Storage of produced text, extended in-place.
     * @param pouName Name of the surrounding POU.
     */
    private void toText(PlcCommentLine cmtLine, CodeBox boxBuilder, String pouName) {
        if (cmtLine.commentText == null) {
            boxBuilder.add("%s", cmtLine.isEmptyStatement ? ";" : "");
        } else {
            boxBuilder.add("(* %s *)%s", cmtLine.commentText, cmtLine.isEmptyStatement ? " ;" : "");
        }
    }

    /**
     * Convert a PLC comment block to text.
     *
     * @param cmtBlock Comment block to convert.
     * @param boxBuilder Storage of produced text, extended in-place.
     * @param pouName Name of the surrounding POU.
     */
    private void toText(PlcCommentBlock cmtBlock, CodeBox boxBuilder, String pouName) {
        boxBuilder.add("(*" + duplicate("*", cmtBlock.starCount));
        for (String line: cmtBlock.lines) {
            boxBuilder.add(" * " + line);
        }
        boxBuilder.add(" " + duplicate("*", cmtBlock.starCount) + "*)");
    }

    /**
     * Convert a PLC function application statement to text.
     *
     * @param funcApplStat Function application statement to convert.
     * @param boxBuilder Storage of produced text, extended in-place.
     * @param pouName Name of the surrounding POU.
     */
    private void toText(PlcFuncApplStatement funcApplStat, CodeBox boxBuilder, String pouName) {
        boxBuilder.add("%s;", toString(funcApplStat.funcApplExpr, FuncApplPreference.OUTER_PREFIX));
    }

    /**
     * Convert a PLC return statement to text.
     *
     * @param retStat Return statement to convert.
     * @param boxBuilder Storage of produced text, extended in-place.
     * @param pouName Name of the surrounding POU.
     */
    private void toText(PlcReturnStatement retStat, CodeBox boxBuilder, String pouName) {
        if (retStat.returnValue != null) {
            boxBuilder.add("%s := %s;", pouName, toString(retStat.returnValue));
        }
        boxBuilder.add("RETURN;");
    }

    /**
     * Convert a generic PLC selection statement to text.
     *
     * @param selStat Return statement to convert.
     * @param boxBuilder Storage of produced text, extended in-place.
     * @param pouName Name of the surrounding POU.
     */
    private void toText(PlcSelectionStatement selStat, CodeBox boxBuilder, String pouName) {
        if (selStat.condChoices.isEmpty()) {
            // No conditional choices, always perform the 'else' statements.
            toText(selStat.elseStats, boxBuilder, pouName, true);
            return;
        }

        // Conditional choices exist.
        String testText = "IF";
        for (PlcSelectChoice condChoice: selStat.condChoices) {
            boxBuilder.add("%s %s THEN", testText, toString(condChoice.guard));
            testText = "ELSIF";
            boxBuilder.indent();
            toText(condChoice.thenStats, boxBuilder, pouName, true);
            boxBuilder.dedent();
        }
        if (!selStat.elseStats.isEmpty()) {
            boxBuilder.add("ELSE");
            boxBuilder.indent();
            toText(selStat.elseStats, boxBuilder, pouName, true);
            boxBuilder.dedent();
        }
        boxBuilder.add("END_IF;");
    }

    /** Preference in notation of function applications. */
    public static enum FuncApplPreference {
        /** Prefer infix notation for function applications. */
        PREFER_INFIX,

        /** Outer function application must be prefix notation. */
        OUTER_PREFIX;
    }
}
