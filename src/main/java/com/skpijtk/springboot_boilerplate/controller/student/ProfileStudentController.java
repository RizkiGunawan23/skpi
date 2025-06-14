package com.skpijtk.springboot_boilerplate.controller.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skpijtk.springboot_boilerplate.constant.ResponseMessage;
import com.skpijtk.springboot_boilerplate.dto.request.student.profile.StudentProfileQuery;
import com.skpijtk.springboot_boilerplate.dto.response.StudentProfileApiResponse;
import com.skpijtk.springboot_boilerplate.dto.response.student.profile.StudentProfilePageResponse;
import com.skpijtk.springboot_boilerplate.dto.response.student.profile.StudentProfileResponse;
import com.skpijtk.springboot_boilerplate.service.student.ProfileStudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/api/v1/mahasiswa/")
public class ProfileStudentController {
    @Autowired
    private ProfileStudentService profileStudentService;

    @GetMapping("profile")
    public ResponseEntity<StudentProfileApiResponse<StudentProfileResponse>> getStudentProfile(
            @AuthenticationPrincipal UserDetails userDetails, @ModelAttribute StudentProfileQuery query) {
        String email = userDetails.getUsername();
        StudentProfilePageResponse responseData = profileStudentService.getStudentProfile(email, query);
        StudentProfileApiResponse<StudentProfileResponse> response = new StudentProfileApiResponse<StudentProfileResponse>(
                responseData.getData(),
                responseData.getTotalData(),
                responseData.getTotalPage(),
                responseData.getCurrentPage(),
                responseData.getPageSize(),
                ResponseMessage.DATA_DISPLAY_SUCCESS);
        return ResponseEntity.ok(response);
    }
}
