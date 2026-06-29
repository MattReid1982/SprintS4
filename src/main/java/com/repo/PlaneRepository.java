package com.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.model.Plane;

import java.util.Optional;

@Repository
public interface PlaneRepository extends CrudRepository<Plane, Long> {
    Optional<Plane> findAllById(Long id);

    void delete(long id);
}
