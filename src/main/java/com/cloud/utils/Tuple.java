package com.cloud.utils;

import java.util.HashSet;
import java.util.Set;

public class Tuple {
		public int runningEC2Count = 0;
		public Set<String> stoppedInstances = new HashSet<>();
}
