package com.dean.baby.mvc.controller;

import com.dean.baby.common.dto.CategoryForm;
import com.dean.baby.common.entity.Category;
import com.dean.baby.common.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/categories")
public class CategoryController {


    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("newCategory", new CategoryForm());
        return "category/list";
    }

    @PostMapping
    public String addCategory(@RequestParam("zhTW") String zhTW,
                              @RequestParam("en") String en) {
        categoryService.create(zhTW, en);
        return "redirect:/categories";
    }

    // 3. 進入修改頁面 (edit.html)
    @GetMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") UUID id, Model model) {
        Optional<Category> categoryOpt = categoryService.findById(id);
        if (categoryOpt.isPresent()) {
            model.addAttribute("category", categoryOpt.get());
            return "category/edit"; // 對應到 /templates/category/edit.html
        } else {
            // 若找不到則返回列表頁
            return "redirect:/categories";
        }
    }

    @PostMapping("/edit")
    public String updateCategory(@RequestParam("id") UUID id,
                                 @RequestParam("zhTW") String zhTW,
                                 @RequestParam("en") String en) {
        categoryService.update(id, zhTW, en);
        return "redirect:/categories";
    }


    @PostMapping("/delete/{id}")
    public String delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return "redirect:/categories";
    }
}
