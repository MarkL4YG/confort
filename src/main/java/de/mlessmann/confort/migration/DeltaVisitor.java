package de.mlessmann.confort.migration;

import de.mlessmann.confort.antlr.DeltaDescriptorParserBaseVisitor;
import de.mlessmann.confort.migration.nodes.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import static de.mlessmann.confort.antlr.DeltaDescriptorParser.*;

public class DeltaVisitor extends DeltaDescriptorParserBaseVisitor<DeltaDescriptorNode> {

    @Override
    public DeltaDescriptorNode visit(ParseTree tree) {

        if (tree instanceof DeltaDescriptorContext) {
            return visitDeltaDescriptor(((DeltaDescriptorContext) tree));
        }

        return throwUnmatched(tree);
    }

    @Override
    public DeltaDescriptorNode visitDeltaDescriptor(DeltaDescriptorContext ctx) {
        DeltaBlockNode contextNode = new DeltaBlockNode();

        if (ctx.commandBlock() != null) {
            ctx.commandBlock()
                    .stream()
                    .map(this::visitCommandBlock)
                    .forEach(contextNode::appendChild);
        } else {
            return throwUnmatched(ctx);
        }

        return contextNode;
    }

    @Override
    public DeltaDescriptorNode visitCommandBlock(CommandBlockContext ctx) {
        if (ctx.assignment() != null) {
            return visitAssignment(ctx.assignment());
        } else if (ctx.command() != null) {
            return visitCommand(ctx.command());
        } else {
            return throwUnmatched(ctx);
        }
    }

    @Override
    public DeltaDescriptorNode visitAssignment(AssignmentContext ctx) {
        String identifier = ctx.IDENTIFIER().getText();
        String value = unquote(ctx.QUOTED_STRING());

        return new AssignmentNode(
                ctx.OP_WEAK_EQ() != null,
                identifier,
                value
        );
    }

    @Override
    public DeltaDescriptorNode visitCommand(CommandContext ctx) {
        if (ctx.descriptor() != null) {
            return visitDescriptor(ctx.descriptor());

        } else if (ctx.metaCommand() != null) {
            return visitMetaCommand(ctx.metaCommand());

        } else {
            return throwUnmatched(ctx);
        }
    }

    @Override
    public DeltaDescriptorNode visitDescriptor(DescriptorContext ctx) {
        OpArgument leftLocation = visitOperationArg(ctx.operationArgument(0));
        OpArgument rightLocation = null;
        OperationNode.OpCode code = visitOperation(ctx.reversableOP());

        if (ctx.operationArgument().size() > 1) {
            rightLocation = visitOperationArg(ctx.operationArgument(1));
        }

        return new OperationNode(
                leftLocation,
                rightLocation,
                code
        );
    }

    public OpArgument visitOperationArg(OperationArgumentContext ctx) {
        if (ctx.method_identifier() != null) {
            return visitMethodIdentifier(ctx.method_identifier());

        } else if (ctx.node_location() != null) {
            return visitNodeLocation(ctx.node_location());

        } else {
            throwUnmatched(ctx);
        }
        return null;
    }

    public OpArgument visitMethodIdentifier(Method_identifierContext ctx) {
        String identifier = ctx.getText();
        return new OpArgument(identifier);
    }

    public OpArgument visitNodeLocation(Node_locationContext ctx) {
        String identifier = unquote(ctx.QUOTED_STRING());

        return new OpArgument(
                ctx.META_RELATIVE() != null,
                identifier
        );
    }

    private OperationNode.OpCode visitOperation(ReversableOPContext ctx) {
        if (ctx.OP_DROP() != null) {
            return OperationNode.OpCode.DROP;
        } else if (ctx.OP_GENERATE() != null) {
            return OperationNode.OpCode.GENERATE;
        } else if (ctx.OP_MERGE_APPEND() != null) {
            return OperationNode.OpCode.MERGE_APPEND;
        } else if (ctx.OP_MERGE_PREPEND() != null) {
            return OperationNode.OpCode.MERGE_PREPEND;
        } else if (ctx.OP_MOVE() != null) {
            return OperationNode.OpCode.MOVE;
        } else {
            throwUnmatched(ctx);
        }

        return null;
    }

    private String unquote(TerminalNode node) {
        return node.getText().substring(1, node.getText().length() - 1);
    }

    private DeltaDescriptorNode throwUnmatched(ParseTree tree) {
        String message = String.format(
                "Unmatched context encountered during visit: %s at %s",
                tree.getClass().getSimpleName(),
                tree.getSourceInterval().toString()
        );
        throw new RuntimeException(message);
    }
}
