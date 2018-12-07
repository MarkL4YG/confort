package de.mlessmann.confort.migration.nodes;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.migration.DescriptorScope;

public class OperationNode implements DeltaDescriptorNode {

    public static enum OpCode {
        DROP(false),
        MOVE(true),
        MERGE_APPEND(true),
        MERGE_PREPEND(true),
        GENERATE(true),
        CONSUME(true);

        private boolean hasArguments;

        public boolean hasArguments() {
            return hasArguments;
        }

        OpCode(boolean hasArguments) {
            this.hasArguments = hasArguments;
        }
    }

    private OpArgument left;
    private OpArgument right;
    private OpCode opCode;

    public OperationNode(OpArgument left, OpArgument right, OpCode opCode) {
        this.left = left;
        this.right = right;
        this.opCode = opCode;
    }

    @Override
    public void executeOn(IConfigNode root, IConfigNode relativeRoot, DescriptorScope scope) {

    }
}
