# iTextAssessment_Watermark with iText 8.0.5

## Project Overview
This project demonstrates how to automate the process of applying watermarks and overlaying additional text on PDF documents using iText 8.0.5 in Java. It includes:
1. **Event Handler-Based Automation**: Automatically applies a watermark to every page as the document is processed.

---

## Features
- **Watermark Automation**: Adds a customizable watermark to every page of a PDF document.
- **Text Overlay**: Adds additional text on top of the watermark for testing or documentation purposes.
- **Dynamic Positioning**: Ensures the watermark and text are centered, regardless of page size.
- **Opacity and Rotation**: Configurable watermark opacity and rotation for better integration with the document's content.

---

## Prerequisites
To run this project, ensure you have:
1. **Java Development Kit (JDK)**: Version 8 or later.
2. **Apache Maven**: To manage dependencies and build the project.
3. **iText 8.0.5**: Included via Maven dependencies in the `pom.xml` file.

---

## How to Use

### 1. Clone the Repository
```bash
git clone https://github.com/<your-username>/WatermarkAutomation.git
cd WatermarkAutomation
```

### 2. Build the Project
Run Maven to clean and build the project:

```bash
mvn clean install
```

### 3. Run the Project
```bash
java -cp target/<your-jar-file>.jar org.example.WatermarkEventHandler
```

### 4. Outputs
The resulting PDFs will be saved in the src/main/results folder:

- **watermarked_output_event_handler.pdf**: Generated text watermark using event handlers.
- **watermarked_output_event_handler_withText.pdf**: Generated text on top of the watermark.

