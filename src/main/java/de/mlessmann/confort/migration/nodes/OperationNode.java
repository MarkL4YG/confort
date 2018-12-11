package de.mlessmann.confort.migration.nodes;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.migration.DescriptorScope;
import de.mlessmann.confort.migration.nodes.operations.OpCode;

public class OperationNode implements DeltaDescriptorNode {

    private OpArgument left;
    private OpArgument right;
    private OpCode opCode;

    public OperationNode(OpArgument left, OpArgument right, OpCode opCode) {
        this.left = left;
        this.right = right;
        this.opCode = opCode;
    }

    @Override
    public void readAhead(DescriptorScope scope) {
        // Not required for operations
    }

    @Override
    public void executeOn(IConfigNode root, IConfigNode relativeRoot, DescriptorScope scope) {

    }
}
