package Task.demo;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/operations")
public class OperationsController {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    public OperationsController(EventRepository eventRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
    }

    // Näytä kaikki tapahtumat ja lomake uuden tapahtuman lisäämiseksi
    @GetMapping
    public String showOperationsPage(Model model) {
        model.addAttribute("events", eventRepository.findAllWithCategories());
        model.addAttribute("event", new Event());
        model.addAttribute("categories", categoryRepository.findAll());
        return "operations";
    }

    // Käsittele uuden tapahtuman lisääminen
    @PostMapping("/add")
    public String addOperation(@ModelAttribute Event event) {
        eventRepository.save(event);
        return "redirect:/operations";
    }

    // Näytä lomake tapahtuman muokkaamiseksi
    @GetMapping("/edit/{id}")
    public String showEditOperationForm(@PathVariable Long id, Model model) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        model.addAttribute("event", event);
        model.addAttribute("categories", categoryRepository.findAll());
        return "edit-operation";
    }

    // Käsittele tapahtuman muokkaaminen
    @PostMapping("/edit/{id}")
    public String editOperation(@PathVariable Long id, @ModelAttribute Event event) {
        event.setId(id);
        eventRepository.save(event);
        return "redirect:/operations";
    }

    // Käsittele tapahtuman poistaminen
    @GetMapping("/delete/{id}")
    public String deleteOperation(@PathVariable Long id) {
        eventRepository.deleteById(id);
        return "redirect:/operations";
    }
}