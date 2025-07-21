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

  public static class ClassRegistration{
    public static final String CLASS_NOT_FOUND="exception.class-registration.not.found";
    public static final String REGISTRATION_NOT_FOUND="exception.registration.not.found";
    public static final String UNAUTHORIZED = "You do not have the right to take this action.";
    public static final String ALREADY_REGISTERED="exception.registration.already.registered";
    public static final String GET_MY_REGISTRATIONS_FAILED = "Cannot retrieve the list of registered classes";
    public static final String GET_ALL_REGISTRATIONS_FAILED = "Cannot retrieve the list of all registrations.";
    public static final String FILTER_REGISTRATIONS_FAILED = "Cannot filter the registration list.";

  }

  public static class Contest{
     public static final String INTERNAL_SERVER_ERROR="System error, please try again later.";
     public static final String VALIDATION_FAILED="The input data is invalid.";
     public static final String MISSING_REQUIRED_FIELDS="missing required information";
     public static final String CONTEST_NOT_FOUND="not found contest";
     public static final String CONTEST_NAME_REQUIRED="The contest name cannot be left blank.";
     public static final String CONTEST_TIME_INVALID = "invalid time";
     public static final String CONTEST_DESCRIPTION_REQUIRED = "The contest description cannot be empty.";
     public static final String CONTEST_DETAIL_NOT_FOUND = "Cannot view contest details.";
     public static final String CONTEST_RESULT_NOT_AVAILABLE = "The contest results have not been announced yet.";
     public static final String FILE_UPLOAD_FAILED = "Download file unsuccesfull!";
     public static final String FILE_TOO_LARGE = "The file size exceeds the allowed limit.";
     public static final String FILE_TYPE_NOT_SUPPORTED = "The file format is not supported.";

  }
}
