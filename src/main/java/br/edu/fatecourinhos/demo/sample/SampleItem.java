package br.edu.fatecourinhos.demo.sample;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SampleItem(Long id, LocalDate someDate, BigDecimal someValue) {
}
