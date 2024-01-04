//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.chi.typecheck.CheckType.dropReferences;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.metamodel.chi.BinaryExpression;
import org.eclipse.escet.chi.metamodel.chi.BinaryOperators;
import org.eclipse.escet.chi.metamodel.chi.BoolLiteral;
import org.eclipse.escet.chi.metamodel.chi.BoolType;
import org.eclipse.escet.chi.metamodel.chi.CallExpression;
import org.eclipse.escet.chi.metamodel.chi.CastExpression;
import org.eclipse.escet.chi.metamodel.chi.ChannelExpression;
import org.eclipse.escet.chi.metamodel.chi.ChannelType;
import org.eclipse.escet.chi.metamodel.chi.ConstantReference;
import org.eclipse.escet.chi.metamodel.chi.DictType;
import org.eclipse.escet.chi.metamodel.chi.DictionaryExpression;
import org.eclipse.escet.chi.metamodel.chi.DictionaryPair;
import org.eclipse.escet.chi.metamodel.chi.DistributionType;
import org.eclipse.escet.chi.metamodel.chi.EnumTypeReference;
import org.eclipse.escet.chi.metamodel.chi.EnumValueReference;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.FieldReference;
import org.eclipse.escet.chi.metamodel.chi.FileType;
import org.eclipse.escet.chi.metamodel.chi.FunctionReference;
import org.eclipse.escet.chi.metamodel.chi.FunctionType;
import org.eclipse.escet.chi.metamodel.chi.InstanceType;
import org.eclipse.escet.chi.metamodel.chi.IntNumber;
import org.eclipse.escet.chi.metamodel.chi.IntType;
import org.eclipse.escet.chi.metamodel.chi.ListExpression;
import org.eclipse.escet.chi.metamodel.chi.ListType;
import org.eclipse.escet.chi.metamodel.chi.MatrixExpression;
import org.eclipse.escet.chi.metamodel.chi.MatrixRow;
import org.eclipse.escet.chi.metamodel.chi.MatrixType;
import org.eclipse.escet.chi.metamodel.chi.ModelReference;
import org.eclipse.escet.chi.metamodel.chi.ProcessReference;
import org.eclipse.escet.chi.metamodel.chi.ReadCallExpression;
import org.eclipse.escet.chi.metamodel.chi.RealNumber;
import org.eclipse.escet.chi.metamodel.chi.RealType;
import org.eclipse.escet.chi.metamodel.chi.SetExpression;
import org.eclipse.escet.chi.metamodel.chi.SetType;
import org.eclipse.escet.chi.metamodel.chi.SliceExpression;
import org.eclipse.escet.chi.metamodel.chi.StdLibFunctionReference;
import org.eclipse.escet.chi.metamodel.chi.StringLiteral;
import org.eclipse.escet.chi.metamodel.chi.StringType;
import org.eclipse.escet.chi.metamodel.chi.TimeLiteral;
import org.eclipse.escet.chi.metamodel.chi.TimerType;
import org.eclipse.escet.chi.metamodel.chi.TupleExpression;
import org.eclipse.escet.chi.metamodel.chi.TupleField;
import org.eclipse.escet.chi.metamodel.chi.TupleType;
import org.eclipse.escet.chi.metamodel.chi.Type;
import org.eclipse.escet.chi.metamodel.chi.TypeReference;
import org.eclipse.escet.chi.metamodel.chi.UnaryExpression;
import org.eclipse.escet.chi.metamodel.chi.UnaryOperators;
import org.eclipse.escet.chi.metamodel.chi.VariableReference;
import org.eclipse.escet.chi.metamodel.chi.VoidType;
import org.eclipse.escet.common.java.Assert;

/** Helper class for deriving expression properties. */
public class BehaviorHelper {
    /** Constructor of the {@link BehaviorHelper} class. */
    private BehaviorHelper() {
        // Static class.
    }

    /**
     * Decide how the value of an expression can change.
     *
     * @param expr Expression to analyze.
     * @return Behavior of the value of the expression.
     */
    public static ExprBehavior getBehavior(Expression expr) {
        // Unknown behavior.
        if (expr instanceof ChannelExpression) {
            return ExprBehavior.UNKNOWN;
        } else if (expr instanceof ReadCallExpression) {
            return ExprBehavior.UNKNOWN;
        } else if (expr instanceof ModelReference) {
            return ExprBehavior.UNKNOWN;
        } else if (expr instanceof TimeLiteral) {
            return ExprBehavior.TIME;
        }

        // Literal values and constants never change.
        if (expr instanceof BoolLiteral) {
            return ExprBehavior.CONSTANT;
        } else if (expr instanceof IntNumber) {
            return ExprBehavior.CONSTANT;
        } else if (expr instanceof StringLiteral) {
            return ExprBehavior.CONSTANT;
        } else if (expr instanceof RealNumber) {
            return ExprBehavior.CONSTANT;
        } else if (expr instanceof ProcessReference) {
            return ExprBehavior.CONSTANT;
        } else if (expr instanceof EnumValueReference) {
            return ExprBehavior.CONSTANT;
        } else if (expr instanceof FunctionReference) {
            return ExprBehavior.CONSTANT;
        } else if (expr instanceof ConstantReference) {
            return ExprBehavior.CONSTANT;
        }

        // Most stdlib functions derive new values from existing ones in a
        // deterministic way, so they do not change if their arguments do not
        // change. 'ready' is over-approximated to be safe, file-io and process
        // instances are considered to be totally unpredictable.
        if (expr instanceof StdLibFunctionReference) {
            StdLibFunctionReference sfr = (StdLibFunctionReference)expr;
            switch (sfr.getFunction()) {
                case ABS:
                case ACOS:
                case ACOSH:
                case ASIN:
                case ASINH:
                case ATAN:
                case ATANH:
                case BERNOULLI:
                case BETA:
                case BINOMIAL:
                case CBRT:
                case CEIL:
                case CONSTANT:
                case COS:
                case COSH:
                case EMPTY:
                case ENUMERATE:
                case ERLANG:
                case EXP:
                case EXPONENTIAL:
                case FLOOR:
                case GAMMA:
                case GEOMETRIC:
                case INSERT:
                case LN:
                case LOG:
                case LOG_NORMAL:
                case MAX:
                case MIN:
                case NORMAL:
                case POISSON:
                case POP:
                case RANDOM:
                case RANGE:
                case ROUND:
                case SIGN:
                case SIN:
                case SINH:
                case SIZE:
                case SORT:
                case SQRT:
                case TAN:
                case TANH:
                case TRIANGLE:
                case UNIFORM:
                case WEIBULL:
                case DELETE:
                case DICT_KEYS:
                case DICT_VALUES:
                    return ExprBehavior.CONSTANT;

                case READY:
                    return ExprBehavior.TIME;

                case EOF:
                case EOL:
                case NEWLINES:
                case FINISHED:
                case OPEN:
                    return ExprBehavior.UNKNOWN;
            }
            Assert.fail(fmt("Unknown type of library function: %s", sfr.getFunction()));
        }

        if (expr instanceof ListExpression) {
            ListExpression le = (ListExpression)expr;
            ExprBehavior behavior = ExprBehavior.CONSTANT;
            for (Expression e: le.getElements()) {
                behavior = merge(behavior, getBehavior(e));
            }
            return behavior;
        } else if (expr instanceof SetExpression) {
            SetExpression se = (SetExpression)expr;
            ExprBehavior behavior = ExprBehavior.CONSTANT;
            for (Expression e: se.getElements()) {
                behavior = merge(behavior, getBehavior(e));
            }
            return behavior;
        } else if (expr instanceof MatrixExpression) {
            MatrixExpression me = (MatrixExpression)expr;
            ExprBehavior behavior = ExprBehavior.CONSTANT;
            for (MatrixRow mr: me.getRows()) {
                for (Expression e: mr.getElements()) {
                    behavior = merge(behavior, getBehavior(e));
                }
            }
            return behavior;
        } else if (expr instanceof CastExpression) {
            CastExpression ce = (CastExpression)expr;
            Type tp = dropReferences(ce.getType());
            // Casts to timer may change in time, otherwise casting is as
            // stable as its argument.
            ExprBehavior behavior;
            if (tp instanceof TimerType) {
                behavior = ExprBehavior.TIME;
            } else {
                behavior = ExprBehavior.CONSTANT;
            }
            return merge(behavior, getBehavior(ce.getExpression()));
        } else if (expr instanceof CallExpression) {
            // Over-approximation of function calls. In general, it is not
            // known how a function derives its value. Therefore we take all
            // information supplied to the function into account.
            CallExpression ce = (CallExpression)expr;
            ExprBehavior behavior = getBehavior(ce.getFunction());
            for (Expression e: ce.getArguments()) {
                behavior = merge(behavior, getBehavior(e.getType()));
                behavior = merge(behavior, getBehavior(e));
            }
            return behavior;
        }

        if (expr instanceof TupleExpression) {
            TupleExpression te = (TupleExpression)expr;
            ExprBehavior behavior = ExprBehavior.CONSTANT;
            for (Expression e: te.getFields()) {
                behavior = merge(behavior, getBehavior(e));
            }
            return behavior;
        } else if (expr instanceof DictionaryExpression) {
            DictionaryExpression de = (DictionaryExpression)expr;
            ExprBehavior behavior = ExprBehavior.CONSTANT;
            for (DictionaryPair p: de.getPairs()) {
                behavior = merge(behavior, getBehavior(p.getKey()));
                behavior = merge(behavior, getBehavior(p.getValue()));
            }
            return behavior;
        }

        // Unary and binary expressions. Most of these functions behave 'nice'
        // in the sense that they produce a new value rather than re-using an
        // existing value. The exceptions are projection and sample.
        if (expr instanceof BinaryExpression) {
            BinaryExpression be = (BinaryExpression)expr;
            ExprBehavior behavior;
            if (be.getOp() == BinaryOperators.PROJECTION) {
                // Projection behaves like a variable reference.
                behavior = getBehavior(expr.getType());
            } else {
                behavior = ExprBehavior.CONSTANT;
            }
            behavior = merge(behavior, getBehavior(be.getLeft()));
            return merge(behavior, getBehavior(be.getRight()));
        } else if (expr instanceof UnaryExpression) {
            UnaryExpression ue = (UnaryExpression)expr;
            // Sample gives non-deterministic output.
            if (ue.getOp() == UnaryOperators.SAMPLE) {
                return ExprBehavior.UNKNOWN;
            }
            return getBehavior(ue.getChild());
        }

        // Over-approximation of the variable-like expressions.
        if (expr instanceof VariableReference) {
            return getBehavior(expr.getType());
        } else if (expr instanceof FieldReference) {
            return getBehavior(expr.getType());
        }

        if (expr instanceof SliceExpression) {
            SliceExpression se = (SliceExpression)expr;
            ExprBehavior behavior = getBehavior(se.getSource());
            if (se.getStart() != null) {
                behavior = merge(behavior, getBehavior(se.getStart()));
            }
            if (se.getEnd() != null) {
                behavior = merge(behavior, getBehavior(se.getEnd()));
            }
            if (se.getStep() != null) {
                behavior = merge(behavior, getBehavior(se.getStep()));
            }
            return behavior;
        }

        Assert.fail(fmt("Unknown type of expression: %s", expr));
        return ExprBehavior.CONSTANT; // Not reached.
    }

    /**
     * Decide how the value of an expression node can change by inspecting its type.
     *
     * @param tp Type to analyze.
     * @return Behavior of the analyzed type.
     */
    private static ExprBehavior getBehavior(Type tp) {
        // Constant and discrete variable types.
        if (tp instanceof VoidType) {
            return ExprBehavior.CONSTANT;
        } else if (tp instanceof BoolType) {
            return ExprBehavior.DISCRETE;
        } else if (tp instanceof IntType) {
            return ExprBehavior.DISCRETE;
        } else if (tp instanceof StringType) {
            return ExprBehavior.DISCRETE;
        } else if (tp instanceof RealType) {
            return ExprBehavior.DISCRETE;
        } else if (tp instanceof ChannelType) {
            return ExprBehavior.DISCRETE;
        } else if (tp instanceof FunctionType) {
            return ExprBehavior.DISCRETE;
        } else if (tp instanceof MatrixType) {
            return ExprBehavior.DISCRETE;
        } else if (tp instanceof EnumTypeReference) {
            return ExprBehavior.DISCRETE;
        }

        // Types with side-effects.
        if (tp instanceof FileType) {
            return ExprBehavior.UNKNOWN;
        } else if (tp instanceof DistributionType) {
            return ExprBehavior.UNKNOWN;
        } else if (tp instanceof InstanceType) {
            return ExprBehavior.UNKNOWN;
        } else if (tp instanceof TimerType) {
            return ExprBehavior.TIME;
        }

        // Container types.
        if (tp instanceof DictType) {
            DictType ttp = (DictType)tp;
            return merge(getBehavior(ttp.getKeyType()), getBehavior(ttp.getValueType()));
        } else if (tp instanceof SetType) {
            SetType ttp = (SetType)tp;
            return getBehavior(ttp.getElementType());
        } else if (tp instanceof ListType) {
            ListType ttp = (ListType)tp;
            return getBehavior(ttp.getElementType());
        } else if (tp instanceof TupleType) {
            TupleType ttp = (TupleType)tp;
            ExprBehavior behavior = ExprBehavior.CONSTANT;
            for (TupleField tf: ttp.getFields()) {
                behavior = merge(behavior, getBehavior(tf.getType()));
            }
            return behavior;
        }

        // Type references.
        if (tp instanceof TypeReference) {
            TypeReference ttp = (TypeReference)tp;
            return getBehavior(ttp.getType().getType());
        }

        // ModelType and ProcessType not handled, should never occur in value
        // expressions.
        Assert.fail(fmt("Unknown type: %s", tp));
        return ExprBehavior.CONSTANT; // Not reached.
    }

    /**
     * Decide the merged behavior, given the behavior of two parts.
     *
     * @param beh1 Behavior of the first part.
     * @param beh2 Behavior of the second part.
     * @return Behavior of the combined parts.
     */
    private static ExprBehavior merge(ExprBehavior beh1, ExprBehavior beh2) {
        if (beh1.value > beh2.value) {
            return beh1;
        }
        return beh2;
    }

    /**
     * Kinds of behavior of an expression value.
     *
     * <p>
     * Note that this is an over-approximation, when an expression changes in time, it is assumed to also change when
     * local variables are modified.
     * </p>
     */
    public static enum ExprBehavior {
        /** Value never changes. */
        CONSTANT(0),

        /** Value depends on local variables. */
        DISCRETE(1),

        /** Value depends on time. */
        TIME(2),

        /** Value may change at any time. */
        UNKNOWN(3);

        /** Measure of instability, a larger number means a less stable value. */
        public final int value;

        /**
         * Constructor of the {@link ExprBehavior} enum class.
         *
         * @param value Measure of instability (larger number means a less stable value).
         */
        private ExprBehavior(int value) {
            this.value = value;
        }
    }
}
