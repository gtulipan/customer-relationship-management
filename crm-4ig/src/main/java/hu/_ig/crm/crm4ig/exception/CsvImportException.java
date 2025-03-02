package hu._ig.crm.crm4ig.exception;

public class CsvImportException  extends RuntimeException {
    public CsvImportException(String errorMessage, Throwable error){
        super(errorMessage, error);
    }

    public CsvImportException(String errorMessage){
        super(errorMessage);
    }

    public CsvImportException(Throwable error){
        super(error);
    }
}
