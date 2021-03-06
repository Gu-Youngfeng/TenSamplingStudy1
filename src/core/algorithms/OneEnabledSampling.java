package core.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core.SamplingAlgorithm;

/**
 * <p><b>OneEnabledSampling 启一采样</b> 方法每次启用 1 个配置项，而禁用其余配置项。</p>
 * @author yongfeng
 */
public class OneEnabledSampling extends SamplingAlgorithm{

	/**
	 * <p>利用启一采样的方法抽取 file 文件中的配置，采样个数即配置项的个数，即 directives。</p>
	 * @param file C 文件
	 */
	@Override
	public List<List<String>> getSamples(File file) throws Exception {
		List<List<String>> configurations = new ArrayList<>();
		directives = this.getDirectives(file);
		
		// It activates each macro separately..
		for (int i = 0; i < (directives.size()); i++){
			List<String> configuration = new ArrayList<>();
			configuration.add(directives.get(i)); // 每次启用 1 个配置项
			configurations.add(configuration);
		}
		
		if (configurations.size() == 0){
			configurations.add(new ArrayList<String>());
		}
		
		// It gets each configuration and adds an #UNDEF for the macros that are not active..
		for (List<String> configuration : configurations){
			for (String directive : directives){
				if (!configuration.contains(directive) && !configuration.contains("!" + directive)){
					configuration.add("!" + directive);
				}
			}
		}
		
		return configurations;
	}

}
