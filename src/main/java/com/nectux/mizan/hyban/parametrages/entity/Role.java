package com.nectux.mizan.hyban.parametrages.entity;

import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;




@Table(name="CGECI_RHPAIE_ROLE")
@SequenceGenerator(name="CGECI_RHPAIE_ROLE_SEQUENCE", sequenceName="CGECI_RHPAIE_ROLE_SEQ", initialValue=1, allocationSize=1)
@Entity
public class Role implements GrantedAuthority {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CGECI_RHPAIE_ROLE_SEQUENCE")
	@Column(unique=true, nullable=false)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(unique = true, nullable = false)
	private RoleName name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "CGECI_RHPAIE_ROLE_PERMISSION",
		joinColumns = @JoinColumn(name = "role_id"),
		inverseJoinColumns = @JoinColumn(name = "permission_id")
	)
	private Set<Permission> permissions = new HashSet<>();

	public Role() {}

	public Role(RoleName name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleName getName() {
		return name;
	}

	public void setName(RoleName name) {
		this.name = name;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String getAuthority() {
		return name.name();
	}
}


