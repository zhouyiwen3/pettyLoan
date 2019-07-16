package com.ps.controller;

import com.ps.service.impl.QnUploadServiceImpl;
import com.qiniu.common.QiniuException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.IOException;

@RestController
public class QnUploadController {

    @Autowired
    private QnUploadServiceImpl qnUploadService;

    @PostMapping("/uploadInputStream")
    public String uploadInputStream(@RequestParam Part file) throws IOException {
        return qnUploadService.uploadFile(file.getInputStream(), "cln");
    }


    @RequestMapping("/deleteFile")
    public String deleteFile() throws QiniuException {
        return qnUploadService.delete("");
    }


    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam MultipartFile file) throws IOException {
        // System.out.println(file.getOriginalFilename());
        return qnUploadService.uploadFile(file.getInputStream(), "aaa");
    }


}
