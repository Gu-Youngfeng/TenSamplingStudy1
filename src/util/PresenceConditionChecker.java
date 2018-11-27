package util;

import java.io.File;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/***
 * <p>PresenceConditionChecker 类提供了访问 bugs.xls 文件的方法，能够读出每个 bug 的触发配置组合。</p>
 * @editor yongfeng
 *
 */
public class PresenceConditionChecker {

	public static void main(String[] args) throws BiffException, IOException {
		File inputWorkbook = new File("bugs.xls");
		Workbook w = Workbook.getWorkbook(inputWorkbook); // 建立 Workbook 对象
		Sheet sheet = w.getSheet(0); // 获取第 1 个表单
		
		String /*project, path,*/ presenceCondition = null;
		
		for (int i = 0; i < sheet.getRows(); i++) {
			
			//project = sheet.getCell(0, i).getContents();
			//path = sheet.getCell(3, i).getContents();
			presenceCondition = sheet.getCell(4, i).getContents(); // 触发错误的配置组合，位于 xls 表第 5 列
			
			presenceCondition = presenceCondition.replaceAll("\\s", "");
			String[] options = presenceCondition.split("\\)\\|\\|\\("); // 按或将配置组合分组
			
			String pc = null; // 包含 && 元素最少的的一个配置组合
			for (String option : options){
				if (pc == null){
					pc = option;
				} else {
					
					int countPC = pc.split("&&").length;
					int countOpt = option.split("&&").length;
					
					if (countOpt < countPC){
						pc = option;
					}
					
				}
			}
			
			String[] macros = pc.split("&&"); // macros 包含单独的配置项
			int enabled = 0;
			int disabled = 0;
			
			for (String macro : macros){
				macro = macro.replace("(", "").replace(")", "").trim();
				if (macro.startsWith("!")){
					disabled++;
				} else {
					enabled++;
				}
			}
			
//			System.out.println(enabled + " | " + disabled); // CHANGE2: complete information
			System.out.println(pc + ":" + enabled + " | " + disabled);
			
		}
	}
	
}
