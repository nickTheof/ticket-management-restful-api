package gr.aueb.cf.ticketmanagement.core.exceptions;

public class AppServerException extends EntityGenericException {
    public AppServerException(String code, String message) {
        super(code, message);
    }
}
