/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.standard;

import com.security.PBKDF;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 *
 * @author Karol
 */
public class UserReg {
    
    
    private final String userkey;
    private final String username;
    private final String salt;
    private final String hashFun = PBKDF.SHA1_ALGORITHM;
    
    /**
     * Creates a new user with given password and username.
     * For storing the user in the database needed are:
     * username
     * salt - used for hashing a password
     * userkey - a hash of a password
     * hash function - to be able to know 
     *   which hash function was used for creating a hash,
     *   for now the only one is using SHA256 hash.
     * 
     * @param name
     * @param password 
     * @throws java.security.NoSuchAlgorithmException 
     * @throws java.security.spec.InvalidKeySpecException 
     */
    public UserReg(String name, char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        username = name;
        String hash = PBKDF.createHash(password, hashFun, 128 / 8);
        String[] params = hash.split(":");
        userkey = params[PBKDF.PBKDF2_INDEX];
        salt = params[PBKDF.SALT_INDEX];
    }
    
    public String getUserkey() {
        return userkey;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getSalt() {
        return salt;
    }
    
    public String getHashFun() {
        return hashFun;
    }
    
    
}
