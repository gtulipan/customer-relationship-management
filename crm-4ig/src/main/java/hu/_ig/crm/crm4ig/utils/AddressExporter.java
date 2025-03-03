package hu._ig.crm.crm4ig.utils;

import hu._ig.crm.crm4ig.domain.Address;
import hu._ig.crm.crm4ig.exception.PdfGenerationException;
import hu._ig.crm.crm4ig.model.AddressExportDto;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static hu._ig.crm.crm4ig.constants.Constants.*;

@Slf4j
@UtilityClass
public class AddressExporter {

    public static List<AddressExportDto> exportAddresses(List<Address> addresses) {
        return addresses.stream().map(address ->
                AddressExportDto.builder()
                        .partnerName(address.getPartner().getName())
                        .country(address.getCountry())
                        .city(address.getCity())
                        .street(address.getStreet())
                        .houseNumber(address.getHouseNumber())
                        .floor(address.getFloor())
                        .door(address.getDoor())
                        .build()
        ).toList();
    }

    public static byte[] exportAddressesToPdf(List<Address> addresses) {

        List<AddressExportDto> exportDtos = exportAddresses(addresses);

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            document.addPage(page);
            PDType0Font font = loadFontType(document);
            loadPdfContent(document, page, font, exportDtos);
            document.save(out);

            return out.toByteArray();
        } catch (IOException e) {
            log.error("An error occurred while generating the PDF: {}", e.getMessage());
            throw new PdfGenerationException(AN_ERROR_OCCURRED_WHILE_GENERATING_THE_PDF, e);
        }
    }

    private PDType0Font loadFontType(PDDocument document) throws IOException {
        try (InputStream fontStream = PartnerExporter.class.getResourceAsStream(FONTS_LIBERATION_SANS_REGULAR_TTF)) {
            return PDType0Font.load(document, fontStream);
        }
    }

    private void loadPdfContent(PDDocument document, PDPage page, PDType0Font font, List<AddressExportDto> exportDtos) throws IOException {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 750);

            for (AddressExportDto address : exportDtos) {
                contentStream.showText(String.join(EMPTY_STRING, PARTNER_NAME, address.getPartnerName()));
                contentStream.newLine();
                contentStream.showText(String.join(EMPTY_STRING, COUNTRY, address.getCountry()));
                contentStream.newLine();
                contentStream.showText(String.join(EMPTY_STRING, CITY, address.getCity()));
                contentStream.newLine();
                contentStream.showText(String.join(EMPTY_STRING, STREET, address.getStreet()));
                contentStream.newLine();
                contentStream.showText(String.join(EMPTY_STRING, HOUSE_NUMBER, address.getHouseNumber()));
                contentStream.newLine();
                contentStream.showText(String.join(EMPTY_STRING, FLOOR, address.getHouseNumber()));
                contentStream.newLine();
                contentStream.showText(String.join(EMPTY_STRING, DOOR, address.getHouseNumber()));
                contentStream.newLine();
                contentStream.newLine();
            }

            if (exportDtos.isEmpty()) {
                contentStream.showText(THERE_IS_NOT_ADDRESS_DATA);
            }
            contentStream.endText();
        }
    }
}
