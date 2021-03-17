package ru.dcp.gamedev.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dcp.gamedev.demo.models.model.Characters;
import ru.dcp.gamedev.demo.models.model.User;


@Repository
@Transactional
public interface JpaCharacterRepository extends JpaRepository<Characters, Integer> {

    Iterable<Characters> findAllByUser(User user);
}
