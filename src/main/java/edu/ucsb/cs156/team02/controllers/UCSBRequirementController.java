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
    
    @Autowired
    UCSBRequirementRepository ucsbRequirementRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "List all UCSBRequirements.")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public Iterable<UCSBRequirement> allUcsbRequirements() {
        loggingService.logMethod();
        Iterable<UCSBRequirement> requirements = ucsbRequirementRepository.findAll();
        return requirements;
    }

    @ApiOperation(value = "Get a single UCSBRequirement by id.")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<String> getUcsbRequirementById(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {

        loggingService.logMethod();

        Optional<UCSBRequirement> optionalRequirement = ucsbRequirementRepository.findById(id);

        if (optionalRequirement.isEmpty()) {
            ResponseEntity<String> error;
            error = ResponseEntity
                    .badRequest()
                    .body(String.format("id %d not found", id));
            return error;
        }

        UCSBRequirement requirement = optionalRequirement.get();

        String body = mapper.writeValueAsString(requirement);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Create a new UCSBRequirement.")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
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

    @ApiOperation(value = "Delete a UCSB requirement")
    @PreAuthorize("hasRole('ROLE_USER')  || hasRole('ROLE_ADMIN')")
    @DeleteMapping("")
    public ResponseEntity<String> deleteUcsbRequirement(
            @ApiParam("id") @RequestParam Long id) {
        loggingService.logMethod();


        Optional<UCSBRequirement> optionalRequirement = ucsbRequirementRepository.findById(id);

        if (optionalRequirement.isEmpty()) {
            ResponseEntity<String> error;
            error = ResponseEntity
                    .badRequest()
                    .body(String.format("id %d not found", id));
            return error;
        }

        ucsbRequirementRepository.deleteById(id);

        return ResponseEntity.ok().body(String.format("UCSB Requirement with id %d deleted", id));
    }

    @ApiOperation(value = "Update a single UCSB requirement")
    @PreAuthorize("hasRole('ROLE_ADMIN')  || hasRole('ROLE_ADMIN')")
    @PutMapping("")
    public ResponseEntity<String> putUcsbRequirementById(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid UCSBRequirement incomingUcsbRequirement) throws JsonProcessingException {
        loggingService.logMethod();

        Optional<UCSBRequirement> optionalRequirement = ucsbRequirementRepository.findById(id);

        if (optionalRequirement.isEmpty()) {
            ResponseEntity<String> error;
            error = ResponseEntity
                    .badRequest()
                    .body(String.format("id %d not found", id));
            return error;
        }

        incomingUcsbRequirement.setId(id);
        ucsbRequirementRepository.save(incomingUcsbRequirement);

        String body = mapper.writeValueAsString(incomingUcsbRequirement);
        return ResponseEntity.ok().body(body);
    }

}
