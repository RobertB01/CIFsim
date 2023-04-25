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

package org.eclipse.escet.cif.plcgen.conversion.expressions;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.cif2plc.plcdata.PlcVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.statements.PlcStatement;
import org.eclipse.escet.common.java.Assert;

/**
 * Storage of a converted CIF expression.
 *
 * <p>
 * The CIF language has a very rich set of expression constructs, it is much larger set than the PLC expression
 * constructs. For this reason, a converted CIF expression cannot always be a PLC expression. It may first need to
 * perform code to derive the value.
 * </p>
 *
 * <p>
 * The {@link ExprGenResult} encapsulates this need. It has
 * <ul>
 * <li>optional {@link #code}, statements that need to be executed before reading the {@link #value} of this result,
 * </li>
 * <li>optional {@link #codeVariables}, temporary variables used in the {@link #code},</li>
 * <li>a {@link #value}, an expression that can be evaluated to obtain the final result value of the converted CIF
 * expression, and</li>
 * <li>optional {@link #valueVariables}, temporary variables assigned from the {@link #code} fragment and used in the
 * {@link #value} expression.</li>
 * </ul>
 * To construct an {@link ExprGenResult} instance, an expression expressing the final value must be constructed and
 * stored in {@link #value}. If the former expression needs dynamically computed values, the {@link #code} part can be
 * used to store statements for that purpose.
 * </p>
 * <p>
 * As the code computes values needed in the final value, intermediate variables are needed to transfer computed values
 * from the statement to the moment of evaluating the final value. These variables should be stored in
 * {@link #valueVariables} and be exclusively reserved for this purpose before the code is executed until after
 * evaluating the {@link #value} expression. In addition, the code may need additional variables for its internal flow.
 * These should be stored in {@link #codeVariables}, and be exclusively reserved for this purpose before the code is
 * executed until execution of that code has finished.
 * </p>
 *
 * <p>
 * To obtain the final value of the instance in the PLC, the following steps should be followed.
 * <ol>
 * <li>Execute the statements in {@link #code} if it exists. Doing this may change the {@link #codeVariables} and the
 * {@link #valueVariables}. As these variables should be reserved for this purpose at the time of obtaining the
 * {@link ExprGenResult} instance, there is no need to take care of reserving them beforehand.</li>
 * <li>After the {@link #code} has been executed, variables stored in the {@link #codeVariables} are not needed any more
 * to obtain the final value. They should be released by calling {@link #releaseCodeVariables}.</li>
 * <li>Obtain the final value by evaluating the {@link #value} expression. Use the final value or store it somewhere
 * safe.</li>
 * <li>After the final value is used or safely stored, the temporary values in {@link #valueVariables} are not needed
 * any more, they should be released by calling {@link #releaseValueVariables}.</li>
 * </ol>
 * </p>
 */
public class ExprGenResult {
    /** The expression generator managing the temporary variables that have been added here. */
    private final ExprGenerator generator;

    /**
     * Temporary variables that are used in {@link #code the PLC code}. They should be released for reuse after
     * inserting the {@link #code} in its execution context. In that way, the variables will not have important values
     * for other parts in the generated result.
     */
    public Set<PlcVariable> codeVariables = set();

    /** Code to perform before evaluating the {@link #value}. */
    public List<PlcStatement> code = list();

    /**
     * Temporary variables that are assigned in {@link #code the PLC code} and used in evaluating the {@link #value
     * result value}. They should be released for reuse after evaluating the {@link #value} expression in the generated
     * result. In that way, the variables will not have important values for other parts in the generated result.
     */
    public Set<PlcVariable> valueVariables = set();

    /**
     * Expression to evaluate after running the {@link #code} if it exists. The {@link #valueVariables} should be
     * released for reuse afterwards.
     *
     * <p>
     * While the value can be set directly, it is generally recommended to use the {@link #setValue} daisy-chain method
     * to construct an {@link ExprGenResult} instance.
     * </p>
     */
    public PlcExpression value = null;

    /**
     * Constructor of the {@link ExprGenResult} class.
     *
     * @param generator Expression generator managing the temporary variables.
     * @param parentResults Results of child sub-expressions. Their code, code variables and value variables are copied
     *     into this result in the specified order.
     */
    public ExprGenResult(ExprGenerator generator, ExprGenResult... parentResults) {
        this.generator = generator;

        for (ExprGenResult parentResult: parentResults) {
            mergeCodeAndVariables(parentResult);
        }
    }

    /**
     * Append the code variables, code, and value variables of the parent result to this result.
     *
     * @param parentResult Source of the code variables, code, and value variables to copy.
     */
    public void mergeCodeAndVariables(ExprGenResult parentResult) {
        mergeCodeVariables(parentResult);
        mergeCode(parentResult);
        mergeValueVariables(parentResult);
    }

    /**
     * Append code variables of the parent result to this result.
     *
     * @param parentResult Source of the code variables to copy.
     */
    public void mergeCodeVariables(ExprGenResult parentResult) {
        codeVariables.addAll(parentResult.codeVariables);
    }

    /**
     * Append code of the parent result to this result.
     *
     * @param parentResult Source of the code to copy.
     */
    public void mergeCode(ExprGenResult parentResult) {
        code.addAll(PlcStatement.copy(parentResult.code));
    }

    /**
     * Append values variables of the parent result to this result.
     *
     * @param parentResult Source of the value variables to copy.
     */
    public void mergeValueVariables(ExprGenResult parentResult) {
        valueVariables.addAll(parentResult.valueVariables);
    }

    /**
     * Does the result have code that should be executed before evaluating {@link #value}?
     *
     * @return Whether code exists that should be executed before evaluating {@link #value}.
     */
    public boolean hasCode() {
        Assert.implies(code.isEmpty(), codeVariables.isEmpty());
        return !code.isEmpty();
    }

    /**
     * Whether the result owns temporary variables needed to execute the {@link #code} part.
     *
     * @return Whether the {@link #code} uses temporary variables to obtain the {@link #value expression value}.
     */
    public boolean hasCodeVariables() {
        Assert.implies(code.isEmpty(), codeVariables.isEmpty());
        return !codeVariables.isEmpty();
    }

    /**
     * Release the variables used in the {@link #code} of this result. After release the variables may be used for other
     * purposes, executing the {@link #code} at that time may have unintended side-effects.
     */
    public void releaseCodeVariables() {
        generator.releaseTempVariables(codeVariables);
        codeVariables.clear();
    }

    /**
     * Whether the expression generation result has temporary variables needed to evaluate the {@link #value} after
     * executing the {@link #code} if it exists.
     *
     * @return Whether the expression generation result has temporary variables needed to evaluate the {@link #value}.
     */
    public boolean hasValueVariables() {
        Assert.implies(value == null, valueVariables.isEmpty());
        return !valueVariables.isEmpty();
    }

    /** Release the variables used in the {@link #value} result. After release, their content may change at any time. */
    public void releaseValueVariables() {
        generator.releaseTempVariables(valueVariables);
        valueVariables.clear();
    }

    /**
     * Set the result value.
     *
     * @param plcExpr Expression to give to the result.
     * @return This result instance for daisy-chaining.
     */
    public ExprGenResult setValue(PlcExpression plcExpr) {
        value = plcExpr;
        return this;
    }
}
