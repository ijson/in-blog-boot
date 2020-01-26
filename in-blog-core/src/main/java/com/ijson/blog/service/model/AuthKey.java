package com.ijson.blog.service.model;

import lombok.Data;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/26 4:39 PM
 */
@Data
public class AuthKey {
    private String authId;
    private String authEname;
    private String authCname;
    private String path;
    private boolean checked;
    private boolean disabled;

    public AuthKey() {
    }

    public AuthKey(String authId, String authEname, String authCname, String path,boolean checked,boolean disabled) {
        this.authId = authId;
        this.authEname = authEname;
        this.authCname = authCname;
        this.path = path;
        this.checked = checked;
        this.disabled = disabled;
    }
}
