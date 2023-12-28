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

import static org.eclipse.escet.cif.codegen.updates.tree.UpdateData.generateAssignment;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.copy;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.eclipse.escet.cif.cif2cif.AddDefaultInitialValues;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimStateEvtExclInvs;
import org.eclipse.escet.cif.cif2cif.LinearizeMerge;
import org.eclipse.escet.cif.cif2cif.MergeEnums;
import org.eclipse.escet.cif.cif2cif.PrintFileIntoDecls;
import org.eclipse.escet.cif.cif2cif.RemoveCifSvgDecls;
import org.eclipse.escet.cif.cif2cif.RemovePositionInfo;
import org.eclipse.escet.cif.cif2cif.SimplifyOthers;
import org.eclipse.escet.cif.cif2cif.SimplifyValuesNoRefsOptimized;
import org.eclipse.escet.cif.cif2cif.SvgFileIntoDecls;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.options.CodePrefixOption;
import org.eclipse.escet.cif.codegen.options.OutputDirOption;
import org.eclipse.escet.cif.codegen.options.TargetLanguage;
import org.eclipse.escet.cif.codegen.simulink.SimulinkCodeGenPreChecker;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.codegen.updates.AlgDerInvalidations;
import org.eclipse.escet.cif.codegen.updates.VariableWrapper;
import org.eclipse.escet.cif.codegen.updates.tree.SingleVariableAssignment;
import org.eclipse.escet.cif.common.CifCollectUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifValidationUtils;
import org.eclipse.escet.cif.common.ConstantOrderer;
import org.eclipse.escet.cif.common.StateInitVarOrderer;
import org.eclipse.escet.cif.metamodel.cif.IoDecl;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgIn;
import org.eclipse.escet.cif.metamodel.cif.cifsvg.SvgOut;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.TypeDecl;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.print.Print;
import org.eclipse.escet.cif.metamodel.java.CifConstructors;
import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.FileAppStream;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * Base class for code generators for various targets.
 *
 * <p>
 * Derived implements must implement all abstract methods. They may also override {@link #init}. They may also assume
 * the following {@link #replacements} are always available for all targets:
 * <ul>
 * <li>prefix: Supplied by {@link CodePrefixOption}. Is used to prefix file names, identifiers in the code, etc. Is a
 * valid CIF identifier, see {@link CifValidationUtils#isValidIdentifier}.</li>
 * </ul>
 * </p>
 */
public abstract class CodeGen {
    /** Default code indent amount, as number of spaces. */
    private static final int DEFAULT_INDENT = 4;

    /** The target language. */
    protected final TargetLanguage language;

    /** Number of spaces to use for indenting in the generated code. */
    protected final int indent;

    /** Expression code generator. */
    protected ExprCodeGen exprCodeGen;

    /** Type code generator. */
    protected TypeCodeGen typeCodeGen;

    /**
     * Code replacements. Mapping from replacement names to the code fragments to replace them with. Is {@code null} if
     * not available.
     */
    public Map<String, String> replacements;

    /**
     * Mapping from declarations of the specification to their original absolute names (without escaping), in the input
     * specification, before preprocessing. {@code null} if not available. Empty until filled with actual data.
     */
    protected Map<Declaration, String> origDeclNames;

    /**
     * Mapping from CIF objects to their names in the target code. Is {@code null} if not available. Keys must be
     * {@link CifTextUtils#getName named} CIF objects.
     */
    private Map<PositionObject, String> targetNameMap;

    /**
     * Names that are already in use for the target code. Typically includes the target language keywords and other
     * reserved identifiers. Is {@code null} if not available.
     *
     * <p>
     * Set gets expanded with generated identifiers during code generation.
     * </p>
     */
    private Set<String> targetNames;

    /** The location pointer variables of the original specification. */
    protected List<DiscVariable> lpVariables;

    /**
     * The constants of the specification. {@code null} if not available, empty until filled with actual data.
     *
     * <p>
     * The constants are ordered, so that they can be initialized in the order as given in this list, making sure that
     * constants can be initialized using other constants.
     * </p>
     */
    protected List<Constant> constants;

    /**
     * The events of the specification. {@code null} if not available, empty until filled with actual data.
     *
     * <p>
     * Due to linearization, events have no data types. The controllability of events should be ignored.
     * </p>
     */
    protected List<Event> events;

    /**
     * The state variables of the specification. {@code null} if not available, empty until filled with actual data.
     *
     * <p>
     * The state variables are ordered, so that they can be initialized in the order as given in this list, making sure
     * that state variables can be initialized using other state variables.
     * </p>
     *
     * <p>
     * Contains both the discrete variables and the {@link #contVars continuous variables}. Due to preprocessing, all
     * variables have exactly one explicit initial value.
     * </p>
     */
    protected List<Declaration> stateVars;

    /**
     * The continuous variables of the specification. {@code null} if not available, empty until filled with actual
     * data. The continuous variables are also part of {@link #stateVars}.
     *
     * <p>
     * Continuous variables always have an explicit derivative after linearization. There is no need to look for
     * equations.
     * </p>
     */
    protected List<ContVariable> contVars;

    /**
     * The algebraic variables of the specification. {@code null} if not available, empty until filled with actual data.
     *
     * <p>
     * Algebraic variables always have an explicit defining expression after linearization. There is no need to look for
     * equations.
     * </p>
     */
    protected List<AlgVariable> algVars;

    /** The input variables of the specification. {@code null} if not available, empty until filled with actual data. */
    protected List<InputVariable> inputVars;

    /**
     * The internal user-defined functions of the specification. {@code null} if not available, empty until filled with
     * actual data.
     *
     * <p>
     * It is a precondition that the specification contains no external user-defined functions.
     * </p>
     */
    protected List<InternalFunction> functions;

    /**
     * The print declarations of the specification. {@code null} if not available, empty until filled with actual data.
     *
     * <p>
     * Each print declaration contains an explicit print file declaration as part of itself, due to preprocessing. There
     * is no need to look for separate print file declarations.
     * </p>
     */
    protected List<Print> printDecls;

    /**
     * The SVG declarations of the specification. {@code null} if not available, empty until filled with actual data.
     *
     * <p>
     * Each SVG declaration contains an explicit SVG file declaration as part of itself, due to preprocessing. There is
     * no need to look for separate SVG file declarations.
     * </p>
     */
    protected List<IoDecl> svgDecls;

    /**
     * The edges of the specification. {@code null} if not available, empty until filled with actual data.
     *
     * <p>
     * Each linearized edge has at most one guard predicate, and exactly one edge event. There are no implicit 'tau'
     * edges. No send/receive edges are present. All linearized edges are self loops. There are no urgent linearized
     * edges, as it is a precondition that there is no urgent locations or edges in the input specification.
     * </p>
     */
    protected List<Edge> edges;

    /**
     * Reserved ranges of {@link #tmpvarNumber}, contains the first available numbers after reserving. Note that
     * reserving itself does not increase the first number.
     */
    protected List<Integer> reservedTmpvarRanges = list();

    /** Counter for creating unique variable suffixes, contains the first available number. */
    protected int tmpvarNumber = 1;

    /**
     * Invalidation information about algebraic variables and derivative expressions as a result of updating a variable
     * or time.
     *
     * <p>
     * Lazily constructed.
     * </p>
     */
    AlgDerInvalidations invalidations = null;

    /**
     * Constructor for the {@link CodeGen} class.
     *
     * <p>
     * Generated code uses {@link #DEFAULT_INDENT} space indenting.
     * </p>
     *
     * @param language The target language.
     */
    protected CodeGen(TargetLanguage language) {
        this(language, DEFAULT_INDENT);
    }

    /**
     * Constructor for the {@link CodeGen} class.
     *
     * @param language The target language.
     * @param indent Number of spaces to use for indenting in the generated code.
     */
    protected CodeGen(TargetLanguage language, int indent) {
        this.language = language;
        this.indent = indent;
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
        MemoryCodeBox code = new MemoryCodeBox(indent);
        for (int i = 0; i < numIndents; i++) {
            code.indent();
        }
        return code;
    }

    /**
     * Get an expression code generator for generating expression code in the target language.
     *
     * @return The retrieved expression code generator.
     */
    protected abstract ExprCodeGen getExpressionCodeGenerator();

    /**
     * Get a type code generator for generating type code in the target language.
     *
     * @return The retrieved type code generator.
     */
    protected abstract TypeCodeGen getTypeCodeGenerator();

    /**
     * Returns the code to refer to the storage for the variable in the target language.
     *
     * <p>
     * By default, uses the {@link #getTargetVariableName}. Code generators for specific target languages may override
     * this.
     * </p>
     *
     * @param obj The CIF object. Must be a {@link CifTextUtils#getName named} object.
     * @return The code.
     */
    public String getTargetRef(PositionObject obj) {
        return getTargetVariableName(obj);
    }

    /**
     * Get the name of the variable for the target language to use for the given CIF object.
     *
     * @param obj The CIF object. Must be a {@link CifTextUtils#getName named} object.
     * @return Name of the variable to use.
     */
    public String getTargetVariableName(PositionObject obj) {
        // Use previous result if available.
        String targetName = targetNameMap.get(obj);
        if (targetName != null) {
            return targetName;
        }

        // Get original name, and derive target language name from it.
        targetName = origDeclNames.get(obj);
        if (targetName == null) {
            // New object, introduced by preprocessing and/or linearization.
            targetName = CifTextUtils.getName(obj);
        }
        targetName = targetName.replace('.', '_');

        // Avoid conflicts with other code in generated files. Reserve
        // names starting with '_' but not ending in it for other code as
        // well.
        targetName = targetName + "_";

        // If target language name already in use (or a keyword),
        // choose a different name.
        if (targetNames.contains(targetName)) {
            String oldName = targetName;
            targetName = CifScopeUtils.getUniqueName(targetName, targetNames, Collections.emptySet());

            String origName = origDeclNames.get(obj);
            if (origName == null) {
                origName = CifTextUtils.getName(obj);
            }
            warn("%s identifier \"%s\" is renamed to \"%s\" for CIF object \"%s\".", language.readableName, oldName,
                    targetName, origName);
        }

        // Update used names information now that name is fixed.
        targetNames.add(targetName);
        targetNameMap.put(obj, targetName);
        return targetName;
    }

    /**
     * Generate and write code.
     *
     * @param spec The CIF specification for which to generate code.
     * @param cifSpecFileDir The absolute local file system path of the directory that contains the CIF specification.
     * @param outputPath The absolute or relative local file system path to the output directory to which the code files
     *     will be written.
     */
    public void generate(Specification spec, String cifSpecFileDir, String outputPath) {
        // Initialization.
        init();

        // Prepare for code generation.
        prepare(spec);

        // Create code context.
        CodeContext ctxt = new CodeContext(this);

        // Generate the code, putting them in the replacements mapping.
        generateCode(ctxt, spec, cifSpecFileDir);

        // Finalize the generated code.
        postGenerate(ctxt);

        // Write the output.
        write(outputPath);

        // Cleanup.
        cleanup();
    }

    /**
     * Initialize code generation.
     *
     * <p>
     * Derived classes may override this method to perform additional initializations. However, derived implementations
     * must start with calling {@code super.init}.
     * </p>
     */
    protected void init() {
        replacements = map();
        replacements.put("prefix", CodePrefixOption.getPrefix());

        constants = list();
        origDeclNames = map();
        targetNameMap = map();
        targetNames = copy(getReservedTargetNames());
        events = list();
        stateVars = list();
        contVars = list();
        algVars = list();
        inputVars = list();
        functions = list();
        printDecls = list();
        svgDecls = list();
        edges = list();

        // Initialize the expression and type code generators.
        exprCodeGen = getExpressionCodeGenerator();
        typeCodeGen = getTypeCodeGenerator();
        typeCodeGen.init();
    }

    /**
     * Get the names that are already in use in the target language. These names are avoided while generating unique
     * identifiers.
     *
     * @return Names to avoid while generating unique identifiers.
     * @see #targetNames
     */
    protected abstract Set<String> getReservedTargetNames();

    /**
     * Retrieve variable info from a declaration, for reading or writing the variable.
     *
     * @param decl Declaration to inspect.
     * @param ctxt The code generation context.
     * @return The variable information.
     */
    protected VariableInformation getVarInfo(Declaration decl, CodeContext ctxt) {
        String targetName = getTargetRef(decl);
        String varName = getTargetVariableName(decl);

        String origName = origDeclNames.get(decl);
        if (origName == null) {
            // New object, introduced by preprocessing and/or linearization.
            origName = decl.getName();
        }

        if (decl instanceof AlgVariable) {
            AlgVariable algVar = (AlgVariable)decl;
            TypeInfo ti = typeCodeGen.typeToTarget(algVar.getType(), ctxt);
            return new VariableInformation(ti, origName, varName, targetName, false);
        } else if (decl instanceof Constant) {
            Constant constVar = (Constant)decl;
            TypeInfo ti = typeCodeGen.typeToTarget(constVar.getType(), ctxt);
            return new VariableInformation(ti, origName, varName, targetName, false);
        } else if (decl instanceof ContVariable) { // Both continuous and derivative value.
            TypeInfo ti = typeCodeGen.typeToTarget(newRealType(), ctxt);
            return new VariableInformation(ti, origName, varName, targetName, false);
        } else if (decl instanceof EnumDecl) {
            EnumDecl enumDecl = (EnumDecl)decl;
            TypeInfo ti = typeCodeGen.typeToTarget(newEnumType(enumDecl, null), ctxt);
            return new VariableInformation(ti, origName, varName, targetName, false);
        } else if (decl instanceof InputVariable) {
            InputVariable inputVar = (InputVariable)decl;
            TypeInfo ti = typeCodeGen.typeToTarget(inputVar.getType(), ctxt);
            return new VariableInformation(ti, origName, varName, targetName, false);
        } else if (decl instanceof DiscVariable) {
            DiscVariable discVar = (DiscVariable)decl;
            TypeInfo ti = typeCodeGen.typeToTarget(discVar.getType(), ctxt);
            return new VariableInformation(ti, origName, varName, targetName, false);
        }

        throw new RuntimeException("Unexpected kind of declaration encountered: " + str(decl));
    }

    /**
     * Construct a temporary variable for the given variable.
     *
     * @param varInfo Existing variable that needs a copy.
     * @return Variable information of the created variable.
     */
    public VariableInformation makeTempVariable(VariableInformation varInfo) {
        String targetName = fmt("%stmp%d", varInfo.targetVariableName, tmpvarNumber);
        tmpvarNumber++;
        return new VariableInformation(varInfo.typeInfo, varInfo.name, targetName, targetName, true);
    }

    /**
     * Construct a temporary variable for the given type information.
     *
     * @param ti Type of the temporary variable.
     * @param name Part of the name of the new variable.
     * @return Variable information of the created variable.
     */
    public VariableInformation makeTempVariable(TypeInfo ti, String name) {
        String targetName = fmt("%s%d", name, tmpvarNumber);
        tmpvarNumber++;
        return new VariableInformation(ti, name, targetName, targetName, true);
    }

    /**
     * Get the number of created temporary variables since the last reset or reserve.
     *
     * @return The number of created temporary variables since the last reset or reserve.
     */
    public int countCreatedTempVariables() {
        if (reservedTmpvarRanges.isEmpty()) {
            return tmpvarNumber - 1;
        } else {
            return tmpvarNumber - last(reservedTmpvarRanges);
        }
    }

    /**
     * Reserve currently used numbers of temporary variables. These numbers will not be used again, until the
     * corresponding {@link #unreserveTempVariables} has been executed.
     *
     * @return Range of reserved variables, to be used for unreserving.
     */
    public int reserveTempVariables() {
        // For debugging, increment the variable.
        reservedTmpvarRanges.add(tmpvarNumber);
        return tmpvarNumber;
    }

    /**
     * Release the top range of the used numbers of temporary variables. After unreserving, the released temporary
     * numbers get used again.
     *
     * @param reservedValue Range of reserved variables to release, must be equal to returned value from the last call
     *     to {@link #reserveTempVariables}.
     * @see #reserveTempVariables
     */
    public void unreserveTempVariables(int reservedValue) {
        int last = reservedTmpvarRanges.size() - 1;
        Assert.check(reservedTmpvarRanges.get(last) == reservedValue);
        reservedTmpvarRanges.remove(last);
        tmpvarNumber = reservedValue; // Use released range again.
    }

    /**
     * Construct a destination from a variable.
     *
     * @param varInfo Information about the variable.
     * @return Object representing the variable as destination.
     */
    public abstract Destination makeDestination(VariableInformation varInfo);

    /**
     * Make a target-specific data value for the given value.
     *
     * @param value The value.
     * @return The target-specific data value.
     */
    public abstract DataValue makeDataValue(String value);

    /**
     * Perform an assignment to a variable, where the right hand side is required only one time, the left hand side is
     * exactly one variable, and there is no underflow or overflow to worry about.
     *
     * @param code Storage of generated code.
     * @param asgn Assignment to perform.
     * @param value Right hand side value.
     * @param readCtxt Code context for right hand side and index projections at the left hand side.
     * @param writeCtxt Code context for the assigned variable.
     */
    public abstract void performSingleAssign(CodeBox code, SingleVariableAssignment asgn, Expression value,
            CodeContext readCtxt, CodeContext writeCtxt);

    /**
     * Perform an assignment to a variable, where the right hand side is stored in a temporary variable.
     *
     * @param code Storage of generated code.
     * @param asgn Assignment to perform.
     * @param rhsText Right hand side value.
     * @param readCtxt Code context for right hand side and index projections at the left hand side.
     * @param writeCtxt Code context for the assigned variable.
     */
    public abstract void performAssign(CodeBox code, SingleVariableAssignment asgn, String rhsText,
            CodeContext readCtxt, CodeContext writeCtxt);

    /**
     * Get the set of invalidated algebraic variables and derivative expressions when updating a variable.
     *
     * @param v Variable being updated.
     * @return Set of invalidated algebraic variables and derivative expressions.
     */
    public Set<VariableWrapper> getAffectedAlgebraicDerivativeExpressions(VariableWrapper v) {
        if (invalidations == null) {
            invalidations = new AlgDerInvalidations();

            invalidations.computeAffects(algVars, contVars);
        }

        return invalidations.getAffecting(v);
    }

    /**
     * Perform cleanup.
     *
     * <p>
     * Derived classes may override this method to perform additional cleanup. However, derived implementations must
     * start with {@code super.cleanup()}.
     * </p>
     */
    protected void cleanup() {
        typeCodeGen.cleanup();

        exprCodeGen = null;
        replacements = null;

        constants = null;
        origDeclNames = null;
        targetNameMap = null;
        targetNames = null;
        events = null;
        stateVars = null;
        contVars = null;
        algVars = null;
        inputVars = null;
        functions = null;
        printDecls = null;
        edges = null;
    }

    /**
     * Initializes {@link #origDeclNames} by filling it.
     *
     * @param spec The CIF specification.
     */
    private void initOrigDeclNames(Specification spec) {
        InitOrigDeclNamesWalker walker = new InitOrigDeclNamesWalker();
        walker.initOrigDeclNames(spec);
    }

    /** Used by {@link #initOrigDeclNames}. */
    private final class InitOrigDeclNamesWalker extends CifWalker {
        /**
         * Initializes {@link #origDeclNames} by filling it.
         *
         * @param spec The CIF specification.
         */
        public void initOrigDeclNames(Specification spec) {
            walkSpecification(spec);
        }

        @Override
        protected void preprocessDeclaration(Declaration decl) {
            origDeclNames.put(decl, CifTextUtils.getAbsName(decl, false));
        }
    }

    /**
     * Prepares for code generation:
     * <ul>
     * <li>Preprocesses the CIF specification.</li>
     * <li>Executes {@link #initOrigDeclNames}.</li>
     * <li>Checks preconditions using {@link CodeGenPreChecker}.</li>
     * </ul>
     *
     * @param spec The CIF specification to preprocess.
     */
    private void prepare(Specification spec) {
        // Remove position information, for performance.
        new RemovePositionInfo().transform(spec);

        // Remove CIF/SVG declarations, for target languages where we don't
        // generate code for them. By removing the CIF/SVG declarations, we
        // don't get unsupported errors for features used in them.
        if (language != TargetLanguage.HTML) {
            RemoveCifSvgDecls removeCifSvgDecls = new RemoveCifSvgDecls();
            removeCifSvgDecls.transform(spec);
            if (removeCifSvgDecls.haveAnySvgInputDeclarationsBeenRemoved()) {
                warn("The specification contains CIF/SVG input declarations. These will be ignored.");
            }
        }

        // Eliminate component definition/instantiation, to get a concrete
        // specification, without via references, etc.
        new ElimComponentDefInst().transform(spec);

        // Eliminate state/event exclusion invariants, to avoid having to
        // handle them.
        new ElimStateEvtExclInvs().transform(spec);

        // Initialize original declaration names. With component
        // definition/instantiation eliminated, we have all concrete objects.
        // Doing this before linearization ensures the absolute names are
        // intact.
        initOrigDeclNames(spec);

        // Simplify specification. Don't simplify references, as we for
        // instance don't want to inline large constant arrays. We do use
        // the optimized variant, for performance reasons.
        new SimplifyValuesNoRefsOptimized().transform(spec);
        new SimplifyOthers().transform(spec);

        // Check preconditions. Do this before linearization, to ensure most
        // of the specification is still intact as it originally was. Do this
        // after elimination of component definition/instantiation, to make it
        // easier to check. Do this after some simplification, to support more
        // specifications.
        switch (language) {
            case JAVA:
            case JAVASCRIPT:
            case HTML:
            case C89:
            case C99:
                new CodeGenPreChecker().check(spec);
                break;

            case SIMULINK:
                new SimulinkCodeGenPreChecker().check(spec);
                break;

            default:
                throw new RuntimeException("Unknown language: " + str(language));
        }

        // Linearize, to get rid of parallelism.
        //
        // To be consistent with the precondition check that automata must have exactly one initial location, we allow
        // linearization to optimize initialization of newly introduced location pointers, by considering the values of
        // variables and so on when determining whether the location pointer has a single initial value.
        LinearizeMerge linearize = new LinearizeMerge(true);
        linearize.transform(spec);
        lpVariables = linearize.getLPVariables();

        // Extend original object names mapping with newly introduced location pointer variables.
        for (Entry<DiscVariable, String> entry: linearize.getLpVarToAbsAutNameMap().entrySet()) {
            String prev = origDeclNames.put(entry.getKey(), entry.getValue());
            Assert.check(prev == null);
        }

        // Push print file declarations inwards for easier code generation.
        new PrintFileIntoDecls().transform(spec);

        // Push SVG file declarations inwards for easier code generation.
        new SvgFileIntoDecls().transform(spec);

        // Merge enumerations into a single enumeration for easier code generation.
        new MergeEnums().transform(spec);

        // Simplify again, as linearization may introduce a lot of unnecessary
        // 'true' literals etc. Don't simplify references, as we for instance
        // don't want to inline large constant arrays. We do use the optimized
        // variant, for performance reasons.
        new SimplifyValuesNoRefsOptimized().transform(spec);

        // Add default initial values for easier code generation.
        new AddDefaultInitialValues().transform(spec);
    }

    /**
     * Generate code (replacements).
     *
     * @param ctxt The code generation context.
     * @param spec The CIF specification for which to generate code (replacements).
     * @param cifSpecFileDir The absolute local file system path of the directory that contains the CIF specification.
     */
    private void generateCode(CodeContext ctxt, Specification spec, String cifSpecFileDir) {
        // For the specification, we ignore the component definitions (have
        // already been eliminated), equations (eliminated due to
        // linearization), initialization predicates (should not exist, or
        // are trivially 'true', precondition), state invariants (should not
        // exist, or are trivially 'true', precondition), state/event
        // exclusion invariants (have already been eliminated) and marker
        // predicates (have no effect).
        Assert.check(spec.getDefinitions().isEmpty());

        // Get automaton. There is exactly one (precondition and result of
        // linearization). We ignore the alphabet (equal to the events on the
        // edges, after linearization), monitors (no longer present after
        // linearizaton), initialization predicates (should not exist, or
        // are trivially 'true', precondition), state invariants (should not
        // exist, or are trivially 'true', precondition), state/event
        // exclusion invariants (have already been eliminated) and marker
        // predicates (have no effect).
        List<Automaton> automata = listc(1);
        CifCollectUtils.collectAutomata(spec, automata);
        Assert.check(automata.size() == 1);
        Automaton aut = first(automata);

        // Get declarations. We ignore external user-defined functions (should
        // not exist, precondition).
        List<Declaration> decls = list();
        CifCollectUtils.collectDeclarations(spec, decls);

        List<EnumDecl> enumDecls = list();
        for (Declaration decl: decls) {
            if (decl instanceof Constant) {
                constants.add((Constant)decl);
            } else if (decl instanceof Event) {
                events.add((Event)decl);
            } else if (decl instanceof DiscVariable) {
                stateVars.add(decl);
            } else if (decl instanceof ContVariable) {
                stateVars.add(decl);
                contVars.add((ContVariable)decl);
            } else if (decl instanceof AlgVariable) {
                algVars.add((AlgVariable)decl);
            } else if (decl instanceof InputVariable) {
                inputVars.add((InputVariable)decl);
            } else if (decl instanceof InternalFunction) {
                functions.add((InternalFunction)decl);
            } else if (decl instanceof EnumDecl) {
                enumDecls.add((EnumDecl)decl);
            } else if (decl instanceof TypeDecl) {
                // Ignore. Is just an alias for a type.
            } else {
                throw new RuntimeException("Unexpected decl: " + decl);
            }
        }

        // Order declarations.
        constants = new ConstantOrderer().computeOrder(constants);
        stateVars = new StateInitVarOrderer().computeOrder(stateVars);

        // After linearization, enumerations have been merged to a single
        // enumeration. If we only have automata with exactly one location
        // it might be that there is no enum declaration. Otherwise, there
        // should be at most one enum declaration.
        Assert.check(enumDecls.size() <= 1);

        // Make sure we always have an enumeration.
        EnumDecl enumDecl;
        if (enumDecls.isEmpty()) {
            enumDecl = CifConstructors.newEnumDecl(null,
                    list(CifConstructors.newEnumLiteral("__some_dummy_enum_literal", null)), "__some_dummy_enum_name",
                    null);
        } else {
            enumDecl = first(enumDecls);
        }

        // Generate code for the declarations.
        addConstants(ctxt);
        addEvents(ctxt);
        addStateVars(ctxt);
        addContVars(ctxt);
        addAlgVars(ctxt);
        addInputVars(ctxt);
        addFunctions(ctxt);
        addEnum(enumDecl, ctxt);

        // Get code for the print declarations.
        List<IoDecl> ioDecls = list();
        CifCollectUtils.collectIoDeclarations(spec, ioDecls);

        for (IoDecl decl: ioDecls) {
            if (decl instanceof Print) {
                printDecls.add((Print)decl);
            }
        }

        addPrints(ctxt);

        // Get code for the SVG declarations.
        for (IoDecl decl: ioDecls) {
            if (decl instanceof SvgIn || decl instanceof SvgOut) {
                svgDecls.add(decl);
            }
        }

        addSvgDecls(ctxt, cifSpecFileDir);

        // Get single linearized location. We ignore the initialization
        // predicates (should be trivially 'true', precondition), state
        // invariants (should not exist, or are trivially 'true',
        // precondition), state/event exclusion invariants (have already
        // been eliminated), marker predicates (have no effect), urgency
        // (should have no urgency, precondition), equations (have all
        // been eliminated by linearization), and name (irrelevant after
        // linearization).
        Assert.check(aut.getLocations().size() == 1);
        Location loc = first(aut.getLocations());

        // Generate code for the edges, after check the assumptions. We
        // ignore the urgency (should not exist, precondition), and target
        // location (should not be set, all self loops after linearization).
        edges = loc.getEdges();
        for (Edge edge: edges) {
            Assert.check(edge.getGuards().size() <= 1);
            Assert.check(!edge.isUrgent());
            Assert.check(edge.getEvents().size() == 1);
            Assert.check(edge.getTarget() == null);
            Assert.check(!(edge.getEvents().get(0) instanceof EdgeSend));
            Assert.check(!(edge.getEvents().get(0) instanceof EdgeReceive));
        }
        addEdges(ctxt);
    }

    /**
     * Add code (substitutions) for the {@link #constants}.
     *
     * @param ctxt The code generation context.
     */
    protected abstract void addConstants(CodeContext ctxt);

    /**
     * Add code (substitutions) for the {@link #events}.
     *
     * @param ctxt The code generation context.
     */
    protected abstract void addEvents(CodeContext ctxt);

    /**
     * Add code (substitutions) for the {@link #stateVars state variables}.
     *
     * @param ctxt The code generation context.
     */
    protected abstract void addStateVars(CodeContext ctxt);

    /**
     * Add code (substitutions) for the {@link #contVars continuous variables}.
     *
     * @param ctxt The code generation context.
     */
    protected abstract void addContVars(CodeContext ctxt);

    /**
     * Add code (substitutions) for the {@link #algVars algebraic variables}.
     *
     * @param ctxt The code generation context.
     */
    protected abstract void addAlgVars(CodeContext ctxt);

    /**
     * Add code (substitutions) for the {@link #inputVars input variables}.
     *
     * @param ctxt The code generation context.
     */
    protected abstract void addInputVars(CodeContext ctxt);

    /**
     * Add code (substitutions) for the {@link #functions internal user-defined functions}.
     *
     * @param ctxt The code generation context.
     */
    protected abstract void addFunctions(CodeContext ctxt);

    /**
     * Add code (substitutions) for the merged enumeration (at most one).
     *
     * @param enumDecl The merged enumeration declaration.
     * @param ctxt The code generation context.
     */
    protected abstract void addEnum(EnumDecl enumDecl, CodeContext ctxt);

    /**
     * Add code (substitutions) for the {@link #printDecls print declarations}.
     *
     * @param ctxt The code generation context.
     */
    protected abstract void addPrints(CodeContext ctxt);

    /**
     * Add code (substitutions) for the {@link #svgDecls SVG declarations}.
     *
     * @param ctxt The code generation context.
     * @param cifSpecFileDir The absolute local file system path of the directory that contains the CIF specification.
     */
    protected abstract void addSvgDecls(CodeContext ctxt, String cifSpecFileDir);

    /**
     * Add code (substitutions) for the {@link #edges}.
     *
     * <p>
     * To add code for updates, use {@link #addUpdates}.
     * </p>
     *
     * @param ctxt The code generation context.
     */
    protected abstract void addEdges(CodeContext ctxt);

    /**
     * Generate code for the updates. To be used for updates on edges, by calling it from {@link #addEdges}.
     *
     * @param updates The updates.
     * @param code The already generated code. Is extended in-place.
     * @param ctxt The code generation context.
     */
    protected void addUpdates(List<Update> updates, CodeBox code, CodeContext ctxt) {
        Assert.check(!updates.isEmpty());
        generateAssignment(updates, code, ctxt);
    }

    /**
     * Get a fresh generator for update 'if' statements in the target language.
     *
     * @return The created target language 'if' statement generator.
     */
    protected abstract IfElseGenerator getIfElseUpdateGenerator();

    /**
     * Add code for beginning a new local scope, for an assignment. Should first add code for beginning a new local
     * scope, and then indent for the body of the scope.
     *
     * @param code The code generated so far. Is modified in-place.
     */
    protected abstract void addUpdatesBeginScope(CodeBox code);

    /**
     * Add code for ending a new local scope, for an assignment. Should first dedent for the body of the local scope,
     * and then add code for ending the local scope.
     *
     * @param code The code generated so far. Is modified in-place.
     */
    protected abstract void addUpdatesEndScope(CodeBox code);

    /**
     * Returns a mapping from template filenames to output file name postfixes.
     *
     * <p>
     * For a code generator class 'some.pkg.AbcCodeGen' for the 'abc' language, the package that contains the templates
     * is assumed to be named 'some.pkg.templates'. The keys of this mapping are used as template filenames, within that
     * package.
     * </p>
     *
     * <p>
     * The values of the mapping are used as postfixes for the output file names. They are prefixed with the
     * {@link CodePrefixOption code prefix}. The output files are all written to the {@link OutputDirOption output
     * directory}.
     * </p>
     *
     * <p>
     * For instance, for an entry ("main.txt", ".java"), a template file named "main.txt" is used as input, and a file
     * named "[prefix].java" is used as output. For an entry ("math.txt", "Math.java"), a template file named "math.txt"
     * is used as input, and a file named "[prefix]Math.java" is used as output.
     * </p>
     *
     * @return The mapping.
     */
    protected abstract Map<String, String> getTemplates();

    /**
     * Finalize the generated code, after the code has been generated, but before it is written.
     *
     * <p>
     * By default does nothing. Derived classes may override this method to finalize the generated code. For instance,
     * they could collect code in a {@link CodeBox} during code generation and in this method store it in the
     * {@link #replacements}.
     * </p>
     *
     * @param ctxt The code generation context.
     */
    protected void postGenerate(CodeContext ctxt) {
        // By default, nothing is done.
    }

    /**
     * Write the code files to disk.
     *
     * @param path The absolute or relative local file system path to the output directory to which the code files will
     *     be written.
     */
    private void write(String path) {
        // Get templates.
        Map<String, String> templates = getTemplates();

        // Create output directory, if it doesn't exist yet.
        String absPath = Paths.resolve(path);
        Path nioAbsPath = java.nio.file.Paths.get(absPath);
        if (!Files.isDirectory(nioAbsPath)) {
            try {
                Files.createDirectories(nioAbsPath);
            } catch (IOException ex) {
                String msg = fmt("Failed to create output directory \"%s\" for the generated code.", path);
                throw new InputOutputException(msg, ex);
            }
        }

        // Write templates.
        boolean[] used = new boolean[replacements.size()];
        for (Entry<String, String> template: templates.entrySet()) {
            // Get output file path.
            String fileName = template.getValue();
            fileName = replacements.get("prefix") + fileName;
            String filePath = path + File.separator + fileName;
            String absFilePath = Paths.resolve(filePath);

            // Write code.
            String resName = getResourceName(template.getKey());
            Set<Entry<String, String>> replaces = replacements.entrySet();
            ClassLoader classLoader = getClass().getClassLoader();
            try (InputStream fstream = classLoader.getResourceAsStream(resName);
                 InputStream istream = new BufferedInputStream(fstream);
                 AppStream ostream = new FileAppStream(filePath, absFilePath))
            {
                // Process all lines of the template.
                LineIterator lines = IOUtils.lineIterator(istream, "UTF-8");
                while (lines.hasNext()) {
                    // Read the next line.
                    String line = lines.nextLine();

                    // Apply replacements. We skip this for empty lines, as then there is nothing to replace.
                    if (!line.isEmpty()) {
                        // Repeatedly apply replacements, as the replacement may also require replacements.
                        while (true) {
                            boolean anythingReplaced = false;

                            // Apply each replacement, one by one.
                            int i = 0;
                            for (Entry<String, String> replace: replaces) {
                                // Get marker to replace, and replacement text.
                                String name = replace.getKey();
                                String text = replace.getValue();
                                String marker = fmt("${%s}", name);

                                // If we will replace anything, mark that.
                                if (!used[i] && line.contains(marker)) {
                                    used[i] = true;
                                    anythingReplaced = true;
                                }
                                i++;

                                // Perform replacement.
                                line = line.replace(marker, text);
                            }

                            // Stop once no more replacements are possible for the line.
                            if (!anythingReplaced) {
                                break;
                            }
                        }
                    }

                    // Output the line.
                    ostream.println(line);
                }
            } catch (IOException ex) {
                // Should not have a read error for templates.
                String msg = "Template read error: " + resName;
                throw new RuntimeException(msg, ex);
            }
        }

        // Make sure all replacements are used.
        int i = 0;
        for (Entry<String, String> replace: replacements.entrySet()) {
            if (!used[i]) {
                String msg = "Unused replacement: " + replace.getKey();
                throw new RuntimeException(msg);
            }
            i++;
        }
    }

    /**
     * Returns the resource name for a given template filename.
     *
     * @param templateFileName The filename of the template.
     * @return The resource name for the template.
     */
    protected String getResourceName(String templateFileName) {
        String resName = getClass().getPackage().getName();
        resName = resName.replace(".", "/");
        resName += fmt("/templates/%s", templateFileName);
        return resName;
    }

    /**
     * Returns the content of a template.
     *
     * @param templateFileName The filename of the template.
     * @return The lines of the template.
     */
    protected List<String> readTemplate(String templateFileName) {
        String resName = getResourceName(templateFileName);
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream rstream = classLoader.getResourceAsStream(resName);
             InputStream istream = new BufferedInputStream(rstream))
        {
            return IOUtils.readLines(istream, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            // Should not have a read error for templates.
            throw new RuntimeException("Template read error: " + resName, ex);
        }
    }
}
