package net.net.file;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileNotFoundexception extends RuntimeException {
    public FileNotFoundexception(String message) {
        super(message);
    }

    public FileNotFoundexception(String message, Throwable cause) {
        super(message, cause);
    }

}
