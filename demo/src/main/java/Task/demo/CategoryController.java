package Task.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Näytä kategoria-lomake
    @GetMapping("/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "category-add"; // Thymeleaf-näkymä
    }

    // Käsittele kategoria-lomakkeen lähetys
    @PostMapping("/add")
    public String addCategory(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/categories"; // Ohjaus kategoria-listaukseen
    }

    // Näytä kaikki kategoriat
    @GetMapping
    public String getAllCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "categories"; // Thymeleaf-näkymä
    }
}