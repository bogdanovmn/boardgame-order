package com.github.bogdanovmn.boardgameorder.web.app.user;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.Invite;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.User;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.UserRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.UserRole;
import com.github.bogdanovmn.common.spring.jpa.EntityFactory;
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

	@Autowired
	RegistrationService(UserRepository userRepository, EntityFactory entityFactory, InviteService inviteService) {
		this.userRepository = userRepository;
		this.entityFactory = entityFactory;
		this.inviteService = inviteService;
	}

	@Transactional(rollbackFor = Exception.class)
	public void registration(UserRegistrationForm userForm, Invite invite) throws RegistrationException {
		if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
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
	}

	Invite validInvite(String code) throws RegistrationInviteException {
		Invite invite = inviteService.findByCode(code);

		if (invite == null) {
			throw new RegistrationInviteException("Инвайт бракованный");
		}
		if (invite.getInvited() != null) {
			throw new RegistrationInviteException("Инвайт уже использован");
		}
		if (invite.getExpireDate().isBefore(LocalDateTime.now())) {
			throw new RegistrationInviteException("Инвайт просрочен");
		}

		return invite;
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
