package com.example.RafCloud.machines;

import lombok.Data;

@Data
public class ScheduleActionDTO {

	private long machineId;
	private String action;
	private String dateTime;
}
