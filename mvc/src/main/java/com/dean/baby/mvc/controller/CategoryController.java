package com.dean.baby.mvc.controller;

import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.entity.Category;
import com.dean.baby.common.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 初始化資料綁定器，啟用自動增長 nested paths
     * 這樣 Spring 可以自動創建 nested objects
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowNestedPaths(true);
    }

    // 顯示所有 Categories 列表
    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "category/list";
    }

    // 顯示新增 Category 表單
    @GetMapping("/new")
    public String newCategory(Model model) {
        Category category = new Category();
        // 初始化多語名稱物件
        category.setName(new LangFieldObject());
        model.addAttribute("category", category);
        return "category/form";
    }

    // 處理新增 Category
    @PostMapping
    public String createCategory(@ModelAttribute("nameObject") LangFieldObject nameObject,
                                 RedirectAttributes redirectAttributes) {
        try {
            // 若未綁定，避免 NPE
            if (nameObject == null) {
                nameObject = new LangFieldObject();
            }
            categoryService.create(nameObject);
            redirectAttributes.addFlashAttribute("success", "分類創建成功！");
            return "redirect:/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "分類創建失敗：" + e.getMessage());
            return "redirect:/categories/new";
        }
    }

    /**
     * 查看 Category 詳情（包含編輯功能）
     */
    @GetMapping("/view/{id}")
    public String viewCategory(@PathVariable UUID id,
                              @RequestParam(value = "edit", defaultValue = "false") boolean editMode,
                              Model model, RedirectAttributes redirectAttributes) {
        Optional<Category> categoryOpt = categoryService.findById(id);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            if (category.getName() == null) {
                category.setName(new LangFieldObject());
            }
            model.addAttribute("category", category);
            model.addAttribute("editMode", editMode);
            return "category/view";
        } else {
            redirectAttributes.addFlashAttribute("error", "分類不存在！");
            return "redirect:/categories";
        }
    }

    /**
     * 處理更新 Category（從查看頁面提交）
     */
    @PostMapping("/view/{id}")
    public String updateCategoryFromView(@PathVariable UUID id,
                                        @ModelAttribute("nameObject") LangFieldObject nameObject,
                                        RedirectAttributes redirectAttributes) {
        try {
            if (nameObject == null) {
                nameObject = new LangFieldObject();
            }
            categoryService.update(id, nameObject);
            redirectAttributes.addFlashAttribute("success", "分類更新成功！");
            return "redirect:/categories/view/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "分類更新失敗：" + e.getMessage());
            return "redirect:/categories/view/" + id + "?edit=true";
        }
    }

    // 處理刪除 Category
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);
            redirectAttributes.addFlashAttribute("success", "分類刪除成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "分類刪除失敗：" + e.getMessage());
        }
        return "redirect:/categories";
    }
}
