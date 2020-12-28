package com.cloud.aws;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import com.cloud.dao.*;
import com.cloud.impl.LoadBalancerImpl;
import com.cloud.utils.*;


@RestController
@EnableAutoConfiguration
@RequestMapping("/")
public class Controller {
	
    @Autowired
    private SQSService sqsService;
    
    @Autowired
    private S3Service s3;
    
    @Autowired
    private LoadBalancerImpl loadBalancer;
    
    @RequestMapping(value = "info")
    public String entry(@RequestParam String file) {
    	return "Hi" + file;
    }
    
	@RequestMapping(value = "update")
	public ResponseEntity<String> updateVideo(@RequestParam String fileName) {
		
		try {
			sqsService.sendMessage(fileName, GlobalConstants.INPUT_QUEUE);
			loadBalancer.scaleEC2();
		} // catch (InterruptedException | IOException e) {
		catch (Exception e) {
			//sqsService.deleteMessage(url, GlobalConstants.INPUT_QUEUE);
			throw new RuntimeException(e.getMessage());
		}
		return new ResponseEntity<String>("Succesful", HttpStatus.OK);
	}
	
	@PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public  ResponseEntity<String> sendToSQSAndUploadToS3(@RequestParam MultipartFile file) throws IOException {
		try {
			s3.putObject(file.getOriginalFilename(), file.getInputStream(), GlobalConstants.INPUT_BUCKET_NAME);
			sqsService.sendMessage(file.getOriginalFilename(), GlobalConstants.INPUT_QUEUE);
			loadBalancer.scaleEC2();
		} // catch (InterruptedException | IOException e) {
		catch (Exception e) {
			//sqsService.deleteMessage(url, GlobalConstants.INPUT_QUEUE);
			throw new RuntimeException(e.getMessage());
		}
		return new ResponseEntity<String>("Succesful", HttpStatus.OK);
    }
	
	@RequestMapping(value = "/result")
	public ResponseEntity<String> updateToS3(@RequestParam String fileName, String content) {
		
		try {
			s3.putStringObject(fileName, content, GlobalConstants.OUTPUT_BUCKET_NAME);
		} // catch (InterruptedException | IOException e) {
		catch (Exception e) {
			//sqsService.deleteMessage(url, GlobalConstants.INPUT_QUEUE);
			throw new RuntimeException(e.getMessage());
		}
		return new ResponseEntity<String>("Succesful", HttpStatus.OK);
	}
	
}
