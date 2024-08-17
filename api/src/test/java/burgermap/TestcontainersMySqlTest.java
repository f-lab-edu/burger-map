package burgermap;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestcontainersMySqlTest {

    @Container
    MySQLContainer mysql = new MySQLContainer("mysql:8.0.33-debian");
}
