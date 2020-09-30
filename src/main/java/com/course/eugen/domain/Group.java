package com.course.eugen.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "id")
@Builder
@Entity(name = "groups")
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_ids_gen")
    @SequenceGenerator(name = "group_ids_gen", sequenceName = "group_id_seq", allocationSize = 1)
    private BigInteger id;

    @Column(nullable = false, unique = true)
    private String name;

    @JoinColumn(name = "user_id", nullable = false)
    private BigInteger createdBy;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> users;

    @UpdateTimestamp
    private Timestamp updated;

}
