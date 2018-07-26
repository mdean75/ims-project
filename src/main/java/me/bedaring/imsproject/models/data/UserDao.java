package me.bedaring.imsproject.models.data;

import me.bedaring.imsproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Integer> {
    //User findByUsername(String username);

    Optional<User> findByUsername(String username);
}
