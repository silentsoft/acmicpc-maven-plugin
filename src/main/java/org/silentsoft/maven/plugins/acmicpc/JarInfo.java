package org.silentsoft.maven.plugins.acmicpc;

public class JarInfo {

    private String filePath;

    private String rootPath;

    private JarInfo() {}

    public JarInfo(String filePath, String rootPath) {
        this.filePath = filePath;
        this.rootPath = rootPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getRootPath() {
        return rootPath;
    }
}
