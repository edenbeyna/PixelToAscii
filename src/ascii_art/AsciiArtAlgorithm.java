package ascii_art;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;
import image.Image;
import java.io.IOException;


/**
 * A class representing the algorithm for generating ASCII art from an image.
 */
public class AsciiArtAlgorithm {
    /**
     * Processor for processing the image.
     */
    private ImageProcessor img;
    /**
     * Resolution for processing the image.
     */
    private int resolution;
    /**
     * Matcher for matching image brightness to characters.
     */
    private SubImgCharMatcher subImgCharMatcher;

    /**
     * Constructs an AsciiArtAlgorithm object with the given parameters.
     *
     * @param img               The ImageProcessor object representing the processed input image.
     * @param resolution        The desired resolution for splitting the image.
     * @param subImgCharMatcher The SubImgCharMatcher object for mapping brightness to characters.
     */

    public AsciiArtAlgorithm(ImageProcessor img, int resolution, SubImgCharMatcher subImgCharMatcher) {
        this.img = img;
        this.resolution = resolution;
        this.subImgCharMatcher = subImgCharMatcher;
    }

    /**
     * Runs the ASCII art generation algorithm.
     *
     * @return A 2D char array representing the generated ASCII art.
     * @throws IOException if there's an error reading the image or performing image processing.
     */

    public char[][] run() throws IOException {
        img.padImage();
        Image[][] subImages = img.splitImage(resolution);
        char[][] result = new char[subImages.length][subImages[0].length];
        for (int i = 0; i < subImages.length; i++) {
            for (int j = 0; j < subImages[i].length; j++) {
                double brigthness = img.getBrightness(subImages[i][j]);
                result[i][j] = subImgCharMatcher.getCharByImageBrightness(brigthness);
            }
        }
        return result;
    }



}

