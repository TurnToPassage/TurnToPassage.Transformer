package nl.mikero.turntopassage.core.embedder;

import com.google.inject.Inject;
import nl.siegmann.epublib.domain.Book;
import org.pegdown.ast.*;
import org.pegdown.plugins.ToHtmlSerializerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Runs through all nodes in a pegdown document and embeds any resource that
 * needs embedding.
 */
public class ResourceEmbedder implements Visitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceEmbedder.class);

    private final EmbedderFactory factory;

    private Book book;

    @Inject
    public ResourceEmbedder(EmbedderFactory factory) {
        this.factory = factory;
    }

    /**
     * Runs through all the children of the given root node and embeds any
     * nodes that are embeddable.
     *
     * @see Embedder
     * @param book book to embed resources in
     * @param rootNode root node to run through
     */
    public void embed(Book book, RootNode rootNode) {
        this.book = book;
        visit(rootNode);
        this.book = null;
    }

    @Override
    public void visit(RootNode node) {
        for (ReferenceNode refNode : node.getReferences()) {
            visitChildren(refNode);
        }
        for (AbbreviationNode abbrNode : node.getAbbreviations()) {
            visitChildren(abbrNode);
        }
        visitChildren(node);
    }

    @Override
    public void visit(AbbreviationNode node) { visitChildren(node);
    }

    @Override
    public void visit(BlockQuoteNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(BulletListNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(DefinitionListNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(DefinitionNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(DefinitionTermNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(ExpImageNode node) {
        try {
            Embedder embedder = factory.get(node);
            embedder.embed(this.book, createUrlFromString(node.url));
        } catch (IOException e) {
            LOGGER.error("Error during embedding of '{}'", node.url, e);
        }

        visitChildren(node);
    }

    private URL createUrlFromString(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void visit(ExpLinkNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(HeaderNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(ListItemNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(OrderedListNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(ParaNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(QuotedNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(ReferenceNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(RefImageNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(RefLinkNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(StrikeNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(StrongEmphSuperNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(TableBodyNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(TableCaptionNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(TableCellNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(TableColumnNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(TableHeaderNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(TableNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(TableRowNode node) {
        visitChildren(node);
    }

    @Override
    public void visit(SuperNode node) {
        visitChildren(node);
    }

    protected void visitChildren(SuperNode node) {
        for (Node child : node.getChildren()) {
            child.accept(this);
        }
    }

    @Override
    public void visit(AnchorLinkNode node) { }
    @Override
    public void visit(AutoLinkNode node) { }
    @Override
    public void visit(CodeNode node) { }
    @Override
    public void visit(HtmlBlockNode node) { }
    @Override
    public void visit(InlineHtmlNode node) { }
    @Override
    public void visit(MailLinkNode node) { }
    @Override
    public void visit(SimpleNode node) { }
    @Override
    public void visit(SpecialTextNode node) { }
    @Override
    public void visit(VerbatimNode node) { }
    @Override
    public void visit(WikiLinkNode node) { }
    @Override
    public void visit(TextNode node) { }
    @Override
    public void visit(Node node) { }
}