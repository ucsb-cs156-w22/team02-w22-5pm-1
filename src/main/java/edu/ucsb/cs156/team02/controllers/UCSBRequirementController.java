package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.entities.UCSBRequirement;
import edu.ucsb.cs156.team02.entities.User;
import edu.ucsb.cs156.team02.models.CurrentUser;
import edu.ucsb.cs156.team02.repositories.UCSBRequirementRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Api(description = "API for UCSBRequirements")
@RequestMapping("/api/UCSBRequirements")
@RestController
@Slf4j
public class UCSBRequirementController extends ApiController{
    
    public class UCSBRequirementOrError {
        Long id;
        UCSBRequirement ucsbRequirement;
        ResponseEntity<String> error;

        public UCSBRequirementOrError(Long id) {
            this.id = id;
        }
    }

    @Autowired
    UCSBRequirementRepository ucsbRequirementRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "List all UCSBRequirements")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<UCSBRequirement> allUcsbRequirements() {
        loggingService.logMethod();
        Iterable<UCSBRequirement> requirements = ucsbRequirementRepository.findAll();
        return requirements;
    }

    @ApiOperation(value = "Create a new UCSBRequirement")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/post")
    public UCSBRequirement postUcsbRequirement(
            @ApiParam("requirement_code") @RequestParam String requirementCode,
            @ApiParam("requirement_translation") @RequestParam String requirementTranslation,
            @ApiParam("college_code") @RequestParam String collegeCode,
            @ApiParam("obj_code") @RequestParam String objCode,
            @ApiParam("course_count") @RequestParam int courseCount,
            @ApiParam("units") @RequestParam int units,
            @ApiParam("inactive") @RequestParam boolean inactive) {
        loggingService.logMethod();

        UCSBRequirement ucsbRequirement = new UCSBRequirement();
        ucsbRequirement.setRequirementCode(requirementCode);
        ucsbRequirement.setRequirementTranslation(requirementTranslation);
        ucsbRequirement.setCollegeCode(collegeCode);
        ucsbRequirement.setObjCode(objCode);
        ucsbRequirement.setCourseCount(courseCount);
        ucsbRequirement.setUnits(units);
        ucsbRequirement.setInactive(inactive);
        
        UCSBRequirement savedUcsbRequirement = ucsbRequirementRepository.save(ucsbRequirement);
        return savedUcsbRequirement;
    }
}
