package com.github.vsyx.premiumcalculator.models;

import java.util.Collection;
import java.util.Objects;

import com.github.vsyx.premiumcalculator.enums.PolicyStatus;

public record Policy(String number, PolicyStatus status, Collection<PolicyObject> policyObjects) {
    public Policy {
        Objects.requireNonNull(number);
        Objects.requireNonNull(status);
        Objects.requireNonNull(policyObjects);

        if (policyObjects.isEmpty()) {
            throw new IllegalArgumentException("A policy must have at least 1 policy object");
        }
    }
}
