package no.dcat.harvester.dcat.domain.theme.builders;

import no.difi.dcat.datastore.domain.dcat.SkosCode;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for CodeBuildersTest
 */
public class CodeBuildersTest {

    @Test
    public void testModel() throws IOException {
        Model model = createModel();
        List<SkosCode> codes = new CodeBuilders(model).build();

        assertEquals("http://data.brreg.no/datakatalog/provinens/vedtak", codes.get(0).getCode());
        assertEquals("Governmental decisions", codes.get(0).getTitle().get("en"));
        assertEquals("Statlig vedtak", codes.get(0).getTitle().get("nb"));
        assertEquals("Statlig vedtak", codes.get(0).getTitle().get("nn"));

        assertEquals("http://data.brreg.no/datakatalog/provinens/bruker", codes.get(1).getCode());
        assertEquals("User collection", codes.get(1).getTitle().get("en"));
        assertEquals("Brukerinnsamling", codes.get(1).getTitle().get("nb"));
        assertEquals("Brukerinnsamling", codes.get(1).getTitle().get("nn"));
    }

    private Model createModel() throws IOException {
        String defultPath = new File(".").getCanonicalPath().toString();
        String fileWithPath = String.format("file:%s/src/test/resources/rdf/%s", defultPath, "provenance.rdf");

        final Model model = ModelFactory.createDefaultModel();
        model.read(fileWithPath);
        return model;
    }
}