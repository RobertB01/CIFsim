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

package org.eclipse.escet.tooldef.typechecker;

import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.str;
import static org.eclipse.escet.tooldef.common.ToolDefTextUtils.getAbsName;
import static org.eclipse.escet.tooldef.common.ToolDefTextUtils.typeToStr;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.isSubType;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.makeTupleType;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.makeTupleTypeFromValues;
import static org.eclipse.escet.tooldef.common.ToolDefTypeUtils.normalizeType;
import static org.eclipse.escet.tooldef.typechecker.TypeHints.NO_HINTS;
import static org.eclipse.escet.tooldef.typechecker.TypesChecker.NON_NULLABLE_BOOL_HINT;
import static org.eclipse.escet.tooldef.typechecker.TypesChecker.NON_NULLABLE_BOOL_TYPE;
import static org.eclipse.escet.tooldef.typechecker.TypesChecker.NON_NULLABLE_INT_HINT;
import static org.eclipse.escet.tooldef.typechecker.TypesChecker.NON_NULLABLE_INT_TYPE;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.common.typechecker.SemanticException;
import org.eclipse.escet.tooldef.common.ToolDefTextUtils;
import org.eclipse.escet.tooldef.common.ToolDefTypeUtils;
import org.eclipse.escet.tooldef.metamodel.tooldef.Script;
import org.eclipse.escet.tooldef.metamodel.tooldef.Tool;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.Expression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ProjectionExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolParamExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.TupleExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.VariableExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.AddressableDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.AssignmentStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.BreakStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ContinueStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ElifStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ExitStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ForStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.IfStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ReturnStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Statement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.TupleAddressableDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.Variable;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.VariableAddressableDecl;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.WhileStatement;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ListType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.MapType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.SetType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.StringType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;
import org.eclipse.escet.tooldef.metamodel.tooldef.types.TupleType;

/** Statement and declaration type checker. */
public class StatementsChecker {
    /** Constructor for the {@link StatementsChecker} class. */
    private StatementsChecker() {
        // Static class.
    }

    /**
     * Type check statements, which together form a statement block.
     *
     * @param stats The statements.
     * @param ctxt The type checker context of the statement block. The context is responsible for creating a new
     *     context, and passing it here.
     */
    public static void tcheck(List<Statement> stats, CheckerContext ctxt) {
        try {
            for (Statement stat: stats) {
                tcheck(stat, ctxt);
            }
        } catch (SemanticException ex) {
            // Stop checking the statements. But continue checking.
        }
    }

    /**
     * Type check a statement.
     *
     * @param stat The statement.
     * @param ctxt The type checker context.
     */
    public static void tcheck(Statement stat, CheckerContext ctxt) {
        if (stat instanceof AssignmentStatement) {
            tcheck((AssignmentStatement)stat, ctxt);
        } else if (stat instanceof BreakStatement) {
            tcheck((BreakStatement)stat, ctxt);
        } else if (stat instanceof ContinueStatement) {
            tcheck((ContinueStatement)stat, ctxt);
        } else if (stat instanceof ExitStatement) {
            tcheck((ExitStatement)stat, ctxt);
        } else if (stat instanceof ForStatement) {
            tcheck((ForStatement)stat, ctxt);
        } else if (stat instanceof IfStatement) {
            tcheck((IfStatement)stat, ctxt);
        } else if (stat instanceof ReturnStatement) {
            tcheck((ReturnStatement)stat, ctxt);
        } else if (stat instanceof ToolInvokeStatement) {
            tcheck((ToolInvokeStatement)stat, ctxt);
        } else if (stat instanceof Variable) {
            tcheck((Variable)stat, ctxt);
        } else if (stat instanceof WhileStatement) {
            tcheck((WhileStatement)stat, ctxt);
        } else {
            throw new RuntimeException("Unknown statement: " + stat);
        }
    }

    /**
     * Type check an assignment statement.
     *
     * @param stat The assignment statement.
     * @param ctxt The type checker context.
     */
    private static void tcheck(AssignmentStatement stat, CheckerContext ctxt) {
        // Type check the addressable expressions. No useful type hints to provide.
        for (Expression addr: stat.getAddressables()) {
            ExprsChecker.tcheck(addr, ctxt, TypeHints.NO_HINTS);
        }

        // Type check the addressables.
        Map<PositionObject, Position> addrMap = map();
        for (Expression addr: stat.getAddressables()) {
            tcheckAddr(addr, ctxt, addrMap);
        }

        // Get normalized types of the addressables.
        List<ToolDefType> addrTypes = listc(stat.getAddressables().size());
        for (Expression addr: stat.getAddressables()) {
            addrTypes.add(normalizeType(addr.getType()));
        }

        // Get single addressables type.
        ToolDefType addrType = makeTupleType(addrTypes);

        // Get type hints for values.
        TypeHints[] hints = new TypeHints[stat.getValues().size()];
        for (int i = 0; i < hints.length; i++) {
            hints[i] = new TypeHints();
        }

        if (stat.getValues().size() == 1) {
            // Single value.
            hints[0].add(addrType);
        } else if (addrType instanceof TupleType) {
            // Multiple values, to match against fields of the addressable.
            List<ToolDefType> types = ((TupleType)addrType).getFields();
            int cnt = Math.min(types.size(), stat.getValues().size());
            for (int i = 0; i < cnt; i++) {
                hints[i].add(types.get(i));
            }
        }

        // Type check the values.
        for (int i = 0; i < stat.getValues().size(); i++) {
            Expression value = stat.getValues().get(i);
            ExprsChecker.tcheck(value, ctxt, hints[i]);
        }

        // Check type of the values against type of the addressables.
        ToolDefType valueType = makeTupleTypeFromValues(stat.getValues());
        if (!isSubType(valueType, addrType)) {
            ctxt.addProblem(Message.ASGN_TYPE_VALUE_MISMATCH, stat.getPosition(), typeToStr(valueType),
                    typeToStr(addrType));
            // Non-fatal error.
        }
    }

    /**
     * Type checks an addressable expression.
     *
     * @param addr The addressable expression.
     * @param ctxt The type checker context.
     * @param addrMap Mapping from the already assigned variables (and tool parameters) to the position information of
     *     their addressables. Is extended in-place.
     */
    private static void tcheckAddr(Expression addr, CheckerContext ctxt, Map<PositionObject, Position> addrMap) {
        // Check recursively for tuples.
        if (addr instanceof TupleExpression) {
            TupleExpression taddr = (TupleExpression)addr;
            for (Expression elem: taddr.getElements()) {
                tcheckAddr(elem, ctxt, addrMap);
            }
            return;
        }

        // Variable (or tool parameter), with optional projections.

        // Get projections, and variable reference.
        List<ProjectionExpression> projs = list();
        while (addr instanceof ProjectionExpression) {
            ProjectionExpression proj = (ProjectionExpression)addr;
            projs.add(proj);
            addr = proj.getChild();
        }

        // Check for assignable objects.
        PositionObject var;
        if (addr instanceof VariableExpression) {
            var = ((VariableExpression)addr).getVariable();
        } else if (addr instanceof ToolParamExpression) {
            var = ((ToolParamExpression)addr).getParam();
        } else {
            // Reference to wrong kind of object.
            PositionObject obj = ToolDefTypeUtils.getRefObjFromRef(addr);
            ctxt.addProblem(Message.ASGN_NON_ASSIGNABLE, addr.getPosition(), ToolDefTextUtils.getAbsDescr(obj));
            throw new SemanticException();
        }

        // String projections are not allowed as addressables.
        PositionObject ancestor = (PositionObject)addr.eContainer();
        while (ancestor instanceof ProjectionExpression) {
            ProjectionExpression proj = (ProjectionExpression)ancestor;
            ToolDefType type = proj.getChild().getType();
            type = ToolDefTypeUtils.normalizeType(type);
            if (type instanceof StringType) {
                ctxt.addProblem(Message.ASGN_STRING_PROJ, ancestor.getPosition(), ToolDefTextUtils.getAbsDescr(var));
                // Non-fatal error.
            }
            ancestor = (PositionObject)ancestor.eContainer();
        }

        // No duplicate variables in addressable of a single (multi-)assignment.
        Position prevAddrPos = addrMap.get(var);
        if (prevAddrPos == null) {
            addrMap.put(var, addr.getPosition());
        } else {
            ctxt.addProblem(Message.ASGN_DUPL_VAR, prevAddrPos, ToolDefTextUtils.getAbsDescr(var));
            ctxt.addProblem(Message.ASGN_DUPL_VAR, addr.getPosition(), ToolDefTextUtils.getAbsDescr(var));
            // Non-fatal error.
        }
    }

    /**
     * Type check a break statement.
     *
     * @param stat The break statement.
     * @param ctxt The type checker context.
     */
    private static void tcheck(BreakStatement stat, CheckerContext ctxt) {
        // Check whether inside a loop statement.
        boolean inLoop = false;
        EObject ancestor = stat.eContainer();
        while (true) {
            if (ancestor instanceof Script) {
                break;
            }
            if (ancestor instanceof Tool) {
                break;
            }

            if (ancestor instanceof WhileStatement || ancestor instanceof ForStatement) {
                inLoop = true;
                break;
            }

            ancestor = ancestor.eContainer();
        }

        if (!inLoop) {
            ctxt.addProblem(Message.NOT_IN_LOOP, stat.getPosition(), "break");
            // Non-fatal problem.
        }
    }

    /**
     * Type check a continue statement.
     *
     * @param stat The continue statement.
     * @param ctxt The type checker context.
     */
    private static void tcheck(ContinueStatement stat, CheckerContext ctxt) {
        // Check whether inside a loop statement.
        boolean inLoop = false;
        EObject ancestor = stat.eContainer();
        while (true) {
            if (ancestor instanceof Script) {
                break;
            }
            if (ancestor instanceof Tool) {
                break;
            }

            if (ancestor instanceof WhileStatement || ancestor instanceof ForStatement) {
                inLoop = true;
                break;
            }

            ancestor = ancestor.eContainer();
        }

        if (!inLoop) {
            ctxt.addProblem(Message.NOT_IN_LOOP, stat.getPosition(), "continue");
            // Non-fatal problem.
        }
    }

    /**
     * Type check an exit statement.
     *
     * @param stat The exit statement.
     * @param ctxt The type checker context.
     */
    private static void tcheck(ExitStatement stat, CheckerContext ctxt) {
        // Check exit code, if present.
        if (stat.getExitCode() != null) {
            ExprsChecker.tcheck(stat.getExitCode(), ctxt, NON_NULLABLE_INT_HINT);
            Expression cond = stat.getExitCode();
            ToolDefType type = cond.getType();
            if (!isSubType(type, NON_NULLABLE_INT_TYPE)) {
                ctxt.addProblem(Message.EXIT_CODE_NON_INT, cond.getPosition(), typeToStr(type));
                // Non-fatal problem.
            }
        }
    }

    /**
     * Type check a for statement.
     *
     * @param stat The for statement.
     * @param ctxt The type checker context.
     */
    private static void tcheck(ForStatement stat, CheckerContext ctxt) {
        // Type check the source expression. No useful hints to provide.
        ExprsChecker.tcheck(stat.getSource(), ctxt, TypeHints.NO_HINTS);
        Expression source = stat.getSource();

        // Check the type of the source expression.
        ToolDefType sourceType = source.getType();
        sourceType = ToolDefTypeUtils.normalizeType(sourceType);

        if (sourceType.isNullable()) {
            ctxt.addProblem(Message.FOR_SOURCE_NULL, source.getPosition(), typeToStr(sourceType));
            throw new SemanticException();
        }

        if (!(sourceType instanceof ListType) && !(sourceType instanceof MapType) && !(sourceType instanceof SetType)) {
            ctxt.addProblem(Message.FOR_SOURCE_TYPE, source.getPosition(), typeToStr(sourceType));
            throw new SemanticException();
        }

        // Get type of the addressable.
        ToolDefType addrType;
        if (sourceType instanceof ListType) {
            addrType = ((ListType)sourceType).getElemType();
        } else if (sourceType instanceof MapType) {
            MapType mtype = (MapType)sourceType;
            ToolDefType ktype = mtype.getKeyType();
            ToolDefType vtype = mtype.getValueType();
            addrType = makeTupleType(list(deepclone(ktype), deepclone(vtype)));
        } else if (sourceType instanceof SetType) {
            addrType = ((SetType)sourceType).getElemType();
        } else {
            throw new RuntimeException("Unknown src type: " + sourceType);
        }

        // Create context for the body.
        CheckerContext bodyCtxt = new CheckerContext(ctxt, stat);

        // Check form of the addressable against the type of the addressable.
        // Also adds the variables to the body context.
        Position addrPos = first(stat.getAddressables()).getPosition();
        tcheck(stat.getAddressables(), addrType, addrPos, bodyCtxt);

        // Check body.
        tcheck(stat.getStatements(), bodyCtxt);
    }

    /**
     * Type check addressable declarations.
     *
     * @param addrs The addressable declarations.
     * @param type The type of the addressable declarations.
     * @param position The position on which to report form mismatches.
     * @param ctxt The type checker context. Used to report problems, and add the variables.
     */
    private static void tcheck(List<AddressableDecl> addrs, ToolDefType type, Position position, CheckerContext ctxt) {
        if (addrs.size() == 1) {
            AddressableDecl addr = first(addrs);
            if (addr instanceof TupleAddressableDecl) {
                // Remove tuple addressable wrapper.
                List<AddressableDecl> elems = ((TupleAddressableDecl)addr).getElements();
                tcheck(elems, type, addr.getPosition(), ctxt);
            } else if (addr instanceof VariableAddressableDecl) {
                // Set variable type.
                Variable var = ((VariableAddressableDecl)addr).getVariable();
                var.setType(deepclone(type));

                // Add variable to body context.
                ctxt.addDecl(var);
            } else {
                throw new RuntimeException("Unknown addressable decl: " + addr);
            }
        } else {
            // Need to match multiple addressables against tuple type.
            type = ToolDefTypeUtils.normalizeType(type);
            if (!(type instanceof TupleType)) {
                ctxt.addProblem(Message.FOR_ADDRS_TOO_MANY, position, str(addrs.size()), typeToStr(type));
                throw new SemanticException();
            }

            // Need to match multiple addressables against as many tuple fields.
            TupleType ttype = (TupleType)type;
            if (addrs.size() != ttype.getFields().size()) {
                ctxt.addProblem(Message.FOR_ADDRS_CNT, position, str(addrs.size()), str(ttype.getFields().size()),
                        typeToStr(type));
                throw new SemanticException();
            }

            // Match each addressable against corresponding tuple field.
            for (int i = 0; i < addrs.size(); i++) {
                AddressableDecl addr = addrs.get(i);
                tcheck(list(addr), ttype.getFields().get(i), addr.getPosition(), ctxt);
            }
        }
    }

    /**
     * Type check an if statement.
     *
     * @param stat The if statement.
     * @param ctxt The type checker context.
     */
    private static void tcheck(IfStatement stat, CheckerContext ctxt) {
        // Check 'if' condition.
        ExprsChecker.tcheck(stat.getCondition(), ctxt, NON_NULLABLE_BOOL_HINT);
        Expression cond = stat.getCondition();
        ToolDefType type = cond.getType();
        if (!isSubType(type, NON_NULLABLE_BOOL_TYPE)) {
            ctxt.addProblem(Message.COND_NON_BOOL, cond.getPosition(), typeToStr(type));
            // Non-fatal problem.
        }

        // Check 'elif' conditions.
        for (ElifStatement elif: stat.getElifs()) {
            ExprsChecker.tcheck(elif.getCondition(), ctxt, NON_NULLABLE_BOOL_HINT);
            cond = elif.getCondition();
            type = cond.getType();
            if (!isSubType(type, NON_NULLABLE_BOOL_TYPE)) {
                ctxt.addProblem(Message.COND_NON_BOOL, cond.getPosition(), typeToStr(type));
                // Non-fatal problem.
            }
        }

        // Check bodies.
        tcheck(stat.getThens(), new CheckerContext(ctxt, stat));
        for (ElifStatement elif: stat.getElifs()) {
            tcheck(elif.getThens(), new CheckerContext(ctxt, stat));
        }
        tcheck(stat.getElses(), new CheckerContext(ctxt, stat));
    }

    /**
     * Type check a return statement.
     *
     * @param stat The return statement.
     * @param ctxt The type checker context.
     */
    private static void tcheck(ReturnStatement stat, CheckerContext ctxt) {
        // Check whether inside a tool definition.
        Tool tool = null;
        EObject ancestor = stat.eContainer();
        while (true) {
            if (ancestor instanceof Script) {
                break;
            }

            if (ancestor instanceof Tool) {
                tool = (Tool)ancestor;
                break;
            }

            ancestor = ancestor.eContainer();
        }

        if (tool == null) {
            ctxt.addProblem(Message.NOT_IN_TOOL, stat.getPosition());
            // Non-fatal problem.
            return;
        }

        // If no return types, and no return values, then OK.
        if (stat.getValues().isEmpty() && tool.getReturnTypes().isEmpty()) {
            return;
        }

        // Check for no types/values mismatch.
        if (!stat.getValues().isEmpty() && tool.getReturnTypes().isEmpty()) {
            ctxt.addProblem(Message.RETURN_NO_TYPES, stat.getPosition(), getAbsName(tool));
            // Non-fatal problem.
            return;
        }

        if (stat.getValues().isEmpty() && !tool.getReturnTypes().isEmpty()) {
            ctxt.addProblem(Message.RETURN_NO_VALUES, stat.getPosition(), getAbsName(tool));
            // Non-fatal problem.
            return;
        }

        // Get single return type for the tool.
        ToolDefType retType = makeTupleType(deepclone(tool.getReturnTypes()));

        // Get type hints for the return values.
        int valueCnt = stat.getValues().size();
        TypeHints[] valueHints = new TypeHints[valueCnt];
        if (stat.getValues().size() == 1) {
            // Single value, single type.
            valueHints[0] = new TypeHints();
            valueHints[0].add(retType);
        } else {
            // Try to match against tuple type.
            for (int i = 0; i < valueCnt; i++) {
                valueHints[i] = new TypeHints();
            }
            if (retType instanceof TupleType) {
                List<ToolDefType> fields = ((TupleType)retType).getFields();
                int cnt = Math.min(valueCnt, fields.size());
                for (int i = 0; i < cnt; i++) {
                    valueHints[i].add(fields.get(i));
                }
            }
        }

        // Type check the return values.
        for (int i = 0; i < valueCnt; i++) {
            ExprsChecker.tcheck(stat.getValues().get(i), ctxt, valueHints[i]);
        }

        // Check return value(s) against tool return type.
        ToolDefType valueType = makeTupleTypeFromValues(stat.getValues());
        if (!isSubType(valueType, retType)) {
            ctxt.addProblem(Message.RETURN_VALUE_TYPE, stat.getPosition(), typeToStr(valueType), getAbsName(tool),
                    typeToStr(retType));
            // Non-fatal problem.
        }
    }

    /**
     * Type check a tool invocation statement.
     *
     * @param stat The tool invocation statement.
     * @param ctxt The type checker context.
     */
    private static void tcheck(ToolInvokeStatement stat, CheckerContext ctxt) {
        // No useful type hints to provide, as return values of the tool (if
        // any) are not used.
        ToolInvokeChecker.tcheck(stat.getInvocation(), ctxt, NO_HINTS, false);
    }

    /**
     * Type check a variable declaration statement.
     *
     * @param var The variable declaration statement.
     * @param ctxt The type checker context.
     */
    private static void tcheck(Variable var, CheckerContext ctxt) {
        // Type check the type of the variable.
        TypesChecker.tcheck(var.getType(), ctxt);
        ToolDefType varType = var.getType();

        // Type check initial value.
        if (var.getValue() == null) {
            // Make sure default value exists.
            if (!ToolDefTypeUtils.hasDefaultValue(var.getType())) {
                ctxt.addProblem(Message.VAR_NO_INITIAL_VALUE, varType.getPosition(), getAbsName(var),
                        typeToStr(var.getType()));
                // Non-fatal problem.
            }
        } else {
            // Type check initial value expression. Can't have self cycles, as
            // the variable itself isn't added yet.
            TypeHints hints = new TypeHints();
            hints.add(var.getType());
            ExprsChecker.tcheck(var.getValue(), ctxt, hints);
            Expression value = var.getValue();
            ToolDefType valueType = value.getType();

            // Check initial value expression type.
            if (!isSubType(valueType, varType)) {
                ctxt.addProblem(Message.VAR_VALUE_TYPE, value.getPosition(), getAbsName(var), typeToStr(valueType),
                        typeToStr(varType));
                // Non-fatal problem.
            }
        }

        // Add to context.
        ctxt.addDecl(var);
    }

    /**
     * Type check a while statement.
     *
     * @param stat The while statement.
     * @param ctxt The type checker context.
     */
    private static void tcheck(WhileStatement stat, CheckerContext ctxt) {
        // Check condition.
        ExprsChecker.tcheck(stat.getCondition(), ctxt, NON_NULLABLE_BOOL_HINT);
        Expression cond = stat.getCondition();
        ToolDefType type = cond.getType();
        if (!isSubType(type, NON_NULLABLE_BOOL_TYPE)) {
            ctxt.addProblem(Message.COND_NON_BOOL, cond.getPosition(), typeToStr(type));
            // Non-fatal problem.
        }

        // Check body.
        tcheck(stat.getStatements(), new CheckerContext(ctxt, stat));
    }
}
