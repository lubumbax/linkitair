package com.lubumbax.linkitair.flights.service;

import com.lubumbax.linkitair.flights.model.Parent;
import com.lubumbax.linkitair.flights.repository.ParentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ParentsService {
    @Autowired
    private ParentsRepository repository;

    public List<Parent> getAllParents() {
        return repository.findAll();
    }

    public List<Parent> getParentsByName(String name) {
        return repository.findByNameIn(Collections.singletonList(name));
    }

    public List<Parent> getParentsOfChildren(List<String> ids) {
        //return repository.findByChildIdIn(ids);
        return repository.findByChildrenIds(ids);
    }

    public List<Parent> getParentsOfChildId(String id) {
        return repository.findByChild(id);
    }

    public List<Parent> getParentsOfChildName(String name) {
        return repository.findByChildName(name);
    }
}
