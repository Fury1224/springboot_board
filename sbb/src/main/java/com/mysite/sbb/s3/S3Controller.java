package com.mysite.sbb.s3;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    
    // 글 작성 시 이미지 업로드 
    @PostMapping("/image/upload")
    @ResponseBody	// 본문에 바로 이미지를 띄워주기 위함
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
    
    // 프로필 이미지 업로드
    @PostMapping("/profile/upload")
    public ResponseEntity<Map<String, String>> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        try {
            String s3Url = s3Service.uploadImage(file);
            return ResponseEntity.ok(Map.of("imageUrl", s3Url));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Upload failed"));
        }
    }
}