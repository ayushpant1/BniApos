package com.example.bniapos.terminallib.Common;

public interface ISuccessResponse {
    void processFinish(String output);
    void processFailed(String Exception);
    void processTimeOut();

}

