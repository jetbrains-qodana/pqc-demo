package org.pqc.demo.complexSslContextPackage;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

public class SslContextWrapper {
    public static SSLContext getTlsSslContext(String protocol) throws NoSuchAlgorithmException {
        return SSLContext.getInstance(protocol);
    }
}
