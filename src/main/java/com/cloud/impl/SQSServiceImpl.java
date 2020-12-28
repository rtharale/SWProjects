package com.cloud.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.cloud.dao.*;

@Service
public class SQSServiceImpl implements SQSService {
	
    @Autowired
    private AmazonSQSAsync sqs;

	@Override
	public void deleteMessage(Message message, String queueName) {
		// TODO Auto-generated method stub
		String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
		String messageReceiptHandle = message.getReceiptHandle();
		DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest(queueUrl, messageReceiptHandle);
		sqs.deleteMessage(deleteMessageRequest);
	}

	@Override
	public Message receiveMessage(String queueName, Integer waitTime, Integer visibilityTimeout) {
		// TODO Auto-generated method stub
		String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
		receiveMessageRequest.setMaxNumberOfMessages(1);
		receiveMessageRequest.setWaitTimeSeconds(waitTime);
		receiveMessageRequest.setVisibilityTimeout(visibilityTimeout);
		ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(receiveMessageRequest);
		List<Message> messageList = receiveMessageResult.getMessages();
		if (messageList.isEmpty()) {
			return null;
		}
		return messageList.get(0);
	
	}

	@Override
	public void sendMessage(String messageBody, String queueName) {
		String queueUrl = null;
		//try {
			queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
		//} catch (QueueDoesNotExistException queueDoesNotExistException) {
		//	CreateQueueResult createQueueResult = this.createQueue(queueName);
		//}
		queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
		SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(queueUrl)
				.withMessageBody(messageBody).withDelaySeconds(0);
		sqs.sendMessage(sendMessageRequest);
	}

	@Override
	public Integer getQueueCount(String queueName) {
		// TODO Auto-generated method stub
		String queueUrl = null;
		try {
			queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
		} catch (Exception e) {
			//CreateQueueResult createQueueResult = this.createQueue(queueName);
		}
		queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
		List<String> attributeNames = new ArrayList<String>();
		attributeNames.add("ApproximateNumberOfMessages");
		attributeNames.add("ApproximateNumberOfMessagesNotVisible");
		GetQueueAttributesRequest getQueueAttributesRequest = new GetQueueAttributesRequest(queueUrl, attributeNames);
		Map map = sqs.getQueueAttributes(getQueueAttributesRequest).getAttributes();
		String numberOfMessagesString = (String) map.get("ApproximateNumberOfMessages") ;

		String numberOfMessagesStringNV = (String) map.get("ApproximateNumberOfMessagesNotVisible") ;
		Integer numberOfMessages = Integer.valueOf(numberOfMessagesString) + Integer.valueOf(numberOfMessagesStringNV);
		return numberOfMessages;
	}

}
