package com.techbayportal.wataya.helper;

public class ResponseCode {

    public static int SUCCESS = 200;
    public static int INTERNAL_SERVER_ERROR = 500;
    public static int BAD_REQUEST = 400;
    public static int FORBIDDEN = 403;
    public static int UNAUTHORIZED = 401;

    public static boolean isBetweenSuccessRange(int reqCode) {
        return reqCode > 199 && reqCode < 300;
    }
}
