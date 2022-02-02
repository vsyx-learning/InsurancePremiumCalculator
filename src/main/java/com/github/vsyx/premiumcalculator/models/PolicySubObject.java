package com.github.vsyx.premiumcalculator.models;

import java.util.Objects;

import com.github.vsyx.premiumcalculator.enums.RiskType;

public record PolicySubObject(String name, double sumInsured, RiskType riskType) {
    public PolicySubObject {
        Objects.requireNonNull(name);
        Objects.requireNonNull(riskType);
    }
}
