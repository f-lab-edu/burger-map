package burgermap.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class TestcontainersTest {
    @Container
    private MySQLContainer container = new MySQLContainer("mysql:8.3.0")
            .withDatabaseName("burgermap-test")
            .withUsername("user")
            .withPassword("useruser");

    @Test
    @DisplayName("MySQL 컨테이너 동작")
    void containerTest() {
        Assertions.assertThat(container.isRunning()).isTrue();
    }
}
