//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import java.util.List;

import org.eclipse.escet.cif.plcgen.model.expressions.PlcArrayLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBasicFuncDescription;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBasicFuncDescription.ExprBinding;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBasicFuncDescription.PlcParamDirection;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBasicFuncDescription.PlcParameterDescription;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcBoolLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcFuncAppl;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcIntLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcNamedValue;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcRealLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcStructLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression.PlcArrayProjection;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression.PlcProjection;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression.PlcStructProjection;
import org.eclipse.escet.cif.plcgen.model.statements.PlcAssignmentStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcCommentLine;
import org.eclipse.escet.cif.plcgen.model.statements.PlcFuncApplStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcReturnStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcSelectionStatement;
import org.eclipse.escet.cif.plcgen.model.statements.PlcSelectionStatement.PlcSelectChoice;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
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
        if (expr instanceof PlcBoolLiteral boolLil) {
            toText(boolLil, textBuilder);
        } else if (expr instanceof PlcIntLiteral intLil) {
            toText(intLil, textBuilder);
        } else if (expr instanceof PlcRealLiteral realLil) {
            toText(realLil, textBuilder);
        } else if (expr instanceof PlcArrayLiteral arrayLil) {
            toText(arrayLil, textBuilder, funcApplPreference);
        } else if (expr instanceof PlcStructLiteral structLil) {
            toText(structLil, textBuilder, funcApplPreference);
        } else if (expr instanceof PlcFuncAppl funcAppl) {
            toText(funcAppl, textBuilder, parentBinding, atParentLeft, atParentRight, funcApplPreference);
        } else if (expr instanceof PlcVarExpression varExpr) {
            toText(varExpr, textBuilder, funcApplPreference);
        } else {
            throw new AssertionError("Unexpected PLC expression \"" + String.valueOf(expr) + "\" found.");
        }
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
                if (idx == -1) {
                    idx = rslt.length();
                }
            }
            rslt = rslt.substring(0, idx) + ".0" + rslt.substring(idx);
        }
        textBuilder.append(rslt);
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
        PlcBasicFuncDescription function = funcAppl.function;
        List<PlcNamedValue> arguments = funcAppl.arguments;

        // Prefer infix notation.
        if (funcApplPreference.equals(FuncApplPreference.PREFER_INFIX) && function.infixFuncName != null) {
            boolean needsParentheses = function.infixBinding.needsParentheses(parentBinding, atParentLeft,
                    atParentRight);
            textBuilder.append(needsParentheses ? "(" : "");

            int lastArgumentIndex = arguments.size() - 1;
            if (lastArgumentIndex == 0) {
                // Single parameter infix notation is literally pre-pended to make it a prefix.
                textBuilder.append(function.infixFuncName);
            }

            // Write the expressions.
            String infixString = " " + function.infixFuncName + " ";
            for (int i = 0; i <= lastArgumentIndex; i++) {
                textBuilder.append(i > 0 ? infixString : "");
                toText(arguments.get(i).value, textBuilder, function.infixBinding, i == 0, i == lastArgumentIndex,
                        FuncApplPreference.PREFER_INFIX);
            }
            textBuilder.append(needsParentheses ? ")" : "");
        } else {
            // No infix available, use formal prefix notation instead.
            Assert.notNull(function.prefixFuncName);
            textBuilder.append(function.prefixFuncName);
            textBuilder.append("(");
            boolean first = true;
            for (PlcNamedValue namedValue: arguments) {
                if (!first) {
                    textBuilder.append(", ");
                }
                first = false;
                PlcParameterDescription paramDesc = function.prefixParameters.get(namedValue.name);
                Assert.notNull(paramDesc, "Parameter name \"" + String.valueOf(namedValue.name)
                        + "\" is not defined for function \"" + function.prefixFuncName + "\"'");
                textBuilder.append(namedValue.name);
                textBuilder.append(paramDesc.direction == PlcParamDirection.OUTPUT_ONLY ? " => " : " := ");
                toText(namedValue.value, textBuilder, FuncApplPreference.PREFER_INFIX);
            }
            textBuilder.append(")");
        }
    }

    /**
     * Convert a variable expression to text.
     *
     * @param varExpr Expression to convert.
     * @param textBuilder Storage of produced text, extended in-place.
     * @param funcApplPreference Notation preference for function application.
     */
    private void toText(PlcVarExpression varExpr, StringBuilder textBuilder, FuncApplPreference funcApplPreference) {
        textBuilder.append(varExpr.variable.name);
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
            boolean first = true;
            for (PlcExpression indexExpr: arrayProj.indexExpressions) {
                if (!first) {
                    textBuilder.append(", ");
                }
                first = false;
                toText(indexExpr, textBuilder, funcApplPreference);
            }
            textBuilder.append(']');
        } else {
            throw new AssertionError("Unexpected PLC prjection \"" + String.valueOf(proj) + "\" found.");
        }
    }

    /**
     * Convert a sequence of PLC statementS to text. Without {@code fixCodeBlock} an sequence without proper statement
     * will crash the application.
     *
     * @param plcStats StatementS to convert.
     * @param pouName Name of the surrounding POU.
     * @param fixCodeBlcok If set, make sequences without proper statement correct by adding an empty statement.
     * @return The generated output.
     */
    public String toString(List<PlcStatement> plcStats, String pouName, boolean fixCodeBlcok) {
        CodeBox boxBuilder = new MemoryCodeBox();
        toText(plcStats, boxBuilder, pouName, fixCodeBlcok);
        return boxBuilder.toString();
    }

    /**
     * Convert a sequence of PLC statementS to text. Without {@code fixCodeBlock} an sequence without proper statement
     * will crash the application.
     *
     * @param plcStats StatementS to convert.
     * @param boxBuilder Storage of produced text, extended in-place.
     * @param pouName Name of the surrounding POU.
     * @param fixCodeBlcok If set, make sequences without proper statement correct by adding an empty statement.
     */
    public void toText(List<PlcStatement> plcStats, CodeBox boxBuilder, String pouName, boolean fixCodeBlcok) {
        // Generate the output while checking if the sequence contains at least one proper statement.
        boolean foundStat = false;
        for (PlcStatement plcStat: plcStats) {
            toText(plcStat, boxBuilder, pouName);

            if (!(plcStat instanceof PlcCommentLine cmtLine) || cmtLine.isEmptyStatement) {
                foundStat = true;
            }
        }

        if (!foundStat) {
            if (fixCodeBlcok) {
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
        } else if (plcStat instanceof PlcReturnStatement retStat) {
            toText(retStat, boxBuilder, pouName);
        } else if (plcStat instanceof PlcSelectionStatement selStat) {
            toText(selStat, boxBuilder, pouName);
        } else if (plcStat instanceof PlcFuncApplStatement funcApplStat) {
            toText(funcApplStat, boxBuilder, pouName);
        } else {
            throw new AssertionError("Unexpected PLC statement \"" + String.valueOf(plcStat) + "\" found.");
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
     * Convert a PLC comment to text.
     *
     * @param cmtLine Comment line to convert.
     * @param boxBuilder Storage of produced text, extended in-place.
     * @param pouName Name of the surrounding POU.
     */
    private void toText(PlcCommentLine cmtLine, CodeBox boxBuilder, String pouName) {
        // Nesting of comments is not allowed.
        Assert.check(!cmtLine.commentText.contains("(*"));
        Assert.check(!cmtLine.commentText.contains("*)"));

        String emptyStat = cmtLine.isEmptyStatement ? " ;" : "";
        boxBuilder.add("(* %s *)%s", cmtLine.commentText, emptyStat);
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
