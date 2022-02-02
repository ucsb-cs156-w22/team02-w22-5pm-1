package edu.ucsb.cs156.team02.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import  edu.ucsb.cs156.team02.entities.UCSBSubject;

@Repository
public interface UCSBSubjectRepository extends CrudRepository<UCSBSubject, Long>{
    Iterable<UCSBSubject> findBySubject(String subject);
}
