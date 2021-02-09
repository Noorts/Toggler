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
                .addComponent(new JBLabel("Json"))
                .addComponentFillVertically(myJsonText, 1)
                .addComponent(myLabel)
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
