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

package org.eclipse.escet.chi.codegen.expressions;

import static org.eclipse.escet.chi.codegen.Constants.CHI_READ_MEMORY_FILE_FQC;
import static org.eclipse.escet.chi.codegen.Constants.RANDOM_PKG;
import static org.eclipse.escet.chi.codegen.Constants.TIMER_FQC;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createTypeID;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.makeIntTypeID;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.makeListTypeID;
import static org.eclipse.escet.chi.typecheck.CheckType.dropReferences;
import static org.eclipse.escet.common.java.Assert.check;
import static org.eclipse.escet.common.java.Assert.fail;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.Constants;
import org.eclipse.escet.chi.codegen.OutputPosition;
import org.eclipse.escet.chi.codegen.java.JavaClass;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.codegen.java.JavaMethod;
import org.eclipse.escet.chi.codegen.types.MatrixTypeID;
import org.eclipse.escet.chi.codegen.types.TypeID;
import org.eclipse.escet.chi.codegen.types.TypeIDCreation;
import org.eclipse.escet.chi.metamodel.chi.BinaryExpression;
import org.eclipse.escet.chi.metamodel.chi.BinaryOperators;
import org.eclipse.escet.chi.metamodel.chi.BoolType;
import org.eclipse.escet.chi.metamodel.chi.CallExpression;
import org.eclipse.escet.chi.metamodel.chi.CastExpression;
import org.eclipse.escet.chi.metamodel.chi.ConstantReference;
import org.eclipse.escet.chi.metamodel.chi.DistributionType;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.FunctionReference;
import org.eclipse.escet.chi.metamodel.chi.FunctionType;
import org.eclipse.escet.chi.metamodel.chi.IntType;
import org.eclipse.escet.chi.metamodel.chi.ListType;
import org.eclipse.escet.chi.metamodel.chi.MatrixType;
import org.eclipse.escet.chi.metamodel.chi.ModelDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ModelReference;
import org.eclipse.escet.chi.metamodel.chi.ModelType;
import org.eclipse.escet.chi.metamodel.chi.ProcessDeclaration;
import org.eclipse.escet.chi.metamodel.chi.ProcessReference;
import org.eclipse.escet.chi.metamodel.chi.ProcessType;
import org.eclipse.escet.chi.metamodel.chi.ReadCallExpression;
import org.eclipse.escet.chi.metamodel.chi.RealType;
import org.eclipse.escet.chi.metamodel.chi.SetType;
import org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference;
import org.eclipse.escet.chi.metamodel.chi.StdLibFunctions;
import org.eclipse.escet.chi.metamodel.chi.StringType;
import org.eclipse.escet.chi.metamodel.chi.TimerType;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.UnaryExpression;
import org.eclipse.escet.chi.metamodel.chi.UnaryOperators;
import org.eclipse.escet.chi.metamodel.chi.VariableReference;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Base class of generated expressions. */
public abstract class ExpressionBase extends OutputPosition {
    /**
     * Constructor of the {@link ExpressionBase} class, deriving the position from a Chi source object.
     *
     * @param chiobj Source object to derive the position information from.
     */
    public ExpressionBase(PositionObject chiobj) {
        super(chiobj);
    }

    /**
     * Retrieve the program text to execute before the value is available.
     *
     * @return The program text that needs to be executed before the value becomes available. May be empty.
     */
    public abstract List<String> getCode();

    /**
     * Retrieve the expression text to derive the value of the expression. Normally, the {@link #getValue()} should be
     * used instead of this method.
     *
     * <p>
     * If {@link #getCode} returns text, that code should be executed before using this value to get the expression
     * value.
     * </p>
     * <p>
     * To prevent using values more than one time without noticing, the {@code reRead} parameter denotes whether it is
     * the first time the value is being read.
     * </p>
     *
     * @param reRead If {@code false}, the value must not have been read before (the default). If {@code true}, the
     *     value must have been read before.
     * @return The expression text containing the expression value.
     */
    public abstract String getValue(boolean reRead);

    /**
     * Read the value of an expression for the first time.
     *
     * @return The value of the expression.
     */
    public String getValue() {
        return getValue(false);
    }

    /**
     * Construct a new expression from an old one.
     *
     * @param code Code lines, if any. May be {@code null}.
     * @param value Expression value.
     * @param position Position information associate with the expression.
     * @return New expression.
     */
    public static ExpressionBase makeExpression(List<String> code, String value, PositionObject position) {
        if (code == null || code.isEmpty()) {
            return new SimpleExpression(value, position);
        }
        return new CodeExpression(code, value, position);
    }

    /**
     * Convert an expression to the implementation.
     *
     * @param expr Expression to convert.
     * @param ctxt Code generator context.
     * @param currentFile Current Java class targeted for code generation.
     * @return Converted expression.
     */
    public static ExpressionBase convertExpression(Expression expr, CodeGeneratorContext ctxt, JavaFile currentFile) {
        // Unfold constant references to their expression.
        while (expr instanceof ConstantReference) {
            ConstantReference cr = (ConstantReference)expr;
            expr = cr.getConstant().getValue();
        }

        // Handle variable references globally.
        if (expr instanceof VariableReference) {
            VariableReference varRef = (VariableReference)expr;
            String value = ctxt.getDefinition(varRef.getVariable());
            return new SimpleExpression(value, varRef);
        }

        if (expr instanceof CallExpression) {
            CallExpression ce = (CallExpression)expr;
            // standard library application?
            if (ce.getFunction() instanceof StdLibFunctionReference) {
                return convertStdlibFuncApplication(ce, ctxt, currentFile);
            }
            // user function application?
            if (ce.getFunction() instanceof FunctionReference) {
                return convertUserFuncApplication(ce, ctxt, currentFile);
            }

            // 'Weird' function call? (through a function variable or a
            // computed value)
            if (ce.getFunction().getType() instanceof FunctionType) {
                return convertComputedFuncApplication(ce, ctxt, currentFile);
            }

            // Process instance.
            if (ce.getFunction().getType() instanceof ProcessType) {
                return convertProcessApplication(ce, ctxt, currentFile);
            }

            // Model execution (that is, a sub-simulation)
            check(ce.getFunction().getType() instanceof ModelType);
            return convertSubSimulation(ce, ctxt, currentFile);
        }

        if (expr instanceof BinaryExpression) {
            // For projection, let the lhs handle it.
            BinaryExpression bin = (BinaryExpression)expr;
            if (bin.getOp().equals(BinaryOperators.PROJECTION)) {
                TypeID tid = createTypeID(bin.getLeft().getType(), ctxt);
                return tid.convertExprNode(expr, ctxt, currentFile);
            }
            // For 'element of' or 'subset', let the right operator handle it.
            if (bin.getOp().equals(BinaryOperators.SUBSET) || bin.getOp().equals(BinaryOperators.ELEMENT_TEST)) {
                TypeID tid = createTypeID(bin.getRight().getType(), ctxt);
                return tid.convertExprNode(expr, ctxt, currentFile);
            }
        }

        if (expr instanceof UnaryExpression) {
            UnaryExpression unexpr = (UnaryExpression)expr;
            if (unexpr.getOp().equals(UnaryOperators.SAMPLE)) {
                return convertSampleExpression(unexpr, ctxt, currentFile);
            }
        }

        if (expr instanceof CastExpression) {
            return convertCastExpression((CastExpression)expr, ctxt, currentFile);
        }

        if (expr instanceof ReadCallExpression) {
            ReadCallExpression rce = (ReadCallExpression)expr;
            return convertReadCallExpression(rce, ctxt, currentFile);
        }

        // Default case: Let the type handle the conversion.
        TypeID tid = createTypeID(expr.getType(), ctxt);
        return tid.convertExprNode(expr, ctxt, currentFile);
    }

    /**
     * Convert the read() expression.
     *
     * @param expr Expression to convert.
     * @param ctxt Code generator context.
     * @param currentFile Current Java class targeted for code generation.
     * @return The converted expression.
     */
    private static ExpressionBase convertReadCallExpression(ReadCallExpression expr, CodeGeneratorContext ctxt,
            JavaFile currentFile)
    {
        List<String> lines = list();
        String fileValue;
        if (expr.getFile() == null) {
            fileValue = "chiCoordinator.getStdin()";
        } else {
            ExpressionBase fileExpr = convertExpression(expr.getFile(), ctxt, currentFile);
            lines.addAll(fileExpr.getCode());
            fileValue = fileExpr.getValue();
        }
        TypeID tid = createTypeID(expr.getLoadType(), ctxt);
        String line = tid.getReadName(fileValue, currentFile);
        return makeExpression(lines, line, expr);
    }

    /**
     * Convert the cast expression.
     *
     * @param ce Expression to convert.
     * @param ctxt Code generator context.
     * @param currentFile Current Java class targeted for code generation.
     * @return The converted expression.
     */
    private static ExpressionBase convertCastExpression(CastExpression ce, CodeGeneratorContext ctxt,
            JavaFile currentFile)
    {
        ExpressionBase child = convertExpression(ce.getExpression(), ctxt, currentFile);
        Type chTp = dropReferences(ce.getExpression().getType());
        Type resTp = dropReferences(ce.getCastType());
        String line;
        if (chTp instanceof BoolType) {
            check(resTp instanceof StringType);
            line = fmt("(%s) ? \"true\" : \"false\"", child.getValue());
            return makeExpression(child.getCode(), line, ce);
        } else if (chTp instanceof IntType) {
            if (resTp instanceof StringType) {
                line = fmt("String.valueOf(%s)", child.getValue());
                return makeExpression(child.getCode(), line, ce);
            } else if (resTp instanceof RealType) {
                line = fmt("(double)(%s)", child.getValue());
                return makeExpression(child.getCode(), line, ce);
            }
        } else if (chTp instanceof RealType) {
            if (resTp instanceof StringType) {
                line = fmt("String.valueOf(%s)", child.getValue());
                return makeExpression(child.getCode(), line, ce);
            } else if (resTp instanceof TimerType) {
                List<String> lines = list();
                lines.addAll(child.getCode());
                String tim = ctxt.makeUniqueName("timer");
                line = fmt("Timer %s = new Timer(chiCoordinator, chiCoordinator.getCurrentTime() + (%s));", tim,
                        child.getValue());
                lines.add(line);
                currentFile.addImport(TIMER_FQC, false);
                return makeExpression(lines, tim, ce);
            }
        } else if (chTp instanceof StringType) {
            List<String> lines = list();
            lines.addAll(child.getCode());
            String mem = ctxt.makeUniqueName("mem");
            lines.add("ChiReadMemoryFile " + mem + " = new ChiReadMemoryFile(" + child.getValue() + ");");
            currentFile.addImport(CHI_READ_MEMORY_FILE_FQC, false);

            TypeID tid = createTypeID(resTp, ctxt);
            String var = ctxt.makeUniqueName("var");
            line = fmt("%s %s = %s;", tid.getJavaType(), var, tid.getReadName(mem, currentFile));
            lines.add(line);
            return makeExpression(lines, var, ce);
        } else if (chTp instanceof TimerType) {
            check(resTp instanceof RealType);
            line = fmt("(%s).getRemaining()", child.getValue());
            return makeExpression(child.getCode(), line, ce);
        } else if (chTp instanceof ListType) {
            if (resTp instanceof SetType) {
                SetType setType = (SetType)resTp;
                TypeID setTid = createTypeID(resTp, ctxt);
                String setVar = ctxt.makeUniqueName("set");
                TypeID listTid = createTypeID(chTp, ctxt);
                String listVar = ctxt.makeUniqueName("list");

                List<String> lines = list();
                lines.addAll(child.getCode());
                line = fmt("%s %s = %s;", listTid.getJavaType(), listVar, child.getValue());
                lines.add(line);
                line = fmt("%s %s = new %s(chiCoordinator, %s.size());", setTid.getJavaType(), setVar,
                        setTid.getJavaClassType(), listVar);
                lines.add(line);
                TypeID elmTid = createTypeID(setType.getElementType(), ctxt);
                line = fmt("for (%s x: %s) %s.add(x);", elmTid.getJavaType(), listVar, setVar);
                lines.add(line);
                return makeExpression(lines, setVar, ce);
            }
            check(resTp instanceof MatrixType);
            MatrixType mType = (MatrixType)resTp;

            TypeID tid = createTypeID(resTp, ctxt);
            String matClass = tid.getJavaClassType();
            currentFile.addImport(matClass, false);
            matClass = matClass.substring(matClass.lastIndexOf('.') + 1);
            String var = ctxt.makeUniqueName("mat");
            List<String> lines = list();
            lines.addAll(child.getCode());
            line = fmt("%s %s = new %s(%d, %d);", matClass, var, matClass, MatrixTypeID.getValue(mType.getRowSize()),
                    MatrixTypeID.getValue(mType.getColumnSize()));
            lines.add(line);
            line = fmt("%s.loadList(%s);", var, child.getValue());
            lines.add(line);
            return makeExpression(lines, var, ce);
        }
        fail("Unexpected type of child of cast expression found");
        return null;
    }

    /**
     * Generate code for a call through a function object (A variable holding a function, or a function returning a
     * function).
     *
     * @param ce Call expression.
     * @param ctxt Code generator context.
     * @param currentFile Current Java class targeted for code generation.
     * @return Code to execute to apply the function.
     */
    private static ExpressionBase convertComputedFuncApplication(CallExpression ce, CodeGeneratorContext ctxt,
            JavaFile currentFile)
    {
        ExpressionBase fnc = convertExpression(ce.getFunction(), ctxt, currentFile);
        List<String> lines = list();
        lines.addAll(fnc.getCode());
        String argText = "positionStack";
        for (Expression arg: ce.getArguments()) {
            ExpressionBase exprBase = convertExpression(arg, ctxt, currentFile);
            lines.addAll(exprBase.getCode());
            if (!argText.isEmpty()) {
                argText += ", ";
            }
            argText += exprBase.getValue();
        }

        String text = fmt("%s.compute(%s)", fnc.getValue(), argText);
        return makeExpression(lines, text, ce);
    }

    /**
     * Generate code for starting a new process.
     *
     * @param ce Call expression.
     * @param ctxt Code generator context.
     * @param currentFile Current Java class targeted for code generation.
     * @return Code to execute to start the process.
     */
    private static ExpressionBase convertProcessApplication(CallExpression ce, CodeGeneratorContext ctxt,
            JavaFile currentFile)
    {
        List<String> lines = list();

        String pName;
        // Normal case: Direct instantiation of a process definition
        if (ce.getFunction() instanceof ProcessReference) {
            ProcessDeclaration pd = ((ProcessReference)ce.getFunction()).getProcess();
            pName = "new " + ctxt.getDefinition(pd);
        } else {
            // General case: Process definition variable or a computed value.
            ExpressionBase fnc = convertExpression(ce.getFunction(), ctxt, currentFile);
            lines.addAll(fnc.getCode());
            pName = fnc.getValue() + ".create";
        }
        String argText = "spec, chiCoordinator";
        for (Expression arg: ce.getArguments()) {
            ExpressionBase exprBase = convertExpression(arg, ctxt, currentFile);
            lines.addAll(exprBase.getCode());
            argText += ", " + exprBase.getValue();
        }

        String text = fmt("%s(%s)", pName, argText);
        return makeExpression(lines, text, ce);
    }

    /**
     * Generate code for 'executing' a model (that is, perform a sub-simulation).
     *
     * @param ce Call expression.
     * @param ctxt Code generator context.
     * @param currentFile Current Java class targeted for code generation.
     * @return Code to execute the model.
     */
    private static ExpressionBase convertSubSimulation(CallExpression ce, CodeGeneratorContext ctxt,
            JavaFile currentFile)
    {
        List<String> lines = list();
        // Construct the model instance.
        String argText = "spec, chiCoordinator";
        for (Expression arg: ce.getArguments()) {
            ExpressionBase code = convertExpression(arg, ctxt, currentFile);
            lines.addAll(code.getCode());
            argText += ", " + code.getValue();
        }
        // Model instance is always a direct model reference
        // (Chi has no 'model' type or variable).
        Assert.check(ce.getFunction() instanceof ModelReference);
        ModelDeclaration md = ((ModelReference)ce.getFunction()).getModel();
        String modelVar = ctxt.makeUniqueName("mdl");
        String line = fmt("BaseProcess %s = new %s(%s);", modelVar, ctxt.getDefinition(md), argText);
        lines.add(line);

        // Generate 'execution' of the model.
        String resVar = ctxt.makeUniqueName("rslt");
        line = fmt("Object %s = chiCoordinator.runSubSimulation(%s);", resVar, modelVar);
        lines.add(line);

        // Generate a cast to the expected exit type.
        TypeID tid = createTypeID(md.getReturnType(), ctxt);
        line = "((" + tid.getJavaClassType() + ")" + resVar + ")";
        return new CodeExpression(lines, line, ce);
    }

    /**
     * Generate code to call a user-defined function.
     *
     * @param ce Call expression.
     * @param ctxt Code generator context.
     * @param currentFile Current Java class targeted for code generation.
     * @return Code to execute to apply the function.
     */
    private static ExpressionBase convertUserFuncApplication(CallExpression ce, CodeGeneratorContext ctxt,
            JavaFile currentFile)
    {
        FunctionReference fr = (FunctionReference)ce.getFunction();
        String funcName = ctxt.getDefinition(fr.getFunction());

        List<String> lines = list();
        String argText = "positionStack";
        for (Expression arg: ce.getArguments()) {
            ExpressionBase exprBase = convertExpression(arg, ctxt, currentFile);
            lines.addAll(exprBase.getCode());
            if (!argText.isEmpty()) {
                argText += ", ";
            }
            argText += exprBase.getValue();
        }

        String text = fmt("spec.instance%s.compute(%s)", funcName, argText);
        return makeExpression(lines, text, ce);
    }

    /**
     * Function call of a standard library function.
     *
     * @param ce Call expression.
     * @param ctxt Code generator context.
     * @param currentFile Current Java class targeted for code generation.
     * @return Converted expression performing the function application to the standard library.
     */
    private static ExpressionBase convertStdlibFuncApplication(CallExpression ce, CodeGeneratorContext ctxt,
            JavaFile currentFile)
    {
        final String FAST_MATH = "org.apache.commons.math3.util.FastMath";

        StdLibFunctionReference sref = (StdLibFunctionReference)ce.getFunction();
        List<ExpressionBase> argExprs = list();
        String text = "";
        for (Expression arg: ce.getArguments()) {
            ExpressionBase eb = convertExpression(arg, ctxt, currentFile);
            if (!text.isEmpty()) {
                text += ", ";
            }
            text += eb.getValue();
            argExprs.add(eb);
        }
        List<String> lines = list();
        for (ExpressionBase exprBase: argExprs) {
            lines.addAll(exprBase.getCode());
        }

        switch (sref.getFunction()) {
            case ABS:
                text = fmt("Math.abs(%s)", text);
                break;

            case ACOS:
                text = fmt("acos(%s)", text);
                currentFile.addImport(FAST_MATH + ".acos", true);
                break;

            case ACOSH:
                text = fmt("acosh(%s)", text);
                currentFile.addImport(FAST_MATH + ".acosh", true);
                break;

            case ASIN:
                text = fmt("asin(%s)", text);
                currentFile.addImport(FAST_MATH + ".asin", true);
                break;

            case ASINH:
                text = fmt("asinh(%s)", text);
                currentFile.addImport(FAST_MATH + ".asinh", true);
                break;

            case ATAN:
                text = fmt("atan(%s)", text);
                currentFile.addImport(FAST_MATH + ".atan", true);
                break;

            case ATANH:
                text = fmt("atanh(%s)", text);
                currentFile.addImport(FAST_MATH + ".atanh", true);
                break;

            case CBRT:
                text = fmt("cbrt(%s)", text);
                currentFile.addImport(FAST_MATH + ".cbrt", true);
                break;

            case CEIL:
                text = fmt("(int)ceil(%s)", text);
                currentFile.addImport(FAST_MATH + ".ceil", true);
                break;

            case COS:
                text = fmt("cos(%s)", text);
                currentFile.addImport(FAST_MATH + ".cos", true);
                break;

            case COSH:
                text = fmt("cosh(%s)", text);
                currentFile.addImport(FAST_MATH + ".cosh", true);
                break;

            case DICT_KEYS:
                text = fmt("(%s).getKeyList()", text);
                break;

            case DICT_VALUES:
                text = fmt("(%s).getValueList()", text);
                break;

            case EMPTY:
                text = fmt("(%s).isEmpty()", text);
                break;

            case ENUMERATE: {
                check(ce.getArguments().size() == 1);
                TypeID tid = createTypeID(ce.getArguments().get(0).getType(), ctxt);
                text = getEnumerateMethod(tid, ctxt) + "(chiCoordinator, " + text + ")";
                break;
            }

            case EXP:
                text = fmt("Math.exp(%s)", text);
                break;

            case FINISHED:
                text = fmt("(%s).isFinished()", text);
                break;

            case FLOOR:
                text = fmt("(int)Math.floor(%s)", text);
                break;

            case INSERT: {
                // insert(list, element, pred) -> list
                TypeID tid = createTypeID(ce.getArguments().get(0).getType(), ctxt);
                text = fmt("%s.insert(%s)", tid.getJavaClassType(), text);
                break;
            }
            case SORT: {
                // sort(list, pred) -> list
                TypeID tid = createTypeID(ce.getArguments().get(0).getType(), ctxt);
                text = fmt("%s.sort(%s)", tid.getJavaClassType(), text);
                break;
            }

            case SIZE: {
                String funcName = "size";
                Type tp = dropReferences(ce.getArguments().get(0).getType());
                if (tp instanceof StringType) {
                    funcName = "length";
                }
                text = fmt("(%s).%s()", text, funcName);
                break;
            }

            case LN:
                text = fmt("log(%s)", text);
                currentFile.addImport(FAST_MATH + ".log", true);
                break;

            case LOG:
                text = fmt("log10(%s)", text);
                currentFile.addImport(FAST_MATH + ".log10", true);
                break;

            case MIN:
            case MAX: {
                if (ce.getArguments().size() == 1) {
                    String fnName = (sref.getFunction() == StdLibFunctions.MIN) ? "getMinimum" : "getMaximum";
                    text = fmt("(%s).%s()", text, fnName);
                    break;
                } else if (ce.getArguments().size() >= 2) {
                    Type resType = dropReferences(ce.getType());
                    TypeID resTid = createTypeID(resType, ctxt);
                    String resVar = ctxt.makeUniqueName("res");
                    String tempVar = ctxt.makeUniqueName("tmp");

                    // Add variable declarations.
                    String line = fmt("%s %s, %s;", resTid.getJavaType(), resVar, tempVar);
                    lines.add(line);

                    // Generate the line to merge a temporary value into the
                    // result.
                    String cmpLine;
                    String cmpOp = (sref.getFunction() == StdLibFunctions.MIN) ? "<" : ">";
                    if (resType instanceof StringType) {
                        cmpLine = fmt("%s = %s.compareTo(%s) %s 0 ? %s : %s;", resVar, resVar, tempVar, cmpOp, resVar,
                                tempVar);
                    } else {
                        cmpLine = fmt("%s = %s %s %s ? %s : %s;", resVar, resVar, cmpOp, tempVar, resVar, tempVar);
                    }

                    // Generate the min/max calculation.
                    boolean first = true;
                    for (ExpressionBase arg: argExprs) {
                        if (first) {
                            line = fmt("%s = %s;", resVar, arg.getValue(true));
                            lines.add(line);
                            first = false;
                        } else {
                            line = fmt("%s = %s;", tempVar, arg.getValue(true));
                            lines.add(line);
                            lines.add(cmpLine);
                        }
                    }
                    text = resVar;
                    break;
                }
                fail("Not expecting to get here for MIN/MAX");
                break;
            }

            case OPEN:
                text = fmt("chiCoordinator.openFile(%s, \"text\")", text);
                break;

            case POP:
                text = fmt("(%s).pop()", text);
                break;

            case RANGE: {
                TypeID tid = makeIntTypeID();
                tid = makeListTypeID(tid, ctxt);
                text = fmt("%s.range(chiCoordinator, %s)", tid.getJavaClassType(), text);
                break;
            }

            case ROUND:
                text = fmt("(int)round(%s)", text);
                currentFile.addImport(FAST_MATH + ".round", true);
                break;

            case SIGN:
                text = fmt("(int)signum(%s)", text);
                currentFile.addImport(FAST_MATH + ".signum", true);
                break;

            case SIN:
                text = fmt("sin(%s)", text);
                currentFile.addImport(FAST_MATH + ".sin", true);
                break;

            case SINH:
                text = fmt("sinh(%s)", text);
                currentFile.addImport(FAST_MATH + ".sinh", true);
                break;

            case SQRT:
                text = fmt("sqrt(%s)", text);
                currentFile.addImport(FAST_MATH + ".sqrt", true);
                break;

            case TAN:
                text = fmt("tan(%s)", text);
                currentFile.addImport(FAST_MATH + ".tan", true);
                break;

            case TANH:
                text = fmt("tanh(%s)", text);
                currentFile.addImport(FAST_MATH + ".tanh", true);
                break;

            case READY:
                text = fmt("(%s).isReady()", text);
                break;

            case DELETE: {
                TypeID tid = createTypeID(ce.getArguments().get(0).getType(), ctxt);
                text = fmt("%s.removeElement(%s)", tid.getJavaClassType(), text);
                break;
            }

            case BERNOULLI:
                currentFile.addImport(RANDOM_PKG + ".BernoulliDistribution", false);
                text = fmt("new BernoulliDistribution(chiCoordinator, %s)", text);
                break;

            case BETA:
                currentFile.addImport(RANDOM_PKG + ".BetaDistribution", false);
                text = fmt("new BetaDistribution(chiCoordinator, %s)", text);
                break;

            case BINOMIAL:
                currentFile.addImport(RANDOM_PKG + ".BinomialDistribution", false);
                text = fmt("new BinomialDistribution(chiCoordinator, %s)", text);
                break;

            case CONSTANT: {
                Type etp = dropReferences(ce.getType());
                check(etp instanceof DistributionType);
                DistributionType dt = (DistributionType)etp;
                etp = dropReferences(dt.getResultType());
                String name;
                if (etp instanceof BoolType) {
                    name = Constants.CONSTANT_BOOL_DISTRIBUTION_CLASSNAME;
                } else if (etp instanceof IntType) {
                    name = Constants.CONSTANT_INT_DISTRIBUTION_CLASSNAME;
                } else if (etp instanceof RealType) {
                    name = Constants.CONSTANT_DOUBLE_DISTRIBUTION_CLASSNAME;
                } else {
                    fail("Unexpected elmtype of a constant distr");
                    name = "NOT_REACHED";
                }
                currentFile.addImport(RANDOM_PKG + "." + name, false);
                text = fmt("new %s(chiCoordinator, %s)", name, text);
                break;
            }

            case EOF:
                text = fmt("chiCoordinator.checkEof(%s)", text);
                break;

            case EOL:
                text = fmt("chiCoordinator.checkEol(%s)", text);
                break;

            case NEWLINES:
                text = fmt("chiCoordinator.getNewlines(%s)", text);
                break;

            case ERLANG:
                currentFile.addImport(RANDOM_PKG + ".ErlangDistribution", false);
                text = fmt("new ErlangDistribution(chiCoordinator, %s)", text);
                break;

            case EXPONENTIAL:
                currentFile.addImport(RANDOM_PKG + ".ExponentialDistribution", false);
                text = fmt("new ExponentialDistribution(chiCoordinator, %s)", text);
                break;

            case GAMMA:
                currentFile.addImport(RANDOM_PKG + ".GammaDistribution", false);
                text = fmt("new GammaDistribution(chiCoordinator, %s)", text);
                break;

            case GEOMETRIC:
                currentFile.addImport(RANDOM_PKG + ".GeometricDistribution", false);
                text = fmt("new GeometricDistribution(chiCoordinator, %s)", text);
                break;

            case LOG_NORMAL:
                currentFile.addImport(RANDOM_PKG + ".LogNormalDistribution", false);
                text = fmt("new LogNormalDistribution(chiCoordinator, %s)", text);
                break;

            case NORMAL:
                currentFile.addImport(RANDOM_PKG + ".NormalDistribution", false);
                text = fmt("new NormalDistribution(chiCoordinator, %s)", text);
                break;

            case POISSON:
                currentFile.addImport(RANDOM_PKG + ".PoissonDistribution", false);
                text = fmt("new PoissonDistribution(chiCoordinator, %s)", text);
                break;

            case RANDOM:
                currentFile.addImport(RANDOM_PKG + ".RandomDistribution", false);
                Assert.check(text.isEmpty());
                text = "new RandomDistribution(chiCoordinator)";
                break;

            case TRIANGLE:
                currentFile.addImport(RANDOM_PKG + ".TriangleDistribution", false);
                text = fmt("new TriangleDistribution(chiCoordinator, %s)", text);
                break;

            case UNIFORM: {
                Type etp = dropReferences(ce.getType());
                check(etp instanceof DistributionType);
                DistributionType dt = (DistributionType)etp;
                etp = dropReferences(dt.getResultType());
                String name;
                if (etp instanceof IntType) {
                    name = "IntegerUniformDistribution";
                } else if (etp instanceof RealType) {
                    name = "DoubleUniformDistribution";
                } else {
                    fail("Unexpected element type of a uniform distribution");
                    name = "NOT_REACHED";
                }
                currentFile.addImport(RANDOM_PKG + "." + name, false);
                text = fmt("new %s(chiCoordinator, %s)", name, text);
                break;
            }

            case WEIBULL:
                currentFile.addImport(RANDOM_PKG + ".WeibullDistribution", false);
                text = fmt("new WeibullDistribution(chiCoordinator, %s)", text);
                break;
        }
        return makeExpression(lines, text, ce);
    }

    /**
     * Convert the 'sample x' unary expression.
     *
     * @param unexpr The sample expression.
     * @param ctxt Code generator context.
     * @param currentFile Current Java class targeted for code generation.
     * @return Translated expression.
     */
    private static ExpressionBase convertSampleExpression(UnaryExpression unexpr, CodeGeneratorContext ctxt,
            JavaFile currentFile)
    {
        ExpressionBase chExpr = convertExpression(unexpr.getChild(), ctxt, currentFile);
        String val = fmt("(%s).sample()", chExpr.getValue());
        return makeExpression(chExpr.getCode(), val, unexpr);
    }

    /**
     * Get the name of the enumerate method that enumerates over the given type. If necessary, construct the method.
     *
     * @param tid Type to iterate over.
     * @param ctxt Code generator context.
     * @return The fully qualified name of the method that implements the enumerate function of the given type.
     */
    public static String getEnumerateMethod(TypeID tid, CodeGeneratorContext ctxt) {
        String fullMethodName = ctxt.getEnumerateName(tid);
        if (fullMethodName != null) {
            return fullMethodName;
        }

        String clsName = ctxt.makeUniqueName("Enumerate");
        fullMethodName = clsName + ".enumerate";
        ctxt.addEnumerateName(tid, fullMethodName);

        TypeID elmTid;
        switch (tid.kind) {
            case DICTIONARY: {
                List<String> names = list("key", "value");
                check(tid.subTypes.size() == 2);
                elmTid = TypeIDCreation.createTupleTypeID(names, tid.subTypes, ctxt);
                break;
            }

            case LIST:
            case SET:
                elmTid = tid.subTypes.get(0);
                break;

            default:
                fail("Unknown type of enumerate source");
                return null; // Never reached.
        }

        List<String> names = list("index", "value");
        List<TypeID> tids = list(makeIntTypeID(), elmTid);
        TypeID tupTid = TypeIDCreation.createTupleTypeID(names, tids, ctxt);
        TypeID retTid = makeListTypeID(tupTid, ctxt);

        JavaClass jc = new JavaClass(null, false, clsName, null, null);
        ctxt.addClass(jc);

        String retClassname = retTid.getJavaClassType();
        JavaMethod jm = new JavaMethod("public static", retClassname, "enumerate",
                "ChiCoordinator chiCoordinator, " + tid.getJavaType() + " source", null);
        jc.addImport(Constants.COORDINATOR_FQC, false);
        jm.lines.add("%s result = new %s(chiCoordinator);", retClassname, retClassname);
        jm.lines.add("int i = 0;");
        jm.lines.add("for (%s elm: source) {", elmTid.getJavaClassType());
        String tupClsName = tupTid.getJavaClassType();
        jm.lines.add("    %s tup = new %s(chiCoordinator);", tupClsName, tupClsName);
        jm.lines.add("    tup.var0 = i;");
        jm.lines.add("    tup.var1 = elm;");
        jm.lines.add("    result.append(tup);");
        jm.lines.add("    i++;");
        jm.lines.add("}");
        jm.lines.add("return result;");
        jc.addMethod(jm);

        return fullMethodName;
    }
}
