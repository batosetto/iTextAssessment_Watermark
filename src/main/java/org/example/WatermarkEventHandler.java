package org.example;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.IOException;

public class WatermarkEventHandler implements IEventHandler {
    //Directory and paths to SRC and DEST files
    public static final String BASE_URI = "src/main/results/";
    public static final String DEST = BASE_URI + "watermarked_output_event_handler.pdf";

    public static void main(String[] args) throws Exception{
        // Creating a new .pdf document
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        // adding a blank page to the doc
        pdfDoc.addNewPage();
        // Event handler to add the watermark whenever a page is closed
        pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new WatermarkEventHandler());
        //closing the document
        pdfDoc.close();

        WatermarkEventHandler handler = new WatermarkEventHandler();
        handler.addTextAfterWatermark(DEST);

    }

    public void handleEvent(Event event) {
        // Casting the event in order to access the pages and doc info
        PdfDocumentEvent pdfEvent = (PdfDocumentEvent) event;
        PdfPage page = pdfEvent.getPage();
        PdfDocument pdfDoc = pdfEvent.getDocument();
        //Calling the method
        manipulatePdf(pdfDoc, page);}

    private void manipulatePdf(PdfDocument pdfDoc, PdfPage page) {
        // Getting the dimensions of the page
        Rectangle pageSize = page.getPageSize();

        // Creating canvas to add the watermark , newContentStreamBefore() is used to control the flow
        PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
        // High-level canvas to add formatting - without this element, I could not implement the centered position of the watermark
        Canvas modelCanvas = new Canvas(canvas, pageSize);

        //Defining the watermark text and adding style to it
        Paragraph watermarkText = new Paragraph("Watermark Example")
                .setFontColor(ColorConstants.GRAY)
                .setFontSize(50)
                .setRotationAngle(Math.toRadians(45)); // very important to transform to radians!

        // Defining the center coordinates in the middle of the page
        float x = pageSize.getWidth() / 2;
        float y = pageSize.getHeight() / 2;

        //The graphic state Object is necessary to add the opacity setting.
        PdfExtGState gs1 = new PdfExtGState();
        gs1.setFillOpacity(0.5f); // Opacity is 50%. It goes from 0 to 1.
        canvas.saveState(); // Saving the graphic state. It's important to save it in order to isolate the properties applied only for the watermark
        canvas.setExtGState(gs1); // adding the opacity to the canvas

        //Placing the watermark in the page
        modelCanvas.showTextAligned(
                watermarkText,
                x, y,
                TextAlignment.CENTER, VerticalAlignment.MIDDLE
        );

        //Restoring the graphic state. Works with saveState() to isolate the formatting properties.
        canvas.restoreState();
    }

    protected void addTextAfterWatermark(String filePath) throws IOException {
        // Open the watermarked PDF for reading and create a new output file with text overlay
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(filePath), new PdfWriter(filePath.replace(".pdf", "_withText.pdf")));

        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
            // Creating canvas to add the text on top of the watermark , newContentStreamAfter() is used to control the flow
            PdfCanvas over = new PdfCanvas(pdfDoc.getPage(i).newContentStreamAfter(), pdfDoc.getPage(i).getResources(), pdfDoc);
            // Getting the dimensions of the page
            Rectangle pageSize = pdfDoc.getPage(i).getPageSize();
            Canvas canvas = new Canvas(over, pageSize);

            // Defining the center coordinates in the middle of the page
            float x = pageSize.getWidth() / 2;
            float y = pageSize.getHeight() / 2;

            // Styling the text
            PdfFont font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
            Paragraph text = new Paragraph("Testing text on top of watermark;" +
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

            //Placing the text in the page, on top of the watermark
            canvas.showTextAligned(
                    text,
                    x, y,
                    TextAlignment.CENTER, VerticalAlignment.MIDDLE
            );
        }

        pdfDoc.close();
        System.out.println("Text added on top of watermark.");
    }

}
