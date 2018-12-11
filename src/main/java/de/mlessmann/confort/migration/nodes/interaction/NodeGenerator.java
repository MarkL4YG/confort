package de.mlessmann.confort.migration.nodes.interaction;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.migration.DescriptorScope;

public interface NodeGenerator {

    void generateOnto(IConfigNode rootNode, IConfigNode providedTarget, DescriptorScope scope);
}
