package com.example.RafCloud.machines;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;

@Data
@Entity
public class ScheduledActionError {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column
	private String message;
	
	@OneToOne
	@JoinColumn(name = "scheduled_action_id", referencedColumnName = "id")
	private ScheduledAction scheduledAction;
}
