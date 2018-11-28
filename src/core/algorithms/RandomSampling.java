package core.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.SamplingAlgorithm;

/**
 * <p><b>RandomSampling 随机采样</b> 方法随机从某个 C 文件中抽取一定数量的配置，是一种较稳定的基线方法。  </p>
 * @editor yongfeng
 *
 */
public class RandomSampling extends SamplingAlgorithm {
	/** 采样个数 */
	public static int NUMBER_CONFIGS = 0;
	
	/**
	 * <p>利用随机采样的方法抽取 file 文件中的配置，采样个数由 {@link#NUMBER_CONFIGS} 控制。</p>
	 * @param file C 文件
	 */
	@Override
	public List<List<String>> getSamples(File file) throws Exception {
		List<List<String>> configurations = new ArrayList<>();
		directives = this.getDirectives(file);

		if (directives.size() > 0
//				&& NUMBER代抽样算法必须实现的方法_CONFIGS < Math.pow(directives.size(), 2)) { // BUG1: this line should be Math.pow(2, directives.size())
				&& NUMBER_CONFIGS < Math.pow(2, directives.size())) {

			for (int j = 0; j < RandomSampling.NUMBER_CONFIGS; j++) {
				// It sets or not-sets each configuration..
				List<String> configuration = new ArrayList<>();
				for (int i = 0; i < (directives.size()); i++) { // 随机添加配置项前的符号，即 enable 和 disable
					if (this.getRandomBoolean()) {
						configuration.add(directives.get(i));
					} else {
						configuration.add("!" + directives.get(i));
					}
				}
				if (!configurations.contains(configuration)) { // 如果该配置组合未被采样过，则添加至 configurations 中
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
