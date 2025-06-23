package com.example.projectbase.constant;

public class UrlConstant {

  public static class Auth {
    private static final String PRE_FIX = "/auth";
    public static final String LOGIN = PRE_FIX + "/login";
    public static final String LOGOUT = PRE_FIX + "/logout";
    public static final String refreshToken = PRE_FIX + "/refreshToken";
    private Auth() {
    }
  }

  public static class User {
    public static final String BASE = "/users";
    public static final String GET_USER = BASE + "/{userId}";
    public static final String CREATE_USER = BASE;
    public static final String UPDATE_USER = BASE + "/{userId}";
    public static final String DELETE_USER = BASE + "/{userId}";
    public static final String GET_USERS = BASE;
    public static final String GET_CURRENT_USER = BASE + "/profile";
    public static final String PASSWORD_CHANGE_FIRST_TIME = BASE + "/change-password-first-time";
    public static final String UPDATE_CURRENT_USER = BASE + "/profile/update";
  }
}
