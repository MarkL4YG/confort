package de.mlessmann.confort.migration.nodes.operations;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.migration.DescriptorScope;
import de.mlessmann.confort.migration.nodes.OpArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropNode extends OpNode {

    private static final Logger logger = LoggerFactory.getLogger(DropNode.class);

    public DropNode(OpArgument leftHand, OpArgument rightHand) {
        super(leftHand, rightHand);
    }

    @Override
    public void executeOn(IConfigNode root, IConfigNode relativeRoot, DescriptorScope scope) {
        if (!getLeftHand().isNodeReference()) {
            throw new IllegalArgumentException("Cannot drop non-node references!");
        }
        logger.info("Dropping node {}", getLeftHand().getIdentifier());

        String[] path = getLeftHand().asPath();
        getParentOf(
                getLeftHand().isRelative() ? relativeRoot : root,
                path
        ).remove(path[path.length - 1]);
    }
}
