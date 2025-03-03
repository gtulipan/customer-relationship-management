package hu._ig.crm.crm4ig.util;

import hu._ig.crm.crm4ig.domain.Address;
import hu._ig.crm.crm4ig.domain.Partner;
import hu._ig.crm.crm4ig.repository.PartnerRepository;
import hu._ig.crm.crm4ig.service.impl.PartnerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doAnswer;

class PartnerImporterTest {

    @Mock
    private PartnerRepository partnerRepository;

    @InjectMocks
    private PartnerServiceImpl partnerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testImportPartnersFromCsv() throws Exception {
        var csvContent = "name,email,country,city,street,house_number\n"
                + "Teszt Elek,teszt.elek@example.com,Hungary,Budapest,Fő utca,1\n"
                + "Teszt Elek,teszt.elek@example.com,Hungary,Szeged,Petőfi utca,2\n"
                + "Est Hajnalka,est.hajnalka@example.com,Hungary,Pécs,Kossuth utca,3\n"
                + "Est Hajnalka,est.hajnalka@example.com,Hungary,Debrecen,Ady utca,4\n";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes());

        partnerService.importPartnersFromCsv(inputStream);

        doAnswer(invocation -> {
            List<Partner> partners = invocation.getArgument(0);
            assertEquals(2, partners.size());

            var partner1 = partners.get(0);
            assertEquals("Teszt Elek", partner1.getName());
            assertEquals("teszt.elek@example.com", partner1.getEmail());
            assertEquals(2, partner1.getAddresses().size());

            var partner1Address1 = partner1.getAddresses().stream()
                    .filter(address -> "Budapest".equals(address.getCity()))
                    .findFirst()
                    .orElse(null);
            assert partner1Address1 != null;
            assertEquals("Hungary", partner1Address1.getCountry());
            assertEquals("Budapest", partner1Address1.getCity());
            assertEquals("Fő utca", partner1Address1.getStreet());
            assertEquals("1", partner1Address1.getHouseNumber());

            var partner1Address2 = partner1.getAddresses().stream()
                    .filter(address -> "Szeged".equals(address.getCity()))
                    .findFirst()
                    .orElse(null);
            assert partner1Address2 != null;
            assertEquals("Hungary", partner1Address2.getCountry());
            assertEquals("Szeged", partner1Address2.getCity());
            assertEquals("Petőfi utca", partner1Address2.getStreet());
            assertEquals("2", partner1Address2.getHouseNumber());

            var partner2 = partners.get(1);
            assertEquals("Est Hajnalka", partner2.getName());
            assertEquals("est.hajnalka@example.com", partner2.getEmail());
            assertEquals(2, partner2.getAddresses().size());

            var partner2Address1 = partner2.getAddresses().stream()
                    .filter(address -> "Pécs".equals(address.getCity()))
                    .findFirst()
                    .orElse(null);
            assert partner2Address1 != null;
            assertEquals("Hungary", partner2Address1.getCountry());
            assertEquals("Pécs", partner2Address1.getCity());
            assertEquals("Kossuth utca", partner2Address1.getStreet());
            assertEquals("3", partner2Address1.getHouseNumber());

            var partner2Address2 = partner2.getAddresses().stream()
                    .filter(address -> "Debrecen".equals(address.getCity()))
                    .findFirst()
                    .orElse(null);
            assert partner2Address2 != null;
            assertEquals("Hungary", partner2Address2.getCountry());
            assertEquals("Debrecen", partner2Address2.getCity());
            assertEquals("Ady utca", partner2Address2.getStreet());
            assertEquals("4", partner2Address2.getHouseNumber());

            return null;
        }).when(partnerRepository).saveAll(anyList());
    }
}
