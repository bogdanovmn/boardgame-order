package com.github.bogdanovmn.boardgameorder.web.app.user;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractMinVisualController;
import com.github.bogdanovmn.boardgameorder.web.app.FormErrors;
import com.github.bogdanovmn.boardgameorder.web.app.config.security.ProjectSecurityService;
import com.github.bogdanovmn.boardgameorder.web.orm.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
class RegistrationController extends AbstractMinVisualController {
	private final RegistrationService registrationService;
	private final ProjectSecurityService securityService;

	@Autowired
	public RegistrationController(RegistrationService registrationService, ProjectSecurityService securityService) {
		this.registrationService = registrationService;
		this.securityService = securityService;
	}

	@GetMapping
	ModelAndView registration(Model model) {
		model.addAttribute("userForm", new UserRegistrationForm());

		return new ModelAndView("registration");
	}

	@PostMapping
	ModelAndView registration(
		@Valid UserRegistrationForm userForm,
		BindingResult bindingResult,
		Model model
	) {
		FormErrors formErrors = new FormErrors(bindingResult);

		if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
			formErrors.add("passwordConfirm", "Пароль не совпадает");
		}
		else if (registrationService.isUserExists(userForm.getEmail())) {
			formErrors.addCustom("Пользователь с таким email уже существует");
		}
		else if (registrationService.isUserNameExists(userForm.getName())) {
			formErrors.addCustom("Пользователь с таким именем уже существует");
		}

		if (formErrors.isNotEmpty()) {
			model.addAllAttributes(formErrors.getModel());
			return new ModelAndView("registration", model.asMap());
		}

		User user = registrationService.addUser(userForm);

		securityService.login(
			user.getName(),
			user.getPasswordHash()
		);

		return new ModelAndView("redirect:/index");
	}
}
