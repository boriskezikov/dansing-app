package com.course.eugen.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@EqualsAndHashCode(of = "id")
@Builder
@Entity(name = "trains")
@NoArgsConstructor
@AllArgsConstructor
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "train_ids_gen")
    @SequenceGenerator(name = "train_ids_gen", sequenceName = "train_id_seq", allocationSize = 1)
    private BigInteger id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Group group;

    @CreationTimestamp
    private Timestamp created;

    @Column(nullable = false)
    private Timestamp beginTime;
}
