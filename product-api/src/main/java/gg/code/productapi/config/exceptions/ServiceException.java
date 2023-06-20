package gg.code.productapi.config.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
public class ServiceException extends RuntimeException{

    private Integer httpStatus;

    public ServiceException(Integer httpStatus, String message){
        super(message);
        this.httpStatus = httpStatus;
    }
}
