package org.gmssl.java;

//import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;


public class SM4Util
{
	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	private String secretKey = "";

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

	private String iv = "";

	public SM4Util()
	{
	}

	public String encryptData_ECB(String plainText)
	{
		try
		{
			SM4Context ctx = new SM4Context();
			ctx.mode = SM4Constant.SM4_ENCRYPT;

			byte[] keyBytes;
			if (ctx.isHexReturn)
			{
				keyBytes = ConvertUtil.hexStringToBytes(secretKey);
			}
			else
			{
				keyBytes = secretKey.getBytes();
			}

			SM4 sm4 = new SM4();
			sm4.sm4_setkey_enc(ctx, keyBytes);
			byte[] encrypted = sm4.sm4_crypt_ecb(ctx, plainText.getBytes("GBK"));

			String cipherText = Base64.encodeBase64String(encrypted);
			if (cipherText != null && cipherText.trim().length() > 0)
			{
				Pattern p = Pattern.compile("\\s*|\t|\r|\n");
				Matcher m = p.matcher(cipherText);
				cipherText = m.replaceAll("");
			}
			return cipherText;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String decryptData_ECB(String cipherText)
	{
		try
		{
			SM4Context ctx = new SM4Context();
			ctx.mode = SM4Constant.SM4_DECRYPT;

			byte[] keyBytes;
			if (ctx.isHexReturn)
			{
				keyBytes = ConvertUtil.hexStringToBytes(secretKey);
			}
			else
			{
				keyBytes = secretKey.getBytes();
			}

			SM4 sm4 = new SM4();
			sm4.sm4_setkey_dec(ctx, keyBytes);
			byte[] decrypted = sm4.sm4_crypt_ecb(ctx, Base64.decodeBase64(cipherText));
			return new String(decrypted, "GBK");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String encryptData_CBC(String plainText)
	{
		try
		{
			SM4Context ctx = new SM4Context();
			ctx.mode = SM4Constant.SM4_ENCRYPT;

			byte[] keyBytes;
			byte[] ivBytes;
			if (ctx.isHexReturn)
			{
				keyBytes = ConvertUtil.hexStringToBytes(secretKey);
				ivBytes = ConvertUtil.hexStringToBytes(iv);
			}
			else
			{
				keyBytes = secretKey.getBytes();
				ivBytes = iv.getBytes();
			}

			SM4 sm4 = new SM4();
			sm4.sm4_setkey_enc(ctx, keyBytes);
			byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, plainText.getBytes("GBK"));

			String cipherText = Base64.encodeBase64String(encrypted);
			if (cipherText != null && cipherText.trim().length() > 0)
			{
				Pattern p = Pattern.compile("\\s*|\t|\r|\n");
				Matcher m = p.matcher(cipherText);
				cipherText = m.replaceAll("");
			}
			return cipherText;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String decryptData_CBC(String cipherText)
	{
		try
		{
			SM4Context ctx = new SM4Context();
			ctx.mode = SM4Constant.SM4_DECRYPT;

			byte[] keyBytes;
			byte[] ivBytes;
			if (ctx.isHexReturn)
			{
				keyBytes = ConvertUtil.hexStringToBytes(secretKey);
				ivBytes = ConvertUtil.hexStringToBytes(iv);
			}
			else
			{
				keyBytes = secretKey.getBytes();
				ivBytes = iv.getBytes();
			}

			SM4 sm4 = new SM4();
			sm4.sm4_setkey_dec(ctx, keyBytes);
			byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, Base64.decodeBase64(cipherText));
			return new String(decrypted, "GBK");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}

