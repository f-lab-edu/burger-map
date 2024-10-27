package burgermap.repository;

import burgermap.entity.MenuCategory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class MySqlMenuCategoryRepositoryTest extends TestcontainersTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MenuCategoryRepository repository;

    private final String[] menuCategoryNames = new String[] {
            "버거", "감자튀김", "양파튀김", "피자", "치킨", "탄산음료", "맥주", "하이볼", "치즈스틱", "치즈볼"
    };

    @BeforeEach
    void initMenuCategoryData() {
        // 테스트를 위한 메뉴 카테고리 데이터 등록
        // 테스트 케이스마다 Id 자동 생성 시작값 초기화
        em.createNativeQuery("alter table menu_category AUTO_INCREMENT = 1").executeUpdate();
        for (String menuCategoryName : menuCategoryNames) {
            MenuCategory menuCategory = new MenuCategory();
            menuCategory.setName(menuCategoryName);
            em.persist(menuCategory);
            System.out.println(menuCategory.getMenuCategoryId());
        }
    }

    @Test
    @DisplayName("등록된 모든 메뉴 카테고리 조회")
    void findAll() {
        // 등록된 모든 메뉴 카테고리 조회, 조회된 메뉴 카테고리 수와 메뉴 카테고리명 검증
        List<MenuCategory> allMenuCategories = repository.findAll();

        assertThat(allMenuCategories).hasSize(menuCategoryNames.length);
        assertThat(allMenuCategories).extracting("name").contains(menuCategoryNames);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 5L, 10L})
    @DisplayName("메뉴 카테고리 번호로 조회")
    void findByMenuCategoryId(long menuCategoryId) {
        // 메뉴 카테고리 번호로 조회, 조회된 메뉴 카테고리의 메뉴 카테고리명 검증
        Optional<MenuCategory> menuCategory = repository.findByMenuCategoryId(menuCategoryId);
        String MenuCategoryName = menuCategoryNames[(int) menuCategoryId - 1];

        assertThat(menuCategory).hasValueSatisfying(category -> assertThat(category.getName()).isEqualTo(MenuCategoryName));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 카테고리 번호로 조회")
    void findByUnregisteredMenuCategoryId() {
        // 존재하지 않는 메뉴 카테고리 번호(-1L, long 최댓값)로 조회, 결과: Optional.empty()
        Optional<MenuCategory> menuCategoryNegativeId = repository.findByMenuCategoryId(-1L);
        Optional<MenuCategory> menuCategoryUnregisteredId = repository.findByMenuCategoryId(Long.MAX_VALUE);

        assertThat(menuCategoryNegativeId).isEmpty();
        assertThat(menuCategoryUnregisteredId).isEmpty();
    }
}
