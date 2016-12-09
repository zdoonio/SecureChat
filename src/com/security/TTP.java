/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.security;


import com.utils.HexUtils;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Karol
 */
public class TTP {
    
    /**
     * Alice AES key. 
     * Used for decrypting the encrypted session key received from Alice.
     */
    private final SecretKey aliceKey;
    
    /**
     * Bob AES key. 
     * Used for ecnrypting the session key, before sending to Bob.
     */
    private final SecretKey bobKey;
    
    /**
     * Session key used for encryption of messages.
     * This key can be any Cipher key.
     */
    private SecretKey sessionKey;
    
    
    
    public TTP(TTPEntity Alice, TTPEntity Bob, String password, String hash_algo) throws 
            NoSuchAlgorithmException, InvalidKeySpecException {
        Alice.setDistrKey(aliceKey = generateKey(password, hash_algo));
        Bob.setDistrKey(bobKey = generateKey(password, hash_algo));
    }
    
    public void decryptSessionKey(byte[] ciphertext) throws 
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aliceKey);
        byte[] sessionKeyByte = cipher.doFinal(ciphertext);
        sessionKey = new SecretKeySpec(sessionKeyByte, "AES");
    }
    
    public byte[] encryptSessionKey() throws 
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, bobKey);
        byte[] cipherByte = cipher.doFinal(sessionKey.getEncoded());
        return cipherByte;
    }
    
    /**
     * Generates AES128 key used for encryption of secret key.
     * This is key generated for both Alice and Bob, separately.
     * The password can be hash value retrieved from database.
     * @return 
     */
    private SecretKey generateKey(String password, String hash_algo) throws 
            NoSuchAlgorithmException, InvalidKeySpecException {
        
        String keyhash = PBKDF.createHash(password, hash_algo, 
                EnabledCiphers.AES_KEY_LEN_BYTES);
        byte[] key = HexUtils.fromHex((keyhash.split(":"))[PBKDF.PBKDF2_INDEX]);
        SecretKey sk = new SecretKeySpec(key, EnabledCiphers.AES);
        return sk;
    }
    
    public static void main(String[] args) {
        
        try {
            
            int aesBlockSize = IvGenerator.AES_BLOCK_SIZE;
            
            String password = "To jest moje haslo";
            
            TTPEntity Alice = new TTPEntity();
            TTPEntity Bob = new TTPEntity();
            
            TTP ttp = new TTP(Alice, Bob, password, PBKDF.SHA1_ALGORITHM);
            
            // Alice generates the key and agrees on it.
            Alice.generateAndAgreeOnSessionKey();
            
            // Alice decrypt the key and sends it to TTP
            byte[] encryptSessionKey = Alice.encryptSessionKey();
            
            // TTP decrypts it then encrypts with bob key and sends to Bob.
            ttp.decryptSessionKey(encryptSessionKey);
            byte[] encryptSessionKeyBob = ttp.encryptSessionKey();
            
            // Bob decrypts it and agress on it
            Bob.decryptAndAgreeOnSessionKey(encryptSessionKeyBob);
            
            // Now they both have the same session key. 
            // We can encrypt and decrypt a message.
            String message = "how are you Alice? ";
            
            IvParameterSpec ivM = IvGenerator.generateIV(aesBlockSize);
            
            byte[] encrypted = Bob.encrypt(message, ivM);
            String decrypted = Alice.decrypt(encrypted, ivM);
            
            System.out.println(decrypted);
            
            
                    
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(TTP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(TTP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(TTP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(TTP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(TTP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(TTP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(TTP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
