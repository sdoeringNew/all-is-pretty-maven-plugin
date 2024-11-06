package com.github.sdoering.allispretty.plugin;

import org.apache.maven.plugin.MojoExecutionException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String PROPERTY_ALLISPRETTY_STATUS = "allispretty.pre-check.status";

    public static List<String> getSourceStatus() throws MojoExecutionException {
        try {
            final Process process = new ProcessBuilder()
                    .redirectInput(ProcessBuilder.Redirect.INHERIT)
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .command("git", "status", "--short", "--untracked-files=no")
                    .start();

            final List<String> outputLines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                reader.lines()
                        .forEach(outputLines::add);
            }
            process.waitFor();

            final int exitValue = process.exitValue();
            if (exitValue == 0) {
                return outputLines;
            }
            throw new MojoExecutionException("Failed to resolve status of sources. Exit value = " + exitValue);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MojoExecutionException("Unable to resolve status of sources", e);
        } catch (final IOException e) {
            throw new MojoExecutionException("Unable to resolve status of sources", e);
        }
    }
}
