package image;
import java.awt.*;

/**
 * The ImageProcessor class provides methods for image processing, such as padding and splitting images.
 */
public class ImageProcessor {

    /** Multiplier for the red component of RGB color */
    private static final double RED = 0.2126;

    /** Multiplier for the green component of RGB color */
    private static final double GREEN = 0.7152;

    /** Multiplier for the blue component of RGB color */
    private static final double BLUE = 0.0722;

    /** Maximum value of RGB color */
    private static final double RGB_MAX = 255.0;

    /** The image to be processed */
    private Image image;

    /**
     * Constructor for ImageProcessor.
     *
     * @param image The image to be processed.
     */

    public ImageProcessor(Image image){
        this.image = image;
    }

    /**
     * Pads the image to make its dimensions powers of two.
     */
    public void padImage() {
        int newWidth = nearestPowerOfTwo(image.getWidth());
        int newHeight = nearestPowerOfTwo(image.getHeight());
        int xOffset = (newWidth - image.getWidth()) / 2;
        int yOffset = (newHeight - image.getHeight()) / 2;

        Color[][] newImgArray = new Color[newHeight][newWidth];
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                if (i >= yOffset && i < yOffset + image.getHeight() &&
                        j >= xOffset && j < xOffset + image.getWidth()) {
                    newImgArray[i][j] = image.getPixel(i - yOffset, j - xOffset);
                } else {
                    newImgArray[i][j] = Color.WHITE;
                }
            }
        }
        this.image = new Image(newImgArray, newWidth, newHeight);
    }

    /**
     * Finds the nearest power of two for a given number.
     *
     * @param num The number to find the nearest power of two for.
     * @return The nearest power of two.
     */

    private static int nearestPowerOfTwo(int num) {
        int nearest = 1;
        while (nearest < num) {
            nearest *= 2;
        }
        return nearest;

    }

    /**
     * Splits the image into sub-images.
     *
     * @param cols The number of columns for the sub-images.
     * @return A 2D array containing the sub-images.
     */
    public Image[][] splitImage(int cols) {
        int newWidth = this.image.getWidth() / cols;
        int rows = this.image.getHeight() / newWidth;
        Image[][] subImages = new Image[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int startX = row * newWidth;
                int startY = col * newWidth;
                Color[][] subImagePixels = new Color[newWidth][newWidth];
                for (int x = 0; x < newWidth; x++) {
                    for (int y = 0; y < newWidth; y++) {
                        subImagePixels[x][y] = this.image.getPixel(startX + x, startY + y);
                    }
                }
                subImages[row][col] = new Image(subImagePixels, newWidth, newWidth);
            }
        }
        return subImages;
    }
    /**
     * Computes the brightness of an image.
     *
     * @param img The image for which brightness is to be computed.
     * @return The brightness value of the image.
     */
    public double getBrightness(Image img) {
        double allBrightness = 0;
        int allPixels = img.getWidth() * img.getHeight();
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                allBrightness += calculateBrightness(img.getPixel(x, y));
            }
        }
        return allBrightness / (allPixels * RGB_MAX);
    }

    /**
     * Calculates the brightness of a pixel.
     *
     * @param pixelColor The color of the pixel.
     * @return The brightness value of the pixel.
     */
    private double calculateBrightness(Color pixelColor) {
        double red = pixelColor.getRed() * RED;
        double green = pixelColor.getGreen() * GREEN;
        double blue = pixelColor.getBlue() * BLUE;
        return red + green + blue;
    }

}
