package org.silentsoft.maven.plugins.acmicpc;

import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class MavenUtils {

    public static void addModule(File parentPomFile, File childPomFile) throws IOException, XmlPullParserException {
        if (parentPomFile != null && childPomFile != null) {
            Path parentPomFilePath = parentPomFile.toPath();
            Path childPomFilePath = childPomFile.toPath();
            if (Files.exists(parentPomFilePath) && Files.exists(childPomFilePath)) {
                Model parentModel = read(parentPomFile);
                {
                    List<String> modules = parentModel.getModules();
                    modules.add(parentPomFilePath.getParent().relativize(childPomFilePath.getParent()).toString());
                    Collections.sort(modules);

                    parentModel.setModules(modules);
                }
                parentModel.setPackaging("pom");

                Model childModel = read(childPomFile);
                {
                    Parent parent = new Parent();
                    parent.setGroupId(parentModel.getGroupId());
                    parent.setArtifactId(parentModel.getArtifactId());
                    parent.setVersion(parentModel.getVersion());
                    parent.setRelativePath(childPomFilePath.getParent().relativize(parentPomFilePath).toString());

                    childModel.setParent(parent);
                }
                childModel.setArtifactId(childPomFilePath.getParent().getFileName().toString());

                write(parentPomFile, parentModel);
                write(childPomFile, childModel);
            }
        }
    }

    public static void removeModules(File pomFile) throws IOException, XmlPullParserException {
        Model model = read(pomFile);
        model.setModules(Collections.emptyList());

        write(pomFile, model);
    }

    public static Model read(File pomFile) throws IOException, XmlPullParserException {
        MavenXpp3Reader reader = new MavenXpp3Reader();

        InputStream inputStream = Files.newInputStream(pomFile.toPath());;
        Model model = reader.read(inputStream/*, false*/);

        IOUtils.closeQuietly(inputStream);

        return model;
    }

    public static void write(File pomFile, Model model) throws IOException {
        MavenXpp3Writer writer = new MavenXpp3Writer();

        OutputStream outputStream = Files.newOutputStream(pomFile.toPath());
        writer.write(outputStream, model);

        IOUtils.closeQuietly(outputStream);
    }

}
