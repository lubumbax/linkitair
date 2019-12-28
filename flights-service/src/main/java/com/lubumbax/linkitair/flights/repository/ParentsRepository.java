package com.lubumbax.linkitair.flights.repository;

import com.lubumbax.linkitair.flights.model.Parent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ParentsRepository extends MongoRepository<Parent, String> {
    List<Parent> findByName(String name);

    // { "child.id" : { "$in" : ["5e0720cb7da1f6404184d1b7"]}}
    // { "child._id" : "{\"$in\" : [\"5e0720cb7da1f6404184d1b7\"]}"}
    List<Parent> findByChildIdIn(List<String> ids);

    // {{child._id=Document{{$in=[5e0725496cab7a2b8e6f7023]}}}}
    // { "child._id" : "{\"$in\" : [\"5e0725496cab7a2b8e6f7023\"]}"}
    @Query("{'child._id': {$in: ?0}}")
    List<Parent> findByChildrenIds(List<String> ids);

    @Query("{?0: {$in: ?1}}")
    List<Parent> findByChildrenIdsWithFilter(String field, List<String> ids);

    // {{child.name=Document{{$in=[child1]}}}}
    // { "child.name" : { "$in" : ["child1"]}}
    @Query("{'child.name': {$in: ?0}}")
    List<Parent> findByNameIn(List<String> names);

    // {{child._id=5e07238a2a2580596040a6cb}}
    // { "child._id" : "5e07238a2a2580596040a6cb"}
    @Query("{'child._id': ?0}")
    List<Parent> findByChild(String id);

    // {{child.name=child2}}
    // { "child.name" : "child2"}
    @Query("{'child.name': ?0}")
    List<Parent> findByChildName(String name);
}
