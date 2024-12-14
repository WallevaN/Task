package Task.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.time.LocalDate;

@Component
public class TestRunner implements CommandLineRunner {
    @Autowired
    private final EventRepository eventRepository;
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private final UserRepository userRepository;
    private final RequestMappingHandlerMapping handlerMapping;

    public TestRunner(
            EventRepository eventRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.handlerMapping = handlerMapping;
    }

    @Override
    public void run(String... args) throws Exception {
        // Testaa tietojen lisäämistä ja hakua
        Category work = new Category();
        work.setName("Work");
        categoryRepository.save(work);

        // Hae tallennettu kategoria tietokannasta
        Category savedWork = categoryRepository.findById(work.getId())
                .orElseThrow(() -> new IllegalStateException("Category not found"));

        // Luo uusi tapahtuma ja lisää kategoria
        Event event = new Event();
        event.setTitle("Team Meeting");
        event.setDescription("Discuss project status");
        event.setDate(LocalDate.now());
        event.getCategories().add(savedWork); // Käytä tallennettua kategoriaa
        eventRepository.save(event);

        // Luo uusi käyttäjä ja tallenna se
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin123"); // Tämä tulisi salata, jos käytetään oikeaa autentikointia
        user.setRole("ROLE_USER");
        userRepository.save(user);

        System.out.println("Kaikki tapahtumat: " + eventRepository.findAll());

        // Tulosta kaikki määritetyt reitit ja niiden käsittelijät
        handlerMapping.getHandlerMethods().forEach((key, value) -> {
            System.out.println(key + " -> " + value);
        });
    }
}