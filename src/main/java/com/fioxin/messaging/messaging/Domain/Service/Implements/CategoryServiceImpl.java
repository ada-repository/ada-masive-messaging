/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fioxin.messaging.messaging.Domain.Service.Implements;

import com.fioxin.messaging.messaging.domain.Repository.CategoryJpaRepository;
import com.fioxin.messaging.messaging.domain.Service.ICategoryService;
import com.fioxin.messaging.messaging.domain.Service.ISubscriptionService;
import com.fioxin.messaging.messaging.domain.entity.Category;
import com.fioxin.messaging.messaging.domain.entity.Subscription;
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
    
    @Autowired
    private ISubscriptionService subService;
    
    @Override
    public List<Category> getAll() {
        return cateRepo.findAll();
    }

    @Override
    public Category getCategoryById(int id) {
        return cateRepo.findById(id).orElse(null);
    }

    @Override
    public boolean deleteCategory(int id) {
       List<Subscription> listSubs = subService.getAll();
       boolean rpta = listSubs.stream().anyMatch(  s -> catActiveInSubs(s, id) );
       if(!rpta){
              Category cate = cateRepo.findById(id).get();
              cate.setStatus(false);
              cateRepo.save(cate);
              return true;
       }  else
           return false;
       
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
    
    
    private boolean catActiveInSubs(Subscription subs, int id){
       return  subs.getStatus() == true && subs.getPlan().getCategory().getId() == id; 
    }
    
}
