package com.hdfs.utils;

import org.apache.hadoop.fs.FSDataInputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.function.ServerRequest;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: hdfs
 * @description:
 * @author: 汤僖龙
 * @create: 2021-06-05 12:49
 */
public class YcFileUtil {
    public static String formatTime(long time){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:sss");
        Date date=new Date(time);
        return sdf.format(date);
    }

    public static String formatFileSize(long fileS){
        DecimalFormat df=new DecimalFormat("#.00");
        String fileSizeString="";
        String wrongSize="0B";
        if (fileS==0){
            return  wrongSize;
        }
        if (fileS<1024){
            fileSizeString=df.format((double)fileS)+"B";
        }else if (fileS<1048576){
            fileSizeString=df.format((double)fileS/1024)+"KB";
        }else if (fileS<1073741824){
            fileSizeString=df.format((double)fileS/1048576)+"MB";
        } else {
            fileSizeString=df.format((double)fileS/1073741824)+"GB";
        }
        return fileSizeString;
    }

    public static ResponseEntity<InputStreamResource> downloadFile(InputStream in, String fileName) {
        try {
            byte[] testBytes=new byte[in.available()];
            HttpHeaders Headers=new HttpHeaders();
            Headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            Headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
            Headers.add("Pragma", "no-cache");
            Headers.add("Expires", "0");
            Headers.add("Content-Language", "UTF-8");
            return  ResponseEntity.ok().headers(Headers).contentLength(testBytes.length)
                    .contentType(MediaType.parseMediaType("application/octet-stream")).body(new InputStreamResource(in));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ResponseEntity<byte[]> downloadDirectory(byte[] bs, String fileName) {
        try {
            HttpHeaders Headers=new HttpHeaders();
            Headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            Headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
            Headers.add("Pragma", "no-cache");
            Headers.add("Expires", "0");
            Headers.add("Content-Language", "UTF-8");
            return  ResponseEntity.ok().headers(Headers).contentLength(bs.length)
                    .contentType(MediaType.parseMediaType("application/octet-stream")).body(bs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static  String genFileName(){
        Date d=new Date();
        DateFormat df=new SimpleDateFormat("yyyyHHddmmss");
        return df.format(d)+".zip";
    }
}
