package fhi360.it.assetverify.issueLog.controller;

import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.issueLog.dto.IssueLogDto;
import fhi360.it.assetverify.issueLog.model.IssueLog;
import fhi360.it.assetverify.issueLog.service.IssueLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "https://asset-inventory.netlify.app")
//@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/")
public class IssueLogController {
    private final IssueLogService issueLogService;

    @GetMapping("all-issuelogs")
    List<IssueLog> getIssueLogs() {
        return issueLogService.getAllIssueLogs();
    }

    @GetMapping("issuelogs/{id}")
    public ResponseEntity<IssueLog> getBinCardById(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        return issueLogService.getBinCardById(id);
    }

    @GetMapping("issuelogs/inventory/{inventoryId}")
    public List<IssueLog> getIssueLogByInventoryId(@PathVariable Long inventoryId) {
        return issueLogService.getIssueLogByInventoryId(inventoryId);
    }

    @GetMapping("issuelogs/invent/{description}")
    public List<IssueLog> getIssueLogByItemDescription(@PathVariable String description) {
        return issueLogService.getIssueLogByItemDescription(description);
    }

    @GetMapping("/issuelogss")
    public List<IssueLog> getIssueLogsByDescription(@RequestParam("description") String description) {
        return issueLogService.getIssueLogsByDescription(description);
    }

    @GetMapping("date/all-issuelogs")
    public List<IssueLog> getAllIssueLogs(@RequestParam("date") String date) {
        // Assuming IssueLog is your entity class representing the data
        return issueLogService.getIssueLogsByDate(date);
    }

    @PatchMapping({"issuelogs/{id}"})
    public ResponseEntity<IssueLog> updateIssueLog(@PathVariable("id") final long id, @Valid @RequestBody final IssueLogDto issueLogDto) {
        return issueLogService.updateIssueLog(id, issueLogDto);
    }

    @GetMapping("issueLog/log/{states}")
    public List<IssueLog> fetchAllIssueLogByStates(@PathVariable("states") String states, Pageable pageable) {
        return issueLogService.findByStates(states, pageable);
    }

    @GetMapping("issueLog/country/{country}")
    public List<IssueLog> findIssueLogsByCountry(@PathVariable("country") String country, Pageable pageable) throws ResourceNotFoundException {
        return issueLogService.findByCountry(country, pageable);
    }

    @GetMapping("issueLog/search/{states}")
    public List<IssueLog> search(@PathVariable("states") String states, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, Pageable pageable) {
        return issueLogService.searchByDate(states, startDate, endDate, pageable);
    }

    @PostMapping("issueLogs")
    public ResponseEntity<IssueLog> addIssueLog(@RequestBody IssueLog issueLog) {
        return issueLogService.createIssueLog(issueLog);
    }
}