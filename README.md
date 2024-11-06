# all-is-pretty-maven-plugin

[![Java CI with Maven](https://github.com/sdoeringNew/all-is-pretty-maven-plugin/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/sdoeringNew/all-is-pretty-maven-plugin/actions/workflows/maven.yml)

A maven plugin that ensures that the code has been formatted if multiple linters, prettifiers and code formatters are
used.

It has been especially designed for usage in your CI pipelines.

## Usage

The plugin provides a three-step process.

1. A pre-check that checks the current status of the source code.
1. Do the expected linting.
1. The actual check, which ensures that the source code has been changed.

Within CI pipelines, no changes shall be detected.
If the post-check fails, the source code has not been properly formatted.
Thus failing the build and therefore the pipeline.

### Configuration

```xml

<plugin>
    <groupId>com.github.sdoering</groupId>
    <artifactId>all-is-pretty-plugin</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <executions>
        <execution>
            <id>pre-build-check</id>
            <phase>validate</phase>
            <goals>
                <goal>pre-check</goal>
            </goals>
        </execution>
        <execution>
            <id>post-build-check</id>
            <phase>verify</phase>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Building

```bash
mvn clean install
```