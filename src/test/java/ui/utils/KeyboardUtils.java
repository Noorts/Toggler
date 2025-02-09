package ui.utils;

import com.intellij.remoterobot.utils.Keyboard;

import java.time.Duration;

public class KeyboardUtils {
    public KeyboardUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Sequentially press the keycodes on the keyboard. This is an alternative to Keyboard.enterText.
     */
    public static void enterKeycodeSequence(Keyboard keyboard, int... keycodes) {
        for (int keycode : keycodes) {
            keyboard.key(keycode, Duration.ofMillis(100));
        }
    }

    public static void repeatKeycodeNTimes(Keyboard keyboard, int keycode, int times) {
        for (int i = 0; i < times; i++) {
            keyboard.key(keycode, Duration.ofMillis(100));
        }
    }
}
