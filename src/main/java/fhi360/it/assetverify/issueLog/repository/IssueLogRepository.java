package fhi360.it.assetverify.issueLog.repository;

import fhi360.it.assetverify.issueLog.model.IssueLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueLogRepository extends JpaRepository<IssueLog, Long> {

    Page<IssueLog> findByOrderByIdAsc(Pageable pageable);

    List<IssueLog> findByInventoryId(Long inventoryId);

    List<IssueLog> findByStatesAndDateIssuedBetween(String states,  String startDate, String endDate, Pageable pageable);

    List<IssueLog> findByDescription(String description);

    List<IssueLog> findByDateReceived(String date);

    List<IssueLog>findByStates(String states, Pageable pageable);

    List<IssueLog> findByCountry(String country, Pageable pageable);
}
