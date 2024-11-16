package fhi360.it.assetverify.assetLog.service;

import fhi360.it.assetverify.assetLog.model.AssetLog;
import fhi360.it.assetverify.assetLog.repository.AssetLogRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AssetLogService {
    private final AssetLogRepository assetLogRepository;

    public AssetLog addAssetLog(AssetLog assetLog) {
        return assetLogRepository.save(assetLog);
    }

    public List<AssetLog> getAllAssetLogs() {
        return assetLogRepository.findAll();
    }


    public List<AssetLog> searchByDate(String startDate, String endDate, Pageable pageable) {
        return assetLogRepository.findByDateBetween(startDate, endDate, pageable);
    }

    public ResponseEntity<List<AssetLog>> getAssetsByState(String state, Pageable pageable) {
        List<AssetLog> assets = assetLogRepository.findByStates(state, pageable);
        if (assets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(assets, HttpStatus.OK);
    }

    public ResponseEntity<List<AssetLog>> getByCountry(String country, Pageable pageable) {
        List<AssetLog> assets = assetLogRepository.findByCountry(country, pageable);
        if (assets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(assets, HttpStatus.OK);
    }
}
