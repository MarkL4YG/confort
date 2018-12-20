package de.mlessmann.confort.lang;

import de.mlessmann.confort.LoaderFactory;
import de.mlessmann.confort.lang.json.JSONConfigLoader;

public class RegisterLoaders {

    public static void registerLoaders() {
        if (!LoaderFactory.hasAny()) {
            LoaderFactory.registerLoader(
                    JSONConfigLoader.class,
                    JSONConfigLoader::new
            );
        }
    }
}
