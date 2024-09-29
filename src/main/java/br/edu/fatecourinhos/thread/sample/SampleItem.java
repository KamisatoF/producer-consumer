package br.edu.fatecourinhos.thread.sample;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SampleItem(Long id, LocalDate someDate, BigDecimal someValue) {
}
