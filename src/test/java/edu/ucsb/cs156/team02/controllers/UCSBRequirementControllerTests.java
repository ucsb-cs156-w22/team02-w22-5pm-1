package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.repositories.UserRepository;
import edu.ucsb.cs156.team02.testconfig.TestConfig;
import io.swagger.models.auth.In;
import edu.ucsb.cs156.team02.ControllerTestCase;
import edu.ucsb.cs156.team02.entities.UCSBRequirement;
import edu.ucsb.cs156.team02.entities.User;
import edu.ucsb.cs156.team02.repositories.UCSBRequirementRepository;

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
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UCSBRequirementController.class)
@Import(TestConfig.class)
public class UCSBRequirementControllerTests extends ControllerTestCase{
    @MockBean
    UCSBRequirementRepository ucsbRequirementRepository;

    @MockBean
    UserRepository userRepository;

    private static final String REQ_CODE = "reqcode";
    private static final String REQ_TRANSLATION= "reqtranslation";
    private static final String COLLEGE_CODE= "collegecode";
    private static final String OBJ_CODE= "objCode";
    private static final Integer COURSE_COUNT= 4;
    private static final Integer UNITS = 8;
    private static final Boolean INACTIVE = true;
    private static final Long ID = 0L;

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_ucsbrequirements_post__user_logged_in() throws Exception {
        
        // arrange
        UCSBRequirement expectedUcsbRequirement = UCSBRequirement.builder()
            .requirementCode(REQ_CODE)
            .requirementTranslation(REQ_TRANSLATION)
            .collegeCode(COLLEGE_CODE)
            .objCode(OBJ_CODE)
            .courseCount(COURSE_COUNT)
            .units(UNITS)
            .inactive(INACTIVE)
            .id(ID)
            .build();

        when(ucsbRequirementRepository.save(eq(expectedUcsbRequirement))).thenReturn(expectedUcsbRequirement);

        String post_url = String.format("/api/UCSBRequirements/post?requirementCode=%s&requirementTranslation=%s&collegeCode=%s&objCode=%s&courseCount=%d&units=%d&inactive=%s", REQ_CODE, REQ_TRANSLATION, COLLEGE_CODE, OBJ_CODE, COURSE_COUNT, UNITS, INACTIVE.toString());

        // act
        MvcResult response = mockMvc.perform(
                post(post_url)
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(ucsbRequirementRepository, times(1)).save(expectedUcsbRequirement);
        String expectedJson = mapper.writeValueAsString(expectedUcsbRequirement);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @Test
    public void api_ucsbrequirements_post_not_logged_in() throws Exception {
        
        // arrange
        UCSBRequirement expectedUcsbRequirement = UCSBRequirement.builder()
            .requirementCode(REQ_CODE)
            .requirementTranslation(REQ_TRANSLATION)
            .collegeCode(COLLEGE_CODE)
            .objCode(OBJ_CODE)
            .courseCount(COURSE_COUNT)
            .units(UNITS)
            .inactive(INACTIVE)
            .id(ID)
            .build();

        String post_url = String.format("/api/UCSBRequirements/post?requirementCode=%s&requirementTranslation=%s&collegeCode=%s&objCode=%s&courseCount=%d&units=%d&inactive=%s", REQ_CODE, REQ_TRANSLATION, COLLEGE_CODE, OBJ_CODE, COURSE_COUNT, UNITS, INACTIVE.toString());

        // act
        MvcResult response = mockMvc.perform(
                post(post_url)
                        .with(csrf()))
                .andExpect(status().is(403)).andReturn();
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_all_ucsb_requirements_get_as_user() throws Exception {

        UCSBRequirement expectedUcsbRequirement = UCSBRequirement.builder()
            .requirementCode(REQ_CODE)
            .requirementTranslation(REQ_TRANSLATION)
            .collegeCode(COLLEGE_CODE)
            .objCode(OBJ_CODE)
            .courseCount(COURSE_COUNT)
            .units(UNITS)
            .inactive(INACTIVE)
            .id(ID)
            .build();

        when(ucsbRequirementRepository.findAll()).thenReturn(List.of(expectedUcsbRequirement));

        MvcResult response = mockMvc.perform(get("/api/UCSBRequirements/all"))
                .andExpect(status().is(200))
                .andReturn();

        verify(ucsbRequirementRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(List.of(expectedUcsbRequirement));
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_all_ucsb_requirements_get_as_admin() throws Exception {

         UCSBRequirement expectedUcsbRequirement = UCSBRequirement.builder()
            .requirementCode(REQ_CODE)
            .requirementTranslation(REQ_TRANSLATION)
            .collegeCode(COLLEGE_CODE)
            .objCode(OBJ_CODE)
            .courseCount(COURSE_COUNT)
            .units(UNITS)
            .inactive(INACTIVE)
            .id(ID)
            .build();

        when(ucsbRequirementRepository.findAll()).thenReturn(List.of(expectedUcsbRequirement));

        MvcResult response = mockMvc.perform(get("/api/UCSBRequirements/all"))
                .andExpect(status().is(200))
                .andReturn();

        verify(ucsbRequirementRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(List.of(expectedUcsbRequirement));
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @Test
    public void api_all_ucsb_requirements_get_as_not_logged_in() throws Exception {

         UCSBRequirement expectedUcsbRequirement = UCSBRequirement.builder()
            .requirementCode(REQ_CODE)
            .requirementTranslation(REQ_TRANSLATION)
            .collegeCode(COLLEGE_CODE)
            .objCode(OBJ_CODE)
            .courseCount(COURSE_COUNT)
            .units(UNITS)
            .inactive(INACTIVE)
            .id(ID)
            .build();

        MvcResult response = mockMvc.perform(get("/api/UCSBRequirements/all"))
                .andExpect(status().is(403))
                .andReturn();
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_get_ucsb_requirements_with_id() throws Exception {

        UCSBRequirement expectedUcsbRequirement = UCSBRequirement.builder()
            .requirementCode(REQ_CODE)
            .requirementTranslation(REQ_TRANSLATION)
            .collegeCode(COLLEGE_CODE)
            .objCode(OBJ_CODE)
            .courseCount(COURSE_COUNT)
            .units(UNITS)
            .inactive(INACTIVE)
            .id(ID)
            .build();

        when(ucsbRequirementRepository.findById(eq(ID))).thenReturn(Optional.of(expectedUcsbRequirement));

        String url = String.format("/api/UCSBRequirements?id=%s", ID);
        MvcResult response = mockMvc.perform(get(url))
                .andExpect(status().is(200))
                .andReturn();

        verify(ucsbRequirementRepository, times(1)).findById(eq(ID));
        String expectedJson = mapper.writeValueAsString(expectedUcsbRequirement);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_get_ucsb_requirements_with_nonexistent_id() throws Exception {

        Long nonexistent_id = 100L;

        UCSBRequirement expectedUcsbRequirement = UCSBRequirement.builder()
            .requirementCode(REQ_CODE)
            .requirementTranslation(REQ_TRANSLATION)
            .collegeCode(COLLEGE_CODE)
            .objCode(OBJ_CODE)
            .courseCount(COURSE_COUNT)
            .units(UNITS)
            .inactive(INACTIVE)
            .id(ID)
            .build();

        when(ucsbRequirementRepository.findById(eq(nonexistent_id))).thenReturn(Optional.empty());

        String url = String.format("/api/UCSBRequirements?id=%s", nonexistent_id);
        MvcResult response = mockMvc.perform(get(url))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(ucsbRequirementRepository, times(1)).findById(eq(nonexistent_id));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("id 100 not found", responseString);
    }


    /**************DELETE TESTS 
     * 
     * 
    */
    @WithMockUser(roles = { "USER", "ADMIN" })
    @Test
    public void api_ucsb_requirements__user_logged_in__delete_ucsb_requirement() throws Exception {

        UCSBRequirement ucsbRequirement = UCSBRequirement.builder()
        .requirementCode(REQ_CODE)
        .requirementTranslation(REQ_TRANSLATION)
        .collegeCode(COLLEGE_CODE)
        .objCode(OBJ_CODE)
        .courseCount(COURSE_COUNT)
        .units(UNITS)
        .inactive(INACTIVE)
        .id(15L)
        .build();
        when(ucsbRequirementRepository.findById(eq(15L))).thenReturn(Optional.of(ucsbRequirement));

        //act
        MvcResult response = mockMvc.perform(
                delete("/api/UCSBRequirements?id=15")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // // assert
        verify(ucsbRequirementRepository, times(1)).findById(15L);
        verify(ucsbRequirementRepository, times(1)).deleteById(15L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSB Requirement with id 15 deleted", responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void api_ucsb_requirements__user_logged_in__cannot_delete_ucsb_requirement_that_does_not_exist() throws Exception {

        UCSBRequirement ucsbRequirement = UCSBRequirement.builder()
        .requirementCode(REQ_CODE)
        .requirementTranslation(REQ_TRANSLATION)
        .collegeCode(COLLEGE_CODE)
        .objCode(OBJ_CODE)
        .courseCount(COURSE_COUNT)
        .units(UNITS)
        .inactive(INACTIVE)
        .id(15L)
        .build();
        
        when(ucsbRequirementRepository.findById(eq(15L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/UCSBRequirements?id=15")
                .with(csrf()))
                .andExpect(status().is(400))
                .andReturn();

        // assert
        // verify(ucsbRequirementRepository, times(1)).findById(15L);
        // verify(ucsbRequirementRepository, times(1)).deleteById(15L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("id 15 not found", responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_ucsb_requirements__admin_logged_in__delete_ucsb_requirement() throws Exception {

        UCSBRequirement ucsbRequirement = UCSBRequirement.builder()
        .requirementCode(REQ_CODE)
        .requirementTranslation(REQ_TRANSLATION)
        .collegeCode(COLLEGE_CODE)
        .objCode(OBJ_CODE)
        .courseCount(COURSE_COUNT)
        .units(UNITS)
        .inactive(INACTIVE)
        .id(15L)
        .build();
        when(ucsbRequirementRepository.findById(eq(15L))).thenReturn(Optional.of(ucsbRequirement));

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/UCSBRequirements?id=15")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(ucsbRequirementRepository, times(1)).findById(15L);
        verify(ucsbRequirementRepository, times(1)).deleteById(15L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSB Requirement with id 15 deleted", responseString);
    }

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void api_ucsb_requirements__admin_logged_in__cannot_delete_ucsb_requirement_that_does_not_exist() throws Exception {

        UCSBRequirement ucsbRequirement = UCSBRequirement.builder()
        .requirementCode(REQ_CODE)
        .requirementTranslation(REQ_TRANSLATION)
        .collegeCode(COLLEGE_CODE)
        .objCode(OBJ_CODE)
        .courseCount(COURSE_COUNT)
        .units(UNITS)
        .inactive(INACTIVE)
        .id(15L)
        .build();
        when(ucsbRequirementRepository.findById(eq(15L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/UCSBRequirements?id=15")
                        .with(csrf()))
                        .andExpect(status().is(400))
                        .andReturn();

        // assert
        // verify(ucsbRequirementRepository, times(1)).findById(15L);
        // verify(ucsbRequirementRepository, times(1)).deleteById(15L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("id 15 not found", responseString);
    }

    /*****
     * 
     * PUT TESTS
     * 
     */

    @WithMockUser(roles = { "ADMIN", "USER" })
    @Test
    public void api_ucsb_requirements__user_logged_in__put_ucsb_requirement() throws Exception {
        // arrange

        UCSBRequirement ucsbRequirement = UCSBRequirement.builder()
        .requirementCode(REQ_CODE)
        .requirementTranslation(REQ_TRANSLATION)
        .collegeCode(COLLEGE_CODE)
        .objCode(OBJ_CODE)
        .courseCount(COURSE_COUNT)
        .units(UNITS)
        .inactive(INACTIVE)
        .id(67L)
        .build();
        when(ucsbRequirementRepository.findById(eq(67L))).thenReturn(Optional.empty());
        // We deliberately set the user information to another user
        // This shoudl get ignored and overwritten with currrent user when todo is saved

        UCSBRequirement updatedUcsbRequirement = UCSBRequirement.builder()
        .requirementCode(REQ_CODE)
        .requirementTranslation(REQ_TRANSLATION)
        .collegeCode(COLLEGE_CODE)
        .objCode(OBJ_CODE)
        .courseCount(6)
        .units(UNITS)
        .inactive(INACTIVE)
        .id(67L)
        .build();
        when(ucsbRequirementRepository.findById(eq(67L))).thenReturn(Optional.empty());
        
        UCSBRequirement correctUcsbRequirement = UCSBRequirement.builder()
        .requirementCode(REQ_CODE)
        .requirementTranslation(REQ_TRANSLATION)
        .collegeCode(COLLEGE_CODE)
        .objCode(OBJ_CODE)
        .courseCount(6)
        .units(UNITS)
        .inactive(INACTIVE)
        .id(67L)
        .build();
        when(ucsbRequirementRepository.findById(eq(67L))).thenReturn(Optional.empty());

        String requestBody = mapper.writeValueAsString(updatedUcsbRequirement);
        String expectedReturn = mapper.writeValueAsString(correctUcsbRequirement);

        when(ucsbRequirementRepository.findById(eq(67L))).thenReturn(Optional.of(ucsbRequirement));

        // act
        MvcResult response = mockMvc.perform(
                put("/api/UCSBRequirements?id=67")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(ucsbRequirementRepository, times(1)).findById(67L);
        verify(ucsbRequirementRepository, times(1)).save(correctUcsbRequirement); // should be saved with correct user
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedReturn, responseString);
    }

    @WithMockUser(roles = {"ADMIN", "USER" })
    @Test
    public void api_ucsb_requirements__user_logged_in__cannot_put_ucsb_requirement_that_does_not_exist() throws Exception {
        // arrange

        UCSBRequirement updatedUcsbRequirement = UCSBRequirement.builder()
        .requirementCode(REQ_CODE)
        .requirementTranslation(REQ_TRANSLATION)
        .collegeCode(COLLEGE_CODE)
        .objCode(OBJ_CODE)
        .courseCount(COURSE_COUNT)
        .units(UNITS)
        .inactive(INACTIVE)
        .id(67L)
        .build();
        when(ucsbRequirementRepository.findById(eq(67L))).thenReturn(Optional.empty());
        
        String requestBody = mapper.writeValueAsString(updatedUcsbRequirement);

        when(ucsbRequirementRepository.findById(eq(67L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                put("/api/UCSBRequirements?id=67")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(ucsbRequirementRepository, times(1)).findById(67L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("id 67 not found", responseString);
    }
}


