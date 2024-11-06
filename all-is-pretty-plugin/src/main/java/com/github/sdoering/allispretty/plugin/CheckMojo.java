package com.github.sdoering.allispretty.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;

import static com.github.sdoering.allispretty.plugin.Utils.PROPERTY_ALLISPRETTY_STATUS;

@Mojo(name = "check", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class CheckMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    public void execute() throws MojoExecutionException {
        final Object status = project.getProperties().get(PROPERTY_ALLISPRETTY_STATUS);
        if (status == null) {
            throw new MojoExecutionException("No pre-check status found. Did you run the pre-check goal?");
        } else if (!(status instanceof List)) {
            throw new MojoExecutionException("Invalid pre-check status found: " + status);
        }

        if (((List<?>) status).isEmpty()) {
            final List<String> newStatus = Utils.getSourceStatus();
            if (newStatus.isEmpty()) {
                getLog().info("All source files are properly prettified.");
            } else {
                throw new MojoExecutionException("Not all source files seem to be properly prettified:" + System.lineSeparator() + "  " +
                        String.join(System.lineSeparator() + "  ", newStatus));
            }
        } else {
            getLog().info("Does not seem to be a CI build. Ignore the check of properly prettified source files.");
        }
    }
}