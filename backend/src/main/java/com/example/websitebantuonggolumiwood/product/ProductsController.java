package com.example.websitebantuonggolumiwood.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("http://localhost:5173")

public class ProductsController {
    private final ProductsService serviceProduct;
    private final ProductsRepositories productsRepositories;
    private final ProductsService productsService;

    public ProductsController(ProductsService serviceProduct, ProductsRepositories productsRepositories, ProductsService productsService) {
        this.serviceProduct = serviceProduct;
        this.productsRepositories = productsRepositories;
        this.productsService = productsService;
    }


    @GetMapping
    public ResponseEntity<Page<ProductsEntity>> filterProducts(
            @RequestParam(required = false) List<Integer> categories,

            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String materials,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int sizePerPage,
            @RequestParam(defaultValue = "price,asc") String sort,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String size
    ) {
        String[] sortParts = sort.split(",");
        String sortBy = sortParts[0];
        String sortDir = sortParts.length > 1 ? sortParts[1] : "asc";
        Page<ProductsEntity> products = productsService.filterProducts(
                categories, minPrice, maxPrice, materials,
                page, sizePerPage, sortBy, sortDir, keyword, size
        );
        return ResponseEntity.ok(products);
    }

    @GetMapping("/slug/{slug}")
    public ProductsEntity getProductsBySlug(@PathVariable String slug) {
        return serviceProduct.getProductsBySlug(slug);
    }
    // get all chất liệu
    @GetMapping("/materials")
    public List<String> getProductsByMaterials() {
        return productsRepositories.findDistinctMaterials();
    }
    @GetMapping("/category/{slug}")
    public List<ProductsEntity> getProductsByCategory(@PathVariable String slug) {
        return serviceProduct.getProductsByCategorySlug(slug);
    }
    // get all product admin
    @GetMapping("/admin")
    public ResponseEntity<Page<ProductsEntity>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "createdAt,desc") String sort
    ) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));

        Specification<ProductsEntity> spec = ProductSpecification.filter(keyword, categoryId, isActive);
        Page<ProductsEntity> products = productsRepositories.findAll(spec, pageable);

        return ResponseEntity.ok(products); // Trả thẳng entity
    }



}
