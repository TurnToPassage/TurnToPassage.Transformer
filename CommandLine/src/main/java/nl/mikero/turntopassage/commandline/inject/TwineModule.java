package nl.mikero.turntopassage.commandline.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import nl.mikero.turntopassage.core.TwineArchiveRepairer;
import nl.mikero.turntopassage.core.TwinePublishedRepairer;
import nl.mikero.turntopassage.core.TwineRepairer;
import nl.mikero.turntopassage.core.embedder.EmbedderFactory;
import nl.mikero.turntopassage.core.embedder.HashEmbedderFactory;
import nl.mikero.turntopassage.core.embedder.ImageEmbedder;
import nl.mikero.turntopassage.core.embedder.ResourceEmbedder;
import nl.mikero.turntopassage.core.transformer.TwineStoryEpubTransformer;
import nl.mikero.turntopassage.core.inject.ArchiveRepairer;
import nl.mikero.turntopassage.core.inject.PublishedRepairer;
import nl.mikero.turntopassage.core.pegdown.plugin.TwineLinkParser;
import nl.mikero.turntopassage.core.pegdown.plugin.TwineLinkRenderer;
import nl.mikero.turntopassage.core.pegdown.plugin.TwineLinkSerializer;
import org.apache.commons.codec.digest.DigestUtils;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.pegdown.plugins.PegDownPlugins;

public class TwineModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TwineRepairer.class).annotatedWith(ArchiveRepairer.class).to(TwineArchiveRepairer.class);
        bind(TwineRepairer.class).annotatedWith(PublishedRepairer.class).to(TwinePublishedRepairer.class);
    }

    @Provides
    EmbedderFactory provideEmbedderFactoy() {
        return new HashEmbedderFactory(DigestUtils.getSha256Digest());
    }

    @Provides
    ImageEmbedder provideImageEmbedder() {
        return new ImageEmbedder(DigestUtils.getSha256Digest());
    }

    @Provides
    PegDownProcessor providePegDownProcessor(TwineLinkSerializer twineLinkSerializer) {
        return new PegDownProcessor(Extensions.WIKILINKS, PegDownPlugins.builder()
                .withPlugin(TwineLinkParser.class)
                .withHtmlSerializer(twineLinkSerializer)
                .build()
        );
    }

    @Provides
    TwineStoryEpubTransformer provideTwineStoryEpubTransformer(PegDownProcessor pegDownProcessor, TwineLinkRenderer twineLinkRenderer, ResourceEmbedder resourceEmbedder) {
        return new TwineStoryEpubTransformer(pegDownProcessor, twineLinkRenderer, resourceEmbedder);
    }

}
