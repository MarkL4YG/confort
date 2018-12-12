package de.mlessmann.confort;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public abstract class ConfigLoader<L extends Lexer, P extends Parser> {

    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);

    private static final Charset CHARSET = Charset.forName("UTF-8");

    public void parse(InputStream input) throws IOException {
        CharStream charStream = CharStreams.fromStream(input, CHARSET);

        L lexer = produceLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        P parser = produceParser(tokens);

        // Use 2-stage parsing for expression performance
        // https://github.com/antlr/antlr4/blob/master/doc/faq/general.md#why-is-my-expression-parser-slow
        try {
            //STAGE 1
            logger.debug("Trying to run STAGE 1 parsing. (SSL prediction)");
            parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
            parse(parser);
        } catch (Exception ex) {
            logger.debug("Trying to run STAGE 2 parsing. (LL prediction)", ex);
            // STAGE 2
            tokens.seek(0); // rewind input stream
            parser.reset();
            parser.getInterpreter().setPredictionMode(PredictionMode.LL);
            parse(parser);
            // if we parse ok, it's LL not SLL
        }
    }

    protected abstract L produceLexer(CharStream in) throws IOException;

    protected abstract P produceParser(CommonTokenStream tokenStream) throws IOException;

    protected abstract void parse(P parser);
}
