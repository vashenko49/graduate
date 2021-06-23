package com.client.client.controller;

import com.client.client.model.Result;
import com.client.client.repos.ResultRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/api")
public class ResultGet {


    @Autowired
    ResultRepository resultRepository;

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/result")
    public ResponseEntity<?> connectToServer(@RequestParam(value = "idUser") String idUser) throws JsonProcessingException {

        final Optional<Result> byIdUser = resultRepository.findByIdUser(idUser);


        System.out.println(byIdUser.isPresent());
        final Result result = byIdUser.get();

        final HashMap<List<Double>, List<List<Double>>> gg = new HashMap<>();

        final HashMap<String, List<List<Double>>> hashMap = objectMapper.readValue(result.getClusterMap(), HashMap.class);

        hashMap.entrySet().stream().map(listListEntry -> {
            final String key = listListEntry.getKey();
            try {
                final List<Double> list = objectMapper.readValue(key, List.class);
                final List<List<Double>> value = listListEntry.getValue();

                gg.put(list, value);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(gg, HttpStatus.OK);
    }
}
