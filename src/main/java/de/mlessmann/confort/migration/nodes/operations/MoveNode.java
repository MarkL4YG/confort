package de.mlessmann.confort.migration.nodes.operations;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.migration.DescriptorScope;
import de.mlessmann.confort.migration.nodes.OpArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveNode extends OpNode {

    private static final Logger logger = LoggerFactory.getLogger(MoveNode.class);

    public MoveNode(OpArgument leftHand, OpArgument rightHand) {
        super(leftHand, rightHand);
    }

    @Override
    public void executeOn(IConfigNode root, IConfigNode relativeRoot, DescriptorScope scope) {
        logger.info("Moving node \"{}\" to \"{}\"",
                getLeftHand().getIdentifier(),
                getRightHand().getIdentifier()
        );

        String[] fromPath = getLeftHand().asPath();
        IConfigNode parent = getParentOf(
                getLeftHand().isRelative() ? relativeRoot : root,
                fromPath
        );
        IConfigNode subject = parent.remove(fromPath[fromPath.length - 1]);

        if (subject != null) {
            String[] toPath = getRightHand().asPath();
            IConfigNode targetParent = getParentOf(
                    getRightHand().isRelative() ? relativeRoot : root,
                    toPath
            );

            targetParent.put(toPath[toPath.length - 1], subject);
        }
    }
}
