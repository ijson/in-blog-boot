package com.ijson.blog.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import java.security.Key;

/**
 * 自定义的DES加密和解密工具,可以对字符串进行加密和解密操作 。
 */
@Slf4j
public class DesUtil {


    /**
     * 字符串默认键值
     */
    public static String strDefaultKey = "s2_I$-!#eu_3y2*4D-8^2{A3R_E}I5%&U#I@-O;!";
    /**
     * 加密工具
     */
    public static Cipher encryptCipher = null;
    /**
     * 解密工具
     */
    public static Cipher decryptCipher = null;

    public static void init() throws Exception {
        if (encryptCipher == null || decryptCipher == null) {
            Key key = getKey(strDefaultKey.getBytes());
            encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);
            decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        }
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     *
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     */
    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 加密字节数组
     *
     * @param arrB 需加密的字节数组
     * @return 加密后的字节数组
     */
    public static byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     *
     * @param strIn 需加密的字符串
     * @return 加密后的字符串
     */
    public static String encrypt(String strIn) {
        String strIn2 = "";
        try {
            init();
            // logger.info("第一次加密前字符串strIn="+strIn);
            // 1 将需加密的字符串第一次字节转换加密
            String strIn1 = byteEncrypt(strIn);
            // logger.info("第一次加密后字符串strIn1="+strIn1);
            // 2 DES加密
            // logger.info("第二次加密后字符串strIn1.getBytes()="+strIn1.getBytes());
            byte[] s3 = encrypt(strIn1.getBytes());
            // logger.info("第二次加密后字符串s3="+s3);
            strIn2 = byteArr2HexStr(s3);
            // logger.info("第二次加密后字符串strIn1="+strIn2);
        } catch (Exception e) {
            log.error("", e);
        }

        return strIn2;
    }

    /**
     * 解密字节数组
     *
     * @param arrB 需解密的字节数组
     * @return 解密后的字节数组
     */
    public static byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密字符串
     *
     * @param strIn 需解密的字符串
     * @return 解密后的字符串
     */
    public static String decrypt(String strIn) {
        try {
            init();
            // 1 DES解密
            String str = new String(decrypt(hexStr2ByteArr(strIn)));
            // 2 将需加密的字符串第二次字节转换加密
            return StringUtils.trimToEmpty(byteDecrypt(str));
        } catch (Exception e) {
            log.error("", e);
        }
        return "";
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     */
    public static Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }

    /**
     * 字节转换加密
     *
     * @param oriStr
     * @return
     */
    public static String byteEncrypt(String oriStr) {
        if (StringUtils.isNotBlank(oriStr)) {
            StringBuffer newStr = new StringBuffer();
            oriStr = StringUtils.trimToEmpty(oriStr);
            char[] strArray = oriStr.toCharArray();
            for (int i = 0; i < strArray.length; i++) {
                char c = strArray[i];
                if (i % 2 == 0) {
                    newStr.append((char) (c + 1));
                } else {
                    newStr.append((char) (c - 1));
                }
            }
            return newStr.toString();
        } else {
            return "";
        }
    }

    /**
     * 字节转换解密
     *
     * @param oriStr
     * @return
     */
    public static String byteDecrypt(String oriStr) {
        if (StringUtils.isNotBlank(oriStr)) {
            StringBuffer newStr = new StringBuffer();
            // oriStr = StringUtils.trimToEmpty(oriStr);
            // 偶数为!时加密后为空格,去空格可能造成问题.因此去除此步骤
            char[] strArray = oriStr.toCharArray();
            for (int i = 0; i < strArray.length; i++) {
                char c = strArray[i];
                if (i % 2 == 0) {
                    newStr.append((char) (c - 1));
                } else {
                    newStr.append((char) (c + 1));
                }
            }
            return newStr.toString();
        } else {
            return "";
        }
    }

    public static void main(String[] args) throws Exception {
        // 加密字符：CAT0001,1478842909958
        // 加密后的字符：131b94c44bcdd8324c7e71dbc960904bee9a537999a97d3f
        // 解密后的字符：CAT0001,1478842909958
        DesUtil.init();
        String token = "CAT0001,1478842909958";
        System.out.println("加密字符：" + token);
        System.out.println("加密后的字符：" + DesUtil.encrypt(token));
        System.out.println("解密后的字符：" + DesUtil.decrypt(encrypt(token)));
    }
}