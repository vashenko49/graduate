package com.streaming.main;

import com.streaming.main.model.Data;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DataRepository extends CrudRepository<Data, String> {
}