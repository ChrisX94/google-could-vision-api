package testing.googleApi.google_could_vision_api;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

// this class transform the data from google vision to  excel file 
public class ExcelHelp extends VisionHelp {

	@Override
	public void writeData(String outDir, String[] imputFileName, List<List<String[]>> dataList) {
		XSSFWorkbook wb = new XSSFWorkbook(); // 建立一個新的工作表

		
		for (int i = 0; i < imputFileName.length; i++) {
			XSSFSheet sheet = wb.createSheet(imputFileName[i]); // 在工作表中建立一個sheet
			XSSFRow headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("Label"); // title
			headerRow.createCell(1).setCellValue("Score"); // title
			List<String[]> datas = dataList.get(i); // 取得資料

			for (int j = 0; j < datas.size(); j++) {
				XSSFRow row = sheet.createRow(j+1); // 選取row的位置
				String[] rowData = datas.get(j); // 取得資料

				XSSFCell desCell = row.createCell(0); // 建立 cell 寫入label名稱
				desCell.setCellValue(datas.get(j)[0]);

				XSSFCell scoCell = row.createCell(1); // 建立 cell 寫入label分數
				scoCell.setCellValue(datas.get(j)[1]);

			}
		}

		try {
			FileOutputStream fis = new FileOutputStream(outDir + "/result.xlsx");
			wb.write(fis);
			fis.flush();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
