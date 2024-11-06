package com.github.sdoering.allispretty.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.List;

@Mojo(name = "pre-check", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class PreCheckMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    public void execute() throws MojoExecutionException {
        final List<String> status = Utils.getSourceStatus();
        project.getProperties().put(Utils.PROPERTY_ALLISPRETTY_STATUS, status);
    }
}