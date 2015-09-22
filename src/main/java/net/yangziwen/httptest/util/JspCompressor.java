package net.yangziwen.httptest.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

public class JspCompressor {
	
	private static final FilenameFilter FILE_FILTER = new OrFileFilter(new SuffixFileFilter("jsp"), DirectoryFileFilter.INSTANCE) ;
	
	private static final Charset JSP_CHARSET = Charset.forName("UTF-8");

	public static void main(String[] args) {
		if(args == null || args.length < 3) {
			System.err.println("[ERROR] Invalid parameters!");
			System.exit(1);
		}
		String finalName = args[0];
		File jspBaseDir = new File(FilenameUtils.concat(args[1], "src/main/webapp/WEB-INF/view"));
		File jspBuildDir = new File(FilenameUtils.concat(args[2], finalName + "/WEB-INF/view"));
		if(!jspBaseDir.exists()) {
			System.err.println("[ERROR] JSP root directory does not exist! " + jspBaseDir.getAbsolutePath());
			System.exit(1);
		}
		compressJsp("", jspBaseDir.getAbsolutePath(), jspBuildDir.getAbsolutePath());
	}
	
	private static void compressJsp(String filePath, String basePath, String buildPath) {
		File file = new File(FilenameUtils.concat(basePath, filePath));
		if(!file.exists()) {
			return;
		}
		if(file.isDirectory()) {
			for(String subPath: file.list(FILE_FILTER)) {
				String subFilePath = StringUtils.isBlank(filePath)
						? subPath: filePath + SystemUtils.FILE_SEPARATOR + subPath;
				compressJsp(subFilePath, basePath, buildPath);
			}
			return;
		}
		try {
			doCompressJsp(filePath, basePath, buildPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void doCompressJsp(String filePath, String basePath, String buildPath) throws IOException {
		File srcFile = new File(FilenameUtils.concat(basePath, filePath));
		File targetFile = new File(FilenameUtils.concat(buildPath, filePath));
		String content = FileUtils.readFileToString(srcFile, JSP_CHARSET)
			.replaceAll(">(?:\\s|\n)+<", "><")		// 简易的实现下将jsp压缩为1行的逻辑
			.replaceAll("<!--.*?-->", "")
			.replaceAll("(?:\n|\\s)+", " ")
			.replaceAll("\n", " ")
		;
		FileUtils.writeStringToFile(targetFile, content, JSP_CHARSET);
		long srcSize = FileUtils.sizeOf(srcFile);
		long targetSize = FileUtils.sizeOf(targetFile);
		System.out.println(String.format("[INFO] %s (%s) -> (%s)[%s]",
			filePath,
			toDisplaySize(srcSize),
			toDisplaySize(targetSize),
			toFixed(targetSize * 100d / srcSize, 2, "%")
		));
	}
	
	private static final String[] SIZE_UNITS = {"b", "kb", "mb", "gb", "tb", "pb"};
	
	private static String toDisplaySize(long size) {
		double s = size + 0d;
		int i = 0, l = SIZE_UNITS.length;
		for(; i < l; i++) {
			if(s / 1024 < 1 || i == l - 1) {
				return toFixed(s, 2, SIZE_UNITS[i]);
			}
			s /= 1024;
		}
		throw new IllegalStateException("Impossible to touch this line!");
	}
	
	private static String toFixed(double num, int digits, String unit) {
		if(unit == null) {
			unit = "";
		}
		return String.format("%." + digits + "f", num).replaceFirst("\\.0+$", "") + unit;
	}
	
}
