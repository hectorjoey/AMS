package fhi360.it.assetverify.fileUpdload;

import fhi360.it.assetverify.asset.model.Asset;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {
    public static List<Asset> parseExcelAssetFile(final InputStream is) {
        List<Asset> lstAsset = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(is)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            int rowNumber = 0;
            DataFormatter formatter = new DataFormatter();

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                rowNumber++;
                if (rowNumber == 1) {
                    continue; // Skip header row
                }

                Asset asset = new Asset();
                Iterator<Cell> cellsInRow = currentRow.iterator();
                int cellIndex = 0;

                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    String cellValue = formatter.formatCellValue(currentCell);

                    switch (cellIndex) {
                        case 0:
                            asset.setDescription(cellValue);
                            break;
                        case 1:
                            asset.setAssetId(cellValue);
                            break;
                        case 2:
                            asset.setManufacturer(cellValue);
                            break;
                        case 3:
                            asset.setModelNumber(cellValue);
                            break;
                        case 4:
                            asset.setSerialNumber(cellValue);
                            break;
                        case 5:
                            asset.setPoNumber(cellValue);
                            break;
                        case 6:
                            asset.setDateReceived(cellValue);
                            break;
                        case 7:
                            asset.setPurchasePrice(cellValue);
                            break;
                        case 8:
                            asset.setCurrentAgeOfAsset(cellValue);
                            break;
                        case 9:
                            asset.setCondition(cellValue);
                            break;
                        case 10:
                            asset.setCountry(cellValue);
                            break;
                        case 11:
                            asset.setStates(cellValue);
                            break;
                        case 12:
                            asset.setAssignee(cellValue);
                            break;
                        case 13:
                            asset.setStatus(cellValue);
                            break;
                        default:
                            break;
                    }
                    cellIndex++;
                }
                lstAsset.add(asset);
            }
        } catch (IOException e) {
            System.out.println("Error:: " + e.getMessage());
            throw new RuntimeException("FAIL! -> message = " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while processing the Excel file. Row number: " + lstAsset.size() + " -> message = " + e.getMessage(), e);
        }
        return lstAsset;
    }
}