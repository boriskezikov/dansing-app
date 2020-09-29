package com.course.eugen.repository;

import com.course.eugen.domain.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface TrainRepository extends JpaRepository<Train, BigInteger> {
}
