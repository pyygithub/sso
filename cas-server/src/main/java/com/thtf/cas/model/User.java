package com.thtf.cas.model;

import lombok.Data;

/**
 * ========================
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/26
 * Time：16:07
 * Version: v1.0
 * ========================
 */
@Data
public class User {
    private String id;

    private String username;

    private String password;

    private Integer expired;

    private Integer disable;

    private String email;
}
