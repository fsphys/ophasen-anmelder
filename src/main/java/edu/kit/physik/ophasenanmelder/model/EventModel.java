package edu.kit.physik.ophasenanmelder.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import de.m4rc3l.nova.jpa.model.TableModelAutoId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
}
