package de.mlessmann.confort.migration;

import de.mlessmann.confort.ConfortVersion;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.migration.nodes.DeltaBlockNode;
import de.mlessmann.confort.migration.nodes.DeltaDescriptorNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigMigration {

    private static final Logger logger = LoggerFactory.getLogger(ConfigMigration.class);

    private static final ConfortVersion CURRENT_VERSION = new ConfortVersion(1, 0, 0);

    private static final String KEY_VERSION = "_version";
    private static final String KEY_FROM = "_fromVersion";
    private static final String KEY_TO = "_toVersion";

    private ConfortVersion descriptorVersion = CURRENT_VERSION;
    private ConfortVersion fromVersion;
    private ConfortVersion toVersion;

    private DescriptorScope rootScope = new DescriptorScope();
    private DeltaDescriptorNode blockNode;

    public void loadBlock(DeltaBlockNode blockNode) {
        this.blockNode = blockNode;
        this.blockNode.readAhead(rootScope);

        String deltaDescriptorVersion = rootScope.get(KEY_VERSION);
        String fromVerString = rootScope.get(KEY_FROM);
        String toVerString = rootScope.get(KEY_TO);

        if (deltaDescriptorVersion != null) {
            descriptorVersion = ConfortVersion.parseString(deltaDescriptorVersion);
        } else {
            logger.warn("Delta descriptor is missing version variable. Assuming latest.");
        }

        if (fromVerString == null) {
            throw new IllegalStateException("No from-version variable has been specified!");
        }

        if (toVerString == null) {
            throw new IllegalStateException("No to-version variable has been specified!");
        }

        fromVersion = ConfortVersion.parseString(fromVerString);
        toVersion = ConfortVersion.parseString(toVerString);
    }

    public void migrateConfig(IConfigNode config) {
        logger.info(String.format("Migrating configuration from %s to %s", fromVersion, toVersion));
        blockNode.executeOn(config, config, rootScope);
    }
}
