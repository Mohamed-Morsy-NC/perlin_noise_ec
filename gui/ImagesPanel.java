package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

import backend.MainFrame;

// This is in charge of showing the images as a gallery. It has the images to show, the names,
// index to track which one is displayed, text label, information label, instructions label, and a path. AI
// helped me figure out how to display these images as a gallery and click between them.
public class ImagesPanel extends JPanel implements ActionListener {

    private List<BufferedImage> images;
    private List<String> imageNames;
    private int currentImageIndex = 0;
    private JLabel imageLabel;
    private JLabel infoLabel;
    private JLabel instructionLabel;
    private String mediaFolderPath = "media";
    private static int btnScaleFactor = 3;
    private BufferedImage bgImage;

    public ImagesPanel() {
        setLayout(new BorderLayout());

        loadImages();
        setupUI();
    }

    // Images are loaded by creating to arraylists that hold the images and their respective names
    // If the directory doesn't exist, we create it as the "media" folder. For every file in the directory
    // we check if it's a valid image, like .png or .jpeg, and then we sort them for better organization and easier
    // data reading. We read the current image based off of an index, and then display that. Clicking the buttons allows you
    // to go between images, and refreshing updates the arrays with the latest screenshots
    private void loadImages() {
        images = new ArrayList<>();
        imageNames = new ArrayList<>();

        File mediaFolder = new File(mediaFolderPath);
        // test comment

        // Try different possible paths if media folder not found
        if (!mediaFolder.exists()) {
            // Try as resource path
            try {
                mediaFolder = new File(getClass().getResource("/media").toURI());
            } catch (Exception e) {
                // Try relative paths
                String[] possiblePaths = {"./media", "../media", "src/media", "assets/media"};
                for (String path : possiblePaths) {
                    File testFolder = new File(path);
                    if (testFolder.exists() && testFolder.isDirectory()) {
                        mediaFolder = testFolder;
                        mediaFolderPath = path;
                        break;
                    }
                }
            }
        }

        // Failed to load the media folder -> create it
        if (!mediaFolder.exists() || !mediaFolder.isDirectory()) {
            mediaFolder.mkdirs();
        }

        // Get all image files from the media folder
        File[] files = mediaFolder.listFiles((dir, name) -> {
            String lowerName = name.toLowerCase();
            return lowerName.endsWith(".png") || lowerName.endsWith(".jpg")
                    || lowerName.endsWith(".jpeg") || lowerName.endsWith(".gif")
                    || lowerName.endsWith(".bmp");
        });

        if (files != null && files.length > 0) {
            // Sort files by name for consistent ordering
            Arrays.sort(files, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));

            for (File file : files) {
                try {
                    BufferedImage image = ImageIO.read(file);
                    if (image != null) {
                        images.add(image);
                        imageNames.add(file.getName());
                        // System.out.println("Loaded image: " + file.getName());
                    }
                } catch (IOException e) {
                    // System.out.println("Failed to load image: " + file.getName());
                    e.printStackTrace();
                }
            }
        }

        // System.out.println("Total images loaded: " + images.size());
    }

    // Sets up the actual window the player sees with the buttons, text, and the current image
    private void setupUI() {
        // Sets up background image and UI elements like the buttons on screen and text
        try {
            bgImage = ImageIO.read(getClass().getResourceAsStream("/assets/galleryimage.png"));
            BackgroundPanel bgPanel = new BackgroundPanel(bgImage);
            this.add(bgPanel, BorderLayout.CENTER);

            // Create main image display area
            imageLabel = new JLabel();
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            imageLabel.setBackground(new Color(0, 0, 0, 0));
            imageLabel.setOpaque(true);

            // Add click listener for cycling images
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (images.size() > 0) {
                        currentImageIndex = (currentImageIndex + 1) % images.size();
                        updateImageDisplay();
                    }
                }
            });

            // Create info panel
            JPanel infoPanel = new JPanel(new BorderLayout());
            infoPanel.setOpaque(false);

            infoLabel = new JLabel();
            infoLabel.setForeground(Color.WHITE);
            infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            infoLabel.setFont(MainFrame.UNIVERSAL_FONT_SMALL);

            instructionLabel = new JLabel();
            instructionLabel.setForeground(Color.LIGHT_GRAY);
            instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
            instructionLabel.setFont(MainFrame.UNIVERSAL_FONT_SMALL);

            infoPanel.add(infoLabel, BorderLayout.CENTER);
            infoPanel.add(instructionLabel, BorderLayout.SOUTH);

            // Create bottom panel with info and return button
            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setOpaque(false);

            // Create return button panel
            JPanel returnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            returnPanel.setOpaque(false);

            JButton returnBtn = PerlinManager.createButton("return", btnScaleFactor, ImageIO.read(getClass().getResourceAsStream("/assets/return_btn.png")), 700, 500, this);

            returnBtn.addActionListener(e -> {
                PerlinManager.goToGenerate();
                PerlinManager.playPop();
            });
            returnPanel.add(returnBtn);

            bottomPanel.add(infoPanel, BorderLayout.CENTER);
            bottomPanel.add(returnPanel, BorderLayout.SOUTH);

            // Add navigation buttons
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.setOpaque(false);

            JButton prevButton = PerlinManager.createButton("previous", btnScaleFactor, ImageIO.read(getClass().getResourceAsStream("/assets/prev_btn.png")), 200, 500, this);
            JButton nextButton = PerlinManager.createButton("next", btnScaleFactor, ImageIO.read(getClass().getResourceAsStream("/assets/next_btn.png")), 400, 500, this);
            JButton refreshButton = PerlinManager.createButton("refresh", btnScaleFactor, ImageIO.read(getClass().getResourceAsStream("/assets/refresh_btn.png")), 700, 500, this);

            prevButton.addActionListener(e -> previousImage());
            nextButton.addActionListener(e -> nextImage());
            refreshButton.addActionListener(e -> refreshImages());

            buttonPanel.add(prevButton);
            buttonPanel.add(nextButton);
            buttonPanel.add(refreshButton);

            // Layout components
            bgPanel.add(imageLabel, BorderLayout.CENTER);
            bgPanel.add(bottomPanel, BorderLayout.SOUTH);
            bgPanel.add(buttonPanel, BorderLayout.NORTH);

            // Display first image or no images message
            updateImageDisplay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {

    }

    // Updates how the image is drawn. AI mostly helped with setting the text, with html
    // being used in the text to display some helpful instruction text if there are no current screenshots
    // in the image list. If there are, the current image (based on the index) is displayed, and is scaled
    // to fit in the screen
    private void updateImageDisplay() {
        if (images.isEmpty()) {
            imageLabel.setIcon(null);
            imageLabel.setFont(MainFrame.UNIVERSAL_FONT_SMALL);
            imageLabel.setText("<html><div style='text-align: center; color: white;'>"
                    + "<h2>No Images Found</h2>"
                    + "<p>Place images in the '" + mediaFolderPath + "' folder</p>"
                    + "<p>Supported formats: PNG, JPG, JPEG, GIF, BMP</p></div></html>");
            infoLabel.setText("No images available");
            infoLabel.setFont(MainFrame.UNIVERSAL_FONT_SMALL);
            instructionLabel.setFont(MainFrame.UNIVERSAL_FONT_SMALL);
            instructionLabel.setText("Add images to the media folder and click Refresh");
        } else {
            BufferedImage currentImage = images.get(currentImageIndex);
            String imageName = imageNames.get(currentImageIndex);

            // Scale image to fit panel while maintaining aspect ratio
            ImageIcon scaledIcon = scaleImageToFit(currentImage, 800, 600);
            imageLabel.setIcon(scaledIcon);
            imageLabel.setText(""); // Clear text when showing image

            // Update info labels
            infoLabel.setText(String.format("%s (%d x %d)",
                    imageName, currentImage.getWidth(), currentImage.getHeight()));
            instructionLabel.setText(String.format("Image %d of %d - Click image or use buttons to navigate",
                    currentImageIndex + 1, images.size()));
        }
    }

    private ImageIcon scaleImageToFit(BufferedImage originalImage, int maxWidth, int maxHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Calculate scaling factor to fit within bounds while maintaining aspect ratio
        double scaleX = (double) maxWidth / originalWidth;
        double scaleY = (double) maxHeight / originalHeight;
        double scale = Math.min(scaleX, scaleY);

        // Don't scale up small images too much so they bloat the screen
        if (scale > 2.0) {
            scale = 2.0;
        }

        int scaledWidth = (int) (originalWidth * scale);
        int scaledHeight = (int) (originalHeight * scale);

        // If no scaling was actually applied, just return the original image
        if (scaledWidth == originalWidth && scaledHeight == originalHeight) {
            return new ImageIcon(originalImage);
        }

        Image scaledImage = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private void nextImage() {
        PerlinManager.playPop();
        if (images.size() > 0) {
            currentImageIndex = (currentImageIndex + 1) % images.size();
            updateImageDisplay();
            revalidate();
            repaint();
        }
    }

    private void previousImage() {
        PerlinManager.playPop();
        if (images.size() > 0) {
            currentImageIndex = (currentImageIndex - 1 + images.size()) % images.size();
            updateImageDisplay();
            revalidate();
            repaint();
        }
    }

    // Updating the arrays with the new images that have just been created
    private void refreshImages() {
        PerlinManager.playPop();
        images.clear();
        imageNames.clear();
        currentImageIndex = 0;
        loadImages();
        updateImageDisplay();
        revalidate();
        repaint();
    }

    // Method to set custom media folder path
    public void setMediaFolderPath(String path) {
        this.mediaFolderPath = path;
        refreshImages();
    }

    // Method to get current image count
    public int getImageCount() {
        return images.size();
    }

    // Method to get current image index
    public int getCurrentImageIndex() {
        return currentImageIndex;
    }
}
