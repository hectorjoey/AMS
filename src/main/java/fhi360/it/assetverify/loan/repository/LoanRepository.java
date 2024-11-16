package fhi360.it.assetverify.loan.repository;

import fhi360.it.assetverify.loan.model.Loan;
import fhi360.it.assetverify.user.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByOrderByExpiryDateDesc(final Pageable pageable, final String expiryDate);

    List<Loan> findByLocation(final String location, final Pageable pageable);


    @Query("SELECT l FROM Loan l WHERE " +
            "l.email LIKE CONCAT('%', :query, '%') " +
            "OR l.status LIKE CONCAT('%', :query, '%') " +
            "OR l.serialnumber LIKE CONCAT('%', :query, '%') ")
    Page<Loan> searchLoan(String query, Pageable pageable);

}
