package org.mmek.craps.crapsc;

import java.io.*;
import org.jcb.craps.crapsc.java.*;

public class SourceContext {
    private File sourceFile;
    private File outputFile;
    private boolean sourceModified;
    private boolean highlightsExists;
    private ObjModule symbolTable;
    private ObjModule objModule;

    public SourceContext() {
        sourceFile = null;
        outputFile = null;
        sourceModified = false;
        highlightsExists = false;
        symbolTable = new ObjModule();
        objModule = new ObjModule();
    }

    public SourceContext(File sourceFile, File outputFile) {
        this.sourceFile = sourceFile;
        this.outputFile = outputFile;
        sourceModified = false;
        highlightsExists = false;
        symbolTable = new ObjModule();
        objModule = new ObjModule();
    }

    public String toString() {
        if (sourceFile == null) return "null";
        return sourceFile.toString();
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public String getTitle() {
        if (sourceFile == null) return "untitled";
        return sourceFile.getName();
    }

    public boolean isSourceModified() {
        return sourceModified;
    }

    public void setSourceModified(boolean sourceModified) {
        this.sourceModified = sourceModified;
    }

    public boolean existHighlights() {
        return highlightsExists;
    }

    public void setHighlightsExist(boolean highlightsExists) {
        this.highlightsExists = highlightsExists;
    }

    public ObjModule getObjModule() {
        return symbolTable;
    }

    public void save() throws IOException {
        symbolTable.save(outputFile);
    }
}
