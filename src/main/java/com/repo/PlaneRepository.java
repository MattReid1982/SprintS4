package com.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.model.Plane;

@Repository
public interface PlaneRepository extends CrudRepository<Plane, Long> {
}
