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

package org.eclipse.escet.cif.common.checkers;

import static org.eclipse.escet.cif.common.CifTextUtils.exprToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.getNamedAncestorOrSelf;
import static org.eclipse.escet.cif.common.CifTextUtils.operatorToStr;
import static org.eclipse.escet.cif.common.CifTextUtils.typeToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.DictType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.cif.metamodel.cif.types.SetType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;

/** CIF check that does not allow certain binary expressions. */
public class NoSpecificBinaryExprsCheck extends CifCheck {
    /** Whether to disallow {@link BinaryOperator#ADDITION}. */
    public boolean disallowAddition;

    /** Whether to disallow {@link BinaryOperator#ADDITION} on integers. */
    public boolean disallowAdditionInts;

    /** Whether to disallow {@link BinaryOperator#ADDITION} on ranged integers. */
    public boolean disallowAdditionIntsRanged;

    /** Whether to disallow {@link BinaryOperator#ADDITION} on rangeless integers. */
    public boolean disallowAdditionIntsRangeless;

    /** Whether to disallow {@link BinaryOperator#ADDITION} on reals. */
    public boolean disallowAdditionReals;

    /** Whether to disallow {@link BinaryOperator#ADDITION} on lists. */
    public boolean disallowAdditionLists;

    /** Whether to disallow {@link BinaryOperator#ADDITION} on strings. */
    public boolean disallowAdditionStrings;

    /** Whether to disallow {@link BinaryOperator#ADDITION} on dictionaries. */
    public boolean disallowAdditionDicts;

    /** Whether to disallow {@link BinaryOperator#BI_CONDITIONAL}. */
    public boolean disallowBiconditional;

    /** Whether to disallow {@link BinaryOperator#CONJUNCTION}. */
    public boolean disallowConjunction;

    /** Whether to disallow {@link BinaryOperator#CONJUNCTION} on sets. */
    public boolean disallowConjunctionSets;

    /** Whether to disallow {@link BinaryOperator#DISJUNCTION}. */
    public boolean disallowDisjunction;

    /** Whether to disallow {@link BinaryOperator#DISJUNCTION} on sets. */
    public boolean disallowDisjunctionSets;

    /** Whether to disallow {@link BinaryOperator#DIVISION}. */
    public boolean disallowDivision;

    /** Whether to disallow {@link BinaryOperator#ELEMENT_OF}. */
    public boolean disallowElementOf;

    /** Whether to disallow {@link BinaryOperator#ELEMENT_OF} on lists. */
    public boolean disallowElementOfLists;

    /** Whether to disallow {@link BinaryOperator#ELEMENT_OF} on sets. */
    public boolean disallowElementOfSets;

    /** Whether to disallow {@link BinaryOperator#ELEMENT_OF} on dictionaries. */
    public boolean disallowElementOfDicts;

    /** Whether to disallow {@link BinaryOperator#EQUAL}. */
    public boolean disallowEqual;

    /** Whether to disallow {@link BinaryOperator#GREATER_EQUAL}. */
    public boolean disallowGreaterEqual;

    /** Whether to disallow {@link BinaryOperator#GREATER_EQUAL} on integers. */
    public boolean disallowGreaterEqualInts;

    /** Whether to disallow {@link BinaryOperator#GREATER_EQUAL} on ranged integers. */
    public boolean disallowGreaterEqualIntsRanged;

    /** Whether to disallow {@link BinaryOperator#GREATER_EQUAL} on rangeless integers. */
    public boolean disallowGreaterEqualIntsRangeless;

    /** Whether to disallow {@link BinaryOperator#GREATER_EQUAL} on reals. */
    public boolean disallowGreaterEqualReals;

    /** Whether to disallow {@link BinaryOperator#GREATER_THAN}. */
    public boolean disallowGreaterThan;

    /** Whether to disallow {@link BinaryOperator#GREATER_THAN} on integers. */
    public boolean disallowGreaterThanInts;

    /** Whether to disallow {@link BinaryOperator#GREATER_THAN} on ranged integers. */
    public boolean disallowGreaterThanIntsRanged;

    /** Whether to disallow {@link BinaryOperator#GREATER_THAN} on rangeless integers. */
    public boolean disallowGreaterThanIntsRangeless;

    /** Whether to disallow {@link BinaryOperator#GREATER_THAN} on reals. */
    public boolean disallowGreaterThanReals;

    /** Whether to disallow {@link BinaryOperator#IMPLICATION}. */
    public boolean disallowImplication;

    /** Whether to disallow {@link BinaryOperator#INTEGER_DIVISION}. */
    public boolean disallowIntegerDivision;

    /** Whether to disallow {@link BinaryOperator#INTEGER_DIVISION} on integers. */
    public boolean disallowIntegerDivisionInts;

    /** Whether to disallow {@link BinaryOperator#INTEGER_DIVISION} on ranged integers. */
    public boolean disallowIntegerDivisionIntsRanged;

    /** Whether to disallow {@link BinaryOperator#INTEGER_DIVISION} on rangeless integers. */
    public boolean disallowIntegerDivisionIntsRangeless;

    /** Whether to disallow {@link BinaryOperator#LESS_EQUAL}. */
    public boolean disallowLessEqual;

    /** Whether to disallow {@link BinaryOperator#LESS_EQUAL} on integers. */
    public boolean disallowLessEqualInts;

    /** Whether to disallow {@link BinaryOperator#LESS_EQUAL} on ranged integers. */
    public boolean disallowLessEqualIntsRanged;

    /** Whether to disallow {@link BinaryOperator#LESS_EQUAL} on rangeless integers. */
    public boolean disallowLessEqualIntsRangeless;

    /** Whether to disallow {@link BinaryOperator#LESS_EQUAL} on reals. */
    public boolean disallowLessEqualReals;

    /** Whether to disallow {@link BinaryOperator#LESS_THAN}. */
    public boolean disallowLessThan;

    /** Whether to disallow {@link BinaryOperator#LESS_THAN} on integers. */
    public boolean disallowLessThanInts;

    /** Whether to disallow {@link BinaryOperator#LESS_THAN} on ranged integers. */
    public boolean disallowLessThanIntsRanged;

    /** Whether to disallow {@link BinaryOperator#LESS_THAN} on rangeless integers. */
    public boolean disallowLessThanIntsRangeless;

    /** Whether to disallow {@link BinaryOperator#LESS_THAN} on reals. */
    public boolean disallowLessThanReals;

    /** Whether to disallow {@link BinaryOperator#MODULUS}. */
    public boolean disallowModulus;

    /** Whether to disallow {@link BinaryOperator#MODULUS} on integers. */
    public boolean disallowModulusInts;

    /** Whether to disallow {@link BinaryOperator#MODULUS} on ranged integers. */
    public boolean disallowModulusIntsRanged;

    /** Whether to disallow {@link BinaryOperator#MODULUS} on rangeless integers. */
    public boolean disallowModulusIntsRangeless;

    /** Whether to disallow {@link BinaryOperator#MULTIPLICATION}. */
    public boolean disallowMultiplication;

    /** Whether to disallow {@link BinaryOperator#MULTIPLICATION} on integers. */
    public boolean disallowMultiplicationInts;

    /** Whether to disallow {@link BinaryOperator#MULTIPLICATION} on ranged integers. */
    public boolean disallowMultiplicationIntsRanged;

    /** Whether to disallow {@link BinaryOperator#MULTIPLICATION} on rangeless integers. */
    public boolean disallowMultiplicationIntsRangeless;

    /** Whether to disallow {@link BinaryOperator#MULTIPLICATION} on reals. */
    public boolean disallowMultiplicationReals;

    /** Whether to disallow {@link BinaryOperator#SUBSET}. */
    public boolean disallowSubset;

    /** Whether to disallow {@link BinaryOperator#SUBTRACTION}. */
    public boolean disallowSubtraction;

    /** Whether to disallow {@link BinaryOperator#SUBTRACTION} on integers. */
    public boolean disallowSubtractionInts;

    /** Whether to disallow {@link BinaryOperator#SUBTRACTION} on ranged integers. */
    public boolean disallowSubtractionIntsRanged;

    /** Whether to disallow {@link BinaryOperator#SUBTRACTION} on rangeless integers. */
    public boolean disallowSubtractionIntsRangeless;

    /** Whether to disallow {@link BinaryOperator#SUBTRACTION} on reals. */
    public boolean disallowSubtractionReals;

    /** Whether to disallow {@link BinaryOperator#SUBTRACTION} on lists. */
    public boolean disallowSubtractionLists;

    /** Whether to disallow {@link BinaryOperator#SUBTRACTION} on sets. */
    public boolean disallowSubtractionSets;

    /** Whether to disallow {@link BinaryOperator#SUBTRACTION} on dictionaries. */
    public boolean disallowSubtractionDicts;

    /** Whether to disallow {@link BinaryOperator#UNEQUAL}. */
    public boolean disallowUnequal;

    @Override
    protected void preprocessBinaryExpression(BinaryExpression binExpr) {
        BinaryOperator op = binExpr.getOperator();
        CifType ltype = CifTypeUtils.normalizeType(binExpr.getLeft().getType());
        CifType rtype = CifTypeUtils.normalizeType(binExpr.getRight().getType());
        switch (op) {
            case ADDITION:
                if (disallowAddition) {
                    addExprViolationOperator(binExpr);
                } else {
                    if (disallowAdditionInts) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr);
                        }
                    } else {
                        if (disallowAdditionIntsRanged) {
                            if ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                        if (disallowAdditionIntsRangeless) {
                            if ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                    }
                    if (disallowAdditionReals) {
                        if (ltype instanceof RealType || rtype instanceof RealType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                    if (disallowAdditionLists) {
                        if (ltype instanceof ListType || rtype instanceof ListType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                    if (disallowAdditionStrings) {
                        if (ltype instanceof StringType || rtype instanceof StringType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                    if (disallowAdditionDicts) {
                        if (ltype instanceof DictType || rtype instanceof DictType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                }
                break;
            case BI_CONDITIONAL:
                if (disallowBiconditional) {
                    addExprViolationOperator(binExpr);
                }
                break;
            case CONJUNCTION:
                if (disallowConjunction) {
                    addExprViolationOperator(binExpr);
                } else if (disallowConjunctionSets) {
                    if (ltype instanceof SetType || rtype instanceof SetType) {
                        addExprViolationOperand(binExpr);
                    }
                }
                break;
            case DISJUNCTION:
                if (disallowDisjunction) {
                    addExprViolationOperator(binExpr);
                } else if (disallowDisjunctionSets) {
                    if (ltype instanceof SetType || rtype instanceof SetType) {
                        addExprViolationOperand(binExpr);
                    }
                }
                break;
            case DIVISION:
                if (disallowDivision) {
                    addExprViolationOperator(binExpr);
                }
                break;
            case ELEMENT_OF:
                if (disallowElementOf) {
                    addExprViolationOperator(binExpr);
                } else {
                    if (disallowElementOfLists) {
                        if (ltype instanceof ListType || rtype instanceof ListType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                    if (disallowElementOfSets) {
                        if (ltype instanceof SetType || rtype instanceof SetType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                    if (disallowElementOfDicts) {
                        if (ltype instanceof DictType || rtype instanceof DictType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                }
                break;
            case EQUAL:
                if (disallowEqual) {
                    addExprViolationOperator(binExpr);
                }
                break;
            case GREATER_EQUAL:
                if (disallowGreaterEqual) {
                    addExprViolationOperator(binExpr);
                } else {
                    if (disallowGreaterEqualInts) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr);
                        }
                    } else {
                        if (disallowGreaterEqualIntsRanged) {
                            if ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                        if (disallowGreaterEqualIntsRangeless) {
                            if ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                    }
                    if (disallowGreaterEqualReals) {
                        if (ltype instanceof RealType || rtype instanceof RealType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                }
                break;
            case GREATER_THAN:
                if (disallowGreaterThan) {
                    addExprViolationOperator(binExpr);
                } else {
                    if (disallowGreaterThanInts) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr);
                        }
                    } else {
                        if (disallowGreaterThanIntsRanged) {
                            if ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                        if (disallowGreaterThanIntsRangeless) {
                            if ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                    }
                    if (disallowGreaterThanReals) {
                        if (ltype instanceof RealType || rtype instanceof RealType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                }
                break;
            case IMPLICATION:
                if (disallowImplication) {
                    addExprViolationOperator(binExpr);
                }
                break;
            case INTEGER_DIVISION:
                if (disallowIntegerDivision) {
                    addExprViolationOperator(binExpr);
                } else {
                    if (disallowIntegerDivisionInts) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr);
                        }
                    } else {
                        if (disallowIntegerDivisionIntsRanged) {
                            if ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                        if (disallowIntegerDivisionIntsRangeless) {
                            if ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                    }
                }
                break;
            case LESS_EQUAL:
                if (disallowLessEqual) {
                    addExprViolationOperator(binExpr);
                } else {
                    if (disallowLessEqualInts) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr);
                        }
                    } else {
                        if (disallowLessEqualIntsRanged) {
                            if ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                        if (disallowLessEqualIntsRangeless) {
                            if ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                    }
                    if (disallowLessEqualReals) {
                        if (ltype instanceof RealType || rtype instanceof RealType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                }
                break;
            case LESS_THAN:
                if (disallowLessThan) {
                    addExprViolationOperator(binExpr);
                } else {
                    if (disallowLessThanInts) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr);
                        }
                    } else {
                        if (disallowLessThanIntsRanged) {
                            if ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                        if (disallowLessThanIntsRangeless) {
                            if ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                    }
                    if (disallowLessThanReals) {
                        if (ltype instanceof RealType || rtype instanceof RealType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                }
                break;
            case MODULUS:
                if (disallowModulus) {
                    addExprViolationOperator(binExpr);
                } else {
                    if (disallowModulusInts) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr);
                        }
                    } else {
                        if (disallowModulusIntsRanged) {
                            if ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                        if (disallowModulusIntsRangeless) {
                            if ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                    }
                }
                break;
            case MULTIPLICATION:
                if (disallowMultiplication) {
                    addExprViolationOperator(binExpr);
                } else {
                    if (disallowMultiplicationInts) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr);
                        }
                    } else {
                        if (disallowMultiplicationIntsRanged) {
                            if ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                        if (disallowMultiplicationIntsRangeless) {
                            if ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                    }
                    if (disallowMultiplicationReals) {
                        if (ltype instanceof RealType || rtype instanceof RealType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                }
                break;
            case SUBSET:
                if (disallowSubset) {
                    addExprViolationOperator(binExpr);
                }
                break;
            case SUBTRACTION:
                if (disallowSubtraction) {
                    addExprViolationOperator(binExpr);
                } else {
                    if (disallowSubtractionInts) {
                        if (ltype instanceof IntType || rtype instanceof IntType) {
                            addExprViolationOperand(binExpr);
                        }
                    } else {
                        if (disallowSubtractionIntsRanged) {
                            if ((ltype instanceof IntType && !CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && !CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                        if (disallowSubtractionIntsRangeless) {
                            if ((ltype instanceof IntType && CifTypeUtils.isRangeless((IntType)ltype))
                                    || (rtype instanceof IntType && CifTypeUtils.isRangeless((IntType)rtype)))
                            {
                                addExprViolationOperand(binExpr);
                            }
                        }
                    }
                    if (disallowSubtractionReals) {
                        if (ltype instanceof RealType || rtype instanceof RealType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                    if (disallowSubtractionLists) {
                        if (ltype instanceof ListType || rtype instanceof ListType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                    if (disallowSubtractionSets) {
                        if (ltype instanceof SetType || rtype instanceof SetType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                    if (disallowSubtractionDicts) {
                        if (ltype instanceof DictType || rtype instanceof DictType) {
                            addExprViolationOperand(binExpr);
                        }
                    }
                }
                break;
            case UNEQUAL:
                if (disallowUnequal) {
                    addExprViolationOperator(binExpr);
                }
                break;
            default:
                throw new RuntimeException("Unknown binary operator: " + op);
        }
    }

    /**
     * Add a violation for the operator of the given binary expression.
     *
     * @param binExpr The binary expression.
     */
    private void addExprViolationOperator(BinaryExpression binExpr) {
        super.addViolation(getNamedAncestorOrSelf(binExpr),
                fmt("uses binary operator \"%s\" in binary expression \"%s\"", operatorToStr(binExpr.getOperator()),
                        exprToStr(binExpr)));
    }

    /**
     * Add a violation for an operand of the the given binary expression.
     *
     * @param binExpr The binary expression.
     */
    private void addExprViolationOperand(BinaryExpression binExpr) {
        CifType ltype = CifTypeUtils.normalizeType(binExpr.getLeft().getType());
        CifType rtype = CifTypeUtils.normalizeType(binExpr.getRight().getType());
        super.addViolation(getNamedAncestorOrSelf(binExpr),
                fmt("uses binary operator \"%s\" on operands of types \"%s\" and \"%s\" in binary expression \"%s\"",
                        operatorToStr(binExpr.getOperator()), typeToStr(ltype), typeToStr(rtype), exprToStr(binExpr)));
    }
}
