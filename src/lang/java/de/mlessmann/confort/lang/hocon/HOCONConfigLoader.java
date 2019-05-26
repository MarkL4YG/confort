package de.mlessmann.confort.lang.hocon;

import de.mlessmann.confort.antlr.HOCONLexer;
import de.mlessmann.confort.antlr.HOCONParser;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.api.lang.IConfigSerializer;
import de.mlessmann.confort.format.FormatRef;
import de.mlessmann.confort.lang.AntlrConfigLoader;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

@FormatRef(
        shortName = "hocon",
        mimeTypes = {"application/hocon"},
        fileExtensions = {"hocon"}
)
public class HOCONConfigLoader extends AntlrConfigLoader<HOCONLexer, HOCONParser> {

    private static final HOCONConfortVisitor VISITOR = new HOCONConfortVisitor();

    private HOCONSerializer serializer = new HOCONSerializer();

    @Override
    protected HOCONLexer produceLexer(CharStream in) throws IOException {
        return new HOCONLexer(in);
    }

    @Override
    protected HOCONParser produceParser(CommonTokenStream tokenStream) throws IOException {
        return new HOCONParser(tokenStream);
    }

    @Override
    protected ParseTree parse(HOCONParser parser) {
        return parser.hocon();
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
