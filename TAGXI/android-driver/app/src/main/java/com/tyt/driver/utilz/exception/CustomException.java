package com.tyt.driver.utilz.exception;

public class CustomException extends Exception {

    private int code;

    private String exception;

    public CustomException(String exception) {
        super(exception);
        this.code = code;
        this.exception = exception;
    }

    public CustomException(int statusCode, String expectedResult) {
        this.code = statusCode;
        this.exception = expectedResult;
    }

    @Override
    public String getMessage() {
        return exception;
    }

    public CustomException(int code, Throwable throwable) {
        this.code = code;
        //   this.exception = throwable.getMessage();
    }

    /* public CustomException(int code, BaseResponse response){
         this.code = code;
         this.exception = response.errorMessage();
     }
 */
    public CustomException() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /*public String getException() {
        return exception;
    }*/

    /*public void setException(String exception) {
        this.exception = exception;
    }*/
}
