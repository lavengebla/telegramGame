package ru.dcp.gamedev.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dcp.gamedev.demo.models.model.DocumentNotification;


@Repository
@Transactional
public interface JpaDocumentNotificationRepository extends JpaRepository<DocumentNotification, Integer> {

    @Query
    ("SELECT n FROM DocumentNotification n WHERE n.state = ?1") Iterable<DocumentNotification> findAllByState(String state);


}
