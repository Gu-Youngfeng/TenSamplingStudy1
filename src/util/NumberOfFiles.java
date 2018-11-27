package util;

import java.io.File;

/**
 * <p>NumberOfFiles 类只包含 1 个静态方法 {@link#listAllFiles}，该方法能返回指定路径下的所有 C 文件。</p>
 * @editor yongfeng
 *
 */
public class NumberOfFiles {
	/** 文件个数 */
	public static int files = 0;
	
	public static void main(String[] args) {
		NumberOfFiles.listAllFiles(new File("code")); // CHANGE1: code 源代码需要下载，这里省去
		NumberOfFiles.listAllFiles(new File("bugs"));
		System.out.println(NumberOfFiles.files);
	}
	
	/***
	 * <p>查看 path 路径下的 C 文件个数，并将文件个数赋值到变量 {@link#files} 中。</p>
	 * @param path 文件路径
	 */
	public static void listAllFiles(File path) {
		if (path.isDirectory()){
			for (File file : path.listFiles()){
				listAllFiles(file);
			}
		} else {
			if (path.getName().endsWith(".c")){
				files++;
			}
		}
	}
	
	
}
