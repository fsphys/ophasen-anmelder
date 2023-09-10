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
@Table(name = "event_type")
public class EventTypeModel extends TableModelAutoId {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "registration_start_time", nullable = false)
    private OffsetDateTime registrationStartTime;

    @Column(name = "registration_end_time", nullable = false)
    private OffsetDateTime registrationEndTime;

    @ManyToOne
    @JoinColumn(name = "event_draw_id")
    private EventDrawModel eventDraw;
}
