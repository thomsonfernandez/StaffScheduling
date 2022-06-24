package com.springboot.webservices.staffscheduling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoUsersException extends RuntimeException{
	public NoUsersException(String message) {
		super(message);
	}
}
