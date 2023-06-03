package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CouserService;
import com.ead.course.services.ModuleService;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    ModuleService moduleService;

    @Autowired
    CouserService couserService;

    @PostMapping("courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable(value = "courseId") UUID courseId,
                                             @RequestBody @Valid ModuleDto moduleDto){
        Optional<CourseModel> couseModelOptinal = couserService.findById(courseId);
        if(!couseModelOptinal.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found.");
        }
        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDto, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(couseModelOptinal.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value = "courseId") UUID courseId,
                                              @PathVariable(value = "moduleId") UUID moduleId){
        Optional<ModuleModel> moduleModelOptinal = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(!moduleModelOptinal.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found.");
        }
        moduleService.delete(moduleModelOptinal.get());
        return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully.");
    }

    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value = "courseId") UUID courseId,
                                              @PathVariable(value = "moduleId") UUID moduleId,
                                              @RequestBody @Valid ModuleDto moduleDto){
        Optional<ModuleModel> moduleModelOptinal = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(!moduleModelOptinal.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found.");
        }
        var moduleModel = moduleModelOptinal.get();
        moduleModel.setTitle(moduleDto.getTitle());
        moduleModel.setDescription(moduleDto.getDescription());
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.save(moduleModel));
    }

    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<Page<ModuleModel>> getAllModules(@PathVariable(value = "courseId") UUID courseId,
                                                           SpecificationTemplate.ModuleSpec spec,
                                                           @PageableDefault(page = 0, size = 10, sort = "moduleId", direction = Sort.Direction.ASC)
                                                           Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findAllByCourse(SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable));
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable(value = "courseId") UUID courseId,
                                              @PathVariable(value = "moduleId") UUID moduleId){
        Optional<ModuleModel> moduleModelOptinal = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(!moduleModelOptinal.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(moduleModelOptinal.get());
    }

}
