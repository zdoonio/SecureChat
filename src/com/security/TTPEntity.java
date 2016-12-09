/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
 * TODO : make encryption and decryption of messages
 */
public class TTPEntity implements CBCEncryptable{
    
    /**
     * Key length for the key used for necryption and decryption of messages.
     */
    public static final int SESSION_KEY_LEN = EnabledCiphers.AES_KEY_LEN_BYTES;
    
    /**
     * Algorithm used for encryption and decryption of messages.
     */
    public static final String CIPHER_ALGO = EnabledCiphers.AES_CBC;
    
    
    /**
     * Distributed key used for encrypting or decrypting 
     * the secret key used for communication. 
     * Currently only AES key. 
     */
    private SecretKey distrKey;
    
    /**
     * Generated session key. Alice's key.
     */
    private SecretKey generatedKey;
    
    /**
     * Decrypted session key. Bob's key.
     */
    private SecretKey decryptedKey;
    
    /**
     * Final session key agreed on by two entities, 
     * used for encryption and decryption of communication.
     */
    private SecretKey sessionKey;
    
    /**
     * Create an instance of TTPEntity with the algorithm and mode
     * for decrypting and encrypting of messages - communication between
     * this and another TTPEntity instance.
     * 
     */
    public TTPEntity() {
       
    }
    
    /**
     * To be used only with creating TTP instance.
     * @param key
     */
    protected void setDistrKey(SecretKey key) {
        distrKey = key;
    }
    
    /**
     * Generation of a session key.
     * If an instance is Alice, she can generate 
     * the session key and then agree on it. 
     * The session key depends on the algorithm chosen 
     * for decryption and encryption of messages.
     */
    public void generateAndAgreeOnSessionKey() {
        SecretKey key = generateKey(SESSION_KEY_LEN, "AES");
        generatedKey = key;
        agreeOnGeneratedKey(generatedKey);
    }
    
    
    /**
     * Encryption of a session key. 
     * This to be used only when an instance is Alice.
     * This encyption should be send to TTP.
     * 
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     */
    public byte[] encryptSessionKey() throws 
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, distrKey);
        byte[] sessionKeyByte = cipher.doFinal(sessionKey.getEncoded());
        return sessionKeyByte;
    }
    
    
    /**
     * Decryption of the session key.
     * This to be used only when an instance is Bob.
     * Encryption is received from TTP.
     * Then, because encrypted is session key, after decryption
     * instance agrees on the decrypted session key.
     * 
     * @param sessionKeyDec - the session key to be decrypted
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException 
     */
    public void decryptAndAgreeOnSessionKey(byte[] sessionKeyDec) throws 
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, distrKey);
        byte[] decrypted = cipher.doFinal(sessionKeyDec);
        SecretKey sk = new SecretKeySpec(decrypted, "AES");
        decryptedKey = sk;
        agreeOnDecryptedKey(decryptedKey);
    }
    
    private void agreeOnGeneratedKey(SecretKey key) {
        sessionKey = key;
    }
    
    private void agreeOnDecryptedKey(SecretKey key){
        sessionKey = key;
    }

    
    /**
     * Util for generating the random key 
     * of given keyLen and with given algorithm.
     * @param keyLen - the length of the key to be generated
     * @param algorithm - string representing algorithm to be used for generation
     * @return generated SecretKey
     */
    private static SecretKey generateKey(int keyLen, String algorithm) {
        SecureRandom random = new SecureRandom();
        byte[] keyByte = new byte[keyLen];
        random.nextBytes(keyByte);
        SecretKey key = new SecretKeySpec(keyByte, algorithm);
        return key;
    }

    public byte[] encrypt(String plaintext, IvParameterSpec iv) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, sessionKey, iv);
            byte[] encryption = cipher.doFinal(plaintext.getBytes());
            return encryption;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(TTPEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(TTPEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(TTPEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(TTPEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(TTPEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(TTPEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String decrypt(byte[] ciphertext, IvParameterSpec iv) {
        
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
            cipher.init(Cipher.DECRYPT_MODE, sessionKey, iv);
            byte[] plaintext = cipher.doFinal(ciphertext);
            return new String(plaintext);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(TTPEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(TTPEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(TTPEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(TTPEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(TTPEntity.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(TTPEntity.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
     
    }
    
    
    
}
