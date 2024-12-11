package edu.setokk.astrocluster.core.file;

public record CsvMessage(byte[] bytes, String filename) implements FileMessage {}
