package hu._ig.crm.crm4ig.exception;

public class AddressException  extends RuntimeException {
    public AddressException(String errorMessage, Throwable error){
        super(errorMessage, error);
    }

    public AddressException(String errorMessage){
        super(errorMessage);
    }

    public AddressException(Throwable error){
        super(error);
    }
}
