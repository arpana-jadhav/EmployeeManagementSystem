package com.example.employeemanagementsystem2.SalaryReport;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class report {

    public static void generatePDF(Context context, List<List<String>> userDataList, int attendanceCount) {
        String fileName = "SalaryReport.pdf";
        OutputStream outputStream = null;
        Uri pdfUri = null;

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
                pdfUri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), contentValues);
                outputStream = context.getContentResolver().openOutputStream(pdfUri);
            } else {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                outputStream = new FileOutputStream(file);
                pdfUri = Uri.fromFile(file);
            }

            if (outputStream == null) {
                throw new IOException("Failed to create output stream.");
            }

            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument, PageSize.A4);

            document.add(new Paragraph("Salary Report")
                    .setBold().setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            if (userDataList == null) {
                throw new IllegalArgumentException("Invalid user data format: Insufficient data");
            }

            String currentYearMonth = new SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(new Date());

            String name = userDataList.get(1).get(1);
            String email = userDataList.get(2).get(1);
            String phone = userDataList.get(3).get(1);
            String address = userDataList.get(4).get(1);
            String salary = userDataList.get(6).get(1);

            String[] fieldLabels = {"Employee Name", "Email", "Phone", "Address", "Attendance for "+currentYearMonth, "Salary for "+currentYearMonth+""};
            String[] fieldValues = {name, email, phone, address, String.valueOf(attendanceCount), salary};

            Table table = new Table(UnitValue.createPercentArray(new float[]{3, 6})).useAllAvailableWidth();

            for (int i = 0; i < fieldLabels.length; i++) {
                table.addCell(new Cell().add(new Paragraph(fieldLabels[i]).setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addCell(new Cell().add(new Paragraph(fieldValues[i])));
            }

            document.add(table);
            document.close();
            outputStream.close();

            Toast.makeText(context, "PDF Saved in Downloads", Toast.LENGTH_SHORT).show();
            Log.d("PDF", "PDF saved at: " + pdfUri.toString());

        } catch (Exception e) {
            Log.e("PDF", "Error generating PDF", e);
            Toast.makeText(context, "Error generating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException ignored) {}
        }
    }
}