package edu.kit.physik.ophasenanmelder.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.getnova.framework.jpa.model.TableModelAutoId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event_models")
public class EventTypeModel extends TableModelAutoId {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "registrationStartTime", nullable = false)
    private OffsetDateTime registrationStartTime;

    @Column(name = "registrationEndTime", nullable = false)
    private OffsetDateTime registrationEndTime;
}
