//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.output.plotviz;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.imageio.ImageIO;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.escet.cif.simulator.runtime.meta.RuntimeStateObjectMeta;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.eclipse.ui.ControlEditor;
import org.eclipse.escet.common.eclipse.ui.MsgBox;
import org.eclipse.escet.common.eclipse.ui.SelectionListenerBase;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Exceptions;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.PlatformUI;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.MatlabTheme;
import org.knowm.xchart.style.Styler.LegendLayout;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.Styler.TextAlignment;
import org.knowm.xchart.style.XYStyler;
import org.knowm.xchart.style.colors.ChartColor;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.SeriesMarkers;

/** Visualizer to use to graphically plot the values of variables as time progresses, during simulation. */
public class PlotVisualizer extends ControlEditor {
    /** The chart, or {@code null} if not available. */
    private XYChart chart;

    /** The canvas, or {@code null} if not available. */
    private PlotVisualizerCanvas canvas;

    /**
     * The data series for the variables. Is {@code null} until initialized by the {@link #initVarDatas} method.
     *
     * @see PlotVisualizerData#varDatas
     */
    public List<PlotVisualizerDataSeries> varDatas;

    /** The plot x-axis range. Is {@code null} if not available. */
    public PlotVisualizerRange rangeX;

    /** The plot visualizer update thread. {@code null} until initialized by the {@link #createContents} method. */
    private PlotVisualizerUpdateThread thread;

    @SuppressWarnings("restriction")
    @Override
    protected Control createContents(Composite parent) {
        // Create chart.
        chart = new XYChartBuilder().build();

        // Configure chart.
        chart.setXAxisTitle("time");

        // Apply Matlab theme.
        XYStyler styler = chart.getStyler();
        styler.setTheme(new MatlabTheme());

        // Custom styling.
        styler.setAntiAlias(true);
        styler.setChartPadding(15);
        styler.setLegendBorderColor(ChartColor.getAWTColor(ChartColor.DARK_GREY));
        styler.setLegendFont(chart.getStyler().getAxisTitleFont());
        styler.setLegendLayout(LegendLayout.Horizontal);
        styler.setLegendPadding(7);
        styler.setLegendPosition(LegendPosition.OutsideS);
        styler.setPlotContentSize(1.0);
        styler.setPlotGridLinesColor(new Color(245, 245, 245));
        styler.setPlotGridLinesStroke(new BasicStroke());
        styler.setPlotBackgroundColor(new Color(252, 252, 252));
        styler.setSeriesMarkers(new Marker[] {SeriesMarkers.NONE});
        styler.setSeriesLines(new BasicStroke[] {SeriesLines.SOLID});
        styler.setSeriesColors(MaterialUiColors600.COLORS);
        styler.setXAxisMin(0.0);
        styler.setYAxisLabelAlignment(TextAlignment.Centre);

        // Create canvas on which to display the chart.
        canvas = new PlotVisualizerCanvas(parent, chart);

        // Let the chart fill the entire Eclipse editor contents.
        FormData tableData = new FormData();
        tableData.left = new FormAttachment(0, 0);
        tableData.right = new FormAttachment(100, 0);
        tableData.top = new FormAttachment(0, 0);
        tableData.bottom = new FormAttachment(100, 0);
        canvas.setLayoutData(tableData);

        // Set up popup menu.
        Menu popupMenu = new Menu(parent);
        MenuItem saveItem = new MenuItem(popupMenu, SWT.NONE);
        saveItem.setEnabled(isSaveAsAllowed());
        saveItem.setText("Save as...");
        canvas.setMenu(popupMenu);

        popupMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuShown(MenuEvent e) {
                saveItem.setEnabled(isSaveAsAllowed());
            }

            @Override
            public void menuHidden(MenuEvent e) {
                // Nothing to do here.
            }
        });

        saveItem.addSelectionListener(new SelectionListenerBase() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                doSaveAs();
            }
        });

        // Start update thread.
        if (thread == null) {
            thread = new PlotVisualizerUpdateThread(chart, canvas);
            thread.start();
        }

        // Return the canvas as contents of the visualizer.
        return canvas;
    }

    /**
     * Notifies the chart that updates have finished on {@link #varDatas} and {@link #rangeX}, and a redraw is needed.
     */
    public void postUpdate() {
        // Construct update data.
        List<PlotVisualizerUpdateDataEntry> updateEntries = listc(varDatas.size());
        for (PlotVisualizerDataSeries varData: varDatas) {
            double[] xValues = varData.getXValuesArray();
            double[] yValues = varData.getYValuesArray();
            updateEntries.add(new PlotVisualizerUpdateDataEntry(varData.meta.name, xValues, yValues));
        }
        PlotVisualizerUpdateData updateData = new PlotVisualizerUpdateData(updateEntries);

        // Inform update thread of the new data, and that a redraw is needed.
        synchronized (thread.updateRequested) {
            thread.updateData.set(updateData);
            thread.updateRangeX.set(rangeX);
            thread.updateRequested.set(true);
            thread.updateRequested.notify();
        }
    }

    /**
     * Initializes the data series for the given variables.
     *
     * @param metas The meta data of the variables.
     * @see PlotVisualizerData#initVarDatas
     */
    public void initVarDatas(List<RuntimeStateObjectMeta> metas) {
        varDatas = listc(metas.size());
        for (RuntimeStateObjectMeta meta: metas) {
            varDatas.add(new PlotVisualizerDataSeries(meta));
        }
    }

    @Override
    public boolean isSaveAsAllowed() {
        return true;
    }

    @Override
    public void doSaveAs() {
        // Check for enough data available. Don't do it in 'isSaveAsAllowed',
        // at it is not rechecked whenever data is added, but only on the
        // visualizer getting focus, etc.
        if (varDatas == null || varDatas.isEmpty()) {
            MsgBox.show(contents.getShell(), SWT.ICON_ERROR | SWT.OK, "Save plot as", "No data available.");
            return;
        }
        if (first(varDatas).points.size() < 2) {
            MsgBox.show(contents.getShell(), SWT.ICON_ERROR | SWT.OK, "Save plot as", "Not enough data available.");
            return;
        }

        // Get directory that contains the CIF file being simulated.
        String path = input.getAbsoluteFilePath();
        int idx = Math.max(path.lastIndexOf('\\'), path.lastIndexOf('/'));
        Assert.check(idx > 0);
        path = path.substring(0, idx);

        // Determine suggested file name.
        String fileName = Paths.getFileName(input.getAbsoluteFilePath());
        fileName = Paths.pathChangeExtension(fileName, "cif", "plot.png");

        // Set up the file dialog.
        FileDialog fd = new FileDialog(contents.getShell(), SWT.SAVE);
        fd.setText("Save plot as");
        fd.setFilterPath(path);
        fd.setFilterExtensions(new String[] {"*.png;*.jpg;*.gif"});
        fd.setFileName(fileName);

        // Get save path using dialog.
        String savePath;
        File saveFile;
        while (true) {
            // Show dialog.
            savePath = fd.open();

            // Check cancellation.
            if (savePath == null || savePath.trim().length() == 0) {
                return;
            }

            // Check overwrite.
            saveFile = new File(savePath);
            Assert.check(saveFile.isAbsolute());
            if (!saveFile.exists()) {
                // Does not yet exist.
                break;
            } else {
                // Ask to overwrite.
                String question = fmt("The file \"%s\" already exists. Do you want to replace the existing file?",
                        savePath);
                int rslt = MsgBox.show(contents.getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO, "Save plot as",
                        question);
                if (rslt == SWT.YES) {
                    break;
                }
            }
        }

        // Check valid file extension.
        if (!savePath.endsWith(".png") && !savePath.endsWith(".jpg") && !savePath.endsWith(".gif")) {
            MsgBox.show(contents.getShell(), SWT.ICON_ERROR | SWT.OK, "Save plot as",
                    "Invalid file extension: use .png, .jpg, or .gif.");
            return;
        }

        // Get image format.
        String imgFormat = Strings.slice(savePath, -3, null);

        // Get size validator.
        IInputValidator sizeValidator = new IInputValidator() {
            @Override
            public String isValid(String newText) {
                // Check 'x'.
                int idx = newText.indexOf("x");
                if (idx == -1) {
                    return "Missing \"x\" between width and height.";
                }

                // Check width.
                String widthTxt = newText.substring(0, idx);
                try {
                    int i = Integer.parseInt(widthTxt);
                    if (i <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    return fmt("Invalid width \"%s\".", widthTxt);
                }

                // Check height.
                String heightTxt = newText.substring(idx + 1);
                try {
                    int i = Integer.parseInt(heightTxt);
                    if (i <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    return fmt("Invalid height \"%s\".", heightTxt);
                }

                // All OK.
                return null;
            }
        };

        // Get image size using input dialog.
        Point lastSize = canvas.getLastSize();
        int width = (lastSize == null) ? 1000 : lastSize.x;
        int height = (lastSize == null) ? 800 : lastSize.y;
        String sizeTxt = str(width) + "x" + str(height);

        InputDialog id = new InputDialog(contents.getShell(), "Save plot as", "Specify size of image, e.g. 640x480:",
                sizeTxt, sizeValidator);
        int idRslt = id.open();

        // Check cancellation.
        if (idRslt == Window.CANCEL) {
            return;
        }

        // Get size.
        sizeTxt = id.getValue();
        idx = sizeTxt.indexOf("x");
        width = Integer.parseInt(sizeTxt.substring(0, idx));
        height = Integer.parseInt(sizeTxt.substring(idx + 1));

        // Save as raster image.
        try {
            // Render image.
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D graphics = image.createGraphics();
            canvas.paint(graphics, width, height);
            graphics.dispose();

            // Write image to file.
            boolean rslt = ImageIO.write(image, imgFormat, saveFile);
            if (!rslt) {
                String msg = fmt("Failed to save image to \"%s\": unknown or unsupported file extension.", savePath);
                throw new IOException(msg);
            }
        } catch (NullPointerException e) {
            // Access denied (etc?) leads to NullPointerException
            // at javax.imageio.ImageIO.write(ImageIO.java:1523), due to
            // attempting to close a 'null' stream.
            MsgBox.show(contents.getShell(), SWT.ICON_ERROR | SWT.OK, "Save plot as", "Failed to save image.");
            return;
        } catch (IOException e) {
            MsgBox.show(contents.getShell(), SWT.ICON_ERROR | SWT.OK, "Save plot as",
                    "Failed to save image." + Strings.NL + Strings.NL + Exceptions.exToStr(e));
            return;
        }

        // Refresh workspace, for the parent directory of the saved image file.
        // If the directory is not in the workspace, nothing is refreshed.
        if (Platform.isRunning() && PlatformUI.isWorkbenchRunning()) {
            URI saveDirUri = saveFile.getParentFile().toURI();
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IContainer[] dirs = root.findContainersForLocationURI(saveDirUri);
            for (IContainer dir: dirs) {
                try {
                    dir.refreshLocal(IResource.DEPTH_INFINITE, null);
                } catch (CoreException e) {
                    // Ignore refresh failures.
                }
            }
        }
    }
}
