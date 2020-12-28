package com.cloud.dao;


import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;

public interface SQSService {

	public void deleteMessage(Message message, String queueName);

	public Message receiveMessage(String queueName, Integer waitTime, Integer visibilityTimeout);

	public void sendMessage(String messageBody, String queueName);
	
	public Integer getQueueCount(String queueName);
}
