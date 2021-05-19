package org.silentsoft.maven.plugins.acmicpc;

import junit.framework.Assert;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class CreateMojoTest extends AbstractMojoTestCase {

    protected void setUp() throws Exception {
        super.setUp();

        Path testSquarePath = Paths.get("target/square");
        if (Files.exists(testSquarePath)) {
            System.out.println(String.format("Deleting test square (%s) before test.", testSquarePath));
            Files.walkFileTree(testSquarePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return super.visitFile(file, attrs);
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return super.postVisitDirectory(dir, exc);
                }
            });
        }
    }

    public void testDefaultTemplate() throws Exception {
        AbstractMojo createMojo = (AbstractMojo) lookupMojo("create", new File("target/test-classes/create-default-template-test-pom.xml"));
        Assert.assertNotNull(createMojo);
        createMojo.execute();

        File problemsDirectory = (File) getVariableValueFromObject(createMojo, "problemsDirectory");
        String problem = (String) getVariableValueFromObject(createMojo, "problem");

        Assert.assertTrue(Validator.contentEquals(Paths.get("src/test/resources/create-default-template-expected-problems/1234"), problemsDirectory.toPath().resolve(problem)));
    }

    public void testOverrideTemplate() throws Exception {
        AbstractMojo createMojo = (AbstractMojo) lookupMojo("create", new File("target/test-classes/create-override-template-test-pom.xml"));
        Assert.assertNotNull(createMojo);
        createMojo.execute();

        File problemsDirectory = (File) getVariableValueFromObject(createMojo, "problemsDirectory");
        String problem = (String) getVariableValueFromObject(createMojo, "problem");

        Assert.assertTrue(Validator.contentEquals(Paths.get("src/test/resources/create-override-template-expected-problems/1234"), problemsDirectory.toPath().resolve(problem)));
    }

    public void testNotExistsTemplate() throws Exception {
        assertExpectedException(MojoFailureException.class, () -> {
            AbstractMojo createMojo = (AbstractMojo) lookupMojo("create", new File("target/test-classes/create-not-exists-template-test-pom.xml"));
            Assert.assertNotNull(createMojo);
            createMojo.execute();
        });
    }

    public void testAlreadyExistsProblem() throws Exception {
        assertExpectedException(MojoFailureException.class, () -> {
            AbstractMojo createMojo = (AbstractMojo) lookupMojo("create", new File("target/test-classes/create-already-exists-problem-test-pom.xml"));
            Assert.assertNotNull(createMojo);
            createMojo.execute();
            createMojo.execute();
        });
    }

    public void testSkipParameter() throws Exception {
        AbstractMojo createMojo = (AbstractMojo) lookupMojo("create", new File("target/test-classes/create-skip-test-pom.xml"));
        Assert.assertNotNull(createMojo);
        createMojo.execute();

        File problemsDirectory = (File) getVariableValueFromObject(createMojo, "problemsDirectory");
        String problem = (String) getVariableValueFromObject(createMojo, "problem");
        Assert.assertTrue(Files.notExists(problemsDirectory.toPath().resolve(problem)));
    }

    public void testImplicitlyProvidedParameter() throws Exception {
        assertExpectedException(MojoFailureException.class, () -> {
            AbstractMojo createMojo = (AbstractMojo) lookupMojo("create", new File("target/test-classes/create-implicitly-provided-parameter-test-pom.xml"));
            Assert.assertNotNull(createMojo);
            createMojo.execute();
        });
    }

    private void assertExpectedException(Class<? extends Throwable> expectedException, Scope scope) {
        boolean expected = false;
        Throwable actualException = null;
        try {
            scope.run();
        } catch (Throwable e) {
            if (e.getClass() == expectedException) {
                expected = true;
            } else {
                actualException = e;
            }
        }
        if (expected == false) {
            Assert.fail(actualException == null ? "" : actualException.getMessage());
        }
    }

}
