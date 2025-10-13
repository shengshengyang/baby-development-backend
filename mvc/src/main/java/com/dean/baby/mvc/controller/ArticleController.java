package com.dean.baby.mvc.controller;

import com.dean.baby.common.dto.ArticleCreateRequestVo;
import com.dean.baby.common.dto.ArticleResponseDto;
import com.dean.baby.common.service.ArticleService;
import com.dean.baby.common.service.OptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final OptionService optionService;

    public ArticleController(ArticleService articleService, OptionService optionService) {
        this.articleService = articleService;
        this.optionService = optionService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("articles", articleService.listArticles(null, null));
        model.addAttribute("categories", optionService.getCategoryOptions());
        return "article/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("categories", optionService.getCategoryOptions());
        return "article/create";
    }

    @PostMapping
    @ResponseBody
    public ArticleResponseDto createArticle(@RequestBody ArticleCreateRequestVo vo) {
        return articleService.createOrUpdateArticle(vo);
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable UUID id, Model model) {
        model.addAttribute("article", articleService.getArticle(id, null));
        model.addAttribute("categories", optionService.getCategoryOptions());
        return "article/edit";
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ArticleResponseDto updateArticle(@RequestBody ArticleCreateRequestVo vo) {
        return articleService.createOrUpdateArticle(vo);
    }

    @PostMapping("/delete/{id}")
    public String deleteArticle(@PathVariable UUID id) {
        articleService.deleteArticle(id);
        return "redirect:/articles";
    }
}
