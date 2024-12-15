package Task.demo;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    public EventController(EventRepository eventRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }

    // Näytä kaikki tapahtumat ja lomake uuden tapahtuman lisäämiseksi
    @GetMapping
    public String showEventsPage(Model model) {
        model.addAttribute("events", eventRepository.findAllWithCategories());
        model.addAttribute("event", new Event());
        model.addAttribute("categories", categoryRepository.findAll());
        return "events";
    }

    // Käsittele uuden tapahtuman lisääminen
    @PostMapping("/add")
    public String addEvent(@ModelAttribute Event event) {
        eventRepository.save(event);
        return "redirect:/events"; // Paluu tapahtumalistaukseen
    }

    // Näytä kategoria-lomake
    @GetMapping("/category-add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category()); // Tyhjä Category-olio
        return "category-add"; // Thymeleaf-sivun nimi
    }

    // Käsittele uuden kategorian lisääminen
    @PostMapping("/category-add")
    public String addCategory(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/events"; // Paluu tapahtumalistaukseen
    }

    // Näytä lomake tapahtuman ja kategorian yhdistämiseksi
    @GetMapping("/category-event")
    public String showCategoryEventForm(Model model) {
        model.addAttribute("events", eventRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        return "category-event"; // Thymeleaf-sivun nimi
    }

    // Käsittele kategorian lisääminen tapahtumaan
    @PostMapping("/category-event")
    public String addCategoryToEvent(Long eventId, Long categoryId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));
        event.getCategories().add(category);
        eventRepository.save(event);
        return "redirect:/events"; // Paluu tapahtumalistaukseen
    }
    @GetMapping("/edit/{id}")
    public String showEditEventForm(@PathVariable("id") Long id, Model model) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found: " + id));
        model.addAttribute("event", event);
        model.addAttribute("categories", categoryRepository.findAll());
        return "event-edit"; // Thymeleaf-tiedosto "event-edit.html"
    }

    @PostMapping("/edit/{id}")
    public String editEvent(@PathVariable("id") Long id,
                            @ModelAttribute Event event,
                            @RequestParam(value = "categoryIds", required = false) Set<Long> categoryIds) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found: " + id));
        existingEvent.setTitle(event.getTitle());
        existingEvent.setDescription(event.getDescription());
        existingEvent.setDate(event.getDate());

        if (categoryIds != null) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
            existingEvent.setCategories(categories);
        }

        eventRepository.save(existingEvent);
        return "redirect:/events";
    }
    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable("id") Long id) {
        eventRepository.deleteById(id);
        return "redirect:/events";
    }


}
