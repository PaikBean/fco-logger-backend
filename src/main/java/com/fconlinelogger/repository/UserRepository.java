package com.fconlinelogger.repository;

import com.fconlinelogger.domain.FCOUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<FCOUser, String> {
    boolean existsFCOUserByNickname(String nickname);
    FCOUser findByNickname(String nickName);
}
