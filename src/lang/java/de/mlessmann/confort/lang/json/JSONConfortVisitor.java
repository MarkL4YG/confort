package de.mlessmann.confort.lang.json;

import de.mlessmann.confort.node.ConfigNode;
import de.mlessmann.confort.antlr.JSONParser;
import de.mlessmann.confort.antlr.JSONParserBaseVisitor;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.lang.ParseVisitException;
import de.mlessmann.confort.lang.UnmatchedContextException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class JSONConfortVisitor extends JSONParserBaseVisitor<IConfigNode> {

    @Override
    public IConfigNode visit(ParseTree tree) {
        if (tree instanceof JSONParser.JsonContext) {
            return visitJson(((JSONParser.JsonContext) tree));
        }

        return throwUnmatched(tree);
    }

    @Override
    public IConfigNode visitJson(JSONParser.JsonContext ctx) {
        if (ctx.value() != null) {
            return visitValue(ctx.value());
        }

        return throwUnmatched(ctx);
    }

    @Override
    public IConfigNode visitValue(JSONParser.ValueContext ctx) {
        if (ctx.array() != null) {
            return visitArray(ctx.array());
        } else if (ctx.obj() != null) {
            return visitObj(ctx.obj());
        }

        IConfigNode node = new ConfigNode();

        if (ctx.LIT_FALSE() != null) {
            node.setBoolean(false);
            return node;

        } else if (ctx.LIT_TRUE() != null) {
            node.setBoolean(true);
            return node;

        } else if (ctx.LIT_NULL() != null) {
            return node;

        } else if (ctx.NUMBER() != null) {
            return parseNumber(ctx.NUMBER(), node);

        } else if (ctx.STRING() != null) {
            return parseString(ctx.STRING(), node);
        }

        return throwUnmatched(ctx);
    }

    public IConfigNode parseNumber(TerminalNode numberTerminalNode, IConfigNode node) {
        String text = numberTerminalNode.getText();

        try {
            if (text.contains(".")) {
                Double value = Double.parseDouble(text);
                node.setDouble(value);
            } else {
                Integer value = Integer.parseInt(text);
                node.setInteger(value);
            }
        } catch (NumberFormatException e) {
            throw new ParseVisitException("Failed to parse numeric value!", e);
        }

        return node;
    }

    private IConfigNode parseString(TerminalNode ctx, IConfigNode node) {
        String str = unquoteString(ctx.getText());

        // Certain numeric values cannot natively be represented by JSON.
        // We work around that with special strings:
        if (str.startsWith("__NaN")) {
            if (str.endsWith("__f")) {
                node.setFloat(Float.NaN);
                return node;
            } else if (str.endsWith("__d")) {
                node.setDouble(Double.NaN);
                return node;
            }

        } else if (str.startsWith("__Inf")) {
            if (str.endsWith("__f")) {
                node.setFloat(str.contains("-") ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY);
                return node;
            } else if (str.endsWith("__d")) {
                node.setDouble(str.contains("-") ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
                return node;
            }
        }

        node.setString(str);
        return node;
    }

    private String unquoteString(String str) {
        if (str.startsWith("\"") && str.endsWith("\"")) {
            str = str.substring(1, str.length() - 1);
        }

        return str;
    }

    @Override
    public IConfigNode visitObj(JSONParser.ObjContext ctx) {
        IConfigNode node = new ConfigNode();

        ctx.pair().forEach(pair -> {
            String key = unquoteString(pair.STRING().getText());
            node.put(key, visitValue(pair.value()));
        });

        return node;
    }

    @Override
    public IConfigNode visitArray(JSONParser.ArrayContext ctx) {
        IConfigNode node = new ConfigNode();

        ctx.value().stream()
                .map(this::visitValue)
                .forEach(node::append);

        return node;
    }

    private IConfigNode throwUnmatched(ParseTree tree) throws ParseVisitException {
        String message = String.format("Unmatched element! %s", tree.getClass().getSimpleName());
        UnmatchedContextException ex = new UnmatchedContextException(message);
        ex.setContext(tree);
        throw ex;
    }
}
