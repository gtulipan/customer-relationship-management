package hu._ig.crm.crm4ig.util;

import hu._ig.crm.crm4ig.domain.Address;
import hu._ig.crm.crm4ig.domain.Partner;
import hu._ig.crm.crm4ig.model.AddressExportDto;
import hu._ig.crm.crm4ig.utils.AddressExporter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddressExporterTest {

    @Test
    void testExportAddresses() {
        List<Address> addresses = getTestAddresses();
        List<AddressExportDto> exportDtos = AddressExporter.exportAddresses(addresses);

        assertEquals(1, exportDtos.size());
        AddressExportDto dto = exportDtos.get(0);
        assertEquals("Teszt Elek", dto.getPartnerName());
        assertEquals("Hungary", dto.getCountry());
        assertEquals("Budapest", dto.getCity());
        assertEquals("Fő utca", dto.getStreet());
        assertEquals("1", dto.getHouseNumber());
        assertEquals("1", dto.getFloor());
        assertEquals("1", dto.getDoor());
    }

    @Test
    void testExportAddressesToPdf() throws Exception {
        List<Address> addresses = getTestAddresses();
        byte[] pdfBytes = AddressExporter.exportAddressesToPdf(addresses);

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            assertEquals(1, document.getNumberOfPages());
        }
    }

    private List<Address> getTestAddresses() {
        var partner = new Partner();
        partner.setName("Teszt Elek");
        partner.setEmail("teszt.elek@example.com");

        var address = new Address();
        address.setCountry("Hungary");
        address.setCity("Budapest");
        address.setStreet("Fő utca");
        address.setHouseNumber("1");
        address.setFloor("1");
        address.setDoor("1");
        address.setPartner(partner);
        return List.of(address);
    }
}
