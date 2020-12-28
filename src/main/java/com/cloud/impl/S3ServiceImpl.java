package com.cloud.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.cloud.dao.S3Service;
import com.cloud.utils.*;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;

@Service
public class S3ServiceImpl implements S3Service {

    @Autowired
    private AmazonS3 s3;

	
	@Override
	public void putObject(String key, InputStream value, String bucketName) {
		// TODO Auto-generated method stub
//		byte[] contentAsBytes = null;
//		try {
//			contentAsBytes = value.getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ByteArrayInputStream contentsAsStream = new ByteArrayInputStream(contentAsBytes);
		ObjectMetadata md = new ObjectMetadata();
	//	md.setContentLength(contentAsBytes.length);
		s3.putObject(bucketName, key, value, md);
		//s3.putObject(new PutObjectRequest(bucketName, key, contentsAsStream, md));
	}
	
	@Override
	public void putStringObject(String key, String value, String bucketName) {
		// TODO Auto-generated method stub
		byte[] contentAsBytes = null;
		try {
			contentAsBytes = value.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteArrayInputStream contentsAsStream = new ByteArrayInputStream(contentAsBytes);
		ObjectMetadata md = new ObjectMetadata();
		md.setContentLength(contentAsBytes.length);
		//s3.putObject(bucketName, key, value, md);
		s3.putObject(new PutObjectRequest(bucketName, key, contentsAsStream, md));
	}


	@Override
	public void getObject(String key, File f) {
		System.out.println("FULL PATH:" + GlobalConstants.INPUT_BUCKET_NAME + " " + key);
		s3.getObject(new GetObjectRequest(GlobalConstants.INPUT_BUCKET_NAME, key), f); 
	}
	
	
}
