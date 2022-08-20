package core;

/**
 * Configuration class that exposes constants to the rest of the application.
 * Note: some configs are stored in the `resources` folder in JSON format.
 */
public class Config {
    /**
     * The characters that indicate a word/symbol its boundaries. Used for the left and right side. The beginning and
     * end of the line the caret is on also function as boundaries.
     */
    public static final Character[] BOUNDARY_CHARS = {' ', ';', ':', '.', ',', '`', '"', '\'', '(', ')', '[', ']', '{', '}'};
}
