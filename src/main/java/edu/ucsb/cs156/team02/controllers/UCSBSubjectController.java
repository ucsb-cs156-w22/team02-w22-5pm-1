package edu.ucsb.cs156.team02.controllers;

import edu.ucsb.cs156.team02.entities.UCSBSubject;
import edu.ucsb.cs156.team02.models.CurrentUser;
import edu.ucsb.cs156.team02.repositories.UCSBSubjectRepository;
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

@Api(description = "API to handle CRUD operations for UCSB Subjects database")
@RequestMapping("/api/UCSBSubjects")
@RestController
@Slf4j
public class UCSBSubjectController extends ApiController {
    @Autowired
    UCSBSubjectRepository subjectRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "List all UCSB Subjects")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public Iterable<UCSBSubject> allUsersTodos() {
        loggingService.logMethod();
        Iterable<UCSBSubject> subjects = subjectRepository.findAll();
        return subjects;
    }


    @ApiOperation(value = "Create a new UCSB Subjects entry in the database")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public UCSBSubject postSubject(
            @ApiParam("subjectCode") @RequestParam String subjectCode,
            @ApiParam("subjectTranslation") @RequestParam String subjectTranslation,
            @ApiParam("deptCode") @RequestParam String deptCode,
            @ApiParam("collegeCode") @RequestParam String collegeCode,
            @ApiParam("relatedDeptCode") @RequestParam String relatedDeptCode,
            @ApiParam("inactive") @RequestParam Boolean inactive) {
        loggingService.logMethod();

        UCSBSubject subject = new UCSBSubject();
        subject.setSubjectCode(subjectCode);
        subject.setSubjectTranslation(subjectTranslation);
        subject.setDeptCode(deptCode);
        subject.setCollegeCode(collegeCode);
        subject.setRelatedDeptCode(relatedDeptCode);
        subject.setInactive(inactive);
        UCSBSubject savedSubject = subjectRepository.save(subject);
        return savedSubject;
    }

    @ApiOperation(value = "Get a single UCSB Subject by ID if it is in the databse.")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<String> getSubjectById(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
        loggingService.logMethod();
        ResponseEntity<String> error;
        UCSBSubject subject;
        Optional<UCSBSubject> optionalSubject = subjectRepository.findById(id);

        if (optionalSubject.isEmpty()) {
            error = ResponseEntity
                    .badRequest()
                    .body(String.format("Subject with id %d not found", id));
            return error;
        }

        subject = optionalSubject.get();

        String body = mapper.writeValueAsString(subject);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Update a single todo (if it belongs to current user)")
    @PreAuthorize("hasRole('ROLE_USER') || hasRole('ROLE_ADMIN')")
    @PutMapping("")
    public ResponseEntity<String> putTodoById(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid UCSBSubject incomingSubject) throws JsonProcessingException {
        loggingService.logMethod();
        Optional<UCSBSubject> optionalSubject = subjectRepository.findById(id);

        if (optionalSubject.isEmpty()) {
            return ResponseEntity
            .badRequest()
            .body(String.format("Subject with id %d not found", id)); 
        }
        else{
            subjectRepository.save(incomingSubject);    
            String body = mapper.writeValueAsString(incomingSubject);
            return ResponseEntity.ok().body(body);
        }
    }
}