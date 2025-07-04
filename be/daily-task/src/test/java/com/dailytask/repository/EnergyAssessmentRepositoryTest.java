package com.dailytask.repository;

import com.dailytask.entity.EnergyAssessment;
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
class EnergyAssessmentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EnergyAssessmentRepository energyAssessmentRepository;

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

        createTestEnergyAssessment(testUser1, today, 4);
        createTestEnergyAssessment(testUser1, yesterday, 3);
        createTestEnergyAssessment(testUser1, twoDaysAgo, 2);
        createTestEnergyAssessment(testUser2, today, 5);
    }

    private void createTestEnergyAssessment(User user, LocalDate date, int energyLevel) {
        EnergyAssessment assessment = new EnergyAssessment();
        assessment.setUser(user);
        assessment.setDate(date);
        assessment.setEnergyLevel(energyLevel);
        entityManager.persistAndFlush(assessment);
    }

    @Test
    void findByUserAndDate_ShouldReturnAssessment_WhenExists() {
        Optional<EnergyAssessment> assessment = energyAssessmentRepository.findByUserAndDate(testUser1, today);

        assertThat(assessment).isPresent();
        assertThat(assessment.get().getEnergyLevel()).isEqualTo(4);
    }

    @Test
    void findByUserAndDate_ShouldReturnEmpty_WhenNotExists() {
        LocalDate futureDate = today.plusDays(1);
        Optional<EnergyAssessment> assessment = energyAssessmentRepository.findByUserAndDate(testUser1, futureDate);

        assertThat(assessment).isEmpty();
    }

    @Test
    void findByUserAndDateBetween_ShouldReturnAssessmentsInRange() {
        List<EnergyAssessment> assessments = energyAssessmentRepository.findByUserAndDateBetween(testUser1, twoDaysAgo, today);

        assertThat(assessments).hasSize(3);
        assertThat(assessments).extracting(EnergyAssessment::getEnergyLevel)
                .containsExactlyInAnyOrder(4, 3, 2);
    }

    @Test
    void findByUserOrderByDateDesc_ShouldReturnAllAssessmentsOrderedByDate() {
        List<EnergyAssessment> assessments = energyAssessmentRepository.findByUserOrderByDateDesc(testUser1);

        assertThat(assessments).hasSize(3);
        assertThat(assessments.get(0).getDate()).isEqualTo(today);
        assertThat(assessments.get(1).getDate()).isEqualTo(yesterday);
        assertThat(assessments.get(2).getDate()).isEqualTo(twoDaysAgo);
    }

    @Test
    void findByUserAndEnergyLevelGreaterThanEqualOrderByDateDesc_ShouldReturnFilteredAssessments() {
        List<EnergyAssessment> assessments = energyAssessmentRepository.findByUserAndEnergyLevelGreaterThanEqualOrderByDateDesc(testUser1, 3);

        assertThat(assessments).hasSize(2);
        assertThat(assessments).extracting(EnergyAssessment::getEnergyLevel)
                .containsExactly(4, 3);
    }

    @Test
    void findByUserAndEnergyLevelLessThanEqualOrderByDateDesc_ShouldReturnFilteredAssessments() {
        List<EnergyAssessment> assessments = energyAssessmentRepository.findByUserAndEnergyLevelLessThanEqualOrderByDateDesc(testUser1, 3);

        assertThat(assessments).hasSize(2);
        assertThat(assessments).extracting(EnergyAssessment::getEnergyLevel)
                .containsExactly(3, 2);
    }

    @Test
    void findByUserAndEnergyLevelBetweenOrderByDateDesc_ShouldReturnFilteredAssessments() {
        List<EnergyAssessment> assessments = energyAssessmentRepository.findByUserAndEnergyLevelBetweenOrderByDateDesc(testUser1, 2, 4);

        assertThat(assessments).hasSize(3);
        assertThat(assessments).extracting(EnergyAssessment::getEnergyLevel)
                .containsExactly(4, 3, 2);
    }

    @Test
    void countByUser_ShouldReturnCorrectCount() {
        long count = energyAssessmentRepository.countByUser(testUser1);

        assertThat(count).isEqualTo(3);
    }

    @Test
    void countByUserAndDateBetween_ShouldReturnCorrectCount() {
        long count = energyAssessmentRepository.countByUserAndDateBetween(testUser1, yesterday, today);

        assertThat(count).isEqualTo(2);
    }

    @Test
    void findAverageEnergyLevelByUser_ShouldReturnCorrectAverage() {
        Double average = energyAssessmentRepository.findAverageEnergyLevelByUser(testUser1);

        assertThat(average).isNotNull();
        assertThat(average).isEqualTo((4.0 + 3.0 + 2.0) / 3);
    }

    @Test
    void findAverageEnergyLevelByUserAndDateBetween_ShouldReturnCorrectAverage() {
        Double average = energyAssessmentRepository.findAverageEnergyLevelByUserAndDateBetween(testUser1, yesterday, today);

        assertThat(average).isNotNull();
        assertThat(average).isEqualTo((4.0 + 3.0) / 2);
    }

    @Test
    void findDistinctDatesByUserOrderByDateDesc_ShouldReturnDistinctDates() {
        List<LocalDate> dates = energyAssessmentRepository.findDistinctDatesByUserOrderByDateDesc(testUser1);

        assertThat(dates).hasSize(3);
        assertThat(dates).containsExactly(today, yesterday, twoDaysAgo);
    }

    @Test
    void existsByUserAndDate_ShouldReturnTrue_WhenExists() {
        boolean exists = energyAssessmentRepository.existsByUserAndDate(testUser1, today);

        assertThat(exists).isTrue();
    }

    @Test
    void existsByUserAndDate_ShouldReturnFalse_WhenNotExists() {
        LocalDate futureDate = today.plusDays(1);
        boolean exists = energyAssessmentRepository.existsByUserAndDate(testUser1, futureDate);

        assertThat(exists).isFalse();
    }

    @Test
    void findFirstByUserAndDateAfterOrderByDateAsc_ShouldReturnNextAssessment() {
        Optional<EnergyAssessment> assessment = energyAssessmentRepository.findFirstByUserAndDateAfterOrderByDateAsc(testUser1, twoDaysAgo);

        assertThat(assessment).isPresent();
        assertThat(assessment.get().getDate()).isEqualTo(yesterday);
    }

    @Test
    void findFirstByUserAndDateBeforeOrderByDateDesc_ShouldReturnPreviousAssessment() {
        Optional<EnergyAssessment> assessment = energyAssessmentRepository.findFirstByUserAndDateBeforeOrderByDateDesc(testUser1, today);

        assertThat(assessment).isPresent();
        assertThat(assessment.get().getDate()).isEqualTo(yesterday);
    }
} 