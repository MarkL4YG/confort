package de.mlessmann.confort.lang.hocon;

import java.util.regex.Pattern;

public class HOCONHelper {

    private static final Pattern FORBIDDEN_CHARS = Pattern.compile("[$\"{}\\[]:=,\\+#`\\^\\?!@\\*&\\\\ ]");
    private static final Pattern STARTS_WITH_NUM = Pattern.compile("^[0-9]");
    /**
     * Rules from: https://github.com/lightbend/config/blob/master/HOCON.md#unquoted-strings
     */
    public static boolean strRequiresQuotes(String str) {
        return FORBIDDEN_CHARS.matcher(str).find()
                || str.contains("//")
                || str.startsWith("true") || str.startsWith("false") || str.startsWith("null")
                || STARTS_WITH_NUM.matcher(str).find();
    }
}
