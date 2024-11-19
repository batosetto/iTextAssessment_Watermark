package org.example;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.File;
import java.io.IOException;

public class WatermarkSampleCode {
    //Directory and paths to SRC and DEST files
    public static final String BASE_URI = "src/main/results/";
    public static final String SRC = BASE_URI + "input.pdf";
    public static final String DEST = BASE_URI + "watermarked_output.pdf";

    public static void main(String[] args) throws IOException {
        //If the input file does not exist, it will be created
        File inputFile = new File(SRC);
        if (!inputFile.exists()) {
            new WatermarkSampleCode().createPdf(SRC);
        }
        //If the output file does not exist, it will be created
        File outputFile = new File(DEST);
        outputFile.getParentFile().mkdirs();

        WatermarkSampleCode watermarkSample = new WatermarkSampleCode();
        watermarkSample.manipulatePdf(SRC, DEST); //Adding watermark to the document
        watermarkSample.addTextAfterWatermark(DEST); // Adding a text on top of the watermark for testing purposes
    }

    protected void createPdf(String filePath) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(filePath));
        pdfDoc.addNewPage();
        pdfDoc.close();
    }
    protected void manipulatePdf(String inFile, String outFile) throws IOException {
        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(inFile), new PdfWriter(outFile));
            int totalPages = pdfDoc.getNumberOfPages();
            for (int i = 1; i <= totalPages; i++) {
                PdfCanvas under = new PdfCanvas(pdfDoc.getPage(i).newContentStreamBefore(), pdfDoc.getPage(i).getResources(), pdfDoc);
                Rectangle pageSize = pdfDoc.getPage(i).getPageSize();
                Canvas canvas = new Canvas(under, pageSize);

                Paragraph watermarkText = new Paragraph("Watermark Example")
                        .setFontColor(ColorConstants.GRAY)
                        .setFontSize(50)
                        .setRotationAngle(Math.toRadians(45) );

                float x = pageSize.getWidth() / 2;
                float y = pageSize.getHeight() / 2;

                float opacity = 0.5f;

                under.saveState();
                PdfExtGState gs1 = new PdfExtGState();
                gs1.setFillOpacity(opacity);
                under.setExtGState(gs1);

                canvas.showTextAligned(
                        watermarkText,
                        x, y,
                        TextAlignment.CENTER, VerticalAlignment.MIDDLE
                );

                under.restoreState();
            }
            pdfDoc.close();
            System.out.println("Watermark added successfully!");
        } catch (IOException e) {
            System.err.println("Error processing the PDF: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    protected void addTextAfterWatermark(String filePath) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(filePath), new PdfWriter(filePath.replace(".pdf", "_withText.pdf")));

        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            PdfCanvas over = new PdfCanvas(pdfDoc.getPage(i).newContentStreamAfter(), pdfDoc.getPage(i).getResources(), pdfDoc);
            Rectangle pageSize = pdfDoc.getPage(i).getPageSize();
            Canvas canvas = new Canvas(over, pageSize);

            float x = pageSize.getWidth() / 2;
            float y = pageSize.getHeight() / 2;

            PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
            Paragraph text = new Paragraph( "Testing text on top of watermark;" +
                    "\nTesting text on top of watermark;" +
                    "\nTesting text on top of watermark;" +
                    "\nTesting text on top of watermark;" +
                    "\nTesting text on top of watermark;" +
                    "\nTesting text on top of watermark;" +
                    "\nTesting text on top of watermark;" +
                    "\nTesting text on top of watermark;" +
                    "\nTesting text on top of watermark;" +
                    "\nTesting text on top of watermark;")
                    .setFont(font)
                    .setFontSize(25)
                    .setFontColor(ColorConstants.BLACK);

            canvas.showTextAligned(
                    text,
                    x, y,
                    TextAlignment.CENTER, VerticalAlignment.MIDDLE
            );
        }

        pdfDoc.close();
        System.out.println("Text added directly over watermark successfully!");
    }


}
