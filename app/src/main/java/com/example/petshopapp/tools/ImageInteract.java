package com.example.petshopapp.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Map;

import okhttp3.ResponseBody;

public class ImageInteract {

    //Convert
    public static Bitmap convertStringToBitmap(String source){
        byte[] code = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            code = Base64.getDecoder().decode(source);
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(code, 0, code.length);
        return bitmap;
    }

    public static Bitmap convertByteArrayToBitmap(byte[] byteArray){
        return BitmapFactory.decodeByteArray(byteArray,0, byteArray.length);
    }

    public static void parseMultipartResponse(ResponseBody responseBody, Map<String, byte[]> files) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody.byteStream()));
        System.out.println(responseBody.byteStream());
        String boundary = null;
        String line;

        // Tìm boundary từ dòng đầu tiên
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("--")) {
                boundary = line;
                break;
            }
        }

        if (boundary == null) {
            throw new IllegalStateException("Boundary not found in response");
        }

        files.clear();

        String currentFileName = null;
        StringBuilder currentFileContent = new StringBuilder();
        boolean isFileContent = false;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith(boundary)) {
                if (currentFileName != null) {
                    files.put(currentFileName, currentFileContent.toString().getBytes());
                }
                currentFileName = null;
                currentFileContent.setLength(0);
                isFileContent = false;
            } else if (line.startsWith("Content-Disposition")) {
                int startIndex = line.indexOf("filename=\"") + 10;
                int endIndex = line.indexOf("\"", startIndex);
                currentFileName = line.substring(startIndex, endIndex);
            } else if (line.isEmpty()) {
                isFileContent = true;
            }
            else if (line.contains(boundary)) {
                System.out.println("currentFileName");
                files.put(currentFileName, currentFileContent.toString().getBytes());
                currentFileName=null;
            }
            else if (isFileContent) {
                currentFileContent.append(line).append("\n");
            }
        }



        // Xử lý các file đã nhận được
        for (Map.Entry<String, byte[]> entry : files.entrySet()) {
            String fileName = entry.getKey();
            byte[] fileContent = entry.getValue();
            // Làm gì đó với fileContent (lưu trữ, xử lý, vv)
            System.out.println("Received file: " + fileName + " with size: " + fileContent.length);
        }
    }

}
