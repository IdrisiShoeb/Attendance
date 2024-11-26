package com.shoeb.full.services.impl;

import com.shoeb.full.entities.Attendance;
import com.shoeb.full.entities.User;
import com.shoeb.full.repositories.AttendanceRepo;
import com.shoeb.full.repositories.UserRepo;
import com.shoeb.full.services.AttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceServiceImpl.class);
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AttendanceRepo attendanceRepo;


    @Override
    public void markStudentAttendance(Long userId, String date, boolean present) {
        Attendance attendance = new Attendance();
        attendance.setUser(userRepo.findById(userId).orElse(null));
        attendance.setDate(date);
        attendance.setPresent(present);
        attendanceRepo.save(attendance);
    }

    @Override
    public Long getTotalPresentByUserId() {
        User user = userRepo.findByUserId(Long.valueOf(1));

//        String startDate = String.valueOf(LocalDate.of(2023, 11, 25));
//        String endDate = String.valueOf(LocalDate.of(2024, 11, 25));

        LocalDate startDate = LocalDate.of(2023, 11, 25);
        LocalDate endDate = LocalDate.of(2024, 11, 25);
        // Format the date as "yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedstartDate = startDate.format(formatter);
        String formattedendDate = endDate.format(formatter);

        LocalDate formatendDate = LocalDate.parse(formattedendDate,formatter);
        LocalDate formatstartDate = LocalDate.parse(formattedstartDate,formatter);


        logger.info("start: {}" ,startDate );
        logger.info("End: {}" ,endDate );
        System.out.println(startDate);
        System.out.println(endDate);




        System.out.println(formattedstartDate);
        System.out.println(formattedendDate);
        System.out.println(formatstartDate);
        System.out.println(formatendDate);


        Long presentDays = attendanceRepo.countPresentByUser(Long.valueOf(1));
//        Long presentBetween = attendanceRepo.countPresentByUserAndBetweenDates(Long.valueOf(1),formatstartDate,formatendDate);
//        System.out.println(presentBetween);
        logger.info("present: {}" , presentDays);
//        logger.info("Between Date: {}" , presentBetween);
        return presentDays;
    }

//    @Override
//    public List<Attendance> getAttendanceForStudent(Long userId) {
//        User user = userRepo.findByUserId(userId);
//        List<Attendance> attendanceList = attendanceRepo.findAllByUser(user);
//        return List.of();
//    }
}
