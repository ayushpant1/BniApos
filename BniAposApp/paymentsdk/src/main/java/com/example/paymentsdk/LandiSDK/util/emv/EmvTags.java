package com.example.paymentsdk.LandiSDK.util.emv;

/**
 * EMV tags.
 */

public class EmvTags {

    /************************************************************************/
    /*  Card ICC Data Labels                                                */
    /************************************************************************/
    public final static String EMV_TAG_IC_AC = "9F26";     // 0 - 应用密文(Application Cryptogram)
    public final static String EMV_TAG_IC_APPCURCODE = "9F42";     // 1 - 应用货币代码(Application Currency Code)
    public final static String EMV_TAG_IC_APPCUREXP = "9F44";     // 2 - 应用货币指数(Application Currency Exponent)
    public final static String EMV_TAG_IC_APPDISCDATA = "9F05";     // 3 - 应用自定义数据(Application Discretionary Data)
    public final static String EMV_TAG_IC_APPEFFECTDATE = "5F25";     // 4 - 应用生效日期(Application Effective Date)
    public final static String EMV_TAG_IC_APPEXPIREDATE = "5F24";     // 5 - 应用失效日期(Application Expiration Date)
    public final static String EMV_TAG_IC_AFL = "9400";     // 6 - 应用文件定位器AFL(Application File Locator)
    public final static String EMV_TAG_IC_AID = "4F00";     // 7 - 应用标识符AID(Application Identifier)
    public final static String EMV_TAG_IC_AIP = "82";     // 8 - 应用交互特征AIP(Application Interchange Profile)
    public final static String EMV_TAG_IC_APPLABEL = "50";     // 9 - 应用标签(Application Label)
    public final static String EMV_TAG_IC_APNAME = "9F12";     // 10 - 应用首选名称(Application Preferred Name)
    public final static String EMV_TAG_IC_PAN = "5A00";     // 11 - 应用主帐号PAN(Application Primary Account Number)
    public final static String EMV_TAG_IC_PANSN = "5F34";     // 12 - 应用主帐号序号PAN_Sn(Application Primary Account Number Sequence Number)
    public final static String EMV_TAG_IC_APID = "8700";     // 13 - 应用优先标识符(Application Priority Indicator)
    public final static String EMV_TAG_IC_APCUR = "9F3B";     // 14 - 应用首选货币(Application Preference Currency)
    public final static String EMV_TAG_IC_APCUREXP = "9F43";     // 15 - 应用首选货币指数(Application Preferece Currency Exponent)
    public final static String EMV_TAG_IC_APPTEMP = "6100";     // 16 - 应用模板(Application Template)
    public final static String EMV_TAG_IC_ATC = "9F36";     // 17 - 应用交易计数器ATC(Application Transaction Counter)
    public final static String EMV_TAG_IC_AUC = "9F07";     // 18 - 应用用途控制AUC(Application Usage Control)
    public final static String EMV_TAG_IC_APPVERNO = "9F08";     // 19 - 应用版本号(Application Version Number)
    public final static String EMV_TAG_IC_CDOL1 = "8C00";     // 20 - 卡片风险管理数据对象列表1CDOL1(Card Risk Management Data Object List 1)
    public final static String EMV_TAG_IC_CDOL2 = "8D00";     // 21 - 卡片风险管理数据对象列表2CDOL2(Card Risk Management Data Object List 2)
    public final static String EMV_TAG_IC_CHNAME = "5F20";     // 22 - 持卡人姓名(Cardholder Name)
    public final static String EMV_TAG_IC_CHNAMEEX = "9F0B";     // 23 - 持卡人扩展姓名(Cardholder Name Extended)
    public final static String EMV_TAG_IC_CVMLIST = "8E00";     // 24 - 持卡人验证方法列表(Cardholder Verification Method List)
    public final static String EMV_TAG_IC_CAPKINDEX = "8F00";     // 25 - 认证中心公钥索引(Certification Authority Public Key Index)
    public final static String EMV_TAG_IC_CID = "9F27";     // 26 - 密文信息数据CID(Cryptogram Infomation Data)
    public final static String EMV_TAG_IC_DTAUTHCODE = "9F45";     // 27 - 数据认证码(Data Authentication Code)
    public final static String EMV_TAG_IC_DFNAME = "84";     // 28 - DF名称(Dedicated File Name)
    public final static String EMV_TAG_IC_DDFNAME = "9D00";     // 29 - 目录定义文件DDF名称(Directory Definition File)
    public final static String EMV_TAG_IC_DIRDISCTEMP = "7300";     // 30 - 目录自定义模板(Directory Discretionary Template)
    public final static String EMV_TAG_IC_DDOL = "9F49";     // 31 - 动态数据认证数据对象列表DDOL(Dynamic Data Authentication Data Object List)
    public final static String EMV_TAG_IC_FCIDISCDATA = "BF0C";     // 32 - FCI发卡行自定义数据(File Control Information Issuer Discretionary Data)
    public final static String EMV_TAG_IC_FCIPROPTEMP = "A500";     // 33 - FCI专用模板(File Control Information Proprietary Template)
    public final static String EMV_TAG_IC_FCITEMP = "6F00";     // 34 - FCI模板(File Control Information Template)
    public final static String EMV_TAG_IC_ICCDYNNUM = "9F4C";     // 35 - IC卡动态数(ICC Dynamic Number)
    public final static String EMV_TAG_IC_PECERT = "9F2D";     // 36 - IC卡PIN加密公钥证书(ICC PIN Encipherment Public Key Certificate)
    public final static String EMV_TAG_IC_PEEXP = "9F2E";     // 37 - IC卡PIN加密公钥指数(ICC PIN Encipherment Public Key Exponent)
    public final static String EMV_TAG_IC_PERMD = "9F2F";     // 38 - IC卡PIN加密公钥余项(ICC PIN Encipherment Public Key Remainder)
    public final static String EMV_TAG_IC_ICCPKCERT = "9F46";     // 39 - IC卡公钥证书(ICC Public Key Certificate)
    public final static String EMV_TAG_IC_ICCPKEXP = "9F47";     // 40 - IC卡公钥指数(ICC Public Key Exponent)
    public final static String EMV_TAG_IC_ICCPKRMD = "9F48";     // 41 - IC卡公钥余项(ICC Public Key Remainder)
    public final static String EMV_TAG_IC_IAC_DEFAULT = "9F0D";     // 42 - 发卡行行为代码-缺省(Issuer Action Code-Default)
    public final static String EMV_TAG_IC_IAC_DENIAL = "9F0E";     // 43 - 发卡行行为代码-拒绝(Issuer Action Code-Denial)
    public final static String EMV_TAG_IC_IAC_ONLINE = "9F0F";     // 44 - 发卡行行为代码-联机(Issuer Action Code-Online)
    public final static String EMV_TAG_IC_ISSAPPDATA = "9F10";     // 45 - 发卡行应用数据(Issuer Application Data)
    public final static String EMV_TAG_IC_ISSCTINDEX = "9F11";     // 46 - 发卡行代码表索引(Issuer Code Table Index)
    public final static String EMV_TAG_IC_ISSCOUNTRYCODE = "5F28";     // 47 - 发卡行国家代码(Issuer Country Code)
    public final static String EMV_TAG_IC_ISSPKCERT = "9000";     // 48 - 发卡行公钥证书(Issuer Public Key Certificate)
    public final static String EMV_TAG_IC_ISSPKEXP = "9F32";     // 49 - 发卡行公钥指数(Issuer Public Key Exponent)
    public final static String EMV_TAG_IC_ISSPKRMD = "9200";     // 50 - 发卡行公钥余项(Issuer Public Key Remainder)
    public final static String EMV_TAG_IC_LANGPREF = "5F2D";     // 52 - 首选语言(Language Preference)
    public final static String EMV_TAG_IC_LASTATC = "9F13";     // 53 - 上次联机ATC寄存器(Last Online Application Transaction Counter Register)
    public final static String EMV_TAG_IC_LCOL = "9F14";     // 54 - 连续脱机交易下限(Lower Consecutive Offline Limit)
    public final static String EMV_TAG_IC_PINTRYCNTR = "9F17";     // 55 - PIN重试计数器(Personal Identification Number Try Counter)
    public final static String EMV_TAG_IC_PDOL = "9F38";     // 56 - 处理选项数据对象列表PDOL(Processing Options Data Object List)
    public final static String EMV_TAG_IC_RMTF1 = "8000";     // 57 - 响应消息模板格式1(Response Message Template Format 1)
    public final static String EMV_TAG_IC_RMTF2 = "7700";     // 58 - 响应消息模板格式2(Response Message Template Format 2)
    public final static String EMV_TAG_IC_SERVICECODE = "5F30";     // 59 - 服务码(Service Code)
    public final static String EMV_TAG_IC_SFI = "8800";     // 60 - 短文件标识符SFI(Short File Indicator)
    public final static String EMV_TAG_IC_SIGNDYNAPPDT = "9F4B";     // 61 - 签名的动态应用数据(Signed Dynamic Application Data)
    public final static String EMV_TAG_IC_SIGNSTAAPPDT = "9300";     // 62 - 签名的静态应用数据(Signed Static Application Data)
    public final static String EMV_TAG_IC_SDATAGLIST = "9F4A";     // 63 - 静态数据认证标签列表(Static Data Authentication Tag List)
    public final static String EMV_TAG_IC_TRACK1DATA = "56";         // Track 1 Data
    public final static String EMV_TAG_IC_TRACK1DD = "9F1F";     // 64 - 1磁道自定义数据(Track 1 Discretionary Data)
    public final static String EMV_TAG_IC_TRACK2DATA = "57";         // 66 - 2磁道等效数据(Track 2 Equivalent Data)
    public final static String EMV_TAG_IC_TRACK2DD = "9F20";     // 65 - 2磁道自定义数据(Track 2 Discretionary Data)
    public final static String EMV_TAG_IC_TDOL = "9700";     // 67 - 交易证书数据对象列表TDOL(Transaction Certificate Data Object List)
    public final static String EMV_TAG_IC_UCOL = "9F23";     // 68 - 连续脱机交易上限(Upper Consecutive Offline Limit)
    public final static String EMV_TAG_IC_LOGENTRY = "9F4D";     // 71 - 交易明细记录文件(Transaction Log Entry)
    public final static String EMV_TAG_IC_LOGFORMAT = "9F4F";     // 72 - 交易明细记录数据元格式(Log Format)
    public final static String EMV_TAG_IC_KERNELID = "9F2A";     // 96 - kernel ID
    public final static String EMV_TAG_IC_BANKIDCODE = "5F54";     // 73 - 银行标识码(Bank Identifier Code - BIC)
    public final static String EMV_TAG_IC_IBAN = "5F53";     // 74 - 国际银行帐号(International Bank Account Number - IBAN)
    public final static String EMV_TAG_IC_ISSCOUNTRYCODE_A2 = "5F55";     // 75 - 发卡行国家代码-2个字母格式(Issuer country code)
    public final static String EMV_TAG_IC_ISSCOUNTRYCODE_A3 = "5F56";     // 76 - 发卡行国家代码-3个字母格式(Issuer country code)
    public final static String EMV_TAG_IC_ISSIDNUMBER = "42";         // 77 - 发卡行标识号码(IIN-Issuer Identification Number)

    /************************************************************************/
    /*  Terminal Data Flag                                                  */
    /************************************************************************/
    public final static String EMV_TAG_TM_ACQID = "9F01";    // 收单行标识符(Acquirer Identifier)
    public final static String EMV_TAG_TM_CAP_AD = "9F40";    // 附加终端性能(Additional Terminal Capability)
    public final static String EMV_TAG_TM_AUTHAMNTB = "81";    // 授权金额|二进制(Amount,Authorised<Binary>)
    public final static String EMV_TAG_TM_AUTHAMNTN = "9F02";    // 授权金额|数字(Amount,Authorised<Binary>)
    public final static String EMV_TAG_TM_OTHERAMNTB = "9F04";    // 其他金额|二进制(Amount,Other<Binary>)
    public final static String EMV_TAG_TM_OTHERAMNTN = "9F03";    // 其他金额|数字(Amount,Other<Numeric>)
    public final static String EMV_TAG_TM_REFCURAMNT = "9F3A";    // 参考货币金额(Amount,Reference Currency)
    public final static String EMV_TAG_TM_AID = "9F06";    // 终端AID(Terminal Application Identifier)
    public final static String EMV_TAG_TM_APPVERNO = "9F09";    // 终端应用版本号(Terminal Application Version Number)
    public final static String EMV_TAG_TM_AUTHCODE = "89";    // 授权码(Authorization Code)
    public final static String EMV_TAG_TM_ARC = "8A";    // 授权响应码ARC(Authorisation Response Code)
    public final static String EMV_TAG_TM_CVMRESULT = "9F34";    // 持卡人验证方法结果(Cardholder Verification Method Results)
    public final static String EMV_TAG_TM_CAPKINDEX = "9F22";    // 终端CA公钥索引(Terminal Certification Authority Public Key Index)
    public final static String EMV_TAG_TM_IFDSN = "9F1E";    // IFD序列号(Interface Device Serial Number)
    public final static String EMV_TAG_TM_ISSAUTHDT = "91";    // 发卡行认证数据(Issuer Authentication Data)
    public final static String EMV_TAG_TM_ISSSCRID = "9F18";    // 发卡行脚本标识符(Issuer Script Identifier)
    public final static String EMV_TAG_TM_MCHCATCODE = "9F15";    // 商户分类码(Merchant Category Code)
    public final static String EMV_TAG_TM_MCHID = "9F16";    // 商户标识符(Merchant Identifier)
    public final static String EMV_TAG_TM_POSENTMODE = "9F39";    // POS输入模式(Point-of-Service Entry Mode)
    public final static String EMV_TAG_TM_CAP = "9F33";    // 终端性能(Terminal Capabilities)
    public final static String EMV_TAG_TM_CNTRYCODE = "9F1A";    // 终端国家代码(Terminal Country Code)
    public final static String EMV_TAG_TM_FLOORLMT = "9F1B";    // 终端限额(Terminal Floor Limit)
    public final static String EMV_TAG_TM_TERMID = "9F1C";    // 终端标识符(Terminal Identification)
    public final static String EMV_TAG_TM_RMDATA = "9F1D";    // 终端风险管理数据(Terminal Risk Management Data)
    public final static String EMV_TAG_TM_TERMTYPE = "9F35";    // 终端类型(Terminal Type)
    public final static String EMV_TAG_TM_TVR = "95";    // 终端验证结果TVR(Terminal Verification Result)
    public final static String EMV_TAG_TM_TCHASH = "98";    // 交易证书哈希值(Transaction Certificate Hash Value)
    public final static String EMV_TAG_TM_CURCODE = "5F2A";    // 交易货币代码(Transaction Currency Code)
    public final static String EMV_TAG_TM_CUREXP = "5F36";    // 交易货币指数(Transaction Currency Exponent)
    public final static String EMV_TAG_TM_TRANSDATE = "9A";    // 交易日期(Transaction Date)
    public final static String EMV_TAG_TM_PINDATA = "99";    // PIN数据(Transaction Personal Identification Number Data)
    public final static String EMV_TAG_TM_REFCURCODE = "9F3C";    // 交易参考货币代码(Transaction Reference Currency Code)
    public final static String EMV_TAG_TM_REFCUREXP = "9F3D";    // 交易参考货币指数(Transaction Reference Currency Exponent)
    public final static String EMV_TAG_TM_TRSEQCNTR = "9F41";    // 交易序列计数器(Transcation Sequence Counter)
    public final static String EMV_TAG_TM_TSI = "9B";    // 交易状态信息TSI(Transaction Status Information)
    public final static String EMV_TAG_TM_TRANSTIME = "9F21";    // 交易时间(Transaction Time)
    public final static String EMV_TAG_TM_TRANSTYPE = "9C";    // 交易类型(Transaction Type)
    public final static String EMV_TAG_TM_UNPNUM = "9F37";    // 不可预知数(Unpredictable Number)
    public final static String EMV_TAG_TM_ISSSCR1 = "71";    // 发卡行脚本模板1(Issuer Script Template1)
    public final static String EMV_TAG_TM_ISSSCR2 = "72";    // 发卡行脚本模板2(Issuer Script Template2)
    public final static String EMV_TAG_TM_MCHNAMELOC = "9F4E";    // 商户名称及位置(Merchant Name and Location)
    public final static String EMV_TAG_TM_ACCOUNTTYPE = "5F57";    // Account Type

    /************************************************************************/
    /* Card Organization Labels:  M-MASTER V-VISA C-PBOC                    */
    /************************************************************************/
    public final static String M_TAG_IC_9F50 = "9F50";     // MASTER:Offline Accumulator Balance
    public final static String M_TAG_IC_9F51 = "9F51";     // MASTER:DRDOL
    public final static String C_TAG_TM_9F53 = "9F53";     // 交易类别代码(Transaction Category Code)
    public final static String M_TAG_TM_9F53 = "9F53";     // 交易类别代码(Transaction Category Code)
    public final static String V_TAG_IC_9F5A = "9F5A";     // Application Program ID
    public final static String M_TAG_IC_9F5D = "9F5D";       // MASTER:Application Capabilities Information
    public final static String C_TAG_IC_9F5D = "9F5D";       // PBOC:非接电子现金卡片余额(Non-cash cash card balance)
    public final static String V_TAG_IC_9F5D = "9F5D";       // VISA:Available Offline Spending Amount (AOSA)
    public final static String M_TAG_IC_9F60 = "9F60";     // MASTER: CVC3 (Track1)L=2
    public final static String C_TAG_IC_9F61 = "9F61";     // {PBOC:持卡人证件号码L=1-40, PayPass:CVC3 (Track2)L=2}
    public final static String M_TAG_IC_9F61 = "9F61";     // {CVC3 (Track2)L=2}
    public final static String C_TAG_IC_9F62 = "9F62";     // 持卡人证件类型(Cardholder ID type)
    public final static String M_TAG_IC_9F62 = "9F62";     // MASTER:PCVC3(Track1)L=6
    public final static String C_TAG_IC_9F63 = "9F63";       // 产品标识信息(Product identification information)
    public final static String M_TAG_IC_9F63 = "9F63";       // PUNATC(Track1)
    public final static String M_TAG_IC_9F64 = "9F64";       // NATC(Track1)
    public final static String M_TAG_IC_9F65 = "9F65";       // PCVC3(Track2)
    public final static String C_TAG_TM_9F66 = "9F66";       // 终端交易属性(Terminal transaction attributes)
    public final static String V_TAG_TM_9F66 = "9F66";       // 终端交易属性(Terminal transaction attributes)
    public final static String M_TAG_IC_9F66 = "9F66";       // PUNATC(Track2)
    public final static String M_TAG_IC_9F67 = "9F67";       // NATC(Track2)
    public final static String C_TAG_IC_9F68 = "9F68";       // qPBOC:卡片附加处理(Card attached processing)
    public final static String C_TAG_IC_9F69 = "9F69";       // qPBOC:卡片认证相关数据(Card certification related data)
    public final static String V_TAG_IC_9F69 = "9F69";       // qVSDC:卡片认证相关数据(Card certification related data)
    public final static String M_TAG_IC_9F69 = "9F69";       // MASTER:UDOL
    public final static String M_TAG_IC_9F6B = "9f6B";       // Track2Data
    public final static String M_TAG_TM_9F6A = "9f6A";       // M:UnPredictable Number
    public final static String C_TAG_IC_9F6C = "9F6C";       // qPBOC:卡片交易属性(Card transaction attributes)
    public final static String V_TAG_IC_9F6C = "9F6C";       // qVSDC:卡片交易属性(Card transaction attributes)
    public final static String V_TAG_IC_9F6E = "9F6E";       // qVSDC:Form Factor Indicator(FFI)
    public final static String M_TAG_IC_9F6E = "9F6E";       // Master:Third Party Data
    public final static String C_TAG_IC_9F6D = "9F6D";     // 电子现金重置阀值(6个字节, N12, Electronic Cash Balance)
    public final static String M_TAG_TM_9F6D = "9F6D";     // Mag-stripe Application Version Number (Reader)
    public final static String C_TAG_IC_9F74 = "9F74";     // 电子现金发卡行授权码(6个字节字母, N12, Electronic Cash Issuer Authorization Code)
    public final static String C_TAG_IC_9F77 = "9F77";     // 电子现金余额上限(6个字节, N12, Electronic Cash Balance Limit)
    public final static String C_TAG_IC_9F78 = "9F78";     // 电子现金单笔交易限额(6个字节, N12, Electronic Cash Sigle Transaction Limit)
    public final static String C_TAG_IC_9F79 = "9F79";     // 电子现金余额(6个字节, N12, Electronic Cash Balance)
    public final static String C_TAG_TM_9F7B = "9F7B";     // 电子现金终端交易限额(Terminal Transaction Limit)
    public final static String C_TAG_TM_9F7A = "9F7A";     // {VISA/PBOC: VLP标识}
    public final static String V_TAG_TM_9F7A = "9F7A";     // {VISA/PBOC: VLP标识}
    public final static String V_TAG_IC_9F7C = "9F7C";     // Customer Exclusive Data(CED)
    public final static String M_TAG_TM_9F7C = "9F7C";     // Merchant Custom Data
    public final static String M_TAG_TM_9F7E = "9F7E";     // M:Mobile Support Indicator

    //PBOC
    public final static String C_TAG_IC_DF4D = "DF4D";     // 圈存明细记录文件(Transaction Log Entry)
    public final static String C_TAG_IC_DF4F = "DF4F";     // 圈存明细记录数据元格式(Log Format)
    public final static String C_TAG_TM_DF69 = "DF69";     // 国密算法标识(National secret algorithm logo)
    public final static String C_TAG_TM_DF31 = "DF31";     // 发卡行脚本执行执行结果(The issuing line script executes the execution result)

    //VISA
    public final static String V_TAG_RD_DDAVER = "DF03";     // Enhanced DDA version number, tag DF03
    public final static String V_TAG_RD_CVM_REQUIRE = "DF04";    // CVM Requirements, tag DF04
    public final static String V_TAG_RD_DSP_FUNDS = "DF05";     // Display Offline Funds Indicator, tag DF05
    public final static String V_TAG_RD_RCP = "DF06";     // Reader Configuration Parameters, tag DF06

    //PayPass
    public final static String M_TAG_IC_PCII = "DF4B";       // POS Cardholder Interaction Information
    public final static String M_TAG_DDTRACK1 = "DF812A"; // DD Card (Track1)
    public final static String M_TAG_DDTRACK2 = "DF812B"; // DD Card (Track2)

    //AMEX
    public final static String A_TAG_TM_9F6D = "9F6D";    // Contactless Reader Capabilities
    public final static String A_TAG_TM_9F6E = "9F6E";    // Enhanced Contactless Reader Capabilities
    public final static String A_TAG_TM_TRANS_LIMIT = "DF8124";    // Terminal Contactless Transaction Limit
    public final static String A_TAG_TM_FLOOR_LIMIT = "DF8123";    // Terminal Contactless Floor Limit
    public final static String A_TAG_TM_CVM_LIMIT = "DF8126";    // Terminal CVM Required Limit
    public final static String A_TAG_PREAGAIN = "DF8130";    // Indicate whether a TryAgain needed or not
    public final static String A_TAG_TM_IN_CARD_BIN_RANGE = "DF8127";    // Indicate if the CardBin in the white CardBin list or not.

    /************************************************************************/
    /* Customize Terminal Parameter Labels                                  */
    /************************************************************************/
    public final static String DEF_TAG_PSE_FLAG = "DF918101";     // Enable usage of the PSE
    public final static String DEF_TAG_GAC_CONTROL = "DF918102";     // Indicate if the merchant can force the transaction to be online or not.
    public final static String DEF_TAG_QUERY_ICCLOG = "DF918103";     // Indicate if the transaction log is supported or not.
    public final static String DEF_TAG_SERVICE_TYPE = "DF918104";     // Service Type reference macro define(different with 9C)
    public final static String DEF_TAG_START_RECOVERY = "DF918105";     // Indicate if the transaction is torn recovery.
    public final static String DEF_TAG_PAN_IN_BLACK = "DF918106";     // Indicate if the card is in the exception file (black list).
    public final static String DEF_TAG_ACCUMULATE_AMOUNT = "DF918107";     // Accumulate amount of offline approved transaction performed with this card of this terminal
    public final static String DEF_TAG_CHV_STATUS = "DF918108";     // Card holder verification status
    public final static String DEF_TAG_ONLINE_STATUS = "DF918109";     // Online status
    public final static String DEF_TAG_AUTHORIZE_FLAG = "DF91810A";     // Online authorize flag
    public final static String DEF_TAG_HOST_TLVDATA = "DF91810B";     // Online transaction host response TLV Data

    public final static String DEF_TAG_RAND_SLT_THRESHOLD = "DF91810C";     // Biased random selection Threshold
    public final static String DEF_TAG_RAND_SLT_PER = "DF91810D";     // Biased random selection target percentage
    public final static String DEF_TAG_RAND_SLT_MAXPER = "DF91810E";     // Biased random selection max target percentage
    public final static String DEF_TAG_TAC_DEFAULT = "DF918110";        // Terminal Action Code_Default
    public final static String DEF_TAG_TAC_DECLINE = "DF918111";        // Terminal Action Code_Decline
    public final static String DEF_TAG_TAC_ONLINE = "DF918112";        // Terminal Action Code_Online
    public final static String DEF_TAG_M_TRANS_MODE = "DF918201";     // Paypass transaction mode
    public final static String DEF_TAG_M_BALANCE_SUP = "DF918202";     // Paypass balance support indicator
    public final static String DEF_TAG_M_REQ_CVM = "DF918205";            // Paypass CVM Capability – CVM Required
    public final static String DEF_TAG_M_REQ_NOCVM = "DF918206";      // Paypass CVM Capability – NoCVM Required
    public final static String DEF_TAG_M_MAG_REQ_CVM = "DF918207";            // Paypass Mag-stripe CVM Capability – CVM Required
    public final static String DEF_TAG_M_MAG_REQ_NOCVM = "DF918208";      // Paypass Mag-stripe CVM Capability – NoCVM Required
    public final static String DEF_TAG_M_MSG_HOLDTIME = "DF918209";      // Paypass Message Hold Time
    public final static String DEF_TAG_M_RF_HOLDTIME = "DF91820A";      // Paypass CARD_TYPE_RF Hold Time Value
    public final static String DEF_TAG_M_TORN_TRANS = "DF918203";     // Paypass torn transaction support indicator
    public final static String DEF_TAG_M_CDV_SUP = "DF918204";     // Paypass On device cardholder verification supported
    public final static String DEF_TAG_V_TRACK1_ACTIVE = "DF918301";    // PayWave Indicate if the Track 1 Actived
    public final static String DEF_TAG_V_TRACK2_ACTIVE = "DF918302";    // PayWave Indicate if the Track 2 Actived
    public final static String DEF_TAG_V_CVN17_ACTIVE = "DF918303";        // PayWave Indicate if the CVN17 Actived
    public final static String DEF_TAG_ALLOW_DUP_ICC_SAMEVALUE = "DF918140";

    public final static String M_TAG_TM_TRANS_LIMIT = "DF8124";            // Contactless Transaction Limit(No On-device CVM)
    public final static String M_TAG_TM_TRANS_LIMIT_CDV = "DF8125";            // Contactless Transaction Limit (On-device CVM)
    public final static String M_TAG_TM_CVM_LIMIT = "DF8126";            // Contactless CVM Required Limit
    public final static String M_TAG_TM_FLOOR_LIMIT = "DF8123";            // Contactless Floor Limit

    public final static String M_TAG_TM_REQ_CVM = "DF8118";         // CVM Capability – CVM Required
    public final static String M_TAG_TM_REQ_NOCVM = "DF8119";         // CVM Capability – NoCVM Required
    public final static String M_TAG_TM_MAG_REQ_CVM = "DF811E";         // Mag-stripe CVM Capability – CVM Required
    public final static String M_TAG_TM_MAG_REQ_NOCVM = "DF812C";         // Mag-stripe CVM Capability – NoCVM Required
    public final static String M_TAG_TM_MSG_HOLDTIME = "DF812D";         // Message Hold Time
    public final static String M_TAG_TM_RF_HOLDTIME = "DF8130";         // CARD_TYPE_RF Hold Time Value

    public final static String C_TAG_TM_TRANS_LIMIT = "DF8124";            // Contactless Transaction Limit
    public final static String C_TAG_TM_CVM_LIMIT = "DF8126";            // Contactless CVM Required Limit
    public final static String C_TAG_TM_FLOOR_LIMIT = "DF8123";        // Contactless Floor Limit

    public final static String V_TAG_TM_TRANS_LIMIT = "DF8124";            // Contactless Transaction Limit
    public final static String V_TAG_TM_CVM_LIMIT = "DF8126";            // Contactless CVM Required Limit
    public final static String V_TAG_TM_FLOOR_LIMIT = "DF8123";            // Contactless Floor Limit

    public final static String DDOL = "DF918121";
    public final static String TDOL = "DF918122";
    public final static String UDOL = "DF918123";
}
