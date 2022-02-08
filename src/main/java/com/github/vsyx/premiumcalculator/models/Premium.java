package com.github.vsyx.premiumcalculator.models;

import java.math.BigDecimal;

public class Premium {
    protected BigDecimal defaultCoefficient;

    public Premium(double defaultCoefficient) {
        this.defaultCoefficient = BigDecimal.valueOf(defaultCoefficient);
    }

    public BigDecimal getCoefficient(BigDecimal _sumInsured) {
        return defaultCoefficient;
    }
}
