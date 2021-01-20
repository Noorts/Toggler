import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class AppSettingsComponent {

    private final JPanel myMainPanel;
    private final JBTextArea myJsonText = new JBTextArea();
    private final JBLabel myLabel = new JBLabel();

    public AppSettingsComponent() {
        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Json"), myJsonText, 1, true)
                .addComponent(myLabel)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return myJsonText;
    }

    @NotNull
    public String getJsonText() {
        return myJsonText.getText();
    }

    public void setJsonText(@NotNull String newText) {
        myJsonText.setText(newText);
    }

    public void setErrorMessage(@NotNull String errorMessage) {
        myLabel.setText(errorMessage);
    }
}
