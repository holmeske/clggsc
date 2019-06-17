package com.sc.clgg.etc;

import java.io.UnsupportedEncodingException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public final class NewDES {

    // DES 加密 不需要向量 当key是3倍长也支持
    public static String ENCRYPTMethod(String HexString, String keyStr) throws Exception {
        try {
            byte[] theKey = null;
            byte[] theMsg = null;
            theMsg = DataFormat.hexToBytes(HexString);
            theKey = DataFormat.hexToBytes(keyStr);
            KeySpec ks = new DESKeySpec(theKey);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher cf = Cipher.getInstance("DES/ECB/NoPadding");
            cf.init(Cipher.ENCRYPT_MODE, ky);
            byte[] theCph = cf.doFinal(theMsg);
            return DataFormat.bytesToHex(theCph);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * DES解密
     *
     * @param hexStr 16位十六进制字符串
     * @param keyStr 密钥16个1
     * @throws Exception
     */
    public static String DECRYPTMethod(String hexStr, String keyStr) throws Exception {
        try {
            byte[] theKey = null;
            byte[] theMsg = null;
            theMsg = DataFormat.hexToBytes(hexStr);
            theKey = DataFormat.hexToBytes(keyStr);
            KeySpec ks = new DESKeySpec(theKey);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher cf = Cipher.getInstance("DES/ECB/NoPadding");
            cf.init(Cipher.DECRYPT_MODE, ky);
            byte[] theCph = cf.doFinal(theMsg);
            return DataFormat.bytesToHex(theCph);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    // 传统上的3DES，但要求key是双倍长
    public static String TriDESEnCryt(String trikey, String srcData) throws Exception {
        // 使用密钥的前16字节，对数据DATA进行加密，得到加密的结果TMP1;
        String DataStart = NewDES.ENCRYPTMethod(srcData, trikey.substring(0, 16));
        // 使用密钥的后16字节，对第一的计算结果TMP1，进行解密，得到解密的结果TMP2
        String DataMiddle = NewDES.DECRYPTMethod(DataStart, trikey.substring(16, 32));
        // 再次使用密钥的前16字节，对第二次的计算结果TMP2，进行加密，得到加密的结果DEST。DEST就为最终的结果
        return NewDES.ENCRYPTMethod(DataMiddle, trikey.substring(0, 16));
    }

    // 3DES 解密
    public static String TriDESDeCryt(String trikey, String dstData) throws Exception {
        // 使用密钥的前16字节，对数据DATA进行加密，得到加密的结果TMP1;
        String DataStart = NewDES.DECRYPTMethod(dstData, trikey.substring(0, 16));
        String DataMiddle = NewDES.ENCRYPTMethod(DataStart, trikey.substring(16, 32));
        return NewDES.DECRYPTMethod(DataMiddle, trikey.substring(0, 16));
    }

    public static String DivKey(String genkey, String div) throws Exception {
        String subkey = "";
        String tempLKey = "", tempRKey = "";

        tempLKey = TriDESEnCryt(genkey, div);
        tempRKey = TriDESEnCryt(genkey, BitNoT(div));

        subkey = tempLKey + tempRKey;
        return subkey;
    }

    /*
     * 按位取反
     */
    public static String BitNoT(String SrcData) throws UnsupportedEncodingException {
        byte[] thedata = null;
        thedata = DataFormat.hexToBytes(SrcData);
        for (int i = 0; i < SrcData.length() / 2; i++) {
            thedata[i] = (byte) (thedata[i] ^ 0XFF);
        }
        return DataFormat.bytesToHex(thedata);
    }

    public static String PBOC_3DES_MAC(String data, String key) throws Exception {
        String I = "";
        try {
            String vector = "0000000000000000";
            if (key.length() != 32) {
                return null;
            }
            int len = data.length();
            int arrLen = len / 16 + 1;
            String[] D = new String[arrLen];
            if (len % 16 == 0) {
                data += "8000000000000000";
            } else {
                data += "80";
                for (int i = 0; i < 15 - len % 16; i++) {
                    data += "00";
                }
            }
            for (int i = 0; i < arrLen; i++) {
                D[i] = data.substring(i * 16, i * 16 + 16);
            }
            // D0 Xor Vector
            I = DataFormat.DataXor(D[0], vector);
            String O = null;
            String kl = key.substring(0, 16);
            for (int i = 1; i < arrLen; i++) {
                O = ENCRYPTMethod(I, kl);
                I = DataFormat.DataXor(D[i], O);
            }
            I = TriDESEnCryt(key, I);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return I;
    }

}
