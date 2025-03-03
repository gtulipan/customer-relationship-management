package hu._ig.crm.crm4ig.util;

import hu._ig.crm.crm4ig.model.PartnerExportDto;
import hu._ig.crm.crm4ig.domain.Partner;
import hu._ig.crm.crm4ig.domain.Address;
import hu._ig.crm.crm4ig.utils.PartnerExporter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PartnerExporterTest {

    @Test
    void testExportPartnersWithAddresses() {
        var partner = new Partner();
        partner.setName("Teszt Elek");
        partner.setEmail("teszt.elek@example.com");

        Address address = new Address();
        address.setCountry("Hungary");
        address.setCity("Budapest");
        address.setStreet("Fő utca");
        address.setHouseNumber("1");
        partner.setAddresses(Set.of(address));

        List<Partner> partners = List.of(partner);

        List<PartnerExportDto> exportDtos = PartnerExporter.exportPartnersWithAddresses(partners);

        assertEquals(1, exportDtos.size());
        PartnerExportDto dto = exportDtos.get(0);
        assertEquals("Teszt Elek", dto.getPartnerName());
        assertEquals("teszt.elek@example.com", dto.getPartnerEmail());
        assertEquals("Hungary", dto.getCountry());
        assertEquals("Budapest", dto.getCity());
        assertEquals("Fő utca", dto.getStreet());
        assertEquals("1", dto.getHouseNumber());
    }

    @Test
    void testExportPartnersToPdf() throws Exception {
        Partner partner = new Partner();
        partner.setName("Teszt Elek");
        partner.setEmail("teszt.elek@example.com");

        Address address = new Address();
        address.setCountry("Hungary");
        address.setCity("Budapest");
        address.setStreet("Fő utca");
        address.setHouseNumber("1");
        partner.setAddresses(Set.of(address));

        List<Partner> partners = List.of(partner);

        byte[] pdfBytes = PartnerExporter.exportPartnersToPdf(partners);

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);

        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            assertEquals(1, document.getNumberOfPages());
        }
    }
}
