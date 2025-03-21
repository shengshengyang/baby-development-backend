package com.dean.baby.mvc.controller;

import com.dean.baby.common.dto.CategoryForm;
import com.dean.baby.common.dto.enums.Language;
import com.dean.baby.common.entity.Category;
import com.dean.baby.common.repository.CategoryRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {


    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("newCategory", new CategoryForm());
        return "category/list";
    }

    @PostMapping
    public String addCategory(@RequestParam("zhTW") String zhTW,
                              @RequestParam("en") String en) {
        Category category = new Category();
        Map<Language, String> name = new HashMap<>();
        name.put(Language.TRADITIONAL_CHINESE, zhTW);
        name.put(Language.ENGLISH, en);
        category.setName(name);
        categoryRepository.save(category);
        return "redirect:/categories";
    }

    // 3. 進入修改頁面 (edit.html)
    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            model.addAttribute("category", categoryOpt.get());
            return "category/edit"; // 對應到 /templates/category/edit.html
        } else {
            // 若找不到則返回列表頁
            return "redirect:/categories";
        }
    }

    @PostMapping("/edit")
    public String updateCategory(@RequestParam("id") Long id,
                                 @RequestParam("zhTW") String zhTW,
                                 @RequestParam("en") String en) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            Map<Language, String> name = category.getName();
            if (name == null) {
                name = new HashMap<>();
            }
            name.put(Language.TRADITIONAL_CHINESE, zhTW);
            name.put(Language.ENGLISH, en);
            category.setName(name);
            categoryRepository.save(category);
        }
        return "redirect:/categories";
    }


    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/categories";
    }
}
