package de.mlessmann.confort.migration;

import de.mlessmann.confort.antlr.DeltaDescriptorLexer;
import de.mlessmann.confort.antlr.DeltaDescriptorParser;
import de.mlessmann.confort.api.IConfigNode;
import de.mlessmann.confort.migration.nodes.DeltaDescriptorNode;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class ConfigurationMigrator {

    private final Charset charset = Charset.forName("UTF-8");

    private DescriptorScope rootScope = new DescriptorScope();
    private DeltaDescriptorNode descriptor;

    public void loadDescriptor(InputStream inputStream) throws IOException {
        CharStream charStream = CharStreams.fromStream(inputStream, charset);
        DeltaDescriptorLexer lexer = new DeltaDescriptorLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        DeltaDescriptorParser parser = new DeltaDescriptorParser(tokenStream);
        DeltaVisitor deltaVisitor = new DeltaVisitor();
        descriptor = deltaVisitor.visit(parser.deltaDescriptor());
        descriptor.readAhead(rootScope);
    }

    public void runMigration(IConfigNode root) {
        descriptor.executeOn(root, root, rootScope);
    }
}
