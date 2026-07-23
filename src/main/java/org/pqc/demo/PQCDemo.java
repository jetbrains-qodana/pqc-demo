package org.pqc.demo;

import org.pqc.demo.complexCipherPackage.CipherConfiguration;
import org.pqc.demo.complexSslContextPackage.SslContextWrapper;
import org.pqc.demo.complexSslContextPackage.SslProtocolNames;

import javax.crypto.*;
import javax.net.ssl.SSLParameters;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class PQCDemo {

    private static void obtainSignature(String algorithm) throws NoSuchAlgorithmException {
        Signature.getInstance(algorithm);
    }

    static void main() throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException,
            CertificateException, IOException {
        // Cryptography Algorithm Detections
        Signature.getInstance("Ed25519"); // pre-quantum

        // taint suport
        obtainSignature("ECDSA"); // pre-quantum
        obtainSignature(AlgorithmConstants.SHA_256_WITH_RSA); // pre-quantum

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
        int randomNumber = System.in.readAllBytes().length;
        String keyTransportAlgorithm;
        if (randomNumber < 10) {
            keyTransportAlgorithm = "ML-KEM-512"; // level 1
        } else if (randomNumber < 100) {
            keyTransportAlgorithm = "ML-KEM-768"; // level 3
        } else {
            keyTransportAlgorithm = "ML-KEM-1024"; // level 5
        }
        KEM.getInstance(keyTransportAlgorithm);

        // Ciphers
        String cipher = CipherConfiguration.obtainConfiguredCipher();
        Cipher.getInstance(cipher);

        // Additional detections for SSL Protocols
        SslContextWrapper.getTlsSslContext(SslProtocolNames.TLS12).createSSLEngine(); // pre-quantum
        SslContextWrapper.getTlsSslContext(SslProtocolNames.TLS13).createSSLEngine(); // compliant

        SSLParameters sslParameters = new SSLParameters(new String[]{"TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256"});
        // explicitly specifying cipher suites in SSLParameters constructor can break post-quantum compliance

        sslParameters.setNamedGroups(new String[]{"X25519"});
        sslParameters.setSignatureSchemes(new String[]{"ecdsa_secp256r1_sha256"});
        // Using certain setters of SSLParameters can break post-quantum compliance

        // Usage of custom cryptography providers
        KDF.getInstance("HKDF-SHA256", "MyProvider");
        CertificateFactory.getInstance("X.509", "MyProvider");
        // lock-in. Better rely on a default provider for reliable security
    }
}
