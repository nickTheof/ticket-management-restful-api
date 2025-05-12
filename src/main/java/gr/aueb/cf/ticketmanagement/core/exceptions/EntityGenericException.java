package gr.aueb.cf.ticketmanagement.core.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EntityGenericException extends Exception{
    private final String code;

    public EntityGenericException(String code, String message) {
        super(message);
        this.code = code;
    }
}
