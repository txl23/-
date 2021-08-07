package com.hdfs.controller;

import com.hdfs.biz.BizImpl.HdfsService;
import com.hdfs.vo.JsonModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @program: hdfs
 * @description:
 * @author: 汤僖龙
 * @create: 2021-06-05 12:57
 */
@RestController
@RequestMapping("/back/hdfs")
public class hdfsController {
    //private static Logger LOGGER= LoggerFactory.getLogger(hdfsController.class);
    @Autowired
    private HdfsService hdfsService;
    @PostMapping("/listFile")
    public JsonModel listFile(@RequestParam("path")String path,JsonModel jm)throws Exception{
        if (StringUtils.isEmpty(path)){
            jm.setCode(0);
            jm.setMsg("请求参数为空");
            return jm;
        }
        List<Map<String,String>>list=hdfsService.listStatus(path);
        jm.setCode(1);
        jm.setObj(list);
        return  jm;
    }
    @PostMapping("/moveTo.action")
    public JsonModel moveTo(@RequestParam("parentPath")String parentPath,@RequestParam("path")String path,@RequestParam("newName") String newName,JsonModel jm )throws Exception{
        if (StringUtils.isEmpty(path)||StringUtils.isEmpty(newName)){
            jm.setCode(0);
            jm.setMsg("请求参数为空");
            return jm;
        }
       try {
           hdfsService.copyFile(path,newName);
           hdfsService.deleteFile(path);
           jm.setCode(1);
           List<Map<String,String>>list=hdfsService.listStatus(parentPath);
           jm.setObj(list);
       }catch (Exception ex){
           ex.printStackTrace();
           jm.setObj(0);
       }
       return jm;
    }

    @PostMapping("/copyTo.action")
    public JsonModel copyTo(@RequestParam("parentPath")String parentPath,@RequestParam("path")String path,@RequestParam("newName") String newName,JsonModel jm )throws Exception{
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(newName)){
            jm.setCode(0);
            jm.setMsg("请求参数为空");
            return jm;
        }
        try {
            hdfsService.copyFile(path,newName);
            jm.setCode(1);
            List<Map<String,String>>list=hdfsService.listStatus(parentPath);
            jm.setObj(list);
        }catch (Exception ex){
            ex.printStackTrace();
            jm.setObj(0);
        }
        return jm;
    }
    @PostMapping("/deleteItem.action")
    public JsonModel deleteItem(@RequestParam("path")String path,JsonModel jm )throws Exception{
        if (StringUtils.isEmpty(path)||StringUtils.isEmpty(path)){
            jm.setCode(0);
            jm.setMsg("请求参数为空");
            return jm;
        }
        boolean f=hdfsService.deleteFile(path);
        if (f){
            jm.setCode(1);
        }else {
            jm.setCode(0);
        }
        return jm;
    }
    @PostMapping("/rename.action")
    public JsonModel deleteItem(@RequestParam("oldPath")String oldPath,@RequestParam("newName") String newName, JsonModel jm )throws Exception{
        if (StringUtils.isEmpty(oldPath)||StringUtils.isEmpty(newName)){
            jm.setCode(0);
            jm.setMsg("请求参数为空");
            return jm;
        }
        String newPath=oldPath.substring(0,oldPath.lastIndexOf("/")+1)+newName;
        boolean f=hdfsService.renameFile(oldPath,newPath);
        if (f){
            jm.setCode(1);
            Map<String,String>map=hdfsService.getFileInfo(newPath);
            jm.setObj(map);
        }else {
            jm.setCode(0);
        }
        return jm;
    }
    @PostMapping("/mkdir.action")
    public JsonModel mkdir(@RequestParam("path")String path,@RequestParam("newName") String newName, JsonModel jm )throws Exception{
        if (StringUtils.isEmpty(path)||StringUtils.isEmpty(newName)){
            jm.setCode(0);
            jm.setMsg("请求参数为空");
            return jm;
        }
        String newPath="";
        if (path.equalsIgnoreCase("/")){
            newPath=path+newName;
        }else {
            newPath=path+"/"+newName;
        }
        if (this.hdfsService.existFile(newPath)){
            jm.setCode(0);
            jm.setMsg("已存在");
            return jm;
        }
        boolean f=hdfsService.mkdir(newPath);
        if (f){
            jm.setCode(1);
            Map<String,String>map=hdfsService.getFileInfo(newPath);
            jm.setObj(map);
        }else {
            jm.setCode(0);
        }
        return jm;
    }
    @PostMapping("/listFileByType")
    public  JsonModel List(final @RequestParam("type") Integer type,JsonModel jm)throws  Exception{
        List<Map<String,String>>list=hdfsService.listStatus(type);
        jm.setCode(1);
        jm.setObj(list);
        return jm;
    }
    @PostMapping("/getHdfsInfo.action")
    public  JsonModel getHdfsInfo(JsonModel jm)throws Exception{
        Map<String,String>info=this.hdfsService.getConfigurationInfoAsMap();
        jm.setCode(1);
        jm.setObj(info);
        return jm;
    }
}
