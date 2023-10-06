/*
 * This file is generated by jOOQ.
 */
package nl.tudelft.simulation.housinggame.data.tables.records;


import nl.tudelft.simulation.housinggame.data.tables.Playerround;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.types.UInteger;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PlayerroundRecord extends UpdatableRecordImpl<PlayerroundRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>housinggame.playerround.id</code>.
     */
    public void setId(UInteger value) {
        set(0, value);
    }

    /**
     * Getter for <code>housinggame.playerround.id</code>.
     */
    public UInteger getId() {
        return (UInteger) get(0);
    }

    /**
     * Setter for <code>housinggame.playerround.satisfaction</code>.
     */
    public void setSatisfaction(UInteger value) {
        set(1, value);
    }

    /**
     * Getter for <code>housinggame.playerround.satisfaction</code>.
     */
    public UInteger getSatisfaction() {
        return (UInteger) get(1);
    }

    /**
     * Setter for <code>housinggame.playerround.saving</code>.
     */
    public void setSaving(UInteger value) {
        set(2, value);
    }

    /**
     * Getter for <code>housinggame.playerround.saving</code>.
     */
    public UInteger getSaving() {
        return (UInteger) get(2);
    }

    /**
     * Setter for <code>housinggame.playerround.mortgage</code>.
     */
    public void setMortgage(UInteger value) {
        set(3, value);
    }

    /**
     * Getter for <code>housinggame.playerround.mortgage</code>.
     */
    public UInteger getMortgage() {
        return (UInteger) get(3);
    }

    /**
     * Setter for <code>housinggame.playerround.living_costs</code>.
     */
    public void setLivingCosts(UInteger value) {
        set(4, value);
    }

    /**
     * Getter for <code>housinggame.playerround.living_costs</code>.
     */
    public UInteger getLivingCosts() {
        return (UInteger) get(4);
    }

    /**
     * Setter for <code>housinggame.playerround.income</code>.
     */
    public void setIncome(UInteger value) {
        set(5, value);
    }

    /**
     * Getter for <code>housinggame.playerround.income</code>.
     */
    public UInteger getIncome() {
        return (UInteger) get(5);
    }

    /**
     * Setter for <code>housinggame.playerround.debt</code>.
     */
    public void setDebt(UInteger value) {
        set(6, value);
    }

    /**
     * Getter for <code>housinggame.playerround.debt</code>.
     */
    public UInteger getDebt() {
        return (UInteger) get(6);
    }

    /**
     * Setter for <code>housinggame.playerround.current_wealth</code>.
     */
    public void setCurrentWealth(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>housinggame.playerround.current_wealth</code>.
     */
    public Integer getCurrentWealth() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>housinggame.playerround.preferred_house_rating</code>.
     */
    public void setPreferredHouseRating(UInteger value) {
        set(8, value);
    }

    /**
     * Getter for <code>housinggame.playerround.preferred_house_rating</code>.
     */
    public UInteger getPreferredHouseRating() {
        return (UInteger) get(8);
    }

    /**
     * Setter for
     * <code>housinggame.playerround.satisfaction_cost_per_point</code>.
     */
    public void setSatisfactionCostPerPoint(UInteger value) {
        set(9, value);
    }

    /**
     * Getter for
     * <code>housinggame.playerround.satisfaction_cost_per_point</code>.
     */
    public UInteger getSatisfactionCostPerPoint() {
        return (UInteger) get(9);
    }

    /**
     * Setter for <code>housinggame.playerround.house_price_sold</code>.
     */
    public void setHousePriceSold(UInteger value) {
        set(10, value);
    }

    /**
     * Getter for <code>housinggame.playerround.house_price_sold</code>.
     */
    public UInteger getHousePriceSold() {
        return (UInteger) get(10);
    }

    /**
     * Setter for <code>housinggame.playerround.house_price_bought</code>.
     */
    public void setHousePriceBought(UInteger value) {
        set(11, value);
    }

    /**
     * Getter for <code>housinggame.playerround.house_price_bought</code>.
     */
    public UInteger getHousePriceBought() {
        return (UInteger) get(11);
    }

    /**
     * Setter for
     * <code>housinggame.playerround.spent_savings_for_buying_house</code>.
     */
    public void setSpentSavingsForBuyingHouse(UInteger value) {
        set(12, value);
    }

    /**
     * Getter for
     * <code>housinggame.playerround.spent_savings_for_buying_house</code>.
     */
    public UInteger getSpentSavingsForBuyingHouse() {
        return (UInteger) get(12);
    }

    /**
     * Setter for <code>housinggame.playerround.paid_off_debt</code>.
     */
    public void setPaidOffDebt(UInteger value) {
        set(13, value);
    }

    /**
     * Getter for <code>housinggame.playerround.paid_off_debt</code>.
     */
    public UInteger getPaidOffDebt() {
        return (UInteger) get(13);
    }

    /**
     * Setter for <code>housinggame.playerround.measure_bought</code>.
     */
    public void setMeasureBought(UInteger value) {
        set(14, value);
    }

    /**
     * Getter for <code>housinggame.playerround.measure_bought</code>.
     */
    public UInteger getMeasureBought() {
        return (UInteger) get(14);
    }

    /**
     * Setter for <code>housinggame.playerround.pluvial_damage</code>.
     */
    public void setPluvialDamage(UInteger value) {
        set(15, value);
    }

    /**
     * Getter for <code>housinggame.playerround.pluvial_damage</code>.
     */
    public UInteger getPluvialDamage() {
        return (UInteger) get(15);
    }

    /**
     * Setter for <code>housinggame.playerround.fluvial_damage</code>.
     */
    public void setFluvialDamage(UInteger value) {
        set(16, value);
    }

    /**
     * Getter for <code>housinggame.playerround.fluvial_damage</code>.
     */
    public UInteger getFluvialDamage() {
        return (UInteger) get(16);
    }

    /**
     * Setter for <code>housinggame.playerround.repaired_damage</code>.
     */
    public void setRepairedDamage(UInteger value) {
        set(17, value);
    }

    /**
     * Getter for <code>housinggame.playerround.repaired_damage</code>.
     */
    public UInteger getRepairedDamage() {
        return (UInteger) get(17);
    }

    /**
     * Setter for
     * <code>housinggame.playerround.satisfaction_point_bought</code>.
     */
    public void setSatisfactionPointBought(UInteger value) {
        set(18, value);
    }

    /**
     * Getter for
     * <code>housinggame.playerround.satisfaction_point_bought</code>.
     */
    public UInteger getSatisfactionPointBought() {
        return (UInteger) get(18);
    }

    /**
     * Setter for <code>housinggame.playerround.house_id</code>.
     */
    public void setHouseId(UInteger value) {
        set(19, value);
    }

    /**
     * Getter for <code>housinggame.playerround.house_id</code>.
     */
    public UInteger getHouseId() {
        return (UInteger) get(19);
    }

    /**
     * Setter for <code>housinggame.playerround.player_id</code>.
     */
    public void setPlayerId(UInteger value) {
        set(20, value);
    }

    /**
     * Getter for <code>housinggame.playerround.player_id</code>.
     */
    public UInteger getPlayerId() {
        return (UInteger) get(20);
    }

    /**
     * Setter for <code>housinggame.playerround.groupround_id</code>.
     */
    public void setGrouproundId(UInteger value) {
        set(21, value);
    }

    /**
     * Getter for <code>housinggame.playerround.groupround_id</code>.
     */
    public UInteger getGrouproundId() {
        return (UInteger) get(21);
    }

    /**
     * Setter for <code>housinggame.playerround.group_id</code>.
     */
    public void setGroupId(UInteger value) {
        set(22, value);
    }

    /**
     * Getter for <code>housinggame.playerround.group_id</code>.
     */
    public UInteger getGroupId() {
        return (UInteger) get(22);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UInteger> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PlayerroundRecord
     */
    public PlayerroundRecord() {
        super(Playerround.PLAYERROUND);
    }

    /**
     * Create a detached, initialised PlayerroundRecord
     */
    public PlayerroundRecord(UInteger id, UInteger satisfaction, UInteger saving, UInteger mortgage, UInteger livingCosts, UInteger income, UInteger debt, Integer currentWealth, UInteger preferredHouseRating, UInteger satisfactionCostPerPoint, UInteger housePriceSold, UInteger housePriceBought, UInteger spentSavingsForBuyingHouse, UInteger paidOffDebt, UInteger measureBought, UInteger pluvialDamage, UInteger fluvialDamage, UInteger repairedDamage, UInteger satisfactionPointBought, UInteger houseId, UInteger playerId, UInteger grouproundId, UInteger groupId) {
        super(Playerround.PLAYERROUND);

        setId(id);
        setSatisfaction(satisfaction);
        setSaving(saving);
        setMortgage(mortgage);
        setLivingCosts(livingCosts);
        setIncome(income);
        setDebt(debt);
        setCurrentWealth(currentWealth);
        setPreferredHouseRating(preferredHouseRating);
        setSatisfactionCostPerPoint(satisfactionCostPerPoint);
        setHousePriceSold(housePriceSold);
        setHousePriceBought(housePriceBought);
        setSpentSavingsForBuyingHouse(spentSavingsForBuyingHouse);
        setPaidOffDebt(paidOffDebt);
        setMeasureBought(measureBought);
        setPluvialDamage(pluvialDamage);
        setFluvialDamage(fluvialDamage);
        setRepairedDamage(repairedDamage);
        setSatisfactionPointBought(satisfactionPointBought);
        setHouseId(houseId);
        setPlayerId(playerId);
        setGrouproundId(grouproundId);
        setGroupId(groupId);
        resetChangedOnNotNull();
    }
}
