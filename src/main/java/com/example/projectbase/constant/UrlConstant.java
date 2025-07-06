package com.example.projectbase.constant;

public class UrlConstant {

    public static class Auth {
        private static final String PRE_FIX = "/auth";
        public static final String LOGIN = PRE_FIX + "/login";
        public static final String LOGOUT = PRE_FIX + "/logout";
        public static final String refreshToken = PRE_FIX + "/refreshToken";
        public static final String SEND_CODE = PRE_FIX + "/send-code";
        public static final String VERIFY_CODE = PRE_FIX + "/verify-code";
        public static final String PASSWORD_CHANGE = PRE_FIX + "/password/change";
        public static final String PASSWORD_CHANGE_FIRST_TIME = PRE_FIX + "/change-password-first-time";
        public static final String FIRST_TIME_LOGIN = PRE_FIX + "/first-time-login";
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
        public static final String PASSWORD_CHANGE = BASE + "/password/change";
        public static final String PASSWORD_CHANGE_FIRST_TIME = BASE + "/change-password-first-time";
        public static final String UPDATE_CURRENT_USER = BASE + "/profile/update";
        public static final String SEND_CODE = BASE + "/sendCode";
        public static final String VERIFY_CODE = BASE + "/verifyCode";
    }

    public static class Class {
        public static final String BASE = "/classes";
        public static final String GET_CLASS = BASE + "/{classId}";
        public static final String CREATE_CLASS = BASE;
        public static final String UPDATE_CLASS = BASE + "/{classId}";
        public static final String DELETE_CLASS = BASE + "/{classId}";
    }

    public static class ClassRegistration {
        public static final String BASE = "/registration";
        public static final String CLASS_REGISTRATION = "/api/v1/registration";
        public static final String CREATE_REGISTRATION = BASE;
        public static final String APPROVE_REGISTRATION = BASE + "/approve";
        public static final String VIEW_REGISTRATION = BASE + "/view";
        public static final String FILTER_REGISTRATION = BASE + "/filter";
        public static final String DEL_REGISTRATION = BASE + "/{id}";
        public static final String CANCEL_REGISTRATION = BASE + "/cancel/{classId}";

    }

    public static class Document {
        public static final String BASE = "/documents";
        public static final String GET_DOCUMENT = BASE + "/{documentId}";
        public static final String CREATE_DOCUMENT = BASE;
        public static final String UPDATE_DOCUMENT = BASE + "/{documentId}";
        public static final String DELETE_DOCUMENT = BASE + "/{documentId}";
    }

    public static class Storage {
        public static final String BASE = "/storage";
        public static final String UPLOAD_FILE = BASE + "/upload";
        public static final String DOWNLOAD_FILE = BASE + "/download/**";
        public static final String DELETE_FILE = BASE + "/delete";
    }
}
