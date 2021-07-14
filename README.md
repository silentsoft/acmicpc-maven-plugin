# Acmicpc Maven Plugin

![Maven Central](https://img.shields.io/maven-central/v/org.silentsoft.maven.plugins/acmicpc-maven-plugin)
[![Build Status](https://travis-ci.com/silentsoft/acmicpc-maven-plugin.svg?branch=main)](https://travis-ci.com/silentsoft/acmicpc-maven-plugin)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=silentsoft_acmicpc-maven-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=silentsoft_acmicpc-maven-plugin)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=silentsoft_acmicpc-maven-plugin&metric=coverage)](https://sonarcloud.io/dashboard?id=silentsoft_acmicpc-maven-plugin)
[![Hits](https://hits.sh/github.com/silentsoft/acmicpc-maven-plugin.svg)](https://hits.sh)

`Acmicpc Maven Plugin` is a simple maven plugin for creating the `problem` project using fully customizable templates.

## Related project
  - [Acmicpc Kit](https://github.com/silentsoft/acmicpc-kit)

## Maven Central
```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.silentsoft.maven.plugins</groupId>
      <artifactId>acmicpc-maven-plugin</artifactId>
      <version>1.0.0</version>
      <extensions>true</extensions>
      <configuration>
        <template>java</template>
      </configuration>
    </plugin>
  </plugins>
</build>
```

## Example usages
  - Creating a problem
    ```
    $ mvn -N acmicpc:create -Dproblem=1234 
    ```
  - Creating a problem with specific template
    ```
    $ mvn -N acmicpc:create -Dproblem=1234 -Dtemplate=specific
    ```
  - Revalidating project
    ```
    $ mvn -N acmicpc:revalidate
    ```

## Frequently Asked Questions
- Can you provide a feature to download the problem text as a file from acmicpc.net ?
  > No. This can lead to sensitive issues related to copyright. Also, web scraping isn't allowed according to the [rules](https://www.acmicpc.net/help/rule).

- Then can you provide a migration feature for downloading my source code that I've already submitted to acmicpc.net ?
  > No. According to the [rules](https://www.acmicpc.net/help/rule), web scraping isn't allowed for now.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please note we have a [CODE_OF_CONDUCT](https://github.com/silentsoft/acmicpc-maven-plugin/blob/master/CODE_OF_CONDUCT.md), please follow it in all your interactions with the project.

## License
Please refer to [LICENSE](https://github.com/silentsoft/acmicpc-maven-plugin/blob/master/LICENSE.txt).
