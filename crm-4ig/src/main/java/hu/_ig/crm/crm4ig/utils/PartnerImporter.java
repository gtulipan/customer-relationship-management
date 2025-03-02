package hu._ig.crm.crm4ig.utils;

import hu._ig.crm.crm4ig.domain.Address;
import hu._ig.crm.crm4ig.domain.Partner;
import hu._ig.crm.crm4ig.exception.CsvImportException;
import hu._ig.crm.crm4ig.repository.PartnerRepository;
import lombok.experimental.UtilityClass;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static hu._ig.crm.crm4ig.constants.Constants.*;

@UtilityClass
public class PartnerImporter {

    public static void importPartnersFromCsv(InputStream csvInputStream, PartnerRepository partnerRepository) {
        try (Reader reader = new BufferedReader(new InputStreamReader(csvInputStream));
             CSVParser csvParser = CSVParser.parse(reader,
                     CSVFormat.Builder.create(CSVFormat.DEFAULT)
                             .setHeader()
                             .setSkipHeaderRecord(true)
                             .setIgnoreHeaderCase(true)
                             .setTrim(true)
                             .build())) {

            Map<String, Partner> partnerMap = new HashMap<>();

            for (CSVRecord csvRecord : csvParser) {
                String email = csvRecord.get(EMAIL_DB);

                Partner partner = partnerMap.getOrDefault(email, new Partner());
                if (!partnerMap.containsKey(email)) {
                    partner.setName(csvRecord.get(NAME_DB));
                    partner.setEmail(csvRecord.get(EMAIL_DB));
                    partner.setAddresses(new HashSet<>());
                    partnerMap.put(email, partner);
                }

                Address address = new Address();
                address.setCountry(csvRecord.get(COUNTRY_DB));
                address.setCity(csvRecord.get(CITY_DB));
                address.setStreet(csvRecord.get(STREET_DB));
                address.setHouseNumber(csvRecord.get(HOUSE_NUMBER_DB));
                address.setPartner(partner);
                partner.getAddresses().add(address);
            }

            partnerRepository.saveAll(partnerMap.values());
        } catch (Exception e) {
            throw new CsvImportException(ERROR_WHILE_IMPORTING_CSV_DATA + e.getMessage(), e);
        }
    }
}
