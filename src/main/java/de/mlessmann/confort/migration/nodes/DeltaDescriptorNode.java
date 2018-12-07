package de.mlessmann.confort.migration.nodes;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.migration.DescriptorScope;

public interface DeltaDescriptorNode {

    void readAhead(DescriptorScope scope);

    void executeOn(IConfigNode root, IConfigNode relativeRoot, DescriptorScope scope);
}
