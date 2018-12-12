package de.mlessmann.confort.migration.nodes.operations;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.migration.DescriptorScope;
import de.mlessmann.confort.migration.nodes.OpArgument;
import de.mlessmann.confort.migration.nodes.interaction.NodeGenerator;

public class GenerateNode extends OpNode {

    public GenerateNode(OpArgument leftHand, OpArgument rightHand) {
        super(leftHand, rightHand);
    }

    @Override
    public void executeOn(IConfigNode root, IConfigNode relativeRoot, DescriptorScope scope) {
        if (!getLeftHand().isNodeReference()) {
            throw new IllegalArgumentException("Generator target may only be a node reference!");
        }

        if (getRightHand().isNodeReference()) {
            throw new IllegalArgumentException("Generator source may only be a generator reference!");
        }

        NodeGenerator generator = scope.optGenerator(getRightHand().getIdentifier())
                .orElseThrow(() -> {
                    String message = String.format("No generator found for: \"%s\"", getRightHand().getIdentifier());
                    return new IllegalArgumentException(message);
                });

        IConfigNode target = (getLeftHand().isRelative() ? relativeRoot : root)
                .getNode(getLeftHand().asPath());
        generator.generateOnto(root, target, scope);
    }
}
