package com.github.bogdanovmn.boardgameorder.web.app.user;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractMinVisualController;
import com.github.bogdanovmn.boardgameorder.web.app.FormErrors;
import com.github.bogdanovmn.boardgameorder.web.app.HeadMenu;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.Invite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


@Controller
@RequestMapping("/registration")
class RegistrationController extends AbstractMinVisualController {
    private final RegistrationService registrationService;

    @Autowired
    RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/{inviteCode}")
    ModelAndView registration(Model model, @PathVariable String inviteCode) {
        try {
            Invite invite = registrationService.validInvite(inviteCode);
            model.addAttribute("invite", invite);
        } catch (RegistrationInviteException e) {
            model.addAttribute("inviteError", true);
            model.addAttribute("customError", e.getMsg());
        }

        return new ModelAndView("registration", model.asMap());
    }

    @PostMapping
    ModelAndView registration(
        @Valid UserRegistrationForm userForm,
        BindingResult bindingResult,
        Model model
    ) {
        FormErrors formErrors = new FormErrors(bindingResult);
        Invite invite = null;
        try {
            invite = registrationService.validInvite(
                userForm.getInviteCode()
            );
            if (!formErrors.isNotEmpty()) {
                registrationService.registration(userForm, invite);
            }
        } catch (RegistrationException e) {
            if (e.isCustomError()) {
                formErrors.addCustom(e.getMsg());
            } else {
                formErrors.add(e.getField(), e.getMsg());
            }
        }

        if (formErrors.isNotEmpty()) {
            model.addAllAttributes(formErrors.getModel());
            model.addAttribute("invite", invite);
            return new ModelAndView("registration", model.asMap());
        }

        return new ModelAndView("redirect:" + HeadMenu.DEFAULT_PAGE);
    }
}
