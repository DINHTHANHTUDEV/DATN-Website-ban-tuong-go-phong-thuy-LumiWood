package com.example.websitebantuonggolumiwood.usermanagement.repository;

import com.example.websitebantuonggolumiwood.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManagementRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    // JpaSpecificationExecutor giúp build truy vấn động, filter theo tier, status, search
}
