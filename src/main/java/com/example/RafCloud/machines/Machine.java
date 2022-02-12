package com.example.RafCloud.machines;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.example.RafCloud.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
@Entity
public class Machine {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column
	@Enumerated
	private Status status;
	
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;
	
	@Column
	private boolean active;
	
	@Column
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "owner_id", referencedColumnName = "id")
	@JsonIgnore
	private User owner;
	
	@JsonIgnore
	@OneToMany(mappedBy = "machine", cascade = CascadeType.ALL)
	private List<ScheduledAction> scheduledActions;
	
	@Version
	private long version;
}
