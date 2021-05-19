package org.silentsoft.maven.plugins.acmicpc;

import junit.framework.Assert;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RevalidateMojoTest extends AbstractMojoTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void test() throws Exception {
        AbstractMojo revalidateMojo = (AbstractMojo) lookupMojo("revalidate", new File("target/test-classes/revalidate-before-test-pom.xml"));
        Assert.assertNotNull(revalidateMojo);
        revalidateMojo.execute();

        File problemsDirectory = (File) getVariableValueFromObject(revalidateMojo, "problemsDirectory");
        Validator.contentEquals(Paths.get("src/test/resources/revalidate-expected-problems"), problemsDirectory.toPath());

        byte[] expectedPom = Files.readAllBytes(Paths.get("src/test/resources/revalidate-expected-test-pom.xml"));
        byte[] actualPom = Files.readAllBytes(Paths.get("target/test-classes/revalidate-before-test-pom.xml"));

        Assert.assertEquals(new String(expectedPom), new String(actualPom));

    }

}
