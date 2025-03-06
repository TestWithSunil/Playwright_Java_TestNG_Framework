package com.qa.demowebshop.ExcelUtils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelReader {

    private Workbook workbook;
    private Sheet sheet;

    /**
     * Constructor that initializes the Workbook and Sheet.
     * @param filePath Path to the Excel file.
     * @param sheetName Name of the sheet (e.g., "LoginData").
     */
    public ExcelReader(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet " + sheetName + " not found in " + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches the data from the specified column for a given test case (TCID).
     * @param tcID The test case ID (assumed to be in the first column).
     * @param columnName The header name of the column (e.g., "Search").
     * @return The cell data as a String.
     */
    public String getCellData(String tcID, String columnName) {
        String cellData = "";
        // Find the column index of the header "columnName"
        Row headerRow = sheet.getRow(0);
        int colIndex = -1;
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && cell.getStringCellValue().trim().equalsIgnoreCase(columnName)) {
                colIndex = i;
                break;
            }
        }
        if (colIndex == -1) {
            throw new RuntimeException("Column " + columnName + " not found.");
        }

        // Loop over the rows to find the matching TCID (assumed to be in the first column)
        int rowCount = sheet.getLastRowNum();
        for (int j = 1; j <= rowCount; j++) {
            Row row = sheet.getRow(j);
            if (row == null) continue;
            Cell tcCell = row.getCell(0);
            if (tcCell != null && tcCell.getStringCellValue().trim().equalsIgnoreCase(tcID)) {
                Cell dataCell = row.getCell(colIndex);
                if (dataCell != null) {
                    cellData = dataCell.getStringCellValue();
                }
                break;
            }
        }
        return cellData;
    }
}
