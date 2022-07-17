package core;

import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.jetbrains.annotations.NotNull;

public class ToggleActionIntegrationTest extends BasePlatformTestCase {
    @Override
    protected @NotNull String getTestDataPath() {
        return "src/test/testData";
    }

    public void testSimpleToggleAction() {
        myFixture.configureByFile("DefaultFile.java");
        myFixture.performEditorAction("ToggleAction");

        myFixture.checkResultByFile("ToggledFile.java");
    }
}
