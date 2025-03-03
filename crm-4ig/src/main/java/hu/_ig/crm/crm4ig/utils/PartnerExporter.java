package hu._ig.crm.crm4ig.utils;

import hu._ig.crm.crm4ig.domain.Partner;
import hu._ig.crm.crm4ig.exception.PdfGenerationException;
import hu._ig.crm.crm4ig.model.PartnerExportDto;
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
public class PartnerExporter {

    public static List<PartnerExportDto> exportPartnersWithAddresses(List<Partner> partners) {
        return partners.stream().flatMap(partner -> partner.getAddresses().stream().map(address -> {
            PartnerExportDto dto = new PartnerExportDto();
            dto.setPartnerName(partner.getName());
            dto.setPartnerEmail(partner.getEmail());
            dto.setCountry(address.getCountry());
            dto.setCity(address.getCity());
            dto.setStreet(address.getStreet());
            dto.setHouseNumber(address.getHouseNumber());
            return dto;
        })).toList();
    }

    public static byte[] exportPartnersToPdf(List<Partner> partners) {

        List<PartnerExportDto> exportDtos = exportPartnersWithAddresses(partners);

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            document.addPage(page);
            PDType0Font font = loadFontType(document);
            loadPdfContent(document, page, font, exportDtos);
            document.save(out);
            document.close();

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

    private void loadPdfContent(PDDocument document, PDPage page, PDType0Font font, List<PartnerExportDto> exportDtos) throws IOException {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.setLeading(14.5f);
            contentStream.newLineAtOffset(25, 750);

            for (PartnerExportDto partner : exportDtos) {
                contentStream.showText(PARTNER_NAME + partner.getPartnerName());
                contentStream.newLine();
                contentStream.showText(EMAIL + partner.getPartnerEmail());
                contentStream.newLine();
                contentStream.showText(COUNTRY + partner.getCountry());
                contentStream.newLine();
                contentStream.showText(CITY + partner.getCity());
                contentStream.newLine();
                contentStream.showText(STREET + partner.getStreet());
                contentStream.newLine();
                contentStream.showText(HOUSE_NUMBER + partner.getHouseNumber());
                contentStream.newLine();
                contentStream.newLine();
            }
            if(exportDtos.isEmpty()){
                contentStream.showText(THERE_IS_NOT_PARTNER_DATA);
            }
            contentStream.endText();
        }
    }
}

