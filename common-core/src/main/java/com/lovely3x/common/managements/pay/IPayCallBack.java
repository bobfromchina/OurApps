package com.lovely3x.common.managements.pay;

/**
 * 支付回调接口
 * Created by lovely3x on 16/8/23.
 */
public interface IPayCallBack {

    /**
     * 当支付完成后执行
     *
     * @param payMethod 支付的方式
     * @param payResult 支付的结果
     */
    public void onPayResult(PayMethod payMethod, PayResult payResult);

    /**
     * 支付方式
     */
    public enum PayMethod {
        /*阿里支付*/
        AliPay,
        /*微信支付*/
        WeChatPay,
        /*银联支付*/
        UnionPay,
        /*支付宝2.0*/
        AliPay2,
        /*支付宝2.0 服务端签名方式*/
        AliPayServerSign,
        /*百度支付*/
        BiaDuPay,
        /*连连支付*/
        LianLainPay,
    }

    /**
     * 支付结果
     */
    public enum PayResult {
        /*支付成功*/
        Successful,
        /*用户取消操作*/
        UserCancel,
        /*支付失败*/
        Fail,
        /*支付等待中*/
        Waiting,
    }
}
