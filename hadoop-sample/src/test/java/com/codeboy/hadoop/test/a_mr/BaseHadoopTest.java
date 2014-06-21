package com.codeboy.hadoop.test.a_mr;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hadoop.conf.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.codeboy.hadoop.util.HadoopClusterUtil;
import com.codeboy.hadoop.util.HadoopFileUtil;

/**
 * This class is used to create a Mini Cluster. It is essentially a knock off of
 * the standard Hadoop class ClusterMapReduceTestCase with the change that
 * cluster/config are accessible to child classes, the configuration uses fixed
 * setting to give the cluster an identity and the starting and stopping is done
 * once in the BeforeClass and AfterClass, which means the cluster only gets
 * started once, as opposed to being started and stopped for each test.
 */

public abstract class BaseHadoopTest {
	static TestClusterManager testClusterManager = TestClusterManager.INSTANCE;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testClusterManager.startUp();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		testClusterManager.shutDown();

	}

	// /com/codeboy/hadoop/resource/testdata/wordcount_input.txt
	protected void copyToHadoop(String localResourcePath,
			String targetHDFSFilePath) throws IOException {
		InputStream inputStream = BaseHadoopTest.class
				.getResourceAsStream(localResourcePath);
		HadoopFileUtil
				.writeInputStreamIntoHDFSFile(inputStream, testClusterManager
						.getHadoopClusterManager().getHadoopCluster(),
						targetHDFSFilePath);
	}

	protected void deleteFile(String targetHDFSFilePath) throws IOException {

		HadoopFileUtil.deleteHDFSFile(testClusterManager
				.getHadoopClusterManager().getHadoopCluster(),
				targetHDFSFilePath);
	}

	public Configuration getHadoopConfiguration() {
		return HadoopClusterUtil.toHadoopConfiguration(testClusterManager
				.getHadoopClusterManager().getHadoopCluster());
	}

	protected static void printOutputFile(final Configuration conf,
			String outputPath) throws  Exception {
		HadoopFileUtil.printMapOutput(outputPath, conf);

		HadoopFileUtil.printReducerOutput(outputPath, conf);
	}

}
