package com.onlinetutorialspoint.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@lombok.Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Data implements Serializable {
    Long numberMessage;
    String id;
    String messageListJSON;
    Operation operation;
    Boolean isFinal;
}
