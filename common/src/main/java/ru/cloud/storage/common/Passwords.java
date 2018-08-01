package ru.cloud.storage.common;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class Passwords {
    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();

    //TODO подумать как лучше передавать пароля с клиента на сервер
    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = "MCygpawJsCpRtfOr".getBytes();

    //возвращает соль для хэширования пароля
    public static byte[] getNextSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    public static String getSaltedHash(char[] password) throws Exception {
        byte[] salt = getNextSalt();
        //Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return ENCODER.encodeToString(salt) + "$" + hash(password, salt);
    }

    //возвращает хэш пароля с солью
    private static String hash(char[] password, byte[] salt) throws Exception {
        if (password == null || password.length == 0)
            throw new IllegalArgumentException("Empty passwords are not supported.");

        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey key = f.generateSecret(spec);

        //Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();

        return ENCODER.encodeToString(key.getEncoded());
    }

    //проверяет соответствуют ли переданные пароль и соль хэшу
//    public static boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash) {
//        byte[] pwdHash = hash(password, salt);
//        Arrays.fill(password, Character.MIN_VALUE);
//        if (pwdHash.length != expectedHash.length) return false;
//        for (int i = 0; i < pwdHash.length; i++) {
//            if (pwdHash[i] != expectedHash[i]) return false;
//        }
//        return true;
//    }
    public static boolean check(String password, String stored) throws Exception {
        String[] saltAndPassword = stored.split("\\$");
        if (saltAndPassword.length != 2) {
//            throw new IllegalStateException(
//                    "The stored password must have the form 'salt$hash'");
            return false;
        }
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String hashOfInput = hash(password.toCharArray(), decoder.decode(saltAndPassword[0]));
        return hashOfInput.equals(saltAndPassword[1]);
    }

    public static boolean isPasswordsEquals(byte[] pwdHash, byte[] dbPwdHash) {
        if (pwdHash.length != dbPwdHash.length) return false;
        for (int i = 0; i < pwdHash.length; i++) {
            if (pwdHash[i] != dbPwdHash[i]) return false;
        }
        return true;
    }

    public static byte[] encrypt(byte[] plainText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(plainText);
    }

    public static byte[] decrypt(byte[] cipherText) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return cipher.doFinal(cipherText);
    }
}
