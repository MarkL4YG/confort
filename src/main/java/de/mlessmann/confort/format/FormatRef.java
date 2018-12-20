package de.mlessmann.confort.format;

public @interface FormatRef {

    String shortName();

    String[] mimeTypes() default {};

    String[] fileExtensions() default {};
}
