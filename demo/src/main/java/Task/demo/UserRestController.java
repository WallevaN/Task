package Task.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserRepository userRepository;

    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET /api/users: Hae kaikki käyttäjät
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // GET /api/users/{id}: Hae yksittäinen käyttäjä
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // POST /api/users: Lisää uusi käyttäjä
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Ei salata salasanaa, tallennetaan sellaisenaan
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // PUT /api/users/{id}: Päivitä käyttäjä
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUserDetails) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setUsername(updatedUserDetails.getUsername());
            user.setRole(updatedUserDetails.getRole());
            user.setEmail(updatedUserDetails.getEmail());
            user.setEnabled(updatedUserDetails.isEnabled());

            // Päivitetään salasana vain, jos se ei ole tyhjä
            if (updatedUserDetails.getPassword() != null && !updatedUserDetails.getPassword().isBlank()) {
                user.setPassword(updatedUserDetails.getPassword()); // Ei salata salasanaa
            }

            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE /api/users/{id}: Poista käyttäjä
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
