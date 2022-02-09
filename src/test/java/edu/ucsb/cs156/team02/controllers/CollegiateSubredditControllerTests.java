package edu.ucsb.cs156.team02.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import edu.ucsb.cs156.team02.ControllerTestCase;
import edu.ucsb.cs156.team02.entities.CollegiateSubreddit;
import edu.ucsb.cs156.team02.entities.User;
import edu.ucsb.cs156.team02.repositories.CollegiateSubredditRepository;
import edu.ucsb.cs156.team02.repositories.UserRepository;
import edu.ucsb.cs156.team02.testconfig.TestConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CollegiateSubredditController.class)
@Import(TestConfig.class)
public class CollegiateSubredditControllerTests extends ControllerTestCase {
    
    @MockBean
    CollegiateSubredditRepository collegiateSubredditRepository;

    @MockBean
    UserRepository userRepository;

    @WithMockUser(roles = { "USER" })
    @Test
    public void getAllSubreddits() throws Exception {
        mockMvc.perform(get("/api/collegiateSubreddits/all")).andExpect(status().isOk());
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void postSubreddit() throws Exception {

        CollegiateSubreddit expectedSubreddit = CollegiateSubreddit.builder()
            .name("TestName")
            .location("TestLoc")
            .subreddit("TestSub")
            .id(0L)
            .build();

        when(collegiateSubredditRepository.save(eq(expectedSubreddit))).thenReturn(expectedSubreddit);

        MvcResult response = mockMvc.perform(
            post("/api/collegiateSubreddits/post?name=TestName&location=TestLoc&subreddit=TestSub")
                .with(csrf()))
                .andExpect(status().isOk()).andReturn();
        
        verify(collegiateSubredditRepository, times(1)).save(expectedSubreddit);
        String expectedJson = mapper.writeValueAsString(expectedSubreddit);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);

    }
}
