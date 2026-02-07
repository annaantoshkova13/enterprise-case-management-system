package org.example.enterprisecasemanagementsystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void shouldHaveThreeRoles() {
        Role[] roles = Role.values();
        assertEquals(3, roles.length);
        assertArrayEquals(new Role[]{Role.ADMIN, Role.STUDENT, Role.TEACHER}, roles);
    }

    @Test
    void shouldParseRoleFromString() {
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"));
        assertEquals(Role.STUDENT, Role.valueOf("STUDENT"));
        assertEquals(Role.TEACHER, Role.valueOf("TEACHER"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ADMIN", "STUDENT", "TEACHER"})
    void shouldParseValidRoleStrings(String roleString) {
        Role role = Role.valueOf(roleString);
        assertNotNull(role);
    }

    @Test
    void shouldThrowExceptionForInvalidRole() {
        assertThrows(IllegalArgumentException.class, () -> Role.valueOf("INVALID"));
    }

    @Test
    void shouldGetRoleName() {
        assertEquals("ADMIN", Role.ADMIN.name());
        assertEquals("STUDENT", Role.STUDENT.name());
        assertEquals("TEACHER", Role.TEACHER.name());
    }

    @Test
    void shouldCheckIfUserHasRole() {
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(Role.ADMIN);

        Set<Role> studentRoles = new HashSet<>();
        studentRoles.add(Role.STUDENT);

        Set<Role> multiRoles = new HashSet<>();
        multiRoles.add(Role.STUDENT);
        multiRoles.add(Role.TEACHER);

        class MockUser {
            private final Set<Role> roles;

            MockUser(Set<Role> roles) {
                this.roles = roles;
            }

            boolean hasRole(Role role) {
                return roles.contains(role);
            }

            boolean hasAnyRole(Role... rolesToCheck) {
                for (Role role : rolesToCheck) {
                    if (roles.contains(role)) {
                        return true;
                    }
                }
                return false;
            }

            boolean hasAllRoles(Role... rolesToCheck) {
                for (Role role : rolesToCheck) {
                    if (!roles.contains(role)) {
                        return false;
                    }
                }
                return true;
            }
        }

        MockUser adminUser = new MockUser(adminRoles);
        MockUser studentUser = new MockUser(studentRoles);
        MockUser multiRoleUser = new MockUser(multiRoles);

        assertTrue(adminUser.hasRole(Role.ADMIN));
        assertFalse(adminUser.hasRole(Role.STUDENT));
        assertFalse(adminUser.hasRole(Role.TEACHER));

        assertTrue(studentUser.hasRole(Role.STUDENT));
        assertFalse(studentUser.hasRole(Role.ADMIN));
        assertFalse(studentUser.hasRole(Role.TEACHER));

        assertTrue(multiRoleUser.hasRole(Role.STUDENT));
        assertTrue(multiRoleUser.hasRole(Role.TEACHER));
        assertFalse(multiRoleUser.hasRole(Role.ADMIN));

        assertTrue(adminUser.hasAnyRole(Role.ADMIN));
        assertTrue(adminUser.hasAnyRole(Role.ADMIN, Role.STUDENT));
        assertFalse(adminUser.hasAnyRole(Role.STUDENT, Role.TEACHER));

        assertTrue(multiRoleUser.hasAllRoles(Role.STUDENT, Role.TEACHER));
        assertFalse(multiRoleUser.hasAllRoles(Role.STUDENT, Role.ADMIN));
    }

    @Test
    void shouldConvertRoleToString() {
        assertEquals("ADMIN", Role.ADMIN.toString());
        assertEquals("STUDENT", Role.STUDENT.toString());
        assertEquals("TEACHER", Role.TEACHER.toString());
    }

    @Test
    void shouldHaveCorrectOrdinalValues() {
        assertEquals(0, Role.ADMIN.ordinal());
        assertEquals(1, Role.STUDENT.ordinal());
        assertEquals(2, Role.TEACHER.ordinal());
    }

    @Test
    void shouldCompareRoles() {
        assertTrue(Role.ADMIN.compareTo(Role.STUDENT) < 0);
        assertTrue(Role.STUDENT.compareTo(Role.TEACHER) < 0);
        assertTrue(Role.ADMIN.compareTo(Role.ADMIN) == 0);
    }
}