package com.lovely3x.common.managements.pay;

/**
 * 支付动作接口
 * 实现该接口,并将自己注册到支付管理器中
 * 支付管理器就会调用 {@link #pay(PayRequest, IPayCallBack.PayMethod)}  来进行支付操作
 * Created by lovely3x on 16/8/24.
 */
public interface PayAction {

    /**
     * 支付
     *
     * @param payRequest 支付请求
     * @param payMethod  支付方式
     * @return 如果该支付动作 支持支付这个类型的支付 那么就应该返回 true 其他情况返回 false
     */
    boolean pay(PayRequest payRequest, IPayCallBack.PayMethod payMethod);

}
