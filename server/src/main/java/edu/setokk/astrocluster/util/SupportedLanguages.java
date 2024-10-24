package edu.setokk.astrocluster.util;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;

@Getter
public enum SupportedLanguages {
	JAVA("java", Arrays.asList("java"));
	
	private final String lang;
	private final List<String> basicExtensions;
	
	public SupportedLanguages(String lang, List<String> basicExtensions) {
		this.lang = lang;
		this.basicExtensions = basicExtensions;
	}
	
	public static Optional<SupportedLanguages> get(String lang) {
		Arrays.stream(values())
			.filter(l -> lang.equals(l.getLang()))
			.findAny()
			.orElse(Optional.empty())
	}
}