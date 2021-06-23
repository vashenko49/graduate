package com.onlinetutorialspoint.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
    Double x;
    Double y;
    Double z;
    Double vx;
    Double vy;
    Double vz;
}
