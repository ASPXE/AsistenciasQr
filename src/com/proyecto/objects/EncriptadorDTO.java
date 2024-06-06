
package com.proyecto.objects;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author aspxe
 */
public class EncriptadorDTO {
    
    private SecretKeySpec key;
    private String clave = "YaPaguenPiojos.l.";

    public EncriptadorDTO() throws Exception {
        this.key = createKey(this.clave);
    }

    // Método para convertir una cadena a una clave AES
    private SecretKeySpec createKey(String keyString) throws Exception {
        byte[] key = keyString.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 32); // Utilizar solo los primeros 256 bits (32 bytes) para AES-256
        return new SecretKeySpec(key, "AES");
    }

    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
    
    
    
}
