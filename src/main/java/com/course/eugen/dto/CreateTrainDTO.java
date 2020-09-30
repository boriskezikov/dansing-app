package com.course.eugen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTrainDTO {

    @NonNull
    private BigInteger creatorId;

    @NonNull
    private BigInteger groupId;

    @NonNull
    private Timestamp beginTime;
}
