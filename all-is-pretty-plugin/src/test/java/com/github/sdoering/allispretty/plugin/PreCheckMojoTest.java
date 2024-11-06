package com.github.sdoering.allispretty.plugin;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PreCheckMojoTest {
    @Mock
    MavenProject project;
    @Mock
    Properties properties;

    PreCheckMojo mojoUnderTest;

    @BeforeEach
    void setUp() throws Exception {
        mojoUnderTest = new PreCheckMojo();

        final Field field = mojoUnderTest.getClass().getDeclaredField("project");
        field.setAccessible(true);
        field.set(mojoUnderTest, project);

        when(project.getProperties()).thenReturn(properties);
    }

    @Test
    void execute_addsCurrentSourceStatus() throws Exception {
        // when
        mojoUnderTest.execute();

        // then
        verify(properties).put(eq("allispretty.pre-check.status"), any(List.class));
    }
}
