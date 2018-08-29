package me.bedaring.imsproject.models.data;

import me.bedaring.imsproject.models.Carrier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CarrierDao extends CrudRepository<Carrier, Integer> {
    Carrier findCarrierById(int id);
}
