package org.silentsoft.maven.plugins.acmicpc;

import org.apache.maven.plugin.MojoFailureException;

public class ParameterUtils {

    public static void checkParameter(String name, String value) throws MojoFailureException {
        if (isImplicitlyProvidedParameter(value)) {
            throw new MojoFailureException(String.format("'%s' must be provided with value.", name));
        }
    }

    public static boolean isImplicitlyProvidedParameter(String parameter) {
        return Boolean.TRUE.toString().equals(parameter);
    }

}
