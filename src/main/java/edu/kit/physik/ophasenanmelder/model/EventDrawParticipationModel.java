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
@Table(name = "event_draw_participation")
public class EventDrawParticipationModel extends TableModelAutoId {

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventModel event;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "given_name", nullable = false)
    private String givenName;

    @Column(name = "mail", nullable = false)
    private String mail;

    @Column(name = "birth_date")
    private OffsetDateTime birthDate;

    @Column(name = "birth_place")
    private String birthPlace;
}
