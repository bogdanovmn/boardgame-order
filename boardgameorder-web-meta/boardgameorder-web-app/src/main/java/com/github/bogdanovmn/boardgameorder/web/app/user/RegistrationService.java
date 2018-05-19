package com.github.bogdanovmn.boardgameorder.web.app.user;

import com.github.bogdanovmn.boardgameorder.web.orm.EntityFactory;
import com.github.bogdanovmn.boardgameorder.web.orm.User;
import com.github.bogdanovmn.boardgameorder.web.orm.UserRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashSet;

@Service
class RegistrationService {
	private final UserRepository userRepository;
	private final EntityFactory entityFactory;

	@Autowired
	public RegistrationService(UserRepository userRepository, EntityFactory entityFactory) {
		this.userRepository = userRepository;
		this.entityFactory = entityFactory;
	}

	User addUser(UserRegistrationForm userForm) {
		return userRepository.save(
			new User(userForm.getName())
				.setEmail(
					userForm.getEmail()
				)
				.setPasswordHash(
					DigestUtils.md5DigestAsHex(
						userForm.getPassword().getBytes()
					)
				)
				.setRegisterDate(new Date())
				.setRoles(
					new HashSet<UserRole>() {{
						add(
							(UserRole) entityFactory.getPersistBaseEntityWithUniqueName(
								new UserRole("User")
							)
						);
					}}
				)
		);
	}

	boolean isUserExists(String email) {
		return userRepository.findFirstByEmail(email) != null;
	}

	boolean isUserNameExists(String name) {
		return userRepository.findFirstByName(name) != null;
	}
}
