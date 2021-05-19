package org.silentsoft.maven.plugins.acmicpc;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class IOUtils {

    public static void copy(Path source, Path target) throws IOException {
        if (JarUtils.isInsideOfJar(source)) {
            JarInfo jarInfo = JarUtils.getJarInfo(source);
            try (JarFile jarFile = new JarFile(jarInfo.getFilePath())) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (String.valueOf(Paths.get(entry.getName())).startsWith(String.valueOf(Paths.get(jarInfo.getRootPath())))) {
                        if (entry.isDirectory()) {
                            Files.createDirectories(target.resolve(Paths.get(jarInfo.getRootPath()).relativize(Paths.get(entry.getName()))));
                        } else {
                            InputStream inputStream = jarFile.getInputStream(entry);
                            Files.copy(inputStream, target.resolve(Paths.get(jarInfo.getRootPath()).relativize(Paths.get(entry.getName()))));
                            IOUtils.closeQuietly(inputStream);
                        }
                    }
                }
            }
        } else {
            Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Files.createDirectories(target.resolve(source.relativize(dir)));
                    return super.preVisitDirectory(dir, attrs);
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.copy(file, target.resolve(source.relativize(file)));
                    return super.visitFile(file, attrs);
                }
            });
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {

            }
        }
    }

}
