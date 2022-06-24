package com.springboot.webservices.staffscheduling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class GeneralNotAcceptableException extends RuntimeException{
	public GeneralNotAcceptableException(String message) {
		super(message);
	}
}
