package com.hdfs.biz.BizImpl;

import com.hdfs.utils.YcFileUtil;
import javassist.bytecode.stackmap.BasicBlock;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @program: hdfs
 * @description:
 * @author: 汤僖龙
 * @create: 2021-06-05 11:12
 */
@Service
public class HdfsServiceImpl implements HdfsService{
    @Value("${hdfs.path}")
    private String path;
    @Value("${hdfs.username}")
    private String username;
    private final int bufferSize=1024 * 1024 * 64;
    public Configuration getConfiguration(){
        Configuration configuration=new Configuration();
        configuration.set("fs.defaultFS",path);
        return configuration;
    }
    public FileSystem getFileSystem() throws Exception{
        FileSystem fileSystem=FileSystem.get(new URI(path),getConfiguration(),username);
        return fileSystem;
    }
    @Override
    public List<Map<String, String>> listStatus(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!existFile(path)) {
            return null;
        }
        FileSystem fs = getFileSystem();
        Path srcPath = new Path(path);
        FileStatus[] fileStatuses = fs.listStatus(srcPath);
        if (fileStatuses == null || fileStatuses.length <= 0) {
            return null;
        }
        List<Map<String,String>>returnlist=new ArrayList<>();
        for (FileStatus file: fileStatuses){
            Map<String,String>map=fileStatusToMap(file);
            returnlist.add(map);
        }
        fs.close();
        return returnlist;
    }

    @Override
    public Boolean existFile(String path) throws Exception {
        if (StringUtils.isEmpty(path)){
            return false;
        }
        FileSystem fs=getFileSystem();
        Path srcPath=new Path(path);
        boolean isExists=fs.exists(srcPath);
        return  isExists;
    }

    @Override
    public List<Map<String, String>> listStatus(int type) throws Exception {
        String path = "/";
        Path srcPath = new Path(path);
        List<Map<String,String>>returnList=new ArrayList<>();
        String reg=null;
        if (type==1){
            reg=".+(.jpeg|.jpg|.png|.bmp|.gif)$";
        }else if (type==2){
            reg=".+(.txt|.rtf|.doc|.docx|.xls|.xlsx|.html|.htm|.xml)$";
        }else if (type==3){
            reg=".+(.mp4|.avi|.wmv)$";
        }else if (type==4){
            reg=".+(.mp4|.avi)$";
        }
        else if (type==5){
            reg="^\\S+\\.*$";
        }
        Pattern pattern=Pattern.compile(reg,Pattern.CASE_INSENSITIVE);
        search(srcPath,returnList,pattern);
        return returnList;
    }
    private void  search(Path srcPath,List<Map<String,String>>list,Pattern pattern )throws Exception{
        FileSystem fs=getFileSystem();
        FileStatus[]fileStatuses=fs.listStatus(srcPath);
        if (fileStatuses!=null&&fileStatuses.length>0){
                for (FileStatus file :fileStatuses){
                    boolean result=file.isFile();
                    if (  !result){
                        search(file.getPath(),list,pattern);
                    }else {
                        boolean b=pattern.matcher(file.getPath().getName()) .find();
                        if (b){
                            Map<String,String>map=this.fileStatusToMap(file);
                            list.add(map);
                        }
                }
            }
        }
    }
    @Override
    public Map<String, String> getFileInfo(String path) throws Exception {
        if (StringUtils.isEmpty(path)){
            return null;
        }
        FileSystem fs=getFileSystem();
        Path srcPath=new Path(path);
        FileStatus fileStatus=fs.getFileStatus(srcPath);
        return  fileStatusToMap(fileStatus);

    }
    private Map<String,String>fileStatusToMap(FileStatus file){
        Map<String,String>map=new HashMap<>();
        Path p=file.getPath();
        map.put("fileName",p.getName());
        String filePath=p.toUri().toString();
        map.put("filePath",filePath);
        String relativePath=filePath.substring(this.path.length());
        map.put("relativePath",relativePath);
        map.put("parentPath",p.getParent().toUri().toString().substring(this.path.length()));
        map.put("owner",file.getOwner());
        map.put("group",file.getGroup());
        map.put("isFile",file.isFile()+"");
        map.put("duplicates",file.getReplication()+"");
        map.put("size",YcFileUtil.formatFileSize(file.getLen()));
        map.put("rights",file.getPermission().toString());
        map.put("modifyTime",YcFileUtil.formatTime(file.getModificationTime()));
        return map;
    }

    @Override
    public boolean renameFile(String oldName, String newName) throws Exception {
        if (StringUtils.isEmpty(oldName)||StringUtils.isEmpty(newName)){
            return false;
        }
        FileSystem fs=getFileSystem();
        Path oldPath=new Path(oldName);
        Path newPath=new Path(newName);
        boolean isOK=fs.rename(oldPath,newPath);
        fs.close();
        return isOK;
    }

    @Override
    public boolean deleteFile(String path) throws Exception {

        if (StringUtils.isEmpty(path)){
            return false;
        }if (!existFile(path)){
            return false;
        }
        FileSystem fs=getFileSystem();
        Path srcPath=new Path(path);
        boolean isOK=fs.delete(srcPath,true);
        fs.close();
        return isOK;
    }

    @Override
    public void copyFile(String sourcePath, String targetPath) throws Exception {
        if (StringUtils.isEmpty(sourcePath) ||StringUtils.isEmpty(targetPath)){
            return;
        }
        FileSystem fs=getFileSystem();
        Path oldPath=new Path(sourcePath);
        Path newPath=new Path(targetPath);
        FSDataInputStream inputStream=null;
        FSDataOutputStream outputStream=null;
        try {
            inputStream=fs.open(oldPath);
            outputStream=fs.create(newPath);
            IOUtils.copyBytes(inputStream,outputStream,bufferSize,false);
        }finally {
            inputStream.close();
            outputStream.close();
            fs.close();
        }

    }

    @Override
    public ResponseEntity<InputStreamResource> downloadFile(String path, String fileName) throws Exception {
        FileSystem fs=this.getFileSystem();
        Path p=new Path(path);
        FSDataInputStream inputStream=fs.open(p);
        return  YcFileUtil.downloadFile(inputStream,fileName);
    }

    @Override
    public void creatFile(String path, MultipartFile file) throws Exception {
        if (StringUtils.isEmpty(path)||null==file.getBytes()){
            return;
        }
        String fileName =file.getOriginalFilename();
        FileSystem fs=getFileSystem();
        Path newPath=null;
        if ("/".equals(path)){
            newPath=new Path(path+fileName);
        }else {
            newPath=new Path(path + "/" + fileName);
        }
        FSDataOutputStream outputStream =fs.create(newPath);
        outputStream.write(file.getBytes());
        outputStream.close();
        fs.close();
    }

    @Override
    public boolean mkdir(String path) throws Exception {
        if (StringUtils.isEmpty(path)){
            return  false;
        }
        if (existFile(path)){
            return  true;
        }
        FileSystem fs=getFileSystem();
        Path srcPath=new Path(path);
        boolean isOk=fs.mkdirs(srcPath);
        fs.close();
        return  isOk;
    }

    @Override
    public Map<String, String> getConfigurationInfoAsMap() throws Exception {
       FileSystem fileSystem=getFileSystem();
       Configuration conf=fileSystem.getConf();
       Iterator<Map.Entry<String,String>>ite=(Iterator<Map.Entry<String, String>>) conf.iterator();
        Map<String,String>map=new HashMap<>();
        while (ite.hasNext()){
            Map.Entry<String,String>entry=ite.next();
            map.put(entry.getKey(),entry.getValue());
        }
        return  map;
    }


    @Override
    public ResponseEntity<byte[]> downloadDirectory(String path, String fileName)throws Exception{
        ByteArrayOutputStream out=null;
        try {
            FileSystem fs=this.getFileSystem();
            out=new ByteArrayOutputStream();
            ZipOutputStream zos=new ZipOutputStream(out);
            compress(path,zos,fs);
            zos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        byte[]bs=out.toByteArray();
        out.close();
        return YcFileUtil.downloadDirectory(bs,YcFileUtil.genFileName());

    }
    public void  compress(String baseDir,ZipOutputStream zipOutputStream,FileSystem fs)throws IOException{
        try {
            FileStatus[] fileStatulist=fs.listStatus(new Path(baseDir));
            String[]strs=baseDir.split("/");
            String lastName=strs[strs.length-1];
            for (int i=0;i<fileStatulist.length;i++){
                String name=fileStatulist[i].getPath().toString();
                name=name.substring(name.indexOf("/" + lastName));
                if (fileStatulist[i].isFile()){
                    Path path =fileStatulist[i].getPath();
                    FSDataInputStream inputStream=fs.open(path);
                    zipOutputStream.putNextEntry(new ZipEntry(name.substring(1)));
                    IOUtils.copyBytes(inputStream,zipOutputStream,this.bufferSize);
                    inputStream.close();
                }else {
                    zipOutputStream.putNextEntry(new ZipEntry(fileStatulist[i].getPath().getName()+"/"));
                    compress(fileStatulist[i].getPath().toString(),zipOutputStream,fs);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
