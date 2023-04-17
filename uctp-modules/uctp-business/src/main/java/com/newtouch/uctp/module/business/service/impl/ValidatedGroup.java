package com.newtouch.uctp.module.business.service.impl;


//接口参数校验组对象
public class ValidatedGroup {

    //保证金充值
    public interface Recharge{};
 
    //保证金提取
    public interface Withdraw{};

    //保证金预占
    public interface Reserve{};

    //保证金实占
    public interface Deduction{};

    //待回填保证金
    public interface Difference{};

    //保证金回填
    public interface Back{};

    //保证金-利润回填
    public interface ProfitBack{};

    //保证金释放
    public interface Release{};
}
