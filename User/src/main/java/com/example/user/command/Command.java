package com.example.user.command;

import org.springframework.http.ResponseEntity;

public interface Command {
    Object execute();
}
