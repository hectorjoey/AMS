package fhi360.it.assetverify.user.service;

import fhi360.it.assetverify.exception.EmailExistsException;
import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.notification.UserAccountCreationNotification;
import fhi360.it.assetverify.user.dto.UserDto;
import fhi360.it.assetverify.user.model.Users;
import fhi360.it.assetverify.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Service
public class UsersService {
    private final UserRepository usersRepository;
    private final UserAccountCreationNotification userAccountCreationNotification;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    public Users save(@RequestBody final Users user) throws EmailExistsException, MessagingException {
        final Users usersEmail = this.usersRepository.findByEmail(user.getEmail());
        if (usersEmail != null) {
            throw new EmailExistsException(String.format("User with email %s already exist", user.getEmail()));
        }
        user.setCountry("Nigeria");
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        user.setCreatedDate(String.valueOf(LocalDate.now()));
        user.setLastUpdatedDate(String.valueOf(LocalDate.now()));
        user.setLastUpdatedBy(user.getLastUpdatedBy());
//        userAccountCreationNotification.sendAccountCreationNotification(user);
        return this.usersRepository.save(user);
    }

    public Users updateUsers(Long id, UserDto userDto) throws ResourceNotFoundException {
        final Users users = usersRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found for this user id :: " + id));

        updateUsersFields(users, userDto);

        return usersRepository.save(users);
    }

    private void updateUsersFields(Users users, UserDto userDto) {

        // Update other fields if provided
        if (userDto.getEmail() != null && !userDto.getEmail().trim().isEmpty()) {
            users.setEmail(userDto.getEmail());
        }

        if (userDto.getRole() != null && !userDto.getRole().trim().isEmpty()) {
            users.setRole(userDto.getRole());
        }

        if (userDto.getFirstname() != null && !userDto.getFirstname().trim().isEmpty()) {
            users.setFirstname(userDto.getFirstname());
        }

        if (userDto.getLastname() != null && !userDto.getLastname().trim().isEmpty()) {
            users.setLastname(userDto.getLastname());
        }

        if (userDto.getDepartment() != null && !userDto.getDepartment().trim().isEmpty()) {
            users.setDepartment(userDto.getDepartment());
        }

        if (userDto.getLastUpdatedBy() != null && !userDto.getLastUpdatedBy().trim().isEmpty()) {
            users.setLastUpdatedBy(userDto.getLastUpdatedBy());
        }

        // Always update the lastUpdatedDate
        users.setLastUpdatedDate(String.valueOf(LocalDate.now()));
    }


    public Page<Users> searchUser(String query, Pageable pageable) {
        return usersRepository.searchUser(query, pageable);
    }

    public Map<String, Boolean> deleteUser(@PathVariable("id") final Long id) throws ResourceNotFoundException {
        final Users user = this.usersRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found for this id :: " + id));
        this.usersRepository.delete(user);
        final Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }


    public boolean isUserAlreadyPresent(final Users user) {
        boolean isUserAlreadyExists = false;
        final Users existingUser = this.usersRepository.getByEmail(user.getEmail());
        if (existingUser != null) {
            isUserAlreadyExists = true;
        }
        return isUserAlreadyExists;
    }


}
