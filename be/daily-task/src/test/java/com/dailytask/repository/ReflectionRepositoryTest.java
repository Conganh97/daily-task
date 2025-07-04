package com.dailytask.repository;

import com.dailytask.entity.Reflection;
import com.dailytask.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ReflectionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReflectionRepository reflectionRepository;

    private User testUser1;
    private User testUser2;
    private LocalDate today;
    private LocalDate yesterday;
    private LocalDate twoDaysAgo;

    @BeforeEach
    void setUp() {
        testUser1 = new User();
        testUser1.setUsername("testuser1");
        entityManager.persistAndFlush(testUser1);

        testUser2 = new User();
        testUser2.setUsername("testuser2");
        entityManager.persistAndFlush(testUser2);

        today = LocalDate.now();
        yesterday = today.minusDays(1);
        twoDaysAgo = today.minusDays(2);

        createTestReflection(testUser1, today, 8, "Great day today!");
        createTestReflection(testUser1, yesterday, 6, "Moderate energy day");
        createTestReflection(testUser1, twoDaysAgo, 3, "Low energy");
        createTestReflection(testUser2, today, 9, "Excellent day!");
    }

    private void createTestReflection(User user, LocalDate date, int energyRating, String reflectionText) {
        Reflection reflection = new Reflection();
        reflection.setUser(user);
        reflection.setDate(date);
        reflection.setEnergyRating(energyRating);
        reflection.setReflectionText(reflectionText);
        entityManager.persistAndFlush(reflection);
    }

    @Test
    void findByUserAndDate_ShouldReturnReflection_WhenExists() {
        Optional<Reflection> reflection = reflectionRepository.findByUserAndDate(testUser1, today);

        assertThat(reflection).isPresent();
        assertThat(reflection.get().getEnergyRating()).isEqualTo(8);
        assertThat(reflection.get().getReflectionText()).isEqualTo("Great day today!");
    }

    @Test
    void findByUserAndDate_ShouldReturnEmpty_WhenNotExists() {
        LocalDate futureDate = today.plusDays(1);
        Optional<Reflection> reflection = reflectionRepository.findByUserAndDate(testUser1, futureDate);

        assertThat(reflection).isEmpty();
    }

    @Test
    void findByUserAndDateBetween_ShouldReturnReflectionsInRange() {
        List<Reflection> reflections = reflectionRepository.findByUserAndDateBetween(testUser1, twoDaysAgo, today);

        assertThat(reflections).hasSize(3);
        assertThat(reflections).extracting(Reflection::getEnergyRating)
                .containsExactlyInAnyOrder(8, 6, 3);
    }

    @Test
    void findByUserOrderByDateDesc_ShouldReturnAllReflectionsOrderedByDate() {
        List<Reflection> reflections = reflectionRepository.findByUserOrderByDateDesc(testUser1);

        assertThat(reflections).hasSize(3);
        assertThat(reflections.get(0).getDate()).isEqualTo(today);
        assertThat(reflections.get(1).getDate()).isEqualTo(yesterday);
        assertThat(reflections.get(2).getDate()).isEqualTo(twoDaysAgo);
    }

    @Test
    void findByUserAndEnergyRatingGreaterThanEqualOrderByDateDesc_ShouldReturnFilteredReflections() {
        List<Reflection> reflections = reflectionRepository.findByUserAndEnergyRatingGreaterThanEqualOrderByDateDesc(testUser1, 6);

        assertThat(reflections).hasSize(2);
        assertThat(reflections).extracting(Reflection::getEnergyRating)
                .containsExactly(8, 6);
    }

    @Test
    void findByUserAndEnergyRatingLessThanEqualOrderByDateDesc_ShouldReturnFilteredReflections() {
        List<Reflection> reflections = reflectionRepository.findByUserAndEnergyRatingLessThanEqualOrderByDateDesc(testUser1, 6);

        assertThat(reflections).hasSize(2);
        assertThat(reflections).extracting(Reflection::getEnergyRating)
                .containsExactly(6, 3);
    }

    @Test
    void findByUserAndEnergyRatingBetweenOrderByDateDesc_ShouldReturnFilteredReflections() {
        List<Reflection> reflections = reflectionRepository.findByUserAndEnergyRatingBetweenOrderByDateDesc(testUser1, 4, 8);

        assertThat(reflections).hasSize(2);
        assertThat(reflections).extracting(Reflection::getEnergyRating)
                .containsExactly(8, 6);
    }

    @Test
    void countByUser_ShouldReturnCorrectCount() {
        long count = reflectionRepository.countByUser(testUser1);

        assertThat(count).isEqualTo(3);
    }

    @Test
    void countByUserAndDateBetween_ShouldReturnCorrectCount() {
        long count = reflectionRepository.countByUserAndDateBetween(testUser1, yesterday, today);

        assertThat(count).isEqualTo(2);
    }

    @Test
    void findAverageEnergyRatingByUser_ShouldReturnCorrectAverage() {
        Double average = reflectionRepository.findAverageEnergyRatingByUser(testUser1);

        assertThat(average).isNotNull();
        assertThat(average).isEqualTo((8.0 + 6.0 + 3.0) / 3);
    }

    @Test
    void findAverageEnergyRatingByUserAndDateBetween_ShouldReturnCorrectAverage() {
        Double average = reflectionRepository.findAverageEnergyRatingByUserAndDateBetween(testUser1, yesterday, today);

        assertThat(average).isNotNull();
        assertThat(average).isEqualTo((8.0 + 6.0) / 2);
    }

    @Test
    void findDistinctDatesByUserOrderByDateDesc_ShouldReturnDistinctDates() {
        List<LocalDate> dates = reflectionRepository.findDistinctDatesByUserOrderByDateDesc(testUser1);

        assertThat(dates).hasSize(3);
        assertThat(dates).containsExactly(today, yesterday, twoDaysAgo);
    }

    @Test
    void existsByUserAndDate_ShouldReturnTrue_WhenExists() {
        boolean exists = reflectionRepository.existsByUserAndDate(testUser1, today);

        assertThat(exists).isTrue();
    }

    @Test
    void existsByUserAndDate_ShouldReturnFalse_WhenNotExists() {
        LocalDate futureDate = today.plusDays(1);
        boolean exists = reflectionRepository.existsByUserAndDate(testUser1, futureDate);

        assertThat(exists).isFalse();
    }

    @Test
    void findByUserWithReflectionTextOrderByDateDesc_ShouldReturnReflectionsWithText() {
        List<Reflection> reflections = reflectionRepository.findByUserWithReflectionTextOrderByDateDesc(testUser1);

        assertThat(reflections).hasSize(3);
        assertThat(reflections).allMatch(r -> r.getReflectionText() != null && !r.getReflectionText().trim().isEmpty());
    }
} 