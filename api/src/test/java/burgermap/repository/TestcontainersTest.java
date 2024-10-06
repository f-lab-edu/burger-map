package burgermap.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@Transactional
class TestcontainersTest {
//    @Container
//    private static MySQLContainer container = new MySQLContainer("mysql:8.3.0")
//            .withDatabaseName("burgermap-test")
//            .withUsername("user")
//            .withPassword("useruser");

    @Container
    private MySQLContainer container = new MySQLContainer("mysql:8.3.0")
            .withDatabaseName("burgermap-test")
            .withUsername("user")
            .withPassword("useruser");

//    @DynamicPropertySource  // 동적으로 프로퍼티 설정
//    static void dynamicProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", container::getJdbcUrl);
//        registry.add("spring.datasource.username", container::getUsername);
//        registry.add("spring.datasource.password", container::getPassword);
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
//        registry.add("spring.jpa.properties.hibernate.hbm2ddl.import_files", () -> "init.sql");
//        registry.add("logging.level.org.hibernate.SQL", () -> "DEBUG");
//        registry.add("logging.level.org.hibernate.orm.jdbc.bind", () -> "TRACE");
//    }

    @Test
    @DisplayName("MySQL 컨테이너 동작")
    void containerTest() {
        Assertions.assertThat(container.isRunning()).isTrue();
    }
}
