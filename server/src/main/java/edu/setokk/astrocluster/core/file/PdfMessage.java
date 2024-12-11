package edu.setokk.astrocluster.core.file;

public record PdfMessage(byte[] bytes, String filename) implements FileMessage {}
