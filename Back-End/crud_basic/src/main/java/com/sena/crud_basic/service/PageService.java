package com.sena.crud_basic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sena.crud_basic.DTO.PageDTO;
import com.sena.crud_basic.DTO.responseDTO;
import com.sena.crud_basic.model.pages;
import com.sena.crud_basic.repository.Ipage;

@Service
public class PageService {
    @Autowired
    private Ipage data;
    
    public List<pages> findAll() {
        return data.findAll();
    }

    public Optional<pages> findById(int id) {
        return data.findById(id);
    }

    public responseDTO deletePage(int id) {
        Optional<pages> pageOpt = findById(id);
        if (!pageOpt.isPresent()) {
            return new responseDTO("404", "La página no existe");
        }
        data.deleteById(id);
        return new responseDTO("200", "Página eliminada correctamente");
    }

    public responseDTO save(PageDTO pageDTO) {
        pages page = convertToModel(pageDTO);
        data.save(page);
        return new responseDTO("200", "Página guardada correctamente");
    }

    public responseDTO updatePage(int id, PageDTO pageDTO) {
        Optional<pages> pageOpt = findById(id);
        if (!pageOpt.isPresent()) {
            return new responseDTO("404", "La página no existe");
        }
        pages updatedPage = pageOpt.get();
        updatedPage.setName(pageDTO.getName());
        updatedPage.setUrl(pageDTO.getUrl());
        updatedPage.setDescription(pageDTO.getDescription());

        data.save(updatedPage);
        return new responseDTO("200", "Página actualizada correctamente");
    }

    public PageDTO convertToDTO(pages page) {
        return new PageDTO(
            page.getPageid(),
            page.getName(),
            page.getUrl(),
            page.getDescription()
        );
    }

    public pages convertToModel(PageDTO pageDTO) {
        return new pages(
            pageDTO.getPageid(),
            pageDTO.getName(),
            pageDTO.getUrl(),
            pageDTO.getDescription()
        );
    }

}