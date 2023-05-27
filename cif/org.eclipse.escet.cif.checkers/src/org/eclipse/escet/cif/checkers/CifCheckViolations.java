//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.sortedgeneric;
import static org.eclipse.escet.common.java.Sets.sortedstrings;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.common.emf.EMFPath;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF check condition violations. */
public class CifCheckViolations {
    /** The violations collected so far. */
    private final Set<CifCheckViolation> violations = set();

    /** The lines of text of the CIF specification. */
    private final List<String> specLines;

    /**
     * Constructor for the {@link CifCheckViolations} class.
     *
     * @param specLines The lines of text of the CIF specification.
     */
    public CifCheckViolations(List<String> specLines) {
        this.specLines = specLines;
    }

    /**
     * Returns whether any violations were collected so far.
     *
     * @return {@code true} if violations were collected, {@code false} otherwise.
     */
    public boolean hasViolations() {
        return !violations.isEmpty();
    }

    /**
     * Returns the violations collected so far.
     *
     * @return The violations.
     */
    public Stream<CifCheckViolation> getViolations() {
        return violations.stream();
    }

    /**
     * Add a violation.
     *
     * @param reportObject The CIF object for which the violation is to be reported.
     * @param message The message describing the violation. Should in principle start with a capital letter, but not end
     *     with a period to end the sentence.
     * @param args The message format pattern arguments.
     * @see CifCheckViolation#CifCheckViolation
     */
    public void add(PositionObject reportObject, String message, Object... args) {
        // Format the message.
        String formattedMessage = fmt(message, args);

        // Get the report object position.
        Assert.notNull(reportObject);
        if (reportObject.getPosition() == null) {
            throw new NullPointerException(new EMFPath(reportObject, null).toString());
        }
        Position position = reportObject.getPosition();

        // Get the specification line.
        String line;
        if (reportObject instanceof Specification) {
            line = null; // Report on entire specification.
        } else {
            int lineNr = position.getStartLine();
            Assert.check(lineNr >= 1, lineNr);
            Assert.check(lineNr <= specLines.size(), lineNr);
            line = specLines.get(lineNr - 1); // Start line is 1-based, lines are 0-based.
        }

        // Add the violation.
        violations.add(new CifCheckViolation(reportObject, formattedMessage, line));
    }

    /**
     * Creates a report of the violations.
     *
     * @return The lines of text of the report.
     */
    public List<String> createReport() {
        List<String> lines = list();

        // Add violations per message.
        int violationMsgNr = 0;
        Map<String, List<CifCheckViolation>> violationsPerMessage = violations.stream()
                .collect(Collectors.groupingBy(v -> v.getMessage()));
        for (String violationMessage: sortedstrings(violationsPerMessage.keySet())) {
            // Determine whether message concerns the entire specification.
            List<CifCheckViolation> violationsForMessage = violationsPerMessage.get(violationMessage);
            Set<Boolean> entireSpecs = violationsForMessage.stream().map(v -> v.getContext().isEntireSpec())
                    .collect(Collectors.toSet());
            Assert.areEqual(entireSpecs.size(), 1); // Don't mix entire-spec violations with non-entire-spec violations.

            // Add violation message and separator.
            violationMsgNr++;
            String violationMsg = fmt("(%d/%d) %s.", violationMsgNr, violationsPerMessage.size(), violationMessage);
            String violationMsgHeader = Strings.duplicate("-", violationMsg.length());
            lines.add("");
            lines.add("  " + violationMsgHeader);
            lines.add("  " + violationMsg);
            lines.add("  " + violationMsgHeader);

            // Add violations per context.
            Map<CifCheckViolationContext, List<CifCheckViolation>> violationsPerContext = violationsForMessage.stream()
                    .collect(Collectors.groupingBy(v -> v.getContext()));
            List<CifCheckViolationContext> contexts = violationsPerContext.keySet().stream().sorted().toList();
            for (CifCheckViolationContext context: contexts) {
                // Nothing more to add if the violation concerns the entire specification.
                if (context.isEntireSpec()) {
                    continue;
                }

                // Add context.
                lines.add(fmt("   * In %s:", context.toString()));

                // Add violations per line.
                List<CifCheckViolation> violationsForContext = violationsPerContext.get(context);
                Map<Integer, List<CifCheckViolation>> violationsPerLineNr = violationsForContext.stream()
                        .collect(Collectors.groupingBy(v -> v.getReportObject().getPosition().getStartLine()));
                for (int lineNr: sortedgeneric(violationsPerLineNr.keySet())) {
                    // Add line.
                    List<CifCheckViolation> violationsForLine = violationsPerLineNr.get(lineNr);
                    String line = first(violationsForLine).getSpecLine().stripTrailing();
                    int lineLen = line.length();
                    line = line.stripLeading();
                    int strippedLineLen = line.length();
                    int strippedCount = lineLen - strippedLineLen;
                    lines.add("     - " + line);

                    // Add line markers, merging the markers on the same line.
                    StringBuilder markers = new StringBuilder();
                    for (CifCheckViolation violation: violationsForLine) {
                        int colNr = violation.getReportObject().getPosition().getStartColumn() - strippedCount;
                        if (markers.length() < colNr) { // Ensure enough spaces are present.
                            markers.append(Strings.duplicate(" ", colNr - markers.length()));
                        }
                        markers.replace(colNr - 1, colNr, "^"); // Add the marker.
                    }
                    lines.add("       " + markers);
                }
            }
        }

        return lines;
    }
}
