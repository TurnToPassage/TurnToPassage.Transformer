package nl.mikero.spiner.core.transformer.latex.pegdown;

import org.junit.Before;
import org.junit.Test;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.ast.*;

import static org.junit.Assert.*;

public class ToLatexSerializerTest {
    private PegDownProcessor pegDownProcessor;
    private ToLatexSerializer serializer;

    @Before
    public void setUp() {
        pegDownProcessor = new PegDownProcessor();
        serializer = new ToLatexSerializer(new LinkRenderer());
    }

    @Test
    public void toLatex_PlainText_ReturnsSameText() {
        // Arrange
        String markdown = "This is just plain text.";
        RootNode rootNode = pegDownProcessor.parseMarkdown(markdown.toCharArray());

        // Act
        String result = serializer.toLatex(rootNode);

        // Assert
        assertEquals(markdown, result);
    }

    @Test
    public void toLatex_Bold_TextBf() {
        // Arrange
        String markdown = "**bold**";
        RootNode rootNode = pegDownProcessor.parseMarkdown(markdown.toCharArray());

        // Act
        String result = serializer.toLatex(rootNode);

        // Assert
        assertEquals("\\textbf{bold}", result);
    }

    @Test
    public void toLatex_Italic_Emph() {
        // Arrange
        String markdown = "*italic*";
        RootNode rootNode = pegDownProcessor.parseMarkdown(markdown.toCharArray());

        // Act
        String result = serializer.toLatex(rootNode);

        // Assert
        assertEquals("\\emph{italic}", result);
    }
}