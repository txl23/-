package com.hdfs.controller;

import com.hdfs.biz.BizImpl.HdfsService;
import com.hdfs.vo.JsonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @program: hdfs
 * @description:
 * @author: 汤僖龙
 * @create: 2021-06-06 20:46
 */
@Controller
@RequestMapping("/back/hdfs")
public class FileController {
    @Autowired
    private HdfsService hdfsService;
    @RequestMapping(value = "/downLoadFile.action",method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource>downloadfile(@RequestParam("path") String path,@RequestParam("fileName")String fileName)throws Exception{
        ResponseEntity<InputStreamResource>result=null;
        try {

            result=this.hdfsService.downloadFile(path,fileName);
        }catch (Exception e){
            e.printStackTrace();

        }
        return  result;
    }
    @RequestMapping(value = "/downloadDirectory.action",method = RequestMethod.GET)
    public ResponseEntity<byte[]>dowl(@RequestParam("path") String path,@RequestParam("fileName") String fileName)throws  Exception{
        ResponseEntity<byte[]>result=null;
        try {
            result=this.hdfsService.downloadDirectory(path,fileName);

        }catch (Exception e){
            e.printStackTrace();
        }
        return  result;
    }
    @RequestMapping(value = "uploadData.action",method = RequestMethod.POST)
    @ResponseBody
    public JsonModel uploadData(MultipartFile file, @RequestParam("currentPath")String currentPath, HttpServletRequest req,JsonModel jm)throws Exception{
        try {
            this.hdfsService.creatFile(currentPath,file);
            jm.setCode(1);
            List<Map<String,String>>list=hdfsService.listStatus(currentPath);
            jm.setObj(list);
        }catch (Exception ex){
            ex.printStackTrace();
            jm.setCode(0);
            jm.setMsg(ex.getMessage());
        }
        return  jm;
    }
}
