package core.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core.SamplingAlgorithm;

/**
 * <p><b>AllEnabledDisabledSampling ȫ��������</b> ������ȡ 2 �������������� 1 ��������ͨȫ��������� 2 ����������ȫ�������</p>
 * @author yongfeng
 */
public class AllEnabledDisabledSampling extends SamplingAlgorithm{

	/**
	 * <p>����ȫ���������ķ�����ȡ file �ļ��е����ã���������Ϊ 2��</p>
	 * @param file C �ļ�
	 */
	@Override
	public List<List<String>> getSamples(File file) throws Exception {
		List<List<String>> configurations = new ArrayList<>();
		directives = this.getDirectives(file);
		
		if (directives.size() > 0){
		
			// All macros activated..
			List<String> allActive = new ArrayList<String>();
			for (String d : directives){
				allActive.add(d); // ����ȫ��������
			}
			configurations.add(allActive);
		
			
			// All macros activated..
			List<String> allDeactive = new ArrayList<String>();
			for (String d : directives){
				allDeactive.add("!" + d); // ����ȫ��������
			}
			configurations.add(allDeactive);
		
		} else {
			configurations.add(new ArrayList<String>());
		}
		
		return configurations;
	}
	
}
