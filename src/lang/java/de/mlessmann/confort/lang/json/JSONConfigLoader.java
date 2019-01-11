package de.mlessmann.confort.lang.json;

import de.mlessmann.confort.antlr.JSONLexer;
import de.mlessmann.confort.antlr.JSONParser;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.lang.IConfigSerializer;
import de.mlessmann.confort.format.FormatRef;
import de.mlessmann.confort.lang.AntlrConfigLoader;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

@FormatRef(
        shortName = "json",
        mimeTypes = {"application/json"},
        fileExtensions = {"json"}
)
public class JSONConfigLoader extends AntlrConfigLoader<JSONLexer, JSONParser> {

    private static final JSONConfortVisitor VISITOR = new JSONConfortVisitor();

    private JSONSerializer serializer = new JSONSerializer();

    @Override
    protected JSONLexer produceLexer(CharStream in) throws IOException {
        return new JSONLexer(in);
    }

    @Override
    protected JSONParser produceParser(CommonTokenStream tokenStream) throws IOException {
        return new JSONParser(tokenStream);
    }

    @Override
    protected ParseTree parse(JSONParser parser) {
        return parser.json();
    }

    @Override
    protected IConfigNode traverse(ParseTree root) {
        return VISITOR.visit(root);
    }

    @Override
    public IConfigSerializer getSerializer() {
        return serializer;
    }
}
