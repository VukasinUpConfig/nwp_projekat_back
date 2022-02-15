package com.example.RafCloud.machines;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.example.RafCloud.rest.AbstractController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
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
