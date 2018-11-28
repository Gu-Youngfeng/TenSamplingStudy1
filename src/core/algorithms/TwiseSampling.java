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
 * <p><b>TwiseSampling T ���ǲ���</b> ��������̰�Ĳ���ѡȡ�ܹ����㸲������ T �����������С�����ռ䡣</p>
 * @author yongfeng
 */
public class TwiseSampling extends SamplingAlgorithm {
	/** T-wise �� T ֵ */
	private int t;
	/** TwsieSampling �Ĺ��캯���� ��ʼ�� T ֵ�� */
	public TwiseSampling(int t) { // ��ʼ�� T ֵ
		this.t = t;
	}
	
	/**
	 * <p>���� T ���ǲ����ķ�����ȡ file �ļ��е����ã�����������������ĸ���������</p>
	 * <p>���������Ѿ�ʹ���� SPLCAT ��������� T-wise �µĸ��Ǿ��� (����ڸ�Ŀ¼�µ� tables �ļ�����)�����ֻ��Ҫƥ����Ӧ�� ������� ���� T ֵ����ƥ�䵽��ȷ�ĸ��Ǿ���</p>
	 * @param file C �ļ�
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
