package com.datarecord.webapp.utils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static java.lang.System.in;

public class HttpServletSupport {

    public static HttpServletSupport getInstance(){
        return new HttpServletSupport();
    }

    public void exportFile(String filePath, HttpServletResponse response) throws IOException {
        File downloadFile = new File(filePath);
        String[] filePathSplit = filePath.split("/");
        response.setHeader("Content-Type", "application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename="
                + toUTF8String(filePathSplit[filePathSplit.length-1]));
        ServletOutputStream out = response.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(downloadFile);
        out.flush();
        int aRead = 0;
        byte b[] = new byte[1024];
        while ((aRead = fileInputStream.read(b)) != -1 & in != null) {
            out.write(b, 0, aRead);
        }
        out.flush();
        out.close();
    }

    public String toUTF8String(String str) {
        StringBuffer sb = new StringBuffer();
        int len = str.length();
        for (int i = 0; i < len; i++) {
            // 取出字符中的每个字符
            char c = str.charAt(i);
            // Unicode码值为0~255时，不做处理
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else { // 转换 UTF-8 编码
                byte b[];
                try {
                    b = Character.toString(c).getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    b = null;
                }
                // 转换为%HH的字符串形式
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) {
                        k &= 255;
                    }
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }
}
