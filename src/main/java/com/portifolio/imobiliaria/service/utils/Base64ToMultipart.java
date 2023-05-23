package com.portifolio.imobiliaria.service.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class Base64ToMultipart {

    public MultipartFile base64ToMultipart(String base64) throws IOException {
        String[] base64Array = base64.split(",");
        byte[] bytes = Base64.getDecoder().decode(base64Array[1]);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        return new MultipartFile() {

            @Override
            public String getName() {
                return "file";
            }

            @Override
            public String getOriginalFilename() {
                return "file.png";
            }

            @Override
            public String getContentType() {
                return "image/png";
            }

            @Override
            public boolean isEmpty() {
                return bytes.length == 0;
            }

            @Override
            public long getSize() {
                return bytes.length;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return bytes;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return inputStream;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                try (FileOutputStream outputStream = new FileOutputStream(dest)) {
                    outputStream.write(bytes);
                }
            }
        };
    }

}
