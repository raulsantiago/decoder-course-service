package com.ead.course.services;

import com.ead.course.models.CourseModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouserService {

    void delete(CourseModel courseModel);

    CourseModel save(CourseModel couseModel);

    Optional<CourseModel> findById(UUID courseId);

    List<CourseModel> findAll();
}
