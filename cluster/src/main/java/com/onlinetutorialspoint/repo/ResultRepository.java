package com.onlinetutorialspoint.repo;

import com.onlinetutorialspoint.model.Result;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ResultRepository extends MongoRepository<Result, String> {
    Optional<Result> findByIdUser(String idUser);
}
