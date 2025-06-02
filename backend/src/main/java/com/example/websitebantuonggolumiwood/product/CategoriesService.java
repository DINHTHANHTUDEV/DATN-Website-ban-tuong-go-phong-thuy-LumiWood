package com.example.websitebantuonggolumiwood.product;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {
    private final CategoriesRepositories repositoriesCategories;

    public CategoriesService(CategoriesRepositories repositoriesCategories) {
        this.repositoriesCategories = repositoriesCategories;
    }

    public List<CategoriesEntity> getAllCategories() {
        return repositoriesCategories.findAll();
    }

    public CategoriesEntity getCategoryBySlug(String slug) {
        return repositoriesCategories.findBySlug(slug).get();
    }
}
