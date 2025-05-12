package gr.aueb.cf.ticketmanagement.core.exceptions;

public class EntityUnauthorizedException extends EntityGenericException {
    private static final String DEFAULT_CODE = "NotAuthorized";

    public EntityUnauthorizedException(String code, String message) {
        super(code + DEFAULT_CODE, message);
    }
}
