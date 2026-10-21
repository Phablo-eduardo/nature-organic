package br.com.gerenciamento.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Util {
    private Util() {}

    public static String md5(String valor) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] hash = digest.digest(valor.getBytes(StandardCharsets.UTF_8));
        return String.format("%032x", new BigInteger(1, hash));
    }
}
