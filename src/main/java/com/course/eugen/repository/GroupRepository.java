package com.course.eugen.repository;

import com.course.eugen.domain.Group;
import com.course.eugen.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GroupRepository extends JpaRepository<Group, BigInteger> {

    Optional<Group> findByName(String name);

    boolean existsByName(String name);

    List<Group> findByUsersContains(User user);
}
