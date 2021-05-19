package org.silentsoft.maven.plugins.acmicpc;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Mojo(name = "create", requiresProject = false)
public class CreateMojo extends AbstractMojo {

    private static final String PROBLEM = "problem";
    private static final String TEMPLATE = "template";
    private static final String PROBLEMS_DIRECTORY = "problemsDirectory";
    private static final String TEMPLATES_DIRECTORY = "templatesDirectory";
    private static final String POM_FILE = "pomFile";
    private static final String SKIP = "skip";

    @Parameter(property = PROBLEM, required = true)
    private String problem;

    @Parameter(property = TEMPLATE, defaultValue = "java", required = true)
    private String template;

    @Parameter(property = PROBLEMS_DIRECTORY, defaultValue = "${project.basedir}/problems", required = true)
    private File problemsDirectory;

    @Parameter(property = TEMPLATES_DIRECTORY, defaultValue = "${project.basedir}/templates", required = true)
    private File templatesDirectory;

    @Parameter(property = POM_FILE, defaultValue = "${project.basedir}/pom.xml", required = true)
    private File pomFile;

    @Parameter(property = SKIP, defaultValue = "false")
    private boolean skip;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            return;
        }

        ParameterUtils.checkParameter(PROBLEM, getProblem());
        ParameterUtils.checkParameter(TEMPLATE, getTemplate());
        ParameterUtils.checkParameter(PROBLEMS_DIRECTORY, getProblemsDirectory().getPath());
        ParameterUtils.checkParameter(TEMPLATES_DIRECTORY, getTemplatesDirectory().getPath());
        ParameterUtils.checkParameter(POM_FILE, getPomFile().getPath());

        createProblem();
    }

    private void createProblem() throws MojoExecutionException, MojoFailureException {
        checkProblemDirectory();
        checkTemplateDirectory();

        try {
            IOUtils.copy(getTemplateDirectory(), getProblemDirectory());

            Path problemPomFilePath = getProblemDirectory().resolve("pom.xml");
            if (Files.exists(problemPomFilePath)) {
                MavenUtils.addModule(getPomFile(), problemPomFilePath.toFile());
            }
        } catch (Throwable e) {
            throw new MojoExecutionException("", e);
        }
    }

    private void checkProblemDirectory() throws MojoFailureException {
        Path problemDirectory = getProblemDirectory();
        if (Files.exists(problemDirectory)) {
            throw new MojoFailureException(String.format("Could not create the problem project due to already exists. (%s)", problemDirectory));
        }
    }

    private void checkTemplateDirectory() throws MojoFailureException {
        Path userDefinedTemplateDirectory = getUserDefinedTemplateDirectory();
        if (Files.notExists(userDefinedTemplateDirectory)) {
            InputStream defaultTemplate = getClass().getResourceAsStream("/templates/".concat(getTemplate()));
            if (defaultTemplate == null) {
                throw new MojoFailureException(String.format("Could not create the problem project due to '%s' template is not provided. Please check template directory exists. (%s)", getTemplate(), userDefinedTemplateDirectory));
            }
            IOUtils.closeQuietly(defaultTemplate);
        }
    }

    private String getProblem() {
        return problem;
    }

    private String getTemplate() {
        return template;
    }

    private File getProblemsDirectory() {
        return problemsDirectory;
    }

    private File getTemplatesDirectory() {
        return templatesDirectory;
    }

    private File getPomFile() {
        return pomFile;
    }

    private Path getProblemDirectory() {
        return getProblemsDirectory().toPath().resolve(getProblem());
    }

    private Path getTemplateDirectory() throws URISyntaxException {
        Path userDefinedTemplateDirectory = getUserDefinedTemplateDirectory();
        if (Files.exists(userDefinedTemplateDirectory)) {
            getLog().info("Template has been override.");
            return userDefinedTemplateDirectory;
        }

        getLog().info("Default template will be used.");
        return getDefaultTemplateDirectory();
    }

    private Path getUserDefinedTemplateDirectory() {
        return getTemplatesDirectory().toPath().resolve(getTemplate());
    }

    private Path getDefaultTemplateDirectory() throws URISyntaxException {
        return Paths.get(normalize(getClass().getResource("/templates").toURI())).resolve(getTemplate());
    }

    private URI normalize(URI uri) {
        if ("jar".equalsIgnoreCase(uri.getScheme())) {
            return URI.create(uri.getRawSchemeSpecificPart());
        }

        return uri;
    }

}
