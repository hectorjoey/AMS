
package fhi360.it.assetverify.inventory.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.inventory.dto.InventoryDto;
import fhi360.it.assetverify.inventory.model.Inventory;
import fhi360.it.assetverify.inventory.repository.InventoryRepository;
import fhi360.it.assetverify.inventory.service.InventoryService;
import fhi360.it.assetverify.issueLog.service.IssueLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/")
public class InventoryController {
    private final InventoryRepository inventoryRepository;
    private final InventoryService inventoryService;
    private final IssueLogService issueLogService;

    @GetMapping("all-inventories/{country}")
    List<Inventory> getInventories(@PathVariable("country") String country) {
        return inventoryService.getAllInventoriesByCountry(country);
    }

    @GetMapping("all-invent")
    List<Inventory> getInventory(Pageable pageable) {
        return inventoryService.getInventories(pageable);
    }

    //get all inventory
    @GetMapping("inventories")
    public List<Inventory> getAllInventories(Pageable pageable) {
        return inventoryService.getInventories(pageable);
    }


    @PostMapping("/inventory")
    public ResponseEntity<InventoryDto> createInventory(@RequestBody InventoryDto inventoryDTO) {
        return new ResponseEntity<>(inventoryService.createInventory(inventoryDTO), HttpStatus.CREATED);
    }

    @GetMapping("inventory/state/{states}")
    public ResponseEntity<Page<Inventory>> getInventoriesByState(@PathVariable String states, Pageable pageable) {
        return inventoryService.getInventoriesByState(states, pageable);
    }

    @GetMapping("inventory/country/{country}")
    public ResponseEntity<Page<Inventory>> getInventoriesByCountry(@PathVariable String country, Pageable pageable) {
        return inventoryService.getInventoriesByCountry(country, pageable);
    }


    //get inventory by Id
    @GetMapping("invent/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        return inventoryService.getInventoryById(id);
    }

//    @PutMapping("inventories/{id}")
//    public Inventory updateInventory(@PathVariable("id") Long id, @Valid @RequestBody InventoryDto inventoryDto) throws ResourceNotFoundException {
//        System.out.println("Update inventory with ID = " + id + "...");
//        Inventory inventory = inventoryRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for this id :: " + id));
////        inventory.setOpeningBalance(inventoryDto.getOpeningBalance());
////        inventory.setBalance(inventoryDto.getBalance());
//        final Inventory updatedInventory = inventoryRepository.save(inventory);
//        System.out.println("Updated Inventory " + updatedInventory);
//        return inventoryRepository.save(updatedInventory);
//    }

    @DeleteMapping("inventory/{id}")
    public Map<String, Boolean> deleteInventory(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for this id :: " + id));
        inventoryRepository.delete(inventory);
//        deleteAssetService.deleteAssetEmail(asset);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


    @GetMapping("searches")
    public Page<Inventory> search(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, @RequestParam("page") int page) {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate start = LocalDate.parse(startDate, inputFormat);
        LocalDate end = LocalDate.parse(endDate, inputFormat);

        DateTimeFormatter desiredFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedStartDate = start.format(desiredFormat);
        String formattedEndDate = end.format(desiredFormat);
        return inventoryService.searchByDate(formattedStartDate, formattedEndDate, PageRequest.of(page - 1, 10));
    }

    @GetMapping("export-consumable-to-pdf")
    public ResponseEntity<byte[]> exportInventoryToPDF() {
        try {
            List<Inventory> data = inventoryService.getInventories();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4.rotate()); // Set landscape orientation
            PdfWriter.getInstance(document, byteArrayOutputStream);

            document.open();
            addDataToPDF(document, data);
            document.close();

            byte[] pdfBytes = byteArrayOutputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "IT-Consumable.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void addDataToPDF(Document document, List<Inventory> data) throws DocumentException {
        PdfPTable table = new PdfPTable(12); // Number of columns
        table.setWidthPercentage(100); // Set table width to 100% of the page

        // Set table headers
        table.addCell(createCell("Country", true));
        table.addCell(createCell("State", true));
        table.addCell(createCell("Description", true));
        table.addCell(createCell("Category", true));
        table.addCell(createCell("Date received", true));
        table.addCell(createCell("PO Number", true));
        table.addCell(createCell("Vendor", true));
        table.addCell(createCell("Unit", true));
        table.addCell(createCell("Stock State", true));
        table.addCell(createCell("Quantity Received", true));
        table.addCell(createCell("Minimum Stock Level", true));

        table.addCell(createCell("Balance", true));
//


        // Add data rows
        for (Inventory obj : data) {
            table.addCell(createCell(obj.getCountry(), false));
            table.addCell(createCell(obj.getStates(), false));
            table.addCell(createCell(obj.getDescription(), false));
            table.addCell(createCell(obj.getCategory(), false));
            table.addCell(createCell(obj.getDateReceived(), false));
            table.addCell(createCell(obj.getPoNumber(), false));
            table.addCell(createCell(obj.getVendor(), false));
            table.addCell(createCell(obj.getUnit(), false));
            table.addCell(createCell(obj.getStockState(), false));
            table.addCell(createCell(obj.getQuantityReceived(), false));
            table.addCell(createCell(obj.getMinimumStockLevel(), false));
            table.addCell(createCell(obj.getBalance(), false));
        }

        // Add the table to the document
        document.add(table);
    }

    private PdfPCell createCell(String content, boolean isHeader) {
        PdfPCell cell = new PdfPCell(new Paragraph(content));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(2);

        Font font = isHeader ? FontFactory.getFont(FontFactory.HELVETICA_BOLD, 6) : FontFactory.getFont(FontFactory.HELVETICA, 7);
        cell.setPhrase(new Paragraph(content, font));

        return cell;
    }


    @GetMapping("inventory/export-to-csv/{states}")
    public ResponseEntity<byte[]> exportToCSVs(@PathVariable(required = false) String states, Pageable pageable) throws ResourceNotFoundException {
        try {
            List<Inventory> datas = inventoryService.findInventoriesByState(states);

            if (states != null) {
                datas = inventoryService.findInventoriesByState(states);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(outputStream);

            // Create CSVWriter object
            CSVWriter csvWriter = new CSVWriter(writer);

            // Write CSV headers
            String[] headers = {
                    "Country", "State", "Description", "Category", "Date Received", "PO Number", "Vendor",
                    "Unit", "Stock State)", "Quantity Received", "Minimum Stock Level", "Balance"
            };
            csvWriter.writeNext(headers);

            // Write data rows
            for (Inventory obj : datas) {
                String[] row = {
                        obj.getCountry(), obj.getStates(), obj.getDescription(), obj.getCategory(), obj.getDateReceived(), obj.getPoNumber(),
                        obj.getVendor(), obj.getUnit(), obj.getStockState(), obj.getQuantityReceived(), obj.getMinimumStockLevel(),
                        obj.getBalance()
                };
                csvWriter.writeNext(row);
            }

            csvWriter.flush();
            writer.flush();

            byte[] csvBytes = outputStream.toByteArray();

            // Close the streams
            writer.close();
            outputStream.close();

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.TEXT_PLAIN);
            if (states != null) {
                header.setContentDispositionFormData("attachment", states + "consumables.csv");
            } else {
                header.setContentDispositionFormData("attachment", "all_consumables.csv");
            }

            // Return the byte array as the response
            return new ResponseEntity<>(csvBytes, header, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("inventory/export-to-pdf/{states}")
    public ResponseEntity<byte[]> exportToPDF(@PathVariable("states") String states) {
        try {
            List<Inventory> data = inventoryService.findInventoriesByState(states);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4.rotate()); // Set landscape orientation
            PdfWriter.getInstance(document, outputStream);

            document.open();
            addDataToPDF(document, data);
            document.close();

            byte[] pdfBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", states + "consumables.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("inventories/inventory/{states}")
    public List<Inventory> getStates(@PathVariable("states") String states) {
        return inventoryService.findInventoriesByState(states);
    }

    @GetMapping("inventory/log/search/{states}")
    public List<Inventory> searchInventories(@RequestParam("query") String query, Pageable pageable) {
        return inventoryService.searchInventory(query, pageable);
    }

}
