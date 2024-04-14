/**
 ************************************************* EDUCATIONAL INTERFACE *************************************************
This project is aimed at improving the teaching learning experience in the field of programming, especially in C 
and Java language. This project can also be used as an active learning assesment tool to monitor the student on
the basis of self-study and active participation by the use of a logging system that is integrated into this project.
Furthermore, a secure and encoded algorithm of profile photo display has been created for this project, wherein the 
user's images from the database are scrambled and can only be unscrambled on the display window.

Contributors: Shantanusinh Parmar, Aryan Mahida
Languages used: Java
**/
import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import javafx.scene.web.WebEngine;


public class Edu_Interface
{
    public static void main(String[] args) { // main method will call the Interface class and start the application and SplitAndRenameImages class to split the images 
        System.setProperty("log4j2.xml", "true");
         new SplitAndRenameImages();
        SplitAndRenameImages.main2(null);
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        new Interface();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }

}
/*
 *This Class SplitAndRenameIamges is for splitting the images form photos folder in to 100 peices and Encoding the images 
 *the class creates the encoded parts of images and creates passkey.txt file that contains the splited images names
 *The output is the image peices which will be stored in the subfolder named by the iamge in folder splittedphotos
 */

class SplitAndRenameImages { 
    public static void main2(String[] args) {

     String currentDirectory = System.getProperty("user.dir");
     String folderPath = currentDirectory + File.separator + "photos" + File.separator;
     String outputFolderPath = currentDirectory + File.separator + "splittedphotos" + File.separator;
//path to the folder that will contain the splitted images
        RenameImages ri = new RenameImages();

        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            //system.out.println("Invalid folder path.");
            return;
        }

        File[] imageFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));

        if (imageFiles == null || imageFiles.length == 0) {
            //system.out.println("No image files found in the folder.");
            return;
        }

        try {

            for (File imageFile : imageFiles) {
                String imageFileName = removeExtension(imageFile.getName());
                String imageOutputFolderPath = outputFolderPath + imageFileName + "/";
                File imageOutputFolder = new File(imageOutputFolderPath);
                imageOutputFolder.mkdirs();

                if (imageOutputFolder.exists() && imageOutputFolder.isDirectory()) {
                    File[] files = imageOutputFolder.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            file.delete();
                        }
                    }
                }

                processImage(imageFile, imageOutputFolderPath);

                ri.renameAndGeneratePasskeys(imageOutputFolder);
            }

            //system.out.println("All images split, renamed, and saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processImage(File imageFile, String outputFolderPath) throws IOException {
// processImage method is responsible for splitting the images in to 100 peices and saving them in the subfolder named by the image name
        BufferedImage originalImage = ImageIO.read(imageFile);
        int width = originalImage.getWidth() / 10; 
        int height = originalImage.getHeight() / 10;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                BufferedImage subImage = originalImage.getSubimage(i * width, j * height, width, height);
                File outputImageFile = new File(outputFolderPath + String.format("%02d%02d.png", j, i));
                ImageIO.write(subImage, "png", outputImageFile);
            }
        }
    }

    private static String removeExtension(String fileName) {
        //removeExtension method is responsible for removing the extension of the image name
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }
}
/*
 * RenameIamges class is responsible for renaming the images and generating the passkey.txt file
 * This class will rename the images in the folder splittedphotos and generate the passkey.txt file that contains the names of the images
 * The output of this class is the renamed images and passkey.txt file
 * The passkey.txt file will be used by the ReconstructImages class to decode the images and display them
 * The images will be displayed in the form of 10*10 matrix
 */
class RenameImages {
    public void renameAndGeneratePasskeys(File folder) {
        File[] imageFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".png"));

        if (imageFiles == null || imageFiles.length == 0) {
            //system.out.println("No image files found in the folder.");
            return;
        }

        List<String> passkeys = new ArrayList<>();
        Random random = new Random();

        for (File imageFile : imageFiles) {
            String newName;
            String extension = getFileExtension(imageFile.getName());

            do {
                newName = generateRandomSequence(random);
            } while (new File(folder.getPath() + File.separator + newName + "." + extension).exists());

            String newFileName = newName + "." + extension;
            File newFile = new File(folder.getPath() + File.separator + newFileName);

            if (imageFile.renameTo(newFile)) {
                passkeys.add(newName);
                //system.out.println(imageFile.getName() + " -> " + newFileName);
            } else {
                //system.out.println("Failed to rename: " + imageFile.getName());
            }
        }

        // Write passkeys to Passkey.txt
        try {
            FileWriter passkeyWriter = new FileWriter(folder.getPath() + File.separator + "Passkey.txt");
            for (String passkey : passkeys) {
                passkeyWriter.write(passkey + "\n");
            }
            passkeyWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateRandomSequence(Random random) {
        //generateRandomSequence method is responsible for generating the random sequence of the images
        char[] sequence = new char[2];
        for (int i = 0; i < 2; i++) {
            sequence[i] = (char) ('A' + random.nextInt(26));
        }
        return new String(sequence);
    }

    private static String getFileExtension(String fileName) {
        //getFileExtension method is responsible for getting the extension of the image
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }
}
/*The ReconstructImages is responsible for decoding the spillted images from rootFolderPath
 *This class takes the passkey.txt file to decode the iamge and display it 
 *The selection of which image to display is done by matching the image name and Roll Number the matched image will be dispalyed  
*/
class ReconstructImages {
   static String rootFolderPath1 = System.getProperty("user.dir") + File.separator + "splittedphotos" + File.separator;

    public  String main5(String args[],String rollcall) {

String rootFolderPath = System.getProperty("user.dir") + File.separator + "splittedphotos" + File.separator;
 String image_path="";
        File rootFolder = new File(rootFolderPath);

        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            //system.out.println("Invalid root folder path.");

        }

        // Specify the folder name you want to display
        String folderName = rollcall; // Replace with the folder name you want to display

        // Get the list of subfolders
        File[] subfolders = rootFolder.listFiles(File::isDirectory);

        if (subfolders == null || subfolders.length == 0) {
            //system.out.println("No subfolders found in the root folder.");

        }

        // Check if the specified folder name exists within the root folder
        File selectedFolder = new File(rootFolder, folderName);
        if (selectedFolder.exists() && selectedFolder.isDirectory()) {
            String newPath = processSubfolder(selectedFolder);
            //system.out.println("New image path: " + newPath);
            image_path = newPath;
        } else {
            //system.out.println("Folder not found: " + folderName);
        }
        return image_path;
    }

    private static String processSubfolder(File subfolder) {
        //processSubfolder method is responsible for processing the subfolder and displaying the images
        List<String> passkeys = readPasskeys(new File(subfolder, "Passkey.txt"));
        if (passkeys.isEmpty()) {
            //system.out.println("No passkeys found in the subfolder: " + subfolder.getName());
            return "";
        }

        int imageCount = passkeys.size();
        int rows = 10;
        int columns = 10;
        int imageWidth = 170 / columns;
        int imageHeight = 170 / rows;
        JPanel panel = new JPanel(new GridLayout(rows, columns));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int index = i * columns + j;
                if (index < imageCount) {
                    String passkey = passkeys.get(index);
                    String imagePath = new File(subfolder, passkey + ".png").getAbsolutePath();
                    BufferedImage image = loadImage(imagePath);
                    if (image != null) {
                        ImageIcon icon = (image != null) ? new ImageIcon(image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH)) : new ImageIcon("path_to_default_image.png");
                        JLabel label = new JLabel(icon);
                        panel.add(label);
                    }
                } else {
                    panel.add(new JLabel());
                }
            }
        }

        displayImagesInFrame(panel);

        BufferedImage newImage = createImageFromPanel(panel);
        String newImagePath = new File(subfolder, "NewImage.png").getAbsolutePath();
        
        //deleteSegmentedImages(subfolder);
        saveImage(newImage, newImagePath);

        return newImagePath;
    }

    private static void displayImagesInFrame(JPanel panel) {
        //displayImagesInFrame method is responsible for displaying the images in the frame

        JFrame frame = new JFrame("Reconstructed Images");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(170,170);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.setLocation(5000,5000);

        frame.add(panel);
        frame.setVisible(true);

    }

    private static List<String> readPasskeys(File file) {
        //readPasskeys method is responsible for reading the passkeys from the passkey.txt file
        List<String> passkeys = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                passkeys.add(line.trim());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return passkeys;
    }

    private static BufferedImage loadImage(String imagePath) {
        //loadImage method is responsible for loading the images from the folder
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            if (image != null) {
                return image;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BufferedImage createImageFromPanel(JPanel panel) {
        //createImageFromPanel method is responsible for creating the image from the panel
        // Get the panel's size
        int width = 170;
        int height = 170;

        // Create a BufferedImage with the same GraphicsConfiguration as the panel
        GraphicsConfiguration gc = panel.getGraphicsConfiguration();
        BufferedImage image = gc.createCompatibleImage(width, height);

        // Get the Graphics object for the BufferedImage
        Graphics2D g2d = image.createGraphics();

        // Paint the panel onto the BufferedImage
        panel.paint(g2d);
        g2d.dispose();

        return image;
    }

    private static void saveImage(BufferedImage image, String imagePath) {
        //saveImage method is responsible for saving the image in the folder
        try {
            ImageIO.write(image, "png", new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}  
/*
 * The Class Interface is the first Frame that the student will see after executing the code this class an transparent see-through Frame
 * It has 2 input fields those are Student Name and Roll Number if the Roll Number and Name matches form database file class_list.xlsx
 * from excel file if the name and roll no are matched then the student will be asked to choice the language from JAVA or C language from TwoButtonFrame Class 
 * Also this class will save the student name,roll no, data, starting time ,ending time and total time student has spent on the application in data.csv file
 * this will help the educators to monitor the student's activity on the application and improve the teaching learning experience
*/
class Interface extends JFrame 
{
    private JButton startButton;
    private JButton exitButton;
    public String Supername, SuperrollNo, SuperBatch;

    public Interface() {
        // Create the main frame and set its properties (size, location, etc.)
        setUndecorated(true); // Remove title bar and borders

        setSize(Toolkit.getDefaultToolkit().getScreenSize()); // Set full screen size
        setLocation(0, 0); // Set frame position at (0, 0)

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set full screen

        // Make the frame transparent by setting the background color with an alpha value (transparency)
        setBackground(new java.awt.Color(0, 0, 0, 128)); // Change the alpha value (last parameter) as needed

        JTextField name = new JTextField();
        name.setBackground(new java.awt.Color(255, 255, 255, 128)); // Set a transparent background
        name.setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 20));

        JTextField pswrd = new JTextField();
        pswrd.setBackground(new java.awt.Color(255, 255, 255, 128)); // Set a transparent background
        pswrd.setFont(new java.awt.Font("Helvetica", java.awt.Font.PLAIN, 20));

        JLabel Name = new JLabel("Name:");
        JLabel Pswrd = new JLabel("Roll No:");

        Name.setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 22));
        Name.setForeground(new java.awt.Color(218, 165, 32));

        Pswrd.setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 22));
        Pswrd.setForeground(new java.awt.Color(218, 165, 32));

        startButton = new JButton("START");
        startButton.setForeground(new java.awt.Color(218, 165, 32)); // Set GOLD color for the text
        startButton.setBackground(java.awt.Color.RED); // Background Color RED
        startButton.setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 24));
        startButton.setFocusPainted(false);
        startButton.setBounds(1050, 600, 120, 40);
        startButton.setEnabled(true); // Set the button initially disabled

        exitButton = new JButton("Exit");
        exitButton.setForeground(java.awt.Color.WHITE); // Set text color to white
        exitButton.setBackground(new java.awt.Color(128, 128, 128));
        exitButton.setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 24));
        exitButton.setFocusPainted(false);
        exitButton.setBounds(10, 10, 100, 35);

        add(startButton);
        add(exitButton);
        add(name);
        add(pswrd);
        add(Name);
        add(Pswrd);

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

        setLayout(null);

        name.setBounds(1000, 450, 200, 30);
        pswrd.setBounds(1000, 550, 200, 30);
        Name.setBounds(790, 450, 200, 30);
        Pswrd.setBounds(790, 550, 200, 30);

        startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Perform the desired action when the button is clicked
                    // For example, you can close the image display and continue with the main code
                    String inputText1 = name.getText().trim(); // Remove leading/trailing whitespace
                    String inputText2 = pswrd.getText().trim(); // Remove leading/trailing whitespace
                    String matchedBatch = getBatchFromExcel(inputText1, inputText2);
                    Supername = inputText1;
                    SuperrollNo = inputText2;
                    SuperBatch = matchedBatch;

                    // Hide the frame to simulate closing the image display
                    setVisible(false);
                    if (matchedBatch != null) {
                        // Valid name and roll number, perform desired actions
                        setVisible(false);
                        //system.out.println("The name of the student is: " + inputText1);
                        //system.out.println("The roll number of the student is: " + inputText2);
                        //system.out.println("Matched Batch: " + matchedBatch);
                        DateTimeExample();
                        writeDataToCSV(inputText1, inputText2);
                        // Calling the user? choice of educational tool window.
                        TwoButtonsFrame.createAndShowGUI(Supername, SuperrollNo, SuperBatch);
                        dispose();

                    } else {
                        // Invalid name and roll number, display a message or take appropriate action
                        JOptionPane.showMessageDialog(Interface.this, "Invalid name and roll number.", "Error", JOptionPane.ERROR_MESSAGE);
                        name.setText(""); // Clear the name text field
                        pswrd.setText(""); // Clear the roll number text field
                        setVisible(true); // Show the frame again
                    }

                }
            });

        exitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0); // Exit the application
                }
            });
    }
/*  
 * The Class TwoButtonsFrame will give the Students the choice of language to select from C or JAVA
 * According to the choice of the student the respective educational tool will be opened either for C or JAVa
 * The Class TwoButtonsFrame is a transparent see-through Frame as the Interface Class
 */
    class TwoButtonsFrame {
        public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        createAndShowGUI("a", "A", "A");
                    }
                });
        }

        public static void createAndShowGUI(String name, String rollNo, String Batch) {
            // Create the main frame and set its properties (size, location, etc.) for the TwoButtonsFrame
            JFrame frame = new JFrame("Two Buttons Frame");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);

            // Create a panel to hold the buttons
            JPanel panel = new JPanel();
            frame.add(panel);
            frame.setUndecorated(true);
            frame.setBackground(new Color(0, 0, 0, 0));
            panel.setBackground(new Color(0, 0, 0, 0));

            // Create ImageIcons from image files (replace with your image file paths)
            ImageIcon cIcon = new ImageIcon("c language.png");
            ImageIcon javaIcon = new ImageIcon("java.png");

            // Create the first button with the "C" image
            JButton button1 = new JButton();
            button1.setIcon(scaleImageIcon(cIcon, 100, 100)); // Set the size of the button
            button1.setBackground(new Color(0, 0, 0, 0));
            button1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // This code will run when Button 1 is clicked
                        // You can edit this code to perform any action you want.
                        new Mannual(name, rollNo, Batch);
                    }
                });

            // Create the second button with the "JAVA" image
            JButton button2 = new JButton();
            button2.setIcon(scaleImageIcon(javaIcon, 100, 100)); // Set the size of the button
            button2.setBackground(new Color(0, 0, 0, 0));
            button2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // This code will run when Button 2 is clicked
                        // You can edit this code to perform any action you want.
                        new MannualForJava(name, rollNo, Batch);
                    }
                });

            // Add the buttons to the panel
            panel.add(button1);
            panel.add(button2);

            // Center the frame on the screen
            frame.setLocationRelativeTo(null);

            // Make the frame visible
            frame.setVisible(true);
        }

        // Method to scale an ImageIcon to the specified width and height
        private static ImageIcon scaleImageIcon(ImageIcon icon, int width, int height) {
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
    }

    private String getBatchFromExcel(String name, String rollNo) {
        //getBatchFromExcel method is responsible for getting the batch of the student from the excel file
        // This method will return the batch of the student if the name and roll no are matched form the excel file
    
        try {
            FileInputStream file = new FileInputStream(new File("Class_list.xlsx"));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0); // Assuming batch information is in the first sheet

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell nameCell = row.getCell(1); // Column B
                Cell rollNoCell = row.getCell(0); // Column A
                Cell batchCell = row.getCell(2); // Column C

                // Handle different cell types
                String excelName = "";
                if (nameCell != null) {
                    if (nameCell.getCellType() == CellType.STRING) {
                        excelName = nameCell.getStringCellValue().trim();
                    } else if (nameCell.getCellType() == CellType.NUMERIC) {
                        excelName = String.valueOf(nameCell.getNumericCellValue()).trim();
                    }
                }

                String excelRollNo = "";
                if (rollNoCell != null) {
                    if (rollNoCell.getCellType() == CellType.STRING) {
                        excelRollNo = rollNoCell.getStringCellValue().trim();
                    } else if (rollNoCell.getCellType() == CellType.NUMERIC) {
                        excelRollNo = String.valueOf((int) rollNoCell.getNumericCellValue()).trim();
                    }
                }

                String batch = "";
                if (batchCell != null && batchCell.getCellType() == CellType.STRING) {
                    batch = batchCell.getStringCellValue().trim();
                }

                if (excelName.equalsIgnoreCase(name) && excelRollNo.equals(rollNo)) {
                    workbook.close();
                    return batch;
                }
            }

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Return null if not found
    }

    private void writeDataToCSV(String name, String rollNo) {
        //writeDataToCSV method is responsible for writing the data of the student in the data.csv file
        // This method will write the data of the student in the data.csv file if the name and roll no are matched form the excel file

        try {
            String filename = "data.csv";
            File csvFile = new File(filename);
            boolean fileExists = csvFile.exists();

            // Create the FileWriter and BufferedWriter
            FileWriter writer = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(writer);

            // If the file does not exist, write the header
            if (!fileExists) {
                bw.write("Name,Roll No.,Total Time Spent,Start Time,End Time,Date\n");
            }

            // Get the current date and time
            LocalDateTime startTime = LocalDateTime.now();
            LocalDateTime endTime = LocalDateTime.now();

            // Calculate the total time spent on the app
            Duration totalTime = Duration.between(startTime, endTime);

            // Define the date and time formatters
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            // Format the date and time
            String formattedStartTime = startTime.format(timeFormatter);
            String formattedEndTime = endTime.format(timeFormatter);
            String formattedDate = startTime.format(dateFormatter);

            // Write the data to the file
            bw.write(name + "," + rollNo + "," + totalTime.toMillis() + ","
                + formattedStartTime + "," + formattedEndTime + "," + formattedDate + "\n");

            // Close the BufferedWriter
            bw.close();

            //system.out.println("Data written to CSV successfully.");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void DateTimeExample() {
        //DateTimeExample method is responsible for getting the date and time of the system and giveing them to writeDataToCSV method
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define the date format
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Format the date and time
        String formattedDate = now.format(dateFormatter);
        String formattedTime = now.format(timeFormatter);

        //system.out.println("Current date (DD/MM/YYYY): " + formattedDate);
        //system.out.println("Local system time (HH:mm:ss): " + formattedTime);
    }

}

/*
 * The Class MCQQuizApp is self explanatory it is a quiz app for the students to test their knowledge on C language
 * This Class will pop a window with 4 options for each question and the student has to select the correct option
 * The student will be given total 20 questions and the student has to answer all the questions
 * The student will be given 1 point for each correct answer and 0 for each wrong answer
 * The student will be given 1 MCQ Quiz for each time the student complete the 1 topic of the C language  
 */

class MCQQuizApp {
    private String[][] questions = {
        // Questions and answers are stored in a 2D array for C language
            {
                "What does the \"int\" data type represent in C?",
                "Integer",
                "Character", 
                "Floating-point number", 
                "Array"
            },
            {
                "Which operator is used to access the value at the address stored in a pointer variable?",
                "*", 
                " &", 
                "-", 
                "->"
            },
            {       "In the \"if-else\" statement, what is the purpose of the \"else\" part?",
                "To handle multiple conditions",
                "To ensure that the program doesn't crash",
                "To provide an alternative course of action",
                "To end the program"
            ,},
            {" Which control structure is specifically designed for handling multiple choices or options?",
                " if-else statement",
                "for loop",
                "switch-case statement",
                "do-while loop"
            },
            {"The ternary operator in C is also known as:",
                "Conditional operator",
                "Binary operator",
                "Looping operator",
                "Comparative operator"},
            {"What purpose does the \"goto\" keyword serve in C programming?",
                "It is used to declare a label.",
                "It is used for memory allocation.",
                "It is used to transfer control to a labeled statement.",
                "It is used for exiting a loop."},
            {"The \"continue\" statement is used to:",
                "Terminate the loop.",
                "Exit the program.",
                "Skip the remaining statements in the current iteration.",
                "Pause and wait for user input."},
            {"What is the function of the \"break\" statement in C?",
                "To end the program.",
                "To skip the current iteration of the loop.",
                "To continue to the next iteration of the loop.",
                "To terminate the loop or switch-case statement."},
            {"Which type of loop is specifically useful when the number of iterations is known beforehand?",
                "While loop",
                "For loop",
                "Do-while loop",
                "Switch-case loop"},
            {"The \"while loop\" is used for",
                "Executing a set of statements a specified number of times.",
                "Iterating over items in an array.",
                "Making a choice between two alternatives.",
                "Iterating as long as a condition holds true."},
            {"How is the \"do-while loop\" different from the \"while loop\"?",
                "The \"do-while loop\" doesn't support loops.",
                "The \"do-while loop\" doesn't require a loop condition.",
                "The \"do-while loop\" always executes at least once, regardless of the condition.",
                "The \"do-while loop\" can only be used for array manipulation."},
            {"What is an array in C?",
                "A collection of different data types.",
                "A single variable that can store multiple values of the same data type",
                " A data type used to store characters only",
                "An operator used to perform arithmetic operations on variables."},
            {"In C, how are strings represented?",
                "As an array of characters.",
                "As a single character",
                "As a numerical value.",
                "As a floating-point value"},
            {" What is a structure in C?",
                "A control statement used to make decisions.",
                "A data type used to store multiple values of different data types under a single name.",
                "A loop construct used for iteration.",
                "A mathematical operator."},
            {"How does a union differ from a structure in C?",
                "Unions can store variables of different data types, while structures cannot.",
                "Structures can store variables of different data types, while unions cannot.",
                "Unions and structures are the same.",
                "Unions can only store characters, while structures can store any data type."},
            {"What is a function in C?",
                "A reserved keyword used to declare variables.",
                "A data type used to store numbers.",
                "A block of code that performs a specific task and can be called from other parts of the program.",
                "An operator used for pointer manipulation."},
            { "What is recursion in C?",
                "A type of loop structure.",
                "A data type used for storing sequential data.",
                " A programming technique where a function calls itself to solve a problem.",
                " An operator used to perform bitwise operations."},
            {"What is a pointer in C?",
                "A special data type used for storing characters.",
                "A variable that stores the memory address of another variable.",
                "A type of loop construct.",
                "An operator used for multiplication."},
            {" In C, when a function receives arguments by call by value, what happens?",
                "The function can modify the original arguments directly.",
                "The function receives a copy of the arguments and cannot modify the original arguments.",
                "The function can access global variables directly.",
                "The function can modify the original arguments through pointers."},
            {"What is \"call by reference\" in C?",
                "A way to pass arguments to a function by sending their values directly.",
                "A way to pass arguments to a function by sending their addresses.",
                "A way to pass arguments to a function by converting them to references.",
                "A way to pass arguments to a function using the \"call\" keyword."},
            {"Which library is used for file operations in C?",
                "stdio.h",
                "math.h",
                "fileio.h",
                "fstream.h"},
            {"In which type of data structure is binary search most efficient?",
                "Linked lists",
                "Stacks",
                "Queues",
                "Sorted arrays"},
            {"What is the purpose of the \"calloc\" function in C?",
                "To allocate memory for a single variable.",
                "To deallocate memory.",
                "To allocate memory for an array of elements and initialize them to zero.",
                "To perform mathematical calculations."},
            {"Which function is used to allocate memory dynamically in C?",
                "new",
                "alloc",
                "malloc",
                "memalloc"
            },
            {"What does the \"realloc\" function do in C?",
                "It releases allocated memory.",
                "It reallocates memory to make it contiguous.",
                "It realigns memory addresses.",
                "It changes the data type of allocated memory."

            }

        };
    private int[] correctAnswers = {0,1,2,2,0,2,2,3,1,3,2,1,0,1,0,2,2,1,1,1,2,3,0,2,0
        }; // Index of correct answer for each question

    private int selectedQuestionIndex;
    public MCQQuizApp(int selectedQuestionIndex) {
        // Constructor for the MCQQuizApp class to initialize the selectedQuestionIndex variable with the value passed as parameter from the description class
        this.selectedQuestionIndex = selectedQuestionIndex;
    }

    public void startQuiz() {
        //startQuiz method is responsible for starting the quiz

        if (selectedQuestionIndex >= 0 && selectedQuestionIndex < questions.length) {
            askQuestion(selectedQuestionIndex);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid question index.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void askQuestion(int questionIndex) {
        //askQuestion method is responsible for asking the question to the student
        // This method will ask the question to the student and the student has to select the correct option
    
        String question = questions[questionIndex][0];
        String[] options = {
                questions[questionIndex][1],
                questions[questionIndex][2],
                questions[questionIndex][3],
                questions[questionIndex][4]
            };

        JFrame customFrame = new JFrame("Question");
        customFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        customFrame.setUndecorated(true); // Remove title bar and borders
        customFrame.setSize(400, 200);
        customFrame.setLocationRelativeTo(null);

        JPanel questionPanel = new JPanel(new BorderLayout());
        JLabel questionLabel = new JLabel(question);

        JPanel optionsPanel = new JPanel(new GridLayout(options.length, 1));

        ButtonGroup optionGroup = new ButtonGroup();

        for (int i = 0; i < options.length; i++) {
            JRadioButton radioButton = new JRadioButton(options[i]);
            radioButton.setActionCommand(Integer.toString(i));
            optionGroup.add(radioButton);
            optionsPanel.add(radioButton);
        }

        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedOption = optionGroup.getSelection().getActionCommand();
                    int userChoice = Integer.parseInt(selectedOption);
                    checkAnswer(questionIndex, userChoice);
                    customFrame.dispose();
                }
            });

        questionPanel.add(questionLabel, BorderLayout.NORTH);
        questionPanel.add(new JScrollPane(optionsPanel), BorderLayout.CENTER);
        questionPanel.add(submitButton, BorderLayout.SOUTH);
        customFrame.add(questionPanel);

        customFrame.setVisible(true);
    }

    private void checkAnswer(int questionIndex, int userChoice) {
        //checkAnswer method is responsible for checking the answer of the student
        // This method will check the answer of the student and will show the result to the student

        if (userChoice == correctAnswers[questionIndex]) {
            JOptionPane.showMessageDialog(null, "Correct!", "Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect!", "Result", JOptionPane.ERROR_MESSAGE);
        }
    }
}

/*
 * The Class MCQQuizAppForJava is self explanatory it is a quiz app for the students to test their knowledge on JAVA language
 * This Class will pop a window with 4 options for each question and the student has to select the correct option
 * The student will be given total 20 questions and the student has to answer all the questions
 * The student will be given 1 point for each correct answer and 0 for each wrong answer
 * The student will be given 1 MCQ Quiz for each time the student complete the 1 topic of the JAVA language  
 */

class MCQQuizAppForJava {
    private String[][] questions = {
        // Questions and answers are stored in a 2D array for JAVA language
            {
                "What is an array in Java?",
                "A collection of different data types.",
                "A single variable that can store multiple values of the same data type.",
                "A data type used to store characters only.",
                "An operator used for arithmetic operations on variables."
            },

            // CLASS
            {
                "In Java, what is a class?",
                "A data structure used to store multiple values.",
                "A blueprint for creating objects.",
                "A reserved keyword for conditional statements.",
                "A loop construct for iteration."
            },

            // COLLECTION FRAMEWORK
            {
                "What is the Java Collection Framework?",
                "A framework for handling exceptions.",
                "A framework for managing files.",
                "A framework for working with collections of objects.",
                "A framework for creating graphical user interfaces."
            },

            // CONSTRUCTOR
            {
                "What is a constructor in Java?",
                "A method used to destroy objects.",
                "A method used to initialize objects.",
                "A method used to perform mathematical calculations.",
                "A method used to access class members."
            },

            // DATA ABSTRACTION
            {
                "What is data abstraction in Java?",
                "Hiding the implementation details and showing only the necessary features of an object.",
                "Storing data in an array.",
                "Representing data in a tabular format.",
                "Creating abstract data types."
            },

            // ENCAPSULATION
            {
                "What is encapsulation in Java?",
                "A way to store data in an array.",
                "A way to hide the implementation details and restrict access to the internal state of an object.",
                "A way to create graphical user interfaces.",
                "A way to perform arithmetic operations on variables."
            },

            // ENUM
            {
                "What is an enum in Java?",
                "A data structure used to store multiple values.",
                "A reserved keyword for defining constants with a fixed set of values.",
                "A type of loop construct for iteration.",
                "A method used to access class members."
            },

            // EXCEPTIONS
            {
                "What are exceptions in Java?",
                "Special keywords used for conditional statements.",
                "Values used to store data.",
                "Objects used to handle errors and exceptional situations in a program.",
                "Methods used for file handling."
            },

            // FILE HANDLING
            {
                "What is file handling in Java?",
                "A way to handle arrays of data.",
                "A way to create graphical user interfaces.",
                "A way to work with files, reading from and writing to them.",
                "A way to perform mathematical calculations."
            },

            // FLOW CONTROL
            {
                "What is flow control in Java?",
                "A way to create graphical user interfaces.",
                "A way to control the execution sequence of statements in a program.",
                "A way to handle exceptions.",
                "A way to store data in arrays."
            },

            // GUI
            {
                "What does GUI stand for in Java?",
                "General User Interface.",
                "Graphical User Interface.",
                "Global User Interaction.",
                "General User Integration."
            },

            // INHERITANCE
            {
                "What is inheritance in Java?",
                "A way to create objects.",
                "A way to define constants.",
                "A way to reuse and extend the properties and behaviors of a class.",
                "A way to handle exceptions."
            },

            // INTERFACE
            {
                "What is an interface in Java?",
                "A way to store data.",
                "A way to define constants.",
                "A way to declare a class.",
                "A way to define a contract for implementing classes."
            },

            // OBJECT
            {
                "What is an object in Java?",
                "A collection of different data types.",
                "A reserved keyword for conditional statements.",
                "An instance of a class that represents a real-world entity and has state and behavior.",
                "A loop construct for iteration."
            },

            // OPERATORS
            {
                "What are operators in Java?",
                "Special methods in a class.",
                "Keywords used to define data types.",
                "Symbols used to perform operations on variables and values.",
                "Statements used to control program flow."
            },

            // PACKAGE
            {
                "What is a package in Java?",
                "A way to store data.",
                "A way to define constants.",
                "A way to group related classes and interfaces together.",
                "A way to handle exceptions."
            },

            // POLYMORPHISM
            {
                "What is polymorphism in Java?",
                "A way to create objects.",
                "A way to perform arithmetic operations.",
                "A way to reuse code.",
                "A way to represent different behaviors in a unified way."
            },

            // STRING
            {
                "What is a string in Java?",
                "A collection of different data types.",
                "A single variable that can store multiple values of the same data type.",
                "A data type used to store characters only.",
                "A reserved keyword for conditional statements."
            },

            // VARIABLES
            {
                "What are variables in Java?",
                "Keywords used to define data types.",
                "Identifiers used to store data values.",
                "Methods used for arithmetic operations.",
                "Statements used to control program flow."
            }

        };
    private int[] correctAnswers = { 
        // Index of correct answer for each question

            0,1,2,1,0,1,1,2,2,1,1,2,3,2,3,2,2,3,1,1
        }; // Index of correct answer for each question

    private int selectedQuestionIndex;

    public MCQQuizAppForJava(int selectedQuestionIndex) {
        // Constructor for the MCQQuizAppForJava class to initialize the selectedQuestionIndex variable with the value passed as parameter from the descriptionFrameForJava class
        this.selectedQuestionIndex = selectedQuestionIndex;
    }

    public void startQuiz() {
        //startQuiz method is responsible for starting the quiz
        // This method will start the quiz for the student
        // This method will ask the question to the student and the student has to select the correct option

        if (selectedQuestionIndex >= 0 && selectedQuestionIndex < questions.length) {
            askQuestion(selectedQuestionIndex);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid question index.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void askQuestion(int questionIndex) {
        //askQuestion method is responsible for asking the question to the student
        // This method will ask the question to the student and the student has to select the correct option

        String question = questions[questionIndex][0];
        String[] options = {
                questions[questionIndex][1],
                questions[questionIndex][2],
                questions[questionIndex][3],
                questions[questionIndex][4]
            };

        JFrame customFrame = new JFrame("Question");
        customFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        customFrame.setUndecorated(true); // Remove title bar and borders
        customFrame.setSize(400, 200);
        customFrame.setLocationRelativeTo(null);

        JPanel questionPanel = new JPanel(new BorderLayout());
        JLabel questionLabel = new JLabel(question);

        JPanel optionsPanel = new JPanel(new GridLayout(options.length, 1));

        ButtonGroup optionGroup = new ButtonGroup();

        for (int i = 0; i < options.length; i++) {
            JRadioButton radioButton = new JRadioButton(options[i]);
            radioButton.setActionCommand(Integer.toString(i));
            optionGroup.add(radioButton);
            optionsPanel.add(radioButton);
        }

        JButton submitButton = new JButton("Submit");

        submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedOption = optionGroup.getSelection().getActionCommand();
                    int userChoice = Integer.parseInt(selectedOption);
                    checkAnswer(questionIndex, userChoice);
                    customFrame.dispose();
                }
            });

        questionPanel.add(questionLabel, BorderLayout.NORTH);
        questionPanel.add(new JScrollPane(optionsPanel), BorderLayout.CENTER);
        questionPanel.add(submitButton, BorderLayout.SOUTH);
        customFrame.add(questionPanel);

        customFrame.setVisible(true);
    }

    private void checkAnswer(int questionIndex, int userChoice) {
        //checkAnswer method is responsible for checking the answer of the student
        

        if (userChoice == correctAnswers[questionIndex]) {
            JOptionPane.showMessageDialog(null, "Correct!", "Result", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect!", "Result", JOptionPane.ERROR_MESSAGE);
        }
    }
}
/*
 * This Class Temp_webview is for the opening the Web link provided in the C language and JAVA language educational tool
 * This Class will open the web link in the same window as the educational tool 
 * The Class is resposible for encourage the students to learn more about the C language and JAVA language on the internet
 * This will devlop the habit of Self-learing in Students 
 */
class Temp_webview extends JFrame {
    private JTextField urlField;
    private JFXPanel jfxPanel;
    private WebView webView;

    public Temp_webview() {
        // Constructor for the Temp_webview class to initialize the urlField, jfxPanel and webView variables
        setTitle("Mini Web Browser");

        setLayout(new BorderLayout());
        setSize(800, 600);

        urlField = new JTextField();
        jfxPanel = new JFXPanel();

        urlField.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    loadURL(urlField.getText());
                }
            });

        //add(urlField, BorderLayout.NORTH);
        add(jfxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
                    webView = new WebView();
                    jfxPanel.setScene(new Scene(webView));
            });
    }

    private void loadURL(String url) {
        //loadURL method is responsible for loading the url in the webview 
        Platform.runLater(() -> {
                    webView.getEngine().load(url);
            });
    }

    public static void main6(String[] args) {
        //main method is responsible for running the Temp_webview class
        SwingUtilities.invokeLater(() -> {
                    Temp_webview browser = new Temp_webview();
                    if (args.length > 0) {
                        browser.urlField.setText(args[0]);
                        browser.loadURL(args[0]);
                    }
                    browser.setVisible(true);
            });
    }

}
/*
 * The Temp_vidview Class is for the opening the Youtube video link provided in the C language and JAVA language educational tool
 * Links are Embbed Links so the studnet will only see the Youtube Video and not the other videos
 * Links will be in the same window as the educational tool
 */
class Temp_vidview extends JFrame {
    private JFXPanel vjfxPanel;
    private WebView vwebView;
    private WebEngine vwebEngine;

    public Temp_vidview() {
        // Constructor for the Temp_vidview class to initialize the vjfxPanel, vwebView and vwebEngine variables
        setTitle("Youtube Player");
        setLayout(new BorderLayout());
        setSize(800, 600);

        vjfxPanel = new JFXPanel();
        add(vjfxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> {
                    vwebView = new WebView();
                    vwebEngine = vwebView.getEngine();
                    vjfxPanel.setScene(new Scene(vwebView));
            });
    }

    private void loadYouTubeVideo(String youtubeEmbedUrl) {
        //loadYouTubeVideo method is responsible for loading the youtube video in the webview
        // This method will load the youtube video in the webview and the student will be able to watch the video

        Platform.runLater(() -> {
                    //system.out.println(youtubeEmbedUrl+"##");
                    vwebEngine.loadContent("<iframe width=\"800\" height=\"600\" src=\"" + youtubeEmbedUrl + "\" frameborder=\"0\" allowfullscreen></iframe>");
            });
    }

    public static void main8(String[] args) {
        //main method is responsible for running the Temp_vidview class

        SwingUtilities.invokeLater(() -> {
                    Temp_vidview vbrowser = new Temp_vidview();
                    if (args.length > 0) {
                        vbrowser.loadYouTubeVideo(args[0]);
                    }
                    vbrowser.setVisible(true);
            });
    }
}
/*
 * The Class Mannual is third class and main hub for learing and testing the knowledge of the students
 * This Class will contain the topics of the C language and  quiz app for the students to test their knowledge on C language
 * The topics will in from of buttons and the student has to click on the button to open the topic 
 * After clicking the topic button description of the topic will open in the new window with buttons to video weblink and example code 
 */
class Mannual  {
    public static boolean b = false;
    JFrame frame;
    JLabel backgroundLabel;
    //  private int currentSetIndex = 0; 
    JLabel nameLabel;
    JLabel rollNoLabel;
    JLabel batchLabel;

    String[] iconNames = {
            //"Annotation",
            "DATA TYPES",
            "OPRATORS",
            "IF ELSE",
            "SWITCH-CASE",
            "TERNARY OPERATOR",
            "GO KEYWORD",
            "CONTINUE",
            "BREAK",
            "FOR LOOP",
            "WHILE LOOP",
            "DO WHILE LOOP",
            "ARRAYS",
            "STRINGS",
            "STRUCTURE",
            "UNION",
            "FUNCTIONS",
            "RECURSION",
            "POINTERS",
            "CALL BY VALUE",
            "CALL BY REFERENCE",
            "FILE MANEGMENT",
            "BINARY SEARCH",
            "CALOC",
            "MALOC",
            "RELLOC"
        };
    JLabel imageLabel;
    public Mannual(String name, String rollNo, String batch) {
        // Constructor for the Mannual class to initialize the name, rollNo and batch variables
        // This constructor will initialize the name, rollNo and batch variables with the values passed as parameters from TwoButtonsFrame class
        // This constructor will also initialize the iconNames array with the names of the topics of the C language
        
        frame = new JFrame("Mannual");
        frame.setUndecorated(true);
        JButton exitButton = new JButton("Exit");
        ReconstructImages bv = new ReconstructImages();
        String s = bv.main5(null,rollNo);
        //system.out.println(s);
        ImageIcon imageIcon = new ImageIcon(s); // Replace with the actual image path
        Image scaledImage2 = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(scaledImage2);
        imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(50, 50, 200, 200);

        // Create JLabels for name, roll number, and batch using the provided arguments
        nameLabel = new JLabel("Name: " + name);
        nameLabel.setBounds(50, 260, 200, 20);
        nameLabel.setForeground(new java.awt.Color(218, 165, 32)); // Change font color to white
        nameLabel.setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 11)); // Change font size and style

        rollNoLabel = new JLabel("Roll No: " + rollNo);
        rollNoLabel.setBounds(50, 280, 200, 20);
        rollNoLabel.setForeground(new java.awt.Color(218, 165, 32)); // Change font color to white
        rollNoLabel.setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 11)); // Change font size and style

        batchLabel = new JLabel("Batch: " + batch);
        batchLabel.setBounds(50, 300, 200, 20);
        batchLabel.setForeground(new java.awt.Color(218, 165, 32)); // Change font color to white
        batchLabel.setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 11)); // Change font size and style

        //   currentSetIndex = loadCurrentSetIndex();

        imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(50, 50, 200, 200);
        frame.add(imageLabel);
        exitButton = new JButton("Exit");
        exitButton.setForeground(java.awt.Color.WHITE);
        exitButton.setBackground(java.awt.Color.RED); 
        exitButton.setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 24));
        exitButton.setFocusPainted(false);
        exitButton.setBounds(10, 10, 100, 35);

        exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    System.exit(0);
                }
            });

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);

        ImageIcon backgroundImage = new ImageIcon("Bck_img.jpg");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, screenSize.width, screenSize.height);
        // Scroll icons
        int totalIcons = iconNames.length; // Total number of icons based on the array size
        int gridSize = (int) Math.ceil(Math.sqrt(totalIcons)); // Size of the square grid
        int iconSize = 112; // Size of the scroll icon (50% smaller)

        JButton[] scrollButtons = new JButton[totalIcons];
        for (int i = 0; i < totalIcons; i++) {
            int index = i; // Create a final variable for index

            ImageIcon scrollIcon = new ImageIcon("Icn_img.png");
            //   ImageIcon scrollIcon1 = new ImageIcon("Sp_icn_img.png");
            Image scaledImage;

            scaledImage = scrollIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);

            scrollButtons[index] = new JButton(new ImageIcon(scaledImage));
            int col = i / gridSize;
            int row = i % gridSize;
            int x = 320 + col * (iconSize + 90);
            int y = 50 + row * (iconSize + 30);
            scrollButtons[index].setBounds(x, y, iconSize, iconSize);

            // Set the name of the button using the corresponding icon name
            scrollButtons[index].setText(iconNames[index]);
            scrollButtons[index].setForeground(new java.awt.Color(255, 255, 0));
            // Set the vertical alignment and center the text
            scrollButtons[index].setVerticalTextPosition(SwingConstants.CENTER);
            scrollButtons[index].setHorizontalTextPosition(SwingConstants.CENTER);
            if(i==totalIcons-1)
            {
                scrollButtons[index].setText(iconNames[index].toUpperCase());

            }
            scrollButtons[index].setText("<html><center>" + iconNames[index] + "</center></html>");

            scrollButtons[index].setVerticalTextPosition(SwingConstants.CENTER);
            scrollButtons[index].setHorizontalTextPosition(SwingConstants.CENTER);

            scrollButtons[index].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        {
                            String iconName = iconNames[index];
                            new DescriptionFrame(iconName);
                        }
                    }
                    //Practice on tictac toe game dynamic change
                });

            frame.add(scrollButtons[index]); 
        }

        frame.add(nameLabel);
        frame.add(rollNoLabel);
        frame.add(batchLabel);
        frame.add(exitButton);
        frame.add(backgroundLabel);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setFocusable(true); // Ensure that the frame is focused to receive key events
        File imageFile = new File(s);
        if (imageFile.exists()) 
        {
            imageFile.delete();
        }
    }
}

/*
 * The Class MannualForJava is third class and main hub for learing and testing the knowledge of the students
 * This Class will contain the topics of the JAVA language and  quiz app for the students to test their knowledge on JAVA language
 * The topics will in from of buttons and the student has to click on the button to open the topic 
 * After clicking the topic button description of the topic will open in the new window with buttons to video weblink and example code
 */

class MannualForJava  {

    public static boolean b = false;
    JFrame frame;
    JLabel backgroundLabel;

    JLabel nameLabel;
    JLabel rollNoLabel;
    JLabel batchLabel;

    String[] iconNames = {

            "ARRAYS",
            "CLASS",
            "COLLECTION FRAMEWORK",
            "CONSTRUCTOR",
            "DATA ABSTRACTION",
            "ENCAPSULATION",
            "ENUM",
            "EXCEPTIONS",
            "FILE HANDLING",
            "FLOW CONTROL",
            "GUI",
            "INHERITANCE",
            "INTERFACE",
            "OBJECT",
            "OPERATORS",
            "PACKAGE",
            "POLYMORPHISM",
            "STRING",
            "VARIABLES",
        };
    JLabel imageLabel;
    public MannualForJava(String name, String rollNo, String batch) {
        // Constructor for the MannualForJava class to initialize the name, rollNo and batch variables
        // This constructor will initialize the name, rollNo and batch variables with the values passed as parameters from TwoButtonsFrame class
        // This constructor will also initialize the iconNames array with the names of the topics of the JAVA language

        frame = new JFrame("Mannual");
        frame.setUndecorated(true);
        JButton exitButton = new JButton("Exit");
        ReconstructImages bv = new ReconstructImages();
        String s = bv.main5(null,rollNo);
        //system.out.println(s);
        ImageIcon imageIcon = new ImageIcon(s); // Replace with the actual image path
        Image scaledImage2 = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(scaledImage2);
        imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(50, 50, 200, 200);

        // Create JLabels for name, roll number, and batch using the provided arguments
        nameLabel = new JLabel("        Name:         " + name);
        nameLabel.setBounds(50, 260, 200, 20);
        nameLabel.setForeground(new java.awt.Color(218, 165, 32)); // Change font color to white
        nameLabel.setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 15)); // Change font size and style

        rollNoLabel = new JLabel("       Roll No:      " + rollNo);
        rollNoLabel.setBounds(50, 280, 200, 20);
        rollNoLabel.setForeground(new java.awt.Color(218, 165, 32)); // Change font color to white
        rollNoLabel.setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 15)); // Change font size and style
        
        batchLabel = new JLabel("       Batch:         " + batch);
        batchLabel.setBounds(50, 300, 200, 20);
        batchLabel.setForeground(new java.awt.Color(218, 165, 32)); // Change font color to white
        batchLabel.setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 15)); // Change font size and style

        //   currentSetIndex = loadCurrentSetIndex();

        imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(50, 50, 200, 200);
        frame.add(imageLabel);
        exitButton = new JButton("Exit");
        exitButton.setForeground(java.awt.Color.WHITE);
        exitButton.setBackground(java.awt.Color.RED); // Set background color to red
        exitButton.setFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 24));
        exitButton.setFocusPainted(false);
        exitButton.setBounds(10, 10, 100, 35);

        exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    System.exit(0);
                }
            });

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);

        // Background images
        ImageIcon backgroundImage = new ImageIcon("Old_Mannual_bck_img.png");
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, screenSize.width, screenSize.height);
        // Scroll icons
        int totalIcons = iconNames.length; // Total number of icons based on the array size
        int gridSize = (int) Math.ceil(Math.sqrt(totalIcons)); // Size of the square grid
        int iconSize = 112; // Size of the scroll icon (50% smaller)

        JButton[] scrollButtons = new JButton[totalIcons];
        for (int i = 0; i < totalIcons; i++) {
            int index = i; // Create a final variable for index

            ImageIcon scrollIcon = new ImageIcon("Old_Icn_img.png");

            Image scaledImage;

            scaledImage = scrollIcon.getImage().getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);

            scrollButtons[index] = new JButton(new ImageIcon(scaledImage));
            int col = i / gridSize;
            int row = i % gridSize;
            int x = 320 + col * (iconSize + 90);
            int y = 50 + row * (iconSize + 30);
            scrollButtons[index].setBounds(x, y, iconSize, iconSize);

            // Set the name of the button using the corresponding icon name
            scrollButtons[index].setText(iconNames[index]);
            scrollButtons[index].setForeground(java.awt.Color.BLUE);
            // Set the vertical alignment and center the text
            scrollButtons[index].setVerticalTextPosition(SwingConstants.CENTER);
            scrollButtons[index].setHorizontalTextPosition(SwingConstants.CENTER);
            if(i==totalIcons-1)
            {
                scrollButtons[index].setText(iconNames[index].toUpperCase());

            }
            scrollButtons[index].setText("<html><center>" + iconNames[index] + "</center></html>");

            scrollButtons[index].setVerticalTextPosition(SwingConstants.CENTER);
            scrollButtons[index].setHorizontalTextPosition(SwingConstants.CENTER);

            scrollButtons[index].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        {
                            String iconName = iconNames[index];
                            new DescriptionFrameForJava(iconName);
                        }
                    }
                });

            frame.add(scrollButtons[index]); 
        }

        frame.add(nameLabel);
        frame.add(rollNoLabel);
        frame.add(batchLabel);
        frame.add(exitButton);
        frame.add(backgroundLabel);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setFocusable(true); 
        
        File imageFile = new File(s);
        if (imageFile.exists()) 
        {
            imageFile.delete();
        }
    }
}

/*
 * The DescriptionFrame Class is the biggest class in the whole project this class links and all the components of the projects and topic content
 * This Class will contain the description of the topic and the example code of the topic
 * This Class will also contain the buttons to open the video link and the quiz app for the topic
 * This Class will also contain the button to close the description window and will start the quiz for the topic 
 */

class DescriptionFrame extends JFrame {
    JTextArea CdescriptionTextArea;
    JTextArea CsyntaxTextArea;
    JButton CexampleCodeButton;
    JButton CButton;
    JButton CvideoLinkButton ;
    JButton CcustomCloseButton;

    String[] CvideoURLs = {
            // Video URLs for each topic of the C language
            "https://www.youtube.com/embed/8zgZI8COHis?si=7PTfY4ADZBzY1pZN",
            "https://www.youtube.com/embed/I185qslVVdo?si=h_XSurvDM6QKdGuS",
            "https://www.youtube.com/embed/sisFATqbunA?si=1Zb7y8SUVAecKk4U",
            "https://www.youtube.com/embed/vunzFvaqbAM?si=vYCKX2taM7pakhTH",
            "https://www.youtube.com/embed/kP9cYyPWTtc?si=zNzv7FR2O5_he9FX",
            "https://www.youtube.com/embed/V7Qwxaj7JpI?si=RQvHHSpy2jhf_eSO",
            "https://www.youtube.com/embed/9HViGe1dN10?si=OSKdTCcEo7fYkbcj",
            "https://www.youtube.com/embed/V7Qwxaj7JpI?si=4gbv8UE3ApLp0GOK",
            "https://www.youtube.com/embed/e_IGjcevCQs?si=HjuAi4b_5Mkh1v0J",
            "https://www.youtube.com/embed/0pgMqk_lYIc?si=Nan2ViC6BWbZqioL",
            "https://www.youtube.com/embed/g-SlikhFAK8?si=Eq9XpS6jG07ygo96",
            "https://www.youtube.com/embed/ou-_Wo1P-tg?si=92Sp_9bKBFVSVn4Z",
            "https://www.youtube.com/embed/BWmf9GIxjtc?si=2zSW4Vmj5JojIeot",
            "https://www.youtube.com/embed/_dsTGaYmrFQ?si=0mTuzUQGmoF96C6o",
            "https://www.youtube.com/embed/_dsTGaYmrFQ?si=z-WFKUYgsIHnZFhn",
            "https://www.youtube.com/embed/_P0U9twtQpw?si=ZMXQVOr8OM-87OjU",
            "https://www.youtube.com/embed/VfQFej3xEk8?si=w8bkXsxwABgPbjJa",
            "https://www.youtube.com/embed/SwfNglWkeNg?si=2SBnyVPFiaHflub5",
            "https://www.youtube.com/embed/vu7bIcGRa6Q?si=TNpCucfW3YCNWKlM",
            "https://www.youtube.com/embed/D2QRd8Nwwq4?si=lfL-_Oe7DLGohZU2",
            "https://www.youtube.com/embed/_dsTGaYmrFQ?si=_vMN77BjQVCZ3cIK",
            "https://www.youtube.com/embed/_P0U9twtQpw?si=z61rJbOilMDA2pGH",
            "https://www.youtube.com/embed/BuRFzVtPbl0?si=MxLbZ4tU6ne_r0Fn",
            "https://www.youtube.com/embed/j8KbWdp4fVk?si=kZpB-IzGqI-C7AhK",
            "https://www.youtube.com/embed/j8KbWdp4fVk?si=iRgSXMg89oLOYevk"
        };
    String[] CiconNames = {
            // Names of the topics of the C language
            "DATA TYPES",
            "OPRATORS",
            "IF ELSE",
            "SWITCH-CASE",
            "TERNARY OPERATOR",
            "GO KEYWORD",
            "CONTINUE",
            "BREAK",
            "FOR LOOP",
            "WHILE LOOP",
            "DO WHILE LOOP",
            "ARRAYS",
            "STRINGS",
            "STRUCTURE",
            "UNION",
            "FUNCTIONS",
            "RECURSION",
            "POINTERS",
            "CALL BY VALUE",
            "CALL BY REFERENCE",
            "FILE MANEGMENT",
            "BINARY SEARCH",
            "CALOC",
            "MALOC",
            "RELLOC"
        };

    String[] CURLs = {
            // Web URLs for each topic of the C language 
            "https://www.programiz.com/c-programming/c-data-types",
            "https://www.programiz.com/c-programming/c-operators",
            "https://www.programiz.com/c-programming/c-if-else-statement",
            "https://www.programiz.com/c-programming/c-switch-case-statement",
            "https://www.programiz.com/c-programming/ternary-operator",
            "https://www.programiz.com/c-programming/c-goto-statement",
            "https://www.programiz.com/c-programming/c-break-continue-statement",
            "https://www.programiz.com/c-programming/c-break-continue-statement",
            "https://www.programiz.com/c-programming/c-for-loop",
            "https://www.programiz.com/c-programming/c-for-loop",
            "https://www.programiz.com/c-programming/c-for-loop",
            "https://www.programiz.com/c-programming/c-arrays",    
            "https://www.programiz.com/c-programming/c-strings",        
            "https://www.programiz.com/c-programming/c-structures",
            "https://www.programiz.com/c-programming/c-unions",
            "https://www.programiz.com/c-programming/c-functions",
            "https://www.programiz.com/c-programming/c-recursion",
            "https://www.programiz.com/c-programming/c-pointers",
            "https://www.javatpoint.com/call-by-value-and-call-by-reference-in-c",    
            "https://www.javatpoint.com/call-by-value-and-call-by-reference-in-c",
            "https://www.programiz.com/c-programming/c-file-input-output",
            "https://www.programiz.com/dsa/binary-search",
            "https://www.programiz.com/c-programming/c-dynamic-memory-allocation",
            "https://www.programiz.com/c-programming/c-dynamic-memory-allocation",
            "https://www.programiz.com/c-programming/c-dynamic-memory-allocation",
            "https://www.programiz.com/dsa/bubble-sort"
        };
    String[] CexampleURLs = {  
            // Example code for each topic of the C language 
            "dataTypes.c",
            "oprators.c",
            "allofifelse.c",
            "SwitchCase.c",
            "TernaryOpreator.c",
            "GoTo.c",
            "Continue.c",
            "Break.c",
            "ForLoop.c",
            "WhileLoop.c",
            "DoWhileLoop.c",
            "Arrays1DAnd2D.c",
            "Strings.c",
            "Structure.c",
            "Union.c",
            "functions.c",
            "Recursion.c",
            "pointer.c",
            "CallByValue.c",
            "CallByReferance.c",
            "FileManagement.c",
            "BinarySearch.c",
            "caloc.c",
            "maloc.c",
            "relloc.c",
        };

    String[] CdescriptionTexts = {
            //A breif Description of each topic of the C language 
            "Data types in C define the kind of value a variable can hold. They include int (integer), float (floating-point), char (character), and more complex types like struct and union",
            "Operators in C are symbols that perform operations on variables or values. Common operators include arithmetic (+, -, *, /), assignment (=), comparison (==, !=, etc.), and logical (&&, ||, !).",
            "The if-else statement in C allows for conditional execution of code. It evaluates a condition, and if it's true, the code inside the if block runs; otherwise, the code inside the else block (if present) runs.",
            "The switch-case statement in C is used for multi-way branching. It evaluates an expression and matches its value to various case labels. The code block associated with the matched case label is executed.",
            "The ternary operator (condition ? expr1 : expr2) is a shorthand for an if-else statement. It evaluates a condition and returns expr1 if true, and expr2 if false.",
            "The goto statement allows us to transfer control of the program to the specified label.",
            "The continue statement in C is used within loops to skip the current iteration and proceed with the next iteration.",
            "The break statement in C is used to exit from loops or switch statements. It terminates the loop or switch and transfers control to the statement following it.",
            "The for loop in C iterates a set of statements a specified number of times. Its syntax is for(initialization; condition; update) { // code }.",
            "The while loop in C repeatedly executes a block of code as long as a given condition is true. Its syntax is while(condition) { // code }.",
            "The do-while loop in C is similar to the while loop but ensures the code block runs at least once before checking the condition. Its syntax is do { // code } while(condition);.",
            "Arrays in C allow you to store multiple elements of the same data type in a contiguous memory block. Declared using type name[size], where type is the data type and size is the number of elements.",
            "Strings in C are represented as arrays of characters (char type). They're terminated by a null character ('\\0') and can be manipulated using various string functions.",
            "A structure in C is a composite data type that groups together variables of different types under a single name. Defined using struct keyword",
            "A union in C is a data structure that allows storing different data types in the same memory location. Unlike structures, only one member can hold a value at a time.",
            "Functions in C are blocks of code that can be called by name. They can take parameters, perform tasks, and return values. Defined using return_type name(parameters) { // code }.",
            "Recursion in C involves a function calling itself to solve a problem. It's often used for tasks that can be broken down into smaller, similar subtasks.",
            "Pointers in C hold memory addresses of another variables. They allow dynamic memory allocation and manipulation. Declared using type *ptr; where type is the data type.",
            "In C, function parameters are passed by value, meaning a copy of the parameter's value is passed to the function.",
            "File handling in C involves operations like reading from and writing to files. Functions like fopen, fclose, fread, and fwrite are used",
            "Binary search is an efficient algorithm to search for an element in a sorted array. It repeatedly divides the search interval in half.",
            "calloc is a function that allocates memory for an array of elements, initializes them to zero, and returns a pointer to the allocated memory",
            "malloc is a function that allocates a specified amount of memory and returns a pointer to the first byte of the allocated memory.",
            "realloc is a function used to resize previously allocated memory. It can be used to increase or decrease memory size.",
            "Sorting is the process of arranging elements in a specific order, often in ascending or descending order, based on a certain key or criteria. It's a fundamental operation in computer science and is used to organize data in a more structured and usable way. Sorting is essential for various applications, including searching, data analysis, and efficient data retrieval. it has 2 types Comparison-Based Sorting and Non-Comparison-Based Sorting "
        };

    String [] CSyntex ={
            //Syntex Description of each topic of the C language
            "int num = 10;<br>" + //
            "float pi = 3.14159;<br>" + //
            "char letter = 'A';",
            "int sum = a + b;<br>" + //
            "int quotient = x / y;<br>" + //
            "int isEqual = (value1 == value2);",
            "if (condition) {<br>" + //
            "    // code to execute if condition is true<br>" + //
            "} else {<br>" + //
            "    // code to execute if condition is false<br>" + //
            "}",
            "switch (expression) {<br>" + //
            "    case value1:<br>" + //
            "        // code for value1<br>" + //
            "        break;<br>" + //
            "    case value2:<br>" + //
            "        // code for value2<br>" + //
            "        break;<br>" + //
            "    default:<br>" + //
            "        // code if no case matches<br>" + //
            "}",
            "result = (condition) ? value_if_true : value_if_false;",
            "GO KEYWORD",
            "for (int i = 0; i < 10; i++) {<br>" + //
            "    if (i == 5) {<br>" + //
            "        continue; // skip iteration when i is 5<br>" + //
            "    }<br>" + //
            "    // code here<br>" + //
            "}",
            "for (int i = 0; i < 10; i++) {<br>" + //
            "    if (i == 5) {<br>" + //
            "        break; // exit the loop when i is 5<br>" + //
            "    }<br>" + //
            "    // code here<br>" + //
            "}",
            "for (int i = 0; i < 5; i++) {<br>" + //
            "    // code to execute in each iteration<br>" + //
            "}",
            "while (condition) {<br>" + //
            "    // code to execute while condition is true<br>" + //
            "}",
            "do {<br>" + //
            "    // code to execute at least once<br>" + //
            "} while (condition);",
            "int numbers[5] = {1, 2, 3, 4, 5};",
            "char greeting[] = \"{ Enter Your String Text here\" };",
            "struct Person {<br>" + //
            "    char name[50];//Data member of char type<br>" + //
            "    int age; //Data member of int data type<br>" + //
            "}",
            "union Data {<br>" + //
            "    int intValue;<br>" + //
            "    float floatValue;<br>" + //
            "    char stringValue[20];<br>" + //
            "};",
            "#include <stdio.h><br>" + //
            "<br>" + //
            "// Function declaration<br>" + //
            "int add(int a, int b);<br>" + //
            "<br>" + //
            "int main() {<br>" + //
            "int result = add(5, 3); // Function call<br>" + //
            "printf(\"Sum: %d\\n" + //
            "\", result);<br>" + //
            "return 0;<br>" + //
            "}<br>" + //
            "<br>" + //
            "// Function definition<br>" + //
            "int add(int a, int b) {<br>" + //
            "return a + b;<br>" + //
            "}",
            "int factorial(int n);<br>" + //
            "int factorial(int n) {<br>" + //
            "    if (n <= 1) {<br>" + //
            "        return 1;<br>" + //
            "    }<br>" + //
            "    return n * factorial(n - 1);<br>" + //
            "}",
            "dataType Var_Name = Value;<br>" + //
            "    dataType_Of_Pointer *ptrName; // Declare a pointer<br>" + //
            "    ptr = &num; // Assign the address of num to the pointer<br>" ,
            "#include <stdio.h><br>" + //
            "<br>" + //
            "void modifyValue(int x);<br>" + //
            "<br>" + //
            "int main() {<br>" + //
            "    int num = 10;<br>" + //
            "    modifyValue(num);<br>" + //
            "    printf(\"Value after modification: %d\\n" + //
            "\", num);<br>" + //
            "    return 0;<br>" + //
            "}<br>" + //
            "<br>" + //
            "void modifyValue(int x) {<br>" + //
            "    x = 20;<br>" + //
            "}",
            "void modifyValue(int *ptr);<br>\"",
            "FILE *file = fopen(\"example.txt\", \"w\"); // Open file for writing<br>" + //
            "    if (file == NULL) {<br>" + //
            "        printf(\"Error opening file.\\n" + //
            "\");<br>" + //
            "        return 1;<br>" + //
            "    }<br>" + //
            "    <br>" + //
            "    fprintf(file, \"Hello, File!\");<br>" + //
            "    fclose(file); // Close the file",
            "int binarySearch(int arr[], int size, int target) {<br>" + //
            "    int left = 0, right = size - 1;<br>" + //
            "    while (left <= right) {<br>" + //
            "        int mid = left + (right - left) / 2;<br>" + //
            "        if (arr[mid] == target) {<br>" + //
            "            return mid;<br>" + //
            "        }<br>" + //
            "        if (arr[mid] < target) {<br>" + //
            "            left = mid + 1;<br>" + //
            "        } else {<br>" + //
            "            right = mid - 1;<br>" + //
            "        }<br>" + //
            "    }<br>" + //
            "    return -1; // Not found<br>" + //
            "}",
            "int *numbers = (int *)calloc(5, sizeof(int));",
            "int *number = (int *)malloc(sizeof(int));",
            "int *newNumbers = (int *)realloc(oldNumbers, new_size * sizeof(int));<br>" ,
            "it depends of what sorting you will do "      
        };

    String[] CHardproblems={
            //Hard problems for each topic of the C language
            "Create a program that converts a given temperature in Fahrenheit to Celsius. The formula for conversion is: Celsius = (Fahrenheit - 32) * 5/9.",
            "Create a program that calculates the factorial of a given positive integer using a loop. Avoid using recursion.",
            "Create a program that simulates a simple login system. Ask the user for a username and password, and if they match predefined values, grant access; otherwise, deny access.",
            "Create a program that acts as a simple phone directory. Users can enter a contact name, and the program displays the corresponding phone number. Handle cases where the contact is not found.",
            "Create a program that compares three numbers and prints the largest one using the ternary operator.",
            "Create a program that takes an input string and reverses it using the go keyword without using built-in reverse functions.",
            "Create a program that prints the first 10 prime numbers using a while loop and the continue statement.",
            "Create a program that takes a number from the user and checks if it's prime using a do-while loop and the break statement.",
            "Create a program that generates the Fibonacci sequence up to a given number using a for loop.",
            "Create a program that finds the largest prime factor of a given number using a while loop.",
            "Create a program that simulates a basic quiz game. Ask the user questions in a loop, and at the end of the quiz, display their score.",
            "Create a program that merges two sorted arrays into a single sorted array.",
            "Create a program that finds the longest common subsequence of two strings using dynamic programming.",
            "Create a program that simulates a basic banking system using structures. Define a structure for a bank account with attributes like account number, account holder name, and balance. Allow users to deposit, withdraw, and check balance.",
            "Create a program that defines a union named Data to represent different types of data (integer, float, boolean). Implement a function that takes a data type identifier and a value and sets the value in the union based on the data type.",
            "Create a program that converts a decimal number to binary using a recursive function.",
            "Create a program that calculates the nth term of the Fibonacci sequence using recursion and memoization.",
            "Create a program that dynamically allocates memory for an integer array using pointers and fills it with user-input values.",
            "Create a program that takes two integers from the user and finds their greatest common divisor (GCD) using call by value.",
            "Create a program that swaps the values of two variables using call by reference.",
            "Create a program that reads data from one Text/Binary file, modifies it, and writes it to another Text/Binary file.",
            "Create a program that searches for a word in a sorted list of words using binary search.",
            "Create a program that reads a sentence from the user and dynamically allocates memory for a string using calloc, then reverses the string and prints it.",
            "Create a program that simulates a basic contact management system. Allow users to add, delete, and search contacts using dynamically allocated memory for contact details.",
            "Create a program that reads numbers from the user and dynamically allocates memory for an array using malloc. Allow the user to add more numbers to the array using realloc."
        };

    String[] CMediumProblems={
            //Medium problems for each topic of the C language
            "Implement a program that calculates the area of a rectangle given its length and width. Ensure that the length and width are positive integers.",
            "Implement a calculator program that takes two numbers and an operator (+, -, *, /) as input and performs the corresponding arithmetic operation.",
            "Implement a program that determines whether a year entered by the user is a leap year or not. A leap year is divisible by 4 and not divisible by 100, or it's divisible by 400.",
            "Implement a simple calculator that takes two numbers and an operator (+, -, *, /) from the user. Use a switch-case statement to perform the operation.",
            "Implement a program that checks if a given year is a leap year or not using the ternary operator.",
            "Implement a program that calculates the factorial of a given positive integer using a for loop and the go keyword.",
            "Implement a program that prints all even numbers from 1 to 20 using a for loop and the continue statement.",
            "Implement a program that calculates the sum of all numbers divisible by 7 from 1 to 200 using a while loop and the break statement.",
            "Implement a program that prints the multiplication table of a given number using a nested for loop.",
            "Implement a program that checks if a given number is a palindrome or not using a while loop.",
            "Implement a program that calculates the average of a set of numbers entered by the user using a do-while loop.",
            "Implement a program to find the second largest element in an array of integers.",
            "Implement a program that checks if a given string is a palindrome or not.",
            "Implement a program that defines a structure named Book with attributes title, author, and publicationYear. Create an array of Book structures and sort them based on publication year",
            "Implement a program that defines a union named Variant to store different types of values (integer, float, string). Create functions to set and retrieve values from the union.",
            "Implement a program that calculates the nth Fibonacci number using a recursive function.",
            "Implement a program to solve the Tower of Hanoi puzzle using recursion",
            "Implement a program that swaps the values of two integer variables using pointersRS",
            "Implement a program that calculates the factorial of a number using call by value.",
            "Implement a program that sorts an array of integers using call by reference.",
            "Implement a program that takes user input and writes it to a text file.",
            "Implement a program that finds the square root of a number using binary search.",
            "Implement a program that dynamically allocates memory for a matrix (2D array) of integers using calloc and performs matrix multiplication.",
            "Implement a program that dynamically allocates memory for an array of strings using malloc and displays them in reverse order.",
            "Implement a program that takes a sentence from the user and dynamically allocates memory for a string using malloc. Then, allow the user to add more characters to the string using realloc"
        };

    String[] CEasyProblems={
            //Easy problems for each topic of the C language
            "Write a program that takes user input for an integer, a float, and a character. Print these values along with their data types.",
            "Write a program to calculate the sum and average of two numbers entered by the user.",
            "Write a program that checks if a number entered by the user is even or odd and displays an appropriate message.",
            "Write a program that takes a number from the user (1-7) and displays the corresponding day of the week.",
            "Write a program that takes an integer from the user and prints whether it's positive, negative, or zero using the ternary operator.",
            "Write a program that prints \"Hello, World!\" using the go keyword.",
            "Write a program that prints all even numbers from 1 to 20 using a for loop and the continue statement.",
            "Write a program that prints all odd numbers from 1 to 15 using a while loop and the break statement.",
            "Write a program that calculates the sum of all numbers from 1 to 100 using a for loop.",
            "Write a program that calculates the factorial of a given positive integer using a while loop.",
            "Write a program that takes numbers from the user until they enter a negative number. Calculate and print the sum of all positive numbers.",
            "Write a program to find the sum of all elements in an integer array.",
            "Write a program that counts the number of vowels in a given string.",
            "Write a program that defines a structure named Student with attributes name, age, and rollNumber. Create an array of 5 Student structures and display their details.",
            "Write a program that defines a union named Value with attributes intValue, floatValue, and charValue. Input a value of any data type and display its content based on the selected data type.",
            "Write a program that calculates the factorial of a given positive integer using a function.",
            "Write a program to calculate the sum of digits of a given positive integer using recursion.",
            "Write a program that demonstrates how to declare and use a pointer to an integer variable.",
            "Write a program that swaps the values of two variables using call by value.",
            "Write a program that swaps the values of two variables using call by reference.",
            "Write a program to read the contents of a text file and display them on the console.",
            "Write a program that performs a binary search on a sorted array of integers to find a specific element.",
            "Write a program that dynamically allocates memory for an integer array using calloc and fills it with user-input values.",
            "Write a program that dynamically allocates memory for an integer variable using malloc and assigns it a value.",
            "Write a program that dynamically allocates memory for an integer array using malloc and later resizes it using realloc."
        };

    public DescriptionFrame(String CiconName) {
        //Constructor of the DescriptionFrame class
        setTitle(CiconName + " Description");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null); // Center the frame on the screen
        setUndecorated(false); // Set the frame as undecorated (no title bar)

        // Create the components
        CdescriptionTextArea = new JTextArea();
        CsyntaxTextArea = new JTextArea();
        CexampleCodeButton = new JButton("Example Code");
        CButton = new JButton("Web Link");
        CvideoLinkButton = new JButton("Video Link");
        JButton CcreateFileButton = new JButton("Create .C File");

        // Set the layout
        setLayout(new BorderLayout());

        JPanel CdescriptionPanel = new JPanel(new BorderLayout());
        JEditorPane CdescriptionEditorPane = new JEditorPane();
        CdescriptionEditorPane.setContentType("text/html"); 
        CdescriptionEditorPane.setEditable(false); 
        CdescriptionPanel.add(new JScrollPane(CdescriptionEditorPane), BorderLayout.CENTER);

        int Cindex = Arrays.asList(CiconNames).indexOf(CiconName);
      
        String CformattedText = "<html><p style='font-size: 20px; font-family: Arial, sans-serif; color: Black;'>"
            + CdescriptionTexts[Cindex] + "</p><br><p style='font-size: 16px; font-family: Arial, sans-serif; font-style: Bold; color : Black'>"
            + "Syntax: <br>" +"</p><br><p style='font-size: 14px; font-family: Arial, sans-serif; font-style: italic,Bold; color : red'>"+ CSyntex[Cindex] +
            "</p><br><p style='font-size: 16px; font-family: Arial, sans-serif; font-style: Bold; color : red'>"+"Problems: <br>"+
            "</p><p style='font-size: 11px; font-family: Arial, sans-serif; font-style: Bold; color : black'>"+"Easy : "+ CEasyProblems[Cindex]+
            "</p><p style='font-size: 11px; font-family: Arial, sans-serif; font-style: Bold; color : green'>"+"<br>Medium : "+ CMediumProblems[Cindex]+
            "</p><p style='font-size: 11px; font-family: Arial, sans-serif; font-style: Bold; color : red'>"+"<br>Hard : "+ CHardproblems[Cindex]+ "</p></html>";

        CdescriptionEditorPane.setText(CformattedText);
       
        add(CdescriptionPanel, BorderLayout.CENTER);

        add(CdescriptionPanel, BorderLayout.CENTER);
        CdescriptionTextArea.setEditable(false);
        CdescriptionTextArea.setLineWrap(true); 
        CdescriptionTextArea.setWrapStyleWord(true);

        JPanel CbottomPanel = new JPanel(new BorderLayout());

        JPanel CbuttonsPanel = new JPanel();
        CbuttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0)); 
        CbuttonsPanel.add(CButton);
        CbuttonsPanel.add(CexampleCodeButton);
        CbuttonsPanel.add(CvideoLinkButton);
        CbottomPanel.add(CbuttonsPanel, BorderLayout.SOUTH);
        CbuttonsPanel.add(CcreateFileButton);
        add(CbottomPanel, BorderLayout.SOUTH);

        CexampleCodeButton.setBackground(new java.awt.Color(144, 238, 144)); 
        CButton.setBackground(java.awt.Color.CYAN);
        CvideoLinkButton.setBackground(new java.awt.Color(135, 206, 235)); 
        CcreateFileButton.setBackground(new java.awt.Color(64, 224, 208));

        CexampleCodeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String CexampleFilePath = getCExampleURL(CiconName);
                    if (!CexampleFilePath.isEmpty()) {
                        File CexampleFile = new File(CexampleFilePath);

                        try {
                            Desktop.getDesktop().open(CexampleFile);

                        } catch (IOException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(
                                DescriptionFrame.this, "An error occurred while opening the file.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

        CvideoLinkButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String CvideoURL = CgetVideoURL(CiconName);
                    if (!CvideoURL.isEmpty()) {
                        //system.out.println(CvideoURL);
                        Temp_vidview.main8(new String[]{CvideoURL});

                    }
                }
            });

        CButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String CURL = getCURL(CiconName);
                    if (!CURL.isEmpty()) {
                        Temp_webview.main6(new String[]{CURL});

                    }
                }
            });

        setVisible(true);

        CcreateFileButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JPanel CcodePanel = new JPanel(new BorderLayout());
                    RSyntaxTextArea CcodeTextArea = new RSyntaxTextArea();
                    CcodeTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
                    CcodeTextArea.setCodeFoldingEnabled(true);

                    JButton CcompileAndRunButton = new JButton("Compile and Run");
                    CcompileAndRunButton.setPreferredSize(new Dimension(150, 30)); // Set the button size
                    CcompileAndRunButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                CcompileAndRunCode(CcodeTextArea.getText());
                            }
                        });
                    // Create a new JFrame for the code editor
                    JFrame CcodeEditorFrame = new JFrame("C Code Editor");
                    CcodeEditorFrame.setSize(600, 400);
                    CcodeEditorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    CcodeEditorFrame.setLocationRelativeTo(null);
                    JButton saveFileButton = new JButton("Save .C File");
                    saveFileButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JFileChooser CfileChooser = new JFileChooser();
                                int Coption = CfileChooser.showSaveDialog(CcodeEditorFrame);

                                if (Coption == JFileChooser.APPROVE_OPTION) {
                                    File CselectedFile = CfileChooser.getSelectedFile();
                                    if (!CselectedFile.getName().endsWith(".c")) {
                                        CselectedFile = new File(CselectedFile.getAbsolutePath() + ".c");
                                    }

                                    String CselectedCode = CcodeTextArea.getText();

                                    try (FileWriter Cwriter = new FileWriter(CselectedFile)) {
                                        Cwriter.write(CselectedCode);

                                        JOptionPane.showMessageDialog(
                                            CcodeEditorFrame, "File saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(
                                            CcodeEditorFrame, "An error occurred while saving the file.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                        });

                    CcodePanel.add(new RTextScrollPane(CcodeTextArea), BorderLayout.CENTER);
                    CcodePanel.add(saveFileButton, BorderLayout.SOUTH);
                    CcodePanel.add(CcompileAndRunButton);

                    CcodeEditorFrame.getContentPane().add(CcodePanel);
                    CcodePanel.add(new RTextScrollPane(CcodeTextArea), BorderLayout.CENTER);
                    JPanel CbuttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)); 
                    CbuttonPanel.add(saveFileButton);
                    CbuttonPanel.add(CcompileAndRunButton);
                    CcodePanel.add(CbuttonPanel, BorderLayout.SOUTH); 

                    if (Cindex >= 0 && Cindex < CSyntex.length) {
                        CcodeTextArea.setText("//"+CHardproblems[Cindex]+"\n //"+CMediumProblems[Cindex]+"\n //"+CEasyProblems[Cindex]);
                    }

                    CcodeEditorFrame.setVisible(true);
                }
            });

        addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {

                    //system.out.println("Window closed. Do something here.");
                    MCQQuizApp Cquiz = new MCQQuizApp(Cindex);
                    Cquiz.startQuiz();
                }
            });

        setVisible(true);
    }

    private void CcompileAndRunCode(String code) {
        // Compile and run the code using the command line and cmd.exe (Windows) or Terminal (Mac/Linux) 
        try {
            File CtempFile = File.createTempFile("temp", ".c");
            CtempFile.deleteOnExit();

            try (FileWriter Cwriter = new FileWriter(CtempFile)) {
                Cwriter.write(code);
            }

            StringBuilder CoutputBuffer = new StringBuilder();

            ProcessBuilder CcompileProcessBuilder = new ProcessBuilder("gcc", CtempFile.getAbsolutePath(), "-o", "output");
            CcompileProcessBuilder.redirectErrorStream(true);

            Process CcompileProcess = CcompileProcessBuilder.start();
            int CcompileExitCode = CcompileProcess.waitFor();

            try (BufferedReader Creader = new BufferedReader(new InputStreamReader(CcompileProcess.getInputStream()))) {
                String Cline;
                while ((Cline = Creader.readLine()) != null) {
                    CoutputBuffer.append(Cline).append('\n');
                }
            }

            if (CcompileExitCode == 0) {

                ProcessBuilder CrunProcessBuilder = new ProcessBuilder("cmd.exe", "/c", "start", "cmd.exe", "/K", "output");
                CrunProcessBuilder.redirectErrorStream(true);
                Process CrunProcess = CrunProcessBuilder.start();

                CrunProcess.waitFor();
            } else {

                String compilationErrorMessage = "Compilation failed. Output:\n" + CoutputBuffer.toString();
                JOptionPane.showMessageDialog(null, compilationErrorMessage, "Compilation Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                null, "An error occurred while compiling and running the code.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String CgetVideoURL(String CiconName) {
        // Get the video URL for the given topic
        int Cindex = Arrays.asList(CiconNames).indexOf(CiconName);
        if (Cindex >= 0 && Cindex < CvideoURLs.length) {
            return CvideoURLs[Cindex];
        }
        return "";
    }

    private String getCURL(String CiconName) {
        // Get the web URL for the given topic
        int Cindex = Arrays.asList(CiconNames).indexOf(CiconName);
        if (Cindex >= 0 && Cindex < CURLs.length) {

            return CURLs[Cindex];
        }
        return "";
    }

    private String getCExampleURL(String CiconName) {
        // Get the example file path for the given topic
        int Cindex = Arrays.asList(CiconNames).indexOf(CiconName);
        if (Cindex >= 0 && Cindex < CexampleURLs.length) {
            return CexampleURLs[Cindex];
        }
        return "";
    }

}

/*
 * The DescriptionFrameForJava Class is the biggest class in the whole project this class links and all the components of the projects and topic content
 * This Class will contain the description of the topic and the example code of the topic
 * This Class will also contain the buttons to open the video link and the quiz app for the topic
 * This Class will also contain the button to close the description window and will start the quiz for the topic 
 */

class DescriptionFrameForJava extends JFrame{
    JTextArea descriptionTextArea;
    JTextArea syntaxTextArea;
    JButton exampleCodeButton;
    JButton javadocButton;
    JButton videoLinkButton ;
    JButton customCloseButton;
    public int buttonsClickedCount=0;

    String[] videoURLs = {
            //Video URLs for each topic of the Java language
            "https://www.youtube.com/embed/BuNkybyjG7s?si=h_19WLoNlF5yGJDg",
            "https://www.youtube.com/embed/_Ylzm140wwY?si=K4wZsk4j6Xhs-A1U",
            "https://www.youtube.com/embed/QOES7VIl0Rc?si=OtNa4Z7UQ5Nd-vvT",
            "https://www.youtube.com/embed/oxX2zCsWgiE?si=TfgC8e_1P-kjRrWR",
            "https://www.youtube.com/embed/RHxIZAsa_MA?si=1s5XKwa8sr5wF-PM",
            "https://www.youtube.com/embed/qWhECkQ_SiE?si=_aY_SCUUYA-MDnbv",
            "https://www.youtube.com/embed/EVHzs6rN8go?si=dYSALk_z4j6thoKJ",
            "https://www.youtube.com/embed/Ckbw00GtC04?si=kO9CWSIoR1QIrjNC",
            "https://www.youtube.com/embed/vgDZu3JrhYI?si=LXD03bbqnbLfyy3k",
            "https://www.youtube.com/embed/ukz1Qrxx2tg?si=wAPauwfU2yVJI_Jq",
            "https://www.youtube.com/embed/5o3fMLPY7qY?si=Yx3ehS8iw8I4iR7i",
            "https://www.youtube.com/embed/Q4uhiF6HYmw?si=iBaisF33srxg0Bog",
            "https://www.youtube.com/embed/paElXOzWcvI?si=GgezEa5bbqQA6-cS",
            "https://www.youtube.com/embed/0dDPQJOtjUg?si=S8XmLUy40J_GvtAM",
            "https://www.youtube.com/embed/3PwmWaHdojA?si=4DMKT1PdpqnbNLpf",
            "https://www.youtube.com/embed/LdhW9WqcT2c?si=2EfyJ2t93jH7guc4",
            "https://www.youtube.com/embed/YmHojdZu4tw?si=eVgTdFqHMvizokOu",
            "https://www.youtube.com/embed/GnyB2hCeXeM?si=LUg62CRQrsHeY7ak",
            "https://www.youtube.com/embed/8P6tOnO0DYg?si=UaWqnfdClfIU5Bjj",
        };
    String[] iconNames = {
        //Icon names for each topic of the Java language

            "ARRAYS",
            "CLASS",
            "COLLECTION FRAMEWORK",
            "CONSTRUCTOR",
            "DATA ABSTRACTION",
            "ENCAPSULATION",
            "ENUM",
            "EXCEPTIONS",
            "FILE HANDLING",
            "FLOW CONTROL",
            "GUI",
            "INHERITANCE",
            "INTERFACE",
            "OBJECT",
            "OPERATORS",
            "PACKAGE",
            "POLYMORPHISM",
            "STRING",
            "VARIABLES",
        };

    String[] javadocURLs = {
            //Javadoc URLs for each topic of the Java language

            "https://docs.oracle.com/javase/tutorial/java/nutsandbolts/arrays.html",
            "https://docs.oracle.com/javase/tutorial/java/javaOO/classes.html",
            "https://docs.oracle.com/javase/tutorial/collections/intro/index.html",
            "https://docs.oracle.com/javase/tutorial/reflect/member/ctor.html",
            "https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html",
            "https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html",
            "https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html",
            "https://docs.oracle.com/javase/tutorial/essential/exceptions/definition.html",
            "https://docs.oracle.com/javase/tutorial/essential/io/index.html",
            "https://docs.oracle.com/javase/tutorial/java/nutsandbolts/flow.html",
            "https://docs.oracle.com/javase/tutorial/uiswing/start/index.html",
            "https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html",//INHERITANCE
            "https://docs.oracle.com/javase/tutorial/java/concepts/interface.html",
            "https://docs.oracle.com/javase/tutorial/java/javaOO/objects.html",
            "https://docs.oracle.com/javase/tutorial/java/nutsandbolts/operators.html",
            "https://docs.oracle.com/javase/tutorial/java/package/index.html",
            "https://docs.oracle.com/javase/tutorial/java/IandI/polymorphism.html",
            "https://docs.oracle.com/javase/tutorial/java/data/strings.html",
            "https://docs.oracle.com/javase/tutorial/java/nutsandbolts/variables.html"
        };
    String[] exampleURLs = {   
            //Example Codes for each topic of the Java language
            "ArraysExample.java",
            "Car.java",
            "CollectionFrameworkExample.java",
            "Constructor1.java",
            "DataAbstractionExample.java",
            "Person.java",
            "EnumExample.java",
            "ExceptionExample.java",
            "FileHandlingExample.java",
            "FlowControlExample.java",
            "Traffic_lights.java",
            "InheritanceDemo.java",
            "InterfaceExample.java",
            "ObjectExample.java",
            "calculator.java",
            "MyClass.java",
            "Message.java",
            "String_funcs.java",
            "VariablesExample.java"

        };

    String[] descriptionTexts = {
            //A breif Description texts for each topic of the Java language
            "Arrays are a collection of variabes joined together by the reference of index numbers",
            "Class is an abstract concept that has no real data holding capacity but is considered to be a bunch of mehtods, objects etc.",
            "Collection Framework is a group of special data holding members that can help ease data handling functions.",
            "Constructor is the method that runs by itself and at the very beginning of the code, but after the main method.",
            "Data Abstraction is the concept of hiding the unwanted or complex snippet of code to maintain ease of low and make it user-friendly.",
            "Encapsulation is the concept of hiding sensitive data from unauthorized users by the use of access modifiers.",
            "Enum is the collection of variables that cannot be further modified by any other class or method.",
            "Exceptions are the special cases that are generated when a syntatical or logical error is made in the program.",
            "File Handling is the concept of creatibg, modifying or deleting: xlsx, docx, txt or pdf files, using Java.",
            "Flow control is the logical part of any code that uses conditions and cases to link the user's decision to the outcomes.",
            "GUI is the tool that helps to make any program interactive and graphically enriched.",
            "Inheritance is the concept where ,a sub-class if you will, derives methods(and variables too) from say a parent class and either uses it or modifies it. ",
            "Interface is the tool that can help to connect the user with the methods in the program.",
            "Object is something that has state and behaviour, for eg: a basket of 4 bananas. The basket being object, has state=4 and behaviour = banana.",
            "Operators are the tools that can be used to modify and compare the values of literals or any other data holders. ",
            "Package is a closed library that helps to enclose a bunch of programs and provides increased data protection and accessibility.",
            "Polymorphism is the concept of re-iterating a method or re-defining it but with the same name so as to decrease code-complexity.",
            "String is the literal that contains an array of characters or the language the we speak.",
            "Variables are the memebrs of OOP that hold data and can be differentiated by their types for eg: int, doube, boolean, etc.",
            "Now we will see some interactive bits of programs."
        };

    String [] Syntex ={
            //Syntex for each topic of the Java language
            "dataType[] arrayName = new dataType[size];",
            "class ClassName { /* class members and methods */ }",
            "import java.util.Collection;\nimport java.util.ArrayList;\n\nCollection<dataType> collectionName = new ArrayList<>();",
            "class ClassName {\n    ClassName(parameters) { /* constructor body */ }\n}",
            "abstract class AbstractClassName {\n    abstract dataType methodName(parameters);\n}",
            "class ClassName {\n    private dataType variableName;\n    public dataType getVariable() { /* return the variable */ }\n    public void setVariable(dataType value) { /* set the variable */ }\n}",
            "enum EnumName { VALUE1, VALUE2, ... }",
            "try {\n    // code that might throw an exception\n} catch (ExceptionType e) {\n    // handle the exception\n}",
            "try {\n    // code to open and handle the file\n} catch (IOException e) {\n    // handle file-related exception\n} finally {\n    // code to clean up resources\n}",
            "if (condition) {\n    // code to execute if condition is true\n} else {\n    // code to execute if condition is false\n}",
            "// GUI code using a library or framework",
            "class ChildClass extends ParentClass { /* additional members and methods */ }",
            "interface InterfaceName {\n    // method declarations\n}",
            "class ClassName {\n    // class members and methods\n}",
            "int sum = a + b;\nint quotient = x / y;\nint isEqual = (value1 == value2);",
            "package packageName;",
            "class ParentClass {\n    void commonMethod() { /* code */ }\n}\nclass ChildClass extends ParentClass {\n    void commonMethod() { /* overridden code */ }\n}",
            "String str = \"Hello, world!\";",
            "dataType variableName = value;", 
        };

    String[] Hardproblems={
            //Hard problems for each topic of the Java language

            "Implement a parallel sorting algorithm using multi-threading to improve the performance of sorting large arrays.",
            "Design a comprehensive framework for generating and managing reports in a business application, using dynamic class loading.",
            "Build a distributed cache system using Java's collection classes, ensuring data consistency and efficient storage across nodes.",
            "Develop a dependency injection container that dynamically manages object creation and initialization based on class annotations.",
            "Create an advanced database abstraction layer that supports multiple database systems and optimizes query performance.",
            "Design a secure cryptographic library in Java, encapsulating complex encryption and decryption algorithms for various use cases.",
            "Build a configuration management system using enums, allowing users to define and manage application configurations easily.",
            "Develop a distributed fault-tolerant system using exceptions to handle failures and ensure continuous operation across nodes.",
            "Create a distributed file synchronization system using Java's file handling capabilities, ensuring data consistency across nodes.",
            "Implement a complex workflow management system using flow control mechanisms, allowing users to define intricate processes.",
            "Design an advanced 3D graphics application using Java's GUI libraries, incorporating realistic rendering and user interactions.",
            "Build a simulation framework for a complex ecosystem using inheritance, modeling diverse species and their ecological interactions.",
            "Develop a pluggable architecture for a game engine using interfaces, allowing third-party developers to create custom game components.",
            "Create a distributed object-oriented simulation environment where objects can interact and communicate across networked nodes.",
            "Design a custom expression evaluation engine that handles complex mathematical expressions, including user-defined operators.",
            "Implement a modular microservices architecture using Java's package system, with each package representing an independent service.",
            "Build an AI-powered virtual pet simulation with polymorphic behaviors, allowing virtual pets to learn from user interactions.",
            "Develop a natural language processing application that can generate coherent and contextually relevant paragraphs of text.",
            "Design a self-modifying program that dynamically adjusts variable values based on environmental conditions and user inputs."};

    String[] MediumProblems={ 
            //Medium problems for each topic of the Java language

            "Write a Java program to find the maximum and minimum elements in an array.",
            "Create a simple 'Person' class with attributes for name and age. Add methods to set and display these attributes.",
            "Implement a program to store a list of names in an ArrayList and display them.",
            "Design a class 'Rectangle' with attributes for length and width. Write a constructor to initialize these attributes.",
            "Develop a program to manage a library with a 'Book' class that contains attributes like title and author.",
            "Build a 'BankAccount' class with private balance and methods to deposit, withdraw, and display balance.",
            "Define an enum 'Days' for the days of the week. Write a program to print all the days using a loop.",
            "Create a program that asks the user for their age. Handle cases where the input is not a valid integer.",
            "Write a program that reads text from a file and copies it to another file.",
            "Implement a simple calculator program that takes two numbers and an operator (+, -, *, /) and displays the result.",
            "Design a basic calculator GUI application that allows users to perform arithmetic operations.",
            "Create a 'Vehicle' class and subclasses 'Car' and 'Bike'. Add methods like 'startEngine' specific to each subclass.",
            "Define an interface 'Shape' with a method 'calculateArea'. Implement it in classes like 'Circle' and 'Rectangle'.",
            "Write a Java program to create objects of a 'Student' class with attributes like name, age, and grade.",
            "Develop a program that calculates the area of a triangle using the base and height provided by the user.",
            "Organize a set of utility classes related to math operations into a package called 'math.utils'.",
            "Create a 'Shape' class with a method 'draw'. Implement subclasses 'Circle' and 'Square' to override the method.",
            "Write a program that takes a sentence and counts the occurrences of a specific word in it.",
            "Design a program that swaps the values of two variables without using a temporary variable."

        };

    String[] EasyProblems={
            //Easy problems for each topic of the Java language

            "Write a Java program that declares an array of integers and calculates their sum.",
            "Create a class called 'Person' with attributes for name and age. Display their information using methods.",
            "Implement an ArrayList to store a list of names. Display the names using a loop.",
            "Design a 'Car' class with a constructor to set its make and model. Display the car's information.",
            "Develop a 'BankAccount' class with balance and methods for deposit and withdrawal. Display the balance.",
            "Build a 'Student' class with private attributes for name and grade. Provide methods to access and display them.",
            "Create an enum 'Color' with values representing basic colors. Display all the colors using a loop.",
            "Write a program that asks the user for their age. Handle cases where the input is not a valid integer.",
            "Implement a program to read a text file and display its content on the console.",
            "Create a program that checks if a given number is positive, negative, or zero.",
            "Design a basic calculator GUI application that allows users to perform addition and subtraction.",
            "Develop a 'Vehicle' class and a subclass 'Car'. Display information about a car using inherited methods.",
            "Define an interface 'Shape' with a method 'calculateArea'. Implement it in a 'Circle' class.",
            "Write a Java program to create an object of a 'Book' class with attributes like title and author.",
            "Write a program that takes two numbers from the user and displays their sum.",
            "Organize a set of utility classes related to geometry into a package called 'geometry.utils'.",
            "Create a 'Shape' class with a method 'draw'. Implement subclasses 'Circle' and 'Rectangle' to override the method.",
            "Design a program that takes a name as input and displays a welcome message using string concatenation.",
            "Declare an integer variable to store your age and display it with a message."
        };

    public DescriptionFrameForJava(String iconName) {
        //Constructor for the DescriptionFrameForJava class that will create the frame for the description window and will add all the components to it

        setTitle(iconName + " Description");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 
        setUndecorated(false); 

        descriptionTextArea = new JTextArea();
        syntaxTextArea = new JTextArea();
        exampleCodeButton = new JButton("Example Code");
        javadocButton = new JButton("Web Link");
        videoLinkButton = new JButton("Video Link");
        JButton createFileButton = new JButton("Create .java File");

        setLayout(new BorderLayout());
     
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        JEditorPane descriptionEditorPane = new JEditorPane();
        descriptionEditorPane.setContentType("text/html");
        descriptionEditorPane.setEditable(false); 
        descriptionPanel.add(new JScrollPane(descriptionEditorPane), BorderLayout.CENTER);

        int index = Arrays.asList(iconNames).indexOf(iconName);

        // Set the formatted HTML text to the JEditorPane
        String formattedText = "<html><p style='font-size: 20px; font-family: Arial, sans-serif; color: Black;'>"
            + descriptionTexts[index] + "</p><br><p style='font-size: 16px; font-family: Arial, sans-serif; font-style: Bold; color : Black'>"
            + "Syntax: <br>" +"</p><br><p style='font-size: 14px; font-family: Arial, sans-serif; font-style: italic,Bold; color : red'>"+ Syntex[index] +
            "</p><br><p style='font-size: 16px; font-family: Arial, sans-serif; font-style: Bold; color : red'>"+"Problems: <br>"+
            "</p><p style='font-size: 11px; font-family: Arial, sans-serif; font-style: Bold; color : black'>"+"Easy : "+ EasyProblems[index]+
            "</p><p style='font-size: 11px; font-family: Arial, sans-serif; font-style: Bold; color : green'>"+"<br>Medium : "+ MediumProblems[index]+
            "</p><p style='font-size: 11px; font-family: Arial, sans-serif; font-style: Bold; color : red'>"+"<br>Hard : "+ Hardproblems[index]+ "</p></html>";

        descriptionEditorPane.setText(formattedText);
      
        add(descriptionPanel, BorderLayout.CENTER);

        add(descriptionPanel, BorderLayout.CENTER);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    //system.out.println("Uncomment following to open another window!");
                    e.getWindow().dispose();
                    //system.out.println("JFrame Closed!");
                 MCQQuizAppForJava MCQFORJAVA= new MCQQuizAppForJava(index);
                 MCQFORJAVA.startQuiz();
                }
            });

        descriptionTextArea.setEditable(false);
        descriptionTextArea.setLineWrap(true); 
        descriptionTextArea.setWrapStyleWord(true);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0)); 
        buttonsPanel.add(javadocButton);
        buttonsPanel.add(exampleCodeButton);
        buttonsPanel.add(videoLinkButton);
        bottomPanel.add(buttonsPanel, BorderLayout.SOUTH);
        bottomPanel.add(createFileButton);

        add(bottomPanel, BorderLayout.SOUTH);

        exampleCodeButton.setBackground(new java.awt.Color(144, 238, 144)); 
        javadocButton.setBackground(java.awt.Color.CYAN);
        videoLinkButton.setBackground(new java.awt.Color(135, 206, 235)); 
        createFileButton.setBackground(new java.awt.Color(64, 224, 208));

        exampleCodeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String exampleFilePath = getExampleURL(iconName);
                    if (!exampleFilePath.isEmpty()) {
                        File exampleFile = new File(exampleFilePath);

                        try {
                            Desktop.getDesktop().open(exampleFile);
                            buttonsClickedCount++; 

                        } catch (IOException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(
                                DescriptionFrameForJava.this, "An error occurred while opening the file.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });

        videoLinkButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String videoURL = getVideoURL(iconName);
                    if (!videoURL.isEmpty()) {
                        Temp_vidview.main8(new String[]{videoURL});

                    }
                }
            }); 

        javadocButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String javadocURL = getJavadocURL(iconName);

                    if (!javadocURL.isEmpty()) {
                        Temp_webview.main6(new String[]{javadocURL});
                    }
                }
            });

        setVisible(true);

createFileButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Replace this with your desired folder path
        String folderPath = "D:\\java_project\\final hai\\C_X\\canwritehere";
        String iconJavaFileName = iconName + ".java"; // Java file name based on iconName

        // Determine the path to the VS Code executable
        String vsCodePath = "C:\\Program Files\\Microsoft VS Code\\Code.exe"; // Replace with your actual VS Code path

        // Open the folder in VS Code
        List<String> vsCodeCommand = new ArrayList<>();
        vsCodeCommand.add(vsCodePath);
        vsCodeCommand.add(folderPath);

        ProcessBuilder vsCodeProcessBuilder = new ProcessBuilder(vsCodeCommand);

        // Create and write to the Java file for the selected iconName
        File iconJavaFile = new File(folderPath, iconJavaFileName);
        try (FileWriter writer = new FileWriter(iconJavaFile)) {
            writer.write("// Hard Problems:\n");
            for (String hardProblem : Hardproblems) {
                writer.write("// " + hardProblem + "\n");
            }
            writer.write("// Medium Problems:\n");
            for (String mediumProblem : MediumProblems) {
                writer.write("// " + mediumProblem + "\n");
            }
            writer.write("// Easy Problems:\n");
            for (String easyProblem : EasyProblems) {
                writer.write("// " + easyProblem + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                null, "An error occurred while creating the Java file.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            vsCodeProcessBuilder.start();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                null, "An error occurred while opening in VS Code.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
});
    }

    private String getVideoURL(String iconName) {
        //
        int index = Arrays.asList(iconNames).indexOf(iconName);
        if (index >= 0 && index < videoURLs.length) {
            return videoURLs[index];
        }
        return "";
    }

    // private void openURL(String url) {
    //     try {
    //         Desktop.getDesktop().browse(new URI(url));
    //     } catch (IOException | URISyntaxException ex) {
    //         ex.printStackTrace();
    //     }
    // }

    private String getJavadocURL(String iconName) {
        // Get the Javadoc URL for the given topic
        int index = Arrays.asList(iconNames).indexOf(iconName);
        if (index >= 0 && index < javadocURLs.length) {
            return javadocURLs[index];
        }
        return "";
    }

    private String getExampleURL(String iconName) {
        // Get the example file path for the given topic

        int index = Arrays.asList(iconNames).indexOf(iconName);
        if (index >= 0 && index < exampleURLs.length) {
            return exampleURLs[index];
        }
        return "";
    }

}