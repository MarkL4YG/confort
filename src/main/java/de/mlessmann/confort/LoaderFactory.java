package de.mlessmann.confort;

import de.mlessmann.confort.except.LoaderLookupException;
import de.mlessmann.confort.lang.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class LoaderFactory {

    private static final Logger logger = LoggerFactory.getLogger(LoaderFactory.class);

    public static <T extends ConfigLoader> void registerLoader(
            String protocol,
            Class<T> loaderClass,
            Supplier<T> loaderProducer) {
        if (loaderClass == null) {
            throw new IllegalArgumentException("Loader class shall not be null!");
        }

        T loader = loaderProducer.get();
        if (loader == null) {
            String message = String.format("Producer for loader of \"%s\" returned NULL! Expected %s", protocol, loaderClass.getName());
            throw new IllegalStateException(message);
        }

        if (loaderClass.isAssignableFrom(loader.getClass())) {
            String message = String.format("Registering loader for protocol \"%s\": %s", protocol, loaderClass.getName());
            logger.debug(message);
            LOADERS.putIfAbsent(protocol, loaderClass.cast(loader));
        } else {
            String message = String.format("Unassignable loader produced for protocol \"%s\"! %s cannot be assigned to %s",
                    protocol,
                    loaderClass.getName(),
                    loader.getClass().getName()
            );
            throw new LoaderLookupException(message);
        }
    }

    private static final Map<String, ConfigLoader> LOADERS = new ConcurrentHashMap<>();

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
}
