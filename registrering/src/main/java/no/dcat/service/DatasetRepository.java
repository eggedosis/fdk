package no.dcat.service;

import no.dcat.model.Dataset;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DatasetRepository extends ElasticsearchRepository<Dataset, String> {


}
