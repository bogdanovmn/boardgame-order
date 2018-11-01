package com.github.bogdanovmn.boardgameorder.web.app.admin.user;

import com.github.bogdanovmn.boardgameorder.web.orm.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
class UserService {
	private final UserRepository userRepository;

	UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	List<UserSummary> usersSummary() {
		return userRepository.findAll().stream()
			.map(x -> new UserSummary(x))
			.collect(Collectors.toList());
	}
}
