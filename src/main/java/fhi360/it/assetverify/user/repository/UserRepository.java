package fhi360.it.assetverify.user.repository;

import fhi360.it.assetverify.user.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Users, Long> {
    Users findByEmail(final String email);

    Users getByEmail(final String email);

//    Page<Users> findByOrderByUserTypeAsc(final Pageable pageable);
    Page<Users> findByOrderByRole(final Pageable pageable);

    @Query("SELECT u FROM Users u WHERE " +
            "u.email LIKE CONCAT('%', :query, '%') " +
            "OR u.states LIKE CONCAT('%', :query, '%') " +
            "OR u.department LIKE CONCAT('%', :query, '%') ")
    Page<Users> searchUser(String query, Pageable pageable);
}
