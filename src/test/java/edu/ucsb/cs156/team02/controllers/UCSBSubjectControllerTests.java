package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.repositories.UserRepository;
import edu.ucsb.cs156.team02.testconfig.TestConfig;
import edu.ucsb.cs156.team02.ControllerTestCase;
import edu.ucsb.cs156.team02.entities.Todo;
import edu.ucsb.cs156.team02.entities.UCSBSubject;
import edu.ucsb.cs156.team02.entities.User;
import edu.ucsb.cs156.team02.repositories.TodoRepository;
import edu.ucsb.cs156.team02.repositories.UCSBSubjectRepository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UCSBSubjectController.class)
@Import(TestConfig.class)
public class UCSBSubjectControllerTests extends ControllerTestCase {

    @MockBean
    UCSBSubjectRepository subjectRepository;

    // Authorization tests for /api/todos/admin/all

    @Test
    public void ucsb_subject_get_logged_out() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().isOk());
    }
    

    @WithMockUser(roles = { "USER" })
    @Test
    public void ucsb_subject_get_user() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().isOk());
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void ucsb_subject_get_admin() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().isOk());
    }


    @Test
    public void ucsb_subject_post_logged_out() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/post?subjectCode=69420&subjectTranslation=mike oxlong&deptCode=69&collegeCode=420&inactive=false"))
                .andExpect(status().isOk());
    }



    @WithMockUser(roles = { "USER" })
    @Test
    public void ucsb_subject_post_user() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/post?subjectCode=69420&subjectTranslation=mike oxlong&deptCode=69&collegeCode=420&inactive=false"))
                .andExpect(status().isOk());
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void ucsb_subject_post_admin() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/post?subjectCode=69420&subjectTranslation=mike oxlong&deptCode=69&collegeCode=420&inactive=false"))
                .andExpect(status().isOk());
    }


}
