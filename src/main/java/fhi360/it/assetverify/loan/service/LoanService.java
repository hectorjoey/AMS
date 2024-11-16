package fhi360.it.assetverify.loan.service;


import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.loan.dto.LoanDto;
import fhi360.it.assetverify.loan.model.Loan;
import fhi360.it.assetverify.loan.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;

   public List<Loan> getAllLoans() {
        return this.loanRepository.findAll();
    }

    public List<Loan> getLoansByLocation(String location, Pageable pageable) {
        return this.loanRepository.findByLocation(location, pageable);
    }

    public Loan createLoans(@RequestBody final Loan loan) {
        return this.loanRepository.save(loan);
    }

    public Loan updateLoan(Long id, LoanDto loanDto) throws ResourceNotFoundException {
        final Loan loan = this.loanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loan not found for this id :: " + id));
        final Loan updatedLoan = this.loanRepository.save(loan);
        return this.loanRepository.save(updatedLoan);
    }

    public Loan updateLoans(Long id, LoanDto loanDto) throws ResourceNotFoundException {
        final Loan loan = this.loanRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Loan not found for this id :: " + id));
        loan.setLastname(loanDto.getLastname());
        final Loan updatedLoan = this.loanRepository.save(loan);
        return this.loanRepository.save(updatedLoan);
    }

    public ResponseEntity<Loan> getLoanById(Long id) throws ResourceNotFoundException {
        Optional<Loan> optionalLoan = loanRepository.findById(id);
        if (optionalLoan.isPresent()){
            Loan loan = optionalLoan.get();
            return ResponseEntity.ok().body(loan);
        }else {
           throw new ResourceNotFoundException("Loan not found for this Id :: " +id);
        }
    }


    public Page<Loan> searchLoan(String query, Pageable pageable) {
        return loanRepository.searchLoan(query, pageable);
    }
}
