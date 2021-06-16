package org.eclipse.escet.cif.controllercheck;

import org.eclipse.core.resources.IFile;

import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.eclipse.ui.SingleFileCommandHandler;

/** Controller properties check command handler. */
public class ControllerCheckCommandHandler extends SingleFileCommandHandler {
    @Override
    protected String[] getCommandLineArgs(IFile file) {
        return new String[] {getFileName(file), "--option-dialog=yes"};
    }

    @Override
    protected Class<? extends Application<?>> getApplicationClass() {
        return ControllerCheckApp.class;
    }
}
