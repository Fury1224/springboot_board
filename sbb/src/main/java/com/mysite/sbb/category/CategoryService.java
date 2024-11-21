package com.mysite.sbb.category;

import java.util.List;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;

import lombok.RequiredArgsConstructor;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        this.categoryRepository.save(category);
    }
    
    public List<Category> getAll() {
        return this.categoryRepository.findAll();
    }

    public Category getCategoryByName(String name) {
        Optional<Category> oc = this.categoryRepository.findByName(name);
        if (oc.isPresent()) {
            return oc.get();
        } else {
            throw new DataNotFoundException("category not found");
        }
    }
     

    public Optional<Category> getCategoryById(Integer id) {
        return this.categoryRepository.findById(id);
    } 
    
}
