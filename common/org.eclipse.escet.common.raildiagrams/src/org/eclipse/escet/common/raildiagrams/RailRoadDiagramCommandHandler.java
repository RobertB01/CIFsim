package org.eclipse.escet.common.raildiagrams;

import java.util.List;

import org.eclipse.core.resources.IFile;

import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.eclipse.ui.MultiFileCommandHandler;

public class RailRoadDiagramCommandHandler extends MultiFileCommandHandler {
    @Override
    protected Class<? extends Application<?>> getApplicationClass() {
        return RailRoadDiagramApplication.class;
    }

    @Override
    protected String[] getCommandLineArgs(List<IFile> files, String name) {
        String workingDir = Paths.getCurWorkingDir();

        String[] args = new String[files.size() + 1];
        for (int i = 0; i < files.size(); i++) {
            IFile file = files.get(i);
            String absPath = file.getLocation().toString();
            args[i] = Paths.getRelativePath(absPath, workingDir);
        }
        args[files.size()] = "--option-dialog=yes";
        return args;
    }
}
