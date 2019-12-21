package nl.mikero.spiner.core;

import nl.mikero.spiner.core.transformer.TransformService;
import nl.mikero.spiner.core.transformer.epub.TwineStoryEpubTransformer;
import nl.mikero.spiner.core.transformer.epub.embedder.EmbedderFactory;
import nl.mikero.spiner.core.transformer.epub.embedder.HashEmbedderFactory;
import nl.mikero.spiner.core.transformer.epub.embedder.ResourceEmbedder;
import nl.mikero.spiner.core.twine.TwineArchiveParser;
import nl.mikero.spiner.core.twine.TwinePublishedRepairer;
import nl.mikero.spiner.core.twine.extended.ExtendTwineXmlTransformer;
import nl.mikero.spiner.core.twine.markdown.PegdownTransitionMarkdownRenderParser;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.output.TeeOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.function.BiConsumer;

public class OracleTest {
    private InputStream givenTwineStory;

    private TransformService transformerService;
    private TwineStoryEpubTransformer twineStoryEpubTransformer;
    private EpubReader epubReader;

    private Book actualBook;

    @Before
    public void setUp() {
        TwinePublishedRepairer publishedRepairer = new TwinePublishedRepairer();
        ExtendTwineXmlTransformer extendTwineXmlTransformer = new ExtendTwineXmlTransformer();
        TwineArchiveParser twineArchiveParser = new TwineArchiveParser();

        transformerService = new TransformService(publishedRepairer, extendTwineXmlTransformer, twineArchiveParser);

        PegdownTransitionMarkdownRenderParser parser = new PegdownTransitionMarkdownRenderParser();
        EmbedderFactory embedderFactory = new HashEmbedderFactory(DigestUtils.getSha256Digest());
        ResourceEmbedder resourceEmbedder = new ResourceEmbedder(embedderFactory);
        twineStoryEpubTransformer = new TwineStoryEpubTransformer(parser, resourceEmbedder);

        epubReader = new EpubReader();
    }

    @After
    public void tearDown() throws IOException {
        if (givenTwineStory != null) {
            givenTwineStory.close();
        }
    }

    @Test
    public void testFeatures() throws IOException {
        testOracleFile("../example/Features.html", "../example/Features.epub");
    }

    @Test
    public void testParagraphs() throws IOException {
        testOracleFile("src/test/resources/stories/paragraphs.html", "src/test/resources/epub/paragraphs.epub");
    }

    @Test
    public void testHeaders() throws IOException {
        testOracleFile("src/test/resources/stories/headers.html", "src/test/resources/epub/headers.epub");
    }

    @Test
    public void testEmphasis() throws IOException {
        testOracleFile("src/test/resources/stories/emphasis.html", "src/test/resources/epub/emphasis.epub");
    }

    @Test
    public void testLists() throws IOException {
        testOracleFile("src/test/resources/stories/lists.html", "src/test/resources/epub/lists.epub");
    }

    @Test
    public void testLinks() throws IOException {
        testOracleFile("src/test/resources/stories/links.html", "src/test/resources/epub/links.epub");
    }

    public void testRule() throws IOException {
        testOracleFile("src/test/resources/stories/rule.html", "src/test/resources/epub/rule.epub");
    }

    private void testOracleFile(String inputFile, String oracleFile) throws IOException {
        givenATwineStory(inputFile);
        whenTransformedToEpub(oracleFile);
        thenItsMetadataShouldMatch(oracleFile);
        thenItsPagesShouldMatch(oracleFile);
        thenItsStylesheetShouldMatch(oracleFile);
    }

    private void givenATwineStory(String twineStoryFilePath) throws FileNotFoundException {
        System.out.println( System.getProperty("user.dir"));
        givenTwineStory = new FileInputStream(twineStoryFilePath);
    }

    private void whenTransformedToEpub(String targetFile) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        OutputStream outputStream = byteOutputStream;
        if (shouldUpdateOracleFile()) {
            outputStream = new TeeOutputStream(outputStream, new FileOutputStream(targetFile));
        }
        transformerService.transform(givenTwineStory, outputStream, twineStoryEpubTransformer);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteOutputStream.toByteArray());
        actualBook = epubReader.readEpub(inputStream);

        outputStream.close();
        inputStream.close();
    }

    private void thenItsPagesShouldMatch(String epubFilePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(epubFilePath);
        Book expectedBook = epubReader.readEpub(fileInputStream);

        for(Resource expectedResource : expectedBook.getResources().getResourcesByMediaType(MediatypeService.XHTML)) {
            Resource actualResource = actualBook.getResources().getByHref(expectedResource.getHref());
            Assert.assertNotNull(actualResource);
            Assert.assertEquals(new String(expectedResource.getData()), new String(actualResource.getData()));
        }
        Assert.assertEquals(expectedBook.getResources().size(), actualBook.getResources().size());

        fileInputStream.close();
    }

    private void thenItsMetadataShouldMatch(String epubFilePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(epubFilePath);
        Book expectedBook = epubReader.readEpub(fileInputStream);

        Metadata expectedMetadata = expectedBook.getMetadata();
        Metadata actualMetadata = actualBook.getMetadata();

        assertListEquals("identifiers", expectedMetadata.getIdentifiers(), actualMetadata.getIdentifiers(), (expected, actual) -> {
            if (expected.getScheme().equals("UUID")) {
                return;
            }

            Assert.assertEquals("identifiers.scheme", expected.getScheme(), actual.getScheme());
            Assert.assertEquals("identifiers.value", expected.getValue(), actual.getValue());
        });
        assertListEquals("titles", expectedMetadata.getTitles(), actualMetadata.getTitles(), Assert::assertEquals);
        Assert.assertEquals("language", expectedMetadata.getLanguage(), actualMetadata.getLanguage());
        assertListEquals("contributors", expectedMetadata.getContributors(), actualMetadata.getContributors(), Assert::assertEquals);
        assertListEquals("authors", expectedMetadata.getAuthors(), actualMetadata.getAuthors(), Assert::assertEquals);
        assertListEquals("dates", expectedMetadata.getDates(), actualMetadata.getDates(), (expected, actual) -> {
            Assert.assertEquals("dates.event", expected.getEvent(), actual.getEvent());
            Assert.assertEquals("dates.value", expected.getValue(), actual.getValue());
        });
        assertListEquals("descriptions", expectedMetadata.getDescriptions(), actualMetadata.getDescriptions(), Assert::assertEquals);
        Assert.assertEquals("formats", expectedMetadata.getFormat(), actualMetadata.getFormat());
        assertListEquals("publishers", expectedMetadata.getPublishers(), actualMetadata.getPublishers(), Assert::assertEquals);
        assertListEquals("rights", expectedMetadata.getRights(), actualMetadata.getRights(), Assert::assertEquals);
        assertListEquals("subjects", expectedMetadata.getSubjects(), actualMetadata.getSubjects(), Assert::assertEquals);
        assertListEquals("types", expectedMetadata.getTypes(), actualMetadata.getTypes(), Assert::assertEquals);

        fileInputStream.close();
    }

    private void thenItsStylesheetShouldMatch(String epubFilePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(epubFilePath);
        Book expectedBook = epubReader.readEpub(fileInputStream);

        for(Resource expectedResource : expectedBook.getResources().getResourcesByMediaType(MediatypeService.CSS)) {
            Resource actualResource = actualBook.getResources().getByHref(expectedResource.getHref());
            Assert.assertNotNull(actualResource);
            Assert.assertEquals(new String(expectedResource.getData()), new String(actualResource.getData()));
        }
        Assert.assertEquals(expectedBook.getResources().size(), actualBook.getResources().size());

        fileInputStream.close();
    }

    private <T> void assertListEquals(String metadataName, List<T> expectedList, List<T> actualList, BiConsumer<T, T> assertion) {
        for (int i = 0; i < expectedList.size(); i++) {
            T expectedValue = expectedList.get(i);
            T actualValue = actualList.get(i);

            assertion.accept(expectedValue, actualValue);
        }
        Assert.assertEquals(metadataName, expectedList.size(), actualList.size());
    }

    private boolean shouldUpdateOracleFile() {
        return System.getenv("UPDATE_ORACLE") != null;
    }
}