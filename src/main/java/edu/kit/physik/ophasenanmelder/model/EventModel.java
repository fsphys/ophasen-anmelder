package edu.kit.physik.ophasenanmelder.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.getnova.framework.jpa.model.TableModelAutoId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class EventModel extends TableModelAutoId {

    @ManyToOne
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventTypeModel eventType;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "maxParticipants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "startTime", nullable = false)
    private OffsetDateTime startTime;
}
