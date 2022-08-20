package core;

/**
 * Configuration class that exposes constants to the rest of the application.
 */
public class Config {
    /**
     * The characters that indicate a word/symbol its boundaries. Used for the left and right side. The beginning and
     * end of the line the caret is on also function as boundaries.
     */
    public static final Character[] BOUNDARY_CHARS =
            {' ', ';', ':', '.', ',', '`', '"', '\'', '(', ')', '[', ']', '{', '}'};

    /**
     * Toggler's default collection of toggles. These are the toggles the plugin loads into its configuration if the
     * user hasn't modified them yet. These are also used when the user uses the "Reset to Default" functionality in
     * the configuration menu.
     *
     * The toggles are stored in this Config class instead of an external JSON file for OS compatibility and performance
     * reasons.
     */
    public static final String DEFAULT_TOGGLES = (
            "[" +
                    "[`public`,`private`,`protected`]," +
                    "[`class`,`interface`]," +
                    "[`extends`,`implements`]," +
                    "[`import`,`export`]," +
                    "[`byte`,`short`,`int`,`long`,`float`,`double`]," +
                    "[`String`,`Character`]," +

                    "[`get`,`set`]," +
                    "[`add`,`remove`]," +
                    "[`min`,`max`]," +
                    "[`pop`,`push`]," +

                    "[`true`,`false`]," +
                    "[`yes`,`no`]," +
                    "[`on`,`off`]," +
                    "[`0`,`1`]," +
                    "[`x`,`y`]," +
                    "[`enable`,`disable`]," +
                    "[`enabled`,`disabled`]," +
                    "[`open`,`close`]," +

                    "[`up`,`down`]," +
                    "[`left`,`right`]," +
                    "[`top`,`bottom`]," +
                    "[`start`,`end`]," +
                    "[`first`,`last`]," +
                    "[`before`,`after`]," +
                    "[`ceil`,`floor`]," +
                    "[`read`,`write`]," +
                    "[`show`,`hide`]," +
                    "[`input`,`output`]," +
                    "[`dev`,`prod`]," +
                    "[`development`,`production`]," +
                    "[`row`,`column`]," +
                    "[`req`,`res`]," +

                    "[`&&`,`||`]," +
                    "[`&`,`|`]," +
                    "[`<`,`>`]," +
                    "[`+`,`-`]," +
                    "[`*`,`/`]," +
                    "[`++`,`--`]," +
                    "[`+=`,`-=`]," +
                    "[`*=`,`/=`]," +
                    "[`&=`,`|=`]," +
                    "[`<<=`,`>>=`]," +
                    "[`<=`,`>=`]," +
                    "[`==`,`!=`]," +
                    "[`===`,`!==`]," +
            "]"
    ).replace('`', '"');
}
