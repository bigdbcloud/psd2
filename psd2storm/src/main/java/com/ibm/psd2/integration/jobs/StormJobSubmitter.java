package com.ibm.psd2.integration.jobs;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.storm.Config;
//import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.StormTopology;

import com.ibm.psd2.integration.ArgumentsContainer;

public class StormJobSubmitter implements JobSubmitter
{

	private static final Logger logger = LogManager.getLogger(StormJobSubmitter.class);

	private ArgumentsContainer ac;

	EventProcessTopology eventProcessTopology;

	public StormJobSubmitter(ArgumentsContainer ac)
	{
		this.ac = ac;
		this.eventProcessTopology = new EventProcessTopology(ac);
	}

	@Override
	public void submitJob() throws Exception
	{
		logger.info("submitting job");
		StormTopology st = eventProcessTopology.createTopology();

		Map conf = new HashMap<String, Object>();
		conf.put(Config.TOPOLOGY_DEBUG, false);

		// LocalCluster cluster = new LocalCluster();
		StormSubmitter.submitTopology("PSD2Topology", conf, st);

		// cluster.submitTopology("kafkaTopology", conf, st);

	}

}
