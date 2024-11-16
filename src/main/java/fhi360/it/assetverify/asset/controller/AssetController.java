package fhi360.it.assetverify.asset.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVWriter;
import fhi360.it.assetverify.asset.dto.AssetDto;
import fhi360.it.assetverify.asset.model.Asset;
import fhi360.it.assetverify.asset.repository.AssetRepository;
import fhi360.it.assetverify.asset.service.AssetService;
import fhi360.it.assetverify.exception.AlreadyExistsException;
import fhi360.it.assetverify.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping({"/api/v1/"})
@RequiredArgsConstructor
@Slf4j
public class AssetController {
    private final AssetRepository assetRepository;
    private final AssetService assetService;

    @GetMapping({"assets/{country}"})
    public List<Asset> getAllAssets(@PathVariable("country") String country) {
        return assetService.getAllAssetByCountry(country);
    }

    @GetMapping({"assets-states/{states}"})
    public List<Asset> getMyAssets(@PathVariable("states") String states) {
        return assetService.getMyStates(states);
    }

    @GetMapping("assets/region/{country}")
    public Page<Asset> findAssetsByCountry(@PathVariable("country") String country, Pageable pageable) {
        return assetService.findAssetsByCountry(country, pageable);
    }

    @GetMapping("assetss/{states}")
    public ResponseEntity<Page<Asset>> getAssetsByState(@PathVariable String states, Pageable pageable) {
        return assetService.getAssetsByState(states, pageable);
    }

    @GetMapping({"asset/assets"})
    public Page<Asset> getAssets(final Pageable pageable) {
        return this.assetService.getAssets(pageable);
    }

//    @GetMapping({"all-country-assets/{country}"})
//    List<Asset> getAssets(@PathVariable("country") String country) {
//        return assetService.getCountryAssets(country);
//    }

    @GetMapping({"all-country-assets/{country}"})
    long getCount(@PathVariable("country") String country) {
        return assetService.getCount(country);
    }

    @GetMapping({"all-country-states/{states}"})
    long getStateCount(@PathVariable("states") String states) {
        return assetService.getStateCount(states);
    }

    @GetMapping({"all-assets/{id}"})
    public Optional<Asset> getAssetsById(@PathVariable("id") final Long id) {
        return this.assetRepository.findById(id);
    }


    @GetMapping({"asset/{id}"})
    public ResponseEntity<Asset> getAssetById(@PathVariable("id") final Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(assetService.getAssetById(id), HttpStatus.OK);
    }

    @PostMapping({"asset/serial/{serialNumber}"})
    public Asset getByAssetSerialNumber(@PathVariable("serialNumber") final String serialNumber) throws ResourceNotFoundException {
        return assetService.getByAssetSerialNumber(serialNumber);
    }

    @GetMapping({"asset/tag/{assetId}"})
    public Asset getByAssetTag(@PathVariable("assetId") final String assetId) throws ResourceNotFoundException {
        return this.assetService.getByAssetTag(assetId);
    }

    @PostMapping({"asset"})
    public ResponseEntity<Asset> createAsset(@Valid @RequestBody final Asset asset) throws AlreadyExistsException, MessagingException {
        return new ResponseEntity<>(assetService.save(asset), HttpStatus.CREATED);
    }

    @PatchMapping({"asset/{id}"})
    public ResponseEntity<Asset> updateAsset(@PathVariable("id") final long id, @Valid @RequestBody final AssetDto assetDto) throws MessagingException {
        return assetService.updateAsset(id, assetDto);
    }

    @DeleteMapping({"asset/{id}"})
    public Map<String, Boolean> deleteAsset(@PathVariable("id") final Long id) throws ResourceNotFoundException, MessagingException {
        final Asset asset = this.assetRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Asset not found for this id :: " + id));
        this.assetRepository.delete(asset);
        final Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @GetMapping("asset/export-to-pdf/{country}")
    public ResponseEntity<byte[]> exportToPDFs(@PathVariable("country") String country) {
        try {
            List<Asset> data = assetService.getAllAssetByCountry(country);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document pdfDocumnent = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(pdfDocumnent, outputStream);

            pdfDocumnent.open();
            addDataToPDF(pdfDocumnent, data);
            pdfDocumnent.close();

            byte[] pdfBytes = outputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            headers.setContentDispositionFormData("attachment", "assets_" + country + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("asset/export-to-pdf/{state}")
    public ResponseEntity<byte[]> exportToPDF(@PathVariable("state") String state) {
        try {
            List<Asset> data = assetService.getAssetsByState(state);

            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4.rotate()); // Set landscape orientation
            PdfWriter.getInstance(document, arrayOutputStream);

            document.open();
            addDataToPDF(document, data);
            document.close();

            byte[] pdfBytes = arrayOutputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "assets_" + state + ".pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private void addDataToPDF(Document document, List<Asset> data) throws DocumentException {
        PdfPTable table = new PdfPTable(21); // Number of columns
        table.setWidthPercentage(100); // Set table width to 100% of the page

        // Set table headers
        table.addCell(createCell("Description", true));
        table.addCell(createCell("Asset ID", true));
        table.addCell(createCell("Manufacturer", true));
        table.addCell(createCell("Model number", true));
        table.addCell(createCell("Serial number", true));
        table.addCell(createCell("PO Number", true));
        table.addCell(createCell("Date received", true));
        table.addCell(createCell("Purchase price ($)", true));
        table.addCell(createCell("Purchase price (N)", true));
        table.addCell(createCell("Funder", true));
        table.addCell(createCell("Project", true));
        table.addCell(createCell("Useful life span (Years)", true));
        table.addCell(createCell("Current age of asset", true));
        table.addCell(createCell("Condition", true));
        table.addCell(createCell("Country", true));
        table.addCell(createCell("States", true));
        table.addCell(createCell("Facility", true));
        table.addCell(createCell("Location ", true));
        table.addCell(createCell("Assignee ", true));
        table.addCell(createCell("Status", true));
        table.addCell(createCell("Email Address", true));

        // Add data rows
        for (Asset obj : data) {
            table.addCell(createCell(obj.getDescription(), false));
            table.addCell(createCell(obj.getAssetId(), false));
            table.addCell(createCell(obj.getManufacturer(), false));
            table.addCell(createCell(obj.getModelNumber(), false));
            table.addCell(createCell(obj.getSerialNumber(), false));
            table.addCell(createCell(obj.getPoNumber(), false));
            table.addCell(createCell(obj.getDateReceived(), false));
            table.addCell(createCell(obj.getPurchasePrice(), false));
            table.addCell(createCell(obj.getCurrentAgeOfAsset(), false));
            table.addCell(createCell(obj.getCondition(), false));
            table.addCell(createCell(obj.getCountry(), false));
            table.addCell(createCell(obj.getStates(), false));
            table.addCell(createCell(obj.getAssignee(), false));
            table.addCell(createCell(obj.getStatus(), false));
            table.addCell(createCell(obj.getEmailAddress(), false));
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


    @GetMapping("asset/export-to-csv-asset/{country}")
    public ResponseEntity<byte[]> exportToCSV(@PathVariable(required = false) String country) {
        try {
            List<Asset> data = assetService.getAllAssetByCountry(country);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(outputStream);

            // Create CSVWriter object

            CSVWriter csvWriter = new CSVWriter(writer);

            // Write CSV headers
            String[] headers = {
                    "Description", "Asset ID", "Manufacturer", "Model Number", "Serial Number",
                    "PO Number", "Date Received", "Purchase Price (N)",
                    "Current age of asset", "Condition", "Country", "States"
                    ,"Assignee", "Status", "Email Address"
            };
            csvWriter.writeNext(headers);

            // Write data rows
            for (Asset obj : data) {
                String[] row = {
                        obj.getDescription(), obj.getAssetId(), obj.getManufacturer(), obj.getModelNumber(), obj.getSerialNumber(),
                        obj.getPoNumber(), obj.getDateReceived(),  obj.getPurchasePrice(), obj.getCurrentAgeOfAsset(), obj.getCondition(),
                        obj.getCountry(), obj.getStates(), obj.getAssignee(), obj.getStatus(), obj.getEmailAddress()
                };
                csvWriter.writeNext(row);
            }

            csvWriter.flush();
            byte[] csvBytes = outputStream.toByteArray();

            writer.flush();
            writer.close();

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.TEXT_PLAIN);
            header.setContentDispositionFormData("attachment", country + "_asset.csv");

            // Return the byte array as the response
            return new ResponseEntity<>(csvBytes, header, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("asset/export-to-csv/{state}")
    public ResponseEntity<byte[]> exportToCSVs(@PathVariable(required = false) String state) throws ResourceNotFoundException {
        try {
            List<Asset> data = assetService.getAssetsByState(state);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(outputStream);

            // Create CSVWriter object
            CSVWriter csvWriter = new CSVWriter(writer);

            // Write CSV headers
            String[] headers = {
                    "Description", "Asset ID", "Manufacturer", "Model Number", "Serial Number",
                    "PO Number", "Date Received", "Purchase Price (N)",
                      "Current age of asset", "Condition", "States"
                   , "Assignee", "Status", "Email Address"
            };
            csvWriter.writeNext(headers);

            // Write data rows
            for (Asset obj : data) {
                String[] row = {
                        obj.getDescription(), obj.getAssetId(), obj.getManufacturer(), obj.getModelNumber(), obj.getSerialNumber(),
                        obj.getPoNumber(), obj.getDateReceived(), obj.getPurchasePrice(),
                         obj.getCurrentAgeOfAsset(), obj.getCondition(), obj.getStates(),
                        obj.getAssignee(), obj.getStatus(), obj.getEmailAddress()
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
            header.setContentDispositionFormData("attachment", "assets_" + state + ".csv");
            // Return the byte array as the response
            return new ResponseEntity<>(csvBytes, header, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("asset/search")
    public ResponseEntity<Page<Asset>> searchAssets(@RequestParam("query") String query, Pageable pageable) {
        Page<Asset> searchedAssets = assetService.searchAssets(query, pageable);
        if (searchedAssets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(searchedAssets);
    }
}