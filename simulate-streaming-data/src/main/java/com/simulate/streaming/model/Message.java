package com.simulate.streaming.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    Double x;
    Double y;
    Double z;
    Double vx;
    Double vy;
    Double vz;

}
