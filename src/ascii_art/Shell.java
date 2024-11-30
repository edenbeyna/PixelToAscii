package ascii_art;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.ImageProcessor;
import image.Image;
import image_char_matching.SubImgCharMatcher;
import java.util.Arrays;

import java.io.IOException;



/**
 * The main shell class for handling user input and executing commands.
 * Supports commands for generating ASCII art from an image.
 */
public class Shell {
    /**
     * Default character set used for generating ASCII art.
     */
    private static final char[] DEFAULT_CHARSET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * Default resolution for processing the image.
     */
    private static final int DEFAULT_RES = 128;
    /**
     * Minimum ASCII value for characters.
     */
    private static final int MIN_ASCII = 32;
    /**
     * Maximum ASCII value for characters.
     */
    private static final int MAX_ASCII = 126;
    /**
     * Index of the first character in a command array.
     */

    private static final int FIRST_IND = 0;
    /**
     * Index of the second character in a command array.
     */

    private static final int SECOND_IND = 1;

    /**
     * Default filename for the image.
     */
    private static final String DEFAULT_FILENAME = "cat.jpeg";

    /**
     * Default path for the image.
     */

    private static final String DEFAULT_HTML_PATH = "out.html";

    /**
     * Default font for the image.
     */

    private static final String DEFAULT_FONT = "Courier New";

    /**
     * Command to exit the shell.
     */

    private static final String EXIT = "exit";
    /**
     * Command to generate ascii art.
     */
    private static final String ASCII_ART = "asciiArt";
    /**
     * Command to add characters to the character set.
     */

    private static final String ADD = "add";

    /**
     * Command to remove characters to the character set.
     */

    private static final String REMOVE = "remove";

    /**
     * Command used to remove all/add all.
     */

    private static final String ALL = "all";
    /**
     * Command to add space character to the character set.
     */

    private static final String SPACE = "space";

    /**
     * Command to specify a range of characters.
     */

    private static final String HYPHEN = "-";

    /**
     * Space character string.
     */

    private static final String SPACE_CHAR = " ";
    /**
     * Command to specify an image.
     */
    private static final String IMAGE = "image";
    /**
     * Command to specify resolution.
     */
    private static final String RES = "res";

    /**
     * Command to specify characters.
     */

    private static final String CHARS = "chars";

    /**
     * Prompt for user input.
     */
    private static final String INPUT_PROMPT = ">>> ";

    /**
     * Command to specify the output method.
     */
    private static final String OUTPUT = "output";
    /**
     * Output method option for console.
     */
    private static final String CONSOLE = "console";

    /**
     * Output method option for HTML.
     */
    private static final String HTML = "html";
    /**
     * Error message for incorrect resolution format.
     */
    private static final String RES_FORMAT_ERROR_MSG = "Did not change resolution due " +
            "to incorrect format.";
    /**
     * Error message for exceeding resoloution boundaries.
     */
    public static final String RES_LIMITS_ERROR_MSG = "Did not change resolution due to " +
            "exceeding boundaries.";
    /**
     * Success message for changing resolution.
     */
    private static final String RES_SUCCESS_MSG = "Resolution set to ";
    /**
     * Error message for incorrect output format.
     */
    private static final String OUTPUT_ERROR_MSG = "Did not change output method due to incorrect " +
            "format";
    /**
     * Error message for incorrect image format.
     */
    private static final String IMAGE_FORMAT_ERROR_MSG = "Did not change image file path due to incorrect " +
            "format";
    /**
     * Error message for problem with image file.
     */
    private static final String IMAGE_ERROR_MSG = "Did not execute due to problem with image file.";
    /**
     * Error message for an invalid request to add characters.
     */
    private static final String ADD_FORMAT_ERROR_MSG = "Did not add due to incorrect format.";

    /**
     * Error message for an invalid request to remove characters.
     */
    private static final String REMOVE_FORMAT_ERROR_MSG = "Did not remove due to incorrect format.";
    /**
     * Error message for an invalid command request.
     */
    private static final String INCORRECT_COMMAND_ERROR_MSG = "Did not execute due to incorrect command.";
    /**
     * Flag indicating whether the shell is running.
     */
    private static boolean running;
    /**
     * Character set used for generating ASCII art.
     */
    private char[] charSet;
    /**
     * Resolution for processing the image.
     */
    private int resolution;
    /**
     * Matcher for matching image brightness to characters.
     */
    private SubImgCharMatcher matcher;
    /**
     * Filename of the image.
     */
    private String filename;
    /**
     * Processor for processing the image.
     */
    private ImageProcessor processor;
    /**
     * Output method for displaying ASCII art.
     */
    private AsciiOutput output;


    /**
     * Constructs a Shell object with default settings.
     */
    public Shell() {
        try {
            this.charSet = DEFAULT_CHARSET;
            this.matcher = new SubImgCharMatcher(this.charSet);
            this.filename = DEFAULT_FILENAME;
            Image img = new Image(filename);
            this.processor = new ImageProcessor(img);
            this.resolution = DEFAULT_RES;
            this.output = new ConsoleAsciiOutput();
        } catch (IOException e) {
            System.out.println(IMAGE_ERROR_MSG);
        }
    }

    /**
     * Starts the shell, allowing user input and command execution.
     */
    public void run(){
        running = true;
        while (running) {
            System.out.print(INPUT_PROMPT);
            String userInput = KeyboardInput.readLine();
            running = handleCommand(userInput);
        }
    }

    /**
     * Handles user commands and executes corresponding actions.
     * @param userInput The command entered by the user.
     * @return True if the shell should continue running, false otherwise.
     */
    private boolean handleCommand(String userInput){
        String[] command = userInput.split(SPACE_CHAR);
        switch (command[FIRST_IND]) {
            case EXIT -> {
                return false;
            }
            case CHARS -> {
                printCharSet();
            }
            case ASCII_ART -> {
                runAsciiArt();
            }
            case RES -> {
                if (command.length == 2) {
                    changeResolution(command[1]);
                } else {
                    System.out.println(RES_FORMAT_ERROR_MSG);
                }
            }
            case OUTPUT-> {
                if (command.length == 2) {
                    changeOutput(command[1]);
                } else {
                    System.out.println(OUTPUT_ERROR_MSG);
                }
            }
            case IMAGE-> {
                if (command.length == 2) {
                    changeImage(command[1]);
                } else {
                    System.out.println(IMAGE_FORMAT_ERROR_MSG);
                }
            }
            case REMOVE-> handleRemove(command);
            case ADD -> handleAdd(command);
            default -> System.out.println(INCORRECT_COMMAND_ERROR_MSG);
        }
        return true;
    }

    /**
     * Prints the current character set.
     */
        private void printCharSet () {
            Arrays.sort(charSet);
            for (char c : charSet) {
                System.out.print((int) c + " ");
            }
            System.out.println();
        }
    /**
     * Generates and displays ASCII art from the image using the current settings.
     */
        private void runAsciiArt (){
            AsciiArtAlgorithm algorithm = new AsciiArtAlgorithm(processor, resolution, matcher);
            try {
                char[][] result = algorithm.run();
                output.out(result);
            }catch (IOException e){
                System.out.println(IMAGE_ERROR_MSG);
            }
        }


    /**
     * Changes the resolution of the image.
     * @param command The resolution change command (up or down).
     */
    private void changeResolution(String command){
        try {
            Image image = new Image(filename);
            int maxResolution = image.getWidth();
            int minResolution = Math.max(1, image.getWidth() / image.getHeight());
            switch (command) {
                case "up":
                    updateResolution(maxResolution, minResolution, 2);
                    break;
                case "down":
                    updateResolution(maxResolution, minResolution, 0.5);
                    break;
                default:
                    System.out.println(RES_FORMAT_ERROR_MSG);
                    break;
            }
        } catch (IOException e) {
            System.out.println(IMAGE_ERROR_MSG);
        }
    }
    /**
     * Updates the resolution based on the provided factor.
     * @param maxResolution The maximum allowable resolution.
     * @param minResolution The minimum allowable resolution.
     * @param factor The factor by which to adjust the resolution.
     */
    private void updateResolution(int maxResolution, int minResolution, double factor) {
        int newResolution = (int) (this.resolution * factor);
        if (newResolution >= minResolution && newResolution <= maxResolution) {
            this.resolution = newResolution;
            System.out.println(RES_SUCCESS_MSG + this.resolution);
        } else {
            System.out.println(RES_LIMITS_ERROR_MSG);
        }
    }
    /**
     * Changes the output type for displaying ASCII art.
     * @param type The type of output (console or html).
     */
    private void changeOutput(String type){
        switch (type) {
            case CONSOLE:
                output = new ConsoleAsciiOutput();
                break;
            case HTML:
                output = new HtmlAsciiOutput(DEFAULT_HTML_PATH,DEFAULT_FONT);
                break;
            default:
                System.out.println(OUTPUT_ERROR_MSG);
                break;
        }


    }
    /**
     * Changes the image file.
     * @param newFileName The path to the new image file.
     */
    private void changeImage(String newFileName){
        try {
            Image img = new Image(newFileName);
            this.filename = newFileName;
            this.processor = new ImageProcessor(img);
        } catch (IOException e) {
            System.out.println(IMAGE_ERROR_MSG);
        }
    }

    /**
     * Handles the addition of characters to the character set.
     * @param command The add command entered by the user.
     */

    private void handleAdd(String[] command) {
        switch (command[SECOND_IND]) {
            case ALL:
                for (char i = MIN_ASCII; i <= MAX_ASCII; i++) {
                    matcher.addChar(i);
                }
                this.charSet = matcher.getCharSet();
                break;
            case SPACE:
                matcher.addChar((char) MIN_ASCII);
                this.charSet = matcher.getCharSet();
                break;
            default:
                switch (command[SECOND_IND].length()) {
                    case 1:
                        char c = command[SECOND_IND].charAt(FIRST_IND);
                        matcher.addChar(c);
                        this.charSet = matcher.getCharSet();
                        break;
                    case 3:
                        String[] chars = command[SECOND_IND].split(HYPHEN);
                        Arrays.sort(chars);
                        for (char i = chars[FIRST_IND].charAt(FIRST_IND);
                             i <= chars[SECOND_IND].charAt(FIRST_IND); i++) {
                            matcher.addChar(i);
                        }
                        this.charSet = matcher.getCharSet();
                        break;
                    default:
                        System.out.println(ADD_FORMAT_ERROR_MSG);
                        break;
                }
                break;
        }
    }



    /**
     * Handles the removal of characters from the character set.
     * @param command The remove command entered by the user.
     */
    private void handleRemove(String[] command) {
        switch (command[SECOND_IND]) {
            case ALL:
                for (char i = MIN_ASCII; i <= MAX_ASCII; i++) {
                    matcher.removeChar(i);
                }
                this.charSet = matcher.getCharSet();
                break;
            case SPACE:
                matcher.removeChar((char) MIN_ASCII);
                this.charSet = matcher.getCharSet();
                break;
            default:
                switch (command[SECOND_IND].length()) {
                    case 1:
                        char c = command[SECOND_IND].charAt(FIRST_IND);
                        matcher.removeChar(c);
                        this.charSet = matcher.getCharSet();
                        break;
                    case 3:
                        String[] chars = command[SECOND_IND].split(HYPHEN);
                        Arrays.sort(chars);
                        for (char i = chars[FIRST_IND].charAt(FIRST_IND);
                             i <= chars[SECOND_IND].charAt(FIRST_IND); i++) {
                            matcher.removeChar(i);
                        }
                        this.charSet = matcher.getCharSet();
                        break;
                    default:
                        System.out.println(REMOVE_FORMAT_ERROR_MSG);
                        break;
                }
                break;
        }
    }
    /**
     * The main entry point of the program.
     * Creates an instance of the Shell class and runs it.
     */
    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.run();
    }

}


