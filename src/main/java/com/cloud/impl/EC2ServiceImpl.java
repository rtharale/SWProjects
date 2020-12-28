package com.cloud.impl;

import java.util.*;

import com.cloud.dao.EC2Service;
import com.cloud.utils.GlobalConstants;
import com.cloud.utils.Tuple;
import com.amazonaws.services.ec2.AmazonEC2Async;
import com.amazonaws.services.ec2.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EC2ServiceImpl implements EC2Service {

	@Autowired
	private AmazonEC2Async ec2AsyncClient;
	
	@Override
	public Tuple getEC2InstanceStatus() {
		// TODO Auto-generated method stub
		DescribeInstanceStatusRequest describeInstanceReq = new DescribeInstanceStatusRequest();
		describeInstanceReq.setIncludeAllInstances(true);
		DescribeInstanceStatusResult describeInstances = ec2AsyncClient.describeInstanceStatus(describeInstanceReq);
		List<InstanceStatus> instanceStatusList = describeInstances.getInstanceStatuses();
		Tuple tuple = new Tuple();
        for (InstanceStatus instanceStatus : instanceStatusList) {
			InstanceState instanceState = instanceStatus.getInstanceState();
			if (instanceState.getName().equals(InstanceStateName.Running.toString()) ||
					instanceState.getName().equals(InstanceStateName.Pending.toString())) {
				tuple.runningEC2Count++;
			} else if (instanceState.getName().equals(InstanceStateName.Stopping.toString()) ||
					instanceState.getName().equals(InstanceStateName.Stopped.toString())){
			    tuple.stoppedInstances.add(instanceStatus.getInstanceId());
            }
		}
		return tuple;
	
	}

	@Override
	public int startInstances(int count) {
		// TODO Auto-generated method stub
		List<String> securityGroupIds = new ArrayList<>();
		securityGroupIds.add(GlobalConstants.SECURITY_GROUP); 
		Collection<TagSpecification> tagSpecifications = new ArrayList<TagSpecification>();
		Collection<Tag> tags = new ArrayList<>();
        tags.add(new Tag().withKey("Name").withValue("App-Instance"));
		tagSpecifications.add(new TagSpecification().withResourceType("instance").withTags(tags));
		RunInstancesRequest rir = new RunInstancesRequest(GlobalConstants.AMI_ID, 1, count).withInstanceType("t2.micro")
                .withSecurityGroupIds(securityGroupIds)
                .withTagSpecifications(tagSpecifications)
                .withUserData(Base64.getEncoder().encodeToString(getUserDataScript().getBytes()))
                .withKeyName(GlobalConstants.KEY_PAIR)
                .withIamInstanceProfile(new IamInstanceProfileSpecification().withArn(GlobalConstants.IAM_ARN));
		try {
			ec2AsyncClient.runInstances(rir);
			//System.out.println(result);
		} catch (AmazonEC2Exception amzEc2Exp) {
			System.out.println("Running Instances" + amzEc2Exp.getMessage());
		} catch (Exception e) {
			System.out.println("Running Instances Error");
		}
		return 1;
	}

	@Override
	public int restartInstances(int instCount, Set<String> stoppedInstances) {
		int restartCount = Math.min(instCount, stoppedInstances.size());
		if (restartCount == 0)
			return 0;
		List<String> instanceIds = new ArrayList<>();
		int count = 0;
		for (String instanceId : stoppedInstances)
		{
			if (count == instCount)
				break;
			instanceIds.add(instanceId);
			count++;
			
		}
		ec2AsyncClient.startInstances(new StartInstancesRequest(instanceIds));
		return restartCount;
	}
	
	private String getUserDataScript() {
		StringBuilder sh = new StringBuilder("");
		sh.append("#! /bin/bash").append("\ncd /home/ubuntu").append("\nXvfb :1 & export DISPLAY=:1\n")
				.append("nohup java -jar aws-appinstance-1.5.jar");
		return sh.toString();
	}

}
