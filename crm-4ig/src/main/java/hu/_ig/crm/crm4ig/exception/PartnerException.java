package hu._ig.crm.crm4ig.exception;

public class PartnerException extends RuntimeException {
    public PartnerException(String errorMessage, Throwable error){
        super(errorMessage, error);
    }

    public PartnerException(String errorMessage){
        super(errorMessage);
    }

    public PartnerException(Throwable error){
        super(error);
    }
}
