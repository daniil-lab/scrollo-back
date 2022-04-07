package com.inst.base.util;

import org.springframework.http.HttpStatus;

public final class FileValidator {
    public static boolean checkOnImage(String fileName) {
        String fileType = fileName.split("\\.")[fileName.split("\\.").length - 1];

        return fileType.equals("png") || fileType.equals("jpeg") || fileType.equals("jpg");
    }

    public static boolean checkOnVideo(String fileName) {
        String fileType = fileName.split("\\.")[fileName.split("\\.").length - 1];

        return fileType.equals("mp4") || fileType.equals("mov") || fileType.equals("avi") || fileType.equals("webm");
    }
}
