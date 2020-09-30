package com.course.eugen.dto;

import com.course.eugen.domain.enums.RolesEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {

    private BigInteger id;
    private String name;
    private BigInteger createdBy;
    private Timestamp updated;
    private RolesEnum role;
    private Integer membersCount;

}
