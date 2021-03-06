package no.difi.dcat.datastore.domain.dcat;

import no.difi.dcat.datastore.domain.DcatSource;
import no.difi.dcat.datastore.domain.dcat.builders.DatasetBuilder;
import no.difi.dcat.datastore.domain.dcat.vocabulary.DCAT;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by nodavsko on 01.11.2016.
 */
public class DatasetTest {

    static private Logger logger = LoggerFactory.getLogger(DatasetTest.class);

    private DcatSource dcatSource;
    private Dataset data;

    @Before
    public void setup() {
        URL url = this.getClass().getClassLoader().getResource("catalog.ttl");
        logger.info("Open file: " + url.toString());
        dcatSource = new DcatSource("http//dcat.no/test", "Test", url.toString(), "admin_user", "123456789");
        org.apache.jena.query.Dataset dataset = RDFDataMgr.loadDataset(dcatSource.getUrl());
        Model model = ModelFactory.createUnion(ModelFactory.createDefaultModel(), dataset.getDefaultModel());
        ResIterator catalogIterator = model.listResourcesWithProperty(RDF.type, DCAT.Catalog);
        ResIterator datasetIterator = model.listResourcesWithProperty(RDF.type, DCAT.Dataset);

        Resource catalogResource = catalogIterator.next();
        Resource datasetResource = datasetIterator.next();

        //Create codes.
        Map<String, Map<String, SkosCode>> codes = new HashMap<>();
        Map<String, SkosCode> locations = new HashMap<>();

        codes.put(Types.PROVENANCESTATEMENT.getType(), generateCode("statlig vedtak", "http://data.brreg.no/datakatalog/provinens/vedtak"));
        codes.put(Types.LINGUISTICSYSTEM.getType(), generateCode("norsk", "http://publications.europa.eu/resource/authority/language/2"));
        codes.put(Types.RIGHTSSTATEMENT.getType(), generateCode("Offentlig", "http://publications.europa.eu/resource/authority/access-right/PUBLIC"));
        codes.put(Types.FREQUENCY.getType(), generateCode("kontinuerlig", "http://publications.europa.eu/resource/authority/frequency/CONT"));
        locations = generateCode("Norge", "http://sws.geonames.org/3144096/");

        data = DatasetBuilder.create(datasetResource, catalogResource, locations, codes, new HashMap<>());
    }

    @Test
    public void publisherExists() {
        Publisher actual = data.getPublisher();
        Publisher expected = new Publisher();
        expected.setId("http://data.brreg.no/enhetsregisteret/enhet/974760673");
        expected.setName("Brønnøysundregistrene");

        logger.debug(actual.getId());
        logger.debug(actual.getName());

        Assert.assertEquals("Expects uri", expected.getId(), actual.getId());
        Assert.assertEquals("Expects name", expected.getName(), actual.getName());
    }

    @Test
    public void contactExists() {
        Contact actual = data.getContactPoint();
        Contact expected = new Contact();
        expected.setId("http://data.brreg.no/datakatalog/kontaktpunkt/4");
        expected.setFullname("Kontakt for Altinn");
        expected.setTelephone("tel:+4775007500");
        expected.setEmail("mailto:bjarne.base@brreg.no");
        expected.setOrganizationName("Skatt");
        expected.setOrganizationUnit("AAS");
        expected.setHasURL("httpd://skatt.no/schema");

        Assert.assertEquals("id expected", expected.getId(), actual.getId());
        Assert.assertEquals("id expected", expected.getFullname(), actual.getFullname());
        Assert.assertEquals("id expected", expected.getTelephone(), actual.getTelephone());
        Assert.assertEquals("id expected", expected.getEmail(), actual.getEmail());
        Assert.assertEquals("Org. name expected", expected.getOrganizationName(), actual.getOrganizationName());
        Assert.assertEquals("Org. unit expected", expected.getOrganizationUnit(), actual.getOrganizationUnit());
        Assert.assertEquals("Url expected", expected.getHasURL(), actual.getHasURL());
    }

    @Test
    public void datasetProperties() throws ParseException {
        Dataset expected = new Dataset();
        expected.setIdentifier(createListOfStrings("10"));
        expected.setSubject(createListOfStrings("http://brreg.no/begrep/orgnr"));

        SkosCode accrualPeriodicity = new SkosCode("http://publications.europa.eu/resource/authority/frequency/CONT", new HashMap<String, String>());
        accrualPeriodicity.getTitle().put("no", "kontinuerlig");
        expected.setAccrualPeriodicity(accrualPeriodicity);

        expected.setPage(createListOfStrings("https://www.brreg.no/lag-og-foreninger/registrering-i-frivillighetsregisteret/"));
        expected.setADMSIdentifier(createListOfStrings("http://data.brreg.no/identifikator/99"));
        expected.setType("Type");

        SkosCode accessRight = new SkosCode("http://publications.europa.eu/resource/authority/access-right/PUBLIC", new HashMap<String, String>());
        accessRight.getTitle().put("no", "Offentlig");
        expected.setAccessRights(accessRight);

        expected.setDescription(createMapOfStrings("Oversikt over lag og foreninger som er registrert i Frivillighetsregisteret.  Har som formål å bedre og forenkle samhandlingen mellom frivillige organisasjoner og offentlige myndigheter. Registeret skal sikre systematisk informasjon som kan styrke legitimiteten til og kunnskapen om den frivillige aktiviteten. Registeret er lagt til Brønnøysundregistrene og åpnet for registrering 2. desember 2008"));
        expected.setIssued(createDate("01-01-2009 00:00:00"));
        expected.setLandingPage("https://w2.brreg.no/frivillighetsregisteret/");

        SkosCode language = new SkosCode("http://publications.europa.eu/resource/authority/language/2", new HashMap<String,String>());
        language.getTitle().put("no", "norsk");
        expected.setLanguage(language);

        SkosCode provinance = new SkosCode("http://data.brreg.no/datakatalog/provinens/vedtak", new HashMap<String, String>());
        provinance.getTitle().put("no", "statlig vedtak");
        expected.setProvenance(provinance);

        expected.setTitle(createMapOfStrings("Frivillighetsregisteret"));

        expected.setSpatial(createListOfMaps("http://sws.geonames.org/3144096/", "Norge"));

        Assert.assertEquals(expected.getIdentifier(), data.getIdentifier());
        Assert.assertEquals(expected.getSubject(), data.getSubject());
        Assert.assertEquals(expected.getAccrualPeriodicity().getCode(), data.getAccrualPeriodicity().getCode());
        Assert.assertEquals(expected.getPage(), data.getPage());
        Assert.assertEquals(expected.getADMSIdentifier(), data.getADMSIdentifier());
        Assert.assertEquals(expected.getType(), data.getType());
        Assert.assertEquals(expected.getAccessRights().getCode(), data.getAccessRights().getCode());
        Assert.assertEquals(expected.getDescription().get("nb"), data.getDescription().get("nb"));
        Assert.assertEquals(expected.getIssued(), data.getIssued());
        Assert.assertEquals(expected.getLandingPage(), data.getLandingPage());
        Assert.assertEquals(expected.getLanguage().getCode(), data.getLanguage().getCode());
        Assert.assertEquals(expected.getProvenance().getCode(), data.getProvenance().getCode());
        Assert.assertEquals(expected.getSpatial().get(0).getCode(), data.getSpatial().get(0).getCode());
        Assert.assertEquals(expected.getSpatial().get(0).getTitle().get("no"), data.getSpatial().get(0).getTitle().get("no"));
        Assert.assertEquals(expected.getTitle(), data.getTitle());
    }

    private Map<String, SkosCode> generateCode(String norwegianTitle, String code) {
        Map titles = new HashMap();
        titles.put("no", norwegianTitle);

        SkosCode skosCode = new SkosCode(code, titles);
        Map<String, SkosCode> CodeMap = new HashMap<>();

        CodeMap.put(code, skosCode);
        return CodeMap;
    }

    private Date createDate(String dateInString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        return sdf.parse(dateInString);
    }

    private List<String> createListOfStrings(String data) {
        List list = new ArrayList<String>();
        list.add(data);
        return list;
    }

    private List<SkosCode> createListOfMaps(String code, String title) {
        SkosCode co = new SkosCode();
        co.setCode(code);

        Map<String, String> map = new HashMap<>();
        map.put("no", title);

        co.setTitle(map);

        List<SkosCode> list = new ArrayList<>();
        list.add(co);
        return list;
    }

    private Map<String, String> createMapOfStrings(String data) {
        Map map = new HashMap<String, String>();
        map.put("nb", data);
        return map;
    }
}
