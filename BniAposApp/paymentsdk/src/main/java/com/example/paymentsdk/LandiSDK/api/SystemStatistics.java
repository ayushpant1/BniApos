package com.example.paymentsdk.LandiSDK.api;

import android.os.RemoteException;

import com.example.paymentsdk.CTIApplication;
import com.usdk.apiservice.aidl.systemstatistics.StatisticInfo;
import com.usdk.apiservice.aidl.systemstatistics.USystemStatistics;

import java.util.List;

/**
 * System statistics API.
 */

public class SystemStatistics {

    /**
     * System statistics object.
     */
    private USystemStatistics systemStatistics = CTIApplication.getDeviceService().getSystemStatistics();

    /**
     * Get all statistics.
     */
    public List<StatisticInfo> getAllStatistics() throws RemoteException {
        return systemStatistics.getAllStatistics();
    }

    /**
     * Get all statistics and status.
     */
    public List<StatisticInfo> getAllStatisticsAndStatus() throws RemoteException {
        return systemStatistics.getAllStatisticsAndStatus();
    }

    /**
     * Get all status.
     */
    public List<StatisticInfo> getAllStatus() throws RemoteException {
        return systemStatistics.getAllStatus();
    }

    /**
     * Get statistics item.
     */
    public StatisticInfo getStatisticItem(int tagNo, int factorNo) throws RemoteException {
        return systemStatistics.getStatisticItem(tagNo, factorNo);
    }

    /**
     * Creator.
     */
    private static class Creator {
        private static final SystemStatistics INSTANCE = new SystemStatistics();
    }

    /**
     * Get system statistics instance.
     */
    public static SystemStatistics getInstance() {
        return Creator.INSTANCE;
    }

    /**
     * Constructor.
     */
    private SystemStatistics() {

    }
}
