package org.oapen.memoproject.taskrunner.entities;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "query")
//@Setter 
@Getter @ToString
@Immutable
public class Query implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Type(type="uuid-char")
	@EqualsAndHashCode.Include
    private UUID id; 
	private String name; 

	@ToString.Exclude
	private String body, params, notes;
}