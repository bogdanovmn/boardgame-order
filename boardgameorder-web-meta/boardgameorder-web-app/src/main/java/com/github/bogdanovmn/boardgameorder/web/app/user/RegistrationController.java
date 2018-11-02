package com.github.bogdanovmn.boardgameorder.web.app.user;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractMinVisualController;
import com.github.bogdanovmn.boardgameorder.web.app.FormErrors;
import com.github.bogdanovmn.boardgameorder.web.app.HeadMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/registration")
class RegistrationController extends AbstractMinVisualController {
	private final RegistrationService registrationService;

	@Autowired
	public RegistrationController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}

	@GetMapping
	ModelAndView registration(Model model) {
		model.addAttribute("userForm", new UserRegistrationForm());

		return new ModelAndView("registration");
	}

	@PostMapping
	ModelAndView registration(
		UserRegistrationForm userForm,
		BindingResult bindingResult,
		Model model
	) {
		FormErrors formErrors = new FormErrors(bindingResult);

		try {
			registrationService.registration(userForm);
		}
		catch (RegistrationException e) {
			if (e.isCustomError()) {
				formErrors.addCustom(e.getMsg());
			}
			else {
				formErrors.add(e.getField(), e.getMsg());
			}
		}

		if (formErrors.isNotEmpty()) {
			model.addAllAttributes(formErrors.getModel());
			return new ModelAndView("registration", model.asMap());
		}

		return new ModelAndView("redirect:" + HeadMenu.DEFAULT_PAGE);
	}
}
