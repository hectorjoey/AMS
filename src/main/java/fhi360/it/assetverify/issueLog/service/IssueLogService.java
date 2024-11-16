package fhi360.it.assetverify.issueLog.service;

import fhi360.it.assetverify.asset.model.Asset;
import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.inventory.model.Inventory;
import fhi360.it.assetverify.inventory.repository.InventoryRepository;
import fhi360.it.assetverify.issueLog.dto.IssueLogDto;
import fhi360.it.assetverify.issueLog.model.IssueLog;
import fhi360.it.assetverify.issueLog.repository.IssueLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IssueLogService {

    private final InventoryRepository inventoryRepository;
    private final IssueLogRepository issueLogRepository;

    public List<IssueLog> getAllIssueLogs() {
        return issueLogRepository.findAll();
    }

    public Page<IssueLog> getAllIssueLogs(Pageable pageable) {
        return issueLogRepository.findByOrderByIdAsc(pageable);
    }

    public List<IssueLog> getIssueLogsByDescription(String description) {
        return issueLogRepository.findByDescription(description);
    }

    public List<IssueLog> searchByDate(String states, String startDate, String endDate, Pageable pageable) {
        return issueLogRepository.findByStatesAndDateIssuedBetween(states, startDate, endDate, pageable);
    }

    public ResponseEntity<IssueLog> updateIssueLog(long id, IssueLogDto issueLogDto) {
        Optional<IssueLog> optionalIssueLog = this.issueLogRepository.findById(id);
        if (optionalIssueLog.isPresent()) {
            IssueLog issueLog = optionalIssueLog.get();
            Optional<Inventory> optionalInventory = inventoryRepository.findById(issueLog.getInventoryId());
            if (optionalInventory.isPresent()) {
                Inventory inventory = optionalInventory.get();
                issueLog.setQuantityIssued(issueLogDto.getQuantityIssued());
                issueLog.setIssuedTo(issueLogDto.getIssuedTo());
                issueLog.setBalance(updateBalance(issueLog.getBalance(), inventory.getQuantityIssued(), issueLogDto.getQuantityIssued()));
                inventory.setBalance(issueLog.getBalance());
                inventory.setIssuedTo(issueLogDto.getIssuedTo());
                inventory.setQuantityIssued(issueLogDto.getQuantityIssued());
            }
            return new ResponseEntity<>(issueLogRepository.save(issueLog), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    private String updateBalance(String balance, String quantityIssued, String issuedQuantity) {
        int agg = Integer.parseInt(balance) + Integer.parseInt(quantityIssued);
        int totalBalance = agg - Integer.parseInt(issuedQuantity);
        return String.valueOf(totalBalance);
    }

    public List<IssueLog> findByDescription(String description) {
        return issueLogRepository.findByDescription(description);
    }

    public List<IssueLog> getIssueLogsByDate(String dateReceived) {
        // Implement logic to retrieve data based on the date
        // This might involve calling methods on the repository
        return issueLogRepository.findByDateReceived(dateReceived);
    }


    public List<IssueLog> findByStates(String states, Pageable pageable) {
        return issueLogRepository.findByStates(states, pageable);
    }

    public List<IssueLog> findByCountry(String country, Pageable pageable) throws ResourceNotFoundException {
        List<IssueLog> issueLogs = issueLogRepository.findByCountry(country, pageable);
        if (issueLogs == null || issueLogs.isEmpty()) {
            System.out.println("country :: " + country);
            throw new ResourceNotFoundException("Assets from the country is not found: " + country);
        } else {
            return issueLogs;
        }
    }


    public ResponseEntity<IssueLog> createIssueLog(IssueLog issueLog) {
        String quantityIssued = issueLog.getQuantityIssued();
        String issuedTo = issueLog.getIssuedTo();
        LocalDate issuedDate = LocalDate.now();
        Optional<Inventory> optionalInventory = inventoryRepository.findById(issueLog.getInventoryId());

        if (optionalInventory.isPresent()) {
            Inventory inventory = optionalInventory.get();
            inventory.setIssuedTo(issuedTo);
            inventory.setQuantityIssued(quantityIssued);
            inventory.setDateIssued(String.valueOf(issuedDate));
            inventory.setBalance(calBalance(issueLog.getBalance(), issueLog.getQuantityIssued()));

            inventoryRepository.save(inventory);

            issueLog.setBalance(calBalance(issueLog.getBalance(), issueLog.getQuantityIssued()));

            System.out.println("issue Log" + issueLog);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        System.out.println("issue Log" + issueLog);
        return new ResponseEntity<>(issueLogRepository.save(issueLog), HttpStatus.CREATED);
    }

    private String calBalance(String balance, String quantityIssued) {
        int stockBalance = Integer.parseInt(balance) - Integer.parseInt(quantityIssued);
        return String.valueOf(stockBalance);
    }

    public List<IssueLog> search(String states, String startDate, String endDate, Pageable pageable) {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, inputFormat);
        LocalDate end = LocalDate.parse(endDate, inputFormat);

        DateTimeFormatter desiredFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedStartDate = start.format(desiredFormat);
        String formattedEndDate = end.format(desiredFormat);

        return issueLogRepository.findByStatesAndDateIssuedBetween(states, formattedStartDate, formattedEndDate, pageable);
    }

    public ResponseEntity<IssueLog> getBinCardById(Long id)throws ResourceNotFoundException {
        IssueLog issueLog = issueLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IssueLog not found for this id :: " + id));
        return ResponseEntity.ok().body(issueLog);
    }

    public List<IssueLog> getIssueLogByItemDescription(String description) {
        return issueLogRepository.findByDescription(description);
    }

    public List<IssueLog> getIssueLogByInventoryId(Long inventoryId) {
        return issueLogRepository.findByInventoryId(inventoryId);
    }

}