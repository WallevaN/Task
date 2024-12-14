package Task.demo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Voit lisätä mukautettuja kyselyitä tarvittaessa, esim. tapahtumat päivämäärän mukaan
}

