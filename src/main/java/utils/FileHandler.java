package utils;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ThrowableComputable;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDate;

/**
 * A utility written to handle saving and loading of a String to/from a file.
 *
 * @author Noorts
 */
public class FileHandler {

    /**
     * Return the contents of a file as a String.
     * The file to load is selected through a file chooser dialog.
     * @return the content of the chosen file or null if loading the chosen file failed.
     * @throws FileSelectionCancelledException
     * thrown when the selection of a file through the file chooser was cancelled.
     * @throws IOException thrown when an unexpected IOException has occurred.
     */
    public static @NotNull String loadContentFromFileToString()
            throws FileSelectionCancelledException, IOException {

        // Open the file chooser and allow the user to choose a file.
        VirtualFile virtualFile = FileChooser.chooseFile(
                FileChooserDescriptorFactory.createSingleFileDescriptor(),
                null, null);

        // If no file was selected because the dialog was closed or cancelled throw an appropriate error.
        if (virtualFile == null) {
            throw new FileSelectionCancelledException();
        }

        // Load the text from the selected file. Throw an IOException if an IO error occurs.
        return VfsUtil.loadText(virtualFile);
    }

    /**
     * Save text to disk with a file saver dialog.
     * @param textToSaveToFile the text to be written to the file.
     * @throws FileSelectionCancelledException thrown when the selection of a file through the file saver was cancelled.
     * @throws IOException thrown when an unexpected IOException has occurred.
     */
    public static void saveTextToDisk(@NotNull String textToSaveToFile)
            throws FileSelectionCancelledException, IOException  {

        final String filenameForJsonExportFile = String.format(
                "TogglerExport_%s.json",
                LocalDate.now().toString()
        );

        // Create the file saver dialog that will be used to select/create the file to a directory.
        FileSaverDialog fileSaverDialog = FileChooserFactory.getInstance().createSaveFileDialog(
                new FileSaverDescriptor(
                        "Toggler JSON Export",
                        "Export the currently saved toggles to a JSON file."),
                (Project) null
        );

        // Open the file saver dialog and allow the user to choose a file (whether it exists or not doesn't matter).
        VirtualFileWrapper virtualFileWrapper = fileSaverDialog.save((VirtualFile) null, filenameForJsonExportFile);

        // If no file was selected because the dialog was closed or cancelled throw an appropriate error.
        if (virtualFileWrapper == null) {
            throw new FileSelectionCancelledException();
        }

        /* Save the text to disk. A virtual file is created if the user didn't overwrite an existing one.
         *
         * A ThrowableComputable is used to be able to throw an IOException inside of the thread started
         * by runWriteAction (Runnable doesn't allow throwing exceptions).
         * This works for now but could probably be improved upon. */
        ThrowableComputable<Boolean, IOException> throwableComputable = () -> {
            VirtualFile finalVirtualFile = virtualFileWrapper.getVirtualFile(true);
            if (finalVirtualFile == null) {
                throw new IOException();
            }
            VfsUtil.saveText(finalVirtualFile, textToSaveToFile);
            return null;
        };
        // Run the lambda defined above to save the file to disk.
        ApplicationManager.getApplication().runWriteAction(throwableComputable);
    }

    /**
     * A custom exception thrown when selection of a file through a file saver dialog was cancelled.
     * @see FileHandler#saveTextToDisk
     */
    public static class FileSelectionCancelledException extends Exception {
        public FileSelectionCancelledException() {}
    }
}
