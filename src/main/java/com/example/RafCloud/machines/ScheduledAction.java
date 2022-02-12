package com.example.RafCloud.machines;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
public class ScheduledAction {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column
	@Enumerated
	private ScheduledActionStatus status;
	
	@Column
	@Enumerated
	private Action action;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date processingDate;
	
	@OneToOne
	@JoinColumn(name = "machine_id", referencedColumnName = "id")
	private Machine machine;
}
