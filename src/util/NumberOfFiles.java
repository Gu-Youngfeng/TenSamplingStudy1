package util;

import java.io.File;

/**
 * <p>NumberOfFiles ��ֻ���� 1 ����̬���� {@link#listAllFiles}���÷����ܷ���ָ��·���µ����� C �ļ���</p>
 * @editor yongfeng
 *
 */
public class NumberOfFiles {
	/** �ļ����� */
	public static int files = 0;
	
	public static void main(String[] args) {
		NumberOfFiles.listAllFiles(new File("code")); // CHANGE1: code Դ������Ҫ���أ�����ʡȥ
		NumberOfFiles.listAllFiles(new File("bugs"));
		System.out.println(NumberOfFiles.files);
	}
	
	/***
	 * <p>�鿴 path ·���µ� C �ļ������������ļ�������ֵ������ {@link#files} �С�</p>
	 * @param path �ļ�·��
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
