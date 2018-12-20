package de.mlessmann.confort.lang;

import de.mlessmann.confort.format.FormatRef;

import java.util.function.Supplier;

public class FormatRegistration<T extends ConfigLoader> {

    private FormatRef specs;
    private Supplier<T> producer;

    public FormatRegistration(FormatRef specs, Supplier<T> producer) {
        this.specs = specs;
        this.producer = producer;
    }

    public FormatRef getSpecs() {
        return specs;
    }

    public boolean matches(String protocol) {
        if (protocol == null || protocol.trim().isEmpty()) {
            throw new IllegalArgumentException("Protocol may not be null or empty");
        }

        if (protocol.equals(specs.shortName())) {
            return true;
        }

        String[] mimeTypes = specs.mimeTypes();
        for (int i = mimeTypes.length - 1; i >= 0; i--) {
          if (protocol.equals(mimeTypes[i])) {
              return true;
          }
        }

        String[] fileExts = specs.fileExtensions();
        for (int i = fileExts.length - 1; i >= 0; i--) {
            if (protocol.equals(fileExts[i])) {
                return true;
            }
        }

        return false;
    }

    public Supplier<T> getProducer() {
        return producer;
    }
}
