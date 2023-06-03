package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CouserService;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    CouserService couserService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDto courseDto){
        var couseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, couseModel);
        couseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        couseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(couserService.save(couseModel));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCouse(@PathVariable(value = "courseId") UUID courseId){
        Optional<CourseModel> couseModelOptinal = couserService.findById(courseId);
        if(!couseModelOptinal.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coused Not Found.");
        }
        couserService.delete(couseModelOptinal.get());
        return ResponseEntity.status(HttpStatus.OK).body("Coused deleted successfully.");
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCouse(@PathVariable(value = "courseId") UUID courseId,
                                              @RequestBody @Valid CourseDto courseDto){
        Optional<CourseModel> couseModelOptinal = couserService.findById(courseId);
        if(!couseModelOptinal.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coused Not Found.");
        }
        var couseModel = new CourseModel();
        couseModel.setName(courseDto.getName());
        couseModel.setDescription(courseDto.getDescription());
        couseModel.setImageUrl(courseDto.getImageUrl());
        couseModel.setCourseStatus(courseDto.getCourseStatus());
        couseModel.setCourseLevel(courseDto.getCourseLevel());
        couseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.OK).body(couserService.save(couseModel));
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCouses(SpecificationTemplate.CourseSpec spec,
                                                          @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC)
                                                          Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(couserService.findAll(spec, pageable));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCouse(@PathVariable(value = "courseId") UUID courseId){
        Optional<CourseModel> couseModelOptinal = couserService.findById(courseId);
        if(!couseModelOptinal.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coused Not Found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(couseModelOptinal.get());
    }

}
