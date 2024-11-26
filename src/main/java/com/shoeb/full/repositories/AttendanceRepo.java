package com.shoeb.full.repositories;

import com.shoeb.full.entities.Attendance;
import com.shoeb.full.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepo extends JpaRepository<Attendance, Long> {


    @Query("SELECT a FROM Attendance a WHERE a.user.userId = :userId AND a.present = true")
    List<Attendance> findByUser(@Param("userId") Long userId);

    //    @Query("SELECT a FROM Attendance a WHERE a.user.userId = :userId AND a.date BETWEEN :startDate AND :endDate AND a.present = true")
    @Query("SELECT a FROM Attendance a WHERE a.user.userId = :userId AND STR_TO_DATE(a.date, '%d-%m-%Y') BETWEEN STR_TO_DATE(:startDate, '%d-%m-%Y') AND STR_TO_DATE(:endDate, '%d-%m-%Y') AND a.present = true")
    List<Attendance> findByUserAndDateBetween(@Param("userId") Long userId, @Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query("SELECT COUNT(*) FROM Attendance a WHERE a.user.userId = :userId AND a.present = true")
    Long countPresentByUser(@Param("userId") Long userId);
//
//    @Query("SELECT COUNT(*) FROM Attendance a WHERE a.user.userId = :userId AND a.date BETWEEN :startDate AND :endDate AND a.present = true")
//    Long countPresentBetweenDates(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

//@Query("SELECT COUNT(a) FROM Attendance a WHERE a.user.userId = :userId AND a.date >= :startDate AND a.date <= :endDate")
@Query("SELECT COUNT(*) FROM Attendance a WHERE a.user.userId = :userId AND a.present = true AND STR_TO_DATE(a.date, '%d-%m-%Y') BETWEEN STR_TO_DATE(:startDate, '%d-%m-%Y') AND STR_TO_DATE(:endDate, '%d-%m-%Y')")
    Long countPresentByUserAndBetweenDates(@Param("userId") Long userId, @Param("startDate") String startDate, @Param("endDate") String endDate);
}