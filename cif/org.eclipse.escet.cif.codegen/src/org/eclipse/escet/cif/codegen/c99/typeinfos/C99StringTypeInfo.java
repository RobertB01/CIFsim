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

package org.eclipse.escet.cif.codegen.c99.typeinfos;

import static org.eclipse.escet.cif.codegen.c99.C99CodeGen.ENUM_NAMES_LIST;
import static org.eclipse.escet.cif.codegen.c99.C99DataValue.constructReference;
import static org.eclipse.escet.cif.codegen.c99.C99DataValue.makeComputed;
import static org.eclipse.escet.cif.codegen.c99.C99DataValue.makeValue;
import static org.eclipse.escet.cif.codegen.c99.typeinfos.C99TypeInfoHelper.typeGetTypePrintName;
import static org.eclipse.escet.cif.codegen.c99.typeinfos.C99TypeInfoHelper.typeUsesValues;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.escape;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.typeinfos.StringTypeInfo;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.FormatDecoder;
import org.eclipse.escet.common.java.FormatDescription;
import org.eclipse.escet.common.java.FormatDescription.Conversion;

/** Type information class about strings in the C99 target language. */
public class C99StringTypeInfo extends StringTypeInfo implements C99TypeInfo {
    /**
     * If set, generate functions only available in the current source file, else generate globally accessible
     * functions.
     */
    public final boolean genLocalFunctions;

    /**
     * Constructor for {@link C99StringTypeInfo} class.
     *
     * @param genLocalFunctions If set, generate functions only available in the current source file, else generate
     *     globally accessible functions.
     * @param cifType The CIF type used for creating this type information object.
     */
    public C99StringTypeInfo(boolean genLocalFunctions, CifType cifType) {
        super(cifType);
        this.genLocalFunctions = genLocalFunctions;
    }

    @Override
    public boolean supportRawMemCmp() {
        return false; // Type has unused trailing data which may be anything.
    }

    @Override
    public boolean useValues() {
        return false;
    }

    @Override
    public String getTypePrintName(boolean rawString) {
        return rawString ? "StringTypePrintRaw" : "StringTypePrintEscaped";
    }

    @Override
    public ExprCode convertLiteral(String value, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();

        // Construct destination for the copy if needed.
        String destRef;
        if (dest == null) {
            VariableInformation tempVar = ctxt.makeTempVariable(this, "str_tmp");
            destRef = "&" + tempVar.targetName;

            result.add(fmt("%s %s;", getTargetType(), tempVar.targetName));
            result.setDataValue(makeValue(tempVar.targetName));
        } else {
            destRef = dest.getReference();
        }

        // Perform the copy.
        result.add(fmt("StringTypeCopyText(%s, %s);", destRef, value));
        return result;
    }

    @Override
    public ExprCode convertConcatenation(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        ExprCode leftCode = ctxt.exprToTarget(expr.getLeft(), null);
        ExprCode rightCode = ctxt.exprToTarget(expr.getRight(), null);

        ExprCode result = new ExprCode();
        result.add(leftCode);
        result.add(rightCode);

        // Construct destination for the concatenation if needed.
        String destRef;
        if (dest == null) {
            VariableInformation tempVar = ctxt.makeTempVariable(this, "str_tmp");
            destRef = "&" + tempVar.targetName;

            result.add(fmt("%s %s;", getTargetType(), tempVar.targetName));
            result.setDataValue(makeValue(tempVar.targetName));
        } else {
            destRef = dest.getReference();
        }

        String leftRef = constructReference(leftCode.getRawDataValue(), this, ctxt, result);
        String rightRef = constructReference(rightCode.getRawDataValue(), this, ctxt, result);

        // Perform the copy.
        result.add(fmt("StringTypeConcat(%s, %s, %s);", destRef, leftRef, rightRef));
        return result;
    }

    @Override
    public ExprCode convertSizeStdLib(Expression expression, Destination dest, CodeContext ctxt) {
        ExprCode argCode = ctxt.exprToTarget(expression, null);

        ExprCode result = new ExprCode();
        result.add(argCode.getCode());

        String argRef = constructReference(argCode.getRawDataValue(), this, ctxt, result);

        result.setDestination(dest);
        String resultText = fmt("StringTypeSize(%s)", argRef);
        result.setDataValue(makeComputed(resultText));
        return result;
    }

    @Override
    public ExprCode convertFormatFunction(String pattern, List<Expression> params, List<CifType> paramTypes,
            Destination dest, CodeContext ctxt)
    {
        ExprCode result = new ExprCode();

        CodeBox code = ctxt.makeCodeBox();
        code.add("{");
        code.indent();

        // Convert arguments, avoiding double computation and unwanted side-effects of double evaluation.
        FormatDecoder decoder = new FormatDecoder();
        List<FormatDescription> parts = decoder.decode(pattern);
        String[] argText = convertFmtArguments(parts, params, paramTypes, code, ctxt);

        // Construct destination for the output if needed.
        String destValue; // Final destination string.
        String destScratch; // Reference to intermediate destination string (may point to destValue).
        boolean needsDestScratch;
        if (dest == null) {
            // Temporary variable required, is definitely not a parameter we could overwrite.
            VariableInformation tempVar = ctxt.makeTempVariable(this, "str_tmp");
            destValue = tempVar.targetName;
            destScratch = "&" + destValue; // Use the temp var directly.
            needsDestScratch = false;

            result.add(fmt("%s %s;", getTargetType(), tempVar.targetName));
            result.setDataValue(makeValue(tempVar.targetName));
        } else {
            // Use a temporary destination string, as the final destination of the format call
            // may also be used as argument.
            destValue = dest.getData();

            // If no string used as parameter, we certainly cannot overwrite a parameter while filling the destination.
            needsDestScratch = false;
            destScratch = "&" + destValue; // Use the destination directly.
            for (CifType type: paramTypes) {
                if (type instanceof StringType) {
                    // String parameter found, fall-back to temporary destScratch space.
                    destScratch = "&dest_scratch";
                    needsDestScratch = true;
                    break;
                }
            }
        }

        // Construct scratch space for creating parameter values if required.
        boolean needsScratch = false;
        for (CifType paramType: paramTypes) {
            needsScratch = argumentNeedsScratchSpace(paramType);
            if (needsScratch) {
                break;
            }
        }

        // Generate 'print' code.
        String strSize = "(MAX_STRING_SIZE + 1) > 128 ? (MAX_STRING_SIZE + 1) : 128";
        if (needsScratch) {
            code.add("char scratch[%s]; /* Value scratch space. */", strSize);
        }
        if (needsDestScratch) {
            code.add("StringType dest_scratch; /* Resulting string scratch space. */");
        }
        code.add("int index = 0;");

        int implicitIndex = 0; // First unused argument.
        for (FormatDescription part: parts) {
            int idx = 0;

            if (part.conversion != Conversion.LITERAL) {
                if (!part.index.isEmpty()) {
                    idx = part.getExplicitIndex() - 1;
                } else {
                    idx = implicitIndex;
                    implicitIndex++;
                }
            }

            String flags;
            if (part.flags == null) {
                flags = "FMTFLAGS_NONE";
            } else {
                List<String> flagsParts = list();
                if (part.flags.contains("-")) {
                    flagsParts.add("FMTFLAGS_LEFT");
                }
                if (part.flags.contains("+")) {
                    flagsParts.add("FMTFLAGS_SIGN");
                }
                if (part.flags.contains(" ")) {
                    flagsParts.add("FMTFLAGS_SPACE");
                }
                if (part.flags.contains("0")) {
                    flagsParts.add("FMTFLAGS_ZEROES");
                }
                if (part.flags.contains(",")) {
                    flagsParts.add("FMTFLAGS_GROUPS");
                }
                flags = flagsParts.isEmpty() ? "FMTFLAGS_NONE" : String.join("|", flagsParts);
            }

            String width = part.width;
            if (width == null || width.isEmpty()) {
                width = "0";
            }

            String text = convertArgument(part, paramTypes.get(idx), argText[idx], code, ctxt);
            code.add("index = StringTypeAppendText(%s, index, %s, %s, %s);", destScratch, flags, width, text);
        }

        // Finish the string and the code.
        if (needsDestScratch) {
            code.add("memcpy(%s.data, dest_scratch.data, MAX_STRING_SIZE);", destValue);
        }
        code.dedent();
        code.add("}");

        result.add(code);
        return result;
    }

    /**
     * Convert arguments, avoiding double computation and unwanted side-effects due to double evaluation.
     *
     * @param parts Decoded parts of the format string.
     * @param params Parameters of the format call.
     * @param paramTypes Type of the parameters of the format call.
     * @param code Destination of generated code.
     * @param ctxt Code generation context.
     * @return Text for accessing each argument, parameters that are used more than once are stored in a temporary
     *     variable.
     */
    private String[] convertFmtArguments(List<FormatDescription> parts, List<Expression> params,
            List<CifType> paramTypes, CodeBox code, CodeContext ctxt)
    {
        int[] usageCount = countFmtParamUsages(params.size(), parts);

        String[] argText = new String[params.size()];
        for (int i = 0; i < argText.length; i++) {
            CifType paramType = paramTypes.get(i);
            TypeInfo paramTi = ctxt.typeToTarget(paramType);

            if (usageCount[i] == 0) {
                argText[i] = null;
            } else if (usageCount[i] == 1) {
                ExprCode argCode = ctxt.exprToTarget(params.get(i), null);
                code.add(argCode.getCode());

                if (typeUsesValues(paramTi)) {
                    argText[i] = argCode.getData();
                } else {
                    DataValue dataValue = argCode.getRawDataValue();
                    if (dataValue.canBeReferenced()) {
                        argText[i] = dataValue.getReference();
                    } else {
                        VariableInformation tempVar = ctxt.makeTempVariable(paramTi, "fmt_temp");
                        code.add("%s %s = %s;", paramTi.getTargetType(), tempVar.targetName, dataValue.getData());
                        argText[i] = "&" + tempVar.targetName;
                    }
                }
            } else {
                VariableInformation tempVar = ctxt.makeTempVariable(paramTi, "fmt_temp");
                ExprCode argCode = ctxt.exprToTarget(params.get(i), null);
                code.add(argCode.getCode());

                if (typeUsesValues(paramTi)) {
                    code.add("%s %s = %s;", paramTi.getTargetType(), tempVar.targetName, argCode.getData());
                    argText[i] = tempVar.targetName;
                } else {
                    DataValue dataValue = argCode.getRawDataValue();
                    if (dataValue.canBeReferenced()) {
                        code.add("%s *%s = %s;", paramTi.getTargetType(), tempVar.targetName, dataValue.getReference());
                        argText[i] = tempVar.targetName;
                    } else {
                        code.add("%s %s = %s;", paramTi.getTargetType(), tempVar.targetName, dataValue.getData());
                        argText[i] = "&" + tempVar.targetName;
                    }
                }
            }
        }
        return argText;
    }

    /**
     * Count number of uses of each value parameter.
     *
     * @param numParams Number of value parameters.
     * @param parts Decoded format string parts.
     * @return Array with usage count for each part.
     */
    private int[] countFmtParamUsages(int numParams, List<FormatDescription> parts) {
        int[] usageCount = new int[numParams];
        for (int i = 0; i < usageCount.length; i++) {
            usageCount[i] = 0;
        }

        int implicitIndex = 0; // First unused argument.
        for (FormatDescription part: parts) {
            if (part.conversion == Conversion.LITERAL) {
                continue;
            }

            // Get 0-based index of specifier.
            int idx;
            if (!part.index.isEmpty()) {
                idx = part.getExplicitIndex() - 1;
            } else {
                idx = implicitIndex;
                implicitIndex++;
            }

            Assert.check(idx >= 0 && idx < usageCount.length);
            usageCount[idx]++;
        }
        return usageCount;
    }

    /**
     * Convert a part of a format string to a code fragment, and a result expression value in the target language.
     *
     * @param part Part to convert.
     * @param paramType Type of the parameter used by the part.
     * @param argText Text in the target language to access the parameter value.
     * @param code Generated code so far (extended in-place).
     * @param ctxt Code generation context.
     * @return Result expression value in the target language.
     */
    private String convertArgument(FormatDescription part, CifType paramType, String argText, CodeBox code,
            CodeContext ctxt)
    {
        if (part.conversion.equals(Conversion.LITERAL)) {
            return "\"" + escape(part.text) + "\"";
        } else if (paramType instanceof BoolType) {
            if (part.text.contains("B")) {
                return fmt("(%s) ? \"TRUE\" : \"FALSE\"", argText);
            } else {
                return fmt("(%s) ? \"true\" : \"false\"", argText);
            }
        } else if (paramType instanceof IntType) {
            String fmtPat = part.text.contains("X") ? "X" : (part.text.contains("x") ? "x" : "d");

            code.add("snprintf(scratch, sizeof(scratch), \"%%%s\", %s);", fmtPat, argText);
            return "scratch";
        } else if (paramType instanceof RealType) {
            String fmtPat;
            // Deal with specified precision.
            if (part.precision == null || part.precision.isEmpty()) {
                fmtPat = "%";
            } else {
                int precision = Integer.parseInt(part.precision);
                if (precision < 0) {
                    precision = 0;
                }
                if (precision > 30) {
                    precision = 30;
                }
                fmtPat = fmt("%%.%d", precision);
            }

            // Add conversion type.
            if (part.text.contains("e")) {
                fmtPat += "e";
            } else if (part.text.contains("E")) {
                fmtPat += "E";
            } else if (part.text.contains("g")) {
                fmtPat += "g";
            } else if (part.text.contains("G")) {
                fmtPat += "G";
            } else {
                fmtPat += "f";
            }

            code.add("snprintf(scratch, sizeof(scratch), \"%s\", %s);", fmtPat, argText);
            return "scratch";
        } else if (paramType instanceof StringType) {
            return fmt("(%s)->data", argText);
        } else if (paramType instanceof EnumType) {
            return fmt("%s[%s]", ENUM_NAMES_LIST, argText);
        } else {
            Assert.check(paramType instanceof ListType || paramType instanceof TupleType);
            TypeInfo paramTi = ctxt.typeToTarget(paramType);
            code.add("%s(%s, scratch, 0, MAX_STRING_SIZE);", typeGetTypePrintName(paramTi, false), argText);
            return "scratch";
        }
    }

    /**
     * Does the provided parameter type require temporary "scratch" space?
     *
     * @param paramType To to inspect.
     * @return Whether the parameter type needs the temporary "scratch" space.
     */
    private boolean argumentNeedsScratchSpace(CifType paramType) {
        return (paramType instanceof IntType) || (paramType instanceof RealType) || (paramType instanceof ListType)
                || (paramType instanceof TupleType);
    }

    @Override
    public ExprCode getProjectedValue(ExprCode stringCode, ExprCode indexCode, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.add(stringCode);
        result.add(indexCode);

        // Construct destination for the concatenation if needed.
        String destRef;
        if (dest == null) {
            VariableInformation tempVar = ctxt.makeTempVariable(this, "str_tmp");
            destRef = "&" + tempVar.targetName;

            result.add(fmt("%s %s;", getTargetType(), tempVar.targetName));
            result.setDataValue(makeValue(tempVar.targetName));
        } else {
            destRef = dest.getReference();
        }

        // Perform the projection.
        String stringRef = constructReference(stringCode.getRawDataValue(), this, ctxt, result);
        result.add(fmt("StringTypeProject(%s, %s, %s);", destRef, stringRef, indexCode.getData()));
        return result;
    }

    @Override
    public String getTargetType() {
        return "StringType";
    }

    @Override
    public void generateCode(CodeContext ctxt) {
        // Nothing to generate.
    }

    @Override
    public void storeValue(CodeBox code, DataValue sourceValue, Destination dest) {
        code.add(dest.getCode());
        code.add("%s = %s;", dest.getData(), sourceValue.getData());
    }

    @Override
    public void declareInit(CodeBox code, DataValue sourceValue, Destination dest) {
        code.add(dest.getCode());
        code.add("%s %s = %s;", getTargetType(), dest.getData(), sourceValue.getData());
    }

    @Override
    public String getBinaryExpressionTemplate(BinaryOperator binOp) {
        if (binOp.equals(BinaryOperator.EQUAL)) {
            return "StringTypeEquals(${left-ref}, ${right-ref})";
        } else if (binOp.equals(BinaryOperator.UNEQUAL)) {
            return "!StringTypeEquals(${left-ref}, ${right-ref})";
        }

        throw new RuntimeException("Unexpected binary operator: " + str(binOp));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof C99StringTypeInfo;
    }

    @Override
    public int hashCode() {
        return C99StringTypeInfo.class.hashCode();
    }
}
