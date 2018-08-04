package me.bedaring.imsproject.models.data;

import me.bedaring.imsproject.models.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import javax.validation.constraints.Past;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ImsDao extends CrudRepository<Ticket, Integer> {

    int countTicketBySeverity(Severity severity);

    @Query(value = "select count(id) from ticket where category_sub_id = ?1 or category_main_id = ?1 or " +
            "category_detail_id = ?1", nativeQuery = true)
    int countTicketByCategory(Category category);

    @Query(value = "select count(id) from ticket where assigned_group_id = ?1", nativeQuery = true)
    int countTicketByAssignedGroup(AssignedGroup assignedGroup);

    Ticket findAllByAssignedPersonEquals(String user);

    Ticket findAllByAssignedGroupEquals(AssignedGroup group);
}
