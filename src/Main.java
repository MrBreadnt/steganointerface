import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JFileChooser fileChooser = new JFileChooser();
        JFrame mainFrame = new JFrame("Steganography");

        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        JPanel textPanel = new JPanel();
        JPanel imagePanel = new JPanel();
        textPanel.setLayout(null);

        JLabel image = new JLabel();
        image.setBounds(460, 50, 500, 500);
        textPanel.add(image);
        JLabel label = new JLabel("Text:");
        label.setBounds(50, 20, 100, 30);
        textPanel.add(label);
        JTextField text = new JTextField("Text");
        text.setBounds(50, 50, 400, 30);
        label = new JLabel("Key:");
        label.setBounds(50, 90, 100, 30);
        textPanel.add(label);
        JTextField key = new JTextField("Key");
        key.setBounds(50, 120, 100, 30);
        label = new JLabel("Image:");
        label.setBounds(50, 150, 100, 30);
        textPanel.add(label);
        JButton getFileButton = new JButton("Image");
        getFileButton.setBounds(50, 180, 100, 30);
        getFileButton.addActionListener(e -> {
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser.showOpenDialog(mainFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File picIn = fileChooser.getSelectedFile();
                BufferedImage img = null;
                try {
                    img = ImageIO.read(picIn);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                int max = Math.max(img.getWidth(), img.getHeight());
                ImageIcon icon = new ImageIcon(img.getScaledInstance(img.getWidth() * 500 / max, img.getHeight() * 500 / max, 0));
                image.setIcon(icon);
            }
        });
        textPanel.add(getFileButton);
        JButton encryptButton = new JButton("Encrypt");
        encryptButton.setBounds(50, 250, 100, 30);
        encryptButton.addActionListener(e -> {
            String inputFile = String.valueOf(fileChooser.getSelectedFile());
            int result = fileChooser.showSaveDialog(mainFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                Steganography.encodeText(text.getText(), key.getText(), inputFile, String.valueOf(fileChooser.getSelectedFile()));
            }
        });
        textPanel.add(encryptButton);
        JButton decryptButton = new JButton("Decrypt");
        decryptButton.setBounds(50, 280, 100, 30);
        decryptButton.addActionListener(e -> {
            String inputFile = String.valueOf(fileChooser.getSelectedFile());
            text.setText(Steganography.decodeText(inputFile, key.getText()));
        });
        textPanel.add(decryptButton);

        textPanel.add(text);
        textPanel.add(key);

        tabs.addTab("Text", textPanel);

        imagePanel.setLayout(null);

        String imageOut = "";
        String imageIn = "";

        JFileChooser fileChooser1 = new JFileChooser(), fileChooser2 = new JFileChooser(), fileChooser3 = new JFileChooser();

        JLabel image2 = new JLabel();
        image2.setBounds(460, 50, 500, 500);
        imagePanel.add(image2);
        label = new JLabel("Image Out:");
        label.setBounds(50, 90, 100, 30);
        imagePanel.add(label);
        JButton getFile1Button = new JButton("Image Out");
        getFile1Button.setBounds(50, 120, 100, 30);
        getFile1Button.addActionListener(e -> {
            fileChooser1.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser1.showOpenDialog(mainFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File picIn = fileChooser1.getSelectedFile();
                BufferedImage img = null;
                try {
                    img = ImageIO.read(picIn);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                int max = Math.max(img.getWidth(), img.getHeight());
                ImageIcon icon = new ImageIcon(img.getScaledInstance(img.getWidth() * 500 / max, img.getHeight() * 500 / max, 0));
                image2.setIcon(icon);
            }
        });
        imagePanel.add(getFile1Button);
        label = new JLabel("Image In:");
        label.setBounds(50, 150, 100, 30);
        imagePanel.add(label);
        JButton getFile2Button = new JButton("Image In");
        getFile2Button.setBounds(50, 180, 100, 30);
        getFile2Button.addActionListener(e -> {
            fileChooser2.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser2.showOpenDialog(mainFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File picIn = fileChooser2.getSelectedFile();
                BufferedImage img = null;
                try {
                    img = ImageIO.read(picIn);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                int max = Math.max(img.getWidth(), img.getHeight());
                ImageIcon icon = new ImageIcon(img.getScaledInstance(img.getWidth() * 500 / max, img.getHeight() * 500 / max, 0));
                image2.setIcon(icon);
            }
        });
        imagePanel.add(getFile2Button);
        encryptButton = new JButton("Encrypt");
        encryptButton.setBounds(50, 250, 100, 30);
        encryptButton.addActionListener(e -> {
            String inputFile = String.valueOf(fileChooser3.getSelectedFile());
            int result = fileChooser3.showSaveDialog(mainFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                Steganography.encodeImage(String.valueOf(fileChooser1.getSelectedFile()), String.valueOf(fileChooser2.getSelectedFile()), String.valueOf(fileChooser3.getSelectedFile()));
            }
        });
        imagePanel.add(encryptButton);
        decryptButton = new JButton("Decrypt");
        decryptButton.setBounds(50, 280, 100, 30);
        decryptButton.addActionListener(e -> {
            String inputFile = String.valueOf(fileChooser1.getSelectedFile());
            //String outputFile = String.valueOf(fileChooser3.getSelectedFile());
            /*int result = fileChooser3.showSaveDialog(mainFrame);
            if (result == JFileChooser.APPROVE_OPTION) {*/
            BufferedImage img = Steganography.decodeImage(inputFile, inputFile, false);
            int max = Math.max(img.getWidth(), img.getHeight());
            ImageIcon icon = new ImageIcon(img.getScaledInstance(img.getWidth() * 500 / max, img.getHeight() * 500 / max, 0));
            image2.setIcon(icon);
            //}
        });
        imagePanel.add(decryptButton);

        tabs.addTab("Image", imagePanel);

        mainFrame.setLayout(new GridLayout());
        mainFrame.add(tabs);
        mainFrame.setSize(1000, 700);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}