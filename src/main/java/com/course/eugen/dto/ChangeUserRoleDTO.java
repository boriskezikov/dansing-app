package com.course.eugen.dto;

import com.course.eugen.domain.enums.RolesEnum;
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
public class ChangeUserRoleDTO {

    @NonNull
    private BigInteger userId;
    @NonNull
    private RolesEnum role;
    @NonNull
    private BigInteger groupId;
}
