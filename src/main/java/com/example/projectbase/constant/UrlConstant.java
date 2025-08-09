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
        public static final String TOTAL = PRE_FIX + "/total";
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
        public static final String GET_USERS_BY_FILTER = BASE + "/filter";
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
        public static final String FILTER_CLASS = BASE + "/filter";
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
        public static final String ACCEPTED_REGISTRATION = BASE + "/accept";

    }

    public static class Document {
        public static final String BASE = "/documents";
        public static final String GET_DOCUMENT = BASE + "/{documentId}";
        public static final String GET_DOCUMENT_BY_CLASSID = BASE + "/class/{classId}";
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

    public static class Contest {
        public static final String BASE = "/contests";
        public static final String GET_CONTEST = BASE + "/{contestId}";
        public static final String CREATE_CONTEST = BASE;
        public static final String UPDATE_CONTEST = BASE + "/{contestId}";
        public static final String DELETE_CONTEST = BASE + "/{contestId}";
    }

    public static class Blog {
        public static final String BASE = "/blogs";
        public static final String GET_BLOG = BASE + "/{blogId:\\d+}";
        public static final String CREATE_BLOG = BASE;
        public static final String UPDATE_BLOG = BASE + "/{blogId}";
        public static final String DELETE_BLOG = BASE + "/{blogId}";
        public static final String SEARCH_BLOG = BASE + "/search/{tag}";

        public static final String COMMENT_BLOG = BASE + "/comment/set";
        public static final String GET_ALL_COMMENTS = BASE + "/comments/{blogId}";
        public static final String DELETE_COMMENTS = BASE + "/comment/{commentId}";
        public static final String CREATE_REPLY = BASE +  "/comment/{commentId}/reply";
        public static final String GET_REPLIES = BASE + "/comment/{commentId}/replies";
        public static final String UPDATE_COMMENT = BASE + "/comment/{commentId}";
        public static final String GET_COMMENT = BASE + "/comment/{commentId}";

        public static final String DROP_REACT = BASE + "/react";
        public static final String GET_REACT = BASE + "/react";
    }

}
