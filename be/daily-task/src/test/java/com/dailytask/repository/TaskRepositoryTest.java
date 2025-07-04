package com.dailytask.repository;

import com.dailytask.entity.Task;
import com.dailytask.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
class TaskRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TaskRepository taskRepository;

    private User testUser1;
    private User testUser2;
    private LocalDate today;
    private LocalDate yesterday;
    private LocalDate tomorrow;

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
        tomorrow = today.plusDays(1);

        createTestTask("Task 1", "Description 1", testUser1, today, false, true);
        createTestTask("Task 2", "Description 2", testUser1, today, true, false);
        createTestTask("Task 3", "Description 3", testUser1, yesterday, false, true);
        createTestTask("Task 4", "Description 4", testUser2, today, true, true);
        createTestTask("Task 5", "Description 5", testUser1, tomorrow, false, false);
    }

    private void createTestTask(String title, String description, User user, LocalDate date, boolean completed, boolean starred) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setUser(user);
        task.setDate(date);
        task.setCompleted(completed);
        task.setStarred(starred);
        entityManager.persistAndFlush(task);
    }

    @Test
    void findByUserAndDate_ShouldReturnTasksForUserAndDate() {
        List<Task> tasks = taskRepository.findByUserAndDate(testUser1, today);

        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Task 1", "Task 2");
    }

    @Test
    void findByUserAndDateBetween_ShouldReturnTasksInDateRange() {
        List<Task> tasks = taskRepository.findByUserAndDateBetween(testUser1, yesterday, today);

        assertThat(tasks).hasSize(3);
        assertThat(tasks).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Task 1", "Task 2", "Task 3");
    }

    @Test
    void findByUserAndStarred_ShouldReturnStarredTasks() {
        List<Task> tasks = taskRepository.findByUserAndStarred(testUser1, true);

        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Task 1", "Task 3");
    }

    @Test
    void findByUserAndCompleted_ShouldReturnCompletedTasks() {
        List<Task> tasks = taskRepository.findByUserAndCompleted(testUser1, true);

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Task 2");
    }

    @Test
    void findByUserAndDateAndCompleted_ShouldReturnCompletedTasksForDate() {
        List<Task> tasks = taskRepository.findByUserAndDateAndCompleted(testUser1, today, true);

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Task 2");
    }

    @Test
    void findByUserAndDateAndStarred_ShouldReturnStarredTasksForDate() {
        List<Task> tasks = taskRepository.findByUserAndDateAndStarred(testUser1, today, true);

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Task 1");
    }

    @Test
    void findByUserAndDateOrderByStarredDescCreatedAtAsc_ShouldReturnOrderedTasks() {
        List<Task> tasks = taskRepository.findByUserAndDateOrderByStarredDescCreatedAtAsc(testUser1, today);

        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getStarred()).isTrue();
        assertThat(tasks.get(1).getStarred()).isFalse();
    }

    @Test
    void findStarredTasksByUserOrderByDateDesc_ShouldReturnStarredTasksOrderedByDate() {
        List<Task> tasks = taskRepository.findStarredTasksByUserOrderByDateDesc(testUser1);

        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Task::getTitle)
                .containsExactly("Task 1", "Task 3");
    }

    @Test
    void findIncompleteTasksByUserOrderByDateAscStarredDesc_ShouldReturnIncompleteTasksOrdered() {
        List<Task> tasks = taskRepository.findIncompleteTasksByUserOrderByDateAscStarredDesc(testUser1);

        assertThat(tasks).hasSize(3);
        assertThat(tasks).extracting(Task::getCompleted)
                .containsOnly(false);
    }

    @Test
    void countByUserAndDate_ShouldReturnCorrectCount() {
        long count = taskRepository.countByUserAndDate(testUser1, today);

        assertThat(count).isEqualTo(2);
    }

    @Test
    void countCompletedByUserAndDate_ShouldReturnCorrectCount() {
        long count = taskRepository.countCompletedByUserAndDate(testUser1, today);

        assertThat(count).isEqualTo(1);
    }

    @Test
    void countStarredByUser_ShouldReturnCorrectCount() {
        long count = taskRepository.countStarredByUser(testUser1);

        assertThat(count).isEqualTo(2);
    }

    @Test
    void findByUserOrderByDateDescCreatedAtDesc_ShouldReturnAllUserTasksOrdered() {
        List<Task> tasks = taskRepository.findByUserOrderByDateDescCreatedAtDesc(testUser1);

        assertThat(tasks).hasSize(4);
        assertThat(tasks.get(0).getDate()).isEqualTo(tomorrow);
    }

    @Test
    void findDistinctDatesByUserOrderByDateDesc_ShouldReturnDistinctDates() {
        List<LocalDate> dates = taskRepository.findDistinctDatesByUserOrderByDateDesc(testUser1);

        assertThat(dates).hasSize(3);
        assertThat(dates).containsExactly(tomorrow, today, yesterday);
    }
} 