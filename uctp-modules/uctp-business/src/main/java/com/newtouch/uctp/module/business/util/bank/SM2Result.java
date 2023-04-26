package com.newtouch.uctp.module.business.util.bank;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

/**
 * SM2
 * 
 */
public class SM2Result {

    public SM2Result() {}
    
    public BigInteger r;// 签名r

    public BigInteger s;
    
    public BigInteger R;// 验签R
    
    public byte[] sa;// 密钥交换

    public byte[] sb;

    public byte[] s1;

    public byte[] s2;

    public ECPoint keyra;

    public ECPoint keyrb;

}
