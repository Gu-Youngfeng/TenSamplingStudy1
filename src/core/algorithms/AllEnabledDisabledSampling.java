package core.algorithms;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import core.SamplingAlgorithm;

/**
 * <p><b>AllEnabledDisabledSampling 全启禁采样</b> 方法抽取 2 个配置样本，第 1 个样本精通全部配置项，第 2 个样本启用全部配置项。</p>
 * @author yongfeng
 */
public class AllEnabledDisabledSampling extends SamplingAlgorithm{

	/**
	 * <p>利用全启禁采样的方法抽取 file 文件中的配置，采样个数为 2。</p>
	 * @param file C 文件
	 */
	@Override
	public List<List<String>> getSamples(File file) throws Exception {
		List<List<String>> configurations = new ArrayList<>();
		directives = this.getDirectives(file);
		
		if (directives.size() > 0){
		
			// All macros activated..
			List<String> allActive = new ArrayList<String>();
			for (String d : directives){
				allActive.add(d); // 启用全部配置项
			}
			configurations.add(allActive);
		
			
			// All macros activated..
			List<String> allDeactive = new ArrayList<String>();
			for (String d : directives){
				allDeactive.add("!" + d); // 禁用全部配置项
			}
			configurations.add(allDeactive);
		
		} else {
			configurations.add(new ArrayList<String>());
		}
		
		return configurations;
	}
	
}
