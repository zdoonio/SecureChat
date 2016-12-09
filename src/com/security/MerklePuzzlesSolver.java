package com.security;

import com.utils.ArrayUtils;
import com.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;

/**
 *
 * @author Karol
 * TODO : implement a real agreement between two entities
 */
public class MerklePuzzlesSolver {
    
    /**
     * an instance of Merkle Puzzles to be used
     **/
    private final MerklePuzzles merklePuzzle;
    
    /**
     * Create a solver given an object of MerklePuzzle
     * @param mp - the MerklePuzzles object to be used for solving
    **/
    public MerklePuzzlesSolver(MerklePuzzles mp) {
        merklePuzzle = mp;
    }
    
    /**
     * Solve when having puzzles stored in an ArrayList
     * @param puzzles the ArrayList containing puzzles in byte tables
     * @return the byte table containig PublicKey 
     * to send for agreeing on a secret key
     */
    public String solve(ArrayList<byte[]> puzzles) {
        int puzzleNumber = chooseRandomPuzzle(puzzles.size());
        byte[] puzzle = puzzles.get(puzzleNumber);
        String solved = solve(puzzle);
        return solved;
    }
    
    /**
     * Solve when having puzzles stored in a file with a given filename.
     * @param filename the name of the file containing the puzzles
     * @return the byte table containing PublicKey 
     * to send for agreeing on a secret key
     */
    public String solve(String filename) {
        
        try {
            File file = new File(filename);
            int puzzleNumber = chooseRandomPuzzle(
                    FileUtils.countLines(file));
            byte[] puzzle = parsePuzzle(file, puzzleNumber);
            String solved = solve(puzzle);
            return solved;
        } catch (IOException ex) {
            Logger.getLogger(MerklePuzzlesSolver.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    /**
     * Solve having one puzzle. Method loops through possible 
     * randomly-generated keys to solve a puzzle.
     * @param puzzle a puzzle to solve
     * @return a PublicKey 
     */
    private String solve(byte[] puzzle) {
        String prefix = merklePuzzle.getPrefix();
        int prefixLen = prefix.length();
        boolean isSolved = false;
        String publicKey;
        String decryption = null;
        while(!isSolved) {
            try {
                SecretKey key = merklePuzzle.randomEncKey();
                decryption = merklePuzzle.decrypt(key, puzzle);
                isSolved = isSolved(decryption, prefix);
                
            } catch (InvalidKeyException ex) {
                isSolved = false;
            } catch (IllegalBlockSizeException ex) {
                isSolved = false;
            } catch (BadPaddingException ex) {
                isSolved = false;
            } catch (UnsupportedEncodingException ex) {
                isSolved = false;
            }
        }
        
        publicKey = solve(decryption, prefixLen);
        
        return publicKey == null ? null : publicKey;
    }
    
    /**
     * Check if the prefix of the decrypted message is the 
     * same as the prefix of MerklePuzzle instance. 
     * If so the decryption is ok and puzzle is solved.
     * @param decryption
     * @param prefix
     * @return boolean value if the puzzle is solved
     */
    private boolean isSolved(String decryption, String prefix) {
        boolean isSolved = false;
        String decPrefix = decryption.substring(0, prefix.length());
        if(decPrefix.equals(prefix)) isSolved = true;
        return isSolved;
    }
    
    /**
     * Get the public key out of decrypted puzzle.
     * @param decryption
     * @param prefixLen
     * @return 
     */
    private String solve(String decryption, int prefixLen) {
        if(decryption == null) return null;
        int decLen = decryption.length();
        int publicKeyLen = (decLen - prefixLen) / 2;
        String publicKey = decryption
                .substring(publicKeyLen + prefixLen, decLen);
        return publicKey;
    } 
    
    /**
     * Parse puzzle from a file. 
     * Because file contains values stored in bytes in form of a table, 
     * then it crucial to properly parse those byte values.
     * @param file
     * @param puzzleNumber
     * @return 
     */
    public byte[] parsePuzzle(File file, int puzzleNumber) {
        String line = FileUtils.getLine(file.getName(), puzzleNumber);
        byte[] puzzle = ArrayUtils.parseBytes(line);
        return puzzle;
    }
    
    /**
     * 
     * @param numberOfPuzzles
     * @return 
     */
    public int chooseRandomPuzzle(int numberOfPuzzles) {
        Random random = new Random();
        return random.nextInt(numberOfPuzzles);
        
    }
    
    public static void main(String[] args) {

        MerklePuzzles mp = new MerklePuzzles(MerklePuzzles.algorithms.DES);
        SecretKey sk = mp.randomEncKey();
        mp.puzzles(sk, 10, null);

        MerklePuzzlesSolver mps = new MerklePuzzlesSolver(mp);

        String solve = mps.solve("puzzles.txt");
        System.out.println(solve);


    }
    
}
