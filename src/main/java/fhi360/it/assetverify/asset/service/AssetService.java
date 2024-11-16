package fhi360.it.assetverify.asset.service;

import fhi360.it.assetverify.asset.dto.AssetDto;
import fhi360.it.assetverify.asset.model.Asset;
import fhi360.it.assetverify.asset.repository.AssetRepository;
import fhi360.it.assetverify.assetLog.model.AssetLog;
import fhi360.it.assetverify.assetLog.repository.AssetLogRepository;
import fhi360.it.assetverify.exception.AlreadyExistsException;
import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.notification.AssetCreationNotification;
import fhi360.it.assetverify.notification.AssetUpdateNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetLogRepository assetLogRepository;
    private final AssetUpdateNotification assetUpdateNotification;
    private final AssetCreationNotification assetCreationNotification;

    public List<Asset> getAllAssetByCountry(String country) {
        return assetRepository.findByCountry(country);
    }

    public Asset save(Asset asset) throws AlreadyExistsException, MessagingException {
        final Asset assetsID = this.assetRepository.findByAssetId(asset.getAssetId());
        final Asset assetsSerial = this.assetRepository.findBySerialNumber(asset.getSerialNumber());
        if (assetsID != null) {
            throw new AlreadyExistsException(String.format("Asset with assetsID %s already exist", asset.getAssetId()));
        }
        if (assetsSerial != null) {
            throw new AlreadyExistsException(String.format("Asset with Serial Number %s already exist", asset.getSerialNumber()));
        }
        if (asset.getEmailAddress() != null) {
            assetCreationNotification.sendAssetCreationNotification(asset);
        }
        if (asset.getStatus() == null) {
            asset.setStatus("Not Deployed");
        }
        asset.setCountry("Nigeria");
        asset.setLastModifiedBy(asset.getLastModifiedBy());
        asset.setLastUpdatedDate(String.valueOf(LocalDate.now()));
        return this.assetRepository.save(asset);
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

//    public Page<Asset> getAssets(Pageable pageable) {
//        Page<Asset> results = assetRepository.findByOrderByIdAsc(pageable);
//
//        List<Integer> result = results.stream()
//                .map(asset -> {
//                    String dateReceived = asset.getDateReceived();
//                    LocalDate date = parseDate(dateReceived);
//                    int age = calculateAgeOfAsset(date, LocalDate.now());
//                    asset.setCurrentAgeOfAsset(String.valueOf(age));
//                    return calculateAgeOfAsset(date, LocalDate.now());
//                })
//
//                .toList();
//
////        // Process all pages
////        while (result.hasNext()) {
////            // Iterate over all assets in the current page
////            for (Asset asset : result.getContent()) {
////                String dateReceived = asset.getDateReceived();
////                LocalDate date = parseDate(dateReceived);
////                int age = calculateAgeOfAsset(date, LocalDate.now());
////                // Update the current age of the asset
////                asset.setCurrentAgeOfAsset(String.valueOf(age));
////                // Save updated assets to the database
////                assetRepository.saveAll(result.getContent());
////            }
////            // Move to the next page
////            pageable = pageable.next();
////            result = assetRepository.findByOrderByIdAsc(pageable);
////        }
//
//
//        return results;
//    }

    public Page<Asset> getAssets(Pageable pageable) {
        // Retrieve the first page of assets
        Page<Asset> results = assetRepository.findByOrderByIdAsc(pageable);

        while (results.hasContent()) {
            // Iterate over all assets in the current page
            for (Asset asset : results.getContent()) {
                String dateReceived = asset.getDateReceived();
                LocalDate date = parseDate(dateReceived);
                int age = calculateAgeOfAsset(date, LocalDate.now());
                // Update the current age of the asset
                asset.setCurrentAgeOfAsset(String.valueOf(age));
            }
            // Save updated assets to the database
            assetRepository.saveAll(results.getContent());

            // Move to the next page
            pageable = pageable.next();
            results = assetRepository.findByOrderByIdAsc(pageable);
        }

        return results;
    }


    public Page<Asset> searchAssets(String query, Pageable pageable) {
        // Retrieve the paginated result from the repository
        Page<Asset> assetsPage = assetRepository.searchAsset(query, pageable);

        // Use a Set to filter out duplicates based on assetId and serialNumber
        Set<String> seen = new HashSet<>();
        List<Asset> distinctAssets = assetsPage.getContent()
                .stream()
                .filter(asset -> seen.add(asset.getAssetId() + asset.getSerialNumber()))
                .collect(Collectors.toList());

        // Print the distinct assets for debugging
        System.out.println(distinctAssets);

        // Return a new PageImpl with distinct assets, original pageable, and the distinct list size
        return new PageImpl<>(distinctAssets, pageable, distinctAssets.size());
    }


//    @Override
//    public Page<Asset> getAssets(Pageable pageable) {
//        // Initialize page number
//        int pageNumber = 0;
//        Page<Asset> result;
//
//        // Process all pages
//        do {
//            // Increment page number
//            pageNumber++;
//
//            // Fetch the page
//            pageable = PageRequest.of(pageNumber, pageable.getPageSize());
//            result = assetRepository.findByOrderByIdAsc(pageable);
//
//            // Process the current page
//            for (Asset asset : result.getContent()) {
//                String dateReceived = asset.getDateReceived();
//                LocalDate date = parseDate(dateReceived);
//                int age = calculateAgeOfAsset(date, LocalDate.now());
//                // Update the current age of the asset
//                asset.setCurrentAgeOfAsset(String.valueOf(age));
//            }
//            // Save updated assets to the database
//            assetRepository.saveAll(result);
//
//        } while (result.hasNext());
//
//        return (Page<Asset>) assetRepository.saveAll(result);
//    }


    private LocalDate parseDate(String dateReceived) {
        LocalDate date;
        if (dateReceived.matches("\\d/\\d/\\d{2}")) { // Check if date format is single digit for day and month
            date = LocalDate.parse(dateReceived, DateTimeFormatter.ofPattern("M/d/yy"));
        } else if (dateReceived.matches("\\d{2}/\\d/\\d{2}")) { // Check if date format has double-digit month and single-digit day
            date = LocalDate.parse(dateReceived, DateTimeFormatter.ofPattern("MM/d/yy"));
        } else if (dateReceived.matches("\\d/\\d{2}/\\d{2}")) { // Check if date format has single-digit month and double-digit day
            date = LocalDate.parse(dateReceived, DateTimeFormatter.ofPattern("M/dd/yy"));
        } else if (dateReceived.matches("\\d{2}/\\d{2}/\\d{4}")) { // Check if date format is MM/dd/yyyy
            date = LocalDate.parse(dateReceived, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        } else if (dateReceived.matches("\\d/\\d{2}/\\d{4}")) { // Check if date format is single-digit month MM/dd/yyyy
            date = LocalDate.parse(dateReceived, DateTimeFormatter.ofPattern("M/dd/yyyy"));
        } else if (dateReceived.matches("\\d{2}/\\d/\\d{4}")) { // Check if date format is double-digit day MM/dd/yyyy
            date = LocalDate.parse(dateReceived, DateTimeFormatter.ofPattern("MM/d/yyyy"));
        } else { // Assume date format is MM/dd/yy by default
            date = LocalDate.parse(dateReceived, DateTimeFormatter.ofPattern("MM/dd/yy"));
        }

        return date;
    }

    private int calculateAgeOfAsset(LocalDate dateReceived, LocalDate currentDate) {
        return Period.between(dateReceived, currentDate).getYears();
    }

    public ResponseEntity<Page<Asset>> getAssetsByState(String state, Pageable pageable) {
        Page<Asset> assets = assetRepository.findByStates(state, pageable);
        if (assets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(assets, HttpStatus.OK);
    }

    public List<Asset> getAssetsByState(String state) throws ResourceNotFoundException {
        List<Asset> assets = assetRepository.findByStates(state);
        if (assets.isEmpty()) {
            throw new ResourceNotFoundException("No assets found for the state: " + state);
        }
        return assets;
    }


    public ResponseEntity<Asset> updateAsset(long id, AssetDto assetDto) {
        Optional<Asset> optionalAsset = assetRepository.findById(id);
        if (!optionalAsset.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Asset asset = optionalAsset.get();
        updateAssetFields(asset, assetDto);

        AssetLog assetLog = createAssetLog(asset);
        assetLogRepository.save(assetLog);

        sendNotificationIfNeeded(assetDto, asset);

        return new ResponseEntity<>(assetRepository.save(asset), HttpStatus.OK);
    }

    private void updateAssetFields(Asset asset, AssetDto assetDto) {
        asset.setEmailAddress(assetDto.getEmailAddress());
        asset.setStates(assetDto.getStates());
        asset.setAssignee(assetDto.getAssignee());
        asset.setStatus(assetDto.getStatus());
        asset.setPhone(assetDto.getPhone());
        asset.setComment(assetDto.getComment());
        asset.setCondition(assetDto.getCondition());
        asset.setApprovedBy(assetDto.getApprovedBy());
        asset.setLastModifiedBy(assetDto.getLastModifiedBy());
        asset.setLastUpdatedDate(String.valueOf(LocalDate.now()));
    }



    private void sendNotificationIfNeeded(AssetDto assetDto, Asset asset) {
        if (assetDto.getEmailAddress() != null && !assetDto.getEmailAddress().isEmpty()) {
            try {
                assetUpdateNotification.sendAssetUpdateNotification(asset);
            } catch (MessagingException e) {
                // Log the error (use a proper logging framework in real applications)
                e.printStackTrace();
                // Optionally, you might want to return a specific error response here
            }
        }
    }


    private AssetLog createAssetLog(Asset asset) {
        AssetLog assetLog = new AssetLog();
        assetLog.setDate(String.valueOf(LocalDate.now()));
        assetLog.setDescription(asset.getDescription());
        assetLog.setAssetId(asset.getAssetId());
        assetLog.setManufacturer(asset.getManufacturer());
        assetLog.setPoNumber(asset.getPoNumber());
        assetLog.setSerialNumber(asset.getSerialNumber());
        assetLog.setModelNumber(asset.getModelNumber());
        assetLog.setCountry(asset.getCountry());
        assetLog.setStates(asset.getStates());
        assetLog.setPhone(asset.getPhone());
        assetLog.setAssignee(asset.getAssignee());
        assetLog.setCondition(asset.getCondition());
        assetLog.setEmailAddress(asset.getEmailAddress());
        assetLog.setStatus(asset.getStatus());
        assetLog.setApprovedBy(asset.getApprovedBy());
        assetLog.setComment(asset.getComment());
        assetLog.setLastModifiedBy(asset.getLastModifiedBy());
        assetLog.setLastUpdatedDate(String.valueOf(LocalDate.now()));
        return assetLog;
    }

    public Asset getAssetById(@PathVariable("id") final Long id) throws ResourceNotFoundException {
        return this.assetRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Asset not found for this id :: " + id));
    }

    public Asset getByAssetTag(String assetId) throws ResourceNotFoundException {
        final Asset asset = this.assetRepository.findByAssetId(assetId);
        if (asset == null) {
            throw new ResourceNotFoundException("Asset not found for this asset Tag " + assetId);
        }
        return this.assetRepository.findByAssetId(assetId);
    }

    public Asset getByAssetSerialNumber(String serialNumber) throws ResourceNotFoundException {
        final Asset asset = this.assetRepository.findBySerialNumber(serialNumber);
        if (asset == null) {
            throw new ResourceNotFoundException("Asset not found for this asset Tag " + serialNumber);
        }
        return this.assetRepository.findBySerialNumber(serialNumber);
    }

    public Page<Asset> findAssetsByCountry(String country, Pageable pageable) {
        Page<Asset> assets = assetRepository.findByCountry(country, pageable);

        // Filter distinct assets
        List<Asset> distinctAssets = assets.getContent()
                .stream()
                .distinct()
                .toList();

        // Create a new Page with the filtered results
        return new PageImpl<>(distinctAssets, pageable, assets.getTotalElements());
    }

//        if (assets == null || assets.isEmpty()) {
//            System.out.println("country :: " + country);
//            throw new ResourceNotFoundException("Assets from the country is not found: " + country);
//        } else {
//            return assets;
//        }
//    }

    public List<Asset> getCountryAssets(String country) {
        return assetRepository.findByCountry(country)
                .stream()
                .distinct()
                .toList();
    }


    public long getCount(String country) {
        return assetRepository.findByCountry(country)
                .stream()
                .distinct()
                .count();
    }

    public long getStateCount(String states) {
        return assetRepository.findByStates(states)
                .stream()
                .distinct()
                .count();
    }

    public List<Asset> getMyStates( String states){
        return  assetRepository.findByStates(states);
    }
}