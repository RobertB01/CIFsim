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

package org.eclipse.escet.cif.typechecker.scopes;

import static org.eclipse.escet.cif.common.CifExtFuncUtils.splitExtJavaRef;
import static org.eclipse.escet.cif.common.CifExtFuncUtils.splitJavaClassPathEntries;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.cif.common.CifTypeUtils.makeTupleType;
import static org.eclipse.escet.cif.common.CifTypeUtils.makeTupleTypeFromValues;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAssignmentFuncStatement;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBreakFuncStatement;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newContinueFuncStatement;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newElifFuncStatement;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIfFuncStatement;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newReturnFuncStatement;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newWhileFuncStatement;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.BOOL_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.NO_TYPE_HINT;
import static org.eclipse.escet.cif.typechecker.CifExprsTypeChecker.transExpression;
import static org.eclipse.escet.cif.typechecker.CifTypesTypeChecker.transCifType;
import static org.eclipse.escet.cif.typechecker.ExprContext.Condition.NO_TIME;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifAddressableUtils;
import org.eclipse.escet.cif.common.CifExtFuncUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.RangeCompat;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.ComponentDef;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ProjectionExpression;
import org.eclipse.escet.cif.metamodel.cif.functions.AssignmentFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.BreakFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ContinueFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ElifFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.ExternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.Function;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.IfFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.functions.ReturnFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.functions.WhileFuncStatement;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.CompInstWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.CompParamWrapType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.DistType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.FuncType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.metamodel.cif.types.TypeRef;
import org.eclipse.escet.cif.parser.ast.AFuncDecl;
import org.eclipse.escet.cif.parser.ast.automata.ALocation;
import org.eclipse.escet.cif.parser.ast.expressions.AExpression;
import org.eclipse.escet.cif.parser.ast.functions.AAssignFuncStatement;
import org.eclipse.escet.cif.parser.ast.functions.ABreakFuncStatement;
import org.eclipse.escet.cif.parser.ast.functions.AContinueFuncStatement;
import org.eclipse.escet.cif.parser.ast.functions.AElifFuncStatement;
import org.eclipse.escet.cif.parser.ast.functions.AExternalFuncBody;
import org.eclipse.escet.cif.parser.ast.functions.AFuncStatement;
import org.eclipse.escet.cif.parser.ast.functions.AIfFuncStatement;
import org.eclipse.escet.cif.parser.ast.functions.AInternalFuncBody;
import org.eclipse.escet.cif.parser.ast.functions.AReturnFuncStatement;
import org.eclipse.escet.cif.parser.ast.functions.AWhileFuncStatement;
import org.eclipse.escet.cif.parser.ast.types.ACifType;
import org.eclipse.escet.cif.typechecker.AssignmentUniquenessChecker;
import org.eclipse.escet.cif.typechecker.CifTypeChecker;
import org.eclipse.escet.cif.typechecker.ErrMsg;
import org.eclipse.escet.cif.typechecker.ExprContext;
import org.eclipse.escet.cif.typechecker.SymbolTableEntry;
import org.eclipse.escet.cif.typechecker.declwrap.ConstDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.DeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.EnumDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.EnumLiteralDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.FuncParamDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.FuncVariableDeclWrap;
import org.eclipse.escet.cif.typechecker.declwrap.TypeDeclWrap;
import org.eclipse.escet.common.app.framework.PlatformUriUtils;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Numbers;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.eclipse.escet.common.typechecker.SemanticException;

/** Function scope. */
public class FunctionScope extends ParentScope<Function> {
    /** The expression type checking context to use within functions. */
    private static final ExprContext FUNC_CTXT = ExprContext.DEFAULT_CTXT.add(NO_TIME);

    /** The CIF AST function declaration object representing this scope. */
    private final AFuncDecl astDecl;

    /** Whether this function is used (referenced from anywhere). */
    public boolean used = false;

    /** The return type of the function. Is {@code null} until resolved. */
    private CifType returnType = null;

    /**
     * Constructor for the {@link FunctionScope} class.
     *
     * @param obj The CIF metamodel automaton object representing this scope.
     * @param astDecl The CIF AST function declaration object representing this scope.
     * @param parent The parent scope.
     * @param tchecker The CIF type checker to use.
     */
    public FunctionScope(Function obj, AFuncDecl astDecl, ParentScope<?> parent, CifTypeChecker tchecker) {
        super(obj, parent, tchecker);
        this.astDecl = astDecl;
    }

    /**
     * Returns the return type of the function.
     *
     * @return The return type of the function.
     */
    public CifType getReturnType() {
        Assert.notNull(returnType);
        return returnType;
    }

    @Override
    public String getName() {
        return obj.getName();
    }

    @Override
    public String getAbsName() {
        return CifTextUtils.getAbsName(obj);
    }

    @Override
    public String getAbsText() {
        return fmt("function \"%s\"", getAbsName());
    }

    @Override
    protected String getScopeTypeName() {
        return "func";
    }

    @Override
    protected ComplexComponent getComplexComponent() {
        // Functions are not complex components.
        throw new UnsupportedOperationException();
    }

    @Override
    protected Group getGroup() {
        // Functions are not groups.
        throw new UnsupportedOperationException();
    }

    @Override
    protected ComponentDef getComponentDef() {
        // Functions are not component definitions.
        throw new UnsupportedOperationException();
    }

    @Override
    protected Automaton getAutomaton() {
        // Functions are not automata.
        throw new UnsupportedOperationException();
    }

    @Override
    public List<ALocation> getAstLocs() {
        return null;
    }

    @Override
    public void addChildScope(SymbolScope<?> scope) {
        // Functions have no child scopes.
        throw new UnsupportedOperationException();
    }

    @Override
    protected void tcheckScopeForUse() {
        // Type check return type, in the parent scope of the function scope,
        // not in the function scope itself.
        tchecker.addToCycle(this);

        List<CifType> returnTypes = list();
        try {
            for (ACifType astType: astDecl.returnTypes) {
                CifType retType = transCifType(astType, getParent(), tchecker);
                returnTypes.add(retType);

                // Check for allowed types.
                if (CifTypeUtils.hasComponentLikeType(retType)) {
                    tchecker.addProblem(ErrMsg.TYPE_INVALID_TYPE, retType.getPosition(),
                            CifTextUtils.typeToStr(retType), "return values of functions");
                    throw new SemanticException();
                }
            }
        } finally {
            tchecker.removeFromCycle(this);
        }

        // Add return types to function declaration.
        obj.getReturnTypes().addAll(returnTypes);

        // Create a single return type for the function.
        Assert.check(!returnTypes.isEmpty());
        this.returnType = makeTupleType(deepclone(returnTypes));

        // Type check the function parameters.
        tchecker.addToCycle(this);
        try {
            for (DeclWrap<?> decl: declarations.values()) {
                if (decl instanceof FuncParamDeclWrap) {
                    decl.tcheckForUse();
                }
            }
        } finally {
            tchecker.removeFromCycle(this);
        }
    }

    @Override
    protected void tcheckScopeFull() {
        if (astDecl.body instanceof AInternalFuncBody) {
            tcheckIntFuncBody((AInternalFuncBody)astDecl.body);
        } else {
            Assert.check(astDecl.body instanceof AExternalFuncBody);
            tcheckExtFuncBody((AExternalFuncBody)astDecl.body);
        }
    }

    /**
     * Type check the body of an internal user-defined function.
     *
     * @param body The body.
     */
    private void tcheckIntFuncBody(AInternalFuncBody body) {
        InternalFunction ifunc = (InternalFunction)obj;

        // Check statements.
        boolean reachable = true;
        for (AFuncStatement stat: body.statements) {
            reachable = typeCheckStatement(stat, ifunc.getStatements(), false, reachable);
        }

        // Make sure all paths end with 'return'.
        checkEndsWithReturn(ifunc.getStatements());
    }

    /**
     * Type check the body of an external user-defined function.
     *
     * @param body The body.
     */
    private void tcheckExtFuncBody(AExternalFuncBody body) {
        // Mark parameters as used, as we have no body where we can refer
        // to them.
        for (DeclWrap<?> decl: declarations.values()) {
            Assert.check(decl instanceof FuncParamDeclWrap);
            decl.used = true;
        }

        // Get language name. Use external function reference from metamodel,
        // instead of from the AST, as the one in the metamodel is properly
        // updated during import merging.
        ExternalFunction func = (ExternalFunction)obj;
        String extRef = func.getFunction();
        String langName = CifExtFuncUtils.getLangName(extRef);

        // Perform additional checks based on the external language.
        if (langName.equals("java")) {
            tcheckExtJavaFunc(func, extRef, body.position);
        } else {
            tchecker.addProblem(ErrMsg.UNSUPPORTED_EXT_FUNC_LANG, body.position, langName);
            // Non-fatal problem.
        }
    }

    /**
     * Type checks the body of an external user-defined Java function.
     *
     * @param func The external user-defined function.
     * @param extRef The external implementation reference.
     * @param position Position information for the external implementation reference.
     */
    private void tcheckExtJavaFunc(ExternalFunction func, String extRef, Position position) {
        // Check class path entries.
        String[] parts = splitExtJavaRef(extRef);
        String classPath = parts[3];
        if (classPath != null) {
            // Get entries.
            String[] entries = splitJavaClassPathEntries(classPath);

            // Check entries.
            for (int i = 0; i < entries.length; i++) {
                String entry = entries[i];

                // Make sure path exists.
                String absEntry = tchecker.resolveImport(entry);
                if (!PlatformUriUtils.exists(absEntry)) {
                    tchecker.addProblem(ErrMsg.CLS_PATH_NOT_FOUND, position, entry);
                    // Non-fatal problem.
                }
            }
        }

        // Check parameter types.
        for (int i = 0; i < func.getParameters().size(); i++) {
            FunctionParameter param = func.getParameters().get(i);
            CifType paramType = param.getParameter().getType();
            if (!tcheckJavaFuncType(paramType)) {
                tchecker.addProblem(ErrMsg.EXT_FUNC_PARAM_RET_TYPE, paramType.getPosition(), Numbers.toOrdinal(i + 1),
                        "parameter", getAbsName(), typeToStr(paramType), "Java");
                // Non-fatal problem.
            }
        }

        // Check return types.
        for (int i = 0; i < func.getReturnTypes().size(); i++) {
            CifType retType = func.getReturnTypes().get(i);
            if (!tcheckJavaFuncType(retType)) {
                tchecker.addProblem(ErrMsg.EXT_FUNC_PARAM_RET_TYPE, retType.getPosition(), Numbers.toOrdinal(i + 1),
                        "return value", getAbsName(), typeToStr(retType), "Java");
                // Non-fatal problem.
            }
        }
    }

    /**
     * Type checks the type of a parameter of return value of an external user-defined Java function.
     *
     * @param type The CIF type.
     * @return {@code true} if the type is supported, {@code false} otherwise.
     */
    private static boolean tcheckJavaFuncType(CifType type) {
        if (type instanceof BoolType) {
            return true;
        } else if (type instanceof IntType) {
            return true;
        } else if (type instanceof TypeRef) {
            return tcheckJavaFuncType(((TypeRef)type).getType().getType());
        } else if (type instanceof EnumType) {
            return false;
        } else if (type instanceof RealType) {
            return true;
        } else if (type instanceof StringType) {
            return true;
        } else if (type instanceof ListType) {
            CifType etype = ((ListType)type).getElementType();
            return tcheckJavaFuncType(etype);
        } else if (type instanceof SetType) {
            CifType etype = ((SetType)type).getElementType();
            return tcheckJavaFuncType(etype);
        } else if (type instanceof FuncType) {
            return false;
        } else if (type instanceof DictType) {
            CifType ktype = ((DictType)type).getKeyType();
            CifType vtype = ((DictType)type).getValueType();
            return tcheckJavaFuncType(ktype) && tcheckJavaFuncType(vtype);
        } else if (type instanceof TupleType) {
            for (Field field: ((TupleType)type).getFields()) {
                if (!tcheckJavaFuncType(field.getType())) {
                    return false;
                }
            }
            return true;
        } else if (type instanceof DistType) {
            return false;
        } else if (type instanceof CompInstWrapType) {
            CifType wtype = ((CompInstWrapType)type).getReference();
            return tcheckJavaFuncType(wtype);
        } else if (type instanceof CompParamWrapType) {
            CifType wtype = ((CompParamWrapType)type).getReference();
            return tcheckJavaFuncType(wtype);
        } else {
            throw new RuntimeException("Unexpected type: " + type);
        }
    }

    /**
     * Type checks an internal function statement.
     *
     * @param stat The CIF AST representation of the statement.
     * @param stats The list of statements to which to add the type checked result.
     * @param inWhile Whether the statement occurs in the body of a 'while' statement.
     * @param reachable Whether the statement is reachable.
     * @return {@code true} if statements after this statement are reachable, {@code false} otherwise.
     */
    private boolean typeCheckStatement(AFuncStatement stat, List<FunctionStatement> stats, boolean inWhile,
            boolean reachable)
    {
        // Check for unreachable statements.
        if (!reachable) {
            tchecker.addProblem(ErrMsg.STAT_UNREACHABLE, stat.position);
            // Non-fatal problem.
        }

        // Type check based on type of statement.
        if (stat instanceof AAssignFuncStatement) {
            // Create assignment statement, and add it to the parent.
            AAssignFuncStatement asgn = (AAssignFuncStatement)stat;
            AssignmentFuncStatement astat = newAssignmentFuncStatement();
            astat.setPosition(stat.position);
            stats.add(astat);

            // Type check and set addressable.
            List<Expression> addrs = list();
            for (AExpression addressable: asgn.addressables) {
                Expression addr = transExpression(addressable, NO_TYPE_HINT, this, FUNC_CTXT, tchecker);
                addrs.add(addr);
            }

            Expression addr = CifValueUtils.makeTuple(addrs);
            astat.setAddressable(addr);

            // Make sure we refer to local variables and/or function
            // parameters.
            for (Expression expr: CifAddressableUtils.getRefExprs(addr)) {
                if (!(expr instanceof DiscVariableExpression)) {
                    // Reference to wrong kind of object.
                    Expression uexpr = CifTypeUtils.unwrapExpression(expr);
                    PositionObject obj = CifScopeUtils.getRefObjFromRef(uexpr);
                    tchecker.addProblem(ErrMsg.RESOLVE_NON_FUNC_VAR, expr.getPosition(), CifTextUtils.getAbsName(obj),
                            getAbsName());
                    throw new SemanticException();
                }

                // Paranoia checking.
                DiscVariable var = ((DiscVariableExpression)expr).getVariable();

                EObject func = var.eContainer();
                while (func != null && !(func instanceof Function)) {
                    func = func.eContainer();
                }
                Assert.notNull(func);
                Assert.check(func == obj);

                // String projections are not allowed as addressables.
                PositionObject varAncestor = (PositionObject)expr.eContainer();
                while (varAncestor instanceof ProjectionExpression) {
                    ProjectionExpression proj = (ProjectionExpression)varAncestor;
                    CifType type = proj.getChild().getType();
                    CifType ntype = CifTypeUtils.normalizeType(type);
                    if (ntype instanceof StringType) {
                        tchecker.addProblem(ErrMsg.ASGN_STRING_PROJ, varAncestor.getPosition(),
                                CifTextUtils.getAbsName(var));
                        // Non-fatal error.
                    }
                    varAncestor = (PositionObject)varAncestor.eContainer();
                }
            }

            // Get type hints for values.
            CifType addrType = addr.getType();
            CifType naddrType = CifTypeUtils.normalizeType(addr.getType());
            CifType[] valueHints = new CifType[asgn.values.size()];
            if (asgn.values.size() == 1) {
                valueHints[0] = addrType;
            } else if (naddrType instanceof TupleType) {
                TupleType ttype = (TupleType)naddrType;
                if (ttype.getFields().size() == asgn.values.size()) {
                    for (int i = 0; i < valueHints.length; i++) {
                        valueHints[i] = ttype.getFields().get(i).getType();
                    }
                }
            }

            // Type check and set value.
            List<Expression> values = list();
            for (int i = 0; i < asgn.values.size(); i++) {
                AExpression avalue = asgn.values.get(i);
                CifType valueHint = valueHints[i];
                Expression value = transExpression(avalue, valueHint, this, FUNC_CTXT, tchecker);
                values.add(value);
            }

            Expression value = CifValueUtils.makeTuple(values);
            astat.setValue(value);

            // Compatible types for addressable and value.
            CifType valueType = value.getType();
            if (!CifTypeUtils.checkTypeCompat(addrType, valueType, RangeCompat.OVERLAP)) {
                tchecker.addProblem(ErrMsg.ASGN_TYPE_VALUE_MISMATCH, stat.position, CifTextUtils.typeToStr(valueType),
                        CifTextUtils.typeToStr(addrType));
                // Non-fatal error.
            }

            // Check for assignments to unique parts of variables.
            Map<Declaration, Set<Pair<Position, List<Object>>>> asgnMap = map();
            AssignmentUniquenessChecker.checkUniqueAsgns(addr, asgnMap, tchecker, ErrMsg.DUPL_VAR_ASGN_FUNC);

            // Statement after the assignment is reachable if and only if the
            // assignment itself is reachable.
            return reachable;
        } else if (stat instanceof ABreakFuncStatement) {
            // Create break statement, and add it to the parent.
            BreakFuncStatement bstat = newBreakFuncStatement();
            bstat.setPosition(stat.position);
            stats.add(bstat);

            // Check 'in while' constraint.
            if (!inWhile) {
                tchecker.addProblem(ErrMsg.STAT_NOT_IN_WHILE, stat.position, "break");
                // Non-fatal problem.
            }

            // Statements after break are unreachable.
            return false;
        } else if (stat instanceof AContinueFuncStatement) {
            // Create continue statement, and add it to the parent.
            ContinueFuncStatement cstat = newContinueFuncStatement();
            cstat.setPosition(stat.position);
            stats.add(cstat);

            // Check 'in while' constraint.
            if (!inWhile) {
                tchecker.addProblem(ErrMsg.STAT_NOT_IN_WHILE, stat.position, "continue");
                // Non-fatal problem.
            }

            // Statements after continue are unreachable.
            return false;
        } else if (stat instanceof AIfFuncStatement) {
            // Create if statement, and add it to the parent.
            AIfFuncStatement astat = (AIfFuncStatement)stat;
            IfFuncStatement istat = newIfFuncStatement();
            istat.setPosition(stat.position);
            stats.add(istat);

            // Guards.
            List<Expression> guards = istat.getGuards();
            for (AExpression g: astat.guards) {
                Expression guard = transExpression(g, BOOL_TYPE_HINT, this, FUNC_CTXT, tchecker);
                CifType t = guard.getType();
                CifType nt = CifTypeUtils.normalizeType(t);
                if (!(nt instanceof BoolType)) {
                    tchecker.addProblem(ErrMsg.GUARD_NON_BOOL, guard.getPosition(), CifTextUtils.typeToStr(t));
                    // Non-fatal error.
                }
                guards.add(guard);
            }

            // Thens.
            List<FunctionStatement> thens = istat.getThens();
            boolean thenReachable = reachable;
            for (AFuncStatement thenStat: astat.thens) {
                thenReachable = typeCheckStatement(thenStat, thens, inWhile, thenReachable);
            }

            // Elifs.
            boolean elifsReachable = reachable;
            List<ElifFuncStatement> elifs = istat.getElifs();
            for (AElifFuncStatement elif1: astat.elifs) {
                ElifFuncStatement elif2 = newElifFuncStatement();
                elif2.setPosition(elif1.position);
                elifs.add(elif2);

                // Guards.
                guards = elif2.getGuards();
                for (AExpression g: elif1.guards) {
                    Expression guard = transExpression(g, BOOL_TYPE_HINT, this, FUNC_CTXT, tchecker);
                    CifType t = guard.getType();
                    CifType nt = CifTypeUtils.normalizeType(t);
                    if (!(nt instanceof BoolType)) {
                        tchecker.addProblem(ErrMsg.GUARD_NON_BOOL, guard.getPosition(), CifTextUtils.typeToStr(t));
                        // Non-fatal error.
                    }
                    guards.add(guard);
                }

                // Elif/thens.
                List<FunctionStatement> elifThens = elif2.getThens();
                boolean elifReachable = reachable;
                for (AFuncStatement elifStat: elif1.thens) {
                    elifReachable = typeCheckStatement(elifStat, elifThens, inWhile, elifReachable);
                }
                elifsReachable = elifsReachable && elifReachable;
            }
            if (astat.elifs.isEmpty()) {
                elifsReachable = false;
            }

            // Elses.
            List<FunctionStatement> elses = istat.getElses();
            boolean elseReachable = reachable;
            if (astat.elseStat != null) {
                for (AFuncStatement elseStat: astat.elseStat.elses) {
                    elseReachable = typeCheckStatement(elseStat, elses, inWhile, elseReachable);
                }
            }

            // Statement after 'if' reachable if either of the thens, elifs or
            // elses indicates that the statement after it is reachable. For
            // elses, if not present, it indicates whether the 'if' itself
            // was reachable. If there are no 'elifs', 'elifsReachable' is
            // false, and is irrelevant.
            return thenReachable || elifsReachable || elseReachable;
        } else if (stat instanceof AReturnFuncStatement) {
            // Create return statement, and add it to the parent.
            ReturnFuncStatement rstat = newReturnFuncStatement();
            rstat.setPosition(stat.position);
            stats.add(rstat);

            // Get type hints for the return values.
            List<AExpression> avalues = ((AReturnFuncStatement)stat).values;
            int valueCnt = avalues.size();
            CifType[] valueHints = new CifType[valueCnt];
            if (valueCnt == 1) {
                // Single value, single type.
                valueHints[0] = returnType;
            } else {
                // Try to match against tuple type.
                if (returnType instanceof TupleType) {
                    List<Field> fields = ((TupleType)returnType).getFields();
                    int cnt = Math.min(valueCnt, fields.size());
                    for (int i = 0; i < cnt; i++) {
                        valueHints[i] = fields.get(i).getType();
                    }
                }
            }

            // Type check return values.
            List<Expression> values = rstat.getValues();
            for (int i = 0; i < avalues.size(); i++) {
                AExpression avalue = avalues.get(i);
                Expression value = transExpression(avalue, valueHints[i], this, FUNC_CTXT, tchecker);
                values.add(value);
            }

            // Construct one return type for the return values.
            Assert.check(!values.isEmpty());
            CifType retStatType = makeTupleTypeFromValues(values);

            // Make sure return values are compatible with return type of the
            // function.
            if (!CifTypeUtils.checkTypeCompat(returnType, retStatType, RangeCompat.CONTAINED)) {
                tchecker.addProblem(ErrMsg.STAT_RETURN_TYPE, stat.position, CifTextUtils.typeToStr(retStatType),
                        CifTextUtils.typeToStr(returnType), getAbsName());
                // Non-fatal problem.
            }

            // Statements after return are unreachable.
            return false;
        } else if (stat instanceof AWhileFuncStatement) {
            // Create while statement, and add it to the parent.
            AWhileFuncStatement awhile = (AWhileFuncStatement)stat;
            WhileFuncStatement wstat = newWhileFuncStatement();
            wstat.setPosition(stat.position);
            stats.add(wstat);

            // Type check guards.
            List<Expression> guards = wstat.getGuards();
            for (AExpression g: awhile.guards) {
                Expression guard = transExpression(g, BOOL_TYPE_HINT, this, FUNC_CTXT, tchecker);
                CifType t = guard.getType();
                CifType nt = CifTypeUtils.normalizeType(t);
                if (!(nt instanceof BoolType)) {
                    tchecker.addProblem(ErrMsg.GUARD_NON_BOOL, guard.getPosition(), CifTextUtils.typeToStr(t));
                    // Non-fatal error.
                }
                guards.add(guard);
            }

            // Type check body statements.
            boolean bodyStatReachable = reachable;
            for (AFuncStatement bodyStat: awhile.statements) {
                bodyStatReachable = typeCheckStatement(bodyStat, wstat.getStatements(), true, bodyStatReachable);
            }

            // Statement after the 'while' is reachable if and only if the
            // 'while' itself is reachable.
            return reachable;
        } else {
            throw new RuntimeException("Unknown func statement: " + stat);
        }
    }

    /**
     * Checks a sequence of statements to make sure all paths end with a {@link ReturnFuncStatement}. Note that this
     * method does not take into account whether statements are reachable.
     *
     * @param stats The statements.
     */
    private void checkEndsWithReturn(List<FunctionStatement> stats) {
        checkEndsWithReturn(last(stats));
    }

    /**
     * Checks a statement to make sure all paths end with a {@link ReturnFuncStatement}. Note that this method does not
     * take into account whether statements are reachable.
     *
     * @param stat The statement.
     */
    private void checkEndsWithReturn(FunctionStatement stat) {
        if (stat instanceof AssignmentFuncStatement) {
            // Trivially not OK.
            tchecker.addProblem(ErrMsg.FUNC_NOT_END_RETURN, stat.getPosition(), getAbsName());
            // Non-fatal problem.
        } else if (stat instanceof BreakFuncStatement) {
            // Trivially not OK.
            tchecker.addProblem(ErrMsg.FUNC_NOT_END_RETURN, stat.getPosition(), getAbsName());
            // Non-fatal problem.
        } else if (stat instanceof ContinueFuncStatement) {
            // Trivially not OK.
            tchecker.addProblem(ErrMsg.FUNC_NOT_END_RETURN, stat.getPosition(), getAbsName());
            // Non-fatal problem.
        } else if (stat instanceof IfFuncStatement) {
            // If the 'else' part is missing, it is trivially not OK.
            // Otherwise, check the 'thens', 'elifs', and 'elses'.
            IfFuncStatement istat = (IfFuncStatement)stat;
            if (istat.getElses().isEmpty()) {
                // Trivially not OK.
                tchecker.addProblem(ErrMsg.FUNC_NOT_END_RETURN, stat.getPosition(), getAbsName());
                // Non-fatal problem.
            } else {
                // Each of the alternatives should end with a return statement.
                checkEndsWithReturn(istat.getThens());
                for (ElifFuncStatement elif: istat.getElifs()) {
                    checkEndsWithReturn(elif.getThens());
                }
                checkEndsWithReturn(istat.getElses());
            }
        } else if (stat instanceof ReturnFuncStatement) {
            // Trivially OK.
        } else if (stat instanceof WhileFuncStatement) {
            // Trivially not OK.
            tchecker.addProblem(ErrMsg.FUNC_NOT_END_RETURN, stat.getPosition(), getAbsName());
            // Non-fatal problem.
        } else {
            throw new RuntimeException("Unknown function statement: " + stat);
        }
    }

    @Override
    public SymbolTableEntry resolve(Position position, String name, CifTypeChecker tchecker, SymbolScope<?> originScope) {
        // Paranoia check: we should only resolve things inside of a function,
        // for internal functions, and not for external functions.
        Assert.check(obj instanceof InternalFunction);

        // First, resolve as usual.
        SymbolTableEntry entry = super.resolve(position, name, tchecker, originScope);

        // Make sure that we only reference things that may be referenced from
        // a function.
        if (entry instanceof ConstDeclWrap || entry instanceof EnumDeclWrap || entry instanceof EnumLiteralDeclWrap
                || entry instanceof FuncParamDeclWrap || entry instanceof FuncVariableDeclWrap
                || entry instanceof TypeDeclWrap || entry instanceof FunctionScope)
        {
            return entry;
        }

        // We may not reference this from a function.
        tchecker.addProblem(ErrMsg.RESOLVE_NOT_IN_FUNC_SCOPE, position, entry.getAbsName(), getAbsName());
        throw new SemanticException();
    }

    @Override
    protected boolean isSubScope() {
        return true;
    }

    @Override
    protected boolean isRootScope() {
        return false;
    }
}
