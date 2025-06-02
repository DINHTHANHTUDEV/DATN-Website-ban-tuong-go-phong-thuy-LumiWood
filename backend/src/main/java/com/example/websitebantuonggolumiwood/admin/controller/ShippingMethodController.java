package com.example.websitebantuonggolumiwood.admin.controller;

import com.example.websitebantuonggolumiwood.admin.entity.ShippingMethod;
import com.example.websitebantuonggolumiwood.admin.repository.ShippingMethodRepository;
import com.example.websitebantuonggolumiwood.admin.response.ShippingMethodResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/shipping-methods")
@CrossOrigin(origins = "http://localhost:5174")
public class ShippingMethodController {

    @Autowired
    private ShippingMethodRepository shippingMethodRepository;

    @GetMapping
    public Page<ShippingMethodResponse> getAll(Pageable pageable) {
        Page<ShippingMethod> page = shippingMethodRepository.findAll(pageable);
        return page.map(method -> new ShippingMethodResponse(
                method.getId(),
                method.getName(),
                method.getDescription(),
                method.getBaseCost(),
                method.getEstimatedDaysMin(),
                method.getEstimatedDaysMax(),
                method.getIsActive()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShippingMethodResponse> getById(@PathVariable Integer id) {
        return shippingMethodRepository.findById(id)
                .map(method -> {
                    ShippingMethodResponse response = new ShippingMethodResponse(
                            method.getId(),
                            method.getName(),
                            method.getDescription(),
                            method.getBaseCost(),
                            method.getEstimatedDaysMin(),
                            method.getEstimatedDaysMax(),
                            method.getIsActive()
                    );
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createShippingMethod(@RequestBody ShippingMethod newMethod) {
        ShippingMethod saved = shippingMethodRepository.save(newMethod);
        return ResponseEntity.ok(Map.of("message", "Thêm mới thành công", "id", saved.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateShippingMethod(@PathVariable Integer id, @RequestBody ShippingMethod updatedMethod) {
        return shippingMethodRepository.findById(id)
                .map(method -> {
                    method.setName(updatedMethod.getName());
                    method.setDescription(updatedMethod.getDescription());
                    method.setBaseCost(updatedMethod.getBaseCost());
                    method.setEstimatedDaysMin(updatedMethod.getEstimatedDaysMin());
                    method.setEstimatedDaysMax(updatedMethod.getEstimatedDaysMax());
                    method.setIsActive(updatedMethod.getIsActive());
                    shippingMethodRepository.save(method);
                    return ResponseEntity.ok(Map.of("message", "Cập nhật thành công"));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShippingMethod(@PathVariable Integer id) {
        return shippingMethodRepository.findById(id)
                .map(method -> {
                    shippingMethodRepository.delete(method);
                    return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
