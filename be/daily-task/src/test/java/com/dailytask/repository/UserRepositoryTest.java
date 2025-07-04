package com.dailytask.repository;

import com.dailytask.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        testUser1 = new User();
        testUser1.setUsername("testuser1");
        entityManager.persistAndFlush(testUser1);

        testUser2 = new User();
        testUser2.setUsername("testuser2");
        entityManager.persistAndFlush(testUser2);
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenUserExists() {
        Optional<User> found = userRepository.findByUsername("testuser1");

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser1");
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenUserDoesNotExist() {
        Optional<User> found = userRepository.findByUsername("nonexistent");

        assertThat(found).isEmpty();
    }

    @Test
    void existsByUsername_ShouldReturnTrue_WhenUserExists() {
        boolean exists = userRepository.existsByUsername("testuser1");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByUsername_ShouldReturnFalse_WhenUserDoesNotExist() {
        boolean exists = userRepository.existsByUsername("nonexistent");

        assertThat(exists).isFalse();
    }

    @Test
    void existsByUsernameAndIdNot_ShouldReturnTrue_WhenUsernameExistsForDifferentUser() {
        boolean exists = userRepository.existsByUsernameAndIdNot("testuser1", testUser2.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void existsByUsernameAndIdNot_ShouldReturnFalse_WhenUsernameExistsForSameUser() {
        boolean exists = userRepository.existsByUsernameAndIdNot("testuser1", testUser1.getId());

        assertThat(exists).isFalse();
    }

    @Test
    void findByUsernameContainingIgnoreCase_ShouldReturnMatchingUsers() {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase("test");

        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getUsername)
                .containsExactlyInAnyOrder("testuser1", "testuser2");
    }

    @Test
    void findByUsernameContainingIgnoreCase_ShouldReturnEmpty_WhenNoMatch() {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase("nomatch");

        assertThat(users).isEmpty();
    }

    @Test
    void countByUsernameStartingWith_ShouldReturnCorrectCount() {
        long count = userRepository.countByUsernameStartingWith("test");

        assertThat(count).isEqualTo(2);
    }

    @Test
    void countByUsernameStartingWith_ShouldReturnZero_WhenNoMatch() {
        long count = userRepository.countByUsernameStartingWith("nomatch");

        assertThat(count).isZero();
    }
} 