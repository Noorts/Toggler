package core;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;

public class ToggleActionIntegrationTest extends BasePlatformTestCase {

    // The actionIds used to specify the editor action can be found:
    // https://github.com/JetBrains/intellij-community/tree/idea/222.3739.54/platform/ide-core/src/com/intellij/openapi/actionSystem/IdeActions.java

    private final String TOGGLE_ACTION = "ToggleAction"; // Defined in plugin.xml
    private final String TEST_FILE_NAME = "TestFile.java"; // Temporary virtual file name

    public void testToggleActionWraps() {
        myFixture.configureByText(TEST_FILE_NAME, "getName"); // |getName
        myFixture.performEditorAction(TOGGLE_ACTION); // |setName
        myFixture.performEditorAction(TOGGLE_ACTION); // |getName
        myFixture.checkResult("getName");
    }

    // The default toggles include: public, private, protected.
    public void testToggleActionReverseTogglesToNext() {
        myFixture.configureByText(TEST_FILE_NAME, "public"); // |public

        myFixture.performEditorAction(TOGGLE_ACTION); // |private
        myFixture.checkResult("private");

        myFixture.performEditorAction(TOGGLE_ACTION); // |protected
        myFixture.checkResult("protected");

        myFixture.performEditorAction(TOGGLE_ACTION); // |public
        myFixture.checkResult("public");
    }

    public void testToggleActionTogglesAWordRegardlessOfPosition() {
        // In front
        myFixture.configureByText(TEST_FILE_NAME, "getName");
        myFixture.performEditorAction(TOGGLE_ACTION); // |setName
        myFixture.checkResult("setName");

        // Behind
        myFixture.performEditorAction("EditorRight"); // s|etName
        myFixture.performEditorAction("EditorRight"); // se|tName
        myFixture.performEditorAction("EditorRight"); // set|Name
        myFixture.performEditorAction(TOGGLE_ACTION); // get|Name
        myFixture.checkResult("getName");

        // Inside
        myFixture.performEditorAction("EditorLineStart");; // |getName
        myFixture.performEditorAction("EditorRight"); // g|etName
        myFixture.performEditorAction(TOGGLE_ACTION); // s|etName
        myFixture.checkResult("setName");
    }

    public void testToggleActionDisablesPartialMatchingOnSelection() {
        myFixture.configureByText(TEST_FILE_NAME, "development"); // |development
        myFixture.performEditorAction(TOGGLE_ACTION); // |production - Favours largest match.
        myFixture.checkResult("production");

        // Selection is created to disable partial matching and to thus only toggle on full matches.
        myFixture.performEditorAction("EditorRightWithSelection"); // |p|roduction
        myFixture.performEditorAction("EditorRightWithSelection"); // |pr|oduction
        myFixture.performEditorAction("EditorRightWithSelection"); // |pro|duction
        myFixture.performEditorAction(TOGGLE_ACTION); // |pro|uction - No match was found yet.
        myFixture.checkResult("production");

        myFixture.performEditorAction("EditorRightWithSelection"); // |prod|uction
        myFixture.performEditorAction(TOGGLE_ACTION); // |dev|uction
        myFixture.checkResult("devuction");
    }

    public void testToggleActionFavoursMatchOnTheLeft() {
        myFixture.configureByText(TEST_FILE_NAME, "getFirst"); // |getFirst
        myFixture.performEditorAction("EditorRight"); // g|etFirst
        myFixture.performEditorAction("EditorRight"); // ge|tFirst
        myFixture.performEditorAction("EditorRight"); // get|First
        myFixture.performEditorAction(TOGGLE_ACTION); // set|First
        myFixture.checkResult("setFirst");
    }

    public void testToggleActionSupportsMultipleCursorsOnDifferentWords() {
        myFixture.configureByText(TEST_FILE_NAME, "import\npublic\nsetName"); // |import \n public \n setName
        myFixture.performEditorAction("EditorCloneCaretBelow"); // |import \n |public \n setName
        myFixture.performEditorAction(TOGGLE_ACTION); // |export \n |private \n setName
        myFixture.checkResult("export\nprivate\nsetName");
    }

    public void testToggleActionSupportsMultipleCursorsOnTheSameWord() {
        myFixture.configureByText(TEST_FILE_NAME, "import\npublic\nsetName"); // |import \n public \n setName
        myFixture.performEditorAction("EditorCloneCaretBelow"); // |import \n |public \n setName
        myFixture.performEditorAction("EditorLeft"); // |import| \n public \n setName
        myFixture.performEditorAction("EditorLeft"); // |impor|t \n public \n setName
        // Note: only one caret remains on the word toggled.
        myFixture.performEditorAction(TOGGLE_ACTION); // |export \n public \n setName
        myFixture.checkResult("export\npublic\nsetName");
    }

    // Check out the `StringTransformer` class and its own tests for more details. This validates basic case transfer.
    public void testToggleActionTransfersCase() {
        myFixture.configureByText(TEST_FILE_NAME, "Set"); // |Set
        myFixture.performEditorAction(TOGGLE_ACTION); // |Get
        myFixture.checkResult("Get");
    }
}
