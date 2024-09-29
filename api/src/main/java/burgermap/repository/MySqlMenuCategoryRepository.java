package burgermap.repository;

import burgermap.entity.MenuCategory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MySqlMenuCategoryRepository implements MenuCategoryRepository {

    private final EntityManager em;

    @Override
    public List<MenuCategory> findAll() {
        return em.createQuery("select m from MenuCategory m", MenuCategory.class).getResultList();
    }

    @Override
    public Optional<MenuCategory> findByMenuCategoryId(Long menuCategoryId) {
        MenuCategory menuCategory = em.find(MenuCategory.class, menuCategoryId);
        return Optional.ofNullable(menuCategory);
    }
}
