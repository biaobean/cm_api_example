package com.cloudera.cm.client;

import com.cloudera.api.ClouderaManagerClientBuilder;
import com.cloudera.api.model.ApiCluster;
import com.cloudera.api.model.ApiClusterList;
import com.cloudera.api.model.ApiClusterVersion;
import com.cloudera.api.v7.ClustersResourceV7;
import com.cloudera.api.v7.RootResourceV7;

public class TestException {

	public static void main(String[] args) {

//		curl -X POST -H "Content-Type:application/json" -u admin:admin -d '{ "items": [ { "name": "Cluster-1", "version": "CDH5" } ] }' 'http://52.78.44.52:7180/api/v7/clusters'

		String hostname = "localhost";
		if ((args != null) && (args.length > 0)) {
			hostname = args[0];
		}
		
		RootResourceV7 apiRoot = new ClouderaManagerClientBuilder().withHost(hostname)
				.withUsernamePassword("admin", "admin").build().getRootV7();
		
		ClustersResourceV7 clustersResource = apiRoot.getClustersResource();
		ApiCluster apiCluster = new ApiCluster();
		apiCluster.setName("Cluster-1");
		apiCluster.setDisplayName("Cluster-1");
		apiCluster.setVersion(ApiClusterVersion.CDH5);
		apiCluster.setFullVersion("5.7.0");

		ApiClusterList cl = new ApiClusterList();
		cl.add(apiCluster);

		try {
			clustersResource.createClusters(cl);

			// Test exception
//			{
//			  "message" : "Duplicate entry 'Cluster-1' for key 'NAME'",
//			  "causes" : [ "Duplicate entry 'Cluster-1' for key 'NAME'" ]
//			}
			clustersResource.createClusters(cl);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.err.println(e.getLocalizedMessage());
			System.err.println(e.getCause());
			e.printStackTrace();
		}
	}
}
