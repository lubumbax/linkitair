package com.lubumbax.linkitair.flights.controller;

import com.lubumbax.linkitair.flights.model.Parent;
import com.lubumbax.linkitair.flights.service.ParentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/parents")
public class ParentsController {
    @Autowired
    private ParentsService parentsService;

    @GetMapping("/")
    public List<Parent> findAll() {
        System.out.println("* Looking for all parents");
        return parentsService.getAllParents();
    }

    @GetMapping("/{name}")
    public List<Parent> findByName(@PathVariable String name) {
        System.out.println("* Looking for parent by name " + name);
        return parentsService.getParentsByName(name);
    }

    @GetMapping("/child/{id}")
    public List<Parent> findByChild(@PathVariable String id) {
        System.out.println("* Looking for parent of child id " + id);
        return parentsService.getParentsOfChildId(id);
    }

    @GetMapping("/child/name/{name}")
    public List<Parent> findByChildName(@PathVariable String name) {
        System.out.println("* Looking for parent of child name " + name);
        return parentsService.getParentsOfChildName(name);
    }

    @GetMapping("/children")
    public List<Parent> findByChildren(@RequestParam List<String> ids) {
        System.out.println("* Looking for parents of children " + ids);
        return parentsService.getParentsOfChildren(ids);
    }
}
