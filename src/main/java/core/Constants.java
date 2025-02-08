package core;

import java.util.Set;

public class Constants {
    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Characters used to indicate a word's boundaries on the left and right side.
     * The beginning and end of the line the caret is on also function as boundaries.
     */
    public static final Set<Character> BOUNDARY_CHARS =
        Set.of(' ', ';', ':', '.', ',', '`', '"', '\'', '(', ')', '[', ']', '{', '}', '\t');

    /**
     * Toggler's default collection of word combinations / toggles.
     * The "Reset to Default" button resets to these.
     * <p>
     * The toggles are stored in this class instead of an external
     * file for OS compatibility and performance reasons.
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
