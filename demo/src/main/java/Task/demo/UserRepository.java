package Task.demo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Käyttäjän haku käyttäjätunnuksen perusteella autentikointia varten
    User findByUsername(String username);
}

