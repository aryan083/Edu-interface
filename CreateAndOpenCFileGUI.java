import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class CreateAndOpenCFileGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Create and Open C File");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JButton createButton = new JButton("Create and Open C File");
            createButton.addActionListener(e -> createAndOpenCFile());

            frame.add(createButton, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        });
    }

    private static void createAndOpenCFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save C File");
        
        // Set default save location (e.g., Desktop)
        String userHome = System.getProperty("user.home");
        File defaultSaveLocation = new File(userHome + File.separator + "Desktop");
        fileChooser.setCurrentDirectory(defaultSaveLocation);

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            if (!fileToSave.getName().endsWith(".c")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".c");
            }

            try {
                FileWriter writer = new FileWriter(fileToSave);
                writer.close();
                
                // Open the created C file using the default system editor
                Desktop.getDesktop().edit(fileToSave);

                JOptionPane.showMessageDialog(null, "C file created and opened successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error creating C file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
