package Testing.Automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class mappingTest {
	
	public static boolean inMergedRegion(Cell c, List<CellRangeAddress> sh)
	{
		for (CellRangeAddress mergedRegion : sh) {
		      if (mergedRegion.isInRange(c.getRowIndex(), c.getColumnIndex())) {
		         // This region contains the cell in question
		         return true;
		      }
		   }
		return false;
	}
	
	public static CellRangeAddress getMergedRegionForCell(Cell c, List<CellRangeAddress> sh) {
		   for (CellRangeAddress mergedRegion : sh) {
		      if (mergedRegion.isInRange(c.getRowIndex(), c.getColumnIndex())) {
		         return mergedRegion;
		      }
		   }
		   return null;
		}
	
	public static void parseForDocSpec(Cell c, CellRangeAddress cra)
	{
		if(cra == null)
		{
			int rowIndex = c.getRowIndex();
			
		}
	}
	
	public static void main(String[] args) throws IOException {
		
			File myFile = new File("Specs_Attribute_Mapping_V0.7.xlsx");
			FileInputStream fis = new FileInputStream(myFile);
			Workbook workbook = new XSSFWorkbook(fis);
			Sheet sh = workbook.getSheet("Out-ref-20");
			List<CellRangeAddress> mergedCellRanges = sh.getMergedRegions();
			boolean imc = false;
			CellRangeAddress cra = null;
			
			int rowCount = sh.getPhysicalNumberOfRows();
			for(int j = 1; j< rowCount; j++)
			{
				imc = false;
				cra = null;
				Cell c = sh.getRow(j).getCell(1);
				if(c != null)
				{
					if(inMergedRegion(c,mergedCellRanges))
					{
						imc = true;
						cra = getMergedRegionForCell(c, mergedCellRanges);
						j = j + cra.getLastRow() - c.getRowIndex();
					}
					System.out.println(c.getAddress().toString() + " : " + sh.getRow(c.getRowIndex()).getCell(1).getStringCellValue());
					if(imc)
					{
						parseForDocSpec(null, cra);
					}
					else
					{
						parseForDocSpec(c, null);
					}
				}
			}
			

		}
}
