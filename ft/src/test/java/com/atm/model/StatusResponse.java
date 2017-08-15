package com.atm.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StatusResponse {
    private final String status;

    @JsonCreator
    public StatusResponse(@JsonProperty("status") String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
