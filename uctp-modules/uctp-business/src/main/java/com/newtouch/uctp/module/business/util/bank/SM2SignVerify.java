package com.newtouch.uctp.module.business.util.bank;

import org.bouncycastle.asn1.*;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.Enumeration;

/**
 * 国密算法的签名、验签
 * 
 */
public class SM2SignVerify {

    /**
     * 默认USERID
     */
    public static String USER_ID = "1234567812345678";

    /**
     * 私钥签名 使用SM3进行对明文数据计算一个摘要值
     * 
     * @param privatekey 私钥
     * @param sourceData 明文数据
     * @return 签名后的值
     * @throws Exception
     */
    public static SM2Sign sign(byte[] privatekey, byte[] sourceData) throws Exception {
        SM2Sign sm2SignVO = new SM2Sign();
        sm2SignVO.setSm2_type("sign");
        SM2Factory factory = SM2Factory.getInstance();
        BigInteger userD = new BigInteger(1, privatekey);
        sm2SignVO.setSm2_userd(userD.toString(16));

        ECPoint userKey = factory.ecc_point_g.multiply(userD);
        sm2SignVO.setX_coord(userKey.getXCoord().toBigInteger().toString(16));
        sm2SignVO.setY_coord(userKey.getYCoord().toBigInteger().toString(16));

        SM3Digest sm3Digest = new SM3Digest();
        byte[] z = factory.sm2GetZ(USER_ID.getBytes(), userKey);
        sm2SignVO.setSm3_z(SM2Util.getHexString(z));
        sm2SignVO.setSign_express(SM2Util.getHexString(sourceData));
        sm3Digest.update(z, 0, z.length);
        sm3Digest.update(sourceData, 0, sourceData.length);
        byte[] md = new byte[32];
        sm3Digest.doFinal(md, 0);
        sm2SignVO.setSm3_digest(SM2Util.getHexString(md));

        SM2Result sm2Result = new SM2Result();
        factory.sm2Sign(md, userD, userKey, sm2Result);
        sm2SignVO.setSign_r(sm2Result.r.toString(16));
        sm2SignVO.setSign_s(sm2Result.s.toString(16));

        ASN1Integer d_r = new ASN1Integer(sm2Result.r);
        ASN1Integer d_s = new ASN1Integer(sm2Result.s);
        ASN1EncodableVector v2 = new ASN1EncodableVector();
        v2.add(d_r);
        v2.add(d_s);
        DERSequence sign = new DERSequence(v2);
        String result = ByteUtils.toHexString(sign.getEncoded());
        sm2SignVO.setSm2_sign(result);
        return sm2SignVO;
    }

    /**
     * 验证签名(验签)
     * 
     * @param publicKey 公钥信息
     * @param sourceData 密文信息
     * @param signData 签名信息
     * @return 验签的对象 包含了相关参数和验签结果
     */
    @SuppressWarnings("unchecked")
    public static SM2Sign validateSign(byte[] publicKey, byte[] sourceData, byte[] signData) {
        try {
            byte[] formatedPubKey;
            SM2Sign verifyVo = new SM2Sign();
            verifyVo.setSm2_type("verify");
            if (publicKey.length == 64) {
                // 添加一字节标识，用于ECPoint解析
                formatedPubKey = new byte[65];
                formatedPubKey[0] = 0x04;
                System.arraycopy(publicKey, 0, formatedPubKey, 1, publicKey.length);
            } else {
                formatedPubKey = publicKey;
            }
            SM2Factory factory = SM2Factory.getInstance();
            ECPoint userKey = factory.ecc_curve.decodePoint(formatedPubKey);

            SM3Digest sm3Digest = new SM3Digest();
            byte[] z = factory.sm2GetZ(USER_ID.getBytes(), userKey);
            verifyVo.setSm3_z(SM2Util.getHexString(z));
            sm3Digest.update(z, 0, z.length);
            sm3Digest.update(sourceData, 0, sourceData.length);
            byte[] md = new byte[32];
            sm3Digest.doFinal(md, 0);
            verifyVo.setSm3_digest(SM2Util.getHexString(md));

            ByteArrayInputStream bis = new ByteArrayInputStream(signData);
            ASN1InputStream dis = new ASN1InputStream(bis);
            SM2Result sm2Result = null;
            ASN1Primitive derObj = dis.readObject();
            dis.close();
            bis.close();
            Enumeration<ASN1Integer> e = ((ASN1Sequence) derObj).getObjects();
            BigInteger r = ((ASN1Integer) e.nextElement()).getValue();
            BigInteger s = ((ASN1Integer) e.nextElement()).getValue();
            sm2Result = new SM2Result();
            sm2Result.r = r;
            sm2Result.s = s;
            verifyVo.setVerify_r(sm2Result.r.toString(16));
            verifyVo.setVerify_s(sm2Result.s.toString(16));
            factory.sm2Verify(md, userKey, sm2Result.r, sm2Result.s, sm2Result);
            boolean verifyFlag = sm2Result.r.equals(sm2Result.R);
            verifyVo.setVerify(verifyFlag);
            return verifyVo;
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
