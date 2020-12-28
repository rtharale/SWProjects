package com.cloud.dao;
import java.util.*;

import com.cloud.utils.Tuple;

public interface EC2Service {
	
	Tuple getEC2InstanceStatus();
	
	int startInstances(int count);

	int restartInstances(int instCount, Set<String> stoppedInstances);
}
