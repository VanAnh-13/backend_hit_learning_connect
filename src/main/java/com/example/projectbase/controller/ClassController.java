package com.example.projectbase.controller;


import com.example.projectbase.base.RestApiV1;
import com.example.projectbase.base.VsResponseUtil;
import com.example.projectbase.constant.ErrorMessage;
import com.example.projectbase.constant.ResponseMessage;
import com.example.projectbase.constant.UrlConstant;
import com.example.projectbase.domain.dto.request.classes.ClassRequestDto;
import com.example.projectbase.service.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Validated
@RestApiV1
public class ClassController {

    private final ClassService classService;

    @Tag(name = "class-controller", description = "Authenticated")
    @GetMapping(UrlConstant.Class.GET_CLASS)
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public ResponseEntity<?> getClassById(@PathVariable Long classId) {
        return VsResponseUtil.success(classService.getClassById(classId));
    }

    @Tag(name = "class-controller")
    @Operation(summary = "API create class", description = "Admin / Leader")
    @PostMapping(UrlConstant.Class.CREATE_CLASS)
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public ResponseEntity<?> createClass(@RequestBody @Valid ClassRequestDto classRequestDto) {
        return VsResponseUtil.success(classService.addNewClass(classRequestDto));
    }

    @Tag(name = "class-controller")
    @Operation(summary = "API update class by id", description = "Admin / Leader")
    @PutMapping(UrlConstant.Class.UPDATE_CLASS)
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public ResponseEntity<?> updateClass(@PathVariable Long classId,
                                         @RequestBody @Valid ClassRequestDto classRequestDto) {
        return VsResponseUtil.success(classService.editClass(classId, classRequestDto));
    }

    @Tag(name = "class-controller")
    @Operation(summary = "API delete class by id", description = "Admin / Leader")
    @DeleteMapping(UrlConstant.Class.DELETE_CLASS)
    @PreAuthorize("hasAnyRole('ADMIN','LEADER')")
    public ResponseEntity<?> deleteClass(@PathVariable Long classId) {
        classService.deleteClass(classId);
        return VsResponseUtil.success(ResponseMessage.DELETE_SUCCESS);
    }

    @Tag(name = "class-controller")
    @Operation(summary = "API get all classes", description = "Authenticated")
    @GetMapping(UrlConstant.Class.BASE)
    public ResponseEntity<?> getAllClasses() {
        return VsResponseUtil.success(classService.getALlClass());
    }

    @Tag(name = "class-controller")
    @Operation(summary = "API get all classes by filter", description = "Authenticated")
    @GetMapping(UrlConstant.Class.FILTER_CLASS)
    public ResponseEntity<?> getAllClassesByFilter(@ParameterObject @PageableDefault(page = 0, size = 1000, sort = "classId", direction = Sort.Direction.ASC)
                                                  Pageable pageable,
                                                  @RequestParam(value = "keyword", required = false) String keyword
    ) {
        return VsResponseUtil.success(classService.getALlClassByFilter(pageable, keyword));
    }


}
