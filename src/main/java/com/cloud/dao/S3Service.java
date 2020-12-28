package com.cloud.dao;

import java.io.File;
import java.io.InputStream;

public interface S3Service {
	public void putObject(String key, InputStream value, String bucketName);
	
	public void putStringObject(String key, String value, String bucketName);
	
	public void getObject(String key, File f);
}
