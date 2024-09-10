package edu.kit.physik.ophasenanmelder.model;

import de.m4rc3l.nova.jpa.model.TableModelAutoId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class EventModel extends TableModelAutoId {

    @ManyToOne
    @JoinColumn(name = "event_type_id", nullable = false)
    private EventTypeModel eventType;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "needs_birth_information", nullable = false)
    private Boolean needsBirthInformation;

    @Column(name = "meeting_time")
    private OffsetDateTime meetingTime;

    @Column(name = "meeting_point")
    private String meetingPoint;
}
