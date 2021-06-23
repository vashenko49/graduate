package com.simulate.streaming;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.simulate.streaming.model.Message;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Service
public class CSVService {
    AtomicInteger index = new AtomicInteger(0);
    List<Message> messages = new ArrayList<>();

    public CSVService() {
        loadFilesToMemory();
    }

    void loadFilesToMemory() {
        File file = new File("./archive");
        Arrays.stream(file.listFiles()).
                filter(file1 -> file1.getName().toLowerCase().endsWith(".csv"))
                .forEach(f -> {
                    try {

                        CSVReader reader = new CSVReader(new FileReader(f));
                        String[] record = null;

                        reader.readNext();
                        while ((record = reader.readNext()) != null) {
                            Message build = Message.builder()
                                    .x(Double.parseDouble(record[0]))
                                    .y(Double.parseDouble(record[1]))
                                    .z(Double.parseDouble(record[2]))
                                    .vx(Double.parseDouble(record[3]))
                                    .vy(Double.parseDouble(record[4]))
                                    .vz(Double.parseDouble(record[5]))
                                    .build();
                            messages.add(build);
                        }
                    } catch (CsvValidationException | IOException e) {
                        System.exit(-1);
                    }
                });
    }
}
