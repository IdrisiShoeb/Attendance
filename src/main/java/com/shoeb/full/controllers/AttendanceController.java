package com.shoeb.full.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoeb.full.entities.Attendance;
import com.shoeb.full.entities.User;
import com.shoeb.full.forms.AttendanceForm;
import com.shoeb.full.helpers.Helper;
import com.shoeb.full.repositories.AttendanceRepo;
import com.shoeb.full.repositories.UserRepo;
import com.shoeb.full.services.AttendanceService;
import com.shoeb.full.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AttendanceController {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AttendanceRepo attendanceRepo;

    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);
    @Autowired
    private UserService userService;

    @Autowired
    private AttendanceService attendanceService;

    public AttendanceController(UserRepo userRepo, AttendanceRepo attendanceRepo) {
        this.userRepo = userRepo;
        this.attendanceRepo = attendanceRepo;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/markAttendance")
    public String attendance(Model model){
        List<User> student = userService.getAllUsers();
        model.addAttribute("student", student);
        model.addAttribute("currentDate", LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        System.out.println("Mark Attendance");
        return "user/mark_attendance";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/mark-attendance")
    public String markAttendance(@RequestParam String date,
                               @RequestParam String status, @RequestParam Long userId,
                               Model model){

        boolean present;
        if (status.equals("PRESENT")) {
            present = true;  // Set present to true if status is "PRESENT"
        } else {
            present = false; // Set present to false otherwise
        }

        attendanceService.markStudentAttendance(userId,date,present);

        logger.info("Attendance: {}" , date);
        logger.info("Attendance: {}" , status );
        logger.info("Attendance: {}" , userId );
        return "redirect:/user/markAttendance";

    }


    @GetMapping("/api/count")
    public String totalpresent(Model model){
        Long presentDays = attendanceService.getTotalPresentByUserId();
        System.out.println(presentDays);
//        model.addAttribute("Count" , presentDays);
        return "tes";
    }

    @GetMapping("/attendanceForm")
    public String showAttendanceForm(Model model) {

        model.addAttribute("attendanceForm", new AttendanceForm());
        return "attendanceForm"; // Thymeleaf template name
    }

//    Now I want to List Students using thymeleaf object and also show student attendance using the attendanceRepository.countPresentByUserIdAndBetweenDates(userId, startDate, endDate) and show it in list of students

    @PostMapping("/countAttendance")
    public String countAttendance(@ModelAttribute AttendanceForm attendanceForm, Model model) {
        User user = userRepo.findByUserId(Long.valueOf(1));

        String startDate = attendanceForm.getStartDate().toString(); // Convert LocalDate to String
        String endDate = attendanceForm.getEndDate().toString(); // Convert LocalDate to String

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedStartDate = attendanceForm.getStartDate().format(formatter);
        String formattedEndDate = attendanceForm.getEndDate().format(formatter);

        model.addAttribute("formattedStartDate", formattedStartDate);
        model.addAttribute("formattedEndDate", formattedEndDate);


        logger.info("Start: {}", startDate);
        logger.info("End: {}", endDate);
        logger.info("Start 1: {}", formattedStartDate);
        logger.info("End 1: {}", formattedEndDate);

        Long count = attendanceRepo.countPresentByUserAndBetweenDates(Long.valueOf(1), formattedStartDate, formattedEndDate);

        model.addAttribute("count", count);
        return "tes"; // Redirect to a result page
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/studentAttendance")
    public String studentsAttendanceCount(Model model, @ModelAttribute AttendanceForm attendanceForm) {

        List<User> students = new ArrayList<>();
        Map<Long, List<String>> studentAttendanceDates = new HashMap<>();



        DecimalFormat df = new DecimalFormat("#.00");
        if (attendanceForm.getStartDate() != null && attendanceForm.getEndDate() != null) {
            students = userRepo.findAll();

            Long totalDays = ChronoUnit.DAYS.between(attendanceForm.getStartDate(), attendanceForm.getEndDate()) + 1; // +1 to include both start and end dates
            String startDate = attendanceForm.getStartDate().toString(); // Convert LocalDate to String
            String endDate = attendanceForm.getEndDate().toString(); // Convert LocalDate to String

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedStartDate = attendanceForm.getStartDate().format(formatter);
            String formattedEndDate = attendanceForm.getEndDate().format(formatter);



            for (User  student : students) {
                List<Attendance> attendanceList = attendanceRepo.findByUserAndDateBetween(student.getUserId(), formattedStartDate, formattedEndDate);
                logger.info("AttendanceList: {}", attendanceList);
                student.setAttendanceList(attendanceList);
                model.addAttribute("attendances",attendanceList);

                Long attendanceCount = attendanceRepo.countPresentByUserAndBetweenDates(student.getUserId(), formattedStartDate, formattedEndDate);
                // Calculate attendance percentage
                double attendancePercentage = (totalDays > 0) ? ((double) attendanceCount / totalDays) * 100 : 0.0;
                student.setAttendanceCount(attendanceCount); // Assuming you have a method to set this
                student.setAttendancePercentage(Double.parseDouble(df.format(attendancePercentage)));

                // Collect attendance dates
//                List<String> presentDates = attendanceList.stream()
//                        .map(attendance -> attendance.getDate())
//                        .collect(Collectors.toList());
//                studentAttendanceDates.put(student.getUserId(), presentDates);
                List<String> formattedPresentDates = attendanceList.stream()
                        .map(attendance -> LocalDate.parse(attendance.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                        .map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .collect(Collectors.toList());
                studentAttendanceDates.put(student.getUserId(), formattedPresentDates);

            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonAttendanceDates;

        try {
            jsonAttendanceDates = objectMapper.writeValueAsString(studentAttendanceDates);
        } catch (JsonProcessingException e) {
            // Handle the exception (e.g., log it)
            jsonAttendanceDates = "{}"; // fallback to empty object
        }
        logger.info("std dates: {}" , studentAttendanceDates);
        logger.info("json std dates: {}" , jsonAttendanceDates);
        model.addAttribute("studentAttendanceDates", studentAttendanceDates);
        model.addAttribute("jsonAttendanceDates", jsonAttendanceDates);
        model.addAttribute("students", students);
        return "user/student_attendance_count";
    }

    @GetMapping("/student/attendance")
    public String studentAttendance(Model model, Authentication authentication) {

        User student = userRepo.findByEmail(Helper.getEmailOfLoggedInUser(authentication)).orElse(null);

        Map<Long, List<String>> studentAttendanceDates = new HashMap<>();



        DecimalFormat df = new DecimalFormat("#.00");
//        if (attendanceForm.getStartDate() != null && attendanceForm.getEndDate() != null) {

//            Long totalDays = ChronoUnit.DAYS.between(attendanceForm.getStartDate(), attendanceForm.getEndDate()) + 1; // +1 to include both start and end dates
//            String startDate = attendanceForm.getStartDate().toString(); // Convert LocalDate to String
//            String endDate = attendanceForm.getEndDate().toString(); // Convert LocalDate to String
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//            String formattedStartDate = attendanceForm.getStartDate().format(formatter);
//            String formattedEndDate = attendanceForm.getEndDate().format(formatter);


//            List<Attendance> attendanceList = attendanceRepo.findByUserAndDateBetween(student.getUserId(), formattedStartDate, formattedEndDate);
            List<Attendance> attendanceList = attendanceRepo.findByUser(student.getUserId());
                logger.info("AttendanceList: {}", attendanceList);
                student.setAttendanceList(attendanceList);
                model.addAttribute("attendances",attendanceList);

//                Long attendanceCount = attendanceRepo.countPresentByUserAndBetweenDates(student.getUserId(), formattedStartDate, formattedEndDate);
                // Calculate attendance percentage
//                double attendancePercentage = (totalDays > 0) ? ((double) attendanceCount / totalDays) * 100 : 0.0;
//                student.setAttendanceCount(attendanceCount); // Assuming you have a method to set this
//                student.setAttendancePercentage(Double.parseDouble(df.format(attendancePercentage)));

                List<String> formattedPresentDates = attendanceList.stream()
                        .map(attendance -> LocalDate.parse(attendance.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                        .map(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                        .collect(Collectors.toList());
                studentAttendanceDates.put(student.getUserId(), formattedPresentDates);
//        }
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonAttendanceDates;

        try {
            jsonAttendanceDates = objectMapper.writeValueAsString(studentAttendanceDates);
        } catch (JsonProcessingException e) {
            // Handle the exception (e.g., log it)
            jsonAttendanceDates = "{}"; // fallback to empty object
        }
        logger.info("std dates: {}" , studentAttendanceDates);

        logger.info("json std dates: {}" , jsonAttendanceDates);

        model.addAttribute("jsonAttendanceDates", jsonAttendanceDates);
        model.addAttribute("student", student);
        return "student/student_attendance";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/export")
    public void exportToExcel(@RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate, HttpServletResponse response) throws IOException {
        List<User> students = userRepo.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Students");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedStartDate = startDate.format(formatter);
        String formattedEndDate = endDate.format(formatter);
        Long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // Create header row
        Row headerRow = sheet.createRow(0);

        headerRow.createCell(0).setCellValue("RollNo");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Phone");
        headerRow.createCell(3).setCellValue("Attendance");
        headerRow.createCell(4).setCellValue("Percentage");

        // Fill data
        int rowNum = 1;
        for (User student : students) {

            Long attendanceCount = attendanceRepo.countPresentByUserAndBetweenDates(student.getUserId(), formattedStartDate, formattedEndDate);
            double attendancePercentage = (totalDays > 0) ? ((double) attendanceCount / totalDays) * 100 : 0.0;
            student.setAttendanceCount(attendanceCount);
            student.setAttendancePercentage(attendancePercentage);
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(student.getRollNo());
            row.createCell(1).setCellValue(student.getName());
            row.createCell(2).setCellValue(student.getPhoneNo());
            row.createCell(3).setCellValue(student.getAttendanceCount());
            row.createCell(4).setCellValue(student.getAttendancePercentage() + "%");
        }

        // Set the response headers
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition",  "attachment; filename=\"attendance_" + formattedStartDate + "_to_" + formattedEndDate + ".xlsx\"");

        // Write the workbook to the response output stream
        workbook.write(response.getOutputStream());
        workbook.close();

    }



}


