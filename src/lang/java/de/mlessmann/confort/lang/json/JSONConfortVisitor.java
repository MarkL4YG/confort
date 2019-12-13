package de.mlessmann.confort.lang.json;

import de.mlessmann.confort.lang.codepoint.EscapeMachine;
import de.mlessmann.confort.lang.except.FileFormatException;
import de.mlessmann.confort.node.ConfigNode;
import de.mlessmann.confort.antlr.JSONParser;
import de.mlessmann.confort.antlr.JSONParserBaseVisitor;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.lang.ParseVisitException;
import de.mlessmann.confort.lang.UnmatchedContextException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

public class JSONConfortVisitor extends JSONParserBaseVisitor<IConfigNode> {

    private EscapeMachine escapeMachine = new JSONEscapeMachine();

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
        try {
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

            } else if (ctx.EXTRA_NOT_A_NUMBER() != null) {
                return parseExtraNaN(ctx.EXTRA_NOT_A_NUMBER().getText(), node);

            } else if (ctx.EXTRA_POSITIVE_INFINITY() != null) {
                return parseExtraInfinity(false, ctx.EXTRA_POSITIVE_INFINITY().getText(), node);

            } else if (ctx.EXTRA_NEGATIVE_INFINITY() != null) {
                return parseExtraInfinity(true, ctx.EXTRA_NEGATIVE_INFINITY().getText(), node);

            } else if (ctx.STRING() != null) {
                return parseString(ctx.STRING(), node);
            }
        } catch (NumberFormatException e) {
            throw new FileFormatException(
                    ctx.getStart().getLine(),
                    ctx.getStart().getCharPositionInLine(),
                    ctx.getStart().getTokenSource().getSourceName(),
                    e.getMessage(),
                    e
            );
        }

        return throwUnmatched(ctx);
    }

    private IConfigNode parseExtraNaN(String str, IConfigNode node) {
        if (str.endsWith("_f\"")) {
            node.setFloat(Float.NaN);
        } else if (str.endsWith("_d\"")) {
            node.setDouble(Double.NaN);
        } else {
            throw new NumberFormatException("Could not determine number type for NaN value: " + str);
        }

        return node;
    }

    private IConfigNode parseExtraInfinity(boolean negative, String str, IConfigNode node) {
        if (str.endsWith("_f\"")) {
            node.setFloat(negative ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY);
        } else if (str.endsWith("_d\"")) {
            node.setDouble(negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
        } else {
            throw new NumberFormatException("Could not determine number type for Infinity value: " + str);
        }
        return node;
    }

    public IConfigNode parseNumber(TerminalNode numberTerminalNode, IConfigNode node) {
        String text = numberTerminalNode.getText();

        if (text.contains(".")) {
            Double value = Double.parseDouble(text);
            node.setDouble(value);
        } else {
            Integer value = Integer.parseInt(text);
            node.setInteger(value);
        }

        return node;
    }

    private IConfigNode parseString(TerminalNode ctx, IConfigNode node) {
        String str = unquoteString(ctx.getText());
        str = escapeMachine.unescape(str);
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

        if (ctx.pair() == null) {
            throw new FileFormatException(
                    ctx.getStart().getLine(),
                    ctx.getStart().getCharPositionInLine(),
                    ctx.getStart().getTokenSource().getSourceName(),
                    "Object context is not a pair!"
            );
        }

        ctx.pair().forEach(pair -> {
            TerminalNode stringNode = pair.STRING();
            if (stringNode == null) {
                throw new FileFormatException(
                        pair.getStart().getLine(),
                        pair.getStart().getCharPositionInLine(),
                        pair.getStart().getTokenSource().getSourceName(),
                        "Missing string key for object context!"
                );
            }

            String key = unquoteString(stringNode.getText());
            node.put(key, visitValue(pair.value()));
        });

        return node;
    }

    @Override
    public IConfigNode visitArray(JSONParser.ArrayContext ctx) {
        if (ctx.value() == null) {
            throw new FileFormatException(
                    ctx.getStart().getLine(),
                    ctx.getStart().getCharPositionInLine(),
                    ctx.getStart().getTokenSource().getSourceName(),
                    "Array node does not contain a values!"
            );
        }

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
