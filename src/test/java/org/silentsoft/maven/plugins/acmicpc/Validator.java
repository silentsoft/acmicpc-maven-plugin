package org.silentsoft.maven.plugins.acmicpc;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

public class Validator {

    public static boolean contentEquals(Path source, Path target) throws IOException {
        if (Files.isDirectory(source) && Files.isDirectory(target)) {
            Map<String, byte[]> sourceMap = populate(source);
            Map<String, byte[]> targetMap = populate(target);

            if (sourceMap.size() != targetMap.size()) {
                return false;
            }

            int referCount = 0;
            for (Map.Entry<String, byte[]> entry : sourceMap.entrySet()) {
                if (entry.getValue() == null) {
                    if (targetMap.get(entry.getKey()) == null) {
                        referCount++;
                    } else {
                        return false;
                    }
                } else {
                    if (new String(entry.getValue()).equals(new String(targetMap.get(entry.getKey())))) {
                        referCount++;
                    } else {
                        return false;
                    }
                }
            }

            return referCount == targetMap.size();
        } else if (Files.isDirectory(source) == false && Files.isDirectory(target) == false) {
            return new String(Files.readAllBytes(source)).equals(new String(Files.readAllBytes(target)));
        }

        return false;
    }

    private static Map<String, byte[]> populate(Path path) throws IOException {
        Map<String, byte[]> map = new HashMap<String, byte[]>();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (path.equals(dir) == false) {
                    map.put(path.relativize(dir).toString(), null);
                }

                return super.preVisitDirectory(dir, attrs);
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                map.put(path.relativize(file).toString(), Files.readAllBytes(file));
                return super.visitFile(file, attrs);
            }
        });
        return map;
    }

}
