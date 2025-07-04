package com.dailytask.repository;

import com.dailytask.entity.Task;
import com.dailytask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserAndDate(User user, LocalDate date);

    List<Task> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<Task> findByUserAndStarred(User user, boolean starred);

    List<Task> findByUserAndCompleted(User user, boolean completed);

    List<Task> findByUserAndDateAndCompleted(User user, LocalDate date, boolean completed);

    List<Task> findByUserAndDateAndStarred(User user, LocalDate date, boolean starred);

    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.date = :date ORDER BY t.starred DESC, t.createdAt ASC")
    List<Task> findByUserAndDateOrderByStarredDescCreatedAtAsc(@Param("user") User user, @Param("date") LocalDate date);

    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.date BETWEEN :startDate AND :endDate ORDER BY t.date DESC, t.starred DESC")
    List<Task> findByUserAndDateBetweenOrderByDateDescStarredDesc(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.starred = true ORDER BY t.date DESC")
    List<Task> findStarredTasksByUserOrderByDateDesc(@Param("user") User user);

    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.completed = false ORDER BY t.date ASC, t.starred DESC")
    List<Task> findIncompleteTasksByUserOrderByDateAscStarredDesc(@Param("user") User user);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.user = :user AND t.date = :date")
    long countByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.user = :user AND t.date = :date AND t.completed = true")
    long countCompletedByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.user = :user AND t.date BETWEEN :startDate AND :endDate")
    long countByUserAndDateBetween(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.user = :user AND t.starred = true")
    long countStarredByUser(@Param("user") User user);

    List<Task> findByUserOrderByDateDescCreatedAtDesc(User user);

    @Query("SELECT DISTINCT t.date FROM Task t WHERE t.user = :user ORDER BY t.date DESC")
    List<LocalDate> findDistinctDatesByUserOrderByDateDesc(@Param("user") User user);
} 