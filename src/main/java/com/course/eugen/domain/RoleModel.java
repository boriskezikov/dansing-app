package com.course.eugen.domain;


import com.course.eugen.domain.enums.RolesEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.math.BigInteger;

@Data
@EqualsAndHashCode(of = "id")
@Builder
@Entity(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class RoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_ids_gen")
    @SequenceGenerator(name = "role_ids_gen", sequenceName = "role_id_seq", allocationSize = 1)
    private BigInteger id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RolesEnum role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private Group group;

}
