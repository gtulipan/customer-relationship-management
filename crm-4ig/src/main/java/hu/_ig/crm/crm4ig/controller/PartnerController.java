package hu._ig.crm.crm4ig.controller;

import hu._ig.crm.crm4ig.constants.Constants;
import hu._ig.crm.crm4ig.model.PartnerDto;
import hu._ig.crm.crm4ig.model.PartnerExportDto;
import hu._ig.crm.crm4ig.service.PartnerService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static hu._ig.crm.crm4ig.constants.Constants.*;
import static hu._ig.crm.crm4ig.utils.Formatters.getFileName;

@Slf4j
@OpenAPIDefinition(info = @Info(title = "CRM 4IG Service", version = "v1"))
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "${angular.client.url}")
@RequestMapping("/v1")
@RestController
public class PartnerController {

    private final PartnerService partnerService;

    @Operation(summary = "GET request for a partner", description = "Gives back a partner by partner identifier.")
    @GetMapping("/partner/{partnerId}")
    public ResponseEntity<PartnerDto> getPartner(@NotNull @PathVariable("partnerId") UUID customerId){
        return new ResponseEntity<>(partnerService.getPartnerById(customerId), HttpStatus.OK);
    }

    @Operation(summary = "GET request for partners list", description = "Retrieves a list of all partners.")
    @GetMapping("/partners")
    public ResponseEntity<List<PartnerDto>> getAllPartner(){
        return new ResponseEntity<>(partnerService.getAllPartners(), HttpStatus.OK);
    }

    @Operation(summary = "POST request to create a new partner", description = "Save a new partner into database.")
    @PostMapping(path = "/partner", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> createPartner(@Valid @RequestBody PartnerDto partnerDto){
        PartnerDto savedDto = partnerService.saveNewPartner(partnerDto);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", String.join(EMPTY_STRING, "/v1/partner/", savedDto.getId().toString()));
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @Operation(summary = "PUT request to update a partner", description = "Update a partner by partner identifier.")
    @PutMapping("/partner/{partnerId}")
    public ResponseEntity<PartnerDto> updatePartner(@NotNull @PathVariable("partnerId") UUID partnerId,
                                        @Valid @RequestBody PartnerDto partnerDto){
        PartnerDto updatedPartner = partnerService.updatePartner(partnerId, partnerDto);
        return ResponseEntity.ok(updatedPartner);
    }

    @Operation(summary = "Delete partner", description = "Deletes a partner based on ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PartnerDto.class))),
            @ApiResponse(responseCode = "404", description = "Partner not found")
    })
    @DeleteMapping("/partner/{partnerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePartner(@NotNull @PathVariable("partnerId") UUID partnerId){
        partnerService.deletePartnerById(partnerId);
    }

    @Operation(summary = "GET request to search for partners based on parts of the address", description = "Search partners by partner's address.")
    @GetMapping("/partners/by-address")
    public ResponseEntity<List<PartnerDto>> getPartnersByAddress(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String street,
            @RequestParam(required = false) String houseNumber) {
        return ResponseEntity.ok(partnerService.findPartnersByAddress(country, city, street, houseNumber));
    }

    @Operation(summary = "GET request to export partners", description = "Export partners with their addresses into Json format.")
    @GetMapping("/partners/export")
    public ResponseEntity<List<PartnerExportDto>> exportPartnersWithAddresses() {
        return ResponseEntity.ok(partnerService.exportPartnersWithAddresses());
    }

    @Operation(summary = "POST request to get the partners as PDF.", description = "Generate a PDF with the partners.")
    @PostMapping("/partners/export-pdf")
    public ResponseEntity<byte[]> printQuote() {
        byte[] pdfBytes = partnerService.exportPartnersToPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(getFileName(Constants.PARTNERS_EXPORT)).build());

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    @Operation(summary = "POST request to import partners from CSV file.", description = "Import partners from CSV file. The class type of file in request is MultipartFile.")
    @PostMapping("/import")
    public ResponseEntity<String> importPartnersFromCsv(@NotNull @RequestParam(FILE) MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(FILE_IS_EMPTY);
        }

        try {
            partnerService.importPartnersFromCsv(file.getInputStream());
            return ResponseEntity.ok(Constants.FILE_UPLOADED_AND_DATA_IMPORTED_SUCCESSFULLY);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(String.join(EMPTY_STRING, Constants.ERROR_WHILE_IMPORTING_DATA, e.getMessage()));
        }
    }
}
