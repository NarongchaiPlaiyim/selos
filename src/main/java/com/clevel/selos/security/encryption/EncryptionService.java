package com.clevel.selos.security.encryption;

import com.clevel.selos.system.Config;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidParameterSpecException;

@ApplicationScoped
public class EncryptionService {
    @Inject
    Logger log;

    private volatile static SecretKey secretKey;
    private static final String KEY_FILE_PASSWORD = "selos";

    @Inject
    @Config(name = "system.encryption.keyFile")
    String keyFile;

    @Inject
    public EncryptionService() {
    }

    @PostConstruct
    public void onCreation() {
        loadKey();
    }

    public void loadKey() {
        if (secretKey==null) {
            try {
                secretKey = loadKey(keyFile,KEY_FILE_PASSWORD);
                log.debug("loading key file successful.");
            } catch (KeyStoreException e) {
                log.error("loading key file failed!",e);
            } catch (IOException e) {
                log.error("loading key file failed!",e);
            } catch (NoSuchAlgorithmException e) {
                log.error("loading key file failed!",e);
            } catch (UnrecoverableEntryException e) {
                log.error("loading key file failed!",e);
            }
        }
    }

    public SecretKey loadKey(String keyFile,String password) throws KeyStoreException, IOException, NoSuchAlgorithmException, UnrecoverableEntryException {
        log.debug("loadKey. (keyFile: {})",keyFile);

        KeyStore ks = KeyStore.getInstance("BKS");
        FileInputStream fis = new FileInputStream(keyFile);
        try {
            ks.load(fis, password.toCharArray());
        } catch (CertificateException e) {
            log.error("",e);
        }

        KeyStore.SecretKeyEntry skEntry = (KeyStore.SecretKeyEntry) ks.getEntry("secretKey",new KeyStore.PasswordProtection(password.toCharArray()));
        SecretKey secretKey = skEntry.getSecretKey();

        fis.close();
        return secretKey;
    }

    public byte[] encrypt(String plaintext) {
        if (plaintext==null) return "".getBytes();

        log.debug("encrypt.",plaintext);
        byte[] data = "".getBytes();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding","BC");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] cipherText = cipher.doFinal(plaintext.getBytes("UTF-8"));
            data = ArrayUtils.addAll(iv, cipherText);
            log.debug("encryption successful.");
        } catch (NoSuchAlgorithmException e) {
            log.error("encryption failed!", e);
        } catch (NoSuchPaddingException e) {
            log.error("encryption failed!", e);
        } catch (InvalidKeyException e) {
            log.error("encryption failed!", e);
        } catch (InvalidParameterSpecException e) {
            log.error("encryption failed!", e);
        } catch (IllegalBlockSizeException e) {
            log.error("encryption failed!", e);
        } catch (BadPaddingException e) {
            log.error("encryption failed!", e);
        } catch (UnsupportedEncodingException e) {
            log.error("encryption failed!", e);
        } catch (NoSuchProviderException e) {
            log.error("encryption failed!", e);
        }
        return data;
    }

    public String decrypt(byte[] data) {
        if (data==null) return "";

        log.debug("decrypt.");
        String text = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding","BC");
            byte[] iv = ArrayUtils.subarray(data,0,16);
            byte[] data2 = ArrayUtils.subarray(data,16,data.length);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            text = new String(cipher.doFinal(data2), "UTF-8");
            log.debug("decryption successful.");
        } catch (NoSuchAlgorithmException e) {
            log.error("decryption failed!",e);
        } catch (NoSuchPaddingException e) {
            log.error("decryption failed!",e);
        } catch (InvalidKeyException e) {
            log.error("decryption failed!",e);
        } catch (InvalidAlgorithmParameterException e) {
            log.error("decryption failed!",e);
        } catch (UnsupportedEncodingException e) {
            log.error("decryption failed!",e);
        } catch (IllegalBlockSizeException e) {
            log.error("decryption failed!",e);
        } catch (BadPaddingException e) {
            log.error("decryption failed!",e);
        } catch (NoSuchProviderException e) {
            log.error("decryption failed!", e);
        }
        return text;
    }

}
