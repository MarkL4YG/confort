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

    public Supplier<T> getProducer() {
        return producer;
    }
}
