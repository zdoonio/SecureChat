/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.security;

import java.security.InvalidKeyException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;

/**
 * Represents one entity of MerklePuzzleKeyAgreement. 
 * The second entity is an instance of MerklePuzzlesSolver
 * @author Karol
 */
public class MerklePuzzlesKeyAgr {
    
    
    private MerklePuzzles mp;
    private MerklePuzzlesSolver mps;
    
    private SecretKey sessionKey;
    
    
    public MerklePuzzlesKeyAgr(MerklePuzzles.algorithms algorithm) {
        mp = new MerklePuzzles(algorithm);
        mps = new MerklePuzzlesSolver(mp);
    }
    
    
    
    public static void main(String[] args) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        MerklePuzzles mp = new MerklePuzzles(MerklePuzzles.algorithms.DES);
        SecretKey sk = mp.randomEncKey();
        mp.puzzles(sk, 10, null);

        MerklePuzzlesSolver mps = new MerklePuzzlesSolver(mp);

        String solve = mps.solve("puzzles.txt");
        System.out.println(solve);
        mp.agreeOnKey(sk, solve, "puzzles.txt");
    }
    
}
