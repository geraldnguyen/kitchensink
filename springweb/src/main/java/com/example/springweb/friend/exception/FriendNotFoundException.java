package com.example.springweb.friend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No Such Friend")
public class FriendNotFoundException extends RuntimeException{
}
