package edu.kit.physik.ophasenanmelder.model;

import de.m4rc3l.nova.jpa.model.TableModelAutoId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
