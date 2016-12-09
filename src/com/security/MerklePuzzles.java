/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.security;


import com.utils.ArrayUtils;
import com.utils.FileUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Karol
 */
public class MerklePuzzles {
    
    // Algorithms available for this puzzles.
    public static enum algorithms {DES, DESede, AES};
    
    // Chosen algorithm
    private String algorithm;
    
    // Prefix for encrypting a message, e.g. "Puzzle#"
    private final String prefix;

    // the key length for encryption (in bits) - the actual value 
    // for which the random key would be computed, e.g.
    // in case of AES it is 32 and rest 96 bits is occupied by zeros.
    private int encKeyLen;
    
    // The actual length of the secret key for encryption (in bits)
    // e.g. in case of aes it's 128
    private int secKeyLen;
    
    // Corresponding key lengths in bytes
    private int encKeyLenBytes;
    private int secKeyLenBytes;
    
    // An instance of cipher, used for encryption and decryption
    private Cipher cipher;
    
    // A secure random generator, used in chossing a random key
    private final SecureRandom random;
    
    // default name of the file for storing puzzles
    public static final String FILE_NAME = "puzzles.txt";
    
    // Standard prefix
    public static final String PREFIX = "Puzzle#";
    
    // final key to agree on
    private SecretKey sessionKey;

    
    /**
     * Default constructor, setting AES as an algorithm 
     * and prefix defined with the constant field PREFIX.
     */
    public MerklePuzzles() {
        this(algorithms.AES, null);
    }
    
    /**
     * Constructor with default prefix but chosen algorithm.
     * @param algo 
     */
    public MerklePuzzles(algorithms algo) {
        this(algo, null);
    }
    
    
    /**
     * Create an instance of merkle puzzles with a specified 
     * algorithm to be used for encryption and decryption, as well as
     * specified prefix to be used to encrypt the puzzles.
     * @param algo
     * @param pref 
     */
    public MerklePuzzles(algorithms algo, String pref) {
        try {
            switch(algo) {
                case DES :
                    setOptions("DES", 24, 68);
                    break;
                case AES :
                    setOptions("AES", 32, 128);
                    break;
                case DESede :
                    setOptions("DESede", 56, 192);
                    break;
                default :
                    setOptions("AES", 32, 128);
                    break;
            }
            
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(MerklePuzzles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(MerklePuzzles.class.getName()).log(Level.SEVERE, null, ex);
        }
        random = new SecureRandom();
        prefix = pref == null ? PREFIX : pref;
    }
    
    /**
     * Set algorithm and key lengths.
     * @param algorithm
     * @param encKeyLen
     * @param secKeyLen 
     */
    private void setOptions(String algorithm, int encKeyLen, int secKeyLen) {
        this.algorithm = algorithm;
        this.encKeyLen = encKeyLen;
        this.encKeyLenBytes = encKeyLen / 8;
        this.secKeyLen = secKeyLen;
        this.secKeyLenBytes = secKeyLen / 8;
    }
    
    /**
     * Create a random string. Used for creating a key. 
     * @param bytes - number of bytes on which the random string should be stored
     * @return the random string
     */
    public String randomString(int bytes) {
        String k = new BigInteger(10000, random)
                .toString(32)
                .substring(0, bytes);
        return k;
        
    }
    
    /**
     * Choose keys for encryption of the puzzles.
     * The keys are always in a form k || 0, where the length of
     * k is specified with the field encKeyLen, and actual length 
     * of 0 is encKeyLen - secKeyLen (in bits, while of course
     * we use here this lengths in bytes).
     * @return the SecretKey instance of a key 
     */
    public SecretKey randomEncKey() {
        byte[] preKey = randomString(encKeyLenBytes).getBytes();
        byte[] realKey = new byte[secKeyLenBytes];
        System.arraycopy(preKey, 0, realKey, 0, encKeyLenBytes);
        SecretKey sks = new SecretKeySpec(realKey, algorithm);
        return sks;
    }
    
    /**
     * Encrypt a message with a secret key. 
     * Used when constructing puzzles.
     * @param key      SecretKey used for encryption
     * @param message  message to be encrypted
     * @return an encryption of a message
     */
    public byte[] encrypt(SecretKey key, String message) {
        try {
            byte[] byteMsg = message.getBytes("UTF8");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] ciphertext = cipher.doFinal(byteMsg);
            return ciphertext;
        } catch (InvalidKeyException ex) {
            Logger.getLogger(MerklePuzzles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(MerklePuzzles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(MerklePuzzles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MerklePuzzles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    } 
    
    /**
     * Decrypt a ciphertext using a secret key. 
     * May be used for solving the puzzle.
     * @param key         the key for decryption
     * @param ciphertext  a ciphertext to decrypt
     * @return a decyption of a ciphertext
     * @throws java.security.InvalidKeyException
     * @throws javax.crypto.IllegalBlockSizeException
     * @throws javax.crypto.BadPaddingException
     * @throws java.io.UnsupportedEncodingException
     */
    public String decrypt(SecretKey key, byte[] ciphertext) throws 
            InvalidKeyException, IllegalBlockSizeException, 
            BadPaddingException, UnsupportedEncodingException {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decipherBytes = cipher.doFinal(ciphertext);
            String plaintext = new String(decipherBytes, "UTF8");
            return plaintext;
        
    }
    
    /**
     * Makes one puzzle - encrypted (prefix || secret key || public key).
     * @param key - encryption key
     * @return one encrypted message - a puzzle
     */
    public byte[] puzzle(SecretKey key) {
        String secretKey = randomString(secKeyLenBytes);
        String publicKey = randomString(secKeyLenBytes);
        String plainPuzzle = prefix + secretKey + publicKey;
        byte[] puzzle = encrypt(key, plainPuzzle);
        return puzzle;
    }
    
    /**
     * Make puzzles. Beware of the memory usage. 
     * 
     * @param key - to be used for encryption
     * @param numOfPuzzles - number of puzzles to create
     * @return instance of ArrayList containing puzzles - encrypted messages.
     */
    public ArrayList<byte[]> puzzles(SecretKey key, int numOfPuzzles) {
        ArrayList<byte[]> puzzles = new ArrayList<byte[]>(numOfPuzzles);
        for(int i = 0; i < numOfPuzzles; i++) {
            puzzles.add(i, puzzle(key));
        }
        return puzzles;
    }
    
    /**
     * Make puzzles and write them into a file.
     * @param key
     * @param numOfPuzzles
     * @param fileName 
     */
    public void puzzles(SecretKey key, int numOfPuzzles, String fileName) {
        
        String filename = fileName == null ? FILE_NAME : fileName;
        File file = null;
        PrintWriter pw = null;
        try {
            file = new File(filename);
            pw = new PrintWriter(file);
            
            for(int i = 0; i < numOfPuzzles; i++) {
                byte[] puzzle = puzzle(key);
                String puzzleStr = Arrays.toString(puzzle);
                pw.print(puzzleStr + System.lineSeparator());
            }
            
            pw.close();
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MerklePuzzles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * Get the algorithm to be used. Helps in solving.
     * @return 
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Get the prefix of the encrypted message.
     * @return 
     */
    public String getPrefix() {
        return prefix;
    }

    public void agreeOnKey(SecretKey secretKey, String publicKey, String fileName) throws 
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String filename = fileName == null ? FILE_NAME : fileName;
        File file = null;
        try {
            file = new File(filename);
            int numOfLines = FileUtils.countLines(file);
            int lineNum = 0;
            String decryptLine = null;
            boolean found = false;
            while(!found & (lineNum < numOfLines )) {
                String line = FileUtils.getLine(filename, lineNum);
                byte[] lineByte = ArrayUtils.parseBytes(line);
                decryptLine = decrypt(secretKey, lineByte);
                if(decryptLine
                        .substring(prefix.length() + secKeyLenBytes, decryptLine.length())
                        .equals(publicKey) ) found = true;
                lineNum += 1;
            }
            
            if(found) {
                String sessionKeyStr = decryptLine.substring(prefix.length(), 2*secKeyLenBytes - 1);
                sessionKey = new SecretKeySpec(sessionKeyStr.getBytes(), algorithm);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(MerklePuzzles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MerklePuzzles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public static void main(String args[]) {
        
        try {
            MerklePuzzles mp = new MerklePuzzles();
            SecretKey sk = mp.randomEncKey();
            ArrayList<byte[]> puzzles = mp.puzzles(sk, 3);
            byte[] puzzleByte = puzzles.get(0);
            String decryption = mp.decrypt(sk, puzzleByte);
            
        } catch (InvalidKeyException ex) {
            Logger.getLogger(MerklePuzzles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(MerklePuzzles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(MerklePuzzles.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MerklePuzzles.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
