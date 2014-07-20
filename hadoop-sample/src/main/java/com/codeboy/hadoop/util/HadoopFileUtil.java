package com.codeboy.hadoop.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;

import com.codeboy.hadoop.base.HadoopCluster;

public class HadoopFileUtil {

	public static void writeInputStreamIntoHDFSFile(InputStream inStream,
			HadoopCluster hadoopCluster, String targetHDFSFile)
			throws IOException {

		FileSystem fs = FileSystem.get(URI.create(hadoopCluster.getHDFSUrl()),
				HadoopClusterUtil.toHadoopConfiguration(hadoopCluster));
		OutputStream out = fs.create(new Path(targetHDFSFile));
		IOUtils.copyBytes(inStream, out, 4096, true);

	}

	public static void deleteHDFSFile(HadoopCluster hadoopCluster,
			String targetHDFSFile) throws IOException {

		FileSystem fs = FileSystem.get(URI.create(hadoopCluster.getHDFSUrl()),
				HadoopClusterUtil.toHadoopConfiguration(hadoopCluster));
		fs.delete(new Path(targetHDFSFile), true);

	}

	public static void deleteHDFSFile(Configuration hadoopCluster,
			String targetHDFSFile) throws IOException {

		FileSystem fs = FileSystem.get(hadoopCluster);
		fs.delete(new Path(targetHDFSFile), true);

	}

	public static LinkedHashMap<String, String> readAllContentAsLinkedMap(
			String path, Configuration conf) throws Exception {
		FileSystem fs = FileSystem.get(conf);
		InputStream is = fs.open(new Path(path));
		return readLinkedMapFromInputStream(is);
	}

	public static void printMapOutput(String outputDir, Configuration conf)
			throws Exception {
		printFileContents(outputDir, "part-m-", conf);

	}

	public static void printReducerOutput(String outputDir, Configuration conf)
			throws Exception {
		printFileContents(outputDir, "part-r-", conf);
	}

	public static void printFileContents(String dir, String filePrefix,
			Configuration conf) throws IOException {
		FileSystem fs = FileSystem.get(conf);
		FileStatus[] files = fs.listStatus(new Path(dir));
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDir() == false
					&& files[i].getPath().getName().startsWith(filePrefix) == true
					&& files[i].getLen() > 0) {
				printFile(fs, files[i].getPath());
			}
		}

	}

	public static void printFileContent(String filePath, Configuration conf)
			throws IOException {
		FileSystem fs = FileSystem.get(conf);
		printFile(fs, new Path(filePath));
	}

	private static void printFile(FileSystem fs, Path path) throws IOException {
		InputStream is = fs.open(path);
		System.out
				.println("printFileContent:start---------------------------------------");
		System.out.println("path = " + path);

		System.out
				.println("-------------------------------------------------------------");
		BufferedReader input = new java.io.BufferedReader(
				new InputStreamReader(is, FileUtil.ENCODING));
		try {
			String line = input.readLine();
			if (line != null && line.length() > 0 && line.charAt(0) == 0xfeff) {
				line = line.substring(1);
			}
			while (line != null) {
				System.out.println(line);

				line = input.readLine();
			}
			// Make sure we return a JavaScript string and not a Java string.
		} finally {
			input.close();
		}
		System.out
				.println("printFileContent:end---------------------------------------");

	}

	private static LinkedHashMap<String, String> readLinkedMapFromInputStream(
			InputStream is) throws Exception {
		LinkedHashMap<String, String> resultMap = new LinkedHashMap<String, String>();

		BufferedReader input = new java.io.BufferedReader(
				new InputStreamReader(is, FileUtil.ENCODING));
		try {
			String line = input.readLine();
			if (line != null && line.length() > 0 && line.charAt(0) == 0xfeff) {
				line = line.substring(1);
			}
			while (line != null) {
				String[] lineData = line.split("\t");
				if (lineData.length == 2) {
					resultMap.put(lineData[0], lineData[1]);
				}
				line = input.readLine();
			}
			// Make sure we return a JavaScript string and not a Java string.
		} finally {
			input.close();
		}

		return resultMap;
	}

	public static List<String> readAllContentAsList(String filePath,
			Configuration hadoopConfiguration) throws IOException {

		List<String> resultList = new ArrayList<String>();
		FileSystem fs = FileSystem.get(hadoopConfiguration);

		InputStream is = fs.open(new Path(filePath));

		BufferedReader input = new java.io.BufferedReader(
				new InputStreamReader(is, FileUtil.ENCODING));
		try {
			String line = input.readLine();
			if (line != null && line.length() > 0 && line.charAt(0) == 0xfeff) {
				line = line.substring(1);
			}
			while (line != null) {
				resultList.add(line);
				line = input.readLine();
			}
			// Make sure we return a JavaScript string and not a Java string.
		} finally {
			input.close();
		}

		return resultList;
	}

	public static FileStatus getHDFSFileInfo(HadoopCluster hadoopCluster,
			String targetHDFSFile) throws IOException {

		FileSystem fs = FileSystem.get(URI.create(hadoopCluster.getHDFSUrl()),
				HadoopClusterUtil.toHadoopConfiguration(hadoopCluster));

		return fs.getFileStatus(new Path(targetHDFSFile));

	}

	public static void printSequenceFileContent(HadoopCluster hadoopCluster,
			String path, Configuration conf, Writable key , Writable value ) throws Exception {

		FileSystem fs = FileSystem.get(URI.create(hadoopCluster.getHDFSUrl()),
				HadoopClusterUtil.toHadoopConfiguration(hadoopCluster));

		SequenceFile.Reader reader = new SequenceFile.Reader(fs,
				new Path(path), conf);

		while (reader.next(key, value)) {
			System.out.println("(" + key + "->" + value + ")");

		}

	}
}
