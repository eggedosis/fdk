package no.dcat.harvester.crawler;

import no.dcat.harvester.crawler.handlers.FusekiResultHandler;
import no.difi.dcat.datastore.AdminDataStore;
import no.difi.dcat.datastore.DcatDataStore;
import no.difi.dcat.datastore.domain.DcatSource;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by bjg on 25.01.2017.
 */
public class CrawlerCodeJobTest {
    @Test
    public void testCrawlerCodeJob() {
        DcatSource dcatSource = new DcatSource("http//dcat.difi.no/test", "Test", "src/test/resources/rdf/access-right-skos.rdf", "tester", "");

        DcatDataStore dcatDataStore = Mockito.mock(DcatDataStore.class);
        AdminDataStore adminDataStore = Mockito.mock(AdminDataStore.class);

        FusekiResultHandler handler = new FusekiResultHandler(dcatDataStore, null);

        handler.process(dcatSource, ModelFactory.createDefaultModel());

        CrawlerCodeJob job = new CrawlerCodeJob("rdf/access-right-skos.rdf", handler);
        job.run();
    }
}
