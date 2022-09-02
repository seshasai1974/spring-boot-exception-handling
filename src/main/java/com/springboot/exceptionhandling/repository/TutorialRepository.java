package com.springboot.exceptionhandling.repository;

import java.util.List;

import com.springboot.exceptionhandling.datamodel.Tutorial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
  List<Tutorial> findByPublished(boolean published);

  List<Tutorial> findByTitleContaining(String title);
}
