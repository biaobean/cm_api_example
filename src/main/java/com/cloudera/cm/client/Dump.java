package com.cloudera.cm.client;

import com.cloudera.api.ClouderaManagerClientBuilder;
import com.cloudera.api.DataView;
import com.cloudera.api.model.ApiCluster;
import com.cloudera.api.model.ApiClusterList;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.v11.RootResourceV11;

public class Dump {
	public static void main(String[] args) throws InterruptedException {
		RootResourceV11 apiRoot = new ClouderaManagerClientBuilder().withHost("cm.cloudera.com")
				.withUsernamePassword("admin", "admin").build().getRootV11();
		// List of clusters
		ApiClusterList clusters = apiRoot.getClustersResource().readClusters(DataView.SUMMARY);
		for (ApiCluster cluster : clusters) {
		  System.out.printf("{}: {}", cluster.getName(), cluster.getVersion());
		}

		// Start the first cluster
		ApiCommand cmd = apiRoot.getClustersResource().startCommand(clusters.get(0).getName());
		while (cmd.isActive()) {
		   Thread.sleep(100);
		   cmd = apiRoot.getCommandsResource().readCommand(cmd.getId());
		}
		System.out.printf("Cluster start {}", cmd.getSuccess() ? "succeeded" : "failed " + cmd.getResultMessage());
	}
}
