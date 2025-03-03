package hu._ig.crm.crm4ig.service.impl;

import hu._ig.crm.crm4ig.domain.Address;
import hu._ig.crm.crm4ig.domain.Partner;
import hu._ig.crm.crm4ig.exception.AddressException;
import hu._ig.crm.crm4ig.exception.PartnerException;
import hu._ig.crm.crm4ig.mapper.AddressMapper;
import hu._ig.crm.crm4ig.model.AddressDto;
import hu._ig.crm.crm4ig.repository.AddressRepository;
import hu._ig.crm.crm4ig.repository.PartnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static hu._ig.crm.crm4ig.constants.Constants.ADDRESS_NOT_EXIST_WITH_ID;
import static hu._ig.crm.crm4ig.constants.Constants.PARTNER_NOT_EXIST_WITH_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceImplTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private PartnerRepository partnerRepository;

    @Mock
    private AddressMapper addressMapper;

    private Address address;
    private AddressDto addressDto;
    private UUID addressId;
    private UUID partnerId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addressId = UUID.randomUUID();
        partnerId = UUID.randomUUID();
        createAddress();
        createAddressDto();
    }

    @Test
    void getAddressById_Success() {
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(addressMapper.toAddressDto(address)).thenReturn(addressDto);

        var result = addressService.getAddressById(addressId);

        assertNotNull(result);
        assertEquals(addressId, result.getId());
        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    void getAddressById_AddressNotFound() {
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());

        var ex = assertThrows(AddressException.class, () -> addressService.getAddressById(addressId));
        assertEquals(String.format(ADDRESS_NOT_EXIST_WITH_ID, addressId), ex.getMessage());
        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    void getAllAddresses_Success() {
        var addresses = new ArrayList<Address>();
        addresses.add(address);
        when(addressRepository.findAll()).thenReturn(addresses);
        when(addressMapper.toAddressDto(address)).thenReturn(addressDto);

        var result = addressService.getAllAddresses();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    void saveNewAddress_Success() {
        when(addressMapper.toAddress(any(AddressDto.class))).thenReturn(address);
        when(partnerRepository.findById(any())).thenReturn(Optional.of(address.getPartner()));
        when(addressRepository.save(any(Address.class))).thenAnswer(i -> address);
        when(addressMapper.toAddressDto(any(Address.class))).thenReturn(addressDto);

        var result = addressService.saveNewAddressToPartner(addressDto, addressDto.getPartnerId());

        assertNotNull(result, "The result should not be null.");
        assertEquals(addressId, result.getId(), "The IDs should match.");
        var addressCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addressRepository, times(1)).save(addressCaptor.capture());
    }

    @Test
    void updateAddress_Success() {
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(partnerRepository.findById(any())).thenReturn(Optional.of(address.getPartner()));
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toAddressDto(address)).thenReturn(addressDto);

        var result = addressService.updateAddress(addressId, addressDto);

        assertNotNull(result);
        assertEquals(addressId, result.getId());
        verify(addressRepository, times(1)).findById(addressId);
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void updateAddress_PartnerNotFound() {
        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));
        when(partnerRepository.findById(any())).thenReturn(Optional.empty());
        var ex = assertThrows(PartnerException.class,
                () -> addressService.updateAddress(addressId, addressDto));
        assertEquals(String.format(PARTNER_NOT_EXIST_WITH_ID, partnerId), ex.getMessage());
        verify(partnerRepository, times(1)).findById(partnerId);
    }

    @Test
    void updateAddress_AddressNotFound() {
        when(addressRepository.findById(addressId)).thenReturn(Optional.empty());
        when(partnerRepository.findById(any())).thenReturn(Optional.of(address.getPartner()));
        var ex = assertThrows(AddressException.class,
                () -> addressService.updateAddress(addressId, addressDto));
        assertEquals(String.format(ADDRESS_NOT_EXIST_WITH_ID, addressId), ex.getMessage());
        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    void deleteAddressById_Success() {
        doNothing().when(addressRepository).deleteById(addressId);

        addressService.deleteAddressById(addressId);

        verify(addressRepository, times(1)).deleteById(addressId);
    }

    private void createAddressDto() {
        var partnerT = new Partner();
        partnerT.setName("Teszt Elek");
        partnerT.setId(partnerId);
        address = new Address();
        address.setId(addressId);
        address.setPartner(partnerT);
        address.setCountry("Hungary");
        address.setCity("Budapest");
        address.setStreet("Fő utca");
        address.setHouseNumber("1");
        address.setFloor("1");
        address.setDoor("1");
    }

    private void createAddress() {
        addressDto = new AddressDto();
        addressDto.setId(addressId);
        addressDto.setPartnerId(partnerId);
        addressDto.setCountry("Hungary");
        addressDto.setCity("Budapest");
        addressDto.setStreet("Fő utca");
        addressDto.setHouseNumber("1");
        addressDto.setFloor("1");
        addressDto.setDoor("1");
    }
}
