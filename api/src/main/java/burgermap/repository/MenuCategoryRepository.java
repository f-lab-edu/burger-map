package burgermap.repository;

import burgermap.entity.MenuCategory;

import java.util.List;
import java.util.Optional;

public interface MenuCategoryRepository {
    public List<MenuCategory> findAll();

    public Optional<MenuCategory> findByMenuCategoryId(Long menuCategoryId);
}
