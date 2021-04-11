package com.ijson.blog.service.model.info;

import lombok.Data;

import java.io.Serializable;

@Data
public class IndexInstall implements Serializable {

    private String address;
    private String port;
    private String database;
    private String openAuth;
    private String username;
    private String password;
}
