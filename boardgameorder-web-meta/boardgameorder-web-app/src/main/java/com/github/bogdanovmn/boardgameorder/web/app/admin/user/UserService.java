package com.github.bogdanovmn.boardgameorder.web.app.admin.user;

import com.github.bogdanovmn.boardgameorder.web.orm.common.EntityFactory;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.User;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.UserRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.UserRole;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
class UserService {
	private final UserRepository userRepository;
	private final EntityFactory entityFactory;

	UserService(UserRepository userRepository, EntityFactory entityFactory) {
		this.userRepository = userRepository;
		this.entityFactory = entityFactory;
	}

	List<UserSummary> usersSummary() {
		return userRepository.findAll().stream()
			.map(x -> new UserSummary(x))
			.collect(Collectors.toList());
	}

	void toggleRole(Integer userId, UserRole.Type roleType) {
		User user = userRepository.findOne(userId);
		if (user != null) {
			Set<UserRole> roles = user.getRoles();
			UserRole toggleRole = entityFactory.getPersistBaseEntityWithUniqueName(
				new UserRole(roleType.name())
			);

			if (!roles.remove(toggleRole)) {
				roles.add(toggleRole);
			}

			userRepository.save(user);
		}
	}
}
