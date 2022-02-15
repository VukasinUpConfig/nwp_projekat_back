package com.example.RafCloud.machines;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MachineRepositoryCustomImpl implements MachineRepositoryCustom {
	
	@Autowired
	private EntityManager em;

	@Override
	public List<Machine> search(String name, Status status, Date dateFrom, Date dateTo) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Machine> cq = cb.createQuery(Machine.class);
		
		Root<Machine> root = cq.from(Machine.class);
		Predicate namePredicate = cb.like(root.get("name"), "%" + name + "%");
		Predicate statusPredicate = cb.equal(root.get("status"), status);
		Predicate toPredicate = cb.lessThanOrEqualTo(root.get("createdAt"), dateTo);
		Predicate fromPredicate = cb.greaterThanOrEqualTo(root.get("createdAt"), dateFrom);
		Predicate activePredicate = cb.equal(root.get("active"), true);
		
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(namePredicate);
		predicates.add(statusPredicate);
		predicates.add(activePredicate);
		if (dateFrom != null) {
			predicates.add(fromPredicate);
		}
		
		if (dateTo != null) {
			predicates.add(toPredicate);
		}
		
		Predicate[] predicatesArray = new Predicate[predicates.size()];
		cq.where(predicates.toArray(predicatesArray));
		
		TypedQuery<Machine> q = em.createQuery(cq);
		return q.getResultList();
	}

}
