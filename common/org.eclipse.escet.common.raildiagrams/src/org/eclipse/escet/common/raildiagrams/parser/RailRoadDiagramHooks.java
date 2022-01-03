//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.parser;

import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;
import java.util.Optional;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.raildiagrams.railroad.BranchLabelNode;
import org.eclipse.escet.common.raildiagrams.railroad.ChoiceNode;
import org.eclipse.escet.common.raildiagrams.railroad.DiagramElement;
import org.eclipse.escet.common.raildiagrams.railroad.EmptyNode;
import org.eclipse.escet.common.raildiagrams.railroad.LoopNode;
import org.eclipse.escet.common.raildiagrams.railroad.NamedNode;
import org.eclipse.escet.common.raildiagrams.railroad.RailRule;
import org.eclipse.escet.common.raildiagrams.railroad.SequenceNode;
import org.eclipse.escet.common.raildiagrams.railroad.SequenceRow;
import org.eclipse.escet.setext.runtime.Parser;
import org.eclipse.escet.setext.runtime.Token;

/**
 * Call back hook methods for:
 * <ul>
 * <li>{@link RailRoadDiagramScanner}</li>
 * <li>{@link RailRoadDiagramParser}</li>
 * </ul>
 */
public final class RailRoadDiagramHooks implements RailRoadDiagramScanner.Hooks, RailRoadDiagramParser.Hooks {
    /** Next available id for a diagram element. */
    private int nextId = 1;

    /**
     * Get a fresh id for a diagram element.
     *
     * @return The fresh id.
     */
    private int getId() {
        int id = nextId;
        nextId++;
        return id;
    }

    @Override
    public void setParser(Parser<?> parser) {
        // Nothing to do.
    }

    @Override
    public void dropOuter(Token token) {
        int strLength = token.originalText.length();
        Assert.check(strLength >= 2);
        token.text = token.text.substring(1, strLength - 1); // Drop first and last character.
    }

    @Override // specification : rule SEMI;
    public List<RailRule> parsespecification1(RailRule r1) {
        return list(r1);
    }

    @Override // specification : specification rule SEMI;
    public List<RailRule> parsespecification2(List<RailRule> l1, RailRule r2) {
        List<RailRule> rules = copy(l1);
        rules.add(r2);
        return rules;
    }

    @Override // rule : body1;
    public RailRule parserule1(List<DiagramElement> l1) {
        DiagramElement seq = l1.size() < 2 ? l1.get(0) : new SequenceNode(list(new SequenceRow(l1)), getId());
        return new RailRule(null, seq, getId());
    }

    @Override // rule : @IDENTIFIER COLON body1;
    public RailRule parserule2(Token t1, List<DiagramElement> l3) {
        DiagramElement body = makeChoice(l3);
        return new RailRule(t1.text, body, getId());
    }

    @Override // body1 : body2;
    public List<DiagramElement> parsebody11(DiagramElement d1) {
        return list(d1);
    }

    @Override // body1 : @BR_STRING body2;
    public List<DiagramElement> parsebody12(Token t1, DiagramElement d2) {
        DiagramElement label = new BranchLabelNode(t1.text, getId());
        return list(makeSequenceRow(list(label, d2)));
    }

    @Override // body1 : body1 PIPE body2;
    public List<DiagramElement> parsebody13(List<DiagramElement> l1, DiagramElement d3) {
        List<DiagramElement> elms = copy(l1);
        elms.add(d3);
        return elms;
    }

    @Override // body1 : body1 PIPE @BR_STRING body2;
    public List<DiagramElement> parsebody14(List<DiagramElement> l1, Token t3, DiagramElement d4) {
        List<DiagramElement> elms = copy(l1);
        DiagramElement label = new BranchLabelNode(t3.text, getId());
        elms.add(makeSequenceRow(list(label, d4)));
        return elms;
    }

    @Override // body2 : body3;
    public DiagramElement parsebody21(List<Optional<DiagramElement>> l1) {
        return makeSequenceMultiRow(l1);
    }

    @Override // body2 : body3 STAR body5;
    public DiagramElement parsebody22(List<Optional<DiagramElement>> l1, Optional<DiagramElement> o3) {
        DiagramElement rhs = o3.isPresent() ? o3.get() : new EmptyNode(getId());
        LoopNode loop = new LoopNode(makeSequenceMultiRow(l1), rhs, getId());
        return new ChoiceNode(list(new EmptyNode(getId()), loop), getId());
    }

    @Override // body2 : body3 PLUS body5;
    public DiagramElement parsebody23(List<Optional<DiagramElement>> l1, Optional<DiagramElement> o3) {
        DiagramElement rhs = o3.isPresent() ? o3.get() : new EmptyNode(getId());
        DiagramElement forward = makeSequenceMultiRow(l1);
        return new LoopNode(forward, rhs, getId());
    }

    @Override // body3 : body4;
    public List<Optional<DiagramElement>> parsebody31(Optional<DiagramElement> o1) {
        return list(o1);
    }

    @Override // body3 : body3 body4;
    public List<Optional<DiagramElement>> parsebody32(List<Optional<DiagramElement>> l1, Optional<DiagramElement> o2) {
        List<Optional<DiagramElement>> elms = copy(l1);
        elms.add(o2);
        return elms;
    }

    @Override // body4 : body5;
    public Optional<DiagramElement> parsebody41(Optional<DiagramElement> o1) {
        return o1;
    }

    @Override // body4 : body5 QUEST;
    public Optional<DiagramElement> parsebody42(Optional<DiagramElement> o1) {
        DiagramElement rhs = o1.isPresent() ? o1.get() : new EmptyNode(getId());
        List<DiagramElement> elms = list(new EmptyNode(getId()), rhs);
        return Optional.of(new ChoiceNode(elms, getId()));
    }

    @Override // body5 : @SQUOTE_STRING;
    public Optional<DiagramElement> parsebody51(Token t1) {
        return Optional.of(new NamedNode(null, t1.text, getId()));
    }

    @Override // body5 : @DQUOTE_STRING;
    public Optional<DiagramElement> parsebody52(Token t1) {
        return Optional.of(new NamedNode(null, t1.text, getId()));
    }

    @Override // body5 : PAROPEN body1 PARCLOSE;
    public Optional<DiagramElement> parsebody53(List<DiagramElement> l2) {
        return Optional.of(makeChoice(l2));
    }

    @Override // body5 : BSLASH_BSLASH;
    public Optional<DiagramElement> parsebody54() {
        return Optional.empty();
    }

    @Override // body5 : PAROPEN PARCLOSE;
    public Optional<DiagramElement> parsebody55() {
        return Optional.of(new EmptyNode(getId()));
    }

    @Override // body5 : @IDENTIFIER;
    public Optional<DiagramElement> parsebody56(Token t1) {
        return Optional.of(new NamedNode(t1.text, getId()));
    }

    /**
     * Construct a choice element if there are at least two children, else return
     * the single child.
     *
     * @param choices Alternatives to choose between.
     * @return An object for performing one of the provided alternatives.
     */
    private DiagramElement makeChoice(List<DiagramElement> choices) {
        if (choices.size() == 1) {
            return choices.get(0);
        }
        return new ChoiceNode(choices, getId());
    }

    /**
     * Construct a possibly multi-row sequence element if there are at least two
     * children, else return the single child.
     *
     * @param optElements Children to sequentially perform, some children may be
     *                    {@code null} indicating a break to the next row in the
     *                    sequence.
     * @return An object for performing all provided alternatives.
     */
    private DiagramElement makeSequenceMultiRow(List<Optional<DiagramElement>> optElements) {
        List<SequenceRow> rows = list();
        List<DiagramElement> elements = list();
        for (Optional<DiagramElement> optVal : optElements) {
            if (optVal.isPresent()) {
                elements.add(optVal.get());
            } else if (!elements.isEmpty()) {
                rows.add(new SequenceRow(elements));
                elements = list();
            }
        }
        if (!elements.isEmpty()) {
            rows.add(new SequenceRow(elements));
        }

        if (rows.size() == 1 && rows.get(0).elements.size() == 1) {
            return first(first(rows).elements);
        }
        return new SequenceNode(rows, getId());
    }

    /**
     * Construct a single row sequence element if there are at least two children,
     * else return the single child.
     *
     * @param sequence Children to sequentially perform.
     * @return An object for performing all provided alternatives.
     */
    private DiagramElement makeSequenceRow(List<DiagramElement> sequence) {
        if (sequence.size() == 1) {
            return sequence.get(0);
        }
        return new SequenceNode(list(new SequenceRow(sequence)), getId());
    }
}
