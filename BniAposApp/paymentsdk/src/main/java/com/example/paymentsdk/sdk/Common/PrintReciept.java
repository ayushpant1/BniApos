package com.example.paymentsdk.sdk.Common;

public class PrintReciept {
    String MMID;
    String MTID;
    String Mobile;
    String PayID;
    String InvoiceNo;
    String Batch;
    String SaleType;
    String IssuerType;
    String Amount;
    String Discount;
    String ET_Amount;
    String Net_Amount;
    String Auth_Code;
    String FooterText1;
    String FooterText2;
    String FooterText3;
    String FooterText4;
    String Date;
    String Time;

    public PrintReciept(String _MMID,String _MTID,String _Date,String _Time,String _AuthCode,String _Net_Amount,
                        String _ET_Amount,String _Discount,String _Amount,String _IssuerType,String _SaleType,
                        String _Batch,String _InvoiceNo,String _PayId,String _Mobile,
                        String FooterText1,String FooterText2,String FooterText3,String FooterText4)
    {

        this.MMID=_MMID;
        this.MTID=_MTID;
        this.Date=_Date;
        this.Time = _Time;
        this.Auth_Code=_AuthCode;
        this.Net_Amount=_Net_Amount;
        this.ET_Amount=_ET_Amount;
        this.Discount = _Discount;
        this.Amount = _Amount;
        this.IssuerType = _IssuerType;
        this.SaleType = _SaleType;
        this.Batch = _Batch;
        this.InvoiceNo = _InvoiceNo;
        this.PayID = _PayId;
        this.Mobile = _Mobile;
        this.FooterText1=FooterText1;
        this.FooterText2 = FooterText2;
        this.FooterText3 = FooterText3;
        this.FooterText4 = FooterText4;
    }

    public String getMMID() {
        return MMID;
    }

    public String getMTID() {
        return MTID;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getPayID() {
        return PayID;
    }

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public String getBatch() {
        return Batch;
    }

    public String getSaleType() {
        return SaleType;
    }

    public String getIssuerType() {
        return IssuerType;
    }

    public String getAmount() {
        return Amount;
    }

    public String getDiscount() {
        return Discount;
    }

    public String getET_Amount() {
        return ET_Amount;
    }

    public String getNet_Amount() {
        return Net_Amount;
    }

    public String getAuth_Code() {
        return Auth_Code;
    }

    public String getFooterText1() {
        return FooterText1;
    }

    public String getFooterText2() {
        return FooterText2;
    }

    public String getFooterText3() {
        return FooterText3;
    }

    public String getFooterText4() {
        return FooterText4;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }
}
