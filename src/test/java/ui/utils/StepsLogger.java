package ui.utils;

import com.intellij.remoterobot.stepsProcessing.StepLogger;
import com.intellij.remoterobot.stepsProcessing.StepWorker;

// Inspired by: https://github.com/JetBrains/intellij-ui-test-robot/blob/139a05eb99e9a49f13605626b81ad9864be23c96/ui-test-example/src/test/kotlin/org/intellij/examples/simple/plugin/utils/StepsLogger.kt
public class StepsLogger {
    private static boolean initialized = false;

    public static void init() {
        if (!initialized) {
            StepWorker.registerProcessor(new StepLogger());
            initialized = true;
        }
    }
}