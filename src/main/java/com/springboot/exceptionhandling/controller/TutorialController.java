package com.springboot.exceptionhandling.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.springboot.exceptionhandling.exception.ResourceNotFoundException;
import com.springboot.exceptionhandling.datamodel.Tutorial;
import com.springboot.exceptionhandling.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TutorialController {

  @Autowired
  TutorialRepository tutorialRepository;

  @GetMapping("/tutorials")
  public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {
    List<Tutorial> tutorials = new ArrayList<Tutorial>();

    if (title == null)
      tutorialRepository.findAll().forEach(tutorials::add);
    else
      tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);

/*    if (tutorials.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }*/

    return new ResponseEntity<>(tutorials, HttpStatus.OK);
  }

  @GetMapping("/tutorials/{id}")
  public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
    Tutorial tutorial = tutorialRepository.findById(id)
       .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id given "));

    return new ResponseEntity<>(tutorial, HttpStatus.OK);
  }

  @GetMapping("/tutorialsNE/{id}")
  public Optional<Tutorial> getTutorialByIdNoException(@PathVariable("id") long id) {
    Optional<Tutorial> tutorial = Optional.ofNullable(tutorialRepository.findById(id).orElseThrow(() -> new RuntimeException()));
    return tutorial;
  }

  @PostMapping("/tutorials")
  public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
    Tutorial _tutorial = tutorialRepository.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
    return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
  }

  @PutMapping("/tutorials/{id}")
  public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
    Tutorial _tutorial = tutorialRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Not found Tutorial with id = " + id));

    _tutorial.setTitle(tutorial.getTitle());
    _tutorial.setDescription(tutorial.getDescription());
    _tutorial.setPublished(tutorial.isPublished());
    
    return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
  }

  @DeleteMapping("/tutorials/{id}")
  public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {

    Tutorial tutorial = tutorialRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(" Could Not delete as no tutorial found with ID " + id));

    System.out.println( " tutorial : " +  tutorial.getTitle());

    if(tutorial != null) {
      System.out.println(" Tutorial Found");
      tutorialRepository.deleteById(id);
    }
//    if (tutorialRepository.deleteById(id) != null)
//    {
//      throw new ResourceNotFoundException("Could not delete as no such data");
//    };
    
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @DeleteMapping("/tutorials")
  public ResponseEntity<HttpStatus> deleteAllTutorials() {
    tutorialRepository.deleteAll();
    
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/tutorials/published")
  public ResponseEntity<List<Tutorial>> findByPublished() {
    List<Tutorial> tutorials = tutorialRepository.findByPublished(true);

    if (tutorials.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    return new ResponseEntity<>(tutorials, HttpStatus.OK);
  }

}
