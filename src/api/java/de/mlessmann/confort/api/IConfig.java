package de.mlessmann.confort.api;

import de.mlessmann.confort.api.except.ParseException;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

/**
 * Abstraction layer above configuration sources.
 * Allows users of the library to be unaware of the data source and format used.
 */
public interface IConfig {

    /**
     * @deprecated encoding should be hidden away just as the format.
     */
    @Deprecated
    void setEncoding(Charset encoding);

    /**
     * Saves the configuration to its source representation.
     *
     * @throws IOException when the internal use of streams throws errors.
     */
    void save() throws IOException;

    /**
     * Loads the configuration from its source representation.
     *
     * @throws IOException    when the internal use of streams throws errors.
     * @throws ParseException when the parsing process throws errors.
     * @implNote Exceptions like {@link java.io.FileNotFoundException} are internally caught.
     */
    void load() throws IOException, ParseException;

    /**
     * Reset the currently stored configuration root with a new, empty instance.
     */
    void createRoot();

    /**
     * Set the stored configuration root.
     */
    void setRoot(IConfigNode root);

    /**
     * Returns the currently stored configuration root.
     */
    IConfigNode getRoot();

    /**
     * {@link URI} that identifies where the config comes from.
     * @apiNote This is only to be used to provide the administrator with more information on errors.
     * @implNote This may return {@code null}. It is <em>highly recommended</em> to provide a valid URI, though.
     */
    URI getSourceURI();
}
