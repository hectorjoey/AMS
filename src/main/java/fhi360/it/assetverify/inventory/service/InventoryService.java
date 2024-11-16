package fhi360.it.assetverify.inventory.service;

import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.inventory.dto.InventoryDto;
import fhi360.it.assetverify.inventory.model.Inventory;
import fhi360.it.assetverify.inventory.repository.InventoryRepository;
import fhi360.it.assetverify.issueLog.model.IssueLog;
import fhi360.it.assetverify.issueLog.repository.IssueLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final IssueLogRepository issueLogRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public ResponseEntity<Inventory> getInventoryById(Long id) throws ResourceNotFoundException {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found for this id :: " + id));
        return ResponseEntity.ok().body(inventory);
    }


    public List<Inventory> getAllInventoriesByCountry(String country){
        return inventoryRepository.findByCountry(country);
    }

    public List<Inventory> getInventories() {
        return inventoryRepository.findAll();
    }


    public Page<Inventory> searchByDate(String startDate, String endDate, Pageable pageable) {
        return inventoryRepository.findByDateReceivedBetween(startDate, endDate, pageable);
    }


    public List<Inventory> findByDateReceivedBetween(String startDate, String endDate) {
        // Implementation for date range search without pagination
        return inventoryRepository.findByDateReceivedBetween(startDate, endDate);
    }


    public Page<Inventory> findByDateReceivedBetween(String startDate, String endDate, Pageable pageable) {
        // Implementation for date range search with pagination
        return inventoryRepository.findByDateReceivedBetween(startDate, endDate, pageable);
    }


    public Page<Inventory> findAllInventories(Pageable pageable) {
        // Implementation for paginated search
        return inventoryRepository.findAll(pageable);
    }

    public InventoryDto createInventory(InventoryDto inventoryDTO) {
        String inventoryDescription = inventoryDTO.getDescription();
        Inventory inventory = new Inventory();
        inventory.setDate(String.valueOf(LocalDate.now()));
        inventory.setCountry(inventoryDTO.getCountry());
        inventory.setStates(inventoryDTO.getStates());
        inventory.setDescription(inventoryDescription.replaceAll(" ", "-") +"("+inventory.getStates()+")");
        inventory.setVendor(inventoryDTO.getVendor());
        inventory.setDateReceived(inventoryDTO.getDateReceived());
        inventory.setPoNumber(inventoryDTO.getPoNumber());
        inventory.setUnit(inventoryDTO.getUnit());
        inventory.setStockState(inventoryDTO.getStockState());
        inventory.setQuantityReceived(inventoryDTO.getQuantityReceived());
        inventory.setCategory(inventoryDTO.getCategory());
        inventory.setMinimumStockLevel(inventoryDTO.getMinimumStockLevel());

        // Retrieve previous balance if description is the same
        String previousBalance = getPreviousBalanceForDescription(inventory.getDescription());

        // Calculate balance
        inventory.setBalance(calculateBalance(inventoryDTO.getQuantityReceived(), previousBalance));

        // Save inventory
        this.inventoryRepository.save(inventory);

        // Create corresponding issue log
        IssueLog issueLog = createIssueLog(inventory);

        // Save issue log
        issueLogRepository.save(issueLog);

        // Map entity back to DTO and return
        return mapToDTO(inventory);

    }

    private InventoryDto mapToDTO(Inventory inventory) {
        InventoryDto inventoryDto = new InventoryDto();
        inventoryDto.setId(inventory.getId());
        inventoryDto.setDate(String.valueOf(LocalDate.now()));
        inventoryDto.setCountry(inventory.getCountry());
        inventoryDto.setStates(inventory.getStates());
        inventoryDto.setDescription(inventory.getDescription());
        inventoryDto.setDateReceived(inventory.getDateReceived());
        inventoryDto.setPoNumber(inventory.getPoNumber());
        inventoryDto.setVendor(inventory.getVendor());
        inventoryDto.setUnit(inventory.getUnit());
        inventoryDto.setStockState(inventory.getStockState());
        inventoryDto.setQuantityReceived(inventory.getQuantityReceived());
        inventoryDto.setBalance(inventory.getBalance());
        inventoryDto.setCategory(inventory.getCategory());
        inventoryDto.setMinimumStockLevel(inventory.getMinimumStockLevel());
        return inventoryDto;
    }

    private IssueLog createIssueLog(Inventory inventory) {
        IssueLog issueLog = new IssueLog();
        issueLog.setDate(String.valueOf(LocalDate.now()));
        issueLog.setCountry(inventory.getCountry());
        issueLog.setStates(inventory.getStates());
        issueLog.setDescription(inventory.getDescription());
        issueLog.setDateReceived(inventory.getDateReceived());
        issueLog.setPoNumber(inventory.getPoNumber());
        issueLog.setVendor(inventory.getVendor());
        issueLog.setUnit(inventory.getUnit());
        issueLog.setQuantityReceived(inventory.getQuantityReceived());
        issueLog.setStockState(inventory.getStockState());
        issueLog.setInventoryId(inventory.getId());
        issueLog.setBalance(inventory.getBalance());
        issueLog.setMinimumStockLevel(inventory.getMinimumStockLevel());
        issueLog.setCategory(inventory.getCategory());
        return issueLog;
    }

    private String getPreviousBalanceForDescription(String description) {
        String jpql = "SELECT i.balance FROM Inventory i WHERE i.description = :description ORDER BY i.dateReceived DESC";
        Query query = entityManager.createQuery(jpql)
                .setParameter("description", description)
                .setMaxResults(1); // Assuming you want the latest balance

        List<?> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return "0"; // Default balance if no previous balance found
        } else {
            return resultList.get(0).toString();
        }
    }

    private String calculateBalance(String quantityReceived, String previousBalance) {
        int prevBalance = 0;
        if (previousBalance != null) {
            prevBalance = Integer.parseInt(previousBalance);
        }

        int closingBalance = prevBalance + Integer.parseInt(quantityReceived);
        return String.valueOf(closingBalance);
    }

    public List<Inventory> getInventories(Pageable pageable) {
        //        for (Inventory value : result) {
//            value.setShellLife(String.valueOf(calculateSLife(value.getManufactureDate(), value.getExpiryDate())));
//            value.setQuantityReceived(value.getQuantityReceived());
//            value.setQuantityIssued(calQuantityIssued(value.getQuantityIssued()));
        // value.setTotal(calcTotal(value.getQuantityIssued()));
//        }
        return inventoryRepository.findByOrderByBalanceAsc(pageable);
    }

    public ResponseEntity<Page<Inventory>> getInventoriesByState(String states, Pageable pageable) {
        List<Inventory> inventories = inventoryRepository.findByStates(states, pageable);

        if (inventories.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // Using a Set to track unique descriptions
        Set<String> uniqueDescriptions = new HashSet<>();
        List<Inventory> uniqueInventories = new ArrayList<>();

        for (Inventory inventory : inventories) {
            if (uniqueDescriptions.add(inventory.getDescription())) {
                uniqueInventories.add(inventory);
            }
        }

        // Create a PageImpl object with the unique inventories and pagination metadata
        Page<Inventory> paginatedResult = new PageImpl<>(uniqueInventories, pageable, uniqueInventories.size());

        return new ResponseEntity<>(paginatedResult, HttpStatus.OK);
    }


//    public ResponseEntity<Page<Inventory>> getInventoriesByState(String states, Pageable pageable) {
//        List<Inventory> inventories = inventoryRepository.findByStates(states, pageable);
//        if (inventories.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//
//        // Using a Set to track unique descriptions
//        Set<String> uniqueDescriptions = new HashSet<>();
//        List<Inventory> uniqueInventories = new ArrayList<>();
//
//        for (Inventory inventory : inventories) {
//            if (uniqueDescriptions.add(inventory.getDescription())) {
//                uniqueInventories.add(inventory);
//            }
//        }
//
//        return new ResponseEntity<>(uniqueInventories, HttpStatus.OK);
//    }


    public ResponseEntity<Page<Inventory>> getInventoriesByCountry(String country, Pageable pageable) {
               return new ResponseEntity<>(inventoryRepository.findByCountry(country, pageable), HttpStatus.OK);
    }

    public List<Inventory> findInventoriesByState(String states){
        return inventoryRepository.findByStates(states);
    }


    public List<Inventory> searchInventory(String query, Pageable pageable) {
            return inventoryRepository.searchInventory(query, pageable);
    }
}