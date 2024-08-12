package utlities;

import java.io.IOException;

import org.testng.annotations.DataProvider;

public class dataProvider {

	@DataProvider(name = "LoginData")
	public String[][] getData() throws IOException{
		String path = ".\\testData\\testData.xlsx";
		ExcelUtlitiy xlUtil =new ExcelUtlitiy(path);
		
		int totalRows = xlUtil.getRowCount("sheet1");
		int totalCols = xlUtil.getCellCount("sheet1", 1);
		
		String loadingData[][] = new String[totalRows][totalCols];
		
		for(int i=1; i<=totalRows;i++) {
			for(int j=0;j<totalCols;j++) {
				String currData = xlUtil.getCellDate("sheet1", i, j);
				loadingData[i-1][j] = currData;
			}
		}
		return loadingData;
	}

}
