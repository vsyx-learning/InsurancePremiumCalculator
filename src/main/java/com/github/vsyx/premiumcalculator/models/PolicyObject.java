package com.github.vsyx.premiumcalculator.models;

import java.util.Collection;
import java.util.Objects;

public record PolicyObject(String name, Collection<PolicySubObject> subObjectPolicies) {
    public PolicyObject {
        Objects.requireNonNull(name);
        Objects.requireNonNull(subObjectPolicies);
    }
}
