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
@Table(name = "event_participations")
public class EventParticipationModel extends TableModelAutoId {

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventModel event;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "given_name", nullable = false)
    private String givenName;

    @Column(name = "mail", nullable = false)
    private String mail;

    @Column(name = "hasTicket")
    private Boolean hasTicket;

    @Column(name = "birthDate")
    private OffsetDateTime birthDate;

    @Column(name = "birthPlace")
    private String birthPlace;
}

