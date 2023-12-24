package hudson.plugins.git;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.TaskListener;
import hudson.tasks.Shell;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.LoggerRule;

public class TaskListenerLoggerTest {

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();

    @Rule
    public LoggerRule loggerRule = new LoggerRule();

    private TaskListener listener = () -> System.out;

    @Test
    public void shouldInvokeTheLoggerMethod() {
        TaskListenerLogger listenerLoggerMock = new TaskListenerLogger();
        listenerLoggerMock.printLogs(listener, "This message should be printed.");
        verify(listenerLoggerMock).printLogs(listener, "This message should be printed.");
    }

    @Test
    public void shouldNotInvokeTheLoggerMethod() {
        TaskListenerLogger listenerLoggerMock = new TaskListenerLogger();
        listenerLoggerMock.printLogs(listener, "This message should not be printed.");
        verify(listenerLoggerMock).printLogs(listener, "This message should not be printed.");
    }

    @Test
    public void shouldPrintOnlyRequiredLogs() throws Exception {
        FreeStyleProject project = jenkinsRule.createFreeStyleProject();
        project.getBuildersList().add(new Shell("echo hello"));
        project.getBuildersList().add(new Shell("INFO: This message should not be printed."));
        FreeStyleBuild build = project.scheduleBuild2(0).get();
        System.out.println(build.getDisplayName() + " completed");
        String s = FileUtils.readFileToString(build.getLogFile());
        assertThat(s, containsString("INFO: This message should not be printed."));

        TaskListenerLogger taskListenerLogger = new TaskListenerLogger();
        TaskListenerLogger listenerLoggerMock = spy(taskListenerLogger);
        listenerLoggerMock.printLogs(listener, "INFO: This message should not be printed.");
        verify(listenerLoggerMock).printLogs(listener, "INFO: This message should not be printed.");
    }

}
