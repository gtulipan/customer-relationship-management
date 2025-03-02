package hu._ig.crm.crm4ig.exception;

public class PdfGenerationException extends RuntimeException {
    public PdfGenerationException(String errorMessage, Throwable error){
        super(errorMessage, error);
    }

    public PdfGenerationException(String errorMessage){
        super(errorMessage);
    }

    public PdfGenerationException(Throwable error){
        super(error);
    }
}
