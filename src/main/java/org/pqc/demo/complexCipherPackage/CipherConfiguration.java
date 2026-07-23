package org.pqc.demo.complexCipherPackage;

public class CipherConfiguration {
    /**
     * Get a cipher algorithm from some configuration. We simulate this by generating a random number
     */
    public static String obtainConfiguredCipher() {
        int arbitraryConfiguration = (int) Math.floor(Math.random() * 4);
        CIPHER cipher = CIPHER.values()[arbitraryConfiguration];
        return cipherFromCipherEnum(cipher);
    }

    private static String cipherFromCipherEnum(CIPHER cipher) {
        return switch (cipher) {
            case RSA -> "RSA"; // pre-quantum
            case AES_128 -> "AES_128"; // level 1
            case AES_192 -> "AES_192"; // level 3
            case AES_256 -> "AES_256"; // level 5
        };
    }
}
