package com.amtzhmt.launcher.push;

import android.util.Base64;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class OriginalUtil {

	private  final static String secretKey = "032jyKNt85c9JQzO9RnFTg==";


	public static final class TripleDES {
		public static final String encrypt(String data) {
			byte[] encrpyted = tripleDES(Cipher.ENCRYPT_MODE, data.getBytes(),
					secretKey.getBytes()); // 3DES加密
			byte[] encoded = Base64.encode(encrpyted, Base64.NO_WRAP); // Base64编码
			return new String(encoded);
		}

		public static final String decrypt(String data) {
			byte[] decoded = Base64.decode(data, Base64.NO_WRAP); // Base64解码
			byte[] decrypted = tripleDES(Cipher.DECRYPT_MODE, decoded,
					secretKey.getBytes());// 3DES解密
			return new String(decrypted);
		}


		private static byte[] tripleDES(int opmode, byte[] data,
										byte[] secretKey) {
			return cipher("DESede", "DESede/CBC/PKCS5Padding", opmode, data,
					"01234567".getBytes(), secretKey);
		}



	}

	public static final String SHA256(String data) {
		try {
			byte[] bytes = data.getBytes();
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(bytes);
			StringBuffer buf = new StringBuffer();
			byte[] digest = md.digest();
			for (int i = 0; i < digest.length; i++) {
				buf.append(Integer.toHexString(0x100 + (digest[i] & 0xff))
						.substring(1));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static final String MD5(String data) {
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			md.update(data.getBytes());
			byte[] hash = md.digest();
			char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
					'9', 'a', 'b', 'c', 'd', 'e', 'f'};
			int j = hash.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = hash[i];
				buf[k++] = hexDigits[b >>> 4 & 0xf];
				buf[k++] = hexDigits[b & 0xf];
			}
			return new String(buf);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	public static final class BASE64 {
		public static final String encode(String data) {
			return new String(Base64.encode(data.getBytes(), Base64.NO_WRAP));
		}

		public static final String decode(String data) {
			return new String(Base64.decode(data.getBytes(), Base64.NO_WRAP));
		}
	}

	/**
	 * 通用的对称加密算法
	 *
	 * @param algorithm      , 算法名称
	 * @param transformation , 算法名称/工作模式/填充模式
	 * @param opmode         ：Cipher.ENCRYPT_MODE和Cipher.DECRYPT_MODE
	 * @param data           , 明文或密文数据
	 * @param iv             , 初始化向量
	 * @param secretKey      ，密钥
	 * @return 加密或解密的结果
	 */
	private static final byte[] cipher(String algorithm, String transformation,
									   int opmode, byte[] data, byte[] iv, byte[] secretKey) {
		try {
			// 转换密钥
			Key key = SecretKeyFactory.getInstance(algorithm).generateSecret(
					new DESedeKeySpec(secretKey));
			// 转换初始化向量
			IvParameterSpec spec = new IvParameterSpec(iv);

			// 加密解密器
			Cipher cipher = Cipher.getInstance(transformation);
			cipher.init(opmode, key, spec);

			// 加密解密操作
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	private static final String KEY_ALGORITHM = "AES";
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法
	private static final String KEY_ADD = "abcdeadcdeab";
	private static final String KEY = "sde@5f98H*^hsff%dfs$r344&df8543*er";
	/**
	 * AES 加密操作
	 *
	 * @param content 待加密内容
	 * @param key 加密密钥
	 * @return 返回Base64转码后的加密数据
	 */
	public static String encrypt(String content, String key) {
		try {
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

			byte[] byteContent = content.getBytes("utf-8");

			cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));// 初始化为加密模式的密码器

			byte[] result = cipher.doFinal(byteContent);// 加密

//			return Base64Utils.encode(result)+KEY_ADD;//通过Base64转码返回
			return Base64.encode(result,0)+KEY_ADD;//通过Base64转码返回

		} catch (Exception ex) {
		}

		return null;
	}

	/**
	 * AES 解密操作
	 *
	 * @param content
	 * @param key
	 * @return
	 */
	public static String decrypt(String content, String key) {
		content = content.replace(KEY_ADD,"");
		try {
			//实例化
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

			//使用密钥初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key));

			//执行操作
//			byte[] result = cipher.doFinal(Base64Utils.decode(content));
			byte[] result = cipher.doFinal(Base64.decode(content,0));


			return new String(result, "utf-8");
		} catch (Exception ex) {
		}

		return null;
	}

	/**
	 * 生成加密秘钥
	 *
	 * @return
	 */
	private static SecretKeySpec getSecretKey(final String key) {
		//返回生成指定算法密钥生成器的 KeyGenerator 对象
		KeyGenerator kg = null;

		try {
			kg = KeyGenerator.getInstance(KEY_ALGORITHM);

			//AES 要求密钥长度为 128
			kg.init(128, new SecureRandom(key.getBytes()));

			//生成一个密钥
			SecretKey secretKey = kg.generateKey();

			return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
		} catch (NoSuchAlgorithmException ex) {
		}

		return null;
	}

	public static void main(String[] args) {
		String content = "1234567890";

		System.out.println("content:" + content);
		String s1 = encrypt(content, KEY);
		System.out.println("s1:" + s1+"length:"+s1.length());
		System.out.println("s2:"+decrypt(s1, KEY));

	}
}
