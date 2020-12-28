package com.cloud.utils;
import com.amazonaws.regions.Regions;


public class GlobalConstants {
	public static final Regions REGION = Regions.US_EAST_1;
	public static final int SQS_WAIT_TIME = 20 ;
	public static final int SQS_VISIBILITY_TIME = 5;
	public static final String INPUT_QUEUE = "inputqueue";
	public static final String INPUT_BUCKET_NAME = "inputbucketgroup12";
	public static final String OUTPUT_BUCKET_NAME = "outputbucketgroup12";
	public static final String SECURITY_GROUP = "sg-0037e32a5985b3679";
	public static final String AMI_ID = "ami-05b86e5d88fdeeecb";
	public static final String KEY_PAIR = "CloudComputing";
	public static final String IAM_ARN = "arn:aws:iam::581419187317:instance-profile/CloudComputing";
	public static final int MAX_INSTANCE_COUNT = 19;
}
