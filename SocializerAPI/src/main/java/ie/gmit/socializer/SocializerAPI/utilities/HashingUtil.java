package ie.gmit.socializer.SocializerAPI.utilities;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.commons.lang3.ArrayUtils;

public class HashingUtil {
    
    private static final int HASHING_VERSION = 100;
    private static final int PASSWORD_KEY_LENGTH = 512;
    private static final String PASSWORD_HASHING_ALGO = "PBKDF2WithHmacSHA512";

    /**
     * Create rawPassword hash with headers
     * 
     * @param rawPassword - raw rawPassword characters
     * @return the rawPassword hash bytes
     */
    public static byte[] createPassword(final char[] rawPassword){
        byte[] salt = SecurityUtil.getSecureBytes(SecurityUtil.getRandomIntInRange(16, 128));
        int iterations = SecurityUtil.getRandomIntInRange(4096, 32768);
        byte[] passwordHash = hashPassword(rawPassword, salt, iterations);
        
        //Create rawPassword header
        byte[] saltLength = ByteBuffer.allocate(Integer.BYTES).putInt(salt.length).array();
        byte[] iterationBytes = ByteBuffer.allocate(Integer.BYTES).putInt(iterations).array();
        byte[] crc32Bytes = ByteBuffer.allocate(Long.BYTES).putLong(
                            calculateCrc32(new byte[]{HASHING_VERSION}, saltLength, salt, iterationBytes, passwordHash)
                        ).array();
        
        return SecurityUtil.addAll(crc32Bytes, new byte[]{HASHING_VERSION}, saltLength, salt, iterationBytes, passwordHash);
    }
    
    /**
     * Validate password against stored hash 
     * 
     * @param passwordHash - the stored hash bytes
     * @param password - the raw password chars
     * @return 
     */
    public static boolean validatePassword(final byte[] passwordHash, final char[] password){
        int currentPosition = 0;
        long crc32 = ByteBuffer.wrap(ArrayUtils.subarray(passwordHash, currentPosition, Long.BYTES)).getLong();
        currentPosition += Long.BYTES;
        long dataCrc = calculateCrc32(ArrayUtils.subarray(passwordHash, currentPosition, passwordHash.length));
        
        int version = (int)passwordHash[currentPosition];
        currentPosition++;
        
        int saltSize = ByteBuffer.wrap(ArrayUtils.subarray(passwordHash, currentPosition, currentPosition + Integer.BYTES)).getInt();
        currentPosition += Integer.BYTES;
        
        byte[] salt = ArrayUtils.subarray(passwordHash, currentPosition, currentPosition + saltSize);
        currentPosition += salt.length;
        
        int iterations = ByteBuffer.wrap(ArrayUtils.subarray(passwordHash, currentPosition, currentPosition + Integer.BYTES)).getInt();
        currentPosition += Integer.BYTES;
        
        byte[] storedHash = ArrayUtils.subarray(passwordHash, currentPosition, passwordHash.length);
        
        if(crc32 != dataCrc || HASHING_VERSION != version){
            Logger.getLogger(HashingUtil.class.getName()).log(Level.SEVERE, null, "Password hash is corrupted or invalid version");
            return false;
        }
        
        return Arrays.equals(storedHash, hashPassword(password, salt, iterations));
    }
    
    /**
     * Create a rawPassword hash
     * 
     * @param rawPassword - raw rawPassword chars
     * @param salt - random bytes
     * @param iterations - the iteration count for current hash 
     * @return rawPassword hash bytes
     */
    private static byte[] hashPassword(final char[] rawPassword, final byte[] salt, final int iterations) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PASSWORD_HASHING_ALGO);
            KeySpec keySpec = new PBEKeySpec(rawPassword, salt, iterations, PASSWORD_KEY_LENGTH);
            SecretKey secretKey = skf.generateSecret(keySpec);
            
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Logger.getLogger(HashingUtil.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Calculate CRC32
     * 
     * @param initalData
     * @param content - the data to sign
     * @return the checksum
     */
    public static long calculateCrc32(final byte[] initalData, final byte[]... content) {
        Checksum checksum = new CRC32();
        
        checksum.update(initalData, 0, initalData.length);
        
        for(byte[] c : content)
            checksum.update(c, 0, c.length);
        
        return checksum.getValue();
    }
}

