package fhi360.it.assetverify.loan.controller;

import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.loan.dto.LoanDto;
import fhi360.it.assetverify.loan.model.Loan;
import fhi360.it.assetverify.loan.repository.LoanRepository;
import fhi360.it.assetverify.loan.service.LoanService;
import fhi360.it.assetverify.user.model.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping({"/api/v1/"})
@Slf4j
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @GetMapping({"loans"})
   public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    @GetMapping({"loan/{location}"})
   public List<Loan> getLoansByLocation(@PathVariable("location") final String location, final Pageable pageable)  {
        return loanService.getLoansByLocation(location, pageable);
    }

    @PostMapping({"loan"})
    public ResponseEntity<Loan> createLoans(@RequestBody final Loan loan) {
        return new ResponseEntity<>(loanService.createLoans(loan), HttpStatus.CREATED);
    }

    @GetMapping({"loan/{id}"})
    public ResponseEntity<Loan> getLoanById(@PathVariable("id") final Long id) throws ResourceNotFoundException {
        return loanService.getLoanById(id);
    }

    @PutMapping({"loan/{id}"})
    public Loan updateLoan(@PathVariable("id") final Long id, @Valid @RequestBody final LoanDto loanDto) throws ResourceNotFoundException {
        return loanService.updateLoan(id, loanDto);
    }

    @PatchMapping({"asset-loan/{id}"})
    public Loan updateLoans(@PathVariable("id") final Long id, @Valid @RequestBody final LoanDto loanDto) throws ResourceNotFoundException {
        return loanService.updateLoans(id, loanDto);
    }

    @GetMapping("asset-loan/search")
    public ResponseEntity<Page<Loan>> searchLoan(@RequestParam("query") String query, Pageable pageable) {
        Page<Loan> searchedLoan = loanService.searchLoan(query, pageable);
        if (searchedLoan.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(searchedLoan);
    }
}
