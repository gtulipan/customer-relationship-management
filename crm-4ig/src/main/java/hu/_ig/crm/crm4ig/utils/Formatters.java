package hu._ig.crm.crm4ig.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static hu._ig.crm.crm4ig.constants.Constants.*;
import static org.springdoc.core.utils.Constants.DOT;

@UtilityClass
public class Formatters {

    public static String getFileName(String fileNameStartsWith) {
        return String.join(EMPTY_STRING,
                fileNameStartsWith.replaceAll(WHITE_SPACE_MARK, EMPTY_STRING),
                UNDERLINE,
                timestamp().replaceAll(COLON, DOT),
                EXTENSION_PDF);
    }

    public static String timestamp(){
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
    }

    public static String date(){
        return DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now());
    }
}
