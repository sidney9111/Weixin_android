package com.peiban.service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.util.Base64;

/**
 * 
 * 功能：文本加密 <br />
 * 日期：2013-2-28<br />
 * 地点：淘高手网<br />
 * 版本：ver 1.0<br />
 * 
 * @author fighter
 * @since
 */
public class DES3
{
  private static String PASSWORD_CRYPT_KEY = "F8WoCaONi88";
  private static String IPS = "fsDaB12s";
  
  /**
   * 加密
   * @param message
   * @return
   * @throws Exception
   * 作者:fighter <br />
   * 创建时间:2013-5-5<br />
   * 修改时间:<br />
   */
  public static String encrypt(String message)
    throws Exception
  {
    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    DESKeySpec desKeySpec = new DESKeySpec(PASSWORD_CRYPT_KEY
      .getBytes("UTF-8"));
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
    IvParameterSpec iv = new IvParameterSpec(IPS.getBytes("UTF-8"));
    cipher.init(1, secretKey, iv);
    byte[] b = cipher.doFinal(message.getBytes("UTF-8"));
    return Base64.encodeToString(b, 0);
  }

  /**
   * 解密
   * @param message
   * @return
   * @throws Exception
   * 作者:fighter <br />
   * 创建时间:2013-5-5<br />
   * 修改时间:<br />
   */
  public static String decrypt(String message)
    throws Exception
  {
    byte[] bytesrc = Base64.decode(message, 0);
    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    DESKeySpec desKeySpec = new DESKeySpec(PASSWORD_CRYPT_KEY
      .getBytes("UTF-8"));
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
    IvParameterSpec iv = new IvParameterSpec(IPS.getBytes("UTF-8"));

    cipher.init(2, secretKey, iv);

    byte[] retByte = cipher.doFinal(bytesrc);
    return new String(retByte);
  }
}