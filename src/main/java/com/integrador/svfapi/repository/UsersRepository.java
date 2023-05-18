package com.integrador.svfapi.repository;

import com.integrador.svfapi.classes.Users;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, String> {
    @Query(value = "SELECT MAX(u.user_id) FROM Users u", nativeQuery = true)
    String findLastUserId();
    boolean existsById(@Nullable String userId);
}
