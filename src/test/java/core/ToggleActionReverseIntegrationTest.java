package core;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

public class ToggleActionReverseIntegrationTest extends BasePlatformTestCase {

    // The actionIds used to specify the editor action can be found:
    // https://github.com/JetBrains/intellij-community/tree/idea/222.3739.54/platform/ide-core/src/com/intellij/openapi/actionSystem/IdeActions.java

    private final String TOGGLE_ACTION = "ToggleActionReverse"; // Defined in plugin.xml
    private final String TEST_FILE_NAME = "TestFile.java"; // Temporary virtual file name

    // The default toggles include: public, private, protected.
    public void testToggleActionReverseTogglesToPrevious() {
        myFixture.configureByText(TEST_FILE_NAME, "public"); // |public

        myFixture.performEditorAction(TOGGLE_ACTION); // |protected
        myFixture.checkResult("protected");

        myFixture.performEditorAction(TOGGLE_ACTION); // |private
        myFixture.checkResult("private");

        myFixture.performEditorAction(TOGGLE_ACTION); // |public
        myFixture.checkResult("public");
    }
}
