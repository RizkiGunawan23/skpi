package com.skpijtk.springboot_boilerplate.dto.response.admin.studentmanagement;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteStudentResponse {
    private int student_id;
    private String student_name;
    private String nim;
}
