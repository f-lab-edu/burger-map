package burgermap.repository;

import burgermap.entity.MenuCategory;

import java.util.List;

public interface MenuCategoryRepository {
    public List<MenuCategory> findAll();
}
