package com.github.vsyx.premiumcalculator.models;

import java.math.BigDecimal;
import java.util.Objects;

import com.github.vsyx.premiumcalculator.enums.RiskType;

public record PolicySubObject(String name, BigDecimal sumInsured, RiskType riskType) {
    public PolicySubObject {
        Objects.requireNonNull(name);
        Objects.requireNonNull(sumInsured);
        Objects.requireNonNull(riskType);
    }
}
