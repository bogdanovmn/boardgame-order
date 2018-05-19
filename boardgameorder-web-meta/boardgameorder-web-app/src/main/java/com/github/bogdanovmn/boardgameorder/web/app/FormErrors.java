package com.github.bogdanovmn.boardgameorder.web.app;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FormErrors {
	private final Map<String, String> customFormErrors = new HashMap<>();
	private final List<String> customErrors = new ArrayList<>();
	private final BindingResult bindingResult;

	public FormErrors(BindingResult bindingResult){
		this.bindingResult = bindingResult;
	}

	public Map<String, Object> getModel() {
		return new HashMap<String, Object>() {{
			put(
				"formError",
				new HashMap<String, Object>() {{
					putAll(
						bindingResult.getFieldErrors().stream()
							.collect(
								Collectors.toMap(
									FieldError::getField,
									FieldError::getDefaultMessage)
							)
					);
					putAll(customFormErrors);
				}}
			);

			put("customError", customErrors);
		}};
	}

	public void addCustom(String errorMsg) {
		this.customErrors.add(errorMsg);
	}

	public void add(String key, String errorMsg) {
		this.customFormErrors.put(key, errorMsg);
	}

	public boolean isNotEmpty() {
		return
			!this.customErrors.isEmpty()
			|| !this.customFormErrors.isEmpty()
			|| this.bindingResult.hasErrors();
	}
}
