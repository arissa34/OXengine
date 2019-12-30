package rma.ox.engine.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    public static String convertToMD5(String input) throws Exception {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(Integer.toHexString(0xFF & b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean matching(String orig, String compare){
        try{
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(compare.getBytes());
            String md5 = new BigInteger(1, digest.digest()).toString(16);
            return md5.equals(orig);

        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }
}
