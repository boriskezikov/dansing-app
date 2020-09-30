package com.course.eugen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MarkUserDTO {

    @NonNull
    private BigInteger userId;
    @NonNull
    private BigInteger trainId;

}
