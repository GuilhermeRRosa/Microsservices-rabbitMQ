package gg.code.productapi.repositories;

import gg.code.productapi.models.Category;
import gg.code.productapi.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameIgnoreCaseContaining(String name);
    List<Product> findBySupplierId(Integer id);
    List<Product> findByCategoryId(Integer id);

    boolean existsByCategoryId(Integer id);
    boolean existsBySupplierId(Integer id);


}
