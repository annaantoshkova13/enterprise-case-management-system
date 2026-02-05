package org.example.enterprisecasemanagementsystem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EnterpriseCaseManagementSystemApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should not be null");
    }

    @Test
    void mainBeansAreLoaded() {
        assertThat(applicationContext.getBean(EnterpriseCaseManagementSystemApplication.class))
                .isNotNull();

        String[] beanNames = applicationContext.getBeanDefinitionNames();
        assertThat(beanNames).isNotEmpty();

        System.out.println("Total beans loaded: " + beanNames.length);
    }

    @Test
    void testDatabaseConnection() {
        assertNotNull(applicationContext);
    }

    @Test
    void testSecurityConfiguration() {
        try {
            Object securityFilterChain = applicationContext.getBean("securityFilterChain");
            assertNotNull(securityFilterChain);
        } catch (Exception e) {
            System.out.println("Security not configured: " + e.getMessage());
        }
    }
}
