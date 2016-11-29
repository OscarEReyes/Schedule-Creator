package ocr;

import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ImageAnalyzer {
	
	public String analyzeImage(File imageFile){
		ITesseract instance = new Tesseract();

		try {
			instance.setDatapath("C:\\Users\\oscarjr\\Documents\\Programming\\workspace\\scheduleCreator\\tessdata");
			String result = instance.doOCR(imageFile);
			return result;
		} catch (TesseractException e){
			System.err.println(e.getMessage());
			return null;
		}
	}
}
