package de.mlessmann.confort.lang;

import de.mlessmann.confort.api.IConfigNode;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;

public abstract class AntlrConfigLoader<L extends Lexer, P extends Parser> extends ConfigLoader {

    private static final Logger logger = LoggerFactory.getLogger(AntlrConfigLoader.class);

    public IConfigNode parse(Reader input) throws IOException {
        CharStream charStream = CharStreams.fromReader(input);

        L lexer = produceLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        P parser = produceParser(tokens);

        // Use 2-stage parsing for expression performance
        // https://github.com/antlr/antlr4/blob/master/doc/faq/general.md#why-is-my-expression-parser-slow
        try {
            //STAGE 1
            logger.debug("Trying to run STAGE 1 parsing. (SSL prediction)");
            parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
            return traverse(parse(parser));
        } catch (Exception ex) {
            // STAGE 2
            logger.debug("Trying to run STAGE 2 parsing. (LL prediction)", ex);
            tokens.seek(0); // rewind input stream
            parser.reset();
            parser.getInterpreter().setPredictionMode(PredictionMode.LL);
            return traverse(parse(parser));
            // if we parse ok, it's LL not SLL
        }
    }

    protected abstract L produceLexer(CharStream in) throws IOException;

    protected abstract P produceParser(CommonTokenStream tokenStream) throws IOException;

    protected abstract ParseTree parse(P parser);

    protected abstract IConfigNode traverse(ParseTree root);
}
