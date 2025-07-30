package com.example.projectbase.constant;

public class ErrorMessage {

  public static final String ERR_EXCEPTION_GENERAL = "exception.general";
  public static final String UNAUTHORIZED = "exception.unauthorized";
  public static final String FORBIDDEN = "exception.forbidden";
  public static final String FORBIDDEN_UPDATE_DELETE = "exception.forbidden.update-delete";

  //error validation dto
  public static final String INVALID_SOME_THING_FIELD = "invalid.general";
  public static final String INVALID_FORMAT_SOME_THING_FIELD = "invalid.general.format";
  public static final String INVALID_SOME_THING_FIELD_IS_REQUIRED = "invalid.general.required";
  public static final String NOT_BLANK_FIELD = "invalid.general.not-blank";
  public static final String INVALID_FORMAT_PASSWORD = "invalid.password-format";
  public static final String NOT_CORRECT_PASSWORD = "invalid.correct-password";
  public static final String INVALID_DATE = "invalid.date-format";
  public static final String INVALID_DATE_FEATURE = "invalid.date-future";
  public static final String INVALID_DATETIME = "invalid.datetime-format";
  public static final String FORBIDDEN_VIEW_USER_REGISTRATION = "Bạn không có quyền xem thông tin đăng ký của người dùng này";

  public static class Auth {
    public static final String ERR_CHANGE_PASSWORD_FIST_TIME_LOGIN = "error.user.first-time-login";
    public static final String ERR_INCORRECT_USERNAME = "exception.auth.incorrect.username";
    public static final String ERR_INCORRECT_PASSWORD = "exception.auth.incorrect.password";
    public static final String ERR_ACCOUNT_NOT_ENABLED = "exception.auth.account.not.enabled";
    public static final String ERR_ACCOUNT_LOCKED = "exception.auth.account.locked";
    public static final String INVALID_REFRESH_TOKEN = "exception.auth.invalid.refresh.token";
    public static final String EXPIRED_REFRESH_TOKEN = "exception.auth.expired.refresh.token";
  }

  public static class User {
    public static final String ERR_NOT_FOUND_USERNAME = "exception.user.not.found.username";
    public static final String ERR_NOT_FOUND_ID = "exception.user.not.found.id";
    public static final String ERR_USER_NAME_BLANK = "User name must not blank";
    public static final String ERR_USER_NAME_EXISTED = "exception.username.existed";
    public static final String ERR_EMAIL_EXISTED = "exception.user.email.existed";
    public static final String ERR_DELETE_FAIL = "Delete user fail";
    public static final String ERR_USER_NOT_FOUND = "exception.user.not.found";
    public static final String ERR_CREATE_FAIL = "Create user fail";
    public static final String ERR_NOT_FOUND="user not found";
  }

  public static class Role {
    public static final String ERR_NOT_FOUND_ROLE = "exception.user.not.found.role";
  }

    public static class ClassRegistration {
        public static final String CLASS_NOT_FOUND = "exception.class-registration.not.found";
        public static final String REGISTRATION_NOT_FOUND = "exception.registration.not.found";
        public static final String UNAUTHORIZED = "exception.class-registration.unauthorized";
        public static final String ALREADY_REGISTERED = "exception.class-registration.already_registered";
        public static final String GET_MY_REGISTRATIONS_FAILED = "exception.class-registration.get_my_registrations_failed";
        public static final String GET_ALL_REGISTRATIONS_FAILED = "exception.class-registration.get_all_registrations_failed";
        public static final String FILTER_REGISTRATIONS_FAILED = "exception.class-registration.filter_failed";
    }

    public static class Contest {
        public static final String INTERNAL_SERVER_ERROR = "exception.contest.internal_server_error";
        public static final String VALIDATION_FAILED = "exception.contest.validation_failed";
        public static final String MISSING_REQUIRED_FIELDS = "exception.contest.missing_required_fields";
        public static final String CONTEST_NOT_FOUND = "exception.contest.not_found";
        public static final String CONTEST_NAME_REQUIRED = "exception.contest.name_required";
        public static final String CONTEST_TIME_INVALID = "exception.contest.time_invalid";
        public static final String CONTEST_DESCRIPTION_REQUIRED = "exception.contest.description_required";
        public static final String CONTEST_DETAIL_NOT_FOUND = "exception.contest.detail_not_found";
        public static final String CONTEST_RESULT_NOT_AVAILABLE = "exception.contest.result_not_available";
        public static final String FILE_UPLOAD_FAILED = "exception.contest.file_upload_failed";
        public static final String FILE_TOO_LARGE = "exception.contest.file_too_large";
        public static final String FILE_TYPE_NOT_SUPPORTED = "exception.contest.file_type_not_supported";
        public static final String USER_CONTEST_NOT_FOUND = "exception.contest.user_contest_not_found";
        public static final String ALREADY_JOINED = "exception.contest.already_joined";
        public static final String FILE_NOT_FOUND="exception.file.not_found";
        public static final String ORIGINAL_FILENAME="exception.original.not_found";
        public static final String UPLOADING_FILE="exception.uploading_file";
        public static final String INVALID_FILE="exception.invalid_file";

    }

    public static class ClassRoom {
      public static final String CLASS_NOT_FOUND = "exception.class.registration.not.found";
    }

    public static class Blog{
      public static final String BLOG_NOT_FOUND="exception.blog.not_found";
      public static final String INTERNAL_SERVER_ERROR="exception.internal.server_error";

    }

    public static class Comment{
      public static final String COMMENT_NOT_FOUND="exception.comment.not_found";
      public static final String INTERNAL_SERVER_ERROR="exception.internal.server_error";
    }
}
