package com.codeboy.hadoop.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPOutputStream;

/**
 * 
 * @author codeboyyong
 *
 */
public class FileUtil {
	public static final String ENCODING = "utf-8";
	public static final String lineSeparator = System
			.getProperty("line.separator");

	

	public static String readAllContent(String filepath) throws Exception {
		// summary: reads a file and returns a string

		File file = new File(filepath);
		InputStream is = new FileInputStream(file);
		return readFromInputStream(is);
	}

	public static String readFromInputStream(InputStream is) throws Exception {
		return readFromInputStream(is, null);

	}

	private static String readFromInputStream(InputStream is, String encoding)
			throws Exception {
		if (encoding == null) {
			encoding = ENCODING;
		}

		BufferedReader input = new java.io.BufferedReader(
				new InputStreamReader(is, encoding));
		try {
			StringBuffer stringBuffer = new StringBuffer();
			String line = input.readLine();

			// Byte Order Mark (BOM) - The Unicode Standard, version 3.0, page
			// 324
			// http://www.unicode.org/faq/utf_bom.html

			// Note that when we use utf-8, the BOM should appear as "EF BB BF",
			// but it doesn't due to this bug in the JDK:
			// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4508058
			if (line != null && line.length() > 0 && line.charAt(0) == 0xfeff) {
				// Eat the BOM, since we've already found the encoding on this
				// file,
				// and we plan to concatenating this buffer with others; the BOM
				// should
				// only appear at the top of a file.
				line = line.substring(1);
			}
			while (line != null) {
				stringBuffer.append(line);
				stringBuffer.append(lineSeparator);
				line = input.readLine();
			}
			// Make sure we return a JavaScript string and not a Java string.
			return stringBuffer.toString(); // String
		} finally {
			input.close();
		}
	}

	public static void writeToFile(String path, String contents,
			String encoding, boolean useGzip) throws IOException {
		// summary: writes a file
		if (encoding == null) {
			encoding = ENCODING;
		}
		File file = new File(path);

		// Make sure destination dir exists.
		File parentDir = file.getParentFile();
		if (!parentDir.exists()) {
			if (!parentDir.mkdirs()) {
				throw new IOException("Could not create directory: "
						+ parentDir.getAbsolutePath());
			}
		}

		OutputStream outStream = new FileOutputStream(file);
		if (useGzip) {
			outStream = new GZIPOutputStream(outStream);
		} else {

		}

		BufferedWriter output = new java.io.BufferedWriter(
				new OutputStreamWriter(outStream, encoding));
		try {
			output.append(contents);
		} finally {
			output.close();
		}

	}


}
