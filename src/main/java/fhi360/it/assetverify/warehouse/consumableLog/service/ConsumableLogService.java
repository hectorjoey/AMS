package fhi360.it.assetverify.warehouse.consumableLog.service;

import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.inventory.model.Inventory;
import fhi360.it.assetverify.inventory.repository.InventoryRepository;
import fhi360.it.assetverify.warehouse.consumableLog.dto.ConsumableLogDto;
import fhi360.it.assetverify.warehouse.consumableLog.model.ConsumableLog;
import fhi360.it.assetverify.warehouse.consumableLog.repository.ConsumableLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ConsumableLogService {

    private final InventoryRepository inventoryRepository;
    private final ConsumableLogRepository consumableLogRepository;

    public List<ConsumableLog> getAllIssueLogs() {
        return consumableLogRepository.findAll();
    }

    public Page<ConsumableLog> getAllIssueLogs(Pageable pageable) {
        return consumableLogRepository.findByOrderByIdAsc(pageable);
    }

    public List<ConsumableLog> getIssueLogsByDescription(String description) {
        return consumableLogRepository.findByDescription(description);
    }

    public List<ConsumableLog> searchByDate(String states, String startDate, String endDate, Pageable pageable) {
        return consumableLogRepository.findByStatesAndDateIssuedBetween(states, startDate, endDate, pageable);
    }

    public ResponseEntity<ConsumableLog> updateIssueLog(long id, ConsumableLogDto consumableLogDto) {
        Optional<ConsumableLog> optionalIssueLog = this.consumableLogRepository.findById(id);
        if (optionalIssueLog.isPresent()) {
            ConsumableLog consumableLog = optionalIssueLog.get();
            Optional<Inventory> optionalInventory = inventoryRepository.findById(consumableLog.getProductConsumableId());
            if (optionalInventory.isPresent()) {
                Inventory inventory = optionalInventory.get();
                consumableLog.setQuantityIssued(consumableLogDto.getQuantityIssued());
                consumableLog.setIssuedTo(consumableLogDto.getIssuedTo());
                consumableLog.setBalance(updateBalance(consumableLog.getBalance(), inventory.getQuantityIssued(), consumableLogDto.getQuantityIssued()));
                inventory.setBalance(consumableLog.getBalance());
                inventory.setIssuedTo(consumableLogDto.getIssuedTo());
                inventory.setQuantityIssued(consumableLogDto.getQuantityIssued());
            }
            return new ResponseEntity<>(consumableLogRepository.save(consumableLog), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    private String updateBalance(String balance, String quantityIssued, String issuedQuantity) {
        int agg = Integer.parseInt(balance) + Integer.parseInt(quantityIssued);
        int totalBalance = agg - Integer.parseInt(issuedQuantity);
        return String.valueOf(totalBalance);
    }

    public List<ConsumableLog> findByDescription(String description) {
        return consumableLogRepository.findByDescription(description);
    }

    public List<ConsumableLog> getIssueLogsByDate(String dateReceived) {
        // Implement logic to retrieve data based on the date
        // This might involve calling methods on the repository
        return consumableLogRepository.findByDateReceived(dateReceived);
    }


    public List<ConsumableLog> findByStates(String states, Pageable pageable) {
        return consumableLogRepository.findByStates(states, pageable);
    }

    public List<ConsumableLog> findByCountry(String country, Pageable pageable) throws ResourceNotFoundException {
        List<ConsumableLog> consumableLogs = consumableLogRepository.findByCountry(country, pageable);
        if (consumableLogs == null || consumableLogs.isEmpty()) {
            System.out.println("country :: " + country);
            throw new ResourceNotFoundException("Assets from the country is not found: " + country);
        } else {
            return consumableLogs;
        }
    }


    public ResponseEntity<ConsumableLog> createIssueLog(ConsumableLog consumableLog) {
        String quantityIssued = consumableLog.getQuantityIssued();
        String issuedTo = consumableLog.getIssuedTo();
        LocalDate issuedDate = LocalDate.now();
        Optional<Inventory> optionalInventory = inventoryRepository.findById(consumableLog.getProductConsumableId());

        if (optionalInventory.isPresent()) {
            Inventory inventory = optionalInventory.get();
            inventory.setIssuedTo(issuedTo);
            inventory.setQuantityIssued(quantityIssued);
            inventory.setDateIssued(String.valueOf(issuedDate));
            inventory.setBalance(calBalance(consumableLog.getBalance(), consumableLog.getQuantityIssued()));

            inventoryRepository.save(inventory);

            consumableLog.setBalance(calBalance(consumableLog.getBalance(), consumableLog.getQuantityIssued()));

            System.out.println("issue Log" + consumableLog);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        System.out.println("issue Log" + consumableLog);
        return new ResponseEntity<>(consumableLogRepository.save(consumableLog), HttpStatus.CREATED);
    }

    private String calBalance(String balance, String quantityIssued) {
        int stockBalance = Integer.parseInt(balance) - Integer.parseInt(quantityIssued);
        return String.valueOf(stockBalance);
    }

    public List<ConsumableLog> search(String states, String startDate, String endDate, Pageable pageable) {
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, inputFormat);
        LocalDate end = LocalDate.parse(endDate, inputFormat);

        DateTimeFormatter desiredFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedStartDate = start.format(desiredFormat);
        String formattedEndDate = end.format(desiredFormat);

        return consumableLogRepository.findByStatesAndDateIssuedBetween(states, formattedStartDate, formattedEndDate, pageable);
    }

    public ResponseEntity<ConsumableLog> getBinCardById(Long id)throws ResourceNotFoundException {
        ConsumableLog consumableLog = consumableLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("IssueLog not found for this id :: " + id));
        return ResponseEntity.ok().body(consumableLog);
    }

    public List<ConsumableLog> getIssueLogByItemDescription(String description) {
        return consumableLogRepository.findByDescription(description);
    }

    public List<ConsumableLog> getIssueLogByInventoryId(Long inventoryId) {
        return consumableLogRepository.findByProductConsumableId(inventoryId);
    }

}