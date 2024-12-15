package Task.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Voit lisätä mukautettuja kyselyitä tarvittaessa, esim. kategorian haku nimen perusteella
    Category findByName(String name);

    // Esimerkkejä mukautetuista kyselyistä
    List<Category> findByNameContaining(String namePart);
    List<Category> findByNameStartingWith(String prefix);
    List<Category> findByNameEndingWith(String suffix);
}