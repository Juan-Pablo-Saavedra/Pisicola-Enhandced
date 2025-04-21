package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.sena.crud_basic.DTO.categoryDTO;
import com.sena.crud_basic.service.categoryService;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api/v1/category")
public class categoryController {

    @Autowired
    private categoryService categoryService;

    @PostMapping
    public ResponseEntity<String> saveCategory(@RequestBody categoryDTO categoryDTO) {
        String response = categoryService.save(categoryDTO);
        HttpStatus status = response.equals("Categoría guardada exitosamente.") ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping
    public ResponseEntity<List<categoryDTO>> getAllCategories() {
        List<categoryDTO> categories = categoryService.getAllCategories();
        return categories.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable int id) {
        Optional<categoryDTO> categoryOpt = categoryService.getCategoryById(id);
        return categoryOpt.isPresent()
                ? ResponseEntity.ok(categoryOpt.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoría no encontrada.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable int id, @RequestBody categoryDTO categoryDTO) {
        String response = categoryService.updateCategory(id, categoryDTO);
        HttpStatus status = response.equals("Categoría actualizada exitosamente.") ? HttpStatus.OK
                : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id) {
        String response = categoryService.deleteCategory(id);
        HttpStatus status = response.equals("Categoría eliminada exitosamente.") ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<categoryDTO>> filterCategories(@RequestParam(required = false) String name) {
        List<categoryDTO> filteredCategories = categoryService.filterCategories(name);
        return filteredCategories.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(filteredCategories, HttpStatus.OK);
    }

}
