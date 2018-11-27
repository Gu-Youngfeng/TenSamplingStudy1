package util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DirectivesPerFile {

	public static List<Integer> directivesPerFile = new ArrayList<>();
	//public static int files = 47; // CHANGE1: 为什么是 47？
	public static int files = 0;
	
	public static void main(String[] args) throws Exception {
		new DirectivesPerFile().listFiles(new File("bugs"));
		System.out.println("Files: " + DirectivesPerFile.files); // 打印  bug 文件夹中的 C 文件个数
		System.out.print("listen.c: " + new DirectivesPerFile().getDirectives(new File("bugs/apache/server/mpm_common.c"))); // 打印 listen.c 文件中的配置项
		for (Integer i : DirectivesPerFile.directivesPerFile){ // 每个 C 文件中包含的配置项个数
			System.out.println(i);
		}
		/*DirectivesPerFile.listAllFiles(new File("code"));
		System.out.println(DirectivesPerFile.files);
		*/
	}
	
	/**
	 * <p>检索 path 路径底下包含的 C 文件个数，并将文件个数添加到 {@link#files} 变量中。</p>
	 * @param path 文件路径
	 * */
	public static void listAllFiles(File path) throws Exception{
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
	
	/**
	 * <p>检索 path 路径下包含的 C 文件所包含的配置项个数，并将配置项个数依次添加到 {@link#directivesPerFile} 列表中。</p>
	 */
	public void listFiles(File path) throws Exception{
		if (path.isDirectory()){
			for (File file : path.listFiles()){
				this.listFiles(file);
			}
		} else {
			if (path.getName().endsWith(".c")){
				directivesPerFile.add(this.getDirectives(path).size());
			}
		}
	}
	
	public boolean isValidJavaIdentifier(String s) {
		// An empty or null string cannot be a valid identifier.
	    if (s == null || s.length() == 0){
	    	return false;
	   	}

	    char[] c = s.toCharArray();
	    if (!Character.isJavaIdentifierStart(c[0])){
	    	return false;
	    }

	    for (int i = 1; i < c.length; i++){
	        if (!Character.isJavaIdentifierPart(c[i])){
	           return false;
	        }
	    }

	    return true;
	}
	
	// It sets the number of configurations..
	/**
	 * <p>挖掘出 C 文件中出现的配置项，注意这里挖掘出的配置项列表与 C 文件对应的 .config 文件有出入。</p>
	 * @param file 文件路径
	 * @return 配置项列表
	 */
	public List<String> getDirectives(File file) throws Exception{
		List<String> directives = new ArrayList<>();
		
		FileInputStream fstream = new FileInputStream(file);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;

		while ((strLine = br.readLine()) != null)   {
			
			strLine = strLine.trim();
			
			if (strLine.startsWith("#")){ // 替换掉 # 后面的空格和跳格
				strLine = strLine.replaceAll("#(\\s)+", "#");
				strLine = strLine.replaceAll("#(\\t)+", "#");
			}
			
			//
			if (strLine.trim().startsWith("#if") || strLine.trim().startsWith("#elif")){
				
				strLine = strLine.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","");
				
				String directive = strLine.replace("#ifdef", "").replace("#ifndef", "").replace("#if", "");
				directive = directive.replace("defined", "").replace("(", "").replace(")", "");
				directive = directive.replace("||", "").replace("&&", "").replace("!", "").replace("<", "").replace(">", "").replace("=", "");
				
				String[] directivesStr = directive.split(" ");
				
				for (int i = 0; i < directivesStr.length; i++){
					if (!directives.contains(directivesStr[i].trim()) && !directivesStr[i].trim().equals("") && this.isValidJavaIdentifier(directivesStr[i].trim())){
						directives.add(directivesStr[i].trim());
					}
				}
			}
			
			if (strLine.startsWith("#")){
				strLine = strLine.replaceAll("#(\\s)+", "#");
				strLine = strLine.replaceAll("#(\\t)+", "#");
				if (strLine.startsWith("#define")){
					String[] parts = strLine.split(" ");
					if (parts.length == 3){ // #define 后面接 2 个参数，才会将第 1 个参数给去掉
						parts[1] = parts[1].trim();
						directives.remove(parts[1]);
					}
				}
			}
			
		}
		
		String fileName = file.getName().toUpperCase() + "_H";
		directives.remove(fileName);
		
		in.close();
		return directives;
	}
	
}
