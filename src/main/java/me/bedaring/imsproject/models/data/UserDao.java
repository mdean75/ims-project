package me.bedaring.imsproject.models.data;

import me.bedaring.imsproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UserDao extends JpaRepository<User, Integer> {
    //User findByUsername(String username);

    Optional<User> findByUsername(String username);
}
