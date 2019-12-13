package de.mlessmann.confort.lang.codepoint;

import de.mlessmann.confort.api.IConfigNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class LiteralUtils {

    /**
     * @throws NumberFormatException when the number could not be parsed to a number.
     */
    public static IConfigNode parseNumber(TerminalNode numberTerminalNode, IConfigNode node) {
        String text = numberTerminalNode.getText();

        if (text.contains(".")) {
            Double value = Double.parseDouble(text);
            node.setDouble(value);
        } else {
            Integer value = Integer.parseInt(text);
            node.setInteger(value);
        }

        return node;
    }
}
