package com.codeboy.hadoop.test.a_mr;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.codeboy.hadoop.base.HadoopCluster;
import com.codeboy.hadoop.base.HadoopCluster.ClusterType;
import com.codeboy.hadoop.base.HadoopClusterManager;
import com.codeboy.hadoop.base.HadoopClusterManagerForMini;
import com.codeboy.hadoop.test.a_mr.a_wordcount.WordCountTest;
import com.codeboy.hadoop.util.HadoopClusterUtil;

public class TestClusterManager {
 
	private HadoopClusterManager hadoopClusterManager = null;
	private Properties overrideProperties;
	
	public TestClusterManager(Properties overrideProperties) {
		this.overrideProperties=overrideProperties; 
	}
	public TestClusterManager(   ) {
		this(null);
 	}

	public HadoopClusterManager getHadoopClusterManager() {
		return hadoopClusterManager;
	}

	protected static String LOG_DIR = "/tmp/logs";

	

	// no stop method since we want to reuse the minicluster for all test
	public boolean startUp() throws Exception {
		if (hadoopClusterManager == null) {

			// make sure the log dir exists
			File logPath = new File(LOG_DIR);
			if (!logPath.exists()) {
				logPath.mkdirs();
			}
			// configure and start the cluster
			System.setProperty("hadoop.log.dir", LOG_DIR);
			System.setProperty("javax.xml.parsers.SAXParserFactory",
					"com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
			System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
					"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");

			InputStream jsonFileInputStream = WordCountTest.class
					.getResourceAsStream("/com/codeboy/hadoop/resource/cluster/conf/"+System.getProperty("user.name")
							+ "/TestCluster.json");
			try {
				HadoopCluster hadoopCluster = HadoopClusterUtil .readHadoopClusterFromJsonInputStram(jsonFileInputStream);
				hadoopCluster.setCustomizedPorpeties(overrideProperties);
				
				if (hadoopCluster.getType().equals(ClusterType.LOCAL)) {

				} else if (hadoopCluster.getType().equals(ClusterType.REMOTE)) {

				} else if (hadoopCluster.getType().equals(ClusterType.MINI)) {
					hadoopClusterManager = new HadoopClusterManagerForMini(
							hadoopCluster);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return hadoopClusterManager.startALL();
		} else {
			return true;
		}

	}

	public void cleanData() throws IOException {
		// clean up the hdfs files created by mini cluster
		String baseTempDir = "build" + File.separator + "test" + File.separator;
		String dfsDir = baseTempDir + "data";

		FileUtils.deleteDirectory(new File(dfsDir));
		String mrDir = baseTempDir + "mapred";
		FileUtils.deleteDirectory(new File(mrDir));
		FileUtils.cleanDirectory(new File(LOG_DIR));

	}

	public void shutDown() throws Exception {
		
		hadoopClusterManager.stopALL();
		this.cleanData();
		
	}

}
