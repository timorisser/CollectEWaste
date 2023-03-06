package at.collew.account.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * This class is for handling responses that should return a meaningful response
 * @author Patrick Stadt
 * @version 2
 */
public class ResponseHandler {

    /**
     * This method returns an Object according to the http status
     * @param message errormessage if the status is not a http from 200-299
     * @param status http statuscode
     * @param responseObj returnObject on successfully request (optionally json object)
     * @return responseEntity as response
     */
    public ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        if(status.is2xxSuccessful()){
            return new ResponseEntity<>(responseObj, status);
        } else {
            return new ResponseEntity<>(message, status);
        }
    }
}
