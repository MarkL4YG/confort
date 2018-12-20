package de.mlessmann.confort;

import de.mlessmann.confort.except.LoaderLookupException;
import de.mlessmann.confort.format.FormatRef;
import de.mlessmann.confort.lang.ConfigLoader;
import de.mlessmann.confort.lang.FormatRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class LoaderFactory {

    private static final Logger logger = LoggerFactory.getLogger(LoaderFactory.class);

    public static <T extends ConfigLoader> void registerLoader(
            Class<T> loaderClass,
            Supplier<T> loaderProducer) {
        if (loaderClass == null) {
            throw new IllegalArgumentException("Loader class shall not be null!");
        }

        FormatRef formatRef = loaderClass.getAnnotation(FormatRef.class);
        if (formatRef == null) {
            throw new IllegalStateException("Cannot register unannotated loader class!");
        }

        FormatRegistration<? extends ConfigLoader> reg = new FormatRegistration<>(formatRef, loaderProducer);
        REGISTRATIONS.add(reg);
    }

    private static final Map<String, ConfigLoader> LOADERS = new ConcurrentHashMap<>();
    private static final List<FormatRegistration<? extends ConfigLoader>> REGISTRATIONS =
            Collections.synchronizedList(new LinkedList<>());

    public static ConfigLoader getLoader(String protocol) {
        ConfigLoader loader = LOADERS.getOrDefault(protocol, null);

        if (loader == null) {
            String message = String.format("No loader defined for protocol: \"%s\"", protocol);
            throw new LoaderLookupException(message);
        }

        return loader;
    }

    public static boolean hasAny() {
        return LOADERS.size() > 0;
    }

    private LoaderFactory() {
        // Hide public constructor
    }
}
