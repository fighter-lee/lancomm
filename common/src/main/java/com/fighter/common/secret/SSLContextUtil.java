package com.fighter.common.secret;

import com.fighter.common.FileUtil;
import com.fighter.common.Trace;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import androidx.annotation.NonNull;

/**
 * 输入.bks或.cer,.crt证书生成SSLContext
 * Created by fighter_lee on 19/12/05.
 */
public class SSLContextUtil {

    public static class Builder {
        KeyStore bksKeyStore;
        KeyStore crtKeyStore;

        private Builder() {
            try {
                bksKeyStore = KeyStore.getInstance("BKS");
                crtKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
        }

        public void addCRT(@NonNull InputStream inputStream) throws NoSuchProviderException, CertificateException, KeyStoreException {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", "BC");
            crtKeyStore.setCertificateEntry(Md5Util.md5sum(inputStream.toString()),
                    certificateFactory.generateCertificate(inputStream));
        }

        public void addCRT(@NonNull String certificatePath) {
            InputStream inputStream = null;
            try {
                inputStream = new FileInputStream(certificatePath);
                addCRT(inputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } finally {
                FileUtil.closeQuietly(inputStream);
            }
        }

        public void addBKS(@NonNull String certificatePath, @NonNull String password) {
            InputStream inputStream = null;
            try {
                // 获得密钥库
                inputStream = new FileInputStream(certificatePath);
                bksKeyStore.load(inputStream, password.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
                Trace.e("Builder", "addBks() e = " + e);
            } finally {
                FileUtil.closeQuietly(inputStream);
            }
        }

        public SSLContext build() {
            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance("TLS");

                KeyManagerFactory keyManagerFactory =
                        KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keyManagerFactory.init(bksKeyStore, "".toCharArray());

                TrustManagerFactory trustManagerFactory =
                        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(crtKeyStore);

                sslContext.init(keyManagerFactory.getKeyManagers(),
                        trustManagerFactory.getTrustManagers(),
                        new SecureRandom()
                );
            } catch (Exception e) {
                e.printStackTrace();
                Trace.e("Builder", "build() e = " + e);
            }
            return sslContext;
        }

    }

    public static Builder build() {
        return new Builder();
    }
}

