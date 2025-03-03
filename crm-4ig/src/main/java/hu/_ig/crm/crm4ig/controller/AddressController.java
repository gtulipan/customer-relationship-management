package hu._ig.crm.crm4ig.controller;

import hu._ig.crm.crm4ig.model.AddressDto;
import hu._ig.crm.crm4ig.model.AddressExportDto;
import hu._ig.crm.crm4ig.service.AddressService;
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

import java.util.List;
import java.util.UUID;

import static hu._ig.crm.crm4ig.constants.Constants.ADDRESSES_EXPORT;
import static hu._ig.crm.crm4ig.constants.Constants.EMPTY_STRING;
import static hu._ig.crm.crm4ig.utils.Formatters.getFileName;

@Slf4j
@OpenAPIDefinition(info = @Info(title = "CRM 4IG Service", version = "v1"))
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "${angular.client.url}")
@RequestMapping("/v1")
@RestController
public class AddressController {

    private final AddressService addressService;

    @Operation(summary = "GET request for an address", description = "Gives back an address by address identifier.")
    @GetMapping("/address/{addressId}")
    public ResponseEntity<AddressDto> getAddress(@NotNull @PathVariable("addressId") UUID addressId) {
        return new ResponseEntity<>(addressService.getAddressById(addressId), HttpStatus.OK);
    }

    @Operation(summary = "GET request for addresses list", description = "Retrieves a list of all addresses.")
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        return new ResponseEntity<>(addressService.getAllAddresses(), HttpStatus.OK);
    }

    @Operation(summary = "POST request to create a new address for a partner", description = "Save a new address for a partner into database.")
    @PostMapping(path = "/{partnerId}/address", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity createAddress(@Valid @RequestBody AddressDto addressDto,
                                        @NotNull @PathVariable(name = "partnerId") UUID partnerId) {
        AddressDto savedDto = addressService.saveNewAddressToPartner(addressDto, partnerId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", String.join(EMPTY_STRING, "/v1/address/", savedDto.getId().toString()));
        return new ResponseEntity(httpHeaders, HttpStatus.CREATED);
    }

    @Operation(summary = "PUT request to update an address", description = "Update an address by address identifier.")
    @PutMapping("/address/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(@NotNull @PathVariable("addressId") UUID addressId,
                                                    @Valid @RequestBody AddressDto addressDto) {
        AddressDto updatedAddress = addressService.updateAddress(addressId, addressDto);
        return ResponseEntity.ok(updatedAddress);
    }

    @Operation(summary = "Delete address", description = "Delete an address based on ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressDto.class))),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @DeleteMapping("/address/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(@NotNull @PathVariable("addressId") UUID addressId) {
        addressService.deleteAddressById(addressId);
    }

    @Operation(summary = "GET request to export addresses", description = "Export addresses into Json format.")
    @GetMapping("/addresses/export")
    public ResponseEntity<List<AddressExportDto>> exportAddresses() {
        return ResponseEntity.ok(addressService.exportAddresses());
    }

    @Operation(summary = "GET request to get all addresses as PDF.", description = "Generate a PDF with all addresses.")
    @GetMapping("/addresses/export-pdf")
    public ResponseEntity<byte[]> printAddresses() {
        byte[] pdfBytes = addressService.exportAddressesToPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(getFileName(ADDRESSES_EXPORT)).build());

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}
