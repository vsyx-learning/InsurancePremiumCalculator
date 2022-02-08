package com.github.vsyx.premiumcalculator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.github.vsyx.premiumcalculator.enums.*;
import com.github.vsyx.premiumcalculator.models.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PremiumCalculatorTest
{
    private static final int BIGDECIMAL_SCALE = 5;

    private static final PolicyObject POLICY_OBJECT_1 = new PolicyObject("test", List.of(
                new PolicySubObject("TV", BigDecimal.valueOf(500), RiskType.FIRE),
                new PolicySubObject("TV", BigDecimal.valueOf(102.51), RiskType.THEFT)
            ));

    private static final PolicyObject POLICY_OBJECT_2 = new PolicyObject("test", List.of(
                new PolicySubObject("TV", BigDecimal.valueOf(100), RiskType.FIRE),
                new PolicySubObject("TV", BigDecimal.valueOf(8), RiskType.THEFT)
            ));

    private static final PolicyObject EMPTY_POLICY_OBJECT = new PolicyObject("test", List.of());

    private static final BigDecimal POLICY_OBJECT_1_PREMIUM = BigDecimal.valueOf((500 * 0.024) + (102.51 * 0.05)); 
    private static final BigDecimal POLICY_OBJECT_2_PREMIUM = BigDecimal.valueOf((100 * 0.014) + (8 * 0.11));

    private final PremiumCalculator calculator = new PremiumCalculator();

    @Test
    public void testPolicyObject1() {
        final var policy = new Policy("LV20-02-100000-5", PolicyStatus.APPROVED, List.of(POLICY_OBJECT_1));

        assertBigDecimalEqualsWithScale(POLICY_OBJECT_1_PREMIUM, calculator.calculate(policy));
    }

    @Test
    public void testPolicyObject2() {
        final var policy = new Policy("LV20-02-100000-5", PolicyStatus.APPROVED, List.of(POLICY_OBJECT_2));

        assertBigDecimalEqualsWithScale(POLICY_OBJECT_2_PREMIUM, calculator.calculate(policy));
    }

    @Test
    public void testPolicyObjectCombined() {
        final var policy = new Policy("LV20-02-100000-5", PolicyStatus.APPROVED, List.of(POLICY_OBJECT_1, POLICY_OBJECT_2));
        final var combinedPremium = POLICY_OBJECT_1_PREMIUM.add(POLICY_OBJECT_2_PREMIUM);

        assertBigDecimalEqualsWithScale(combinedPremium, calculator.calculate(policy));
    }

    @Test
    public void testEmptyPolicyObject() {
        final var policy = new Policy("LV20-02-100000-5", PolicyStatus.APPROVED, List.of(EMPTY_POLICY_OBJECT));

        assertBigDecimalEqualsWithScale(BigDecimal.ZERO, calculator.calculate(policy));
    }

    @Test
    public void testMultipleSubobjectPolicies() {
        final var expected = BigDecimal.valueOf((500 + 41) * 0.024 + (102.51 + 25.1) * 0.05);

        var policyObject = new PolicyObject("test", List.of(
            new PolicySubObject("TV", BigDecimal.valueOf(500), RiskType.FIRE),
            new PolicySubObject("TV", BigDecimal.valueOf(41), RiskType.FIRE),
            new PolicySubObject("TV", BigDecimal.valueOf(102.51), RiskType.THEFT),
            new PolicySubObject("TV", BigDecimal.valueOf(25.1), RiskType.THEFT)
        ));

        final var policy = new Policy("LV20-02-100000-5", PolicyStatus.APPROVED, List.of(policyObject));

        assertBigDecimalEqualsWithScale(expected, calculator.calculate(policy));
    }

    @Test
    public void testMultipleSubobjectPoliciesMixed() {
        final var expected = BigDecimal.valueOf((124 + 41) * 0.024 + (8 + 6) * 0.11);

        var policyObject = new PolicyObject("test", List.of(
            new PolicySubObject("TV", BigDecimal.valueOf(124), RiskType.FIRE),
            new PolicySubObject("TV", BigDecimal.valueOf(41), RiskType.FIRE),
            new PolicySubObject("TV", BigDecimal.valueOf(8), RiskType.THEFT),
            new PolicySubObject("TV", BigDecimal.valueOf(6), RiskType.THEFT)
        ));

        final var policy = new Policy("LV20-02-100000-5", PolicyStatus.APPROVED, List.of(policyObject));

        assertBigDecimalEqualsWithScale(expected, calculator.calculate(policy));
    }

    private void assertBigDecimalEqualsWithScale(BigDecimal a, BigDecimal b) {
        assertTrue(scaleBigDecimal(a).compareTo(scaleBigDecimal(b)) == 0);
    }

    private BigDecimal scaleBigDecimal(BigDecimal a) {
        return a.setScale(BIGDECIMAL_SCALE, RoundingMode.HALF_EVEN);
    }
}
