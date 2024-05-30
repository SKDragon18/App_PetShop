package com.example.petshopapp.tools;

import org.apache.commons.fileupload.MultipartStream;
import org.apache.commons.fileupload.util.Streams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MultipartParser {
    public static List<byte[]> parseMultipartResponse(InputStream inputStream, String boundary) throws IOException {
        List<byte[]> images = new ArrayList<>();

        byte[] boundaryBytes = ("--" + boundary).getBytes(StandardCharsets.UTF_8);
        MultipartStream multipartStream = new MultipartStream(inputStream, boundaryBytes, 500000, null);

        boolean nextPart = multipartStream.skipPreamble();
        while (nextPart) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            multipartStream.readHeaders();
            multipartStream.readBodyData(byteArrayOutputStream);
            images.add(byteArrayOutputStream.toByteArray());
            nextPart = multipartStream.readBoundary();
        }
        return images;
    }

}
