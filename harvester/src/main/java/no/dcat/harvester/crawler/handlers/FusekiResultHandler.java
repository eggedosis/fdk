package no.dcat.harvester.crawler.handlers;

import no.dcat.harvester.crawler.CrawlerResultHandler;
import no.difi.dcat.datastore.AdminDataStore;
import no.difi.dcat.datastore.DcatDataStore;
import no.difi.dcat.datastore.Fuseki;
import no.difi.dcat.datastore.domain.DcatSource;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FusekiResultHandler implements CrawlerResultHandler {

	private final DcatDataStore dcatDataStore;
	private final AdminDataStore adminDataStore; //TODO: remove this?

	private final Logger logger = LoggerFactory.getLogger(FusekiResultHandler.class);

	public FusekiResultHandler(DcatDataStore dcatDataStore, AdminDataStore adminDataStore) {
		this.dcatDataStore = dcatDataStore;
		this.adminDataStore = adminDataStore;

	}

	public FusekiResultHandler(String serviceUriDcatDataStore, String serviceUriAdminDataStore) {
		this.dcatDataStore = new DcatDataStore(new Fuseki(serviceUriDcatDataStore));
		this.adminDataStore = new AdminDataStore(new Fuseki(serviceUriAdminDataStore));

	}

	public void process(DcatSource dcatSource, Model model) {
		logger.trace("Starting processing of rdf results");
		dcatDataStore.saveDataCatalogue(dcatSource, model);
		logger.trace("Finished processing of rdf results");
	}
}





