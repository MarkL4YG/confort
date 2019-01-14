package de.mlessmann.confort;

import de.mlessmann.confort.api.except.LoaderLookupException;
import de.mlessmann.confort.api.lang.IConfigLoader;
import de.mlessmann.confort.format.FormatRef;
import de.mlessmann.confort.format.FormatRegistration;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class LoaderFactory {

    public static <T extends IConfigLoader> void registerLoader(
            Class<T> loaderClass,
            Supplier<T> loaderProducer) {
        if (loaderClass == null) {
            throw new IllegalArgumentException("Loader class shall not be null!");
        }

        FormatRef formatRef = loaderClass.getAnnotation(FormatRef.class);
        if (formatRef == null) {
            throw new IllegalStateException("Cannot register unannotated loader class!");
        }

        FormatRegistration<? extends IConfigLoader> reg = new FormatRegistration<>(formatRef, loaderProducer);
        REGISTRATIONS.add(reg);
    }

    private static final List<FormatRegistration<? extends IConfigLoader>> REGISTRATIONS =
            Collections.synchronizedList(new LinkedList<>());

    public static IConfigLoader getLoader(String protocol) {
        final Supplier[] supplier = new Supplier[]{null};
        REGISTRATIONS.stream()
                .filter(reg -> reg.matches(protocol))
                .forEach(reg -> {
                    if (supplier[0] == null) {
                        supplier[0] = reg.getProducer();
                    } else {
                        String message = String.format("Ambiguous loaders found for %s", protocol);
                        throw new LoaderLookupException(message);
                    }
                });

        if (supplier[0] == null) {
            throw new LoaderLookupException(String.format("No loader found for protocol: %s", protocol));
        }

        return ((IConfigLoader) supplier[0].get());
    }

    public static boolean hasAny() {
        return !REGISTRATIONS.isEmpty();
    }

    private LoaderFactory() {
        // Hide public constructor
    }
}
