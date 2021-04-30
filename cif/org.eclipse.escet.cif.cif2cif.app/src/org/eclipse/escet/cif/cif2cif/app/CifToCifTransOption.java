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

package org.eclipse.escet.cif.cif2cif.app;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.sortedstrings;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.cif2cif.AddDefaultInitialValues;
import org.eclipse.escet.cif.cif2cif.CifToCifTransformation;
import org.eclipse.escet.cif.cif2cif.ElimAlgVariables;
import org.eclipse.escet.cif.cif2cif.ElimAutCasts;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimConsts;
import org.eclipse.escet.cif.cif2cif.ElimEnums;
import org.eclipse.escet.cif.cif2cif.ElimEquations;
import org.eclipse.escet.cif.cif2cif.ElimGroups;
import org.eclipse.escet.cif.cif2cif.ElimIfUpdates;
import org.eclipse.escet.cif.cif2cif.ElimLocRefExprs;
import org.eclipse.escet.cif.cif2cif.ElimMonitors;
import org.eclipse.escet.cif.cif2cif.ElimSelf;
import org.eclipse.escet.cif.cif2cif.ElimStateEvtExclInvs;
import org.eclipse.escet.cif.cif2cif.ElimTauEvent;
import org.eclipse.escet.cif.cif2cif.ElimTupleFieldProjs;
import org.eclipse.escet.cif.cif2cif.ElimTypeDecls;
import org.eclipse.escet.cif.cif2cif.LiftEvents;
import org.eclipse.escet.cif.cif2cif.LinearizeMerge;
import org.eclipse.escet.cif.cif2cif.LinearizeProduct;
import org.eclipse.escet.cif.cif2cif.MergeEnums;
import org.eclipse.escet.cif.cif2cif.PrintFileIntoDecls;
import org.eclipse.escet.cif.cif2cif.RemoveCifSvgDecls;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.cif2cif.RemovePositionInfo;
import org.eclipse.escet.cif.cif2cif.RemovePrintDecls;
import org.eclipse.escet.cif.cif2cif.RemoveRequirements;
import org.eclipse.escet.cif.cif2cif.SimplifyOthers;
import org.eclipse.escet.cif.cif2cif.SimplifyValues;
import org.eclipse.escet.cif.cif2cif.SimplifyValuesNoRefs;
import org.eclipse.escet.cif.cif2cif.SimplifyValuesNoRefsOptimized;
import org.eclipse.escet.cif.cif2cif.SimplifyValuesOptimized;
import org.eclipse.escet.cif.cif2cif.SvgFileIntoDecls;
import org.eclipse.escet.cif.cif2cif.SwitchesToIfs;
import org.eclipse.escet.common.app.framework.exceptions.InvalidOptionException;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionGroup;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/** CIF to CIF transformations option. Indicates which transformations to apply. */
public class CifToCifTransOption extends Option<String> {
    /**
     * Constructor for the {@link CifToCifTransOption} class. Don't directly create instances of this class. Use the
     * {@link Options#getInstance} method instead.
     */
    public CifToCifTransOption() {
        super("Transformations",
                "Specifies the CIF to CIF transformations to apply. TRANS consists of comma separated transformation "
                        + "names, in the order they should be applied. If no transformations are applied, the input "
                        + "is only validated and pretty printed. See documentation for more information. "
                        + "[DEFAULT=\"\"].",
                't', "transformations", "TRANS", true);
    }

    /** Mapping from CIF to CIF transformation names to the class that implements the transformation. */
    public static final Map<String, Class<? extends CifToCifTransformation>> TRANSFORMATIONS;

    static {
        TRANSFORMATIONS = map();

        TRANSFORMATIONS.put("add-default-init-values", AddDefaultInitialValues.class);
        TRANSFORMATIONS.put("elim-alg-vars", ElimAlgVariables.class);
        TRANSFORMATIONS.put("elim-aut-casts", ElimAutCasts.class);
        TRANSFORMATIONS.put("elim-comp-def-inst", ElimComponentDefInst.class);
        TRANSFORMATIONS.put("elim-consts", ElimConsts.class);
        TRANSFORMATIONS.put("elim-enums", ElimEnums.class);
        TRANSFORMATIONS.put("elim-equations", ElimEquations.class);
        TRANSFORMATIONS.put("elim-groups", ElimGroups.class);
        TRANSFORMATIONS.put("elim-if-updates", ElimIfUpdates.class);
        TRANSFORMATIONS.put("elim-locs-in-exprs", ElimLocRefExprs.class);
        TRANSFORMATIONS.put("elim-monitors", ElimMonitors.class);
        TRANSFORMATIONS.put("elim-self", ElimSelf.class);
        TRANSFORMATIONS.put("elim-state-evt-excl-invs", ElimStateEvtExclInvs.class);
        TRANSFORMATIONS.put("elim-tau-event", ElimTauEvent.class);
        TRANSFORMATIONS.put("elim-tuple-field-projs", ElimTupleFieldProjs.class);
        TRANSFORMATIONS.put("elim-type-decls", ElimTypeDecls.class);
        TRANSFORMATIONS.put("lift-events", LiftEvents.class);
        TRANSFORMATIONS.put("linearize-merge", LinearizeMerge.class);
        TRANSFORMATIONS.put("linearize-product", LinearizeProduct.class);
        TRANSFORMATIONS.put("merge-enums", MergeEnums.class);
        TRANSFORMATIONS.put("print-file-into-decls", PrintFileIntoDecls.class);
        TRANSFORMATIONS.put("remove-cif-svg-decls", RemoveCifSvgDecls.class);
        TRANSFORMATIONS.put("remove-io-decls", RemoveIoDecls.class);
        TRANSFORMATIONS.put("remove-print-decls", RemovePrintDecls.class);
        TRANSFORMATIONS.put("remove-pos-info", RemovePositionInfo.class);
        TRANSFORMATIONS.put("remove-reqs", RemoveRequirements.class);
        TRANSFORMATIONS.put("simplify-others", SimplifyOthers.class);
        TRANSFORMATIONS.put("simplify-values", SimplifyValues.class);
        TRANSFORMATIONS.put("simplify-values-optimized", SimplifyValuesOptimized.class);
        TRANSFORMATIONS.put("simplify-values-no-refs", SimplifyValuesNoRefs.class);
        TRANSFORMATIONS.put("simplify-values-no-refs-optimized", SimplifyValuesNoRefsOptimized.class);
        TRANSFORMATIONS.put("svg-file-into-decls", SvgFileIntoDecls.class);
        TRANSFORMATIONS.put("switches-to-ifs", SwitchesToIfs.class);
    }

    @Override
    public String getDefault() {
        return "";
    }

    @Override
    public String parseValue(String optName, String value) {
        // Check for valid value.
        splitValue(value);

        return value;
    }

    /**
     * Splits the command line argument ({@code value}) into parts, one for each transformation name.
     *
     * @param value The command line argument to split.
     * @return The individual transformation names.
     * @throws InvalidOptionException If the given command line argument is invalid.
     */
    protected static List<String> splitValue(String value) {
        String[] parts = value.split(",");
        List<String> rslt = list();
        for (String part: parts) {
            part = part.trim();
            if (part.isEmpty()) {
                continue;
            }

            String msg = fmt("Unknown transformation \"%s\".", part);
            checkValue(TRANSFORMATIONS.containsKey(part), msg);
            rslt.add(part);
        }
        return rslt;
    }

    /**
     * Returns the transformations to apply.
     *
     * @return The transformations to apply.
     */
    public static List<CifToCifTransformation> getTransformations() {
        String value = Options.get(CifToCifTransOption.class);
        List<String> transNames = splitValue(value);
        List<CifToCifTransformation> rslt = list();
        for (String transName: transNames) {
            // Get transformation class.
            Class<? extends CifToCifTransformation> transClass;
            transClass = TRANSFORMATIONS.get(transName);

            // Instantiate transformation.
            CifToCifTransformation trans;
            try {
                trans = transClass.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                String msg = "Failed to instantiate CIF to CIF trans: " + transClass;
                throw new RuntimeException(msg, e);
            } catch (IllegalArgumentException e) {
                String msg = "Failed to instantiate CIF to CIF trans: " + transClass;
                throw new RuntimeException(msg, e);
            } catch (SecurityException e) {
                String msg = "Failed to instantiate CIF to CIF trans: " + transClass;
                throw new RuntimeException(msg, e);
            }

            // Add transformation instance to result.
            rslt.add(trans);
        }
        return rslt;
    }

    @Override
    public String[] getCmdLine(Object value) {
        return new String[] {"--transformations=" + (String)value};
    }

    @Override
    public OptionGroup<String> createOptionGroup(Composite page) {
        return new OptionGroup<>(page, this) {
            org.eclipse.swt.widgets.List availableList;

            org.eclipse.swt.widgets.List chosenList;

            Label availableLabel;

            Label chosenLabel;

            Button addButton;

            Button removeButton;

            @Override
            protected void addComponents(Group group) {
                // Add button.
                addButton = new Button(group, SWT.PUSH);
                addButton.setText("Add");
                FormData addData = new FormData();
                addData.bottom = new FormAttachment(100);
                addData.left = new FormAttachment(0);
                addButton.setLayoutData(addData);
                addButton.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        for (int idx: availableList.getSelectionIndices()) {
                            chosenList.add(availableList.getItem(idx));
                        }
                    }
                });

                // Remove button.
                removeButton = new Button(group, SWT.PUSH);
                removeButton.setText("Remove");
                FormData removeData = new FormData();
                removeData.bottom = new FormAttachment(100);
                removeData.right = new FormAttachment(100);
                removeButton.setLayoutData(removeData);
                removeButton.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        chosenList.remove(chosenList.getSelectionIndices());
                    }
                });

                // Shared list style.
                int listStyle = SWT.MULTI | SWT.BORDER | SWT.V_SCROLL;

                // Available transformations label.
                availableLabel = new Label(group, SWT.NULL);
                availableLabel.setText("Available transformations:");
                FormData availableLabelData = new FormData();
                availableLabelData.top = new FormAttachment(descrLabel);
                availableLabelData.left = new FormAttachment(0);
                availableLabelData.right = new FormAttachment(50);
                availableLabel.setLayoutData(availableLabelData);

                // Chosen transformations label.
                chosenLabel = new Label(group, SWT.NULL);
                chosenLabel.setText("Chosen transformations:");
                FormData chosenLabelData = new FormData();
                chosenLabelData.top = new FormAttachment(descrLabel);
                chosenLabelData.left = new FormAttachment(availableLabel);
                chosenLabel.setLayoutData(chosenLabelData);

                // Available transformations list.
                availableList = new org.eclipse.swt.widgets.List(group, listStyle);
                FormData availableListData = new FormData();
                availableListData.top = new FormAttachment(availableLabel);
                availableListData.bottom = new FormAttachment(addButton);
                availableListData.left = new FormAttachment(0);
                availableListData.right = new FormAttachment(50);
                availableList.setLayoutData(availableListData);
                availableList.addSelectionListener(new SelectionListener() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        int cnt = availableList.getSelectionCount();
                        addButton.setEnabled(cnt > 0);
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent e) {
                        addButton.notifyListeners(SWT.Selection, null);
                    }
                });
                availableList.notifyListeners(SWT.Selection, null);

                // Chosen transformations list.
                chosenList = new org.eclipse.swt.widgets.List(group, listStyle);
                FormData chosenData = new FormData();
                chosenData.top = new FormAttachment(chosenLabel);
                chosenData.bottom = new FormAttachment(removeButton);
                chosenData.left = new FormAttachment(availableList);
                chosenData.right = new FormAttachment(100);
                chosenList.setLayoutData(chosenData);
                chosenList.addSelectionListener(new SelectionListener() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        int cnt = chosenList.getSelectionCount();
                        removeButton.setEnabled(cnt > 0);
                    }

                    @Override
                    public void widgetDefaultSelected(SelectionEvent e) {
                        removeButton.notifyListeners(SWT.Selection, null);
                    }
                });
                chosenList.notifyListeners(SWT.Selection, null);

                // Add available transformations.
                List<String> transNames = sortedstrings(TRANSFORMATIONS.keySet());
                for (String transName: transNames) {
                    availableList.add(transName);
                }
            }

            @Override
            public String getDescription() {
                return "The CIF to CIF transformations to apply. The transformations are applied in the chosen "
                        + "order. If no transformations are applied, the input is only validated and pretty printed. "
                        + "See documentation for more information.";
            }

            @Override
            public void setToValue(String value) {
                List<String> transNames = splitValue(value);
                for (String transName: transNames) {
                    chosenList.add(transName);
                }
            }

            @Override
            public String[] getCmdLine() {
                StringBuilder rslt = new StringBuilder();
                for (String transName: chosenList.getItems()) {
                    if (rslt.length() > 0) {
                        rslt.append(",");
                    }
                    rslt.append(transName);
                }
                return new String[] {"--transformations=" + rslt.toString()};
            }
        };
    }
}
