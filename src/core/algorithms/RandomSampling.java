package core.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.SamplingAlgorithm;

/**
 * <p><b>RandomSampling �������</b> ���������ĳ�� C �ļ��г�ȡһ�����������ã���һ�ֽ��ȶ��Ļ��߷�����  </p>
 * @editor yongfeng
 *
 */
public class RandomSampling extends SamplingAlgorithm {
	/** �������� */
	public static int NUMBER_CONFIGS = 0;
	
	/**
	 * <p>������������ķ�����ȡ file �ļ��е����ã����������� {@link#NUMBER_CONFIGS} ���ơ�</p>
	 * @param file C �ļ�
	 */
	@Override
	public List<List<String>> getSamples(File file) throws Exception {
		List<List<String>> configurations = new ArrayList<>();
		directives = this.getDirectives(file);

		if (directives.size() > 0
//				&& NUMBER�������㷨����ʵ�ֵķ���_CONFIGS < Math.pow(directives.size(), 2)) { // BUG1: this line should be Math.pow(2, directives.size())
				&& NUMBER_CONFIGS < Math.pow(2, directives.size())) {

			for (int j = 0; j < RandomSampling.NUMBER_CONFIGS; j++) {
				// It sets or not-sets each configuration..
				List<String> configuration = new ArrayList<>();
				for (int i = 0; i < (directives.size()); i++) { // ������������ǰ�ķ��ţ��� enable �� disable
					if (this.getRandomBoolean()) {
						configuration.add(directives.get(i));
					} else {
						configuration.add("!" + directives.get(i));
					}
				}
				if (!configurations.contains(configuration)) { // ������������δ����������������� configurations ��
					configurations.add(configuration);
				}
			}

		} else {
//			if (NUMBER_CONFIGS >= Math.pow(directives.size(), 2)) { // BUG2: this line should be Math.pow(2, directives.size())
			if (NUMBER_CONFIGS >= Math.pow(2, directives.size())) {
			configurations = this.powerSet(directives);
			}
		}

		if (configurations.size() == 0) {
			configurations.add(new ArrayList<String>());
		}

		// It gets each configuration and adds an #UNDEF for the macros that are
		// not active..
		for (List<String> configuration : configurations) {
			for (String directive : directives) {
				if (!configuration.contains(directive)
						&& !configuration.contains("!" + directive)) {
					configuration.add("!" + directive);
				}
			}
		}

		return configurations;
	}

	public boolean getRandomBoolean() {
		Random random = new Random();
		return random.nextBoolean();
	}

}
