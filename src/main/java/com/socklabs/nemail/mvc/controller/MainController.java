package com.socklabs.nemail.mvc.controller;

import com.google.common.base.Optional;
import com.socklabs.nemail.Email;
import com.socklabs.nemail.EmailDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
			@RequestParam(value = "from", required = false, defaultValue = "") final String from,
			@RequestParam(value = "subject", required = false, defaultValue = "") final String subject
			) {

		if (!to.equals("")) {
			model.addAttribute("emails", emailDao.findByReceiver(to));
		} else if (!from.equals("")) {
			model.addAttribute("emails", emailDao.findBySender(from));
		} else if (!subject.equals("")) {
			model.addAttribute("emails", emailDao.findBySubject(subject));
		} else {
			model.addAttribute("emails", emailDao.listRecent());
		}

		return "index";
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public String viewMessage(@RequestParam(value = "messageId") final String messageId) {
		Optional<Email> emailOptional = emailDao.findById(messageId);
		if (emailOptional.isPresent()) {
			return "<html><body><pre>" + emailOptional.get().getRawMessage() + "</pre></body></html>";
		}
		return "<html><body><h1>Message not found</h1></body></html>";
	}

}
