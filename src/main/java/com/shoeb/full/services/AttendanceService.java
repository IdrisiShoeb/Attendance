package com.shoeb.full.services;

import com.shoeb.full.entities.Attendance;
import com.shoeb.full.entities.User;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    void markStudentAttendance(Long userId, String date, boolean present);
    Long getTotalPresentByUserId();
//    List<Attendance> getAttendanceForStudent(Long userId);
}
