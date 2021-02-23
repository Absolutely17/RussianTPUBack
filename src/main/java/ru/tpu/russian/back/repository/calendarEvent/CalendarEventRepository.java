package ru.tpu.russian.back.repository.calendarEvent;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.tpu.russian.back.entity.calendarEvent.CalendarEvent;

import java.util.*;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.LOAD;

@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, String> {

    @Query(value = "select event from CalendarEvent event " +
            "left join CalendarEventTargets targets on targets.event.id = event.id " +
            "left join User user on user.id = targets.user.id " +
            "left join StudyGroup gr on gr.id = targets.group.id " +
            "where user.email = :email or event.targetEnum = ru.tpu.russian.back.enums.CalendarEventGroupTarget.ALL or gr.name = user.groupName")
    List<CalendarEvent> findByUserEmail(@Param("email") String email);

    @Query(value = "select event from CalendarEvent event " +
            "left join CalendarEventTargets targets on targets.event.id = event.id " +
            "left join User user on user.id = targets.user.id " +
            "left join StudyGroup gr on gr.id = targets.group.id " +
            "where user.id = :id or event.targetEnum = ru.tpu.russian.back.enums.CalendarEventGroupTarget.ALL or gr.name = user.groupName")
    List<CalendarEvent> findByUserId(@Param("id") String id);

    @Query(value = "select event from CalendarEvent event " +
            "left join CalendarEventTargets targets on targets.event.id = event.id " +
            "where targets.group.id = :groupId or event.targetEnum = ru.tpu.russian.back.enums.CalendarEventGroupTarget.ALL")
    List<CalendarEvent> findByGroupId(@Param("groupId") String groupId);

    @Query("select event from CalendarEvent event " +
            "where event.id = :id")
    @EntityGraph(value = "CalendarEventAndTargets", type = LOAD)
    Optional<CalendarEvent> getByIdWithEagerFetch(@Param("id") String id);
}
