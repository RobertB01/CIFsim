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

package org.eclipse.escet.cif.plcgen.conversion.expressions;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newCastExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newConstant;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newConstantExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newContVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newContVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDictExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newElifExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumLiteral;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEnumLiteralExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newField;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFieldExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFuncType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFunctionCallExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newFunctionExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIfExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInputVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newInputVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newListExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newListType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocationExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newProjectionExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newRealType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSliceExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStdLibFunctionExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newStringExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTimeExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newTupleType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newUnaryExpression;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.FieldExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StdLibFunction;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.Field;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.plcgen.PlcGenSettings;
import org.eclipse.escet.cif.plcgen.conversion.ModelTextGenerator;
import org.eclipse.escet.cif.plcgen.generators.CifProcessor;
import org.eclipse.escet.cif.plcgen.generators.NameGenerator;
import org.eclipse.escet.cif.plcgen.generators.PlcCodeStorage;
import org.eclipse.escet.cif.plcgen.generators.TypeGenerator;
import org.eclipse.escet.cif.plcgen.generators.VariableStorage;
import org.eclipse.escet.cif.plcgen.model.declarations.PlcVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcEnumLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression.PlcProjection;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression.PlcStructProjection;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcFuncNotation;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;
import org.eclipse.escet.cif.plcgen.model.types.PlcArrayType;
import org.eclipse.escet.cif.plcgen.model.types.PlcDerivedType;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.options.PlcNumberBits;
import org.eclipse.escet.cif.plcgen.targets.PlcBaseTarget;
import org.eclipse.escet.cif.plcgen.targets.PlcTargetType;
import org.eclipse.escet.cif.plcgen.writers.Writer;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.output.BlackHoleOutputProvider;
import org.eclipse.escet.common.java.output.WarnOutput;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for the expression generator. */
@SuppressWarnings("javadoc")
public class ExprGeneratorTest {
    private static ModelTextGenerator modelToText = new ModelTextGenerator();

    private TestPlcTarget target;

    private ExprGenerator exprGen;

    // in CIF:
    // - const bool fixed = false;
    // - input int theInput;
    // - disc real flatDisc;
    // - cont timer der 1.0;
    // - automaton *: location here; end [[only the location is created]]
    // - disc tuple(real field1, field2, field3) tupVar;

    private static Constant constantVar = newConstant(null, "fixed", null, newBoolType(),
            newBoolExpression(null, newBoolType(), false));

    private static InputVariable inputVar = newInputVariable(null, "theInput", null, newIntType());

    private static DiscVariable discVar = newDiscVariable(null, "flatDisc", null, newRealType(), null);

    private static ContVariable contVar = newContVariable(null, newRealExpression(null, newRealType(), "1.0"), "timer",
            null, null);

    private static DiscVariable tupVar = newDiscVariable(null, "tupVar", null, makeTupleType(3), null);

    private static TupleType makeTupleType(int length) {
        List<Field> fields = listc(length);
        while (fields.size() < length) {
            fields.add(newField("field" + (fields.size() + 1), null, newRealType()));
        }
        return newTupleType(fields, null);
    }

    private static DiscVariableExpression makeDiscVarExpr() {
        return newDiscVariableExpression(null, newRealType(), discVar);
    }

    @BeforeEach
    public void setup() {
        target = new TestPlcTarget();
        CifDataProvider cifDataProvider = new TestCifDataProvider();
        exprGen = new ExprGenerator(target, cifDataProvider);
    }

    /** PLC target for testing the expression generator. */
    private static class TestPlcTarget extends PlcBaseTarget {
        public boolean supportsLog = true;

        private TypeGenerator testTypeGenerator = new TestTypeGenerator();

        private NameGenerator testNameGenerator = new TestNameGenerator();

        public TestPlcTarget() {
            super(PlcTargetType.IEC_61131_3);

            // Configure the target.
            String projectName = "projName";
            String configurationName = "confName";
            String resourceName = "resName";
            String plcTaskName = "taskName";
            int taskCyceTime = 1;
            int priority = 1;
            String inputPath = "input/path";
            String outputPath = "output/path";
            String ioTablePath = "io/path";
            PlcNumberBits intSize = PlcNumberBits.BITS_32;
            PlcNumberBits realSize = PlcNumberBits.BITS_64;
            boolean simplifyValues = false;
            ConvertEnums enumConversion = ConvertEnums.NO;
            Supplier<Boolean> shouldTerminate = () -> false;
            boolean warnOnRename = false;
            WarnOutput warnOutput = new BlackHoleOutputProvider().getWarnOutput();

            PlcGenSettings settings = new PlcGenSettings(projectName, configurationName, resourceName, plcTaskName,
                    taskCyceTime, priority, null, inputPath, "/" + inputPath, "/" + outputPath, ioTablePath,
                    "/" + ioTablePath, intSize, realSize, simplifyValues, enumConversion, shouldTerminate, warnOnRename,
                    warnOutput);
            setup(settings);
        }

        @Override
        public EnumSet<PlcFuncNotation> getSupportedFuncNotations(PlcFuncOperation funcOper, int numArgs) {
            // LOG support follows the 'supportsLog' variable.
            if (funcOper == PlcFuncOperation.STDLIB_LOG && !supportsLog) {
                return PlcFuncNotation.UNSUPPORTED;
            }
            return super.getSupportedFuncNotations(funcOper, numArgs);
        }

        @Override
        public CifProcessor getCifProcessor() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        public VariableStorage getVarStorage() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        public TypeGenerator getTypeGenerator() {
            return testTypeGenerator;
        }

        @Override
        public PlcCodeStorage getCodeStorage() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        public NameGenerator getNameGenerator() {
            return testNameGenerator;
        }

        @Override
        public boolean supportsArrays() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        public boolean supportsConstants() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        public boolean supportsEnumerations() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        protected int getMaxIntegerTypeSize() {
            return 32;
        }

        @Override
        protected int getMaxRealTypeSize() {
            return 64;
        }

        @Override
        public String getPathSuffixReplacement() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        protected Writer getPlcCodeWriter() {
            throw new UnsupportedOperationException("Not needed for the test.");
        }
    }

    /**
     * CIF data provider for testing the expression generator.
     *
     * <p>
     * <ul>
     * <li>CIF constant {@code X} becomes PLC {@code bool X} (works because the only constant that we have has type
     * {@code bool}).</li>
     * <li>CIF discrete variables are stored in a {@code StateStruct state} structure, although the structure itself is
     * not defined.</li>
     * <li>CIF {@code cont X} becomes variables {@code X} and {@code X_der}.</li>
     * <li>CIF location {@code X} becomes a boolean variable {@code X}.</li>
     * <li>CIF input variable becomes PLC {@code int X} (works because the only input variable that we have has type
     * {@code int}).</li>
     * </ul>
     * </p>
     */
    private static class TestCifDataProvider extends CifDataProvider {
        @Override
        public PlcExpression getValueForConstant(Constant constant) {
            return new PlcVarExpression(new PlcVariable(constant.getName(), PlcElementaryType.BOOL_TYPE));
        }

        @Override
        public PlcExpression getValueForDiscVar(DiscVariable variable) {
            // state.discvar_name
            PlcProjection fieldProj = new PlcStructProjection(variable.getName());
            return new PlcVarExpression(new PlcVariable("state", new PlcDerivedType("StateStruct")), fieldProj);
        }

        @Override
        public PlcVarExpression getAddressableForDiscVar(DiscVariable variable) {
            // newState.discvar_name
            PlcProjection fieldProj = new PlcStructProjection(variable.getName());
            return new PlcVarExpression(new PlcVariable("newState", new PlcDerivedType("StateStruct")), fieldProj);
        }

        @Override
        public PlcExpression getValueForContvar(ContVariable variable, boolean getDerivative) {
            String name = variable.getName() + (getDerivative ? "_der" : "");
            return new PlcVarExpression(new PlcVariable(name, PlcElementaryType.LREAL_TYPE));
        }

        @Override
        public PlcVarExpression getAddressableForContvar(ContVariable variable, boolean writeDerivative) {
            String name = "new_" + variable.getName() + (writeDerivative ? "_der" : "");
            return new PlcVarExpression(new PlcVariable(name, PlcElementaryType.LREAL_TYPE));
        }

        @Override
        public PlcExpression getValueForInputVar(InputVariable variable) {
            return new PlcVarExpression(new PlcVariable(variable.getName(), PlcElementaryType.DINT_TYPE));
        }

        @Override
        public PlcVarExpression getAddressableForInputVar(InputVariable variable) {
            return new PlcVarExpression(new PlcVariable(variable.getName(), PlcElementaryType.DINT_TYPE));
        }
    }

    /**
     * Name generator for testing the expression generator.
     *
     * <p>
     * Generator appends a unique number after the name.
     * </p>
     */
    private static class TestNameGenerator implements NameGenerator {
        private int nextNameSuffix = 100;

        @Override
        public String generateGlobalName(PositionObject posObject) {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        public String generateGlobalName(String initialName, boolean initialIsCifName) {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        public String generateLocalName(String initialName, Map<String, Integer> localSuffixes) {
            nextNameSuffix++;
            return initialName + String.valueOf(nextNameSuffix);
        }
    }

    /** Type generator for testing the expression generator. */
    private static class TestTypeGenerator implements TypeGenerator {
        @Override
        public PlcType convertType(CifType type) {
            if (type instanceof BoolType) {
                return PlcElementaryType.BOOL_TYPE;
            } else if (type instanceof IntType) {
                return PlcElementaryType.DINT_TYPE;
            } else if (type instanceof RealType) {
                return PlcElementaryType.LREAL_TYPE;
            } else if (type instanceof ListType lt) {
                return new PlcArrayType(0, lt.getUpper() - 1, convertType(lt.getElementType()));
            } else if (type instanceof TupleType tt) {
                return new PlcDerivedType("tupType_" + tt.getFields().size());
            }
            Assert.fail("Implement me: " + type.getClass());
            return null;
        }

        @Override
        public PlcStructType getStructureType(PlcType type) {
            if (type instanceof PlcDerivedType dt && dt.name.startsWith("tupType_")) {
                int length = Integer.valueOf(dt.name.charAt(dt.name.length() - 1));
                PlcStructType structType = new PlcStructType();
                for (int idx = 1; idx <= length; idx++) {
                    structType.fields.add(new PlcVariable("field" + idx, PlcElementaryType.LREAL_TYPE));
                }
                return structType;
            }
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        public PlcType convertEnumDecl(EnumDecl enumDecl) {
            throw new UnsupportedOperationException("Not needed for the test.");
        }

        @Override
        public PlcEnumLiteral getPlcEnumLiteral(EnumLiteral enumLit) {
            return new PlcEnumLiteral(enumLit.getName());
        }
    }

    /** Convert a variable to simple text. */
    private static String varToText(PlcVariable var) {
        return fmt("%s %s", typeToText(var.type), var.name);
    }

    private static String typeToText(PlcType type) {
        if (type instanceof PlcElementaryType et) {
            return et.name;
        } else if (type instanceof PlcArrayType at) {
            return fmt("%s[%d..%d]", typeToText(at.elemType), at.lower, at.upper);
        } else if (type instanceof PlcDerivedType dt) {
            return dt.name;
        }
        throw new UnsupportedOperationException("Not needed for the test.");
    }

    /**
     * Run the expression generator at the given expression, and return a dump of the result.
     *
     * @param expr The expression to convert.
     * @return Human-readable representation of the result.
     */
    private String runValueTest(Expression expr) {
        ExprValueResult result = exprGen.convertValue(expr);
        return resultToString(result);
    }

    /**
     * Run the expression generator for the given variable addressable expression, and return a dump of the result.
     *
     * @param expr The expression to convert.
     * @return Human-readable representation of the result.
     */
    private String runVariableAddressableTest(Expression expr) {
        ExprAddressableResult result = exprGen.convertVariableAddressable(expr);
        return resultToString(result);
    }

    /**
     * Run the expression generator for the given projected addressable expression, and return a dump of the result.
     *
     * @param expr The expression to convert.
     * @return Human-readable representation of the result.
     */
    private String runProjectedAddressableTest(Expression expr) {
        ExprAddressableResult result = exprGen.convertProjectedAddressable(expr);
        return resultToString(result);
    }

    /**
     * Convert the expression generation result to a readable text.
     *
     * @param result The result to convert.
     * @return The readable text representation of the result.
     */
    private String resultToString(ExprGenResult<?, ?> result) {
        boolean needsEmptyLine = false;
        StringBuilder sb = new StringBuilder();
        if (result.hasCodeVariables()) {
            sb.append("Code variables:\n");
            for (PlcVariable pcVar: result.codeVariables) {
                sb.append(" - " + varToText(pcVar) + ";");
                sb.append('\n');
            }
            needsEmptyLine = true;
        }

        if (result.hasCode()) {
            if (needsEmptyLine) {
                sb.append("\n");
            }

            sb.append("Code:\n");
            sb.append(modelToText.toString(result.code, "myPou", false));
            sb.append('\n');
            needsEmptyLine = true;
        }

        if (result.hasValueVariables()) {
            if (needsEmptyLine) {
                sb.append("\n");
            }

            sb.append("Value variables:\n");
            for (PlcVariable pcVar: result.valueVariables) {
                sb.append(" - " + varToText(pcVar) + ";");
                sb.append('\n');
            }
            needsEmptyLine = true;
        }

        if (needsEmptyLine) {
            sb.append("\n");
        }
        sb.append("==> " + modelToText.toString(result.value));
        return sb.toString();
    }

    @Test
    public void testBoolExpressionConversion() {
        // true
        String realText = runValueTest(newBoolExpression(null, null, true));
        String expectedText = "==> TRUE";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testIntExpressionConversion() {
        // 111
        String realText = runValueTest(newIntExpression(null, null, 111));
        String expectedText = "==> 111";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testRealExpressionConversion() {
        // 1.31
        String realText = runValueTest(newRealExpression(null, null, "1.31"));
        String expectedText = "==> 1.31";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testStringExpressionConversion() {
        assertThrows(RuntimeException.class, () -> runValueTest(newStringExpression(null, null, "abc")));
    }

    @Test
    public void testTimeExpressionConversion() {
        assertThrows(RuntimeException.class, () -> runValueTest(newTimeExpression(null, null)));
    }

    @Test
    public void testCastExpressionConversion() {
        // <real>17
        Expression child = newIntExpression(null, newIntType(), 17);
        Expression expr = newCastExpression(child, null, newRealType());
        String realText = runValueTest(expr);
        String expectedText = "==> DINT_TO_LREAL(17)";
        assertEquals(expectedText, realText);

        // Equal types, do nothing.
        expr = newCastExpression(child, null, newIntType());
        realText = runValueTest(expr);
        expectedText = "==> 17";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testUnaryExpressionConversion() {
        // not true
        Expression child = newBoolExpression(null, newBoolType(), true);
        Expression expr = newUnaryExpression(child, UnaryOperator.INVERSE, null, newBoolType());
        String realText = runValueTest(expr);
        String expectedText = "==> NOT TRUE";
        assertEquals(expectedText, realText);

        // -(1.58)
        child = newRealExpression(null, newRealType(), "1.58");
        expr = newUnaryExpression(child, UnaryOperator.NEGATE, null, newRealType());
        realText = runValueTest(expr);
        expectedText = "==> -1.58";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testBinaryImplicationConversion() {
        // true => false
        Expression left = newBoolExpression(null, newBoolType(), true);
        Expression right = newBoolExpression(null, newBoolType(), false);
        Expression expr = newBinaryExpression(left, BinaryOperator.IMPLICATION, null, right, newBoolType());
        String realText = runValueTest(expr);
        String expectedText = "==> FALSE OR NOT TRUE";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testBinaryDisjunctionConversion() {
        // true or fixed or false
        Expression left = newBoolExpression(null, newBoolType(), true);
        Expression mid = newConstantExpression(constantVar, null, newBoolType());
        Expression right = newBoolExpression(null, newBoolType(), false);
        Expression expr = newBinaryExpression(mid, BinaryOperator.DISJUNCTION, null, right, newBoolType());
        expr = newBinaryExpression(left, BinaryOperator.DISJUNCTION, null, expr, newBoolType());
        String realText = runValueTest(expr);
        String expectedText = "==> TRUE OR FALSE OR FALSE";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testBinaryLessEqualConversion() {
        // 100 <= 200.0
        Expression left = newIntExpression(null, newIntType(), 100);
        Expression right = newRealExpression(null, newRealType(), "200.0");
        Expression expr = newBinaryExpression(left, BinaryOperator.LESS_EQUAL, null, right, newBoolType());
        String realText = runValueTest(expr);
        String expectedText = "==> DINT_TO_LREAL(100) <= 200.0";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testBinaryUnEqualConversion() {
        // 100 != 200.0
        Expression left = newIntExpression(null, newIntType(), 100);
        Expression right = newRealExpression(null, newRealType(), "200.0");
        Expression expr = newBinaryExpression(left, BinaryOperator.UNEQUAL, null, right, newBoolType());
        String realText = runValueTest(expr);
        String expectedText = "==> 100 <> 200.0";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testBinaryAdditionConversion() {
        // 100 + 200.0 + 300.0
        Expression left = newIntExpression(null, newIntType(), 100);
        Expression mid = newRealExpression(null, newRealType(), "200.0");
        Expression right = newRealExpression(null, newRealType(), "300.0");
        Expression expr = newBinaryExpression(mid, BinaryOperator.ADDITION, null, right, newRealType());
        expr = newBinaryExpression(left, BinaryOperator.ADDITION, null, expr, newRealType());
        String realText = runValueTest(expr);
        String expectedText = "==> DINT_TO_LREAL(100) + 200.0 + 300.0";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testBinaryIntDivisionConversion() {
        // 100 div 200
        Expression left = newIntExpression(null, newIntType(), 100);
        Expression right = newIntExpression(null, newIntType(), 200);
        Expression expr = newBinaryExpression(left, BinaryOperator.INTEGER_DIVISION, null, right, newIntType());
        String realText = runValueTest(expr);
        String expectedText = "==> 100 / 200";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testBinaryDivisionIntIntConversion() {
        // 100 / 200
        Expression left = newIntExpression(null, newIntType(), 100);
        Expression right = newIntExpression(null, newIntType(), 200);
        Expression expr = newBinaryExpression(left, BinaryOperator.DIVISION, null, right, newIntType());
        String realText = runValueTest(expr);
        String expectedText = "==> DINT_TO_LREAL(100) / DINT_TO_LREAL(200)";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testBinaryRealDivisionConversion() {
        // 100.0 / 200
        Expression left = newRealExpression(null, newRealType(), "100.0");
        Expression right = newIntExpression(null, newIntType(), 200);
        Expression expr = newBinaryExpression(left, BinaryOperator.DIVISION, null, right, newIntType());
        String realText = runValueTest(expr);
        String expectedText = "==> 100.0 / DINT_TO_LREAL(200)";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testBinaryElementOfConversion() {
        assertThrows(RuntimeException.class, () -> {
            runValueTest(newBinaryExpression(null, BinaryOperator.ELEMENT_OF, null, null, null));
        });
    }

    @Test
    public void testIfExpressionConversion() {
        // if true: 1 elif (if true: true else false end): 2 else 3 end;

        // Inner part: if true: true else false end
        List<ElifExpression> elifs = List.of();
        Expression elseVal = newBoolExpression(null, newBoolType(), false);
        List<Expression> guards = List.of(newBoolExpression(null, newBoolType(), true));
        Expression then = newBoolExpression(null, newBoolType(), true);
        CifType resultType = newBoolType();
        Expression identityFunc = newIfExpression(elifs, elseVal, guards, null, then, resultType);

        // The entire expression.
        elifs = List.of(newElifExpression(List.of(identityFunc), null, newIntExpression(null, newIntType(), 2)));
        elseVal = newIntExpression(null, newIntType(), 3);
        guards = List.of(newBoolExpression(null, newBoolType(), true));
        then = newIntExpression(null, newIntType(), 1);
        resultType = newIntType();
        Expression ifExpr = newIfExpression(elifs, elseVal, guards, null, then, resultType);
        String realText = runValueTest(ifExpr);
        String expectedText = """
                Code:
                IF TRUE THEN
                    ifResult101 := 1;
                ELSE
                    IF TRUE THEN
                        ifResult102 := TRUE;
                    ELSE
                        ifResult102 := FALSE;
                    END_IF;
                    IF ifResult102 THEN
                        ifResult101 := 2;
                    ELSE
                        ifResult101 := 3;
                    END_IF;
                END_IF;

                Value variables:
                 - DINT ifResult101;

                ==> ifResult101""";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testArrayProjectionExpressionConversion() {
        // [false, true][1]
        ListExpression array = newListExpression(
                List.of(newBoolExpression(null, newBoolType(), false), newBoolExpression(null, newBoolType(), true)),
                null, newListType(newBoolType(), 2, null, 2));
        Expression arrProj = newProjectionExpression(array, newIntExpression(null, newIntType(), 1), null,
                newBoolType());
        String realText = runValueTest(arrProj);
        String expectedText = """
                Code:
                litArray101[0] := FALSE;
                litArray101[1] := TRUE;

                Value variables:
                 - BOOL[0..1] litArray101;

                ==> litArray101[SEL(1 >= 0, 1 + 2, 1)]""";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testStructIntProjectionExpressionConversion() {
        // (flatDisc, 1.2345)[1]
        List<Expression> fields = List.of(makeDiscVarExpr(), newRealExpression(null, newRealType(), "1.2345"));
        TupleExpression struct = newTupleExpression(fields, null, makeTupleType(2));
        Expression structProj = newProjectionExpression(struct, newIntExpression(null, newIntType(), 1), null,
                newBoolType());
        String realText = runValueTest(structProj);
        String expectedText = """
                Code:
                litStruct101.field1 := state.flatDisc;
                litStruct101.field2 := 1.2345;

                Value variables:
                 - tupType_2 litStruct101;

                ==> litStruct101.field2""";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testStructFieldProjectionExpressionConversion() {
        // (flatDisc, 1.2345)[field1]
        List<Expression> fields = List.of(makeDiscVarExpr(), newRealExpression(null, newRealType(), "1.2345"));
        TupleType tt = makeTupleType(2);
        TupleExpression struct = newTupleExpression(fields, null, tt);
        FieldExpression fe = newFieldExpression(tt.getFields().get(0), null, newRealType());
        Expression structProj = newProjectionExpression(struct, fe, null, newRealType());
        String realText = runValueTest(structProj);
        String expectedText = """
                Code:
                litStruct101.field1 := state.flatDisc;
                litStruct101.field2 := 1.2345;

                Value variables:
                 - tupType_2 litStruct101;

                ==> litStruct101.field1""";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testProjectedVarProjectionValueConversion() {
        // tupVar[0]
        DiscVariableExpression tupVarExpr = newDiscVariableExpression(null, makeTupleType(3), tupVar);
        Expression tupProj = newProjectionExpression(tupVarExpr, newIntExpression(null, newIntType(), 0), null,
                newRealType());
        String realText = runValueTest(tupProj);
        String expectedText = "==> state.tupVar.field1";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testProjectedVarProjectionAddressableConversion() {
        DiscVariableExpression tupVarExpr = newDiscVariableExpression(null, makeTupleType(3), tupVar);
        Expression tupProj = newProjectionExpression(tupVarExpr, newIntExpression(null, newIntType(), 0), null,
                newRealType());
        String realText = runProjectedAddressableTest(tupProj);
        String expectedText = "==> newState.tupVar.field1";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testSliceExpressionConversion() {
        assertThrows(RuntimeException.class, () -> runValueTest(newSliceExpression()));
    }

    @Test
    public void testFunctionCallExpressionConversion() {
        // abs(21)
        Expression func = newStdLibFunctionExpression(StdLibFunction.ABS, null, null);
        List<Expression> args = List.of(newIntExpression(null, newIntType(), 21));
        Expression call = newFunctionCallExpression(args, func, null, null);
        String realText = runValueTest(call);
        String expectedText = "==> ABS(21)";
        assertEquals(expectedText, realText);

        // cbrt(17.28)
        func = newStdLibFunctionExpression(StdLibFunction.CBRT, null, null);
        args = List.of(newRealExpression(null, newRealType(), "17.28"));
        call = newFunctionCallExpression(args, func, null, null);
        realText = runValueTest(call);
        expectedText = "==> 17.28 ** (1.0 / 3.0)";
        assertEquals(expectedText, realText);

        // log(17.28) with target support.
        target.supportsLog = true;
        func = newStdLibFunctionExpression(StdLibFunction.LOG, null, null);
        args = List.of(newRealExpression(null, newRealType(), "17.28"));
        call = newFunctionCallExpression(args, func, null, null);
        realText = runValueTest(call);
        expectedText = "==> LOG(17.28)";
        assertEquals(expectedText, realText);

        // log(17.28) without target support.
        target.supportsLog = false;
        func = newStdLibFunctionExpression(StdLibFunction.LOG, null, null);
        args = List.of(newRealExpression(null, newRealType(), "17.28"));
        call = newFunctionCallExpression(args, func, null, null);
        realText = runValueTest(call);
        expectedText = "==> LN(17.28) / LN(10.0)";
        assertEquals(expectedText, realText);

        // power(1, 2), both ranged to allow an int result.
        List<CifType> paramTypes = List.of(newIntType(0, null, 2), newIntType(0, null, 2));
        func = newStdLibFunctionExpression(StdLibFunction.POWER, null, newFuncType(paramTypes, null, newIntType()));
        args = List.of(newIntExpression(null, newIntType(0, null, 2), 1),
                newIntExpression(null, newIntType(0, null, 2), 2));
        call = newFunctionCallExpression(args, func, null, null);
        realText = runValueTest(call);
        expectedText = "==> LREAL_TO_DINT(DINT_TO_LREAL(1) ** 2)";
        assertEquals(expectedText, realText);

        // power(2, 3), both rangeless
        paramTypes = List.of(newIntType(), newIntType());
        func = newStdLibFunctionExpression(StdLibFunction.POWER, null, newFuncType(paramTypes, null, newRealType()));
        args = List.of(newIntExpression(null, newIntType(), 2), newIntExpression(null, newIntType(), 3));
        call = newFunctionCallExpression(args, func, null, null);
        realText = runValueTest(call);
        expectedText = "==> DINT_TO_LREAL(2) ** 3";
        assertEquals(expectedText, realText);

        // power(3, 2.0)
        paramTypes = List.of(newIntType(0, null, 2), newRealType());
        func = newStdLibFunctionExpression(StdLibFunction.POWER, null, newRealType());
        args = List.of(newIntExpression(null, newIntType(), 3), newRealExpression(null, newRealType(), "2.0"));
        call = newFunctionCallExpression(args, func, null, null);
        realText = runValueTest(call);
        expectedText = "==> DINT_TO_LREAL(3) ** 2.0";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testListExpressionConversion() {
        // [false, true]
        ListExpression array = newListExpression(
                List.of(newBoolExpression(null, newBoolType(), false), newBoolExpression(null, newBoolType(), true)),
                null, newListType(newBoolType(), 2, null, 2));
        String realText = runValueTest(array);
        String expectedText = """
                Code:
                litArray101[0] := FALSE;
                litArray101[1] := TRUE;

                Value variables:
                 - BOOL[0..1] litArray101;

                ==> litArray101""";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testTupleExpressionConversion() {
        // (flatDisc, 1.2345)
        List<Expression> fields = List.of(makeDiscVarExpr(), newRealExpression(null, newRealType(), "1.2345"));
        TupleType tt = makeTupleType(2);
        TupleExpression struct = newTupleExpression(fields, null, tt);
        String realText = runValueTest(struct);
        String expectedText = """
                Code:
                litStruct101.field1 := state.flatDisc;
                litStruct101.field2 := 1.2345;

                Value variables:
                 - tupType_2 litStruct101;

                ==> litStruct101""";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testDictExpressionConversion() {
        assertThrows(RuntimeException.class, () -> runValueTest(newDictExpression()));
    }

    @Test
    public void testContConstantExpressionConversion() {
        // fixed
        String realText = runValueTest(newConstantExpression(constantVar, null, newBoolType()));
        String expectedText = "==> FALSE";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testDiscVariableValueConversion() {
        // flatDisc (which is stored in state.flatDisc by the {@code CifDataProvider}).
        String realText = runValueTest(newDiscVariableExpression(null, newRealType(), discVar));
        String expectedText = "==> state.flatDisc";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testDiscVariableAddressableConversion() {
        // flatDisc (which is stored in state.flatDisc by the {@code CifDataProvider}).
        String realText = runVariableAddressableTest(newDiscVariableExpression(null, newRealType(), discVar));
        String expectedText = "==> newState.flatDisc";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testContVariableValueConversion() {
        // timer'
        String realText = runValueTest(newContVariableExpression(true, null, null, contVar));
        String expectedText = "==> timer_der";
        assertEquals(expectedText, realText);

        // timer
        realText = runValueTest(newContVariableExpression(false, null, null, contVar));
        expectedText = "==> timer";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testContVariableAddressableConversion() {
        // timer'
        String realText = runVariableAddressableTest(newContVariableExpression(true, null, null, contVar));
        String expectedText = "==> new_timer_der";
        assertEquals(expectedText, realText);

        // timer
        realText = runVariableAddressableTest(newContVariableExpression(false, null, null, contVar));
        expectedText = "==> new_timer";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testLocationExpressionConversion() {
        assertThrows(RuntimeException.class, () -> runValueTest(newLocationExpression()));
    }

    @Test
    public void testEnumLiteralExpressionConversion() {
        EnumLiteral eLit = newEnumLiteral("value123", null);
        String realText = runValueTest(newEnumLiteralExpression(eLit, null, null));
        String expectedText = "==> value123";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testFunctionExpressionConversion() {
        assertThrows(RuntimeException.class, () -> runValueTest(newFunctionExpression()));
    }

    @Test
    public void testInputVariableExpressionConversion() {
        String realText = runValueTest(newInputVariableExpression(null, newIntType(), inputVar));
        String expectedText = "==> theInput";
        assertEquals(expectedText, realText);
    }

    @Test
    public void testInputVariableAddressableConversion() {
        // While the CIF data provider can provide a destination to write into, the CIF code should never contain such
        // expressions.
        assertThrows(RuntimeException.class,
                () -> runVariableAddressableTest(newInputVariableExpression(null, newIntType(), inputVar)));
    }
}
