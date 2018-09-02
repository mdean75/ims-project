package me.bedaring.imsproject.models.data;

import me.bedaring.imsproject.models.AssignedGroup;
import me.bedaring.imsproject.models.Carrier;
import me.bedaring.imsproject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserDao extends JpaRepository<User, Integer> {
    // get the user that matches the given username
    Optional<User> findByUsername(String username);

    // get the user that matches the given user id
    User findUserById(int id);

    // custom query to use when the user wants to change their password
    @Modifying
    @Query(value = "update user set password = ?1 where id = ?2", nativeQuery = true)
    int updatePasswordById(String password, int id);

    // custom query to use to update the mobile phone number and carrier
    @Modifying
    @Query(value = "update user set phone = ?1, carrier_id_id = ?2 where id = ?3", nativeQuery = true)
    int updatePhoneById(String phone, int carrierId, int id);

    // get a count of all users that match the giver carrier
    int countUserByCarrierId(Carrier carrier);

    // used to perform check before deleting a group in admin controller
    int countUsersByGroupIdEquals(AssignedGroup groupId);

    // check if there are any existing users with the given username or email
    int countUsersByUsernameOrEmail(String username, String email);

    // get all users sorted by last name
    List<User> findAllByOrderByLastName();

    // get all users by group
    List<User> findAllByGroupId(AssignedGroup groupId);

    // get user by token
    Optional<User> findUserByToken(String token);
}
