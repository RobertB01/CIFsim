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

package org.eclipse.escet.cif.codegen;

import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.codegen.updates.VariableWrapper;
import org.eclipse.escet.cif.codegen.updates.tree.SingleVariableAssignment;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;

/** Base code generation context. Provides only basic functionality. */
public class CodeContext {
    /** Global shared code generator object. */
    protected final CodeGen codeGen;

    /**
     * Constructor of the {@link CodeContext} class.
     *
     * @param codeGen Global shared code generator object.
     */
    public CodeContext(CodeGen codeGen) {
        this.codeGen = codeGen;
    }

    /**
     * Create a new code storage object for generated code, starting at the left margin.
     *
     * @return The created storage object.
     */
    public MemoryCodeBox makeCodeBox() {
        return makeCodeBox(0);
    }

    /**
     * Create a new code storage object for generated code, starting at the given number of indents from the left
     * margin.
     *
     * @param numIndents Number of initial indents of the stored code.
     * @return The created storage object.
     */
    public MemoryCodeBox makeCodeBox(int numIndents) {
        return codeGen.makeCodeBox(numIndents);
    }

    /**
     * Add code for beginning a new local scope, for an assignment. Should first add code for beginning a new local
     * scope, and then indent for the body of the scope.
     *
     * @param code The code generated so far. Is modified in-place.
     */
    public void addUpdatesBeginScope(CodeBox code) {
        codeGen.addUpdatesBeginScope(code);
    }

    /**
     * Add code for ending a new local scope, for an assignment. Should first dedent for the body of the local scope,
     * and then add code for ending the local scope.
     *
     * @param code The code generated so far. Is modified in-place.
     */
    public void addUpdatesEndScope(CodeBox code) {
        codeGen.addUpdatesEndScope(code);
    }

    /**
     * Generate a target language code fragment for the given predicates, assuming conjunction between the predicates.
     *
     * @param preds The predicates.
     * @return The target language code that represents the given predicates.
     */
    public ExprCode predsToTarget(List<Expression> preds) {
        return codeGen.exprCodeGen.predsToTarget(preds, this);
    }

    /**
     * Generate a target language code fragment for the given expression.
     *
     * @param expr The expression.
     * @param dest Storage destination of the result, or {@code null}.
     * @return The target language code that represents the given expression.
     */
    public ExprCode exprToTarget(Expression expr, Destination dest) {
        return codeGen.exprCodeGen.exprToTarget(expr, dest, this);
    }

    /**
     * Construct a temporary variable for the given variable.
     *
     * @param varInfo Existing variable that needs a copy.
     * @return Variable information of the created variable.
     */
    public VariableInformation makeTempVariable(VariableInformation varInfo) {
        return codeGen.makeTempVariable(varInfo);
    }

    /**
     * Construct a temporary variable for the given type.
     *
     * @param type Type of the temporary variable.
     * @param name Optional name of the temporary variable.
     * @return Variable information of the created variable.
     */
    public VariableInformation makeTempVariable(CifType type, String name) {
        TypeInfo ti = typeToTarget(type);
        return makeTempVariable(ti, name);
    }

    /**
     * Construct a temporary variable for the given type information.
     *
     * @param ti Type of the temporary variable.
     * @param name Optional part of the name of the new variable.
     * @return Variable information of the created variable.
     */
    public VariableInformation makeTempVariable(TypeInfo ti, String name) {
        if (name == null) {
            name = "tmp";
        }
        return codeGen.makeTempVariable(ti, name);
    }

    /**
     * Construct a (writable) destination for a variable declaration.
     *
     * @param decl Declaration to convert.
     * @return Destination of the variable in the target language.
     */
    public Destination makeDestination(Declaration decl) {
        VariableInformation varInfo = getWriteVarInfo(decl);
        return makeDestination(varInfo);
    }

    /**
     * Construct a destination from a variable.
     *
     * @param varInfo Information about the variable.
     * @return Object representing the variable as destination.
     */
    public Destination makeDestination(VariableInformation varInfo) {
        return codeGen.makeDestination(varInfo);
    }

    /**
     * Reserve currently used numbers of temporary variables. These numbers will not be used again, until the
     * corresponding {@link #unreserveTempVariables} has been executed.
     *
     * @return Range of reserved variables, to be used for unreserving.
     */
    public int reserveTempVariables() {
        return codeGen.reserveTempVariables();
    }

    /**
     * Make a target-specific data value for the given value.
     *
     * @param value The value.
     * @return The target-specific data value.
     */
    public DataValue makeDataValue(String value) {
        return codeGen.makeDataValue(value);
    }

    /**
     * Release the top range of the used numbers of temporary variables. After unreserving, the numbers up-to the
     * previously reserved range are used again.
     *
     * @param reservedValue Range of reserved variables to release, must be equal to returned value from the last call
     *     to {@link #reserveTempVariables}.
     * @see #reserveTempVariables
     */
    public void unreserveTempVariables(int reservedValue) {
        codeGen.unreserveTempVariables(reservedValue);
    }

    /**
     * Get the number of created local variables since the last {@link #reserveTempVariables} call.
     *
     * @return The number of created variables since the last reserve call.
     */
    public int countCreatedTempVariables() {
        return codeGen.countCreatedTempVariables();
    }

    /**
     * Perform an assignment to a variable, where the right hand side is required only one time.
     *
     * @param code Storage of generated code.
     * @param asgn Assignment to perform.
     * @param value Right hand side value.
     * @param readCtxt Code context for right hand side and index projections at the left hand side.
     */
    public void performSingleAssign(CodeBox code, SingleVariableAssignment asgn, Expression value,
            CodeContext readCtxt)
    {
        codeGen.performSingleAssign(code, asgn, value, readCtxt, this);
    }

    /**
     * Perform an assignment to a variable, where the right hand side is stored in a temporary variable.
     *
     * @param code Storage of generated code.
     * @param asgn Assignment to perform.
     * @param rhsText Right hand side value.
     * @param readCtxt Code context for right hand side and index projections at the left hand side.
     */
    public void performAssign(CodeBox code, SingleVariableAssignment asgn, String rhsText, CodeContext readCtxt) {
        codeGen.performAssign(code, asgn, rhsText, readCtxt, this);
    }

    /**
     * Get the set of invalidated algebraic variables and derivative expressions when updating a variable.
     *
     * @param v Variable being updated.
     * @return Set of invalidated algebraic variables and derivative expressions.
     */
    public Set<VariableWrapper> getAffectedAlgebraicDerivativeExpressions(VariableWrapper v) {
        return codeGen.getAffectedAlgebraicDerivativeExpressions(v);
    }

    /**
     * Retrieve variable info from a declaration, for reading the variable.
     *
     * @param var Variable to inspect.
     * @return The variable information for reading the variable.
     */
    public VariableInformation getReadVarInfo(VariableWrapper var) {
        return codeGen.getVarInfo(var.decl, this);
    }

    /**
     * Get the expression code to read the value of a writable variable, including implicit writable algebraic and
     * derivative expression variables.
     *
     * @param var Variable to read.
     * @return Code to read the variable.
     */
    public ExprCode getReadVariableCode(VariableWrapper var) {
        if (var.decl instanceof DiscVariable) {
            DiscVariable discVar = (DiscVariable)var.decl;
            return codeGen.exprCodeGen.convertDiscVariableExpression(discVar, null, this);
        } else if (var.decl instanceof ContVariable) {
            ContVariable contVar = (ContVariable)var.decl;
            return codeGen.exprCodeGen.convertContVariableExpression(contVar, var.isDerivative, null, this);
        } else if (var.decl instanceof AlgVariable) {
            AlgVariable algVar = (AlgVariable)var.decl;
            return codeGen.exprCodeGen.convertAlgVariableExpression(algVar, null, this);
        } else {
            throw new RuntimeException("Unexpected variable read request encountered: " + str(var.decl));
        }
    }

    /**
     * Retrieve variable info from a declaration, for writing the variable.
     *
     * @param decl Declaration to inspect.
     * @return The variable information for writing the value.
     */
    public VariableInformation getWriteVarInfo(Declaration decl) {
        // Basic case uses same variable for both read and write.
        VariableInformation varInfo = codeGen.getVarInfo(decl, this);
        return varInfo;
    }

    /**
     * Get the name of an internal function in the target language.
     *
     * @param func Function to name.
     * @return The name of the function in the target language.
     */
    public String getFunctionName(InternalFunction func) {
        return codeGen.getTargetRef(func);
    }

    /**
     * Get the original name of an internal function in CIF.
     *
     * @param func Function to name.
     * @return The name of the function in CIF.
     */
    public String getOrigFunctionName(InternalFunction func) {
        String origName = codeGen.origDeclNames.get(func);
        if (origName == null) {
            // May be a function introduced for the default initial value of a discrete variable.
            origName = func.getName();
        }
        return origName;
    }

    /**
     * Convert a CIF type to a target language type information object.
     *
     * @param type CIF type to convert to the target language.
     * @return The converted target language type information object.
     */
    public TypeInfo typeToTarget(CifType type) {
        return codeGen.typeCodeGen.typeToTarget(type, this);
    }

    /**
     * Retrieve the code generation prefix.
     *
     * @return The prefix used by the generated code.
     */
    public String getPrefix() {
        return codeGen.replacements.get("prefix");
    }

    /**
     * Append text to a replacement.
     *
     * @param replName Replacement name to append to.
     * @param additionalText Additional text to append.
     */
    public void appendReplacement(String replName, String additionalText) {
        if (additionalText.isEmpty()) {
            return;
        }

        String existingText = codeGen.replacements.get(replName);
        if (existingText == null || existingText.isEmpty()) {
            codeGen.replacements.put(replName, additionalText);
        } else {
            codeGen.replacements.put(replName, existingText + "\n" + additionalText);
        }
    }

    /**
     * Get a fresh generator for update 'if' statements in the target language.
     *
     * @return The created target language 'if' statement generator.
     */
    public IfElseGenerator getIfElseUpdateGenerator() {
        return codeGen.getIfElseUpdateGenerator();
    }
}
