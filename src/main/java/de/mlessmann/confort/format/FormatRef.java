package de.mlessmann.confort.format;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FormatRef {

    String shortName();

    String[] mimeTypes() default {};

    String[] fileExtensions() default {};
}
