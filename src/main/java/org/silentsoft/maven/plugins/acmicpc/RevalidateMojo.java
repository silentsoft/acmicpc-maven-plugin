package org.silentsoft.maven.plugins.acmicpc;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

@Mojo(name = "revalidate", requiresProject = false)
public class RevalidateMojo extends AbstractMojo {

    private static final String PROBLEMS_DIRECTORY = "problemsDirectory";
    private static final String MAX_DEPTH = "maxDepth";
    private static final String POM_FILE = "pomFile";
    private static final String SKIP = "skip";

    @Parameter(property = PROBLEMS_DIRECTORY, defaultValue = "${project.basedir}/problems", required = true)
    private File problemsDirectory;

    @Parameter(property = MAX_DEPTH, defaultValue = "2147483647", required = true)
    private int maxDepth;

    @Parameter(property = POM_FILE, defaultValue = "${project.basedir}/pom.xml", required = true)
    private File pomFile;

    @Parameter(property = SKIP, defaultValue = "false")
    private boolean skip;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            return;
        }

        ParameterUtils.checkParameter(PROBLEMS_DIRECTORY, getProblemsDirectory().getPath());
        ParameterUtils.checkParameter(MAX_DEPTH, String.valueOf(getMaxDepth()));
        ParameterUtils.checkParameter(POM_FILE, getPomFile().getPath());

        revalidate();
    }

    private void revalidate() throws MojoExecutionException {
        try {
            MavenUtils.removeModules(getPomFile());

            Path problemsDirectoryPath = getProblemsDirectory().toPath();
            if (Files.exists(problemsDirectoryPath)) {
                Files.walkFileTree(problemsDirectoryPath, EnumSet.noneOf(FileVisitOption.class), getMaxDepth() < 2 ? 2 : getMaxDepth(), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        if (dir.equals(problemsDirectoryPath)) {
                            return super.preVisitDirectory(dir, attrs);
                        }

                        try {
                            Path problemPomFilePath = dir.resolve("pom.xml");
                            if (Files.exists(problemPomFilePath)) {
                                MavenUtils.addModule(getPomFile(), problemPomFilePath.toFile());
                            }
                        } catch (Throwable e) {
                            throw new IOException(e);
                        }

                        return super.preVisitDirectory(dir, attrs);
                    }
                });
            } else {
                getLog().info("The problems directory is not exists.");
            }
        } catch (Throwable e) {
            throw new MojoExecutionException("", e);
        }
    }

    private File getProblemsDirectory() {
        return problemsDirectory;
    }

    private int getMaxDepth() {
        return maxDepth;
    }

    private File getPomFile() {
        return pomFile;
    }

}
