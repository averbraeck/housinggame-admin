/*
 * This file is generated by jOOQ.
 */
package nl.tudelft.simulation.housinggame.data.tables.records;


import nl.tudelft.simulation.housinggame.data.tables.Scenarioparameters;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record12;
import org.jooq.Row12;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.types.UInteger;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ScenarioparametersRecord extends UpdatableRecordImpl<ScenarioparametersRecord> implements Record12<UInteger, String, Double, Double, Double, Double, Double, Double, Double, Double, Double, Double> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>housinggame.scenarioparameters.id</code>.
     */
    public void setId(UInteger value) {
        set(0, value);
    }

    /**
     * Getter for <code>housinggame.scenarioparameters.id</code>.
     */
    public UInteger getId() {
        return (UInteger) get(0);
    }

    /**
     * Setter for <code>housinggame.scenarioparameters.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>housinggame.scenarioparameters.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for
     * <code>housinggame.scenarioparameters.pluvial_repair_costs</code>.
     */
    public void setPluvialRepairCosts(Double value) {
        set(2, value);
    }

    /**
     * Getter for
     * <code>housinggame.scenarioparameters.pluvial_repair_costs</code>.
     */
    public Double getPluvialRepairCosts() {
        return (Double) get(2);
    }

    /**
     * Setter for
     * <code>housinggame.scenarioparameters.pluvial_satisfaction_penalty</code>.
     */
    public void setPluvialSatisfactionPenalty(Double value) {
        set(3, value);
    }

    /**
     * Getter for
     * <code>housinggame.scenarioparameters.pluvial_satisfaction_penalty</code>.
     */
    public Double getPluvialSatisfactionPenalty() {
        return (Double) get(3);
    }

    /**
     * Setter for
     * <code>housinggame.scenarioparameters.fluvial_repair_costs</code>.
     */
    public void setFluvialRepairCosts(Double value) {
        set(4, value);
    }

    /**
     * Getter for
     * <code>housinggame.scenarioparameters.fluvial_repair_costs</code>.
     */
    public Double getFluvialRepairCosts() {
        return (Double) get(4);
    }

    /**
     * Setter for
     * <code>housinggame.scenarioparameters.fluvial_satisfaction_penalty</code>.
     */
    public void setFluvialSatisfactionPenalty(Double value) {
        set(5, value);
    }

    /**
     * Getter for
     * <code>housinggame.scenarioparameters.fluvial_satisfaction_penalty</code>.
     */
    public Double getFluvialSatisfactionPenalty() {
        return (Double) get(5);
    }

    /**
     * Setter for
     * <code>housinggame.scenarioparameters.satisfaction_debt_penalty</code>.
     */
    public void setSatisfactionDebtPenalty(Double value) {
        set(6, value);
    }

    /**
     * Getter for
     * <code>housinggame.scenarioparameters.satisfaction_debt_penalty</code>.
     */
    public Double getSatisfactionDebtPenalty() {
        return (Double) get(6);
    }

    /**
     * Setter for
     * <code>housinggame.scenarioparameters.satisfaction_house_rating_change</code>.
     */
    public void setSatisfactionHouseRatingChange(Double value) {
        set(7, value);
    }

    /**
     * Getter for
     * <code>housinggame.scenarioparameters.satisfaction_house_rating_change</code>.
     */
    public Double getSatisfactionHouseRatingChange() {
        return (Double) get(7);
    }

    /**
     * Setter for
     * <code>housinggame.scenarioparameters.satisfaction_move_penalty</code>.
     */
    public void setSatisfactionMovePenalty(Double value) {
        set(8, value);
    }

    /**
     * Getter for
     * <code>housinggame.scenarioparameters.satisfaction_move_penalty</code>.
     */
    public Double getSatisfactionMovePenalty() {
        return (Double) get(8);
    }

    /**
     * Setter for <code>housinggame.scenarioparameters.flood_repair_cost</code>.
     */
    public void setFloodRepairCost(Double value) {
        set(9, value);
    }

    /**
     * Getter for <code>housinggame.scenarioparameters.flood_repair_cost</code>.
     */
    public Double getFloodRepairCost() {
        return (Double) get(9);
    }

    /**
     * Setter for
     * <code>housinggame.scenarioparameters.flood_satisfaction_penalty</code>.
     */
    public void setFloodSatisfactionPenalty(Double value) {
        set(10, value);
    }

    /**
     * Getter for
     * <code>housinggame.scenarioparameters.flood_satisfaction_penalty</code>.
     */
    public Double getFloodSatisfactionPenalty() {
        return (Double) get(10);
    }

    /**
     * Setter for
     * <code>housinggame.scenarioparameters.mortgage_percentage</code>.
     */
    public void setMortgagePercentage(Double value) {
        set(11, value);
    }

    /**
     * Getter for
     * <code>housinggame.scenarioparameters.mortgage_percentage</code>.
     */
    public Double getMortgagePercentage() {
        return (Double) get(11);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UInteger> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record12 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row12<UInteger, String, Double, Double, Double, Double, Double, Double, Double, Double, Double, Double> fieldsRow() {
        return (Row12) super.fieldsRow();
    }

    @Override
    public Row12<UInteger, String, Double, Double, Double, Double, Double, Double, Double, Double, Double, Double> valuesRow() {
        return (Row12) super.valuesRow();
    }

    @Override
    public Field<UInteger> field1() {
        return Scenarioparameters.SCENARIOPARAMETERS.ID;
    }

    @Override
    public Field<String> field2() {
        return Scenarioparameters.SCENARIOPARAMETERS.NAME;
    }

    @Override
    public Field<Double> field3() {
        return Scenarioparameters.SCENARIOPARAMETERS.PLUVIAL_REPAIR_COSTS;
    }

    @Override
    public Field<Double> field4() {
        return Scenarioparameters.SCENARIOPARAMETERS.PLUVIAL_SATISFACTION_PENALTY;
    }

    @Override
    public Field<Double> field5() {
        return Scenarioparameters.SCENARIOPARAMETERS.FLUVIAL_REPAIR_COSTS;
    }

    @Override
    public Field<Double> field6() {
        return Scenarioparameters.SCENARIOPARAMETERS.FLUVIAL_SATISFACTION_PENALTY;
    }

    @Override
    public Field<Double> field7() {
        return Scenarioparameters.SCENARIOPARAMETERS.SATISFACTION_DEBT_PENALTY;
    }

    @Override
    public Field<Double> field8() {
        return Scenarioparameters.SCENARIOPARAMETERS.SATISFACTION_HOUSE_RATING_CHANGE;
    }

    @Override
    public Field<Double> field9() {
        return Scenarioparameters.SCENARIOPARAMETERS.SATISFACTION_MOVE_PENALTY;
    }

    @Override
    public Field<Double> field10() {
        return Scenarioparameters.SCENARIOPARAMETERS.FLOOD_REPAIR_COST;
    }

    @Override
    public Field<Double> field11() {
        return Scenarioparameters.SCENARIOPARAMETERS.FLOOD_SATISFACTION_PENALTY;
    }

    @Override
    public Field<Double> field12() {
        return Scenarioparameters.SCENARIOPARAMETERS.MORTGAGE_PERCENTAGE;
    }

    @Override
    public UInteger component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public Double component3() {
        return getPluvialRepairCosts();
    }

    @Override
    public Double component4() {
        return getPluvialSatisfactionPenalty();
    }

    @Override
    public Double component5() {
        return getFluvialRepairCosts();
    }

    @Override
    public Double component6() {
        return getFluvialSatisfactionPenalty();
    }

    @Override
    public Double component7() {
        return getSatisfactionDebtPenalty();
    }

    @Override
    public Double component8() {
        return getSatisfactionHouseRatingChange();
    }

    @Override
    public Double component9() {
        return getSatisfactionMovePenalty();
    }

    @Override
    public Double component10() {
        return getFloodRepairCost();
    }

    @Override
    public Double component11() {
        return getFloodSatisfactionPenalty();
    }

    @Override
    public Double component12() {
        return getMortgagePercentage();
    }

    @Override
    public UInteger value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public Double value3() {
        return getPluvialRepairCosts();
    }

    @Override
    public Double value4() {
        return getPluvialSatisfactionPenalty();
    }

    @Override
    public Double value5() {
        return getFluvialRepairCosts();
    }

    @Override
    public Double value6() {
        return getFluvialSatisfactionPenalty();
    }

    @Override
    public Double value7() {
        return getSatisfactionDebtPenalty();
    }

    @Override
    public Double value8() {
        return getSatisfactionHouseRatingChange();
    }

    @Override
    public Double value9() {
        return getSatisfactionMovePenalty();
    }

    @Override
    public Double value10() {
        return getFloodRepairCost();
    }

    @Override
    public Double value11() {
        return getFloodSatisfactionPenalty();
    }

    @Override
    public Double value12() {
        return getMortgagePercentage();
    }

    @Override
    public ScenarioparametersRecord value1(UInteger value) {
        setId(value);
        return this;
    }

    @Override
    public ScenarioparametersRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public ScenarioparametersRecord value3(Double value) {
        setPluvialRepairCosts(value);
        return this;
    }

    @Override
    public ScenarioparametersRecord value4(Double value) {
        setPluvialSatisfactionPenalty(value);
        return this;
    }

    @Override
    public ScenarioparametersRecord value5(Double value) {
        setFluvialRepairCosts(value);
        return this;
    }

    @Override
    public ScenarioparametersRecord value6(Double value) {
        setFluvialSatisfactionPenalty(value);
        return this;
    }

    @Override
    public ScenarioparametersRecord value7(Double value) {
        setSatisfactionDebtPenalty(value);
        return this;
    }

    @Override
    public ScenarioparametersRecord value8(Double value) {
        setSatisfactionHouseRatingChange(value);
        return this;
    }

    @Override
    public ScenarioparametersRecord value9(Double value) {
        setSatisfactionMovePenalty(value);
        return this;
    }

    @Override
    public ScenarioparametersRecord value10(Double value) {
        setFloodRepairCost(value);
        return this;
    }

    @Override
    public ScenarioparametersRecord value11(Double value) {
        setFloodSatisfactionPenalty(value);
        return this;
    }

    @Override
    public ScenarioparametersRecord value12(Double value) {
        setMortgagePercentage(value);
        return this;
    }

    @Override
    public ScenarioparametersRecord values(UInteger value1, String value2, Double value3, Double value4, Double value5, Double value6, Double value7, Double value8, Double value9, Double value10, Double value11, Double value12) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ScenarioparametersRecord
     */
    public ScenarioparametersRecord() {
        super(Scenarioparameters.SCENARIOPARAMETERS);
    }

    /**
     * Create a detached, initialised ScenarioparametersRecord
     */
    public ScenarioparametersRecord(UInteger id, String name, Double pluvialRepairCosts, Double pluvialSatisfactionPenalty, Double fluvialRepairCosts, Double fluvialSatisfactionPenalty, Double satisfactionDebtPenalty, Double satisfactionHouseRatingChange, Double satisfactionMovePenalty, Double floodRepairCost, Double floodSatisfactionPenalty, Double mortgagePercentage) {
        super(Scenarioparameters.SCENARIOPARAMETERS);

        setId(id);
        setName(name);
        setPluvialRepairCosts(pluvialRepairCosts);
        setPluvialSatisfactionPenalty(pluvialSatisfactionPenalty);
        setFluvialRepairCosts(fluvialRepairCosts);
        setFluvialSatisfactionPenalty(fluvialSatisfactionPenalty);
        setSatisfactionDebtPenalty(satisfactionDebtPenalty);
        setSatisfactionHouseRatingChange(satisfactionHouseRatingChange);
        setSatisfactionMovePenalty(satisfactionMovePenalty);
        setFloodRepairCost(floodRepairCost);
        setFloodSatisfactionPenalty(floodSatisfactionPenalty);
        setMortgagePercentage(mortgagePercentage);
        resetChangedOnNotNull();
    }
}
