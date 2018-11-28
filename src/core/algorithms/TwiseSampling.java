package core.algorithms;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import core.SamplingAlgorithm;

/**
 * <p><b>TwiseSampling T 覆盖采样</b> 方法采用贪心策略选取能够满足覆盖任意 T 个配置项的最小采样空间。</p>
 * @author yongfeng
 */
public class TwiseSampling extends SamplingAlgorithm {
	/** T-wise 的 T 值 */
	private int t;
	/** TwsieSampling 的构造函数， 初始化 T 值。 */
	public TwiseSampling(int t) { // 初始化 T 值
		this.t = t;
	}
	
	/**
	 * <p>利用 T 覆盖采样的方法抽取 file 文件中的配置，采样个数即配置项的个数不定。</p>
	 * <p>由于事先已经使用了 SPLCAT 工具算好了 T-wise 下的覆盖矩阵 (存放在根目录下的 tables 文件夹下)，因此只需要匹配相应的 配置项个 数和 T 值即可匹配到正确的覆盖矩阵。</p>
	 * @param file C 文件
	 */
	@Override
	public List<List<String>> getSamples(File file) throws Exception {
		List<List<String>> configurations = new ArrayList<>();
		directives = this.getDirectives(file);
		Collections.sort(directives);
		
		FileInputStream fstream = new FileInputStream("tables/" + t + "x2/ca." + t + ".2^" + directives.size() + ".txt");
		
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		String strLine;
		// Ignore first line..
		strLine = br.readLine();
		
		// Read File Line By Line
		while ((strLine = br.readLine()) != null) {
			String[] configsTable = strLine.trim().split(" ");
			List<String> configuration = new ArrayList<>();
			
			for (int i = 0; i < configsTable.length; i++){
				if (configsTable[i].equals("1")){
					configuration.add(directives.get(i));
				}
			}
			
			configurations.add(configuration);
		}
		// Close the input stream
		in.close();

		if (directives.size() == 0){
			configurations.add(new ArrayList<String>());
		}
		
		// It gets each configuration and add #UNDEF for the macros that are not active..
		for (List<String> configuration : configurations) {
			for (String directive : directives) {
				if (!configuration.contains(directive)) {
					configuration.add("!" + directive);
				}
			}
		}

		return configurations;
	}

}
