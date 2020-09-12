package ru.tpu.russian.back.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, IUserRepository {

    Optional<User> findByEmail(String email);
}
