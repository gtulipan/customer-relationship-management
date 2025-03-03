package hu._ig.crm.crm4ig.service.impl;

import hu._ig.crm.crm4ig.domain.Partner;
import hu._ig.crm.crm4ig.exception.PartnerException;
import hu._ig.crm.crm4ig.mapper.PartnerMapper;
import hu._ig.crm.crm4ig.model.PartnerDto;
import hu._ig.crm.crm4ig.repository.PartnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static hu._ig.crm.crm4ig.constants.Constants.PARTNER_NOT_EXIST_WITH_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartnerServiceImplTest {

    @InjectMocks
    private PartnerServiceImpl partnerService;

    @Mock
    private PartnerRepository partnerRepository;

    @Mock
    private PartnerMapper partnerMapper;

    private Partner partner;
    private PartnerDto partnerDto;
    private UUID partnerId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        partnerId = UUID.randomUUID();
        createPartner();
        createPartnerDto();
    }

    @Test
    void getPartnerById_Success() {
        when(partnerRepository.findById(partnerId)).thenReturn(Optional.of(partner));
        when(partnerMapper.toPartnerDto(partner)).thenReturn(partnerDto);

        var result = partnerService.getPartnerById(partnerId);

        assertNotNull(result);
        assertEquals(partnerId, result.getId());
        verify(partnerRepository, times(1)).findById(partnerId);
    }

    @Test
    void getPartnerById_PartnerNotFound() {
        when(partnerRepository.findById(partnerId)).thenReturn(Optional.empty());

        var ex = assertThrows(PartnerException.class, () -> partnerService.getPartnerById(partnerId));
        assertEquals(String.format(PARTNER_NOT_EXIST_WITH_ID, partnerId), ex.getMessage());
        verify(partnerRepository, times(1)).findById(partnerId);
    }

    @Test
    void getAllPartners_Success() {
        var partners = new ArrayList<Partner>();
        partners.add(partner);
        when(partnerRepository.findAll()).thenReturn(partners);
        when(partnerMapper.toPartnerDto(partner)).thenReturn(partnerDto);

        var result = partnerService.getAllPartners();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(partnerRepository, times(1)).findAll();
    }

    @Test
    void saveNewPartner_Success() {
        when(partnerMapper.toPartner(any(PartnerDto.class))).thenReturn(partner);
        when(partnerRepository.save(any(Partner.class))).thenAnswer(i -> partner);
        when(partnerMapper.toPartnerDto(any(Partner.class))).thenReturn(partnerDto);

        var result = partnerService.saveNewPartner(partnerDto);

        assertNotNull(result, "The result should not be null.");
        assertEquals(partnerId, result.getId(), "The IDs should match.");
        var partnerCaptor = ArgumentCaptor.forClass(Partner.class);
        verify(partnerRepository, times(1)).save(partnerCaptor.capture());
    }


    @Test
    void updatePartner_Success() {
        when(partnerRepository.findById(partnerId)).thenReturn(Optional.of(partner));
        when(partnerRepository.save(partner)).thenReturn(partner);
        when(partnerMapper.toPartnerDto(partner)).thenReturn(partnerDto);

        var result = partnerService.updatePartner(partnerId, partnerDto);

        assertNotNull(result);
        assertEquals(partnerId, result.getId());
        verify(partnerRepository, times(1)).findById(partnerId);
        verify(partnerRepository, times(2)).save(partner);
    }

    @Test
    void updatePartner_PartnerNotFound() {
        when(partnerRepository.findById(partnerId)).thenReturn(Optional.empty());
        var ex = assertThrows(PartnerException.class,
                () -> partnerService.updatePartner(partnerId, partnerDto));
        assertEquals(String.format(PARTNER_NOT_EXIST_WITH_ID, partnerId), ex.getMessage());
        verify(partnerRepository, times(1)).findById(partnerId);
    }

    @Test
    void deletePartnerById_Success() {
        doNothing().when(partnerRepository).deleteById(partnerId);

        partnerService.deletePartnerById(partnerId);

        verify(partnerRepository, times(1)).deleteById(partnerId);
    }

    @Test
    void testFindPartnersByAddress() {
        var country = "Hungary";
        var city = "Budapest";
        var street = "Fő utca";
        var houseNumber = "1";

        var partners = new ArrayList<Partner>();
        var partnerLocale = new Partner();
        partners.add(partnerLocale);

        when(partnerRepository.findAll(any(Specification.class))).thenReturn(partners);
        when(partnerMapper.toPartnerDto(partner)).thenReturn(new PartnerDto());

        var result = partnerService.findPartnersByAddress(country, city, street, houseNumber);

        assertEquals(1, result.size());
    }

    private void createPartner() {
        partner = new Partner();
        partner.setId(partnerId);
        partner.setName("Teszt Elek");
        partner.setEmail("teszt.elek@example.com");
        partner.setAddresses(new HashSet<>());
    }

    private void createPartnerDto() {
        partnerDto = new PartnerDto();
        partnerDto.setId(partnerId);
        partnerDto.setName("Teszt Elek");
        partnerDto.setEmail("teszt.elek@example.com");
        partnerDto.setAddresses(new HashSet<>());
    }
}
