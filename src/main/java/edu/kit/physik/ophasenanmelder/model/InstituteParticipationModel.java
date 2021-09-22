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

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "institute_participations")
public class InstituteParticipationModel extends TableModelAutoId {

    @ManyToOne
    @JoinColumn(name = "institute_id", nullable = false)
    private InstituteModel institute;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "given_name", nullable = false)
    private String givenName;

    @Column(name = "mail", nullable = false)
    private String mail;
}

