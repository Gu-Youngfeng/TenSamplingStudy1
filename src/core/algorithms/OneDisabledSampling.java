package core.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core.SamplingAlgorithm;

/**
 * <p><b>OneDisabledSampling ��һ����</b> ����ÿ�ν��� 1 ����������������������</p>
 * @author yongfeng
 */
public class OneDisabledSampling extends SamplingAlgorithm{

	/**
	 * <p>���ý�һ�����ķ�����ȡ file �ļ��е����ã�����������������ĸ������� directives��</p>
	 * @param file C �ļ�
	 */
	@Override
	public List<List<String>> getSamples(File file) throws Exception {
		List<List<String>> configurations = new ArrayList<>();
		directives = this.getDirectives(file);
		
		// It activates one macro at time separately..
		for (int i = 0; i < (directives.size()); i++){
			List<String> configuration = new ArrayList<>();
			configuration.add("!" + directives.get(i)); // ÿ�ν��� 1 ��
			configurations.add(configuration);
		}
		
		if (configurations.size() == 0){
			configurations.add(new ArrayList<String>());
		}
		
		// It gets each configuration and add #DEFINE for the macros that are not deactivated..
		for (List<String> configuration : configurations){
			for (String directive : directives){
				if (!configuration.contains(directive) && !configuration.contains("!" + directive)){
					configuration.add(directive);
				}
			}
		}
		
		return configurations;
	}

}
