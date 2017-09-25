package com.feldman.blazej.repository;

import com.feldman.blazej.model.Logo;
import com.feldman.blazej.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogoRepository extends JpaRepository<Logo, Long> {

    Logo findByName(String name);

    List<Logo> findByUserId(User userId);

}
