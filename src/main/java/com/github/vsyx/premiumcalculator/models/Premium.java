package com.github.vsyx.premiumcalculator.models;

public class Premium {
    protected double defaultCoefficient;

    public Premium(double defaultCoefficient) {
        this.defaultCoefficient = defaultCoefficient;
    }

    public double getCoefficient(double sumInsured) {
        return defaultCoefficient;
    }
}
