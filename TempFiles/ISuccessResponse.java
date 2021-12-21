package com.example.paymentsdk.Common;

public interface ISuccessResponse {
    void processFinish(String output);
    void processFailed(String Exception);
    void processTimeOut();

}

