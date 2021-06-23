package com.onlinetutorialspoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinetutorialspoint.model.Data;
import com.onlinetutorialspoint.model.Message;
import com.onlinetutorialspoint.model.Result;
import com.onlinetutorialspoint.repo.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class RabbitMqListener {

    @Autowired
    private ResultRepository repository;
    private final static ObjectMapper objectMapper = new ObjectMapper();

    public void listen(String message) {
        try {
            System.out.println(message);

            final Data data = objectMapper.readValue(message, Data.class);

            final String messageListJSON = data.getMessageListJSON();

            final Message[] messages = objectMapper.readValue(messageListJSON, Message[].class);


            final List<List<Double>> collect = Arrays.stream(messages).map(m -> Arrays.asList(m.getVx(), m.getVy(), m.getVz(), m.getX(), m.getY(), m.getZ())).collect(Collectors.toList());


            final Optional<Result> byIdUser = repository.findByIdUser(data.getId());


            if (!byIdUser.isPresent()) {

                final SeqKMeans seqKMeansNew = new SeqKMeans(collect);
                seqKMeansNew.selectRandomCentroids(seqKMeansNew.k);
                seqKMeansNew.evaluateClusterMap();


                final List<List<Double>> listsNew = seqKMeansNew.kMeans();


                String clusterMap = objectMapper.writeValueAsString(seqKMeansNew.clusterMap);

                final Result build = Result.builder()
                        .idUser(data.getId())
                        .clusterMap(clusterMap)
                        .lists(listsNew)
                        .build();
                final Result save = repository.save(build);
            } else {

                final Result result = byIdUser.get();

                final HashMap<List<Double>, List<List<Double>>> gg = new HashMap<>();

                final HashMap<String, List<List<Double>>> hashMap = objectMapper.readValue(result.getClusterMap(), HashMap.class);

                final List<Object> collect1 = hashMap.entrySet().stream().map(listListEntry -> {
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
//                HashMap<List<Double>, List<List<Double>>>

//                hashMap


                final SeqKMeans seqKMeansNew = new SeqKMeans(result.getLists(), gg, collect);
                seqKMeansNew.selectRandomCentroids(seqKMeansNew.k);
                seqKMeansNew.evaluateClusterMap();
                final List<List<Double>> listsNew = seqKMeansNew.kMeans();

                result.setLists(listsNew);
                String clusterMap = objectMapper.writeValueAsString(seqKMeansNew.clusterMap);

                result.setClusterMap(clusterMap);

                repository.save(result);
            }


            System.out.println("Received a new notification...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}