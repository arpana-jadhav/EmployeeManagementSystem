package com.example.employeemanagementsystem2;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;


import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class report {

    public void generateUserDetails(Context context, List<Map<String, Object>> userList) {
        try {
            File pdfFile;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Downloads.DISPLAY_NAME, "UserDetails.pdf");
                values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
                values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

                Uri uri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
                OutputStream outputStream = context.getContentResolver().openOutputStream(uri);

                PdfWriter writer = new PdfWriter(outputStream);
                PdfDocument pdfDocument = new PdfDocument(writer);
                pdfDocument.setDefaultPageSize(PageSize.A4.rotate());

                Document document = new Document(pdfDocument);
                document.setMargins(10, 10, 10, 10);

                Paragraph title = new Paragraph("User Details")
                        .setBold()
                        .setFontSize(18)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontColor(ColorConstants.BLUE);
                document.add(title);
                document.add(new Paragraph("\n"));

                float[] columnWidths = {100, 30, 50, 50, 100, 50, 50};
                Table table = new Table(UnitValue.createPercentArray(columnWidths));
                table.setWidth(UnitValue.createPercentValue(100));

                String[] headers = {"Name", "Gender", "Email", "Phone", "College", "Role", "Status"};
                for (String header : headers) {
                    Cell headerCell = new Cell().add(new Paragraph(header)
                                    .setBold().setFontSize(10))
                            .setBackgroundColor(ColorConstants.CYAN)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setVerticalAlignment(VerticalAlignment.MIDDLE)
                            .setPadding(2)
                            .setBorder(new SolidBorder(1));
                    table.addHeaderCell(headerCell);
                }

                for (Map<String, Object> user : userList) {
                    table.addCell(new Cell().add(new Paragraph((String) user.getOrDefault("name", "N/A")))
                            .setFontSize(9).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph((String) user.getOrDefault("gender", "N/A")))
                            .setFontSize(9).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph((String) user.getOrDefault("email", "N/A")))
                            .setFontSize(9).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph((String) user.getOrDefault("contact", "N/A")))
                            .setFontSize(9).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph((String) user.getOrDefault("college", "N/A")))
                            .setFontSize(9).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph((String) user.getOrDefault("role", "N/A")))
                            .setFontSize(9).setTextAlignment(TextAlignment.CENTER));
                    table.addCell(new Cell().add(new Paragraph((String) user.getOrDefault("status", "N/A")))
                            .setFontSize(9).setTextAlignment(TextAlignment.CENTER));
                }
                document.add(table);
                document.close();

                Toast.makeText(context.getApplicationContext(), "PDF saved to Downloads!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context.getApplicationContext(), "PDF Downlaoding format error", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("PDF", "Error generating PDF: ", e);
            Toast.makeText(context.getApplicationContext(), "Error downloading PDF!", Toast.LENGTH_SHORT).show();
        }
    }
}

