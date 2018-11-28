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
 * <p>BugsChecker ��Ƚ��� {@link OneEnabledSampling}��{@link OneDisabledSampling}��{@link AllEnabledDisabledSampling} 
 * �� {@link TwiseSampling} �Ĳ������������������ǵļ������Լ����Ч�ʡ�</p>
 * @editor yongfeng
 *
 */
public class BugsChecker {
	/** BugsChecker �������135 �� bug ����Ӧ�� C �ļ��ĸ�Ŀ¼���ڵ� */
	public static final String SOURCE_LOCATION = "bugs/";
	/** BugsChecker ������������������� */
	public int configurations = 0;
	/** BugsChecker ����������� bug ���� */
	public int bugs = 0;
	
	public static void main(String[] args) throws Exception {
		
		double start = System.currentTimeMillis(); // ��ʼ��ʱ
		BugsChecker checker = new BugsChecker(); // ���� BugsChecker ���� 
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("One-Enabled Sampling");
		checker.checkhSampling(new OneEnabledSampling()); // ��� OneEnabledSampling �����ļ������
		double end = System.currentTimeMillis(); // ������ʱ
		System.out.println("Time: " + (end-start) + "\n");
		
		start = System.currentTimeMillis();
		checker = new BugsChecker();
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("One-Disabled Sampling");
		checker.checkhSampling(new OneDisabledSampling()); // ��� OneDisabledSampling �����ļ������
		end = System.currentTimeMillis();
		System.out.println("Time: " + (end-start) + "\n");
		
		start = System.currentTimeMillis();
		checker = new BugsChecker();
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("All-Enabled-Disabled Sampling");
		checker.checkhSampling(new AllEnabledDisabledSampling()); // ��� AllEnabledDisabledSampling �����ļ������
		end = System.currentTimeMillis();
		System.out.println("Time: " + (end-start) + "\n");
		
		start = System.currentTimeMillis();
		checker = new BugsChecker();
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("Pair-wise Sampling");
		checker.checkhSampling(new TwiseSampling(2)); // ��� TwiseSampling (T=2) �����ļ������
		end = System.currentTimeMillis();
		System.out.println("Time: " + (end-start) + "\n");
		
		start = System.currentTimeMillis();
		checker = new BugsChecker();
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("Three-wise Sampling");
		checker.checkhSampling(new TwiseSampling(3)); // ��� TwiseSampling (T=3) �����ļ������
		end = System.currentTimeMillis();
		System.out.println("Time: " + (end-start) + "\n");

		start = System.currentTimeMillis();
		checker = new BugsChecker();
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("Four-wise Sampling");
		checker.checkhSampling(new TwiseSampling(4)); // ��� TwiseSampling (T=4) �����ļ������
		end = System.currentTimeMillis();
		System.out.println("Time: " + (end-start) + "\n");
		
		start = System.currentTimeMillis();
		checker = new BugsChecker();
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("Five-wise Sampling");
		checker.checkhSampling(new TwiseSampling(5)); // ��� TwiseSampling (T=5) �����ļ������
		end = System.currentTimeMillis();
		System.out.println("Time: " + (end-start) + "\n");
		
		start = System.currentTimeMillis();
		checker = new BugsChecker();
		checker.configurations = 0;
		checker.bugs = 0;
		System.out.println("Six-wise Sampling");
		checker.checkhSampling(new TwiseSampling(6)); // ��� TwiseSampling (T=6) �����ļ������
		end = System.currentTimeMillis();
		System.out.println("Time: " + (end-start) + "\n");
	}

	public void checkhSampling(SamplingAlgorithm algorithm) throws Exception{
		File inputWorkbook = new File("bugs.xls");
		Workbook w = Workbook.getWorkbook(inputWorkbook);
		Sheet sheet = w.getSheet(0); // ��ȡ bugs.xls �ļ��ĵ� 1 ���� (��ʵ���ļ�Ҳֻ�� 1 ����)
		
		String project, path, presenceCondition = null;
		
		for (int i = 0; i < sheet.getRows(); i++) { // ��� bugs.xls �е�ÿһ�У������ 135  �� bug �е�ÿһ�� bug
			
			project = sheet.getCell(0, i).getContents(); // �� 1 �У���Ŀ����
			path = sheet.getCell(3, i).getContents(); // �� 4 �У�C �ļ����·��
			presenceCondition = sheet.getCell(4, i).getContents(); // �� 5 �У����� bug ���������
			
			presenceCondition = presenceCondition.replaceAll("\\s", "");
			String[] options = presenceCondition.split("\\)\\|\\|\\(");
			boolean detected = false; // ��Ǹ��� bug �Ƿ񱻼�����
			
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
	 * <p>����·�� path ���µ����� C �ļ�����ʹ�ò������� algorithm ��ȡ��������������������������� {@link#configurations} �С�</p>
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
	 * <p>�ж�ʹ�ò���������ȡ�� �������� samplings �Ƿ��ܹ�������� bug��</p>
	 * @param macros �� bugs.xls �ļ��ж�ȡ���Ĵ���������������
	 * @param samplings ʹ�ò��������õ�����������
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
	 * <p> ��� macros �Ƿ��� file �ļ��У�����������ӡ "PROBLEM:..." ��䡣</p>
	 * @param file C �ļ�
	 * @param macros ���� ���򲻴��� �������������飬�� !a,b,!c ��
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
