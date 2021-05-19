package org.silentsoft.maven.plugins.acmicpc;

import java.io.File;
import java.nio.file.Path;

public class JarUtils {

    public static boolean isInsideOfJar(Path path) {
        return getJarInfo(path).getFilePath().toLowerCase().endsWith(".jar");
    }

    public static JarInfo getJarInfo(Path path) {
        String filePath = new File(path.toUri()).getPath();

        int beginIndex = 0;
        int endIndex = (endIndex = filePath.lastIndexOf("!")) == -1 ? filePath.length() : endIndex;
        String jarPath = filePath.substring(beginIndex, endIndex);

        beginIndex = (beginIndex = filePath.lastIndexOf("!")) == -1 ? 0 : beginIndex + 2;
        endIndex = filePath.length();
        String jarRoot = filePath.substring(beginIndex, endIndex);

        return new JarInfo(jarPath, jarRoot);
    }

}
