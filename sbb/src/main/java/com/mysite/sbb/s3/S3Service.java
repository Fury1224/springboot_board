package com.mysite.sbb.s3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
public class S3Service {
	
	private S3Config s3Config;
	
	@Autowired
	public S3Service(S3Config s3Config) {
        this.s3Config = s3Config;
    }
	
    /**
     * S3에 이미지 업로드 하기
     */
	
	@Value("${cloud.aws.s3.bucket}")
    private String bucket;
	
	private String localLocation = "/images/";
	
    public String uploadImage(MultipartRequest request) throws IOException {
        // request 에서 이미지 추출
    	MultipartFile file = request.getFile("upload");
        
        // 이름 및 확장자 추출
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.indexOf("."));
        
        // 이미지 파일을 유일하게 uuid
        String uuidFileName = UUID.randomUUID() + ext;
        
        // 서버 환경에 저장할 경로
        String localPath = localLocation + uuidFileName;
        
        // 서버 환경에 이미지 저장
        File localFile = new File(localPath);
        file.transferTo(localFile);
        
        // s3에 이미지 올림
        s3Config.amazonS3Client().putObject(new PutObjectRequest(bucket, uuidFileName, localFile)
        		.withCannedAcl(CannedAccessControlList.PublicRead));
        String s3Url = s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();
        
        // 서버에 저장된 로컬 이미지 삭제
        localFile.delete();
        
        return s3Url;
        
    }
}