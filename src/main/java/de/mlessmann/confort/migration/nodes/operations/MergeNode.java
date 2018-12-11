package de.mlessmann.confort.migration.nodes.operations;

import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.migration.DescriptorScope;
import de.mlessmann.confort.migration.nodes.OpArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class MergeNode extends OpNode {

    private static final Logger logger = LoggerFactory.getLogger(MergeNode.class);

    private boolean prepend;

    public MergeNode(OpArgument leftHand, OpArgument rightHand, boolean prepend) {
        super(leftHand, rightHand);
        this.prepend = prepend;
    }

    @Override
    public void executeOn(IConfigNode root, IConfigNode relativeRoot, DescriptorScope scope) {
        String[] srcPath = getLeftHand().asPath();
        IConfigNode srcRoot = getLeftHand().isRelative() ? relativeRoot : root;
        IConfigNode dstRoot = getRightHand().isRelative() ? relativeRoot : root;

        IConfigNode subject = srcRoot.getNode(srcPath);
        IConfigNode target = dstRoot.getNode(getRightHand().asPath());

        if (subject.isMap()) {
            logger.info("Merging mapped node \"{}\" into \"{}\"",
                    getLeftHand().getIdentifier(), getRightHand().getIdentifier());
            subject.asMap().forEach(target::put);

        } else if (subject.isList()) {
            logger.info("{} from \"{}\" to \"{}\"",
                    prepend ? "Prepending" : "Appending",
                    getLeftHand().getIdentifier(),
                    getRightHand().getIdentifier()
            );
            Consumer<IConfigNode> consumer = prepend ? target::prepend : target::append;
            subject.asList().forEach(consumer);

        } else {
            throw new IllegalArgumentException("Can only merge lists or maps! Use #move otherwise.");
        }

        getParentOf(
                srcRoot,
                srcPath
        ).remove(srcPath[srcPath.length - 1]);
    }
}
