package com.client.client.repos;

import com.client.client.model.Result;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ResultRepository extends MongoRepository<Result, String> {
    Optional<Result> findByIdUser(String idUser);
}
