/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fioxin.messaging.messaging.Domain.Service.Implements;

import com.fioxin.messaging.messaging.domain.Repository.CategoryJpaRepository;
import com.fioxin.messaging.messaging.domain.Service.ICategoryService;
import com.fioxin.messaging.messaging.domain.entity.Category;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author FioxinCel
 */
@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryJpaRepository cateRepo;
    
    @Override
    public List<Category> getAll() {
        return cateRepo.findAll();
    }

    @Override
    public Category getCategoryById(int id) {
        return cateRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteCategory(int id) {
        cateRepo.deleteById(id);
    }

    @Override
    public Category updateCategory(Category actually, Category newCategory) {
        actually.setName(newCategory.getName());
        actually.setStatus(newCategory.isStatus());
        return cateRepo.save(actually);
    }

    @Override
    public Category saveCategory(Category category) {
        return cateRepo.save(category);
    }
    
}
