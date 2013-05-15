package com.socklabs.nemail.mvc.controller;

import com.google.common.base.Optional;
import com.socklabs.nemail.Email;
import com.socklabs.nemail.EmailDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping("/")
public class MainController {

	@Resource
	private EmailDao emailDao;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String displayIndex(
			final Model model,
			@RequestParam(value = "to", required = false, defaultValue = "") final String to,
			@RequestParam(value = "from", required = false, defaultValue = "") final String from
			) {

		if (!to.equals("")) {
			model.addAttribute("emails", emailDao.findByReceiver(to));
		} else if (!from.equals("")) {
			model.addAttribute("emails", emailDao.findBySender(from));
		} else {
			model.addAttribute("emails", emailDao.listRecent());
		}

		return "index";
	}

	@RequestMapping(value = "/{messageId}")
	public String viewMessage(final Model model, @PathVariable(value = "messageId") final String messageId) {
		model.addAttribute("emailOptional", emailDao.findById(messageId));
		return "message";
	}

}
