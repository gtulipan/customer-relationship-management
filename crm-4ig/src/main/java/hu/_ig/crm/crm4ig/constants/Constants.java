package hu._ig.crm.crm4ig.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String EMPTY_STRING = "";
    public static final String COLON = " : ";
    public static final String BEARER = "Bearer ";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String USER_NOT_FOUND_FOR_EMAIL = "User not found for email: ";
    public static final String ROLES = "roles";
    public static final String PARTNER_NOT_EXIST_WITH_ID = "Partner not exist with id %s";
    public static final String ADDRESS_NOT_EXIST_WITH_ID = "Address not exist with id %s";
    public static final String CITY = "City: ";
    public static final String PARTNER_NAME = "Partner Name: ";
    public static final String EMAIL = "Email: ";
    public static final String COUNTRY = "Country: ";
    public static final String STREET = "Street: ";
    public static final String HOUSE_NUMBER = "House Number: ";
    public static final String AN_ERROR_OCCURRED_WHILE_GENERATING_THE_PDF = "An error occurred while generating the partner export PDF file.";
    public static final String PARTNERS_EXPORT = "PartnersExport";
    public static final String ADDRESSES_EXPORT = "AddressesExport";
    public static final String UNDERLINE = "_";
    public static final String EXTENSION_PDF = ".pdf";
    public static final String WHITE_SPACE_MARK = "\\s";
    public static final String FONTS_LIBERATION_SANS_REGULAR_TTF = "/fonts/LiberationSans-Regular.ttf";
    public static final String ERROR_WHILE_IMPORTING_CSV_DATA = "Error while importing CSV data: ";
    public static final String NAME_DB = "name";
    public static final String EMAIL_DB = "email";
    public static final String COUNTRY_DB = "country";
    public static final String CITY_DB = "city";
    public static final String STREET_DB = "street";
    public static final String HOUSE_NUMBER_DB = "house_number";
    public static final String FILE_IS_EMPTY = "File is empty";
    public static final String FILE = "file";
    public static final String FILE_UPLOADED_AND_DATA_IMPORTED_SUCCESSFULLY = "File uploaded and data imported successfully";
    public static final String ERROR_WHILE_IMPORTING_DATA = "Error while importing data: ";
    public static final String THERE_IS_NOT_PARTNER_DATA = "Jelenleg nem tartalmaz Partner adatokat az adatbázis.";
    public static final String THERE_IS_NOT_ADDRESS_DATA = "Jelenleg nem tartalmaz cím adatokat az adatbázis.";
    public static final String THERE_IS_NO_PARTNER_FOR_THE_NEW_ADDRESS = "There is no partner for the new address. Partner ID: %s.";
    public static final String ADDRESS_WITHOUT_A_PARTNER_CANNOT_BE_SAVED = "An address without a partner cannot be saved in the database.";
    public static final String FLOOR = "Floor: ";
    public static final String DOOR = "Door: ";
    public static final String ROLE_ADMIN_ROLE_USER_HIERARCHY = "ROLE_ADMIN > ROLE_USER\nROLE_USER > ROLE_GUEST";
}
