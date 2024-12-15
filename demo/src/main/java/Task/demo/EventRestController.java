package Task.demo;

import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventRestController {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    public EventRestController(EventRepository eventRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }

    // GET /api/events: Hae kaikki tapahtumat
    @GetMapping
    @Transactional // Tämä varmistaa, että Hibernate-istunto pysyy auki, kun relaatiot alustetaan
    public List<Event> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        events.forEach(event -> Hibernate.initialize(event.getCategories()));
        return events;
    }

    // GET /api/events/{id}: Hae yksittäinen tapahtuma
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventRepository.findById(id);
        return event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // POST /api/events: Lisää uusi tapahtuma
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }
    @PostMapping("/add")
    public String addEvent(@ModelAttribute Event event) {
        eventRepository.save(event);
        return "redirect:/events"; // Uudelleenohjaus /events, joka näyttää kaikki tapahtumat
    }

    // POST /api/events/{categoryId}: Lisää uusi tapahtuma olemassa olevan kategorian kanssa
    @PostMapping("/{categoryId}")
    @Transactional
    public ResponseEntity<Event> createEventWithCategory(@RequestBody Event event, @PathVariable Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        event.getCategories().add(category);
        Event savedEvent = eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }
    
    // PUT /api/events/{id}: Päivitä tapahtuma
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event updatedEventDetails) {
        Optional<Event> existingEvent = eventRepository.findById(id);

        if (existingEvent.isPresent()) {
            Event event = existingEvent.get();
            event.setTitle(updatedEventDetails.getTitle());
            event.setDescription(updatedEventDetails.getDescription());
            event.setDate(updatedEventDetails.getDate());
            event.setCategories(updatedEventDetails.getCategories());
            Event savedEvent = eventRepository.save(event);
            return ResponseEntity.ok(savedEvent);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // DELETE /api/events/{id}: Poista tapahtuma
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
