package ru.dcp.gamedev.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.dcp.gamedev.demo.models.model.Notification;


@Repository
@Transactional
public interface JpaNotificationRepository extends JpaRepository<Notification, Integer> {

    @Query
    ("SELECT n FROM Notification n WHERE n.state = ?1") Iterable<Notification> findAllByState(String state);


}
