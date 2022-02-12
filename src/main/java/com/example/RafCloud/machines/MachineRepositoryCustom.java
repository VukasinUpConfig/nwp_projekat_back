package com.example.RafCloud.machines;

import java.util.Date;
import java.util.List;

public interface MachineRepositoryCustom {
	List<Machine> search(String name, Status status, Date dateFrom, Date dateTo);
}
