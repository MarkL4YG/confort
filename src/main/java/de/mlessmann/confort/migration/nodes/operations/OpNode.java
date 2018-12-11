package de.mlessmann.confort.migration.nodes.operations;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.migration.DescriptorScope;
import de.mlessmann.confort.migration.nodes.DeltaDescriptorNode;
import de.mlessmann.confort.migration.nodes.OpArgument;

public abstract class OpNode implements DeltaDescriptorNode {

    private OpArgument leftHand;
    private OpArgument rightHand;

    public OpNode(OpArgument leftHand, OpArgument rightHand) {
        this.leftHand = leftHand;
        this.rightHand = rightHand;
    }

    @Override
    public void readAhead(DescriptorScope scope) {
        // not required by operations
    }

    public OpArgument getLeftHand() {
        return leftHand;
    }

    public OpArgument getRightHand() {
        return rightHand;
    }

    protected IConfigNode getParentOf(IConfigNode relativeRoot, String... path) {
        IConfigNode parent = relativeRoot;
        for (int i = 0; i < path.length - 1; i++) {
            parent = parent.getNode(path[i]);
        }
        return parent;
    }
}
