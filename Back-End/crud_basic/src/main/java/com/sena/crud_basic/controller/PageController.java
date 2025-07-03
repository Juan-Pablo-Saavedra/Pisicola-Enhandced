package com.sena.crud_basic.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.crud_basic.DTO.PageDTO;
import com.sena.crud_basic.DTO.responseDTO;
import com.sena.crud_basic.model.pages;
import com.sena.crud_basic.service.PageService;


@RestController
@RequestMapping("/page")
public class PageController {
    @Autowired
    private PageService pageService;

    @GetMapping("/")
    public ResponseEntity<List<pages>> getAllPages() {
        List<pages> pages = pageService.findAll();
        return new ResponseEntity<>(pages, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPageById(@PathVariable int id) {
        Optional<pages> pageOpt = pageService.findById(id);
        if (!pageOpt.isPresent()) {
            return new ResponseEntity<>("Página no encontrada", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pageOpt.get(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Object> createPage(@RequestBody PageDTO pageDTO) {
        responseDTO response = pageService.save(pageDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePage(@PathVariable int id, @RequestBody PageDTO pageDTO) {
        responseDTO response = pageService.updatePage(id, pageDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePage(@PathVariable int id) {
        responseDTO response = pageService.deletePage(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}