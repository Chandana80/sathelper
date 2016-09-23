package  com.ojass.sathelper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandler {

    private MessageSource messageSource;

    @Autowired
    public ExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public String processValidationError(DataIntegrityViolationException ex) {
        return  ex.getLocalizedMessage();
    }

}