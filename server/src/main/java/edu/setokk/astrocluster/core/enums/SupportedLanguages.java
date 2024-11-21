package edu.setokk.astrocluster.core.enums;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public enum SupportedLanguages {
	JAVA("java", Arrays.asList("java"));
	
	private final String lang;
	private final List<String> basicExtensions;
	
	SupportedLanguages(String lang, List<String> basicExtensions) {
		this.lang = lang;
		this.basicExtensions = basicExtensions;
	}
	
	public static Optional<SupportedLanguages> get(@NonNull String lang) {
		return Arrays.stream(values())
				.filter(l -> lang.equals(l.getLang()))
                .findAny();
	}
}