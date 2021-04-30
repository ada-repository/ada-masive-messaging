/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fioxin.messaging.messaging.domain.Service;

import com.fioxin.messaging.messaging.domain.entity.Category;
import java.util.List;

/**
 *
 * @author FioxinCel
 */

public interface ICategoryService {
    
    List<Category> getAll();
    Category getCategoryById(int id);
    void deleteCategory(int id);
    Category updateCategory(Category actually,Category newCategory);
    Category saveCategory(Category category);
    
}
