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
            @Override
            public BigDecimal getCoefficient(BigDecimal sumInsured) {
                return sumInsured.compareTo(BigDecimal.valueOf(100)) > 0 ? BigDecimal.valueOf(0.024) : this.defaultCoefficient;
            }
        }),
        Map.entry(RiskType.THEFT, new Premium(0.11) {
            @Override
            public BigDecimal getCoefficient(BigDecimal sumInsured) {
                return sumInsured.compareTo(BigDecimal.valueOf(15)) >= 0 ? BigDecimal.valueOf(0.05) : this.defaultCoefficient;
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
