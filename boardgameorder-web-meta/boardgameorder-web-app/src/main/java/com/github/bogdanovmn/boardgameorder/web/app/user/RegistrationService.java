package com.github.bogdanovmn.boardgameorder.web.app.user;

import com.github.bogdanovmn.boardgameorder.web.app.config.security.ProjectSecurityService;
import com.github.bogdanovmn.boardgameorder.web.orm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;

@Service
class RegistrationService {
	private final UserRepository userRepository;
	private final EntityFactory entityFactory;
	private final InviteService inviteService;
	private final ProjectSecurityService securityService;



	@Autowired
	RegistrationService(UserRepository userRepository, EntityFactory entityFactory, InviteService inviteService, ProjectSecurityService securityService) {
		this.userRepository = userRepository;
		this.entityFactory = entityFactory;
		this.inviteService = inviteService;
		this.securityService = securityService;
	}

	@Transactional(rollbackFor = Exception.class)
	public void registration(UserRegistrationForm userForm) throws RegistrationException {
		Invite invite = inviteService.findByCode(userForm.getInviteCode());

		if (invite == null) {
			throw new RegistrationException("inviteCode", "Инвайт бракованный");
		}
		if (invite.getInvited() != null) {
			throw new RegistrationException("inviteCode", "Инвайт уже использован");
		}
		if (invite.getExpireDate().isBefore(LocalDateTime.now())) {
			throw new RegistrationException("inviteCode", "Инвайт просрочен");
		}
		else if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
			throw new RegistrationException("passwordConfirm", "Пароль не совпадает");
		}
		else if (isUserExists(userForm.getEmail())) {
			throw new RegistrationException("Пользователь с таким email уже существует");
		}
		else if (isUserNameExists(userForm.getName())) {
			throw new RegistrationException("Пользователь с таким именем уже существует");
		}

		User user = addUser(userForm);
		invite.setInvited(user);

		securityService.login(
			user.getName(),
			user.getPasswordHash()
		);
	}

	private User addUser(UserRegistrationForm userForm) {
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
							entityFactory.getPersistBaseEntityWithUniqueName(
								new UserRole("User")
							)
						);
					}}
				)
		);
	}

	private boolean isUserExists(String email) {
		return userRepository.findFirstByEmail(email) != null;
	}

	private boolean isUserNameExists(String name) {
		return userRepository.findFirstByName(name) != null;
	}
}
