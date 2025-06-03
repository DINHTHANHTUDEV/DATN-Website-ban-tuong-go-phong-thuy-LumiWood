package com.example.websitebantuonggolumiwood.userprofile.repository;

import com.example.websitebantuonggolumiwood.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<User,Long> {
}
