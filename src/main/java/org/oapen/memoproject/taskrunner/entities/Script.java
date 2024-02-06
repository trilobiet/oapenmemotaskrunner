package org.oapen.memoproject.taskrunner.entities;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.ToString;

@Entity
//@Setter 
@Getter 
@ToString
@Immutable
@Table(name = "script")
public class Script implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public enum ScriptType {
		MAIN, SNIP
	}
	
	@Id
	@Type(type="uuid-char")
    private UUID id; 
	private String name;

	@Enumerated(EnumType.STRING)
	private ScriptType type;
	@ToString.Exclude
	private String body, params, notes;
	
	@OneToOne
	@JoinColumn(name = "id_query")
	private Query query;
	
}
