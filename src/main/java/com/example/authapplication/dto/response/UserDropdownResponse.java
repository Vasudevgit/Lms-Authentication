package com.example.authapplication.dto.response;


public class UserDropdownResponse {

    private Long id;
    private String name;

    public UserDropdownResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

