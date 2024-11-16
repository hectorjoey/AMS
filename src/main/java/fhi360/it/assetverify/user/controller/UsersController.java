
package fhi360.it.assetverify.user.controller;

import fhi360.it.assetverify.exception.EmailExistsException;
import fhi360.it.assetverify.exception.ResourceNotFoundException;
import fhi360.it.assetverify.user.dto.UserDto;
import fhi360.it.assetverify.user.model.Users;
import fhi360.it.assetverify.user.repository.UserRepository;
import fhi360.it.assetverify.user.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "https://asset-inventory.netlify.app")
//@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping({"/api/v1/"})
@RequiredArgsConstructor
@Slf4j
public class UsersController {
    private final UserRepository userRepository;
    private final UsersService usersService;

    @GetMapping({"users"})
    public Page<Users> getUserByType(final Pageable pageable) {
        return this.userRepository.findByOrderByRole(pageable);
    }

    @GetMapping({"user/{id}"})
    public ResponseEntity<Users> getUserById(@PathVariable("id") final Long id) {
        Optional<Users> optionalUsers = userRepository.findById(id);
        if (optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            return ResponseEntity.ok().body(users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping({"user"})
    public ResponseEntity<Users> createUser(@Valid @RequestBody final Users users) throws EmailExistsException, MessagingException {
        return new ResponseEntity<>(usersService.save(users), HttpStatus.CREATED);
    }

//    @PatchMapping({"user/{id}"})
//    public ResponseEntity<Users> updateUser(@PathVariable("id") final Long id, @Valid @RequestBody final UserDto userDto) throws ResourceNotFoundException {
//        return new ResponseEntity<>(usersService.updateUser(id, userDto), HttpStatus.OK);
//    }

    @PatchMapping({"user/{id}"})
    public ResponseEntity<Users> updateUsers(@PathVariable("id") final Long id, @Valid @RequestBody final UserDto userDto) throws ResourceNotFoundException {
        return new ResponseEntity<>(usersService.updateUsers(id, userDto), HttpStatus.OK);
    }

    @DeleteMapping({"user/{id}"})
    public Map<String, Boolean> deleteUser(@PathVariable("id") final Long id) throws ResourceNotFoundException {
        return usersService.deleteUser(id);
    }

    @GetMapping("users/search")
    public ResponseEntity<Page<Users>> searchUser(@RequestParam("query") String query, Pageable pageable) {
        Page<Users> searchedUser = usersService.searchUser(query, pageable);
        if (searchedUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(searchedUser);
    }
}
