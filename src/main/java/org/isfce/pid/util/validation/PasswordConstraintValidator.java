package org.isfce.pid.util.validation;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.isfce.pid.util.validation.annotation.ValidPassword;
import org.passay.CharacterRule;
import org.passay.DictionaryRule;
import org.passay.EnglishCharacterData;
import org.passay.IllegalCharacterRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.ResourceBundleMessageResolver;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;
import org.springframework.context.i18n.LocaleContextHolder;

import lombok.SneakyThrows;


public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
	// Liste des règles de validation d'un mot de passe
	private final List<Rule> rules = Arrays.asList(
			new LengthRule(6, 30),
			new CharacterRule(EnglishCharacterData.UpperCase, 1), 
			new CharacterRule(EnglishCharacterData.Digit, 1),	
			new IllegalCharacterRule(new char[] {'&', '<', '>'}),
			//new CharacterOccurrencesRule(2),
			new DictionaryRule(new WordListDictionary(
			         new ArrayWordList(new String[] { "password", "username" }))),
			new WhitespaceRule());

	@Override
	public void initialize(ValidPassword constraintAnnotation) {
		
	}

	@SneakyThrows
	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		// Spécifie le fichier de traduction en fonction de la locale
		/**
		 * Les fichiers passay_xx.properties doivent exister dans les ressources
		 */
		ResourceBundleMessageResolver resolver = new ResourceBundleMessageResolver(
				ResourceBundle.getBundle("passay", LocaleContextHolder.getLocale()));
		// Crée un validateur avec les règles et le traducteur
		PasswordValidator validator = new PasswordValidator(resolver, rules);
		// vérifie la validité du mot de passe
		RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
			return true;
		}
		// construit la liste des messages d'erreurs
		List<String> messages = validator.getMessages(result);
		String messageTemplate = String.join(",", messages);
		context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation()
				.disableDefaultConstraintViolation();
		return false;
	}

}
