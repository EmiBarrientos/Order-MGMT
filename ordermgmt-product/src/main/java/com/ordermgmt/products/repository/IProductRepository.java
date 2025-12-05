package com.ordermgmt.products.repository;

import com.ordermgmt.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByIdIn(List<Long> ids);
}
