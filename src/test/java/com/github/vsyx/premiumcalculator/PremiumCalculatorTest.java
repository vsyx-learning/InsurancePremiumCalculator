package com.github.vsyx.premiumcalculator;

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.github.vsyx.premiumcalculator.enums.*;
import com.github.vsyx.premiumcalculator.models.*;

import org.junit.Test;

public class PremiumCalculatorTest
{
    private static final double DOUBLE_EPSILON = 1e-5;

    private static final PolicyObject POLICY_OBJECT_1 = new PolicyObject("test", List.of(
                new PolicySubObject("TV", 500, RiskType.FIRE),
                new PolicySubObject("TV", 102.51, RiskType.THEFT)
            ));

    private static final PolicyObject POLICY_OBJECT_2 = new PolicyObject("test", List.of(
                new PolicySubObject("TV", 100, RiskType.FIRE),
                new PolicySubObject("TV", 8, RiskType.THEFT)
            ));

    private static final PolicyObject EMPTY_POLICY_OBJECT = new PolicyObject("test", List.of());

    private static final double POLICY_OBJECT_1_PREMIUM = (500 * 0.024) + (102.51 * 0.05); 
    private static final double POLICY_OBJECT_2_PREMIUM = (100 * 0.014) + (8 * 0.11);

    private final PremiumCalculator calculator = new PremiumCalculator();

    @Test
    public void testPolicyObject1() {
        final var policy = new Policy("LV20-02-100000-5", PolicyStatus.APPROVED, List.of(POLICY_OBJECT_1));

        assertEquals(
                POLICY_OBJECT_1_PREMIUM, 
                calculator.calculate(policy),
                DOUBLE_EPSILON);
    }

    @Test
    public void testPolicyObject2() {
        final var policy = new Policy("LV20-02-100000-5", PolicyStatus.APPROVED, List.of(POLICY_OBJECT_2));
        assertEquals(
                POLICY_OBJECT_2_PREMIUM, 
                calculator.calculate(policy),
                DOUBLE_EPSILON);
    }

    @Test
    public void testPolicyObjectCombined() {
        final var policy = new Policy("LV20-02-100000-5", PolicyStatus.APPROVED, List.of(POLICY_OBJECT_1, POLICY_OBJECT_2));

        assertEquals(
                POLICY_OBJECT_1_PREMIUM + POLICY_OBJECT_2_PREMIUM, 
                calculator.calculate(policy),
                DOUBLE_EPSILON);
    }

    @Test
    public void testEmptyPolicyObject() {
        final var policy = new Policy("LV20-02-100000-5", PolicyStatus.APPROVED, List.of(EMPTY_POLICY_OBJECT));

        assertEquals(0, calculator.calculate(policy), DOUBLE_EPSILON);
    }

    @Test
    public void testMultipleSubobjectPolicies() {
        final double expected = (500 + 41) * 0.024 + (102.51 + 25.1) * 0.05;

        var policyObject = new PolicyObject("test", List.of(
            new PolicySubObject("TV", 500, RiskType.FIRE),
            new PolicySubObject("TV", 41, RiskType.FIRE),
            new PolicySubObject("TV", 102.51, RiskType.THEFT),
            new PolicySubObject("TV", 25.1, RiskType.THEFT)
        ));

        final var policy = new Policy("LV20-02-100000-5", PolicyStatus.APPROVED, List.of(policyObject));

        assertEquals(expected, calculator.calculate(policy), DOUBLE_EPSILON);
    }

    @Test
    public void testMultipleSubobjectPoliciesMixed() {
        final double expected = (124 + 41) * 0.024 + (8 + 6) * 0.11;

        var policyObject = new PolicyObject("test", List.of(
            new PolicySubObject("TV", 124, RiskType.FIRE),
            new PolicySubObject("TV", 41, RiskType.FIRE),
            new PolicySubObject("TV", 8, RiskType.THEFT),
            new PolicySubObject("TV", 6, RiskType.THEFT)
        ));

        final var policy = new Policy("LV20-02-100000-5", PolicyStatus.APPROVED, List.of(policyObject));

        assertEquals(expected, calculator.calculate(policy), DOUBLE_EPSILON);
    }
}
