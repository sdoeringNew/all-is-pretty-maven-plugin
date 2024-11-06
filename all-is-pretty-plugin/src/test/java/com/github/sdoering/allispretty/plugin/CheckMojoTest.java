package com.github.sdoering.allispretty.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckMojoTest {
    @Mock
    MavenProject project;
    @Mock
    Properties properties;

    CheckMojo mojoUnderTest;

    @BeforeEach
    void setUp() throws Exception {
        mojoUnderTest = new CheckMojo();

        final Field field = mojoUnderTest.getClass().getDeclaredField("project");
        field.setAccessible(true);
        field.set(mojoUnderTest, project);

        when(project.getProperties()).thenReturn(properties);
    }

    @Test
    void execute_failsOnMissingPreCheck() {
        // given
        when(properties.get("allispretty.pre-check.status")).thenReturn(null);

        // expect
        assertThatThrownBy(mojoUnderTest::execute)
                .isInstanceOf(MojoExecutionException.class)
                .hasMessage("No pre-check status found. Did you run the pre-check goal?");
    }

    @Test
    void execute_failsOnInvalidPreCheckStatus() {
        // given
        when(properties.get("allispretty.pre-check.status")).thenReturn("status");

        // expect
        assertThatThrownBy(mojoUnderTest::execute)
                .isInstanceOf(MojoExecutionException.class)
                .hasMessage("Invalid pre-check status found: status");
    }

    @Test
    void execute_isSkippedIfStatusContainsChangesInTheFirstPlace() {
        // given
        when(properties.get("allispretty.pre-check.status")).thenReturn(List.of("allready-changed-file.file"));

        // expect
        assertThatCode(mojoUnderTest::execute)
                .doesNotThrowAnyException();
    }

    @Test
    void execute_allIsPretty() {
        // given
        when(properties.get("allispretty.pre-check.status")).thenReturn(List.of());

        // expect
        try (MockedStatic<Utils> utils = Mockito.mockStatic(Utils.class)) {
            utils.when(Utils::getSourceStatus).thenReturn(List.of());

            assertThatCode(mojoUnderTest::execute)
                    .doesNotThrowAnyException();
        }
    }

    @Test
    void execute_someFilesNotPretty() {
        // given
        when(properties.get("allispretty.pre-check.status")).thenReturn(List.of());

        try (MockedStatic<Utils> utils = Mockito.mockStatic(Utils.class)) {
            // and
            utils.when(Utils::getSourceStatus).thenReturn(List.of("this-file-has-not-been-formatted.txt", "this-file.too"));

            // expect
            assertThatThrownBy(mojoUnderTest::execute)
                    .isInstanceOf(MojoExecutionException.class)
                    .hasMessage("Not all source files seem to be properly prettified:" + System.lineSeparator() +
                            "  this-file-has-not-been-formatted.txt" + System.lineSeparator() +
                            "  this-file.too");
        }
    }
}
