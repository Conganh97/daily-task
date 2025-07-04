package com.dailytask.repository;

import com.dailytask.entity.EnergyAssessment;
import com.dailytask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnergyAssessmentRepository extends JpaRepository<EnergyAssessment, Long> {

    Optional<EnergyAssessment> findByUserAndDate(User user, LocalDate date);

    List<EnergyAssessment> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<EnergyAssessment> findByUserOrderByDateDesc(User user);

    @Query("SELECT ea FROM EnergyAssessment ea WHERE ea.user = :user AND ea.date BETWEEN :startDate AND :endDate ORDER BY ea.date DESC")
    List<EnergyAssessment> findByUserAndDateBetweenOrderByDateDesc(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT ea FROM EnergyAssessment ea WHERE ea.user = :user AND ea.energyLevel >= :minLevel ORDER BY ea.date DESC")
    List<EnergyAssessment> findByUserAndEnergyLevelGreaterThanEqualOrderByDateDesc(@Param("user") User user, @Param("minLevel") Integer minLevel);

    @Query("SELECT ea FROM EnergyAssessment ea WHERE ea.user = :user AND ea.energyLevel <= :maxLevel ORDER BY ea.date DESC")
    List<EnergyAssessment> findByUserAndEnergyLevelLessThanEqualOrderByDateDesc(@Param("user") User user, @Param("maxLevel") Integer maxLevel);

    @Query("SELECT ea FROM EnergyAssessment ea WHERE ea.user = :user AND ea.energyLevel BETWEEN :minLevel AND :maxLevel ORDER BY ea.date DESC")
    List<EnergyAssessment> findByUserAndEnergyLevelBetweenOrderByDateDesc(@Param("user") User user, @Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel);

    @Query("SELECT COUNT(ea) FROM EnergyAssessment ea WHERE ea.user = :user")
    long countByUser(@Param("user") User user);

    @Query("SELECT COUNT(ea) FROM EnergyAssessment ea WHERE ea.user = :user AND ea.date BETWEEN :startDate AND :endDate")
    long countByUserAndDateBetween(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT AVG(ea.energyLevel) FROM EnergyAssessment ea WHERE ea.user = :user")
    Double findAverageEnergyLevelByUser(@Param("user") User user);

    @Query("SELECT AVG(ea.energyLevel) FROM EnergyAssessment ea WHERE ea.user = :user AND ea.date BETWEEN :startDate AND :endDate")
    Double findAverageEnergyLevelByUserAndDateBetween(@Param("user") User user, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT DISTINCT ea.date FROM EnergyAssessment ea WHERE ea.user = :user ORDER BY ea.date DESC")
    List<LocalDate> findDistinctDatesByUserOrderByDateDesc(@Param("user") User user);

    boolean existsByUserAndDate(User user, LocalDate date);

    @Query("SELECT ea FROM EnergyAssessment ea WHERE ea.user = :user AND ea.date >= :date ORDER BY ea.date ASC LIMIT 1")
    Optional<EnergyAssessment> findFirstByUserAndDateAfterOrderByDateAsc(@Param("user") User user, @Param("date") LocalDate date);

    @Query("SELECT ea FROM EnergyAssessment ea WHERE ea.user = :user AND ea.date <= :date ORDER BY ea.date DESC LIMIT 1")
    Optional<EnergyAssessment> findFirstByUserAndDateBeforeOrderByDateDesc(@Param("user") User user, @Param("date") LocalDate date);
} 