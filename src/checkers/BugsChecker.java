package checkers;

import java.io.File;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import core.SamplingAlgorithm;
import core.algorithms.AllEnabledDisabledSampling;
import core.algorithms.OneDisabledSampling;
import core.algorithms.OneEnabledSampling;
import core.algorithms.TwiseSampling;

/***
 * <p>BugsChecker 类比较了 {@link OneEnabledSampling}，{@link OneDisabledSampling}，{@link AllEnabledDisabledSampling} 
 * 和 {@link TwiseSampling} 的采样方法，计算了他们的检错个数以及检错效率。</p>
 * @editor yongfeng
 *
 */
public class BugsChecker {
	/** BugsChecker 类变量：135 个 bug 所对应的 C 文件的根目录所在地 */
	public static final String SOURCE_LOCATION = "bugs/";
	/** BugsChecker 类变量：采样样本个数 */
	public int configurations = 0;
	/** BugsChecker 类变量：发现 bug 个数 */
	public int bugs = 0;
	
	public static void main(String[] args) throws Exception {
		
		double start = System.currentTimeMillis(); // 开始计时
		BugsChecker checker = new BugsChecker(); // 创建 BugsChecker 对象 
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("One-Enabled Sampling");
		checker.checkhSampling(new OneEnabledSampling()); // 检查 OneEnabledSampling 方法的检错能力
		double end = System.currentTimeMillis(); // 结束计时
		System.out.println("Time: " + (end-start) + "\n");
		
		start = System.currentTimeMillis();
		checker = new BugsChecker();
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("One-Disabled Sampling");
		checker.checkhSampling(new OneDisabledSampling()); // 检查 OneDisabledSampling 方法的检错能力
		end = System.currentTimeMillis();
		System.out.println("Time: " + (end-start) + "\n");
		
		start = System.currentTimeMillis();
		checker = new BugsChecker();
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("All-Enabled-Disabled Sampling");
		checker.checkhSampling(new AllEnabledDisabledSampling()); // 检查 AllEnabledDisabledSampling 方法的检错能力
		end = System.currentTimeMillis();
		System.out.println("Time: " + (end-start) + "\n");
		
		start = System.currentTimeMillis();
		checker = new BugsChecker();
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("Pair-wise Sampling");
		checker.checkhSampling(new TwiseSampling(2)); // 检查 TwiseSampling (T=2) 方法的检错能力
		end = System.currentTimeMillis();
		System.out.println("Time: " + (end-start) + "\n");
		
		start = System.currentTimeMillis();
		checker = new BugsChecker();
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("Three-wise Sampling");
		checker.checkhSampling(new TwiseSampling(3)); // 检查 TwiseSampling (T=3) 方法的检错能力
		end = System.currentTimeMillis();
		System.out.println("Time: " + (end-start) + "\n");

		start = System.currentTimeMillis();
		checker = new BugsChecker();
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("Four-wise Sampling");
		checker.checkhSampling(new TwiseSampling(4)); // 检查 TwiseSampling (T=4) 方法的检错能力
		end = System.currentTimeMillis();
		System.out.println("Time: " + (end-start) + "\n");
		
		start = System.currentTimeMillis();
		checker = new BugsChecker();
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("Five-wise Sampling");
		checker.checkhSampling(new TwiseSampling(5)); // 检查 TwiseSampling (T=5) 方法的检错能力
		end = System.currentTimeMillis();
		System.out.println("Time: " + (end-start) + "\n");
		
		start = System.currentTimeMillis();
		checker = new BugsChecker();
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("Six-wise Sampling");
		checker.checkhSampling(new TwiseSampling(6)); // 检查 TwiseSampling (T=6) 方法的检错能力
		end = System.currentTimeMillis();
		System.out.println("Time: " + (end-start) + "\n");
	}

	public void checkhSampling(SamplingAlgorithm algorithm) throws Exception{
		File inputWorkbook = new File("bugs.xls");
		Workbook w = Workbook.getWorkbook(inputWorkbook);
		Sheet sheet = w.getSheet(0); // 读取 bugs.xls 文件的第 1 个表单 (其实该文件也只有 1 个表单)
		
		String project, path, presenceCondition = null;
		
		for (int i = 0; i < sheet.getRows(); i++) { // 针对 bugs.xls 中的每一行，即针对 135  个 bug 中的每一个 bug
			
			project = sheet.getCell(0, i).getContents(); // 第 1 列：项目名称
			path = sheet.getCell(3, i).getContents(); // 第 4 列：C 文件相对路径
			presenceCondition = sheet.getCell(4, i).getContents(); // 第 5 列：触发 bug 的配置组合
			
			presenceCondition = presenceCondition.replaceAll("\\s", "");
			String[] options = presenceCondition.split("\\)\\|\\|\\(");
			boolean detected = false; // 标记该行 bug 是否被检测出来
			
			for (String option : options){
				String[] macros = option.split("&&");
				
				this.checkingMissingMacros(new File(BugsChecker.SOURCE_LOCATION + project + "/" + path), macros);
				
				List<List<String>> samplings = algorithm.getSamples(new File(BugsChecker.SOURCE_LOCATION + project + "/" + path));
//				this.configurations += samplings.size(); // BUG1: here we should re-assign 0 to this.configurations
				
				detected = this.doesSamplingWork(macros, samplings);
				if (detected){
					bugs++;
					break;
				}	
			}
		}
		
		// It counts the number of configurations in C source files without faults.
		this.listAllFiles(new File("code"), algorithm);
		
		System.out.println("Bugs: " + bugs);
		System.out.println("Configurations:" + configurations);
		
		// Total number of configuration / total number of files in all projects.
		System.out.println("Configurations per file:" + ((double)configurations)/50078);
		
	}
	
	/***
	 * <p>遍历路径 path 底下的所有 C 文件，并使用采样方法 algorithm 获取采样样本，并将样本个数添加至 {@link#configurations} 中。</p>
	 * @param path
	 * @param algorithm
	 * @throws Exception
	 */
	public void listAllFiles(File path, SamplingAlgorithm algorithm) throws Exception{
		if (path.isDirectory()){
			for (File file : path.listFiles()){
				this.listAllFiles(file, algorithm);
			}
		} else {
			if (path.getName().endsWith(".c")){
				List<List<String>> samplings = algorithm.getSamples(path);
				this.configurations += samplings.size();
			}
		}
	}
	
	/***
	 * <p>判断使用采样方法获取的 样本集合 samplings 是否能够触发这个 bug。</p>
	 * @param macros 从 bugs.xls 文件中读取到的触发错误的配置组合
	 * @param samplings 使用采样方法得到的样本集合
	 * @return true or false
	 * @throws Exception
	 */
	public boolean doesSamplingWork(String[] macros, List<List<String>> samplings) throws Exception{
		for (List<String> configuration : samplings){
			boolean containsAll = true;
			
			for (String macro : macros){
				macro = macro.replace("(", "").replace(")", "").replaceAll("\\s", "");
				
				if (!configuration.contains(macro)){
					containsAll = false;
				}
			}
			if (containsAll){
				return true;
			}
		}
		return false;
	}
	
	/***
	 * <p> 检查 macros 是否在 file 文件中，若不在则会打印 "PROBLEM:..." 语句。</p>
	 * @param file C 文件
	 * @param macros 带有 ！或不带有 ！的配置项数组，如 !a,b,!c 等
	 * @throws Exception
	 */
	public void checkingMissingMacros(File file, String[] macros) throws Exception{
		
		SamplingAlgorithm pairwise = new OneDisabledSampling();
		pairwise.getSamples(file);
		List<String> directives = pairwise.getDirectives(file);
		
		for (String macro : macros){
			macro = macro.replace("!", "").replace("(", "").replace(")", "");
			if (!directives.contains(macro)){
				System.out.println("PROBLEM: " + file.getAbsolutePath());
				System.out.println("PROBLEM: " + directives);
				System.out.println("PROBLEM: " + macro);
			}
		}

	}
	
}
