package ru.dcp.gamedev.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dcp.gamedev.demo.models.model.Classes;



@Repository
@Transactional
public interface JpaClassRepository extends JpaRepository<Classes, Integer> {


}
