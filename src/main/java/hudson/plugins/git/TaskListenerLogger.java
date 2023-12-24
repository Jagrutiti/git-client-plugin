package hudson.plugins.git;

import hudson.model.TaskListener;

/**
 * This class is an addition to task listener that will print logs based on flags.
 */
public class TaskListenerLogger {
    /**
     *  ENABLED_LOGGER_LEVEL can have values like: INFO, WARNING, ERROR, FATAL
     */
    String ENABLED_LOGGER_LEVEL = "ERROR";

    /**
     * Prints the log if printDetailedLogs if logger level matches the one in the logs.
     *
     * @param listener the task listener that will print log.
     * @param msg the message to be printed in the log.
     */
    public void printLogs(TaskListener listener, String msg) {
        if (getPrefix(msg).equals(ENABLED_LOGGER_LEVEL)) {
            listener.getLogger().print(msg);
        }
    }

    private String getPrefix(String msg) {
        String[] parts = msg.split(":");
        String prefix = parts[0].trim();
        return prefix;
    }
}
