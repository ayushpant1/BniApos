package com.example.paymentsdk.util.emv;

import com.example.paymentsdk.util.data.BytesUtil;
import com.example.paymentsdk.util.data.TLVList;
import com.usdk.apiservice.aidl.emv.CAPublicKey;
import com.usdk.apiservice.aidl.emv.EMVTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mock emv data.
 */

public class StaticEmvData {
    private static Map<String, VisaParameter> visaParameters;
    private static Map<String, MasterParameter> masterParameters;
    private static Map<String, PbocParameter> pbocParameters;
    private static Map<String, BaseParameter> baseParameters;
    private static Map<String, BaseParameter> baseParametersPaypass;
    private static Map<String, Boolean> aids;
    private static List<CAPublicKey> publicKeys;

    static Map<String, VisaParameter> getMockVisaParameters() {
        if (visaParameters != null) {
            return visaParameters;
        }

        visaParameters = new HashMap<>();
        // VISA parameter hash map:
        // - key: PID. Default VISA parameters use DEFULT_PARAMETER_KEY.
        // - value: VisaParameter
        VisaParameter visaParameter1 = new VisaParameter();
        visaParameter1.setRcp(EmvData.getRcp(false, true, true, true, true, true));
        visaParameter1.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
        visaParameter1.setRfTransactionLimit(0L);
        visaParameter1.setRfFloorLimit(0L);
        visaParameter1.setRfCvmLimit(10000000L);

        visaParameters.put(EmvData.DEFULT_PARAMETER_KEY, visaParameter1);
//
//        // CLM.D.016.00_04Change
//        VisaParameter visaParameter2 = new VisaParameter();
//        visaParameter2.setRcp(EmvData.getRcp(false, true, true, true, false, true));
//        visaParameter2.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter2.setRfTransactionLimit(1000L);
//        visaParameter2.setRfFloorLimit(500L);
//        visaParameter2.setRfCvmLimit(1000L);
//        visaParameters.put("04", visaParameter2); //04
//
//
//
//        // CLQ.D.015.00/01_01
//        VisaParameter visaParameter3 = new VisaParameter();
//        visaParameter3.setRcp(EmvData.getRcp(false, false, false, true, true, true));
//        visaParameter3.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter3.setRfTransactionLimit(2000L);
//        visaParameter3.setRfFloorLimit(1000L);
//        visaParameter3.setRfCvmLimit(500L);
//        visaParameters.put("01", visaParameter3);
//
//        // CLQ.D.015.00/01_02、CLQ.D.016.00_02Change、CLQ.D.017.00_02、CLQ.D.018/019.00_02
//        VisaParameter visaParameter4 = new VisaParameter();
//        visaParameter4.setRcp(EmvData.getRcp(false, true, true, false, true, true));
//        visaParameter4.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter4.setRfTransactionLimit(2000L);
//        visaParameter4.setRfFloorLimit(100000L);
//        visaParameter4.setRfCvmLimit(50000L);
//        visaParameters.put("02", visaParameter4);
//
//        // CLQ.D.015.00/01_03、CLQ.D.017.00_03、CLQ.D.018/019.00_03
//        VisaParameter visaParameter5 = new VisaParameter();
//        visaParameter5.setRcp(EmvData.getRcp(false, true, true, true, true, true));
//        visaParameter5.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter5.setRfTransactionLimit(10000L);
//        visaParameter5.setRfFloorLimit(1000L);
//        visaParameter5.setRfCvmLimit(1000L);
//        visaParameters.put("03", visaParameter5);
//
//        // CLQ.D.016.01_02Change
//        VisaParameter visaParameter6 = new VisaParameter();
//        visaParameter6.setRcp(EmvData.getRcp(false, true, true, false, true, false));
//        visaParameter6.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter6.setRfTransactionLimit(10000L);
//        visaParameter6.setRfFloorLimit(100000L);
//        visaParameter6.setRfCvmLimit(1000L);
//        visaParameters.put("02112233445566779900AABBCCDDEEFF", visaParameter6);
//
//        // CLQ.D.016.01_03Change
//        VisaParameter visaParameter7 = new VisaParameter();
//        visaParameter7.setRcp(EmvData.getRcp(false, true, true, false, true, true));
//        visaParameter7.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter7.setRfTransactionLimit(10000L);
//        visaParameter7.setRfFloorLimit(100000L);
//        visaParameter7.setRfCvmLimit(100000L);
//        visaParameters.put("05112233445566", visaParameter7);
//
//        // CLQ.D.016.01_04Change
//        VisaParameter visaParameter8 = new VisaParameter();
//        visaParameter8.setRcp(EmvData.getRcp(false, true, true, false, false, false));
//        visaParameter8.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter8.setRfTransactionLimit(10000L);
//        visaParameter8.setRfFloorLimit(100000L);
//        visaParameter8.setRfCvmLimit(100000L);
//        visaParameters.put("06AA", visaParameter8);
//
//        // CLQ.D.017.01
//        VisaParameter visaParameter9 = new VisaParameter();
//        visaParameter9.setRcp(EmvData.getRcp(false, true, true, false, true, true));
//        visaParameter9.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter9.setRfTransactionLimit(10000L);
//        visaParameter9.setRfFloorLimit(3000L);
//        visaParameter9.setRfCvmLimit(3000L);
//        visaParameters.put("FFFFAF", visaParameter9);
//
//        // CLQ.D.020.00_01
//        VisaParameter visaParameter10 = new VisaParameter();
//        visaParameter10.setRcp(EmvData.getRcp(false, false, false, true, true, true));
//        visaParameter10.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter10.setRfTransactionLimit(20000L);
//        visaParameter10.setRfFloorLimit(1000L);
//        visaParameter10.setRfCvmLimit(500L);
//        visaParameters.put("10", visaParameter10);
//
//        // CLQ.D.020.00_01
//        VisaParameter visaParameter11 = new VisaParameter();
//        visaParameter11.setRcp(EmvData.getRcp(false, true, true, false, true, true));
//        visaParameter11.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter11.setRfTransactionLimit(20000L);
//        visaParameter11.setRfFloorLimit(100000L);
//        visaParameter11.setRfCvmLimit(50000L);
//        visaParameters.put("110840", visaParameter11);
//
//        // CLQ.D.020.00_03
//        VisaParameter visaParameter12 = new VisaParameter();
//        visaParameter12.setRcp(EmvData.getRcp(false, true, true, true, true, true));
//        visaParameter12.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter12.setRfTransactionLimit(10000L);
//        visaParameter12.setRfFloorLimit(1000L);
//        visaParameter12.setRfCvmLimit(1000L);
//        visaParameters.put("1508400840404550", visaParameter12);
//
//        // CLQ.D.020.00_04
//        VisaParameter visaParameter13 = new VisaParameter();
//        visaParameter13.setRcp(EmvData.getRcp(false, true, false, true, true, true));
//        visaParameter13.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter13.setRfTransactionLimit(10000L);
//        visaParameter13.setRfFloorLimit(1000L);
//        visaParameter13.setRfCvmLimit(1000L);
//        visaParameters.put("19084008406677889900AABBCCDDEEFF", visaParameter13);
//
//        // CLQ.D.020.01_01
//        VisaParameter visaParameter14 = new VisaParameter();
//        visaParameter14.setRcp(EmvData.getRcp(false, true, false, true, true, true));
//        visaParameter14.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter14.setRfTransactionLimit(5000L);
//        visaParameter14.setRfFloorLimit(1000L);
//        visaParameter14.setRfCvmLimit(3000L);
//        visaParameters.put("20", visaParameter14);
//
//        // CLQ.D.020.01_02
//        VisaParameter visaParameter15 = new VisaParameter();
//        visaParameter15.setRcp(EmvData.getRcp(true, true, true, false, true, true));
//        visaParameter15.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter15.setRfTransactionLimit(500L);
//        visaParameter15.setRfFloorLimit(100000L);
//        visaParameter15.setRfCvmLimit(100000L);
//        visaParameters.put("210124", visaParameter15);
//
//        // CLQ.D.020.01_03
//        VisaParameter visaParameter16 = new VisaParameter();
//        visaParameter16.setRcp(EmvData.getRcp(false, true, true, true, true, true));
//        visaParameter16.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter16.setRfTransactionLimit(10000L);
//        visaParameter16.setRfFloorLimit(10000L);
//        visaParameter16.setRfCvmLimit(10000L);
//        visaParameters.put("2501240124607080", visaParameter16);
//
//        // CLQ.D.020.01_04
//        VisaParameter visaParameter17 = new VisaParameter();
//        visaParameter17.setRcp(EmvData.getRcp(true, true, true, false, true, true));
//        visaParameter17.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter17.setRfTransactionLimit(10000L);
//        visaParameter17.setRfFloorLimit(3000L);
//        visaParameter17.setRfCvmLimit(2000L);
//        visaParameters.put("29012401246655443322AABBCCDDEEFF", visaParameter17);
//
//        // CLQ.D.020.02_01
//        VisaParameter visaParameter18 = new VisaParameter();
//        visaParameter18.setRcp(EmvData.getRcp(true, true, true, true, true, true));
//        visaParameter18.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter18.setRfTransactionLimit(3300L);
//        visaParameter18.setRfFloorLimit(9900L);
//        visaParameter18.setRfCvmLimit(5700L);
//        visaParameters.put("30", visaParameter18);
//
//        // CLQ.D.020.02_02
//        VisaParameter visaParameter19 = new VisaParameter();
//        visaParameter19.setRcp(EmvData.getRcp(true, false, false, true, true, true));
//        visaParameter19.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter19.setRfTransactionLimit(72200L);
//        visaParameter19.setRfFloorLimit(54300L);
//        visaParameter19.setRfCvmLimit(19100L);
//        visaParameters.put("320978", visaParameter19);
//
//        // CLQ.D.020.02_03
//        VisaParameter visaParameter20 = new VisaParameter();
//        visaParameter20.setRcp(EmvData.getRcp(false, true, true, true, true, true));
//        visaParameter20.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter20.setRfTransactionLimit(10000L);
//        visaParameter20.setRfFloorLimit(10000L);
//        visaParameter20.setRfCvmLimit(10000L);
//        visaParameters.put("3609780250556677", visaParameter20);
//
//        // CLQ.D.020.02_04
//        VisaParameter visaParameter21 = new VisaParameter();
//        visaParameter21.setRcp(EmvData.getRcp(true, true, true, false, false, true));
//        visaParameter21.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter21.setRfTransactionLimit(10000L);
//        visaParameter21.setRfFloorLimit(10000L);
//        visaParameter21.setRfCvmLimit(2000L);
//        visaParameters.put("39082608266655443322FFEEDDCCBBAA", visaParameter21);
//
//        // CLQ.D.020.03_01
//        VisaParameter visaParameter22 = new VisaParameter();
//        visaParameter22.setRcp(EmvData.getRcp(false, false, false, true, true, true));
//        visaParameter22.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter22.setRfTransactionLimit(20000L);
//        visaParameter22.setRfFloorLimit(30000L);
//        visaParameter22.setRfCvmLimit(15000L);
//        visaParameters.put("40", visaParameter22);
//
//        // CLQ.D.020.03_02
//        VisaParameter visaParameter23 = new VisaParameter();
//        visaParameter23.setRcp(EmvData.getRcp(true, false, false, true, true, true));
//        visaParameter23.setTransactionProperties(new byte[]{0x26, 0x00, 0x40, 0x00});
//        visaParameter23.setRfTransactionLimit(5400L);
//        visaParameter23.setRfFloorLimit(4400L);
//        visaParameter23.setRfCvmLimit(8200L);
//        visaParameters.put("4207", visaParameter23);

        return visaParameters;
    }

    static Map<String, MasterParameter> getMockMasterParameters() {
        if (masterParameters != null) {
            return masterParameters;
        }

        masterParameters = new HashMap<>();
        // Master parameter hash map:
        // - key: AID. Default master parameters use DEFULT_PARAMETER_KEY.
        // - value: MasterParameter
        MasterParameter masterParameter1 = new MasterParameter();
        masterParameter1.setMode(MasterParameter.MODE_MAG_EMV);
        masterParameter1.setBalanceFlag(MasterParameter.BALANCE_FLAG_BOTH);
        masterParameter1.setSupportRecovery(true);
        masterParameter1.setSupportCdv(true);
        masterParameter1.setRfTransactionLimit(3000000L);
        masterParameter1.setRfFloorLimit(0L);
        masterParameter1.setRfCvmLimit(1000000L);
        masterParameter1.setRfTransactionLimitCdv(2000000L);
        masterParameter1.setCvmCapReq((byte) 0x60);
        masterParameter1.setCvmCapNoReq((byte) 0x08);
        masterParameter1.setCvmCapMagReq((byte) 0x20);
        masterParameter1.setCvmCapMagNoReq((byte) 0x00);

        masterParameters.put(EmvData.DEFULT_PARAMETER_KEY, masterParameter1);
//
        MasterParameter masterParameter2 = new MasterParameter();
        masterParameter2.setMode(MasterParameter.MODE_MAG_EMV);
        masterParameter2.setBalanceFlag(MasterParameter.BALANCE_FLAG_BOTH);
        masterParameter2.setSupportRecovery(true);
        masterParameter2.setSupportCdv(true);
        masterParameter2.setRfTransactionLimit(3000000L);
        masterParameter2.setRfFloorLimit(0L);
        masterParameter2.setRfCvmLimit(1000000L);
        masterParameter2.setRfTransactionLimitCdv(2000000L);
        masterParameter2.setCvmCapReq((byte) 0x60);
        masterParameter2.setCvmCapNoReq((byte) 0x08);
        masterParameter2.setCvmCapMagReq((byte) 0x20);
        masterParameter2.setCvmCapMagNoReq((byte) 0x00);
        masterParameters.put("A0000000041010", masterParameter2);
//
        MasterParameter masterParameter3 = new MasterParameter();
        masterParameter3.setMode(MasterParameter.MODE_MAG_EMV);
        masterParameter3.setBalanceFlag(MasterParameter.BALANCE_FLAG_BOTH);
        masterParameter3.setSupportRecovery(true);
        masterParameter3.setSupportCdv(true);
        masterParameter3.setRfTransactionLimit(3000000L);
        masterParameter3.setRfFloorLimit(0L);
        masterParameter3.setRfCvmLimit(1000000L);
        masterParameter3.setRfTransactionLimitCdv(2000000L);
        masterParameter3.setCvmCapReq((byte) 0x60);
        masterParameter3.setCvmCapNoReq((byte) 0x08);
        masterParameter3.setCvmCapMagReq((byte) 0x20);
        masterParameter3.setCvmCapMagNoReq((byte) 0x00);
        masterParameters.put("A0000000043060", masterParameter3);
//
//        MasterParameter masterParameter4 = new MasterParameter();
//        masterParameter4.setMode(MasterParameter.MODE_MAG_EMV);
//        masterParameter4.setBalanceFlag(MasterParameter.BALANCE_FLAG_BOTH);
//        masterParameter4.setSupportRecovery(false);
//        masterParameter4.setSupportCdv(false);
//
//        masterParameter4.setRfTransactionLimit(4003L);
//        masterParameter4.setRfFloorLimit(2003L);
//        masterParameter4.setRfCvmLimit(1003L);
//        masterParameter4.setRfTransactionLimitCdv(3003L);
//        masterParameter4.setCvmCapReq((byte) 0xF8);
//        masterParameter4.setCvmCapNoReq((byte) 0xA8);
//        masterParameter4.setCvmCapMagReq((byte) 0xF0);
//        masterParameter4.setCvmCapMagNoReq((byte) 0xF0);
//        masterParameters.put("A000000003101008", masterParameter4);

        return masterParameters;
    }

    static Map<String, PbocParameter> getMockPbocParameters() {
        if (pbocParameters != null) {
            return pbocParameters;
        }

        pbocParameters = new HashMap<>();
        // PBOC parameter hash map:
        // - key: AID. Default PBOC parameters use DEFULT_PARAMETER_KEY.
        // - value: PbocParameter
        PbocParameter pbocParameter1 = new PbocParameter();
        pbocParameter1.setSupportECash(false);
        pbocParameter1.setSupportSm(true);
        pbocParameter1.setTransactionProperties(new byte[]{0x26, 0x06, 0x00, 0x00});
        pbocParameter1.setEcLimit(500L);
        pbocParameter1.setRfTransactionLimit(100000L);
        pbocParameter1.setRfCvmLimit(200L);
        pbocParameter1.setRfFloorLimit(500L);
        pbocParameters.put(EmvData.DEFULT_PARAMETER_KEY, pbocParameter1);

        PbocParameter pbocParameter2 = new PbocParameter();
        pbocParameter2.setSupportECash(false);
        pbocParameter2.setSupportSm(true);
        pbocParameter2.setTransactionProperties(new byte[]{0x26, 0x06, 0x00, 0x00});
        pbocParameter2.setEcLimit(501L);
        pbocParameter2.setRfTransactionLimit(100001L);
        pbocParameter2.setRfCvmLimit(201L);
        pbocParameter2.setRfFloorLimit(501L);
        pbocParameters.put("A0000003330101", pbocParameter2);

        PbocParameter pbocParameter3 = new PbocParameter();
        pbocParameter3.setSupportECash(false);
        pbocParameter3.setSupportSm(true);
        pbocParameter3.setTransactionProperties(new byte[]{0x26, 0x06, 0x00, 0x00});
        pbocParameter3.setEcLimit(502L);
        pbocParameter3.setRfTransactionLimit(100002L);
        pbocParameter3.setRfCvmLimit(202L);
        pbocParameter3.setRfFloorLimit(502L);
        pbocParameters.put("A00000009990", pbocParameter3);

        PbocParameter pbocParameter4 = new PbocParameter();
        pbocParameter4.setSupportECash(false);
        pbocParameter4.setSupportSm(true);
        pbocParameter4.setTransactionProperties(new byte[]{0x26, 0x06, 0x00, 0x00});
        pbocParameter4.setEcLimit(503L);
        pbocParameter4.setRfTransactionLimit(100003L);
        pbocParameter4.setRfCvmLimit(203L);
        pbocParameter4.setRfFloorLimit(503L);
        pbocParameters.put("A000000003", pbocParameter4);

        return pbocParameters;
    }

    static Map<String, BaseParameter> getMockBaseParameters() {
        if (baseParameters != null) {
            return baseParameters;
        }

        baseParameters = new HashMap<>();
        // EMV parameter hash map:
        // - key: AID. Default EMV parameters use DEFULT_PARAMETER_KEY.
        // - value: BaseParameter


        BaseParameter baseParameter1 = new BaseParameter();
        baseParameter1.setFloorLimit(0);
        baseParameter1.setRandomSelectionThreshold(100L);
        baseParameter1.setRandomSelectionPercentage((byte) 30);
        baseParameter1.setMaxRandomSelectionPercentage((byte) 90);
        baseParameters.put(EmvData.DEFULT_PARAMETER_KEY, baseParameter1);

        BaseParameter baseParameter11 = new BaseParameter();
        baseParameter11.setFloorLimit(0);
        baseParameter11.setRandomSelectionThreshold(100L);
        baseParameter11.setRandomSelectionPercentage((byte) 30);
        baseParameter11.setMaxRandomSelectionPercentage((byte) 90);
        baseParameter11.setTdol(BytesUtil.hexString2ByteArray("9F02065F2A029A039C0195059F3704"));
        baseParameter11.setDdol(BytesUtil.hexString2ByteArray("9F3704"));

        baseParameter11.setAdditionalTerminalCapabilities(BytesUtil.hexString2Bytes("F000F0A001"));
        baseParameter11.setTerminalCapabilities(BytesUtil.hexString2Bytes("E0F8C8"));//E0F8E8
        baseParameter11.setDefaultTac(BytesUtil.hexString2ByteArray("DC4000A800"));
        baseParameter11.setDenialTac(BytesUtil.hexString2ByteArray("0010000000"));
        baseParameter11.setOnlineTac(BytesUtil.hexString2ByteArray("DC4004F800"));

        baseParameters.put("A0000000033010", baseParameter11);

        BaseParameter baseParameter2 = new BaseParameter();
        baseParameter2.setFloorLimit(0);
        baseParameter2.setRandomSelectionThreshold(100L);
        baseParameter2.setRandomSelectionPercentage((byte) 30);
        baseParameter2.setMaxRandomSelectionPercentage((byte) 90);
        baseParameter2.setTdol(BytesUtil.hexString2ByteArray("9F02065F2A029A039C0195059F3704"));
        baseParameter2.setDdol(BytesUtil.hexString2ByteArray("9F3704"));

        baseParameter2.setAdditionalTerminalCapabilities(BytesUtil.hexString2Bytes("F000F0A001"));
        baseParameter2.setTerminalCapabilities(BytesUtil.hexString2Bytes("E0F8C8"));//E0F8E8
        baseParameter2.setDefaultTac(BytesUtil.hexString2ByteArray("DC4000A800"));
        baseParameter2.setDenialTac(BytesUtil.hexString2ByteArray("0010000000"));
        baseParameter2.setOnlineTac(BytesUtil.hexString2ByteArray("DC4004F800"));

        TLVList tlvList = new TLVList();
        //tlvList.addTLV(EMVTag.EMV_TAG_TM_APPVERNO, BytesUtil.hexString2Bytes("0000"));

        //baseParameter2.setOtherParameters(tlvList);
        baseParameters.put("A0000000031010", baseParameter2);

        BaseParameter baseParameter3 = new BaseParameter();
        baseParameter3.setFloorLimit(0);
        baseParameter3.setRandomSelectionThreshold((long)0);
        baseParameter3.setRandomSelectionPercentage((byte) 90);
        baseParameter3.setMaxRandomSelectionPercentage((byte) 90);
        baseParameter3.setTdol(BytesUtil.hexString2ByteArray("9F02065F2A029A039C0195059F3704"));
        baseParameter3.setDdol(BytesUtil.hexString2ByteArray("9F3704"));
        baseParameter3.setAdditionalTerminalCapabilities(BytesUtil.hexString2Bytes("F000F0A001"));
        baseParameter3.setTerminalCapabilities(BytesUtil.hexString2Bytes("E0F0C8"));
        baseParameter3.setDefaultTac(BytesUtil.hexString2ByteArray("DC4000A800"));
        baseParameter3.setDenialTac(BytesUtil.hexString2ByteArray("0010000000"));
        baseParameter3.setOnlineTac(BytesUtil.hexString2ByteArray("DC4004F800"));


        //A00000000410101213
        //A00000000410101215

        //A0000000042010
        //A0000000042203
        //A0000000043010

        //A0000000043060
        //A0000000044010

        //A0000000045010

        TLVList tlvList3 = new TLVList();
        //tlvList3.addTLV(EMVTag.EMV_TAG_TM_APPVERNO, BytesUtil.hexString2Bytes("0000"));
        //baseParameter3.setOtherParameters(tlvList3);
        baseParameters.put("A0000000041010", baseParameter3);

        BaseParameter baseParameter4 = new BaseParameter();
        baseParameter4.setFloorLimit(0);
        baseParameter4.setRandomSelectionThreshold((long)0);
        baseParameter4.setRandomSelectionPercentage((byte) 90);
        baseParameter4.setMaxRandomSelectionPercentage((byte) 90);
        baseParameter4.setTdol(BytesUtil.hexString2ByteArray("9F02065F2A029A039C0195059F3704"));
        baseParameter4.setDdol(BytesUtil.hexString2ByteArray("9F3704"));
        baseParameter4.setAdditionalTerminalCapabilities(BytesUtil.hexString2Bytes("F000F0A001"));
        baseParameter4.setTerminalCapabilities(BytesUtil.hexString2Bytes("E0F0C8"));//E0F8E8
        baseParameter4.setDefaultTac(BytesUtil.hexString2ByteArray("DC4000A800"));//"FC60ACF800"
        baseParameter4.setDenialTac(BytesUtil.hexString2ByteArray("0010000000"));
        baseParameter4.setOnlineTac(BytesUtil.hexString2ByteArray("DC4004F800"));//"FC6024A800"

        TLVList tlvList4 = new TLVList();
        //tlvList4.addTLV(EMVTag.EMV_TAG_TM_APPVERNO, BytesUtil.hexString2Bytes("0000"));
        //baseParameter4.setOtherParameters(tlvList4);
        baseParameters.put("A0000000651010", baseParameter4);



        BaseParameter baseParameter41 = new BaseParameter();
        baseParameter41.setFloorLimit(0);
        baseParameter41.setRandomSelectionThreshold((long)0);
        baseParameter41.setRandomSelectionPercentage((byte) 90);
        baseParameter41.setMaxRandomSelectionPercentage((byte) 90);
        baseParameter41.setTdol(BytesUtil.hexString2ByteArray("9F02065F2A029A039C0195059F3704"));
        baseParameter41.setDdol(BytesUtil.hexString2ByteArray("9F3704"));
        baseParameter41.setAdditionalTerminalCapabilities(BytesUtil.hexString2Bytes("F000F0A001"));
        baseParameter41.setTerminalCapabilities(BytesUtil.hexString2Bytes("E0F0C8"));//E0F8E8
        baseParameter41.setDefaultTac(BytesUtil.hexString2ByteArray("DC4000A800"));//"FC60ACF800"
        baseParameter41.setDenialTac(BytesUtil.hexString2ByteArray("0010000000"));
        baseParameter41.setOnlineTac(BytesUtil.hexString2ByteArray("DC4004F800"));//"FC6024A800"

        baseParameters.put("A0000000043060", baseParameter41);


        BaseParameter baseParameter5 = new BaseParameter();
        baseParameter5.setFloorLimit(0);
        baseParameter5.setRandomSelectionThreshold((long)0);
        baseParameter5.setRandomSelectionPercentage((byte) 70);
        baseParameter5.setMaxRandomSelectionPercentage((byte) 70);
        baseParameter5.setTdol(BytesUtil.hexString2ByteArray("9F02065F2A029A039C0195059F3704"));
        baseParameter5.setDdol(BytesUtil.hexString2ByteArray("9F3704"));
        baseParameter5.setAdditionalTerminalCapabilities(BytesUtil.hexString2Bytes("F000F0A001"));
        baseParameter5.setTerminalCapabilities(BytesUtil.hexString2Bytes("E0F8C8"));//E0F8E8
        baseParameter5.setDefaultTac(BytesUtil.hexString2ByteArray("DC4000A800"));//"FC60ACF800"
        baseParameter5.setDenialTac(BytesUtil.hexString2ByteArray("0010000000"));
        baseParameter5.setOnlineTac(BytesUtil.hexString2ByteArray("DC4004F800"));//"FC6024A800"

        TLVList tlvList5 = new TLVList();
      //  tlvList5.addTLV(EMVTag.EMV_TAG_TM_APPVERNO, BytesUtil.hexString2Bytes("0000"));
      //  baseParameter5.setOtherParameters(tlvList5);
        baseParameters.put("A0000006021010", baseParameter5);


        return baseParameters;
    }

    static Map<String, BaseParameter> getMockBaseParameters_Paypass() {
        if (baseParametersPaypass != null) {
            return baseParametersPaypass;
        }

        baseParametersPaypass = new HashMap<>();
        // EMV parameter hash map:
        // - key: AID. Default EMV parameters use DEFULT_PARAMETER_KEY.
        // - value: BaseParameter


//<priority_level>PRIMARY</priority_level>

//

//            <paypass_terminal_risk_management>2C70000000000000</paypass_terminal_risk_management>

//            <paypass_transaction_category_code>52</paypass_transaction_category_code>
//            <paypass_kernel_configuration>02</paypass_kernel_configuration>
//            <paypass_emv_app_version_term>0002</paypass_emv_app_version_term>
//            <paypass_mstripe_app_version_term>0001</paypass_mstripe_app_version_term>

        BaseParameter baseParameter = new BaseParameter();

        baseParameter.setRandomSelectionThreshold(0L);
        baseParameter.setRandomSelectionPercentage((byte) 20);
        baseParameter.setMaxRandomSelectionPercentage((byte) 30);

        baseParameter.setTdol(BytesUtil.hexString2ByteArray("9F0802"));
        baseParameter.setUdol(BytesUtil.hexString2ByteArray("9F6A04"));
        baseParameter.setTerminalCapabilities(BytesUtil.hexString2Bytes("E008C8"));//E0F8E8

        baseParameter.setDefaultTac(BytesUtil.hexString2ByteArray("F45084800C"));
        baseParameter.setDenialTac(BytesUtil.hexString2ByteArray("0010000000"));//0010000000 //840000000C it has to be this, need to check error
        baseParameter.setOnlineTac(BytesUtil.hexString2ByteArray("F45084800C"));

        TLVList tlvList_base = new TLVList();
        tlvList_base.addTLV(EMVTag.EMV_TAG_TM_APPVERNO, BytesUtil.hexString2Bytes("0002"));
        tlvList_base.addTLV(EMVTag.EMV_TAG_TM_RMDATA,BytesUtil.hexString2Bytes("6C70800000000000"));
        tlvList_base.addTLV(EMVTag.DEF_TAG_M_RRP_SWITCH,BytesUtil.hexString2Bytes("01"));

        baseParameter.setOtherParameters(tlvList_base);
        baseParametersPaypass.put(EmvData.DEFULT_PARAMETER_KEY, baseParameter);

        BaseParameter baseParameter2 = new BaseParameter();

        baseParameter2.setRandomSelectionThreshold(0L);
        baseParameter2.setRandomSelectionPercentage((byte) 20);
        baseParameter2.setMaxRandomSelectionPercentage((byte) 30);

        baseParameter2.setTdol(BytesUtil.hexString2ByteArray("9F0802"));
        baseParameter2.setUdol(BytesUtil.hexString2ByteArray("9F6A04"));
        baseParameter2.setTerminalCapabilities(BytesUtil.hexString2Bytes("E008C8"));//E0F8E8

        baseParameter2.setDefaultTac(BytesUtil.hexString2ByteArray("F45084800C"));
        baseParameter2.setDenialTac(BytesUtil.hexString2ByteArray("0010000000"));//0010000000 //840000000C it has to be this, need to check error
        baseParameter2.setOnlineTac(BytesUtil.hexString2ByteArray("F45084800C"));

        TLVList tlvList = new TLVList();
        tlvList.addTLV(EMVTag.EMV_TAG_TM_APPVERNO, BytesUtil.hexString2Bytes("0002"));
        tlvList.addTLV(EMVTag.EMV_TAG_TM_RMDATA,BytesUtil.hexString2Bytes("6C70800000000000"));
        tlvList.addTLV(EMVTag.DEF_TAG_M_RRP_SWITCH,BytesUtil.hexString2Bytes("01"));

        baseParameter2.setOtherParameters(tlvList);
        baseParametersPaypass.put("A0000000041010", baseParameter2);

        BaseParameter baseParameter3 = new BaseParameter();

        baseParameter3.setRandomSelectionThreshold(0L);
        baseParameter3.setRandomSelectionPercentage((byte) 20);
        baseParameter3.setMaxRandomSelectionPercentage((byte) 0);

        baseParameter3.setTdol(BytesUtil.hexString2ByteArray("9F0802"));
        baseParameter3.setUdol(BytesUtil.hexString2ByteArray("9F6A04"));
        baseParameter3.setTerminalCapabilities(BytesUtil.hexString2Bytes("E0F8C8"));//E0F8E8
        baseParameter3.setDefaultTac(BytesUtil.hexString2ByteArray("F45084800C"));
        baseParameter3.setDenialTac(BytesUtil.hexString2ByteArray("0010000000"));
        baseParameter3.setOnlineTac(BytesUtil.hexString2ByteArray("F45084800C"));

        TLVList tlvList1 = new TLVList();
        tlvList1.addTLV(EMVTag.EMV_TAG_TM_APPVERNO, BytesUtil.hexString2Bytes("0002"));
        tlvList1.addTLV(EMVTag.EMV_TAG_TM_RMDATA,BytesUtil.hexString2Bytes("6C70800000000000"));
        tlvList1.addTLV(EMVTag.DEF_TAG_M_RRP_SWITCH,BytesUtil.hexString2Bytes("01"));
        baseParameter3.setOtherParameters(tlvList1);
        baseParametersPaypass.put("A0000000043060", baseParameter3);


        return baseParametersPaypass;
    }

    static Map<String, Boolean> getMockAids() {
        if (aids != null) {
            return aids;
        }

        aids = new HashMap<>();
        // AID hash map:
        // - key: AID
        // - value: Supports partly AID match or not.
        //aids.put("A0000000031010010203040506070809", true);
        aids.put("A0000000031010", true);
        aids.put("A0000000032010",true);
        aids.put("A0000000033010",true);
        aids.put("A0000000041010", true);
        aids.put("A0000000043060", true);
        aids.put("A0000000651010", true);
        aids.put("A0000006021010", true);


        return aids;
    }

    static List<CAPublicKey> getMockPublicKeys() {
        if (publicKeys != null && publicKeys.size() > 0) {
            return publicKeys;
        }

        publicKeys = new ArrayList<>();
        CAPublicKey publicKey1 = new CAPublicKey();
        publicKey1.setRid(BytesUtil.hexString2ByteArray("A000009999"));
        publicKey1.setIndex((byte) 0xE1);
        publicKey1.setMod(BytesUtil.hexString2ByteArray("99C5B70AA61B4F4C51B6F90B0E3BFB7A3EE0E7DB41BC466888B3EC8E9977C762407EF1D79E0AFB2823100A020C3E8020593DB50E90DBEAC18B78D13F96BB2F57EEDDC30F256592417CDF739CA6804A10A29D2806E774BFA751F22CF3B65B38F37F91B4DAF8AEC9B803F7610E06AC9E6B"));
        publicKey1.setExp(new byte[]{0x03});
        publicKeys.add(publicKey1);

        CAPublicKey publicKey2 = new CAPublicKey();
        publicKey2.setRid(BytesUtil.hexString2ByteArray("A000009999"));
        publicKey2.setIndex((byte) 0xE2);
        publicKey2.setMod(BytesUtil.hexString2ByteArray("BD232E348B118EB3F6446EF4DA6C3BAC9B2AE510C5AD107D38343255D21C4BDF4952A42E92C633B1CE4BFEC39AFB6DFE147ECBB91D681DAC15FB0E198E9A7E4636BDCA107BCDA3384FCB28B06AFEF90F099E7084511F3CC010D4343503E1E5A67264B4367DAA9A3949499272E9B5022F"));
        publicKey2.setExp(new byte[]{0x03});
        publicKeys.add(publicKey2);

        CAPublicKey publicKey3 = new CAPublicKey();
        publicKey3.setRid(BytesUtil.hexString2ByteArray("A000009999"));
        publicKey3.setIndex((byte) 0xE3);
        publicKey3.setMod(BytesUtil.hexString2ByteArray("BC01E12223E1A41E88BFFA801093C5F8CEC5CD05DBBDBB787CE87249E8808327C2D218991F97A1131E8A25B0122ED11E709C533E8886A1259ADDFDCBB396604D24E505A2D0B5DD0384FB0002A7A1EB39BC8A11339C7A9433A948337761BE73BC497B8E58736DA4636538AD282D3CD3DB"));
        publicKey3.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey3);

        CAPublicKey publicKey4 = new CAPublicKey();
        publicKey4.setRid(BytesUtil.hexString2ByteArray("A000009999"));
        publicKey4.setIndex((byte) 0xE4);
        publicKey4.setMod(BytesUtil.hexString2ByteArray("CBF2E40F0836C9A5E390A37BE3B809BDF5D740CB1DA38CFC05D5F8D6B7745B5E9A3FA6961E55FF20412108525E66B970F902F7FF4305DD832CD0763E3AA8B8173F84777100B1047BD1D744509312A0932ED25FED52A959430768CCD902FD8C8AD9123E6ADDB3F34B92E7924D729CB6473533AE2B2B55BF0E44964FDEA8440117"));
        publicKey4.setExp(new byte[]{0x03});
        publicKeys.add(publicKey4);

        CAPublicKey publicKey5 = new CAPublicKey();
        publicKey5.setRid(BytesUtil.hexString2ByteArray("A000009999"));
        publicKey5.setIndex((byte) 0xE5);
        publicKey5.setMod(BytesUtil.hexString2ByteArray("D4FDAE94DEDBECC6D20D38B01E91826DC6954338379917B2BB8A6B36B5D3B0C5EDA60B337448BAFFEBCC3ABDBA869E8DADEC6C870110C42F5AAB90A18F4F867F72E3386FFC7E67E7FF94EBA079E531B3CF329517E81C5DD9B3DC65DB5F9043190BE0BE897E5FE48ADF5D3BFA0585E076E554F26EC69814797F15669F4A255C13"));
        publicKey5.setExp(new byte[]{0x03});
        publicKeys.add(publicKey5);

        CAPublicKey publicKey6 = new CAPublicKey();
        publicKey6.setRid(BytesUtil.hexString2ByteArray("A000009999"));
        publicKey6.setIndex((byte) 0xE6);
        publicKey6.setMod(BytesUtil.hexString2ByteArray("EBF9FAECC3E5C315709694664775D3FBDA5A504D89344DD920C55696E891D9AB622598A9D6AB8FBF35E4599CAB7EB22F956992F8AB2E6535DECB6B576FA0675F97C23DD4C374A66E6AF419C9D204D0B9F93C08D789D63805660FBB629DF1B488CFA1D7A13E9B729437EEAFE718EFA859348BA0D76812A99F31CD364F2A4FD42F"));
        publicKey6.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey6);

        CAPublicKey publicKey7 = new CAPublicKey();
        publicKey7.setRid(BytesUtil.hexString2ByteArray("A000000025"));
        publicKey7.setIndex((byte) 0x60);
        publicKey7.setMod(BytesUtil.hexString2ByteArray("A8EE74EDEF3C0DCA5102FF9B5707975FF67B60D64B5E7322D48DE9D3BB6153F63512A091B606DD8FD5F6A14588324EF8827844C7FFC0BAB2334AE5207770078B69CDC3F2C666CF69E28E16E1816714C4DF313BEF539CC01DA9DD2D6F47DE4F247C500B561C099166AD4FC16DF12DFB684AC48D35CDD2C47A13A86A5A162306F64E33B092AB74EDA71A4091D96E3DAA47"));
        publicKey7.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey7);

        CAPublicKey publicKey8 = new CAPublicKey();
        publicKey8.setRid(BytesUtil.hexString2ByteArray("A000000025"));
        publicKey8.setIndex((byte) 0x61);
        publicKey8.setMod(BytesUtil.hexString2ByteArray("86C7254665E17CE6934DF7D082569F208D1CC1AD8E9FB2FE23E3D7467BE50B4F874F906ADF2280EC9D204F6D10C037A23CE5FD8283C9ED47D1C669ABDD7C1CB356C70BCDC44E5C8AE231555F7B786AC9C3155BCD51F28EFBC1B33CC87277049219B2C890952736C4713487111678911D9F42E08074CF524E65D721D727F054E6B5E85EC92B3EB59FFEE926DD6C314DF555C94AD487A99B67CB7C7BA5E46A5B813DDB918B8E3E0423F4302A58686D1263C0BACA9E82068C493289E3E6936ECA5F9F77E06B0D6FBDA718818B835020098C671C5DD7E9B8E8E841D2DF32EE94A7F4748484CA44108AB241A5263BA1FF00D51360DDDC749D30A1"));
        publicKey8.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey8);


        CAPublicKey publicKey16 = new CAPublicKey();
        publicKey16.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey16.setIndex((byte) 0x99);
        publicKey16.setMod(BytesUtil.hexString2ByteArray("AB79FCC9520896967E776E64444E5DCDD6E13611874F3985722520425295EEA4BD0C2781DE7F31CD3D041F565F747306EED62954B17EDABA3A6C5B85A1DE1BEB9A34141AF38FCF8279C9DEA0D5A6710D08DB4124F041945587E20359BAB47B7575AD94262D4B25F264AF33DEDCF28E09615E937DE32EDC03C54445FE7E382777"));
        publicKey16.setExp(new byte[]{0x03});
        publicKey16.setHash(BytesUtil.hexString2ByteArray("4ABFFD6B1C51212D05552E431C5B17007D2F5E6D"));
        publicKeys.add(publicKey16);

        CAPublicKey publicKey17 = new CAPublicKey();
        publicKey17.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey17.setIndex((byte) 0x95);
        publicKey17.setMod(BytesUtil.hexString2ByteArray("BE9E1FA5E9A803852999C4AB432DB28600DCD9DAB76DFAAA47355A0FE37B1508AC6BF38860D3C6C2E5B12A3CAAF2A7005A7241EBAA7771112C74CF9A0634652FBCA0E5980C54A64761EA101A114E0F0B5572ADD57D010B7C9C887E104CA4EE1272DA66D997B9A90B5A6D624AB6C57E73C8F919000EB5F684898EF8C3DBEFB330C62660BED88EA78E909AFF05F6DA627B"));
        publicKey17.setExp(new byte[]{0x03});
        publicKey17.setHash(BytesUtil.hexString2ByteArray("EE1511CEC71020A9B90443B37B1D5F6E703030F6"));
        publicKeys.add(publicKey17);

        CAPublicKey publicKey18 = new CAPublicKey();
        publicKey18.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey18.setIndex((byte) 0x92);
        publicKey18.setMod(BytesUtil.hexString2ByteArray("996AF56F569187D09293C14810450ED8EE3357397B18A2458EFAA92DA3B6DF6514EC060195318FD43BE9B8F0CC669E3F844057CBDDF8BDA191BB64473BC8DC9A730DB8F6B4EDE3924186FFD9B8C7735789C23A36BA0B8AF65372EB57EA5D89E7D14E9C7B6B557460F10885DA16AC923F15AF3758F0F03EBD3C5C2C949CBA306DB44E6A2C076C5F67E281D7EF56785DC4D75945E491F01918800A9E2DC66F60080566CE0DAF8D17EAD46AD8E30A247C9F"));
        publicKey18.setExp(new byte[]{0x03});
        publicKey18.setHash(BytesUtil.hexString2ByteArray("429C954A3859CEF91295F663C963E582ED6EB253"));
        publicKeys.add(publicKey18);

        CAPublicKey publicKey19 = new CAPublicKey();
        publicKey19.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey19.setIndex((byte) 0x09);
        publicKey19.setMod(BytesUtil.hexString2ByteArray("9D912248DE0A4E39C1A7DDE3F6D2588992C1A4095AFBD1824D1BA74847F2BC4926D2EFD904B4B54954CD189A54C5D1179654F8F9B0D2AB5F0357EB642FEDA95D3912C6576945FAB897E7062CAA44A4AA06B8FE6E3DBA18AF6AE3738E30429EE9BE03427C9D64F695FA8CAB4BFE376853EA34AD1D76BFCAD15908C077FFE6DC5521ECEF5D278A96E26F57359FFAEDA19434B937F1AD999DC5C41EB11935B44C18100E857F431A4A5A6BB65114F174C2D7B59FDF237D6BB1DD0916E644D709DED56481477C75D95CDD68254615F7740EC07F330AC5D67BCD75BF23D28A140826C026DBDE971A37CD3EF9B8DF644AC385010501EFC6509D7A41"));
        publicKey19.setExp(new byte[]{0x03});
        publicKey19.setHash(BytesUtil.hexString2ByteArray("1FF80A40173F52D7D27E0F26A146A1C8CCB29046"));
        publicKeys.add(publicKey19);

        CAPublicKey publicKey20 = new CAPublicKey();
        publicKey20.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey20.setIndex((byte) 0x08);
        publicKey20.setMod(BytesUtil.hexString2ByteArray("D9FD6ED75D51D0E30664BD157023EAA1FFA871E4DA65672B863D255E81E137A51DE4F72BCC9E44ACE12127F87E263D3AF9DD9CF35CA4A7B01E907000BA85D24954C2FCA3074825DDD4C0C8F186CB020F683E02F2DEAD3969133F06F7845166ACEB57CA0FC2603445469811D293BFEFBAFAB57631B3DD91E796BF850A25012F1AE38F05AA5C4D6D03B1DC2E568612785938BBC9B3CD3A910C1DA55A5A9218ACE0F7A21287752682F15832A678D6E1ED0B"));
        publicKey20.setExp(new byte[]{0x03});
        publicKey20.setHash(BytesUtil.hexString2ByteArray("20D213126955DE205ADC2FD2822BD22DE21CF9A8"));
        publicKeys.add(publicKey20);

        CAPublicKey publicKey21 = new CAPublicKey();
        publicKey21.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey21.setIndex((byte) 0x07);
        publicKey21.setMod(BytesUtil.hexString2ByteArray("A89F25A56FA6DA258C8CA8B40427D927B4A1EB4D7EA326BBB12F97DED70AE5E4480FC9C5E8A972177110A1CC318D06D2F8F5C4844AC5FA79A4DC470BB11ED635699C17081B90F1B984F12E92C1C529276D8AF8EC7F28492097D8CD5BECEA16FE4088F6CFAB4A1B42328A1B996F9278B0B7E3311CA5EF856C2F888474B83612A82E4E00D0CD4069A6783140433D50725F"));
        publicKey21.setExp(new byte[]{0x03});
        publicKey21.setHash(BytesUtil.hexString2ByteArray("B4BC56CC4E88324932CBC643D6898F6FE593B172"));
        publicKeys.add(publicKey21);

        CAPublicKey publicKey22 = new CAPublicKey();
        publicKey22.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey22.setIndex((byte) 0x01);
        publicKey22.setMod(BytesUtil.hexString2ByteArray("A89F25A56FA6DA258C8CA8B40427D927B4A1EB4D7EA326BBB12F97DED70AE5E4480FC9C5E8A972177110A1CC318D06D2F8F5C4844AC5FA79A4DC470BB11ED635699C17081B90F1B984F12E92C1C529276D8AF8EC7F28492097D8CD5BECEA16FE4088F6CFAB4A1B42328A1B996F9278B0B7E3311CA5EF856C2F888474B83612A82E4E00D0CD4069A6783140433D50725F"));
        publicKey22.setExp(new byte[]{0x03});
        publicKey22.setHash(BytesUtil.hexString2ByteArray("D34A6A776011C7E7CE3AEC5F03AD2F8CFC5503CC"));
        publicKeys.add(publicKey22);

        CAPublicKey publicKey23 = new CAPublicKey();
        publicKey23.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey23.setIndex((byte) 0x50);
        publicKey23.setMod(BytesUtil.hexString2ByteArray("D11197590057B84196C2F4D11A8F3C05408F422A35D702F90106EA5B019BB28AE607AA9CDEBCD0D81A38D48C7EBB0062D287369EC0C42124246AC30D80CD602AB7238D51084DED4698162C59D25EAC1E66255B4DB2352526EF0982C3B8AD3D1CCE85B01DB5788E75E09F44BE7361366DEF9D1E1317B05E5D0FF5290F88A0DB47"));
        publicKey23.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey23);

        CAPublicKey publicKey24 = new CAPublicKey();
        publicKey24.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey24.setIndex((byte) 0x51);
        publicKey24.setMod(BytesUtil.hexString2ByteArray("DB5FA29D1FDA8C1634B04DCCFF148ABEE63C772035C79851D3512107586E02A917F7C7E885E7C4A7D529710A145334CE67DC412CB1597B77AA2543B98D19CF2CB80C522BDBEA0F1B113FA2C86216C8C610A2D58F29CF3355CEB1BD3EF410D1EDD1F7AE0F16897979DE28C6EF293E0A19282BD1D793F1331523FC71A228800468C01A3653D14C6B4851A5C029478E757F"));
        publicKey24.setExp(new byte[]{0x03});
        publicKey24.setHash(BytesUtil.hexString2ByteArray("B9D248075A3F23B522FE45573E04374DC4995D71"));
        publicKeys.add(publicKey24);

        CAPublicKey publicKey25 = new CAPublicKey();
        publicKey25.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey25.setIndex((byte) 0x53);
        publicKey25.setMod(BytesUtil.hexString2ByteArray("BCD83721BE52CCCC4B6457321F22A7DC769F54EB8025913BE804D9EABBFA19B3D7C5D3CA658D768CAF57067EEC83C7E6E9F81D0586703ED9DDDADD20675D63424980B10EB364E81EB37DB40ED100344C928886FF4CCC37203EE6106D5B59D1AC102E2CD2D7AC17F4D96C398E5FD993ECB4FFDF79B17547FF9FA2AA8EEFD6CBDA124CBB17A0F8528146387135E226B005A474B9062FF264D2FF8EFA36814AA2950065B1B04C0A1AE9B2F69D4A4AA979D6CE95FEE9485ED0A03AEE9BD953E81CFD1EF6E814DFD3C2CE37AEFA38C1F9877371E91D6A5EB59FDEDF75D3325FA3CA66CDFBA0E57146CC789818FF06BE5FCC50ABD362AE4B80996D"));
        publicKey25.setExp(new byte[]{0x03});
        publicKeys.add(publicKey25);

        CAPublicKey publicKey26 = new CAPublicKey();
        publicKey26.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey26.setIndex((byte) 0x54);
        publicKey26.setMod(BytesUtil.hexString2ByteArray("C6DDC0B7645F7F16286AB7E4116655F56DD0C944766040DC68664DD973BD3BFD4C525BCBB95272B6B3AD9BA8860303AD08D9E8CC344A4070F4CFB9EEAF29C8A3460850C264CDA39BBE3A7E7D08A69C31B5C8DD9F94DDBC9265758C0E7399ADCF4362CAEE458D414C52B498274881B196DACCA7273F687F2A65FAEB809D4B2AC1D3D1EFB4F6490322318BD296D153B307A3283AB4E5BE6EBD910359A8565EB9C4360D24BAACA3DBFE393F3D6C830D603C6FC1E83409DFCD80D3A33BA243813BBB4CEAF9CBAB6B74B00116F72AB278A88A011D70071E06CAB140646438D986D48281624B85B3B2EBB9A6AB3BF2178FCC3011E7CAF24897AE7D"));
        publicKey26.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey26);

        CAPublicKey publicKey27 = new CAPublicKey();
        publicKey27.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey27.setIndex((byte) 0x96);
        publicKey27.setMod(BytesUtil.hexString2ByteArray("B74586D19A207BE6627C5B0AAFBC44A2ECF5A2942D3A26CE19C4FFAEEE920521868922E893E7838225A3947A2614796FB2C0628CE8C11E3825A56D3B1BBAEF783A5C6A81F36F8625395126FA983C5216D3166D48ACDE8A431212FF763A7F79D9EDB7FED76B485DE45BEB829A3D4730848A366D3324C3027032FF8D16A1E44D8D"));
        publicKey27.setExp(new byte[]{0x03});
        publicKeys.add(publicKey27);

        CAPublicKey publicKey28 = new CAPublicKey();
        publicKey28.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey28.setIndex((byte) 0x57);
        publicKey28.setMod(BytesUtil.hexString2ByteArray("942B7F2BA5EA307312B63DF77C5243618ACC2002BD7ECB74D821FE7BDC78BF28F49F74190AD9B23B9713B140FFEC1FB429D93F56BDC7ADE4AC075D75532C1E590B21874C7952F29B8C0F0C1CE3AEEDC8DA25343123E71DCF86C6998E15F756E3"));
        publicKey28.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey28);

        CAPublicKey publicKey29 = new CAPublicKey();
        publicKey29.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey29.setIndex((byte) 0x58);
        publicKey29.setMod(BytesUtil.hexString2ByteArray("99552C4A1ECD68A0260157FC4151B5992837445D3FC57365CA5692C87BE358CDCDF2C92FB6837522842A48EB11CDFFE2FD91770C7221E4AF6207C2DE4004C7DEE1B6276DC62D52A87D2CD01FBF2DC4065DB52824D2A2167A06D19E6A0F781071CDB2DD314CB94441D8DC0E936317B77BF06F5177F6C5ABA3A3BC6AA30209C97260B7A1AD3A192C9B8CD1D153570AFCC87C3CD681D13E997FE33B3963A0A1C79772ACF991033E1B8397AD0341500E48A24770BC4CBE19D2CCF419504FDBF0389BC2F2FDCD4D44E61F"));
        publicKey29.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey29);

//        CAPublicKey publicKey30 = new CAPublicKey();
//        publicKey30.setRid(BytesUtil.hexString2ByteArray("A000000003"));
//        publicKey30.setIndex((byte) 0x94);
//        publicKey30.setMod(BytesUtil.hexString2ByteArray("D1BE39615F395AC9337E3307AA5A7AC35EAE0036BF20B92F9A45D190B2F4616ABF9D340CBF5FBB3A2B94BD8F2F977C0A10B90E59D4201AA32669E8CBE753F536119DF4FB5E63CED87F1153CE914B124F3E6B648CD5C97655F7AB4DF62607C95DA50517AB8BE3836672D1C71BCDE9BA7293FF3482F124F86691130AB08177B02F459C025A1F3DFFE0884CE78122542EA1C8EA092B552B586907C83AD65E0C6F91A400E485E11192AA4C171C5A1EF56381F4D091CC7EF6BD8604CBC4C74D5D77FFA07B641D53998CDB5C21B7BC65E082A6513F424A4B252E0D77FA4056986A0AB0CDA6155ED9A883C69CC2992D49ECBD4797DD2864FFC96B8D"));
//        publicKey30.setExp(new byte[]{0x01, 0x00, 0x01});
//        publicKeys.add(publicKey30);

        CAPublicKey publicKey31 = new CAPublicKey();
        publicKey31.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey31.setIndex((byte) 0x97);
        publicKey31.setMod(BytesUtil.hexString2ByteArray("AF0754EAED977043AB6F41D6312AB1E22A6809175BEB28E70D5F99B2DF18CAE73519341BBBD327D0B8BE9D4D0E15F07D36EA3E3A05C892F5B19A3E9D3413B0D97E7AD10A5F5DE8E38860C0AD004B1E06F4040C295ACB457A788551B6127C0B29"));
        publicKey31.setExp(new byte[]{0x03});
        publicKeys.add(publicKey31);



        CAPublicKey publicKey436 = new CAPublicKey();
        publicKey436.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey436.setIndex((byte) 0x94);
        publicKey436.setMod(BytesUtil.hexString2ByteArray("ACD2B12302EE644F3F835ABD1FC7A6F62CCE48FFEC622AA8EF062BEF6FB8BA8BC68BBF6AB5870EED579BC3973E121303D34841A796D6DCBC41DBF9E52C4609795C0CCF7EE86FA1D5CB041071ED2C51D2202F63F1156C58A92D38BC60BDF424E1776E2BC9648078A03B36FB554375FC53D57C73F5160EA59F3AFC5398EC7B67758D65C9BFF7828B6B82D4BE124A416AB7301914311EA462C19F771F31B3B57336000DFF732D3B83DE07052D730354D297BEC72871DCCF0E193F171ABA27EE464C6A97690943D59BDABB2A27EB71CEEBDAFA1176046478FD62FEC452D5CA393296530AA3F41927ADFE434A2DF2AE3054F8840657A26E0FC617"));
        publicKey436.setExp(new byte[]{0x03});
        publicKey436.setHash(BytesUtil.hexString2ByteArray("C4A3C43CCF87327D136B804160E47D43B60E6E0F"));
        publicKeys.add(publicKey436);




        CAPublicKey publicKey437 = new CAPublicKey();
        publicKey437.setRid(BytesUtil.hexString2ByteArray("A000000003"));
        publicKey437.setIndex((byte) 0x8F);
        publicKey437.setMod(BytesUtil.hexString2ByteArray("3C96F7658FBC29A202F19146BDE92166B0F6221BBCCB02E326710B9E229D16FAE9AD0C874C0685916E19F0E32693EE201BCE2359509A6D6572F8EC3FC373126B343F9CB8153D61B7EAB2D42DE19D56083185A03DD14C268D40DF0835C55EABFA38ED28BCE42CD0013DA94F800518B753C246EFFBA08FD2029BAD5DFCF0DAF07B7D801C465FFD252C70B92153B330D95DCA2FA1FAAE2D0168A4EA8B475CD805DC32AA964C17BFCD2CD5D0309AB0EA761B"));
        publicKey437.setExp(new byte[]{0x03});
        publicKeys.add(publicKey437);




        CAPublicKey publicKey32 = new CAPublicKey();
        publicKey32.setRid(BytesUtil.hexString2ByteArray("A000000065"));
        publicKey32.setIndex((byte) 0x02);
        publicKey32.setMod(BytesUtil.hexString2ByteArray("BB7F51983FD8707FD6227C23DEF5D5377A5A737CEF3C5252E578EFE136DF87B50473F9341F1640C8D258034E14C16993FCE6C6B8C3CEEB65FC8FBCD8EB77B3B05AC7C4D09E0FA1BA2EFE87D3184DB6718AE41A7CAD89B8DCE0FE80CEB523D5D647F9DB58A31D2E71AC677E67FA6E75820736C9893761EE4ACD11F31DBDC349EF"));
        publicKey32.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey32);

        CAPublicKey publicKey33 = new CAPublicKey();
        publicKey33.setRid(BytesUtil.hexString2ByteArray("A000000065"));
        publicKey33.setIndex((byte) 0x03);
        publicKey33.setMod(BytesUtil.hexString2ByteArray("C9E6C1F3C6949A8A42A91F8D0224132B2865E6D953A5B5A54CFFB0412439D54AEBA79E9B399A6C104684DF3FB727C7F55984DB7A450E6AA917E110A7F2343A0024D2785D9EBE09F601D592362FDB237700B567BA14BBE2A6D3D23CF1270B3DD822B5496549BF884948F55A0D308348C4B723BAFB6A7F3975AC397CAD3C5D0FC2D178716F5E8E79E75BEB1C84FA202F80E68069A984E008706B30C212305456201540787925E86A8B28B129A11AF204B387CB6EE43DB53D15A46E13901BEBD5CECF4854251D9E9875B16E82AD1C5938A972842C8F1A42EBB5AE5336B04FF3DA8B8DFBE606FCA8B9084EE05BF67950BA89897CD089F924DBCD"));
        publicKey33.setExp(new byte[]{0x03});
        publicKeys.add(publicKey33);

        CAPublicKey publicKey34 = new CAPublicKey();
        publicKey34.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey34.setIndex((byte) 0x02);
        publicKey34.setMod(BytesUtil.hexString2ByteArray("A3767ABD1B6AA69D7F3FBF28C092DE9ED1E658BA5F0909AF7A1CCD907373B7210FDEB16287BA8E78E1529F443976FD27F991EC67D95E5F4E96B127CAB2396A94D6E45CDA44CA4C4867570D6B07542F8D4BF9FF97975DB9891515E66F525D2B3CBEB6D662BFB6C3F338E93B02142BFC44173A3764C56AADD202075B26DC2F9F7D7AE74BD7D00FD05EE430032663D27A57"));
        publicKey34.setExp(new byte[]{0x03});
        publicKeys.add(publicKey34);

        CAPublicKey publicKey35 = new CAPublicKey();
        publicKey35.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey35.setIndex((byte) 0x03);
        publicKey35.setMod(BytesUtil.hexString2ByteArray("B0627DEE87864F9C18C13B9A1F025448BF13C58380C91F4CEBA9F9BCB214FF8414E9B59D6ABA10F941C7331768F47B2127907D857FA39AAF8CE02045DD01619D689EE731C551159BE7EB2D51A372FF56B556E5CB2FDE36E23073A44CA215D6C26CA68847B388E39520E0026E62294B557D6470440CA0AEFC9438C923AEC9B2098D6D3A1AF5E8B1DE36F4B53040109D89B77CAFAF70C26C601ABDF59EEC0FDC8A99089140CD2E817E335175B03B7AA33D"));
        publicKey35.setExp(new byte[]{0x03});
        publicKeys.add(publicKey35);

        CAPublicKey publicKey36 = new CAPublicKey();
        publicKey36.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey36.setIndex((byte) 0x05);
        publicKey36.setMod(BytesUtil.hexString2ByteArray("97CF8BAD30CAE0F9A89285454DDDE967AAFBCD4BC0B78F29ECB1005286F15F6D7532A9C476607C73FF7424316DFC741894AA52EDBAF909719C7B53448343B45CF2F00A8ABFB78CEEBE848933AAED97DBE84F0730F34FB1AA1528D3D6EC75B73252A30D0C717518BE36458ADD0FBF854C65497F3F54084154B60F51561361EE8E85F742A54005524CB00FEBC334276E0E63DAD86C079A9A3DF5DD32BECADE1AB2B71F5F0A0E95A4000D01F1044A578AAD92E9FDE92E3C6AA3DCD4913DFA5552537E7DE75E241FAED455D76CB8FCAFEED3FD6DAB24D7A9C32852F866C751D7710F494A0DF11B67FAECDD87A9A4E2CC44F6F27E46E3C0CCCD0F"));
        publicKey36.setExp(new byte[]{0x03});
        publicKeys.add(publicKey36);

        CAPublicKey publicKey37 = new CAPublicKey();
        publicKey37.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey37.setIndex((byte) 0x08);
        publicKey37.setMod(BytesUtil.hexString2ByteArray("B61645EDFD5498FB246444037A0FA18C0F101EBD8EFA54573CE6E6A7FBF63ED21D66340852B0211CF5EEF6A1CD989F66AF21A8EB19DBD8DBC3706D135363A0D683D046304F5A836BC1BC632821AFE7A2F75DA3C50AC74C545A754562204137169663CFCC0B06E67E2109EBA41BC67FF20CC8AC80D7B6EE1A95465B3B2657533EA56D92D539E5064360EA4850FED2D1BF"));
        publicKey37.setExp(new byte[]{0x03});
        publicKeys.add(publicKey37);

        CAPublicKey publicKey38 = new CAPublicKey();
        publicKey38.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey38.setIndex((byte) 0x80);
        publicKey38.setMod(BytesUtil.hexString2ByteArray("CCDBA686E2EFB84CE2EA01209EEB53BEF21AB6D353274FF8391D7035D76E2156CAEDD07510E07DAFCACABB7CCB0950BA2F0A3CEC313C52EE6CD09EF00401A3D6CC5F68CA5FCD0AC6132141FAFD1CFA36A2692D02DDC27EDA4CD5BEA6FF21913B513CE78BF33E6877AA5B605BC69A534F3777CBED6376BA649C72516A7E16AF85"));
        publicKey38.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey38);

        CAPublicKey publicKey39 = new CAPublicKey();
        publicKey39.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey39.setIndex((byte) 0x61);
        publicKey39.setMod(BytesUtil.hexString2ByteArray("834D2A387C5A5F176EF3E66CAAF83F194B15AAD2470C78C77D6EB38EDAE3A2F9BA1623F6A58C892CC925632DFF48CE954B21A53E1F1E4366BE403C279B90027CBC72605DB6C79049B8992CB4912EFA270BECAB3A7CEFE05BFA46E4C7BBCF7C7A173BD988D989B32CB79FAC8E35FBE1860E7EA9F238A92A3593552D03D1E38601"));
        publicKey39.setExp(new byte[]{0x03});
        publicKey39.setExpDate(BytesUtil.hexString2ByteArray("20301231"));
        publicKeys.add(publicKey39);

        CAPublicKey publicKey40 = new CAPublicKey();
        publicKey40.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey40.setIndex((byte) 0x62);
        publicKey40.setMod(BytesUtil.hexString2ByteArray("B5CDD1E5368819FC3EA65B80C68117BBC29F9096EBD217269B583B0745E0C16433D54B8EF387B1E6CDDAED4923C39E370E5CADFE041773023A6BC0A033B0031B0048F18AC159773CB6695EE99F551F414883FB05E52640E893F4816082241D7BFA3640960003AD7517895C50E184AA956367B7BFFC6D8616A7B57E2D447AB3E1"));
        publicKey40.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKey40.setExpDate(BytesUtil.hexString2ByteArray("20301231"));
        publicKeys.add(publicKey40);

        CAPublicKey publicKey41 = new CAPublicKey();
        publicKey41.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey41.setIndex((byte) 0x63);
        publicKey41.setMod(BytesUtil.hexString2ByteArray("867ECA26A57472DEFB6CA94289312BA39C63052518DC480B6ED491ACC37C028846F4D7B79AFAEEFA07FB011DAA46C06021E932D501BF52F2834ADE3AC7689E94B248B28F3FE2803669DEDA000988DA1249F9A891558A05A1E5A7BD2C282FE18D204189A9994D4ADD86C0CE50952ED8BCEC0CE633679188285E51E1BED840FCBFC10953939AF49DB90048912E48B44181"));
        publicKey41.setExp(new byte[]{0x03});
        publicKey41.setExpDate(BytesUtil.hexString2ByteArray("20301231"));
        publicKeys.add(publicKey41);

        CAPublicKey publicKey42 = new CAPublicKey();
        publicKey42.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey42.setIndex((byte) 0x64);
        publicKey42.setMod(BytesUtil.hexString2ByteArray("91123ECF0230E3CB245C88DDFA3EE57BC58ED00B367B3875FCB79548872680F601E8C839AC0721BAB3B89ED21607281C8919BF726266EAB848502AD874B5107A4E654EF6D37773343F461435C86E4A8F866FB18C7CBA497B426290C38D196E2AFF33C0906F9296F297E156DC602A5E653CA1168F1109261114BF7BE8127A3E8007191830134299395CE2B322228667B76E072EB7FD5D0FB3A83E8AD1D7F6FD81"));
        publicKey42.setExp(new byte[]{0x03});
        publicKey42.setExpDate(BytesUtil.hexString2ByteArray("20301231"));
        publicKeys.add(publicKey42);

        CAPublicKey publicKey43 = new CAPublicKey();
        publicKey43.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey43.setIndex((byte) 0x65);
        publicKey43.setMod(BytesUtil.hexString2ByteArray("81BA1E6B9F671CFC848CA2ACD8E17AF406B4D329D1ECA5D01BC094A87C30AF49867944C632E8185074655FA535AD8CA42A83B41AAAEA859F432FA0B818E72DC07ED3F77FB318A475A261C0760A156E5DDC157AE8B79BA72D89D69FFF754619E928F1516A2A72C0F86B09B8EA25F86DC5A48EBC5A16F83FBA8FC4E3A98278912249F4E079BCBC06E7BED9AED397879D279ED91925394901260949BCCE6FA1169798A2715DAE32988BEFBE9621AE15E0C1"));
        publicKey43.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKey43.setExpDate(BytesUtil.hexString2ByteArray("20301231"));
        publicKeys.add(publicKey43);

        CAPublicKey publicKey44 = new CAPublicKey();
        publicKey44.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey44.setIndex((byte) 0x66);
        publicKey44.setMod(BytesUtil.hexString2ByteArray("7F5A3945794D6B15F5F26B4A21A63A5EF35540D8C8C099151F2279780A5C18A317703C98632E804D25576A7B460C05061E03975E50FBD7495B3ADC8E425E53DF76FA40B035E87F69ABF8765A52523F3B1A39B19528B002239015FADBA5921051"));
        publicKey44.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKey44.setExpDate(BytesUtil.hexString2ByteArray("20301231"));
        publicKeys.add(publicKey44);

        CAPublicKey publicKey45 = new CAPublicKey();
        publicKey45.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey45.setIndex((byte) 0x57);
        publicKey45.setMod(BytesUtil.hexString2ByteArray("E8105E77861FD2EB727C84E36D3D4A5666BD0ADCE8781F0145D3D82D72B92748E22D5404C6C41F3EC8B790DE2F61CF29FAECB168C79F5C8666762D53CC26A460"));
        publicKey45.setExpDate(BytesUtil.hexString2ByteArray("20201231"));
        publicKeys.add(publicKey45);

        CAPublicKey publicKey46 = new CAPublicKey();
        publicKey46.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey46.setIndex((byte) 0x58);
        publicKey46.setMod(BytesUtil.hexString2ByteArray("FFC2B1513320C275411DBADD2188203F7B62519F8C7BA98EF8AA9FD6D2E475984E383C3E12784B42B066960EEA0C8FC8099E14128055D67A666CCA5A058C26A4"));
        publicKey46.setExpDate(BytesUtil.hexString2ByteArray("20201231"));
        publicKeys.add(publicKey46);

        CAPublicKey publicKey47 = new CAPublicKey();
        publicKey47.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey47.setIndex((byte) 0x85);
        publicKey47.setMod(BytesUtil.hexString2ByteArray("C9242EC6030F10E5225E722AA17D9DC894299233AEC3219B950D4F243AF530FA13E3A31AFAA0D4BF4DE562B6B4C3108AEBBC6CB080F90770D532F241BC153641E1BF72F9DC1B08933B9BF77403F6A0FB5777BAA4C9BE91574BBBFB521342A20386790512221F477FBC53FF1B6533A015815435410EC272F0A34EA0735C43967D7E46FBA766EC00CED59B6715E3412D6FB8A934BF9D1497A24A6252C52D7586FD66A450FB5D2B4484EC923061439622BC0535316CD4231C13C627BF4D2EDE102C802464658F1B9D7FF23A3698510FA90D0C3164942FB359255CD823CB2635B3F167FBDFC900641B970D602A2771A7F4F94DF6D34BE8BBBB2669012D"));
        publicKey47.setExp(new byte[]{0x03});
        publicKey47.setExpDate(BytesUtil.hexString2ByteArray("20301231"));
        publicKeys.add(publicKey47);

        CAPublicKey publicKey48 = new CAPublicKey();
        publicKey48.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey48.setIndex((byte) 0x84);
        publicKey48.setMod(BytesUtil.hexString2ByteArray("F9EA5503CFE43038596C720645A94E0154793DE73AE5A935D1FB9D0FE77286B61261E3BB1D3DFEC547449992E2037C01FF4EFB88DA8A82F30FEA3198D5D1675247A1626E9CFFB4CD9E31399990E43FCA77C744A93685A260A20E6A607F3EE3FAE2ABBE99678C9F19DFD2D8EA76789239D13369D7D2D56AF3F2793068950B5B808C462571662D4364B30A2582959DB238333BADACB442F9516B5C336C8A613FE014B7D773581AE10FDF7BDB2669012D"));
        publicKey48.setExp(new byte[]{0x03});
        publicKey48.setExpDate(BytesUtil.hexString2ByteArray("20301231"));
        publicKeys.add(publicKey48);

        CAPublicKey publicKey49 = new CAPublicKey();
        publicKey49.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey49.setIndex((byte) 0x84);
        publicKey49.setMod(BytesUtil.hexString2ByteArray("C7CDB6F2A3FE80A8834CDDDD326E1082AA2288F47C464D57B34718193431711A44119148055044CFE3313708BED0C98E1C589B0F53CF6D7E829FCD906D21A90FD4CB6BAF13110C4685107C27E00981DB29DC0AC186E6D701577F23865626244E1F9B2CD1DDFCB9E899B41F5084D8CCC178A7C3F4546CF93187106FAB055A7AC67DF62E778CB88823BA58CF7546C2B09F"));
        publicKey49.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKey49.setExpDate(BytesUtil.hexString2ByteArray("20301231"));
        publicKeys.add(publicKey49);

        CAPublicKey publicKey50 = new CAPublicKey();
        publicKey50.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey50.setIndex((byte) 0xC1);
        publicKey50.setMod(BytesUtil.hexString2ByteArray("92F083CBE46F8DCC0C04E498BA9952BA9D4C09C80DD277E579F07E45772846FA43DD3AB31CC6B08DD18695715949FB108E53A071D393A7FDDBF9C5FB0B0507138797317480FC48D633ED38B401A451443AD7F15FACDA45A62ABE24FF6343ADD0909EA8389348E54E26F842880D1A69F9214368BA30C18DE5C5E0CB9253B5ABC55FB6EF0A738D927494A30BBF82E340285363B6FAA15673829DBB210E710DA58EE9E578E7CE55DC812AB7D6DCCE0E3B1AE179D664F3356EB951E3C91A1CBBF6A7CA8D0C7EC9C6AF7A4941C5051099B9784E56C9162067B8C3B15C5FA4480A645CD2526A69C80BA8EF361BE2AA9417DEFCE35B62B0C9CF097D"));
        publicKey50.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKey50.setExpDate(BytesUtil.hexString2ByteArray("20301231"));
        publicKeys.add(publicKey50);

        CAPublicKey publicKey51 = new CAPublicKey();
        publicKey51.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey51.setIndex((byte) 0x87);
        publicKey51.setMod(BytesUtil.hexString2ByteArray("942B7F2BA5EA307312B63DF77C5243618ACC2002BD7ECB74D821FE7BDC78BF28F49F74190AD9B23B9713B140FFEC1FB429D93F56BDC7ADE4AC075D75532C1E590B21874C7952F29B8C0F0C1CE3AEEDC8DA25343123E71DCF86C6998E15F756E3"));
        publicKey51.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey51);

        CAPublicKey publicKey52 = new CAPublicKey();
        publicKey52.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey52.setIndex((byte) 0x88);
        publicKey52.setMod(BytesUtil.hexString2ByteArray("99552C4A1ECD68A0260157FC4151B5992837445D3FC57365CA5692C87BE358CDCDF2C92FB6837522842A48EB11CDFFE2FD91770C7221E4AF6207C2DE4004C7DEE1B6276DC62D52A87D2CD01FBF2DC4065DB52824D2A2167A06D19E6A0F781071CDB2DD314CB94441D8DC0E936317B77BF06F5177F6C5ABA3A3BC6AA30209C97260B7A1AD3A192C9B8CD1D153570AFCC87C3CD681D13E997FE33B3963A0A1C79772ACF991033E1B8397AD0341500E48A24770BC4CBE19D2CCF419504FDBF0389BC2F2FDCD4D44E61F"));
        publicKey52.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey52);

        CAPublicKey publicKey53 = new CAPublicKey();
        publicKey53.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey53.setIndex((byte) 0x53);
        publicKey53.setMod(BytesUtil.hexString2ByteArray("BCD83721BE52CCCC4B6457321F22A7DC769F54EB8025913BE804D9EABBFA19B3D7C5D3CA658D768CAF57067EEC83C7E6E9F81D0586703ED9DDDADD20675D63424980B10EB364E81EB37DB40ED100344C928886FF4CCC37203EE6106D5B59D1AC102E2CD2D7AC17F4D96C398E5FD993ECB4FFDF79B17547FF9FA2AA8EEFD6CBDA124CBB17A0F8528146387135E226B005A474B9062FF264D2FF8EFA36814AA2950065B1B04C0A1AE9B2F69D4A4AA979D6CE95FEE9485ED0A03AEE9BD953E81CFD1EF6E814DFD3C2CE37AEFA38C1F9877371E91D6A5EB59FDEDF75D3325FA3CA66CDFBA0E57146CC789818FF06BE5FCC50ABD362AE4B80996D"));
        publicKey53.setExp(new byte[]{0x03});
        publicKeys.add(publicKey53);

        CAPublicKey publicKey54 = new CAPublicKey();
        publicKey54.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey54.setIndex((byte) 0x54);
        publicKey54.setMod(BytesUtil.hexString2ByteArray("C6DDC0B7645F7F16286AB7E4116655F56DD0C944766040DC68664DD973BD3BFD4C525BCBB95272B6B3AD9BA8860303AD08D9E8CC344A4070F4CFB9EEAF29C8A3460850C264CDA39BBE3A7E7D08A69C31B5C8DD9F94DDBC9265758C0E7399ADCF4362CAEE458D414C52B498274881B196DACCA7273F687F2A65FAEB809D4B2AC1D3D1EFB4F6490322318BD296D153B307A3283AB4E5BE6EBD910359A8565EB9C4360D24BAACA3DBFE393F3D6C830D603C6FC1E83409DFCD80D3A33BA243813BBB4CEAF9CBAB6B74B00116F72AB278A88A011D70071E06CAB140646438D986D48281624B85B3B2EBB9A6AB3BF2178FCC3011E7CAF24897AE7D"));
        publicKey54.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey54);

        CAPublicKey publicKey55 = new CAPublicKey();
        publicKey55.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey55.setIndex((byte) 0x96);
        publicKey55.setMod(BytesUtil.hexString2ByteArray("B74586D19A207BE6627C5B0AAFBC44A2ECF5A2942D3A26CE19C4FFAEEE920521868922E893E7838225A3947A2614796FB2C0628CE8C11E3825A56D3B1BBAEF783A5C6A81F36F8625395126FA983C5216D3166D48ACDE8A431212FF763A7F79D9EDB7FED76B485DE45BEB829A3D4730848A366D3324C3027032FF8D16A1E44D8D"));
        publicKey55.setExp(new byte[]{0x03});
        publicKeys.add(publicKey55);

        CAPublicKey publicKey56 = new CAPublicKey();
        publicKey56.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey56.setIndex((byte) 0x50);
        publicKey56.setMod(BytesUtil.hexString2ByteArray("D11197590057B84196C2F4D11A8F3C05408F422A35D702F90106EA5B019BB28AE607AA9CDEBCD0D81A38D48C7EBB0062D287369EC0C42124246AC30D80CD602AB7238D51084DED4698162C59D25EAC1E66255B4DB2352526EF0982C3B8AD3D1CCE85B01DB5788E75E09F44BE7361366DEF9D1E1317B05E5D0FF5290F88A0DB47"));
        publicKey56.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey56);

        CAPublicKey publicKey57 = new CAPublicKey();
        publicKey57.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey57.setIndex((byte) 0x51);
        publicKey57.setMod(BytesUtil.hexString2ByteArray("DB5FA29D1FDA8C1634B04DCCFF148ABEE63C772035C79851D3512107586E02A917F7C7E885E7C4A7D529710A145334CE67DC412CB1597B77AA2543B98D19CF2CB80C522BDBEA0F1B113FA2C86216C8C610A2D58F29CF3355CEB1BD3EF410D1EDD1F7AE0F16897979DE28C6EF293E0A19282BD1D793F1331523FC71A228800468C01A3653D14C6B4851A5C029478E757F"));
        publicKey57.setExp(new byte[]{0x03});
        publicKey57.setHash(BytesUtil.hexString2ByteArray("B9D248075A3F23B522FE45573E04374DC4995D71"));
        publicKeys.add(publicKey57);

        CAPublicKey publicKey58 = new CAPublicKey();
        publicKey58.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey58.setIndex((byte) 0xE1);
        publicKey58.setMod(BytesUtil.hexString2ByteArray("99C5B70AA61B4F4C51B6F90B0E3BFB7A3EE0E7DB41BC466888B3EC8E9977C762407EF1D79E0AFB2823100A020C3E8020593DB50E90DBEAC18B78D13F96BB2F57EEDDC30F256592417CDF739CA6804A10A29D2806E774BFA751F22CF3B65B38F37F91B4DAF8AEC9B803F7610E06AC9E6B"));
        publicKey58.setExp(new byte[]{0x03});
        publicKeys.add(publicKey58);

        CAPublicKey publicKey59 = new CAPublicKey();
        publicKey59.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey59.setIndex((byte) 0xE2);
        publicKey59.setMod(BytesUtil.hexString2ByteArray("BD232E348B118EB3F6446EF4DA6C3BAC9B2AE510C5AD107D38343255D21C4BDF4952A42E92C633B1CE4BFEC39AFB6DFE147ECBB91D681DAC15FB0E198E9A7E4636BDCA107BCDA3384FCB28B06AFEF90F099E7084511F3CC010D4343503E1E5A67264B4367DAA9A3949499272E9B5022F"));
        publicKey59.setExp(new byte[]{0x03});
        publicKeys.add(publicKey59);

        CAPublicKey publicKey60 = new CAPublicKey();
        publicKey60.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey60.setIndex((byte) 0xE3);
        publicKey60.setMod(BytesUtil.hexString2ByteArray("BC01E12223E1A41E88BFFA801093C5F8CEC5CD05DBBDBB787CE87249E8808327C2D218991F97A1131E8A25B0122ED11E709C533E8886A1259ADDFDCBB396604D24E505A2D0B5DD0384FB0002A7A1EB39BC8A11339C7A9433A948337761BE73BC497B8E58736DA4636538AD282D3CD3DB"));
        publicKey60.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey60);

        CAPublicKey publicKey61 = new CAPublicKey();
        publicKey61.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey61.setIndex((byte) 0xE4);
        publicKey61.setMod(BytesUtil.hexString2ByteArray("CBF2E40F0836C9A5E390A37BE3B809BDF5D740CB1DA38CFC05D5F8D6B7745B5E9A3FA6961E55FF20412108525E66B970F902F7FF4305DD832CD0763E3AA8B8173F84777100B1047BD1D744509312A0932ED25FED52A959430768CCD902FD8C8AD9123E6ADDB3F34B92E7924D729CB6473533AE2B2B55BF0E44964FDEA8440117"));
        publicKey61.setExp(new byte[]{0x03});
        publicKeys.add(publicKey61);

        CAPublicKey publicKey62 = new CAPublicKey();
        publicKey62.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey62.setIndex((byte) 0xE5);
        publicKey62.setMod(BytesUtil.hexString2ByteArray("D4FDAE94DEDBECC6D20D38B01E91826DC6954338379917B2BB8A6B36B5D3B0C5EDA60B337448BAFFEBCC3ABDBA869E8DADEC6C870110C42F5AAB90A18F4F867F72E3386FFC7E67E7FF94EBA079E531B3CF329517E81C5DD9B3DC65DB5F9043190BE0BE897E5FE48ADF5D3BFA0585E076E554F26EC69814797F15669F4A255C13"));
        publicKey62.setExp(new byte[]{0x03});
        publicKeys.add(publicKey62);

        CAPublicKey publicKey63 = new CAPublicKey();
        publicKey63.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey63.setIndex((byte) 0xE6);
        publicKey63.setMod(BytesUtil.hexString2ByteArray("EBF9FAECC3E5C315709694664775D3FBDA5A504D89344DD920C55696E891D9AB622598A9D6AB8FBF35E4599CAB7EB22F956992F8AB2E6535DECB6B576FA0675F97C23DD4C374A66E6AF419C9D204D0B9F93C08D789D63805660FBB629DF1B488CFA1D7A13E9B729437EEAFE718EFA859348BA0D76812A99F31CD364F2A4FD42F"));
        publicKey63.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey63);

        CAPublicKey publicKey64 = new CAPublicKey();
        publicKey64.setRid(BytesUtil.hexString2ByteArray("A000000152"));
        publicKey64.setIndex((byte) 0xD0);
        publicKey64.setMod(BytesUtil.hexString2ByteArray("D05C2A09D09C9031366EC092BCAC67D4B1B4F88B10005E1FC45C1B483AE7EB86FF0E884A19C0595A6C34F06386D776A21D620FC9F9C498ADCA00E66D129BCDD4789837B96DCC7F09DA94CCAC5AC7CFC07F4600DF78E493DC1957DEBA3F4838A4B8BD4CEFE4E4C6119085E5BB21077341C568A21D65D049D666807C39C401CDFEE7F7F99B8F9CB34A8841EA62E83E8D63"));
        publicKey64.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKey64.setExpDate(BytesUtil.hexString2ByteArray("20301231"));
        publicKeys.add(publicKey64);

        CAPublicKey publicKey65 = new CAPublicKey();
        publicKey65.setRid(BytesUtil.hexString2ByteArray("A000000152"));
        publicKey65.setIndex((byte) 0xD1);
        publicKey65.setMod(BytesUtil.hexString2ByteArray("A71AF977C1079304D6DFF3F665AB6DB3FBDFA1B170287AC6D7BC0AFCB7A202A4C815E1FC2E34F75A052564EE2148A39CD6B0F39CFAEF95F0294A86C3198E349FF82EECE633D50E5860A15082B4B342A90928024057DD51A2401D781B67AE7598D5D1FF26A441970A19A3A58011CA19284279A85567D3119264806CAF761122A71FC0492AC8D8D42B036C394FC494E03B43600D7E02CB5267755ACE64437CFA7B475AD40DDC93B8C9BCAD63801FC492FD251640E41FD13F6E231F56F97283447AB44CBE11910DB3C75243784AA9BDF57539C31B51C9F35BF8BC2495762881255478264B792BBDCA6498777AE9120ED935BB3E8BEA3EAB13D9"));
        publicKey65.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKey65.setExpDate(BytesUtil.hexString2ByteArray("20301231"));
        publicKeys.add(publicKey65);


        CAPublicKey publicKey66 = new CAPublicKey();
        publicKey66.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey66.setIndex((byte) 0xF8);
        publicKey66.setMod(BytesUtil.hexString2ByteArray("900000000000000000000000001200000000000000000000000000000000480000000000000000000000000000000010800000000000000000000000019800000000000000000000000000000006600000000000000000000000000000000055"));
        publicKey66.setExp(new byte[]{0x03});
        publicKeys.add(publicKey66);


        CAPublicKey publicKey366 = new CAPublicKey();
        publicKey366.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey366.setIndex((byte) 0x00);
        publicKey366.setMod(BytesUtil.hexString2ByteArray("9C6BE5ADB10B4BE3DCE2099B4B210672B89656EBA091204F613ECC623BEDC9C6D77B660E8BAEEA7F7CE30F1B153879A4E36459343D1FE47ACDBD41FCD710030C2BA1D9461597982C6E1BDD08554B726F5EFF7913CE59E79E357295C321E26D0B8BE270A9442345C753E2AA2ACFC9D30850602FE6CAC00C6DDF6B8D9D9B4879B2826B042A07F0E5AE526A3D3C4D22C72B9EAA52EED8893866F866387AC05A1399"));
        publicKey366.setExp(new byte[]{0x03});
        publicKeys.add(publicKey366);

        CAPublicKey publicKey367 = new CAPublicKey();
        publicKey367.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey367.setIndex((byte) 0x01);
        publicKey367.setMod(BytesUtil.hexString2ByteArray("C696034213D7D8546984579D1D0F0EA519CFF8DEFFC429354CF3A871A6F7183F1228DA5C7470C055387100CB935A712C4E2864DF5D64BA93FE7E63E71F25B1E5F5298575EBE1C63AA617706917911DC2A75AC28B251C7EF40F2365912490B939BCA2124A30A28F54402C34AECA331AB67E1E79B285DD5771B5D9FF79EA630B75"));
        publicKey367.setExp(new byte[]{0x03});
        publicKeys.add(publicKey367);

        CAPublicKey publicKey368 = new CAPublicKey();
        publicKey368.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey368.setIndex((byte) 0x02);
        publicKey368.setMod(BytesUtil.hexString2ByteArray("A99A6D3E071889ED9E3A0C391C69B0B804FC160B2B4BDD570C92DD5A0F45F53E8621F7C96C40224266735E1EE1B3C06238AE35046320FD8E81F8CEB3F8B4C97B940930A3AC5E790086DAD41A6A4F5117BA1CE2438A51AC053EB002AED866D2C458FD73359021A12029A0C043045C11664FE0219EC63C10BF2155BB2784609A106421D45163799738C1C30909BB6C6FE52BBB76397B9740CE064A613FF8411185F08842A423EAD20EDFFBFF1CD6C3FE0C9821479199C26D8572CC8AFFF087A9C3"));
        publicKey368.setExp(new byte[]{0x03});
        publicKeys.add(publicKey368);



        CAPublicKey publicKey369 = new CAPublicKey();
        publicKey369.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey369.setIndex((byte) 0x03);
        publicKey369.setMod(BytesUtil.hexString2ByteArray("C2490747FE17EB0584C88D47B1602704150ADC88C5B998BD59CE043EDEBF0FFEE3093AC7956AD3B6AD4554C6DE19A178D6DA295BE15D5220645E3C8131666FA4BE5B84FE131EA44B039307638B9E74A8C42564F892A64DF1CB15712B736E3374F1BBB6819371602D8970E97B900793C7C2A89A4A1649A59BE680574DD0B60145"));
        publicKey369.setExp(new byte[]{0x03});
        publicKeys.add(publicKey369);



        CAPublicKey publicKey370 = new CAPublicKey();
        publicKey370.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey370.setIndex((byte) 0x04);
        publicKey370.setMod(BytesUtil.hexString2ByteArray("A6DA428387A502D7DDFB7A74D3F412BE762627197B25435B7A81716A700157DDD06F7CC99D6CA28C2470527E2C03616B9C59217357C2674F583B3BA5C7DCF2838692D023E3562420B4615C439CA97C44DC9A249CFCE7B3BFB22F68228C3AF13329AA4A613CF8DD853502373D62E49AB256D2BC17120E54AEDCED6D96A4287ACC5C04677D4A5A320DB8BEE2F775E5FEC5"));
        publicKey370.setExp(new byte[]{0x03});
        publicKeys.add(publicKey370);

        CAPublicKey publicKey371 = new CAPublicKey();
        publicKey371.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey371.setIndex((byte) 0x05);
        publicKey371.setMod(BytesUtil.hexString2ByteArray("A1F5E1C9BD8650BD43AB6EE56B891EF7459C0A24FA84F9127D1A6C79D4930F6DB1852E2510F18B61CD354DB83A356BD190B88AB8DF04284D02A4204A7B6CB7C5551977A9B36379CA3DE1A08E69F301C95CC1C20506959275F41723DD5D2925290579E5A95B0DF6323FC8E9273D6F849198C4996209166D9BFC973C361CC826E1"));
        publicKey371.setExp(new byte[]{0x03});
        publicKeys.add(publicKey371);

        // --//


        CAPublicKey publicKey372 = new CAPublicKey();
        publicKey372.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey372.setIndex((byte) 0x06);
        publicKey372.setMod(BytesUtil.hexString2ByteArray("D24C24D2D7FB5509D5B26EBD4077CE74516A2B89E4062D83DC1F7E27D5E5AA6657F376DABDDB6B4251F323426E621F5DFC1DFA07C06035908B7EDF674CBEB598F59F9CCB5C55410521C1595E7BD86AD71C42C328FCD9D82C9DD68DF1E6D3F189C32F578B7E3487E84D642ED2DA3F689AA188C2A1F37E1395732E1872954FFEB19D5C404515E7C3F637E4B9E0F889887C0C43194942B3A92D43B0AB091C5510FB3C24A1264764CBEEBAFEC0AACCA6F948FC973C8950DF934140B7DF87E77193B954193EB3B75E60BBB817C4FEEAA542CE388782885B8460C4C9442937ECFDB808FD8B8979E5368EB859C9068D3D0EA91678D63BC02C87B89DB3EBE6CF1D8F6BE6"));
        publicKey372.setExp(new byte[]{0x03});
        //publicKeys.add(publicKey372);

        CAPublicKey publicKey373 = new CAPublicKey();
        publicKey373.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey373.setIndex((byte) 0x09);
        publicKey373.setMod(BytesUtil.hexString2ByteArray("967B6264436C96AA9305776A5919C70DA796340F9997A6C6EF7BEF1D4DBF9CB4289FB7990ABFF1F3AE692F12844B2452A50AE075FB327976A40E8028F279B1E3CCB623957D696FC1225CA2EC950E2D415E9AA931FF18B13168D661FBD06F0ABB"));
        publicKey373.setExp(new byte[]{0x03});
        publicKeys.add(publicKey373);


        CAPublicKey publicKey374 = new CAPublicKey();
        publicKey374.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey374.setIndex((byte) 0x22);
        publicKey374.setMod(BytesUtil.hexString2ByteArray("BBE43877CC28C0CE1E14BC14E8477317E218364531D155BB8AC5B63C0D6E284DD24259193899F9C04C30BAF167D57929451F67AEBD3BBD0D41444501847D8F02F2C2A2D14817D97AE2625DC163BF8B484C40FFB51749CEDDE9434FB2A0A41099"));
        publicKey374.setExp(new byte[]{0x03});
        publicKeys.add(publicKey374);



        CAPublicKey publicKey375 = new CAPublicKey();
        publicKey375.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey375.setIndex((byte) 0x52);
        publicKey375.setMod(BytesUtil.hexString2ByteArray("B831414E0B4613922BD35B4B36802BC1E1E81C95A27C958F5382003DF646154CA92FC1CE02C3BE047A45E9B02A9089B4B90278237C965192A0FCC86BB49BC82AE6FDC2DE709006B86C7676EFDF597626FAD633A4F7DC48C445D37EB55FCB3B1ABB95BAAA826D5390E15FD14ED403FA2D0CB841C650609524EC555E3BC56CA957"));
        publicKey375.setExp(new byte[]{0x03});
        publicKeys.add(publicKey375);



        CAPublicKey publicKey376 = new CAPublicKey();
        publicKey376.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey376.setIndex((byte) 0xEF);
        publicKey376.setMod(BytesUtil.hexString2ByteArray("A191CB87473F29349B5D60A88B3EAEE0973AA6F1A082F358D849FDDFF9C091F899EDA9792CAF09EF28F5D22404B88A2293EEBBC1949C43BEA4D60CFD879A1539544E09E0F09F60F065B2BF2A13ECC705F3D468B9D33AE77AD9D3F19CA40F23DCF5EB7C04DC8F69EBA565B1EBCB4686CD274785530FF6F6E9EE43AA43FDB02CE00DAEC15C7B8FD6A9B394BABA419D3F6DC85E16569BE8E76989688EFEA2DF22FF7D35C043338DEAA982A02B866DE5328519EBBCD6F03CDD686673847F84DB651AB86C28CF1462562C577B853564A290C8556D818531268D25CC98A4CC6A0BDFFFDA2DCCA3A94C998559E307FDDF915006D9A987B07DDAEB3B"));
        publicKey376.setExp(new byte[]{0x03});
        publicKeys.add(publicKey376);


        CAPublicKey publicKey377 = new CAPublicKey();
        publicKey377.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey377.setIndex((byte) 0xF0);
        publicKey377.setMod(BytesUtil.hexString2ByteArray("7563C51B5276AA6370AB8405522414645832B6BEF2A989C771475B2E8DC654DC8A5BFF9E28E31FF1A370A40DC3FFEB06BC85487D5F1CB61C2441FD71CBCD05D883F8DE413B243AFC9DCA768B061E35B884B5D21B6B016AA36BA12DABCFE49F8E528C893C34C7D4793977E4CC99AB09640D9C7AAB7EC5FF3F40E3D4D18DF7E3A7"));
        publicKey377.setExp(new byte[]{0x03});
        publicKeys.add(publicKey377);


        CAPublicKey publicKey378 = new CAPublicKey();
        publicKey378.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey378.setIndex((byte) 0xF1);
        publicKey378.setMod(BytesUtil.hexString2ByteArray("A0DCF4BDE19C3546B4B6F0414D174DDE294AABBB828C5A834D73AAE27C99B0B053A90278007239B6459FF0BBCD7B4B9C6C50AC02CE91368DA1BD21AAEADBC65347337D89B68F5C99A09D05BE02DD1F8C5BA20E2F13FB2A27C41D3F85CAD5CF6668E75851EC66EDBF98851FD4E42C44C1D59F5984703B27D5B9F21B8FA0D93279FBBF69E090642909C9EA27F898959541AA6757F5F624104F6E1D3A9532F2A6E51515AEAD1B43B3D7835088A2FAFA7BE7"));
      //  publicKey378.setHash(BytesUtil.hexString2ByteArray("D8E68DA167AB5A85D8C3D55ECB9B0517A1A5B4BB"));
      //  publicKey378.setHashFlag((byte) 0X01);
        publicKey378.setExp(new byte[]{0x03});
        publicKeys.add(publicKey378);




        CAPublicKey publicKey379 = new CAPublicKey();
        publicKey379.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey379.setIndex((byte) 0xF4);
        publicKey379.setMod(BytesUtil.hexString2ByteArray("9E2F74BF4AB521019735BFC7E4CBC56B6F64AFF1ED7B79998EE5B3DFFE23DFC8E2DD0025575AF94DE814264528AF6F8005A538B3D6AE881B350F89595588E51F7423E711109DEC169FDD560602D80EF46E582C8C546C8930394BD534412A88CC9FF4DFC08AE716A595EF1AF7C32EDFCF996433EB3C36BCE093E44E0BDE228E0299A0E358BF28308DB4739815DD09F1E89654CC7CC193E2AC17C4DA335D904B8EC06ACFBDE083F76933C969672E9AFEA3"));
        publicKey379.setExp(new byte[]{0x03});
        publicKeys.add(publicKey379);


        CAPublicKey publicKey380 = new CAPublicKey();
        publicKey380.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey380.setIndex((byte) 0xF5);
        publicKey380.setMod(BytesUtil.hexString2ByteArray("A6E6FB72179506F860CCCA8C27F99CECD94C7D4F3191D303BBEE37481C7AA15F233BA755E9E4376345A9A67E7994BDC1C680BB3522D8C93EB0CCC91AD31AD450DA30D337662D19AC03E2B4EF5F6EC18282D491E19767D7B24542DFDEFF6F62185503532069BBB369E3BB9FB19AC6F1C30B97D249EEE764E0BAC97F25C873D973953E5153A42064BBFABFD06A4BB486860BF6637406C9FC36813A4A75F75C31CCA9F69F8DE59ADECEF6BDE7E07800FCBE035D3176AF8473E23E9AA3DFEE221196D1148302677C720CFE2544A03DB553E7F1B8427BA1CC72B0F29B12DFEF4C081D076D353E71880AADFF386352AF0AB7B28ED49E1E672D11F9"));
        publicKey380.setExp(new byte[]{0x03});
        publicKeys.add(publicKey380);


        CAPublicKey publicKey381 = new CAPublicKey();
        publicKey381.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey381.setIndex((byte) 0xF6);
        publicKey381.setMod(BytesUtil.hexString2ByteArray("A25A6BD783A5EF6B8FB6F83055C260F5F99EA16678F3B9053E0F6498E82C3F5D1E8C38F13588017E2B12B3D8FF6F50167F46442910729E9E4D1B3739E5067C0AC7A1F4487E35F675BC16E233315165CB142BFDB25E301A632A54A3371EBAB6572DEEBAF370F337F057EE73B4AE46D1A8BC4DA853EC3CC12C8CBC2DA18322D68530C70B22BDAC351DD36068AE321E11ABF264F4D3569BB71214545005558DE26083C735DB776368172FE8C2F5C85E8B5B890CC682911D2DE71FA626B8817FCCC08922B703869F3BAEAC1459D77CD85376BC36182F4238314D6C4212FBDD7F23D3"));
        publicKey381.setExp(new byte[]{0x03});
        publicKeys.add(publicKey381);


        CAPublicKey publicKey382 = new CAPublicKey();
        publicKey382.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey382.setIndex((byte) 0xF7);
        publicKey382.setMod(BytesUtil.hexString2ByteArray("94EA62F6D58320E354C022ADDCF0559D8CF206CD92E869564905CE21D720F971B7AEA374830EBE1757115A85E088D41C6B77CF5EC821F30B1D890417BF2FA31E5908DED5FA677F8C7B184AD09028FDDE96B6A6109850AA800175EABCDBBB684A96C2EB6379DFEA08D32FE2331FE103233AD58DCDB1E6E077CB9F24EAEC5C25AF"));

        publicKey382.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey382);


        CAPublicKey publicKey383 = new CAPublicKey();
        publicKey383.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey383.setIndex((byte) 0xF8);
        publicKey383.setMod(BytesUtil.hexString2ByteArray("A1F5E1C9BD8650BD43AB6EE56B891EF7459C0A24FA84F9127D1A6C79D4930F6DB1852E2510F18B61CD354DB83A356BD190B88AB8DF04284D02A4204A7B6CB7C5551977A9B36379CA3DE1A08E69F301C95CC1C20506959275F41723DD5D2925290579E5A95B0DF6323FC8E9273D6F849198C4996209166D9BFC973C361CC826E1"));
        publicKey383.setExp(new byte[]{0x03});
        publicKeys.add(publicKey383);

        //---

        CAPublicKey publicKey9 = new CAPublicKey();
        publicKey9.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey9.setIndex((byte) 0xFE);
        publicKey9.setMod(BytesUtil.hexString2ByteArray("E76317965175A08BEE510F58830E87B262C70D529803245FA8B88E0C753562DE7AEB5A9E3E6C1A98E94D8DB7C31407DAC5D071E06B80B09E146F22DB85F1D72D1EA18D22600032C6DD40E3714D5ADA7DE9D7D01E88391F893156D6F4BF13E9063559DA0786DE9BDE6B1C9B0BB968EDDE07145ABF877B931682CCB1FB800728724D04AF241E2827E0FA1F62591914FF25"));
        publicKey9.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey9);

        CAPublicKey publicKey10 = new CAPublicKey();
        publicKey10.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey10.setIndex((byte) 0xFC);
        publicKey10.setMod(BytesUtil.hexString2ByteArray("B3296C91F4795BD97112606903407B6EFF3AB39246E91095E51D17867DA4ADE59A48BE2FE9B52710283D3D32260E2C7D247214C57D46AA6465E47E0A4B3FFAAD8A7F6A190755BCCFE3F3FB3989A9F6B1C9E1845BCCCAD6F20B1DAC6033600234E81DAC4153212B0F760C23099192AA6C4C9083BEFFD9A79D2A27B08FECC8E5D437D6C68550A839B1294151DABA9D9CB2F160F60F749289F500C8C7F334BD20EBAC4AB109CF3C182F1B781C7C097A7903530746C449B99E39E4DB6493DD2A02E37C62AE8BC9A7470ECCCF8DC06A18C33CD24B30D56F25D2755CE82AA4DE4D2EAEC07750A03DB75EBD0D8EBC9F2A1D85A0D252EFF40329BE05"));
        publicKey10.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey10);

        CAPublicKey publicKey11 = new CAPublicKey();
        publicKey11.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey11.setIndex((byte) 0xFD);
        publicKey11.setMod(BytesUtil.hexString2ByteArray("C9485DBEB5E40415D1B397524F47685F306CFDC499D4E2E7D0CBAF222CFA8184BD111DAEEDC9CC6EC8540C3F7271EA9990119CC5C43180501D9F45252D6835053FAE35696AE8CD67A325647449CF5E594DA8F627209F7F03AE8D6DFC0DB3E79E28E415DF29A5B57D6814856CC30A96DA5B8890363E507FCB2E283DA1EBB5F18E8E24102B7D0192BB8E35A4F7CD05A435"));
        publicKey11.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey11);

        CAPublicKey publicKey12 = new CAPublicKey();
        publicKey12.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey12.setIndex((byte) 0xFB);
        publicKey12.setMod(BytesUtil.hexString2ByteArray("9B170603A489C7546C45DA57B8FFD1DB2061240F0E8C6D1F9ABDC6B265AA8911915C1A4EABD8D0ED4755D1B902BA06FE5A645B786CD241295517D44EF1A7C25D75AFE0EB28066E4D69FEE7ABAFDD5EEB230F14E402C9840825FA77EAD12B5F1C5494701DE1897F65FE6BF106D47545EBF70CE7C158068C61F0773534DB742AB83C28038C1494F15905D0AD17CF1BD38D"));
        publicKey12.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey12);

        CAPublicKey publicKey13 = new CAPublicKey();
        publicKey13.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey13.setIndex((byte) 0xFA);
        publicKey13.setMod(BytesUtil.hexString2ByteArray("A90FCD55AA2D5D9963E35ED0F440177699832F49C6BAB15CDAE5794BE93F934D4462D5D12762E48C38BA83D8445DEAA74195A301A102B2F114EADA0D180EE5E7A5C73E0C4E11F67A43DDAB5D55683B1474CC0627F44B8D3088A492FFAADAD4F42422D0E7013536C3C49AD3D0FAE96459B0F6B1B6056538A3D6D44640F94467B108867DEC40FAAECD740C00E2B7A8852D"));
        publicKey13.setExp(new byte[]{0x03});
        publicKeys.add(publicKey13);

        CAPublicKey publicKey14 = new CAPublicKey();
        publicKey14.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey14.setIndex((byte) 0xFF);
        publicKey14.setMod(BytesUtil.hexString2ByteArray("F69DBB5E15983EAE3CCF31CF4E47098C2FC16F97A0C710F84777EFA99622D86502B138728AB12E3481A84D20E014AD2D634D2836F27F294924B895A87F91F81B8169D4DFDAD8D7CBD741804CD61B467C7A9ACFECEB71188CAA73A907547699D45C9C7D2098AC2966266417F665A46BDD012C097DBD33D1D11AFF6EC8A9C0AD814A65B48262CA011636079A328C1AAEB7"));
        publicKey14.setExp(new byte[]{0x01, 0x00, 0x01});
        publicKeys.add(publicKey14);

        CAPublicKey publicKey15 = new CAPublicKey();
        publicKey15.setRid(BytesUtil.hexString2ByteArray("A000000004"));
        publicKey15.setIndex((byte) 0xF3);
        publicKey15.setMod(BytesUtil.hexString2ByteArray("98F0C770F23864C2E766DF02D1E833DFF4FFE92D696E1642F0A88C5694C6479D16DB1537BFE29E4FDC6E6E8AFD1B0EB7EA0124723C333179BF19E93F10658B2F776E829E87DAEDA9C94A8B3382199A350C077977C97AFF08FD11310AC950A72C3CA5002EF513FCCC286E646E3C5387535D509514B3B326E1234F9CB48C36DDD44B416D23654034A66F403BA511C5EFA3"));
        publicKey15.setExp(new byte[]{0x03});
        publicKeys.add(publicKey15);




        CAPublicKey publicKey67 = new CAPublicKey();
        publicKey67.setRid(BytesUtil.hexString2ByteArray("A000000333"));
        publicKey67.setIndex((byte) 0xFE);
        publicKey67.setMod(BytesUtil.hexString2ByteArray("C469BF4F82F1FA41A6287A592750DB700B0C6CE26C83397E45A2E476CFD3DD666C6D70A8471D9BED927D43852489D6ACEE88B279F1E3C936CD80423D52509F2BB421F37A42E542F282718315CC8B63DF172B43267336029EECDD245C6119A0FEFB48F218BAAA84AA5B94CC73B9515312080510480F08EE20DCC00A73FAB745332DEFEEB11FB3B9AE0A6B3BAB59B73AD5"));
        publicKey67.setExp(new byte[]{0x03});
        publicKey67.setExpDate(BytesUtil.hexString2ByteArray("20301231"));
        publicKeys.add(publicKey67);

        return publicKeys;
    }

    public static void updateEMVAIDFromOutside() {
        aids = null;

        baseParameters = null;

        masterParameters = null;

    }





}
