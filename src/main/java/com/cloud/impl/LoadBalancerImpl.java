package com.cloud.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cloud.dao.*;
import com.cloud.utils.GlobalConstants;
import com.cloud.utils.*;

@Service
public class LoadBalancerImpl {
    @Autowired
    private SQSService sqsService;

    @Autowired
    private EC2Service ec2Service;

    @Scheduled(fixedDelay = 2000)
    public void scaleEC2() throws Exception {
		try {
			int numOfMsgs = sqsService.getQueueCount(GlobalConstants.INPUT_QUEUE);
			Tuple currentStatus = ec2Service.getEC2InstanceStatus();
			int countOfRunningInstances = currentStatus.runningEC2Count;
			int numberOfAppInstances = countOfRunningInstances - 1;
			int requiredInst = numOfMsgs - numberOfAppInstances;
			if (requiredInst > 0) {
				System.out.println(currentStatus.stoppedInstances.size());
				requiredInst -= ec2Service.restartInstances(requiredInst, currentStatus.stoppedInstances);
				
				int availableInst = GlobalConstants.MAX_INSTANCE_COUNT - numberOfAppInstances
						- currentStatus.stoppedInstances.size();
				if (requiredInst > 0 && availableInst > 0) {
					if (requiredInst >= availableInst)
						ec2Service.startInstances(availableInst);
					else
						ec2Service.startInstances(requiredInst);
				}
			}
		}catch(Exception e) {
    		throw new Exception(e.getMessage());
    	}
    }
}
