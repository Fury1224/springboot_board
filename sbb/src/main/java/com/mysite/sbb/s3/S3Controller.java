package com.mysite.sbb.s3;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@Controller
public class S3Controller {

    private final S3Service s3Service;
    
    @Autowired
    public S3Controller(S3Service s3Service) {
    	this.s3Service = s3Service;
    }
    
   
    @PostMapping("/image/upload")
    @ResponseBody
    public Map<String, Object> uploadFile(MultipartRequest request) throws Exception {
    	
    	Map<String, Object> responseData = new HashMap<>();
    	
    	
    	try {
			String s3Url = s3Service.uploadImage(request);
			responseData.put("uploaded", true);
			responseData.put("url", s3Url);
			
			return responseData;
			
		} catch (IOException e) {
			responseData.put("uploaded", false);
			return responseData;
		}
    }
}