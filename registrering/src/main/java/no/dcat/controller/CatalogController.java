package no.dcat.controller;

import no.dcat.model.Catalog;
import no.dcat.model.Dataset;
import no.dcat.model.Publisher;
import no.dcat.service.CatalogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping(value = "/catalogs")
public class CatalogController {

    private static Logger logger = LoggerFactory.getLogger(CatalogController.class);

    @Autowired
    private CatalogRepository catalogRepository;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public HttpEntity<PagedResources<Dataset>> listCatalogs(Pageable pageable, PagedResourcesAssembler assembler) {
        Page<Catalog> datasets = catalogRepository.findAll(pageable);
        return new ResponseEntity<>(assembler.toResource(datasets), HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity<Catalog> addCatalog(@RequestBody Catalog catalog) {
        logger.info("Add/modify catalog: " + catalog.toString());
        if(catalog.getId() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://data.brreg.no/enhetsregisteret/enhet/" + catalog.getId() + ".json";
        Enhet enhet = restTemplate.getForObject(uri, Enhet.class);

        Publisher publisher = new Publisher();
        publisher.setId(catalog.getId());
        publisher.setName(enhet.getNavn());
        publisher.setUri(uri);
        catalog.setPublisher(publisher);

        Catalog savedCatalog = catalogRepository.save(catalog);
        return new ResponseEntity<>(savedCatalog, HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public HttpEntity<Catalog> getCatalog(@PathVariable("id") String id) {
        Catalog catalog = catalogRepository.findOne(id);

        if (catalog == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(catalog, HttpStatus.OK);
    }
}