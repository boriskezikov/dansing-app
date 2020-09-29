package com.course.eugen.repository;

import com.course.eugen.domain.Group;
import com.course.eugen.domain.RoleModel;
import com.course.eugen.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface RoleModelRepository extends JpaRepository<RoleModel, BigInteger> {

    Optional<RoleModel> findByUserAndGroup(User user, Group group);
}
