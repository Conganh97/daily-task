package com.dailytask.repository;

import com.dailytask.entity.Reflection;
import com.dailytask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReflectionRepository extends JpaRepository<Reflection, Long> {

    Optional<Reflection> findByUserAndDate(User user, LocalDate date);

    List<Reflection> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<Reflection> findByUserOrderByDateDesc(User user);

    @Query("SELECT r FROM Reflection r WHERE r.user = :user AND r.date BETWEEN :startDate AND :endDate ORDER BY r.date DESC")
    List<Reflection> findByUserAndDateBetweenOrderByDateDesc(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT r FROM Reflection r WHERE r.user = :user AND r.energyRating >= :minRating ORDER BY r.date DESC")
    List<Reflection> findByUserAndEnergyRatingGreaterThanEqualOrderByDateDesc(@Param("user") User user, @Param("minRating") Integer minRating);

    @Query("SELECT r FROM Reflection r WHERE r.user = :user AND r.energyRating <= :maxRating ORDER BY r.date DESC")
    List<Reflection> findByUserAndEnergyRatingLessThanEqualOrderByDateDesc(@Param("user") User user, @Param("maxRating") Integer maxRating);

    @Query("SELECT r FROM Reflection r WHERE r.user = :user AND r.energyRating BETWEEN :minRating AND :maxRating ORDER BY r.date DESC")
    List<Reflection> findByUserAndEnergyRatingBetweenOrderByDateDesc(@Param("user") User user, @Param("minRating") Integer minRating, @Param("maxRating") Integer maxRating);

    @Query("SELECT COUNT(r) FROM Reflection r WHERE r.user = :user")
    long countByUser(@Param("user") User user);

    @Query("SELECT COUNT(r) FROM Reflection r WHERE r.user = :user AND r.date BETWEEN :startDate AND :endDate")
    long countByUserAndDateBetween(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT AVG(r.energyRating) FROM Reflection r WHERE r.user = :user")
    Double findAverageEnergyRatingByUser(@Param("user") User user);

    @Query("SELECT AVG(r.energyRating) FROM Reflection r WHERE r.user = :user AND r.date BETWEEN :startDate AND :endDate")
    Double findAverageEnergyRatingByUserAndDateBetween(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT DISTINCT r.date FROM Reflection r WHERE r.user = :user ORDER BY r.date DESC")
    List<LocalDate> findDistinctDatesByUserOrderByDateDesc(@Param("user") User user);

    boolean existsByUserAndDate(User user, LocalDate date);

    @Query("SELECT r FROM Reflection r WHERE r.user = :user AND r.reflectionText IS NOT NULL AND LENGTH(TRIM(r.reflectionText)) > 0 ORDER BY r.date DESC")
    List<Reflection> findByUserWithReflectionTextOrderByDateDesc(@Param("user") User user);
} 