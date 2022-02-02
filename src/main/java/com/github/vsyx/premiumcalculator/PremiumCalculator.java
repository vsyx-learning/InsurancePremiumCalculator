package com.github.vsyx.premiumcalculator;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.vsyx.premiumcalculator.enums.RiskType;
import com.github.vsyx.premiumcalculator.models.Policy;
import com.github.vsyx.premiumcalculator.models.PolicyObject;
import com.github.vsyx.premiumcalculator.models.PolicySubObject;
import com.github.vsyx.premiumcalculator.models.Premium;

public class PremiumCalculator
{
    private static final Map<RiskType, Premium> premiumCoefficientLookupTable = Map.ofEntries(
        Map.entry(RiskType.FIRE, new Premium(0.014) {
            @Override
            public double getCoefficient(double sumInsured) {
                return sumInsured > 100 ? 0.024 : this.defaultCoefficient;
            }
        }),
        Map.entry(RiskType.THEFT, new Premium(0.11) {
            @Override
            public double getCoefficient(double sumInsured) {
                return sumInsured >= 15 ? 0.05 : this.defaultCoefficient;
            }
        })
    );

    public double calculate(Policy policy) {
        return policy.policyObjects().stream()
            .mapToDouble(p -> calculatePolicyObject(p))
            .sum();
    }

    private double calculatePolicyObject(PolicyObject policyObject) {
        return policyObject.subObjectPolicies().stream()
            .collect(Collectors.groupingBy(PolicySubObject::riskType))
            .entrySet().stream()
            .mapToDouble(e -> calculatePremium(e.getKey(), e.getValue()))
            .sum();
    }

    private double calculatePremium(RiskType riskType, Collection<PolicySubObject> subObjectPolicies) {
        final var totalSumInsured = subObjectPolicies.stream()
            .mapToDouble(PolicySubObject::sumInsured)
            .sum();

        return PremiumCalculator.premiumCoefficientLookupTable.get(riskType)
            .getCoefficient(totalSumInsured) * totalSumInsured;
    }

    /*public static void main( String[] args )
    {
    }*/
}
