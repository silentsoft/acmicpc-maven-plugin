package org.silentsoft.maven.plugins.acmicpc;

import junit.framework.Assert;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelpMojoTest extends AbstractMojoTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void test() throws Exception {
        AbstractMojo helpMojo = (AbstractMojo) lookupMojo("help", new File("target/test-classes/help-test-pom.xml"));
        Assert.assertNotNull(helpMojo);
        helpMojo.execute();
    }

}
