package ie.gmit.socializer.SocializerAPI.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

public class SecurityUtil {
    private static final String RANDOM_SOURCE = "SHA1PRNG";
    private static final Map<Integer, Integer> EXCLUSIONS = new HashMap<Integer, Integer>() {
        {
            put(92, 92);//include in settings
        }
    };

    /**
     * Get an initialized SecureRandom instance
     *
     * @return SecureRandom
     */
    public static SecureRandom getSecureRandom() {
        try {
            return SecureRandom.getInstance(RANDOM_SOURCE);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Invalid environment", ex);
        }
    }

    /**
     * Get secure random bytes
     *
     * @param length - number of bytes to generate
     * @return byte[] with secure bytes
     */
    public static byte[] getSecureBytes(int length) {
        SecureRandom secRandom = getSecureRandom();
        byte[] bytes = new byte[length];
        secRandom.setSeed(secRandom.nextLong());
        secRandom.nextBytes(bytes);

        return bytes;
    }

    /**
     * Generate a random int in range
     *
     * @param min - the lower bound
     * @param max - the upper bound
     * @return the generated random integer
     */
    public static int getRandomIntInRange(int min, int max) {
        SecureRandom secRandom = getSecureRandom();

        return secRandom.nextInt((max - min) + min);
    }

    /**
     * Generate a secure random big integer
     * @return 
     */
    public static BigInteger getRandomBigInt(){
        return new BigInteger(128, getSecureRandom());
    }

    /**
     * Generate secure IV based on algorithm spec
     *
     * @param cipherAlgorithmName - the name of the algorithm to use eg: "AES"
     * @return
     */
    public static IvParameterSpec generateSecureIV(String cipherAlgorithmName) {
        try {
            byte[] iv = getSecureBytes(Cipher.getInstance(cipherAlgorithmName).getBlockSize());

            return new IvParameterSpec(iv);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            throw new RuntimeException("Invalid environment, check max key size", ex);
        }
    }

    /**
     * Generate secure password based on the asci table
     *
     * @param length - minimum 15 maximum 256 char length
     * @return
     */
    public static char[] generateSecurePassword(final int length) {
        if (length < 15 || length > 256) {
            throw new RuntimeException("Password has to be between 15 and 256 chars long");
        }

        char[] password = new char[length];

        for (int i = length - 1; i >= 0; i--) {
            if (i == length - 1 || i == 0) {
                password[i] = (char) getAsciiValueWithExclusions(33);//dont use spaces at the end and beginning
            } else {
                password[i] = (char) getAsciiValueWithExclusions(32);
            }
        }

        return password;
    }
    
    /**
     * Generate a random integer, in range with exclusions
     *
     * Range from 33 to 127
     *
     * @return random integer
     */
    private static int getAsciiValueWithExclusions(int lowerBound) {

        SecureRandom secRandom = getSecureRandom();
        int num;
        do {
            num = (secRandom.nextInt((127 - lowerBound)) + lowerBound);//use acsii chars between 32 and 126
            if (!EXCLUSIONS.containsKey(num)) {
                return num;
            }
        } while (true);
    }
    
    /**
     * Add byte array together
     * @param bytes
     * @return 
     */
    public static byte[] addAll(byte[]... bytes){
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            for(byte[] b : bytes)
                byteStream.write(b);
             
            return byteStream.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(SecurityUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
