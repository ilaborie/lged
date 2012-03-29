package org.ilaborie.pineneedles.web.model.montioring;

import java.util.Calendar;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class Task {

	@Id
	private String id;

	private String name;

	@Enumerated(EnumType.STRING)
	private MonitorStatus status;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar start;

	@Temporal(TemporalType.TIMESTAMP)
	private Calendar end;

	public String getId() {
    	return id;
    }

	public void setId(String id) {
    	this.id = id;
    }

	public String getName() {
    	return name;
    }

	public void setName(String name) {
    	this.name = name;
    }

	public MonitorStatus getStatus() {
    	return status;
    }

	public void setStatus(MonitorStatus status) {
    	this.status = status;
    }

	public Calendar getStart() {
    	return start;
    }

	public void setStart(Calendar start) {
    	this.start = start;
    }

	public Calendar getEnd() {
    	return end;
    }

	public void setEnd(Calendar end) {
    	this.end = end;
    }
	
	

}
