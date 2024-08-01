package com.example.rpl.RPL.controller;

import com.example.rpl.RPL.controller.dto.ActivityResponseDTO;
import com.example.rpl.RPL.controller.dto.ActivityResponseDTO.IOTestResponseDTO;
import com.example.rpl.RPL.controller.dto.CreateIOTestRequestDTO;
import com.example.rpl.RPL.controller.dto.CreateUnitTestRequestDTO;
import com.example.rpl.RPL.model.Activity;
import com.example.rpl.RPL.model.IOTest;
import com.example.rpl.RPL.model.UnitTest;
import com.example.rpl.RPL.security.CurrentUser;
import com.example.rpl.RPL.security.UserPrincipal;
import com.example.rpl.RPL.service.ActivitiesService;
import com.example.rpl.RPL.service.TestService;
import java.util.List;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ActivityTestsController {

    private final ActivitiesService activitiesService;
    private final TestService testService;

    @Autowired
    public ActivityTestsController(
        ActivitiesService activitiesService, TestService testService) {
        this.activitiesService = activitiesService;
        this.testService = testService;
    }

    @PreAuthorize("hasAuthority('activity_manage')")
    @PostMapping(value = "/api/courses/{courseId}/activities/{activityId}/iotests")
    public ResponseEntity<IOTestResponseDTO> createIOTestCase(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId,
        @PathVariable Long activityId,
        @RequestBody @Valid CreateIOTestRequestDTO createIOTestRequestDTO) {

//        check the activity exists
        Activity activity = activitiesService.getActivity(activityId);

        IOTest test = testService.createIOTest(activityId, createIOTestRequestDTO.getName(),
            createIOTestRequestDTO.getTextIn(),
            createIOTestRequestDTO.getTextOut());

        activitiesService.updateTestMode(activity, true);
        return new ResponseEntity<>(
            new IOTestResponseDTO(test.getId(), test.getName(), test.getTestIn(),
                test.getTestOut()),
            HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('activity_manage')")
    @PutMapping(value = "/api/courses/{courseId}/activities/{activityId}/iotests/{ioTestId}")
    public ResponseEntity<IOTestResponseDTO> updateIOTestCase(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId,
        @PathVariable Long activityId,
        @PathVariable Long ioTestId,
        @RequestBody @Valid CreateIOTestRequestDTO createIOTestRequestDTO) {

//        check the activity exists
        activitiesService.getActivity(activityId);

        IOTest test = testService.updateIOTest(ioTestId, createIOTestRequestDTO.getName(),
            createIOTestRequestDTO.getTextIn(),
            createIOTestRequestDTO.getTextOut());
        return new ResponseEntity<>(
            new IOTestResponseDTO(test.getId(), test.getName(), test.getTestIn(),
                test.getTestOut()),
            HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_manage')")
    @DeleteMapping(value = "/api/courses/{courseId}/activities/{activityId}/iotests/{ioTestId}")
    public ResponseEntity<ActivityResponseDTO> deleteIOTestCase(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId,
        @PathVariable Long activityId,
        @PathVariable Long ioTestId) {

        Activity activity = activitiesService.getActivity(activityId);

        testService.deleteUnitTest(ioTestId);

        //        GET UNIT TESTS
        UnitTest unitTest = testService.getUnitTests(activity.getId());

        //        GET IO TESTS
        List<IOTest> ioTests = testService.getAllIOTests(activity.getId());

        return new ResponseEntity<>(ActivityResponseDTO.fromEntity(activity, unitTest, ioTests),
            HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_manage')")
    @PostMapping(value = "/api/courses/{courseId}/activities/{activityId}/unittests")
    public ResponseEntity<ActivityResponseDTO> createUnitTestCase(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId,
        @PathVariable Long activityId,
        @RequestBody @Valid CreateUnitTestRequestDTO createUnitTestRequestDTO) {

//        check the activity exists
        Activity activity = activitiesService.getActivity(activityId);

        UnitTest unitTest = testService
            .createUnitTest(activityId, createUnitTestRequestDTO.getUnitTestCode());
//
        activity = activitiesService.updateTestMode(activity, false);

        //        GET IO TESTS
        List<IOTest> ioTests = testService.getAllIOTests(activity.getId());

        return new ResponseEntity<>(ActivityResponseDTO.fromEntity(activity, unitTest, ioTests),
            HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('activity_manage')")
    @PutMapping(value = "/api/courses/{courseId}/activities/{activityId}/unittests")
    public ResponseEntity<ActivityResponseDTO> updateUnitTestCase(
        @CurrentUser UserPrincipal currentUser,
        @PathVariable Long courseId,
        @PathVariable Long activityId,
        @RequestBody @Valid CreateUnitTestRequestDTO createUnitTestRequestDTO) {

//        check the activity exists
        Activity activity = activitiesService.getActivity(activityId);

        UnitTest unitTest = testService
            .updateUnitTest(activityId, createUnitTestRequestDTO.getUnitTestCode());
//
        activity = activitiesService.updateTestMode(activity, false);

        //        GET IO TESTS
        List<IOTest> ioTests = testService.getAllIOTests(activity.getId());

        return new ResponseEntity<>(ActivityResponseDTO.fromEntity(activity, unitTest, ioTests),
            HttpStatus.OK);
    }

}
