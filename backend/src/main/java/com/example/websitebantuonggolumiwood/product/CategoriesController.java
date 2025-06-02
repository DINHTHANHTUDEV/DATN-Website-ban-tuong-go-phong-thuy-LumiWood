package com.example.websitebantuonggolumiwood.product;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/categories")
@CrossOrigin("http://localhost:5173")
public class CategoriesController {
    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }
    @GetMapping
    public  List<CategoriesEntity>getCategories() {
        return categoriesService.getAllCategories();

    }
    @GetMapping("/slug/{slug}")
    public CategoriesEntity getCategoryBySlug(@PathVariable String slug) {
        return categoriesService.getCategoryBySlug(slug);
    }


}
