package org.example.enterprisecasemanagementsystem.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ProfileTest {

    private static class TestProfile extends Profile {
        public TestProfile() {
            super();
        }

        public TestProfile(String firstName, String lastName) {
            super(firstName, lastName);
        }
    }

    private TestProfile profile;

    @BeforeEach
    void setUp() {
        profile = new TestProfile("John", "Doe");
    }

    @Test
    void shouldCreateProfile_WithConstructor() {
        assertEquals("John", profile.getFirstName());
        assertEquals("Doe", profile.getLastName());
        assertNull(profile.getId());
    }

    @Test
    void shouldSetAndGetId() {
        profile.setId(1L);
        assertEquals(1L, profile.getId());
    }

    @Test
    void shouldSetAndGetFirstName() {
        profile.setFirstName("Jane");
        assertEquals("Jane", profile.getFirstName());
    }

    @Test
    void shouldSetAndGetLastName() {
        profile.setLastName("Smith");
        assertEquals("Smith", profile.getLastName());
    }

    @Test
    void shouldHaveEmptyConstructor() {
        TestProfile emptyProfile = new TestProfile();
        assertNotNull(emptyProfile);
        assertNull(emptyProfile.getFirstName());
        assertNull(emptyProfile.getLastName());
        assertNull(emptyProfile.getId());
    }
}
