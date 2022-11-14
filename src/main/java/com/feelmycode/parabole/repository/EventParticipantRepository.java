package com.feelmycode.parabole.repository;

import com.feelmycode.parabole.domain.EventParticipant;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {

    EventParticipant findByUserIdAndEventId(Long userId, Long eventId);
    List<EventParticipant> findAllByEventId(Long eventId);
    List<EventParticipant> findAllByUserId(Long userId);

    @Transactional
    void deleteByUserIdAndEventId(Long userId, Long eventId);
}
