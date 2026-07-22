package org.pqc.demo;

import javax.crypto.*;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class PQCDemo {
    private static void sign(String algorithm) throws NoSuchAlgorithmException {
        Signature.getInstance(algorithm);
    }

    static void main() throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException, CertificateException {
        // Cryptography Algorithm Detections
        Signature.getInstance("Ed25519"); // pre-quantum

        // taint suport
        sign("ECDSA"); // pre-quantum
        sign(AlgorithmConstants.RSA); // pre-quantum

        // detect algorithms not satisfying an NIST post-quantum cryptography security level
        Signature.getInstance("ML-DSA-44"); // level 2
        Signature.getInstance("ML-DSA-65"); // level 3
        Signature.getInstance("ML-DSA-87"); // level 5
        Signature.getInstance("HSS/LMS"); // level 5

        // Primitives supported apart from Signatures:

        // Hashes:
        MessageDigest.getInstance("MD5"); // pre-quantum
        Mac.getInstance("HmacSHA1"); // level 1
        MessageDigest.getInstance("SHAKE128-256"); // level 2
        MessageDigest.getInstance("SHA-224"); // level 3
        KDF.getInstance("HKDF-SHA512"); // level 5

        // Key Agreement/Key Transport
        KeyAgreement.getInstance("DiffieHellman"); // pre-quantum
        KEM.getInstance("ML-KEM-512"); // level 1
        KEM.getInstance("ML-KEM-768"); // level 3
        KEM.getInstance("ML-KEM-1024"); // level 5

        // Ciphers
        Cipher.getInstance("RSA"); // pre-quantum
        Cipher.getInstance("AES_128"); // level 1
        Cipher.getInstance("AES_192"); // level 3
        Cipher.getInstance("AES_256"); // level 5


        // Additional detections for SSL Protocols
        SSLContext.getInstance("TLSv1.2"); // pre-quantum
        SSLContext.getInstance("TLSv1.3"); // compliant

        SSLParameters sslParameters = new SSLParameters(new String[]{"TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256"});
        // explicitly specifying cipher suites in SSLParameters constructor can break post-quantum compliance

        sslParameters.setNamedGroups(new String[]{"X25519"});
        sslParameters.setSignatureSchemes(new String[]{"ecdsa_secp256r1_sha256"});
        // Using certain setters of SSLParameters can break post-quantum compliance


        // Usage of custom cryptography providers
        KDF.getInstance("HKDF-SHA256", "MyProvider");
        CertificateFactory.getInstance("X.509", "MyProvider");
        // lock-in. Better rely on default provider for reliable security
    }
}
