
package com.proyecto.objects;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author aspxe
 */
public class EncriptadorDTO {
    
    private SecretKeySpec key;
    private String clave = "YaPaguenPiojos.l."; // Clave de encriptación

    public EncriptadorDTO() throws Exception {
        this.key = createKey(this.clave);
    }

    private SecretKeySpec createKey(String keyString) throws Exception {
        byte[] key = keyString.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // Utilizar solo los primeros 128 bits (16 bytes) para AES-128
        return new SecretKeySpec(key, "AES");
    }

    public String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // Modo CBC y relleno PKCS5Padding
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = cipher.getIV(); // Obtener el IV generado
        byte[] encryptedBytes = cipher.doFinal(data.getBytes("UTF-8"));
        byte[] combined = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    private String sanitizeBase64String(String base64String) {
        return base64String.replaceAll("[^a-zA-Z0-9+/=]", "").trim();
    }

    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Sanear la cadena Base64
        byte[] combined = Base64.getDecoder().decode(sanitizeBase64String(encryptedData));

        if (combined.length < 16 || (combined.length - 16) % 16 != 0) {
            throw new IllegalArgumentException("La longitud del array combinado no es válida para CBC con AES");
        }

        byte[] iv = Arrays.copyOfRange(combined, 0, 16);
        byte[] encryptedBytes = Arrays.copyOfRange(combined, 16, combined.length);

        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, "UTF-8");
    }
    
    public static void main(String[] args) {
        try {
            EncriptadorDTO encriptador = new EncriptadorDTO();
            String originalText = "Texto a encriptar";
            String encryptedText = encriptador.encrypt(originalText);
            System.out.println("Encrypted: " + encryptedText);
            String decryptedText = encriptador.decrypt(encryptedText);
            System.out.println("Decrypted: " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
}
