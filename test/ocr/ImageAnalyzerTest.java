package ocr;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ocr.ImageAnalyzer;

import static org.junit.Assert.*;

/**
 * Created by Oscar Reyes on 4/23/2017.
 */
public class ImageAnalyzerTest {
    @Test
    public void analyzeImage() throws Exception {
        File testImage = new File("tempPic/screenshot.png");
        ImageAnalyzer ia = new ImageAnalyzer();
        ia.analyzeImage(testImage);
    }
}