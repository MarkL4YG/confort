package de.mlessmann.confort.migration.nodes;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.migration.DescriptorScope;

import java.util.LinkedList;
import java.util.List;

public class DeltaBlockNode implements DeltaDescriptorNode {

    private List<DeltaDescriptorNode> children = new LinkedList<>();

    @Override
    public void readAhead(DescriptorScope scope) {
        children.forEach(action -> action.readAhead(scope));
    }

    @Override
    public void executeOn(IConfigNode root, IConfigNode relativeRoot, DescriptorScope scope) {
        children.forEach(action -> action.executeOn(root, relativeRoot, scope));
    }

    public void appendChild(DeltaDescriptorNode node) {
        children.add(node);
    }
}
