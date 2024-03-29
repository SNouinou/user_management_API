package com.agentapi.api.gateway.infrastructure.data;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

import com.agentapi.api.core.domain.User;
import com.agentapi.api.core.domain.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id=?")
@FilterDef(name = "deletedUserFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedUserFilter", condition = "deleted = :isDeleted")
public class UserEntity {

	@Id
	private String id;
	
	private String firstName;
	
	private String lastName;
	
	private Date birthDate;
	
	private String city;
	
	private String country;
	
	private String avatar;
	
	private String company;
	
	private String jobPosition;
	
	private String mobile;
	
	private String username;
	
	private String email;
	
	private String password;
	
	private String role;
	
    private boolean deleted = Boolean.FALSE;
    
    private boolean enabled = Boolean.TRUE;
	
	public User toDomain() {
		return new User(
				this.id,
				this.firstName,
				this.lastName,
				this.birthDate,
				this.city,
				this.country,
				this.avatar,
				this.company,
				this.jobPosition,
				this.mobile,
				this.username,
				this.email,
				this.password,
				UserRole.valueOf(this.role),
				this.enabled
				);
	}
	
	public static UserEntity toEntity(User dto) {
		return new UserEntity(
				dto.getId(),
				dto.getFirstName(),
				dto.getLastName(),
				dto.getBirthDate(),
				dto.getCity(),
				dto.getCountry(),
				dto.getAvatar(),
				dto.getCompany(),
				dto.getJobPosition(),
				dto.getMobile(),
				dto.getUsername(),
				dto.getEmail(),
				dto.getPassword(),
				dto.getRole().toString(),
				Boolean.FALSE,
				dto.getEnabled()
				);
	}
	
}
