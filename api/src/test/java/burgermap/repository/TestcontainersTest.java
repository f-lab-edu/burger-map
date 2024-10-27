package burgermap.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@Transactional
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
