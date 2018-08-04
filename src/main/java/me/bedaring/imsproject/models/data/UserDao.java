package me.bedaring.imsproject.models.data;

import me.bedaring.imsproject.models.Carrier;
import me.bedaring.imsproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UserDao extends JpaRepository<User, Integer> {
    //User findByUsername(String username);

    Optional<User> findByUsername(String username);

    User findUserById(int id);

    @Modifying
    @Query(value = "update user set password = ?1 where id = ?2", nativeQuery = true)
    int updatePasswordById(String password, int id);

    @Modifying
    @Query(value = "update user set phone = ?1, carrier_id_id = ?2 where id = ?3", nativeQuery = true)
    int updatePhoneById(String phone, int carrierId, int id);

    int countUserByCarrierId(Carrier carrier);
}
