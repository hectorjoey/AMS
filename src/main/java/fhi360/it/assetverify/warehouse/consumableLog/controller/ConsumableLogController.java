package fhi360.it.assetverify.warehouse.consumableLog.controller;

import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.warehouse.consumableLog.dto.ConsumableLogDto;
import fhi360.it.assetverify.warehouse.consumableLog.model.ConsumableLog;
import fhi360.it.assetverify.warehouse.consumableLog.service.ConsumableLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/")
public class ConsumableLogController {
    private final ConsumableLogService consumableLogService;

    @GetMapping("/product/consumables/all-logs")
    List<ConsumableLog> getIssueLogs() {
        return consumableLogService.getAllIssueLogs();
    }

    @GetMapping("/product/consumables/{id}")
    public ResponseEntity<ConsumableLog> getBinCardById(@PathVariable(value = "id") Long id) throws ResourceNotFoundException {
        return consumableLogService.getBinCardById(id);
    }

    @GetMapping("/product/consumables/{inventoryId}")
    public List<ConsumableLog> getIssueLogByInventoryId(@PathVariable Long inventoryId) {
        return consumableLogService.getIssueLogByInventoryId(inventoryId);
    }

    @GetMapping("/product/consumables/invent/{description}")
    public List<ConsumableLog> getIssueLogByItemDescription(@PathVariable String description) {
        return consumableLogService.getIssueLogByItemDescription(description);
    }

    @GetMapping("/product/consumables")
    public List<ConsumableLog> getIssueLogsByDescription(@RequestParam("description") String description) {
        return consumableLogService.getIssueLogsByDescription(description);
    }

    @GetMapping("/product/date/all-consumables")
    public List<ConsumableLog> getAllIssueLogs(@RequestParam("date") String date) {
        // Assuming IssueLog is your entity class representing the data
        return consumableLogService.getIssueLogsByDate(date);
    }

    @PatchMapping({"/consumables/{id}"})
    public ResponseEntity<ConsumableLog> updateIssueLog(@PathVariable("id") final long id, @Valid @RequestBody final ConsumableLogDto consumableLogDto) {
        return consumableLogService.updateIssueLog(id, consumableLogDto);
    }

    @GetMapping("/product/consumables/log/{states}")
    public List<ConsumableLog> fetchAllIssueLogByStates(@PathVariable("states") String states, Pageable pageable) {
        return consumableLogService.findByStates(states, pageable);
    }

    @GetMapping("/product/consumables/country/{country}")
    public List<ConsumableLog> findIssueLogsByCountry(@PathVariable("country") String country, Pageable pageable) throws ResourceNotFoundException {
        return consumableLogService.findByCountry(country, pageable);
    }

//    @GetMapping("issueLog/search/{states}")
//    public List<ConsumableLog> search(@PathVariable("states") String states, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, Pageable pageable) {
//        return consumableLogService.searchByDate(states, startDate, endDate, pageable);
//    }

    @PostMapping("/product/consumables")
    public ResponseEntity<ConsumableLog> addIssueLog(@RequestBody ConsumableLog consumableLog) {
        return consumableLogService.createIssueLog(consumableLog);
    }
}