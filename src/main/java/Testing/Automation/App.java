package Testing.Automation;

import java.io.FileInputStream;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class App {

	public static void main(String[] args) {
		try {
			FileInputStream fis = new FileInputStream("test.docx");
			XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
			new Parser().Parse(xdoc);

		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

}