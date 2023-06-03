package com.ead.course.services;

import com.ead.course.models.ModuleModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleService {
    void delete(ModuleModel moduleModel);

    ModuleModel save(ModuleModel moduleModel);

    Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId);

    Page<ModuleModel> findAllByCourse(Specification<ModuleModel> spec, Pageable pageable);

    Optional<ModuleModel> findById(UUID moduleId);
}
