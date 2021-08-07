package com.hdfs.biz.BizImpl;

import com.hdfs.vo.JsonModel;
import org.apache.hadoop.fs.Path;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @program: hdfs
 * @description:
 * @author: 作者
 * @create: 2021-06-05 11:09
 */
public interface HdfsService {
    public List<Map<String,String>>listStatus(String path)throws Exception;
    public Boolean existFile(String path)throws Exception;
    public List<Map<String,String>>listStatus(int type)throws Exception;
    public Map<String,String>getFileInfo(String path)throws Exception;
    public boolean renameFile(String oldName,String newName)throws Exception;
    public boolean deleteFile(String path)throws Exception;
    public void copyFile(String sourcePath,String targetPath)throws Exception;
    public ResponseEntity<InputStreamResource>downloadFile(String path,String fileName)throws Exception;
    public void creatFile(String path, MultipartFile file)throws Exception;
    public ResponseEntity<byte[]> downloadDirectory(String path,String fileName) throws Exception;
    boolean mkdir(String newPath) throws Exception;
    public Map<String,String>getConfigurationInfoAsMap()throws Exception;
}
