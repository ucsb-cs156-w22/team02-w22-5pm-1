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

    @MockBean
    UserRepository userRepository;

    // Authorization tests for /api/todos/admin/all
    

    @WithMockUser(roles = { "USER" })
    @Test
    public void ucsb_subject_get_user() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void ucsb_subject_get_admin() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void ucsb_subject_get_logged_out() throws Exception {
        mockMvc.perform(get("/api/UCSBSubjects/all"))
                .andExpect(status().is(403));
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void ucsb_subject_get_id_does_not_exist() throws Exception {
        when(subjectRepository.findById(eq(29L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects?id=29"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert

        verify(subjectRepository, times(1)).findById(eq(29L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("Subject with id 29 not found", responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void ucsb_subject_get_id_exists() throws Exception {
        // arrange

        UCSBSubject subject = UCSBSubject.builder()
        .subjectCode("69420")
        .subjectTranslation("mikeoxlong")
        .deptCode("69")
        .collegeCode("420")
        .relatedDeptCode("bruh")
        .inactive(false)
        .build();
        when(subjectRepository.findById(eq(7L))).thenReturn(Optional.of(subject));

        // act
        MvcResult response = mockMvc.perform(get("/api/UCSBSubjects?id=7"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(subjectRepository, times(1)).findById(eq(7L));
        String expectedJson = mapper.writeValueAsString(subject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @Test
    public void ucsb_subject_post_logged_out() throws Exception {
        mockMvc.perform(
            post("/api/UCSBSubjects/post?relatedDeptCode=bruh&subjectCode=69420&subjectTranslation=mikeoxlong&deptCode=69&collegeCode=420&inactive=true")
            .with(csrf()))        
        .andExpect(status().is(403)).andReturn();
    }



    @WithMockUser(roles = { "USER" })
    @Test
    public void ucsb_subject_post_user() throws Exception {
        UCSBSubject expectedSubject = UCSBSubject.builder()
                .subjectCode("69420")
                .subjectTranslation("mikeoxlong")
                .deptCode("69")
                .collegeCode("420")
                .relatedDeptCode("bruh")
                .inactive(true)
                .build();

        when(subjectRepository.save(eq(expectedSubject))).thenReturn(expectedSubject);
        
        MvcResult response = mockMvc.perform(
            post("/api/UCSBSubjects/post?relatedDeptCode=bruh&subjectCode=69420&subjectTranslation=mikeoxlong&deptCode=69&collegeCode=420&inactive=true")
            .with(csrf()))        
        .andExpect(status().isOk()).andReturn();

        verify(subjectRepository, times(1)).save(expectedSubject);
        String expectedJson = mapper.writeValueAsString(expectedSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void ucsb_subject_post_admin() throws Exception {
        UCSBSubject expectedSubject = UCSBSubject.builder()
                .subjectCode("69420")
                .subjectTranslation("mikeoxlong")
                .deptCode("69")
                .collegeCode("420")
                .relatedDeptCode("bruh")
                .inactive(true)
                .build();

        when(subjectRepository.save(eq(expectedSubject))).thenReturn(expectedSubject);
        
        MvcResult response = mockMvc.perform(
            post("/api/UCSBSubjects/post?relatedDeptCode=bruh&subjectCode=69420&subjectTranslation=mikeoxlong&deptCode=69&collegeCode=420&inactive=true")
            .with(csrf()))        
        .andExpect(status().isOk()).andReturn();

        verify(subjectRepository, times(1)).save(expectedSubject);
        String expectedJson = mapper.writeValueAsString(expectedSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void ucsb_subject_get_subject_with_id_user() throws Exception {
        UCSBSubject expectedSubject = UCSBSubject.builder()
                .subjectCode("69420")
                .subjectTranslation("mikeoxlong")
                .deptCode("69")
                .collegeCode("420")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();

        when(subjectRepository.findById(eq(Long.valueOf(420)))).thenReturn(Optional.of(expectedSubject));
        
        MvcResult response = mockMvc.perform(
            get("/api/UCSBSubjects/?id=420")  )     
        .andExpect(status().isOk()).andReturn();

        verify(subjectRepository, times(1)).findById(eq(Long.valueOf(420)));
        String expectedJson = mapper.writeValueAsString(expectedSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }


    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void ucsb_subject_get_subject_with_id_admin() throws Exception {
        UCSBSubject expectedSubject = UCSBSubject.builder()
                .subjectCode("69420")
                .subjectTranslation("mikeoxlong")
                .deptCode("69")
                .collegeCode("420")
                .relatedDeptCode("bruh")
                .inactive(true)
                .id(Long.valueOf(420))
                .build();

        when(subjectRepository.findById(eq(Long.valueOf(420)))).thenReturn(Optional.of(expectedSubject));
        
        MvcResult response = mockMvc.perform(
            get("/api/UCSBSubjects/?id=420"))        
        .andExpect(status().isOk()).andReturn();

        verify(subjectRepository, times(1)).findById(eq(Long.valueOf(420)));
        String expectedJson = mapper.writeValueAsString(expectedSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }


    @Test
    public void ucsb_subject_get_subject_with_id_not_logged_in() throws Exception {
        mockMvc.perform(
            get("/api/UCSBSubjects/?id=420"))        
        .andExpect(status().is(403)).andReturn();
    }
}
