package com.example.projectbase.controller;


import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.classes.ClassRequestDto;
import com.example.projectbase.service.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Validated
@RestController
@RestApiV1
public class ClassController {

    private final ClassService classService;

    @Tag(name = "class-controller")
    @Operation(summary = "API get class by id")
    @GetMapping(UrlConstant.Class.GET_CLASS)
    public ResponseEntity<?> getClassById(@PathVariable Long classId) {
        return VsResponseUtil.success(classService.getClassById(classId));
    }

    @Tag(name = "class-controller")
    @Operation(summary = "API create class")
    @PostMapping(UrlConstant.Class.CREATE_CLASS)
    public ResponseEntity<?> createClass(@RequestBody @Valid ClassRequestDto classRequestDto) {
        return VsResponseUtil.success(classService.addNewClass(classRequestDto));
    }

    @Tag(name = "class-controller")
    @Operation(summary = "API update class by id")
    @PutMapping(UrlConstant.Class.UPDATE_CLASS)
    public ResponseEntity<?> updateClass(@PathVariable Long classId,
                                         @RequestBody @Valid ClassRequestDto classRequestDto) {
        return VsResponseUtil.success(classService.editClass(classId, classRequestDto));
    }

    @Tag(name = "class-controller")
    @Operation(summary = "API delete class by id")
    @DeleteMapping(UrlConstant.Class.DELETE_CLASS)
    public ResponseEntity<?> deleteClass(@PathVariable Long classId) {
        classService.deleteClass(classId);
        return VsResponseUtil.success("Class deleted");
    }

    @Tag(name = "class-controller")
    @Operation(summary = "API get all classes")
    @GetMapping(UrlConstant.Class.BASE)
    public ResponseEntity<?> getAllClasses() {
        return VsResponseUtil.success(classService.getALlClass());
    }

}
