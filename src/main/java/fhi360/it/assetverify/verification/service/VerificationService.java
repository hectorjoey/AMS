package fhi360.it.assetverify.verification.service;

import fhi360.it.assetverify.asset.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VerificationService {
    private final AssetRepository assetRepository;

}
