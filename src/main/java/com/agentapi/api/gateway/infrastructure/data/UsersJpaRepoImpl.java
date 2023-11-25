package com.agentapi.api.gateway.infrastructure.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.agentapi.api.core.domain.BatchFeedback;
import com.agentapi.api.core.domain.User;
import com.agentapi.api.core.domain.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Repository
//renamed due to circular dep issue
public class UsersJpaRepoImpl implements UserRepository {

	private UsersJpaRepository jpaRepository;

	@Override
	public BatchFeedback saveAll(List<User> userList) {

		List<UserEntity> entities = userList.stream().map(user -> UserEntity.toEntity(user))
				.collect(Collectors.toList());

		List<UserEntity> savedEntries = jpaRepository.saveAll(entities);

		return new BatchFeedback(userList.size(), savedEntries.size(), userList.size() - savedEntries.size());
	}

	@Override
	public User findUserByUsernameOrEmail(String login) {

		UserEntity user = jpaRepository.findByUsernameOrEmail(login);

		if (user == null) {
			return null;
		} else {
			return user.toDomain();
		}
	}

	@Override
	public List<User> findAll() {

		List<UserEntity> userEntityList = jpaRepository.findAll();

		try {
			Stream<User> userList = userEntityList.stream().<User>map((x) -> {
				return x.toDomain();
			});

			return userList.collect(Collectors.toList());

		} catch (ClassCastException ex) {
			ex.printStackTrace();
			return new ArrayList<User>();
		}
	}

	@Override
	public Page<User> getPage(int page, int size, String username) {

		Pageable pageReq = PageRequest.of(page - 1, size);

		Page<UserEntity> userEntityList;

		if (username != null) {
			userEntityList = jpaRepository.findByUsername(pageReq, username);
		} else {
			userEntityList = jpaRepository.findAll(pageReq);
		}

		try {
			return userEntityList.<User>map((x) -> {return x.toDomain();});
		} catch (ClassCastException ex) {
			ex.printStackTrace();
			return Page.<User>empty();
		}
	}

}
