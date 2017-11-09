package ocr;

import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ImageAnalyzer {
	final static String TESSDATA_DATAPATH = "C:\\Users\\oscarjr\\Documents\\Programming\\workspace\\scheduleCreator\\tessdata";
	public static String analyzeImage(File imageFile){
		ITesseract instance = new Tesseract();
		try {
			instance.setDatapath(TESSDATA_DATAPATH);
			String result = instance.doOCR(imageFile);
			System.out.println(result);
			return result;
		} catch (TesseractException e){
			System.err.println(e.getMessage());
			return "";
		}
	}
}
