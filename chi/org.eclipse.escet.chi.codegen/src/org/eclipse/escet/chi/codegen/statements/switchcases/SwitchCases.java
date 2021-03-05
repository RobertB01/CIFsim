//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.codegen.statements.switchcases;

import static org.eclipse.escet.chi.codegen.Constants.INDENT_SIZE;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.makeExpression;
import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.statements.seq.Seq;
import org.eclipse.escet.chi.codegen.statements.seq.SeqBox;
import org.eclipse.escet.chi.codegen.statements.seq.SeqBreak;
import org.eclipse.escet.chi.codegen.statements.seq.SeqCode;
import org.eclipse.escet.chi.codegen.statements.seq.SeqContinue;
import org.eclipse.escet.chi.codegen.statements.seq.SeqExit;
import org.eclipse.escet.chi.codegen.statements.seq.SeqForLoop;
import org.eclipse.escet.chi.codegen.statements.seq.SeqIfElse;
import org.eclipse.escet.chi.codegen.statements.seq.SeqList;
import org.eclipse.escet.chi.codegen.statements.seq.SeqReturn;
import org.eclipse.escet.chi.codegen.statements.seq.SeqSelect;
import org.eclipse.escet.chi.codegen.statements.seq.SeqSelect.SelectAlternative;
import org.eclipse.escet.chi.codegen.statements.seq.SeqWhile;
import org.eclipse.escet.chi.metamodel.chi.BehaviourDeclaration;
import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Collection of {@link SeqCase} that together form a process (<tt>proc</tt> or <tt>model</tt> instance). */
public class SwitchCases {
    /** Reasons to split a sequential language program. */
    public enum SplitReason {
        /** No split needed. */
        NOT_NEEDED,

        /** Uses 'break' to jump, which has wrong semantics in a list of switch cases. */
        USES_BREAK,

        /** Blocks, waiting for a delay or communication. */
        BLOCKS;

        /**
         * Decide the most relevant reason for splitting.
         *
         * @param r1 First reason to split.
         * @param r2 Second reason to split.
         * @return Most relevant reason to split.
         */
        public static SplitReason max(SplitReason r1, SplitReason r2) {
            if (r1 == SplitReason.BLOCKS || r2 == SplitReason.BLOCKS) {
                return SplitReason.BLOCKS;
            } else if (r1 == SplitReason.USES_BREAK || r2 == SplitReason.USES_BREAK) {
                return SplitReason.USES_BREAK;
            }
            return SplitReason.NOT_NEEDED;
        }

        /**
         * Is this reason the most relevant one that exists?
         *
         * @return The provided reason is the most relevant one.
         */
        public boolean isMaxRelevant() {
            return this.equals(SplitReason.BLOCKS);
        }
    }

    /** Last used switch case number. */
    private int number;

    /** Imports needed to have compilable code. */
    private List<String> neededImports;

    /** Converted switch cases. */
    private SeqCaseCollection collection;

    /** Constructor. */
    public SwitchCases() {
        number = -1;
        collection = null;
        neededImports = list();
    }

    /**
     * Add the given import path to the list of needed imports, if not done so already.
     *
     * @param importPath Import path to add.
     */
    @SuppressWarnings("unused")
    private void addNeededImport(String importPath) {
        if (!neededImports.contains(importPath)) {
            neededImports.add(importPath);
        }
    }

    /**
     * Create a sequential language fragment case.
     *
     * <p>
     * The case is not added to any collection.
     * </p>
     *
     * @param seqs Fragment to add, may be {@code null}.
     * @return The created case.
     */
    public SeqCase makeCase(List<Seq> seqs) {
        number++;
        return makeCase(seqs, number);
    }

    /**
     * Create a sequential language fragment case.
     *
     * <p>
     * The case is not added to any collection.
     * </p>
     *
     * @param seqs Fragment to add, may be {@code null}.
     * @param caseNumber Number of the switch case.
     * @return The created case.
     */
    public SeqCase makeCase(List<Seq> seqs, int caseNumber) {
        if (seqs == null) {
            seqs = list();
        }
        for (Seq s: seqs) {
            Assert.notNull(s);
        }
        SeqCase cs = new SeqCase(caseNumber, seqs);
        return cs;
    }

    /**
     * Convert a sequential language process body, and put it into switch cases.
     *
     * @param seqs Sequential language statements.
     * @param bd Process declaration associated with the sequential language statements.
     */
    public void convertBody(List<Seq> seqs, BehaviourDeclaration bd) {
        checkNotNull(seqs);
        // createCode of select alternatives is used as switch-case number.
        // Ensure 'our' number is higher than that.
        number = findMaxCreateCode(seqs, -1);

        List<Seq> endSeqs = list();
        endSeqs.add(SeqReturn.finishProcess(bd));
        SeqCase endCase = makeCase(endSeqs);

        SeqCaseCollection scc = new SeqCaseCollection(endCase, endCase.caseNumber);
        collection = convertBody(seqs, scc, this);
    }

    /**
     * Check that the seqs do not contain {@code null}.
     *
     * @param seqs Seqs to check.
     */
    private static void checkNotNull(List<Seq> seqs) {
        for (Seq s: seqs) {
            if (s instanceof SeqBreak) {
                continue;
            } else if (s instanceof SeqCode) {
                continue;
            } else if (s instanceof SeqBox) {
                continue;
            } else if (s instanceof SeqContinue) {
                continue;
            } else if (s instanceof SeqForLoop) {
                SeqForLoop seqFor = (SeqForLoop)s;
                checkNotNull(seqFor.childStats.seqs);
                continue;
            } else if (s instanceof SeqIfElse) {
                SeqIfElse ifElse = (SeqIfElse)s;
                checkNotNull(ifElse.ifChilds.seqs);
                if (ifElse.elseChilds != null) {
                    checkNotNull(ifElse.elseChilds.seqs);
                }
                continue;
            } else if (s instanceof SeqReturn) {
                continue;
            } else if (s instanceof SeqSelect) {
                SeqSelect sel = (SeqSelect)s;
                for (SelectAlternative a: sel.alternatives) {
                    checkNotNull(a.createCode);
                    if (a.code != null) {
                        checkNotNull(a.code.seqs);
                    }
                }
                continue;
            } else if (s instanceof SeqExit) {
                continue;
            } else if (s instanceof SeqWhile) {
                SeqWhile wseq = (SeqWhile)s;
                checkNotNull(wseq.childs.seqs);
                continue;
            }
            Assert.fail("Unknown sequential language statement " + s.toString() + " encountered.");
        }
    }

    /**
     * Find the biggest number used for a 'select' case.
     *
     * @param seqs Statements to search recursively.
     * @param num Highest number found so far.
     * @return Highest number found in the statements.
     */
    private static int findMaxCreateCode(List<Seq> seqs, int num) {
        for (Seq s: seqs) {
            if (s instanceof SeqSelect) {
                SeqSelect ss = (SeqSelect)s;
                for (SelectAlternative sa: ss.alternatives) {
                    if (sa.number > num) {
                        num = sa.number;
                    }
                    if (sa.code != null) {
                        num = findMaxCreateCode(sa.code.seqs, num);
                    }
                }
            } else if (s instanceof SeqForLoop) {
                SeqForLoop ss = (SeqForLoop)s;
                num = findMaxCreateCode(ss.childStats.seqs, num);
            } else if (s instanceof SeqIfElse) {
                SeqIfElse sie = (SeqIfElse)s;
                num = findMaxCreateCode(sie.ifChilds.seqs, num);
                if (sie.elseChilds != null) {
                    num = findMaxCreateCode(sie.elseChilds.seqs, num);
                }
            } else if (s instanceof SeqWhile) {
                SeqWhile ss = (SeqWhile)s;
                num = findMaxCreateCode(ss.childs.seqs, num);
            }
        }
        return num;
    }

    /**
     * Convert the body to box format.
     *
     * @return The switch cases in box format.
     */
    public Box boxify() {
        VBox outerBox = new VBox(0);
        outerBox.add("if (chiChoice == null) {");

        VBox choiceBox = new VBox(INDENT_SIZE);
        choiceBox.add(fmt("chiProgramCounter = %d;", collection.entry));
        outerBox.add(choiceBox);
        outerBox.add("} else {");

        choiceBox = new VBox(INDENT_SIZE);
        choiceBox.add("chiChoice.setVariables();");
        choiceBox.add("chiProgramCounter = chiChoice.getChoice();");
        outerBox.add(choiceBox);
        outerBox.add("}");

        // Check sanity of the position stack.
        outerBox.add("if (positionStack.size() != 1) throw new ChiSimulatorException(\"Unexpected length of the "
                + "position stack.\");");

        outerBox.add("for (;;) {");
        VBox forBox = new VBox(INDENT_SIZE);
        forBox.add("chiCoordinator.testTerminating();");
        forBox.add("switch (chiProgramCounter) {");
        for (SeqCase sc: collection.cases) {
            VBox caseBox = new VBox(INDENT_SIZE);
            caseBox.add(fmt("case %d: {", sc.caseNumber));
            VBox vb = new VBox(INDENT_SIZE);
            vb.add(sc.boxify());
            caseBox.add(vb);
            caseBox.add("}");
            forBox.add(caseBox);
        }
        VBox caseBox = new VBox(INDENT_SIZE);
        caseBox.add("default: throw new ChiSimulatorException(\"Unknown process fragment \" + "
                + "String.valueOf(chiProgramCounter) + \" encountered.\");");
        forBox.add(caseBox);
        forBox.add("}");
        outerBox.add(forBox);
        outerBox.add("}");
        return outerBox;
    }

    /**
     * Return the imports needed for the code.
     *
     * @return Needed imports.
     */
    public List<String> getNeededImport() {
        return neededImports;
    }

    /**
     * Convert a sequential program to switch cases. At the end jump to the otherCases.
     *
     * @param seqs Sequential program to convert.
     * @param otherCases Cases at the end of the program.
     * @param scs Switch case context.
     * @return Switch cases.
     */
    public static SeqCaseCollection convertBody(List<Seq> seqs, SeqCaseCollection otherCases, SwitchCases scs) {
        SeqCase start = scs.makeCase(null);
        return convertSeqs(start, seqs, otherCases, scs);
    }

    /**
     * Convert (a fragment of) a sequential program to switch cases.
     *
     * @param prefix Switch case above the program fragment, may be used to append statements to.
     * @param seqs Sequential program to convert.
     * @param otherCases Cases at the end of the program.
     * @param scs Switch case context.
     * @return The entire program converted to switch cases.
     */
    private static SeqCaseCollection convertSeqs(SeqCase prefix, List<Seq> seqs, SeqCaseCollection otherCases,
            SwitchCases scs)
    {
        // Move sequential statements to the prefix as much as possible.
        int i = 0;
        while (i < seqs.size() && needsSplit(seqs.get(i)) == SplitReason.NOT_NEEDED) {
            prefix.statements.add(seqs.get(i));
            i++;
        }
        if (i == seqs.size()) {
            // All statements got moved, make it one list of switch cases.
            Seq jump = SeqReturn.jumpToCase(otherCases.entry, null);
            prefix.statements.add(jump);
            List<SeqCase> result = concat(prefix, otherCases.cases);
            return new SeqCaseCollection(result, prefix.caseNumber);
        }

        // seqs[i] needs a split.

        // First, do the seqs[i+1...]
        otherCases = convertBody(seqs.subList(i + 1, seqs.size()), otherCases, scs);

        // Finally, convert the seqs[i] statement.
        if (seqs.get(i) instanceof SeqForLoop) {
            SeqForLoop seq = (SeqForLoop)seqs.get(i);
            return splitForLoop(prefix, seq, otherCases, scs);
        } else if (seqs.get(i) instanceof SeqIfElse) {
            SeqIfElse seq = (SeqIfElse)seqs.get(i);
            return splitIfElse(prefix, seq, otherCases, scs);
        } else if (seqs.get(i) instanceof SeqSelect) {
            SeqSelect seq = (SeqSelect)seqs.get(i);
            return splitSelect(prefix, seq, otherCases, scs);
        } else if (seqs.get(i) instanceof SeqWhile) {
            SeqWhile seq = (SeqWhile)seqs.get(i);
            return splitWhile(prefix, seq, otherCases, scs);
        }
        Assert.fail("Encountered unrecognized statement to split:" + seqs.get(i).toString());
        return null;
    }

    /**
     * Split a 'for' statement to switch cases.
     *
     * @param prefix Switch case above the program fragment, may be used to append statements to.
     * @param seq The for statement to translate.
     * @param otherCases Cases at the end of the program.
     * @param scs Switch case context.
     * @return The entire program converted to switch cases.
     */
    private static SeqCaseCollection splitForLoop(SeqCase prefix, SeqForLoop seq, SeqCaseCollection otherCases,
            SwitchCases scs)
    {
        // Append initialization code to the prefix.
        SeqCode initStat = new SeqCode(null, null);
        if (seq.init != null) {
            seq.init.setCurrentPositionStatement(initStat.lines);
            initStat.lines.addAll(seq.init.getCode());
            initStat.lines.add(seq.init.getValue() + ";");
        }
        prefix.statements.add(initStat);

        SeqCase condTest = scs.makeCase(null);
        if (seq.endCondition != null) {
            // test end condition, and jump to 'otherCases' if it fails.
            condTest.statements.add(makeIfGoto(true, seq.endCondition, otherCases.entry, seq.position));
        }

        // increment, and jump back to the condition test.
        SeqCase incr = scs.makeCase(null);
        SeqCode incrStat = new SeqCode(null, null);
        if (seq.increment != null) {
            seq.increment.setCurrentPositionStatement(incrStat.lines);
            incrStat.lines.addAll(seq.increment.getCode());
            incrStat.lines.add(seq.increment.getValue() + ";");
        }
        incr.statements.add(incrStat);
        incr.statements.add(SeqReturn.jumpToCase(condTest.caseNumber, seq.position));

        // Replace the 'break' and 'continue' statements in the child.
        replaceBreakContinue(otherCases.entry, condTest.caseNumber, seq.childStats);

        // Glue increment code ahead of the otherCases code.
        otherCases = new SeqCaseCollection(concat(incr, otherCases.cases), incr.caseNumber);
        // Convert child body.
        otherCases = convertSeqs(condTest, seq.childStats.seqs, otherCases, scs);

        prefix.statements.add(SeqReturn.jumpToCase(otherCases.entry, seq.position));
        return new SeqCaseCollection(concat(prefix, otherCases.cases), prefix.caseNumber);
    }

    /**
     * Split an 'if' statement to switch cases.
     *
     * @param prefix Switch case above the program fragment, may be used to append statements to.
     * @param seq The if/else statement to translate.
     * @param otherCases Cases at the end of the program.
     * @param scs Switch case context.
     * @return The entire program converted to switch cases.
     */
    private static SeqCaseCollection splitIfElse(SeqCase prefix, SeqIfElse seq, SeqCaseCollection otherCases,
            SwitchCases scs)
    {
        if (seq.elseChilds == null) {
            // No 'else' branch.
            prefix.statements.add(makeIfGoto(true, seq.condition, otherCases.entry, seq.position));
            return convertSeqs(prefix, seq.ifChilds.seqs, otherCases, scs);
        }
        // Both 'if' and 'else' branches.
        SeqCaseCollection elseBranch = convertBody(seq.elseChilds.seqs, otherCases, scs);
        SeqCaseCollection ifDest = new SeqCaseCollection(elseBranch.cases, otherCases.entry);
        otherCases = convertBody(seq.ifChilds.seqs, ifDest, scs);

        prefix.statements.add(makeIfGoto(false, seq.condition, otherCases.entry, seq.position));
        prefix.statements.add(SeqReturn.jumpToCase(elseBranch.entry, seq.position));
        return new SeqCaseCollection(concat(prefix, otherCases.cases), prefix.caseNumber);
    }

    /**
     * Split a select statement.
     *
     * @param prefix Code to execute before the select.
     * @param seq Select statement to split.
     * @param otherCases Code to execute afterwards.
     * @param scs Collected switch cases.
     * @return The generated sequential procedure blocks, and the starting point.
     */
    private static SeqCaseCollection splitSelect(SeqCase prefix, SeqSelect seq, SeqCaseCollection otherCases,
            SwitchCases scs)
    {
        List<SeqCase> swCases = otherCases.cases;
        int jumpTarget = otherCases.entry; // All alternatives jump to here.

        // Prepare the prefix for adding the select alternative creation codes.
        List<String> lines = list();
        lines.add("selectList.clear();");
        prefix.statements.add(new SeqCode(lines, null));

        int i = seq.alternatives.size() - 1;
        while (i >= 0) {
            // Convert alternative i, using alt.code as jump destination.
            SelectAlternative alt = seq.alternatives.get(i);

            // Add the alternative creation code to the prefix.
            Assert.check(needsSplit(alt.createCode) == SplitReason.NOT_NEEDED);
            prefix.statements.addAll(alt.createCode);

            SeqCase start = scs.makeCase(null, alt.number);
            if (alt.code == null) {
                // TODO Silly 'goto' for empty select-cases.
                start.statements.add(SeqReturn.jumpToCase(jumpTarget, null));
                swCases = concat(start, swCases);
            } else {
                // alt.code != null
                SeqCaseCollection scc = new SeqCaseCollection(swCases, jumpTarget);
                scc = convertSeqs(start, alt.code.seqs, scc, scs);
                swCases = scc.cases;
            }
            i--;
        }
        lines = list();
        lines.add("setSelect(selectList);");
        prefix.statements.add(new SeqCode(lines, null));
        // return 'Block on select'.
        prefix.statements.add(SeqReturn.selectBlock(seq.position));
        return new SeqCaseCollection(concat(prefix, swCases), prefix.caseNumber);
    }

    /**
     * Construct an 'if (cond) goto jumpTarget' statement.
     *
     * @param invert Invert the boolean condition.
     * @param cond Condition to check.
     * @param jumpTarget Destination to jump to.
     * @param position Position information of the test/jump.
     * @return The created sequential statement.
     */
    private static Seq makeIfGoto(boolean invert, ExpressionBase cond, int jumpTarget, PositionObject position) {
        List<Seq> ifStats = list();
        if (invert) {
            cond = makeExpression(cond.getCode(), "!(" + cond.getValue() + ")", position);
        }
        ifStats.add(SeqReturn.jumpToCase(jumpTarget, position));
        return new SeqIfElse(cond, new SeqList(ifStats), null, null);
    }

    /**
     * Split a while statement into switch cases to allow its body to be converted to switch cases.
     *
     * @param prefix Switch case above the program fragment, may be used to append statements to.
     * @param seq Sequential while statement to convert.
     * @param otherCases Code to execute afterwards.
     * @param scs Switch case context.
     * @return The entire program converted to switch cases.
     */
    private static SeqCaseCollection splitWhile(SeqCase prefix, SeqWhile seq, SeqCaseCollection otherCases,
            SwitchCases scs)
    {
        // Generate condition + jump to other code.
        SeqCase condTest = scs.makeCase(null);
        condTest.statements.add(makeIfGoto(true, seq.condition, otherCases.entry, seq.position));
        // Replace the 'break' and 'continue' statements in the child.
        replaceBreakContinue(otherCases.entry, condTest.caseNumber, seq.childs);

        otherCases = new SeqCaseCollection(otherCases.cases, condTest.caseNumber);

        // Prefix jumps to the condition.
        prefix.statements.add(SeqReturn.jumpToCase(condTest.caseNumber, null));

        otherCases = convertSeqs(condTest, seq.childs.seqs, otherCases, scs);
        return new SeqCaseCollection(concat(prefix, otherCases.cases), prefix.caseNumber);
    }

    /**
     * Does one of the given sequential language statements need to be split for interruptions?
     *
     * @param seqList Statements to consider.
     * @return Best reason to split.
     */
    private static SplitReason needsSplit(List<Seq> seqList) {
        SplitReason r = SplitReason.NOT_NEEDED;
        for (Seq seq: seqList) {
            r = SplitReason.max(r, needsSplit(seq));
            if (r.isMaxRelevant()) {
                return r;
            }
        }
        return r;
    }

    /**
     * Does the given sequential language statement need to be split for interruptions?
     *
     * @param seq Statement to consider.
     * @return Most relevant reason why the statement needs to be split.
     */
    private static SplitReason needsSplit(Seq seq) {
        if (seq instanceof SeqBreak) {
            SeqBreak sb = (SeqBreak)seq;
            return sb.hasDestination() ? SplitReason.NOT_NEEDED : SplitReason.USES_BREAK;
        } else if (seq instanceof SeqCode) {
            return SplitReason.NOT_NEEDED;
        } else if (seq instanceof SeqBox) {
            return SplitReason.NOT_NEEDED;
        } else if (seq instanceof SeqContinue) {
            return SplitReason.NOT_NEEDED;
        } else if (seq instanceof SeqForLoop) {
            SeqForLoop seqFor = (SeqForLoop)seq;
            return needsSplit(seqFor.childStats.seqs);
        } else if (seq instanceof SeqIfElse) {
            SeqIfElse ifElse = (SeqIfElse)seq;
            SplitReason r = needsSplit(ifElse.ifChilds.seqs);
            if (r.isMaxRelevant()) {
                return r;
            }
            if (ifElse.elseChilds != null) {
                r = SplitReason.max(r, needsSplit(ifElse.elseChilds.seqs));
            }
            return r;
        } else if (seq instanceof SeqReturn) {
            return SplitReason.NOT_NEEDED;
        } else if (seq instanceof SeqSelect) {
            return SplitReason.BLOCKS;
        } else if (seq instanceof SeqExit) {
            return SplitReason.NOT_NEEDED;
        } else if (seq instanceof SeqWhile) {
            SeqWhile wseq = (SeqWhile)seq;
            return needsSplit(wseq.childs.seqs);
        }
        Assert.fail("Unknown sequential language statement " + seq.toString() + " encountered.");
        return SplitReason.NOT_NEEDED;
    }

    /**
     * Replace the 'break' and 'continue' statements by jumps.
     *
     * <p>
     * It skips nested 'while' and 'for' statements.
     * </p>
     *
     * @param breakDest Destination for the 'break' jump. Use {@code null} for not replacing the break statements.
     * @param continueDest Destination for the 'continue' jump. Use {@code null} for not replacing the continue
     *     statements.
     * @param seqs Statements containing the 'break' and 'continue' statements.
     */
    private static void replaceBreakContinue(Integer breakDest, Integer continueDest, SeqList seqs) {
        for (Seq s: seqs.seqs) {
            if (breakDest != null && s instanceof SeqBreak) {
                SeqBreak brk = (SeqBreak)s;
                brk.setDestination(breakDest);
            } else if (continueDest != null && s instanceof SeqContinue) {
                SeqContinue cont = (SeqContinue)s;
                cont.setDestination(continueDest);
            } else if (s instanceof SeqIfElse) {
                SeqIfElse sie = (SeqIfElse)s;
                replaceBreakContinue(breakDest, continueDest, sie.ifChilds);
                if (sie.elseChilds != null) {
                    replaceBreakContinue(breakDest, continueDest, sie.elseChilds);
                }
            } else if (s instanceof SeqSelect) {
                SeqSelect ss = (SeqSelect)s;
                for (SelectAlternative sa: ss.alternatives) {
                    if (sa.code != null) {
                        replaceBreakContinue(breakDest, continueDest, sa.code);
                    }
                }
            }
        }
    }
}
