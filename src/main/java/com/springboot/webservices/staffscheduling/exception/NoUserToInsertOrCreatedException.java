package com.springboot.webservices.staffscheduling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NoUserToInsertOrCreatedException extends RuntimeException{
	public NoUserToInsertOrCreatedException(String message) {
		super(message);
	}
}
