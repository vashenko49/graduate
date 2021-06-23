package com.onlinetutorialspoint.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    @Id
    public String id;
    String idUser;
    List<List<Double>> lists;
    String clusterMap;
}
