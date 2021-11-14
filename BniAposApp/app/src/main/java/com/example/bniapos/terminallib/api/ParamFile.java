package com.example.bniapos.terminallib.api;

import android.os.RemoteException;

import com.example.bniapos.CTIApplication;
import com.usdk.apiservice.aidl.paramfile.UParamFile;

/**
 * Param file API.
 */

public class ParamFile {

    /**
     * Param file object.
     */
    private UParamFile paramFile = CTIApplication.getDeviceService().getParamFile();

    /**
     * Is first run.
     */
    public boolean isFirstRun() throws RemoteException {
        return paramFile.isFirstRun();
    }

    /**
     * Is param file exists.
     */
    public boolean isParamFileExists() throws RemoteException {
        return paramFile.isExists();
    }

    /**
     * Get boolean.
     */
    public boolean getBoolean(String paramName, boolean defaultValue) throws RemoteException {
        return paramFile.getBoolean(paramName, defaultValue);
    }

    /**
     * Get string.
     */
    public String getString(String paramName, String defaultValue) throws RemoteException {
        return paramFile.getString(paramName, defaultValue);
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final ParamFile INSTANCE = new ParamFile();
    }

    /**
     * Get param file instance.
     */
    public static ParamFile getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private ParamFile() {

    }
}
