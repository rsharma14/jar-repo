package com.stockmanagement.util;



import java.util.Base64;

import org.springframework.stereotype.Component;
@Component
public class DecryptionEncryptionUtil {

	private static String dbencryptkey="encKey";
	
    static Base64.Encoder encoder = Base64.getEncoder();  
    static Base64.Decoder decoder = Base64.getDecoder();  
    // Decoding string  
	public static String encodingText(String text) {
		String encyptText = null;
		try {
				encyptText = encoder.encodeToString(text.getBytes());  ;

			
		} catch (Exception e) {
		}
		return encyptText;
	}

	public static String decodingText(String text)
			throws Exception {
		String decryptText = null;
		try {						
			decryptText = new String(decoder.decode(text));  
;			
		} catch (Exception e) {
		}
		return decryptText;
	}
}
