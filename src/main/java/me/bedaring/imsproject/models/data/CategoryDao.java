package me.bedaring.imsproject.models.data;

import me.bedaring.imsproject.models.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CategoryDao extends CrudRepository<Category, Integer> {
    List<Category> findCategoryByCategoryTypeEquals(String categoryType);

    @Query(value = "select * from category order by category_type, category_name ASC ", nativeQuery = true)
    List<Category> findAll();

}
