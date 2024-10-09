package burgermap.repository;

import burgermap.entity.Ingredient;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


class MySqlIngredientRepositoryTest extends TestcontainersTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private IngredientRepository repository;

    private final String[] ingredientNames = new String[] {
        "소고기패티", "체다치즈", "브리오슈번", "양상추", "토마토", "칠리소스", "칠리비프", "새우패티", "해쉬브라운", "베이컨"
    };

    @BeforeEach
    void initIngredientsData() {
        // 테스트를 위한 재료 데이터 등록
        // 테스트 케이스마다 Id 자동 생성 시작값 초기화
        em.createNativeQuery("alter table ingredient AUTO_INCREMENT = 1").executeUpdate();
        for (String ingredientName : ingredientNames) {
            Ingredient ingredient = new Ingredient();
            ingredient.setName(ingredientName);
            em.persist(ingredient);
            System.out.println(ingredient.getIngredientId());
        }
    }

    @Test
    @DisplayName("등록된 모든 재료 조회")
    void findAll() {
        // 등록된 모든 재료 조회, 조회된 재료 수와 재료명 검증
        List<Ingredient> allIngredients = repository.findAll();

        assertThat(allIngredients).hasSize(ingredientNames.length);
        assertThat(allIngredients).extracting("name").contains(ingredientNames);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 5L, 10L})
    @DisplayName("재료 번호로 재료 조회")
    void findByIngredientId(long ingredientId) {
        // 재료 번호로 조회, 조회된 재료의 재료명 검증
        Optional<Ingredient> ingredient = repository.findByIngredientId(ingredientId);
        String ingredientName = ingredientNames[(int) ingredientId - 1];

        assertThat(ingredient).hasValueSatisfying(i -> assertThat(i.getName()).isEqualTo(ingredientName));
    }

    @Test
    @DisplayName("존재하지 않는 재료 번호로 재료 조회")
    void findByUnregisteredIngredientId() {
        // 존재하지 않는 재료 번호(-1L, Long 최댓값)로 조회, 결과: Optional.empty()
        Optional<Ingredient> ingredientNegativeId = repository.findByIngredientId(-1L);
        Optional<Ingredient> ingredientUnregisteredId = repository.findByIngredientId(Long.MAX_VALUE);

        assertThat(ingredientNegativeId).isEmpty();
        assertThat(ingredientUnregisteredId).isEmpty();
    }
}
