package com.example.lab3decryptor;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.security.*;
import java.security.spec.*;
import java.util.ArrayList;
import java.util.List;

public class MyDecryptor {
    private final Cipher cipher;
    private final SecretKeySpec key;
    private AlgorithmParameterSpec spec;

    public MyDecryptor() throws Exception {
        String cipher_password = "jndlasf074hr";
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(cipher_password.getBytes("UTF-8"));
        byte[] keyBytes = new byte[32];
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);

        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        key = new SecretKeySpec(keyBytes, "AES");
        spec = getIV();
    }

    public AlgorithmParameterSpec getIV() {
        return new IvParameterSpec(new byte[16]);
    }

    public void decrypt(String paramString1, String paramString2) throws Exception {
        FileInputStream localFileInputStream = new FileInputStream(paramString1);
        FileOutputStream localFileOutputStream = new FileOutputStream(paramString2);
        this.cipher.init(2, this.key, this.spec);
        CipherInputStream localCipherInputStream = new CipherInputStream(localFileInputStream, this.cipher);
        byte[] arrayOfByte = new byte[8];
        while (true) {
            int i = localCipherInputStream.read(arrayOfByte);
            if (i == -1) {
                localFileOutputStream.flush();
                localFileOutputStream.close();
                localCipherInputStream.close();
                return;
            }
            localFileOutputStream.write(arrayOfByte, 0, i);
        }
    }

    public static List<String> findAllEncryptedFiles(File dir) {
        List<String> encryptedFiles = new ArrayList<>();

        File[] files = dir.listFiles();
        if (files == null) return encryptedFiles;

        for (File file : files) {
            if (file.isDirectory()) {
                encryptedFiles.addAll(findAllEncryptedFiles(file));
            } else if (file.isFile() && file.getName().toLowerCase().endsWith(".enc")) {
                encryptedFiles.add(file.getAbsolutePath());
            }
        }

        return encryptedFiles;
    }
}