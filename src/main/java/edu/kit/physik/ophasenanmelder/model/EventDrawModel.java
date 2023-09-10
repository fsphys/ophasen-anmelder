package edu.kit.physik.ophasenanmelder.model;

import de.m4rc3l.nova.jpa.model.TableModelAutoId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "event_draw")
public class EventDrawModel extends TableModelAutoId {

    @Column(name = "draw_time", nullable = false)
    private OffsetDateTime drawTime;

    @Column(name = "drawn", nullable = false)
    private Boolean drawn;
}
