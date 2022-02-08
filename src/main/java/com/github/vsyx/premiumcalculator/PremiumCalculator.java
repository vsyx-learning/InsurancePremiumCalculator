package com.github.vsyx.premiumcalculator;

import java.math.BigDecimal;
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
    private static final Map<RiskType, Premium> RISK_COEFFICIENT_LOOKUP_TABLE = Map.ofEntries(
        Map.entry(RiskType.FIRE, new Premium(0.014) {
            private final BigDecimal increasedCoefficientTreshold = BigDecimal.valueOf(100);
            private final BigDecimal increasedCoefficient = BigDecimal.valueOf(0.024);

            @Override
            public BigDecimal getCoefficient(BigDecimal sumInsured) {
                return sumInsured.compareTo(increasedCoefficientTreshold) > 0
                ? increasedCoefficient : this.defaultCoefficient;
            }
        }),
        Map.entry(RiskType.THEFT, new Premium(0.11) {
            private final BigDecimal decreasedCoefficientTreshold = BigDecimal.valueOf(15);
            private final BigDecimal decreasedCoefficient = BigDecimal.valueOf(0.05);

            @Override
            public BigDecimal getCoefficient(BigDecimal sumInsured) {
                return sumInsured.compareTo(decreasedCoefficientTreshold) >= 0
                ? decreasedCoefficient : this.defaultCoefficient;
            }
        })
    );

    public BigDecimal calculate(Policy policy) {
        return policy.policyObjects().stream()
            .map(p -> calculatePolicyObject(p))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculatePolicyObject(PolicyObject policyObject) {
        return policyObject.subObjectPolicies().stream()
            .collect(Collectors.groupingBy(PolicySubObject::riskType))
            .entrySet().stream()
            .map(e -> calculatePremium(e.getKey(), e.getValue()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculatePremium(RiskType riskType, Collection<PolicySubObject> subObjectPolicies) {
        final var totalSumInsured = subObjectPolicies.stream()
            .map(PolicySubObject::sumInsured)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return PremiumCalculator.RISK_COEFFICIENT_LOOKUP_TABLE.get(riskType)
            .getCoefficient(totalSumInsured)
            .multiply(totalSumInsured);
    }


    /*public static void main( String[] args )
    {
    }*/
}
