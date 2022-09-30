package edu.kit.physik.ophasenanmelder.model;

import de.m4rc3l.nova.jpa.model.TableModelAutoId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
