
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

    /**
     * Método para convertir la cadena de clave en una clave AES.
     * @param keyString La cadena que se utilizará como clave.
     * @return La clave secreta AES generada.
     * @throws Exception Si ocurre un error al generar la clave.
     */
    private SecretKeySpec createKey(String keyString) throws Exception {
        byte[] key = keyString.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // Utilizar solo los primeros 128 bits (16 bytes) para AES-128
        return new SecretKeySpec(key, "AES");
    }

    /**
     * Método para encriptar una cadena de texto utilizando AES.
     * @param data La cadena de texto a encriptar.
     * @return La cadena encriptada en formato Base64.
     * @throws Exception Si ocurre un error durante la encriptación.
     */
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

    /**
     * Método para desencriptar una cadena encriptada con AES.
     * @param encryptedData La cadena encriptada en formato Base64.
     * @return La cadena desencriptada.
     * @throws Exception Si ocurre un error durante la desencriptación.
     */
    public String decrypt(String encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Decodificar la cadena Base64
        byte[] combined = Base64.getDecoder().decode(encryptedData);

        // Verificar la longitud del array combinado
        if (combined.length < 16 || (combined.length - 16) % 16 != 0) {
            throw new IllegalArgumentException("La longitud del array combinado no es válida para CBC con AES");
        }

        // Extraer el IV de los primeros 16 bytes
        byte[] iv = Arrays.copyOfRange(combined, 0, 16);

        // Extraer los datos cifrados
        byte[] encryptedBytes = Arrays.copyOfRange(combined, 16, combined.length);

        // Inicializar el cipher para desencriptar
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        // Convertir los bytes desencriptados a cadena UTF-8
        return new String(decryptedBytes, "UTF-8");
    }
    
    
    
}
