package com.example.RafCloud.machines;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.RafCloud.rest.AbstractController;

@RestController
@RequestMapping("/api/machines")
public class MachineController extends AbstractController {
	
	@Autowired
	private MachineService machineService;
	
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Machine create(@RequestBody CreateMachineDTO machine) {
		return machineService.createMachine(machine, findActingUserId());
	}
	
	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable long id) {
		machineService.deleteMachine(findActingUserId(), id);
	}
	
	@GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Machine> search(@RequestBody SearchMachineDTO machine) {
		return machineService.search(findActingUserId(), machine);
	}
	
	@PostMapping("/schedule")
	public void manageMachineState(@RequestBody ScheduleActionDTO scheduleAction) {
		machineService.scheduleAction(findActingUserId(), scheduleAction);
	}
}
