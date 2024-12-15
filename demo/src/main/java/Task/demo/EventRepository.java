package Task.demo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.categories")
    List<Event> findAllWithCategories();

    // Hae tapahtumat päivämäärän mukaan
    List<Event> findByDate(LocalDate date);

    // Hae tapahtumat otsikon mukaan
    List<Event> findByTitle(String title);

    // Hae tapahtumat kategorian mukaan
    List<Event> findByCategories_Name(String categoryName);

    // Hae tapahtumat alkavan päivämäärän jälkeen
    List<Event> findByDateAfter(LocalDate date);

    // Hae tapahtumat tietyllä aikavälillä
    List<Event> findByDateBetween(LocalDate startDate, LocalDate endDate);
}

