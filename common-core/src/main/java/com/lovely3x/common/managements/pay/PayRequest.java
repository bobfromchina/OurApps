package com.lovely3x.common.managements.pay;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 支付请求对象
 * Created by lovely3x on 16/8/24.
 */
public class PayRequest implements Parcelable {

    private String orderNum;
    private String tn;  //  appid
    private String sign;//":"0704372C44EC24F5DBCFD6DC1C63B0C0", "
    private String partnerid;//":1483268122", "
    private String prepayid;//":"wx20170628102638db1efb6d440766577450", "
    private String noncestr;//":"hnldjf9us3btpa1d0zs7c8pwyj21yhjw", "
    private String timestamp;//":"1498616798"

    public PayRequest() {
    }

    public PayRequest(String orderNum, String tn, String sign, String partnerid, String prepayid, String noncestr, String timestamp) {
        this.orderNum = orderNum;
        this.tn = tn;
        this.sign = sign;
        this.partnerid = partnerid;
        this.prepayid = prepayid;
        this.noncestr = noncestr;
        this.timestamp = timestamp;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getTn() {
        return tn;
    }

    public void setTn(String tn) {
        this.tn = tn;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PayRequest{" +
                "orderNum='" + orderNum + '\'' +
                ", tn='" + tn + '\'' +
                ", sign='" + sign + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderNum);
        dest.writeString(this.tn);
        dest.writeString(this.sign);
        dest.writeString(this.partnerid);
        dest.writeString(this.noncestr);
        dest.writeString(this.prepayid);
        dest.writeString(this.timestamp);
    }

    protected PayRequest(Parcel in) {
        this.orderNum = in.readString();
        this.tn = in.readString();
        this.sign = in.readString();
        this.partnerid = in.readString();
        this.noncestr = in.readString();
        this.prepayid = in.readString();
        this.timestamp = in.readString();
    }

    public static final Creator<PayRequest> CREATOR = new Creator<PayRequest>() {
        @Override
        public PayRequest createFromParcel(Parcel source) {
            return new PayRequest(source);
        }

        @Override
        public PayRequest[] newArray(int size) {
            return new PayRequest[size];
        }
    };
}

