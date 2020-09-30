package com.course.eugen.repository;

import com.course.eugen.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, BigInteger> {

    boolean existsByMailAndPassword(String mail, String password);

    Optional<User> findByMailAndPassword(String mail, String password);
}
