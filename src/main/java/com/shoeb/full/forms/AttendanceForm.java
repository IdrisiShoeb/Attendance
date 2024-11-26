package com.shoeb.full.forms;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AttendanceForm {

    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;

}
