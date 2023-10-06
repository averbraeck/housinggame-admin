/*
 * This file is generated by jOOQ.
 */
package nl.tudelft.simulation.housinggame.data.tables.records;


import nl.tudelft.simulation.housinggame.data.tables.Tax;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;
import org.jooq.types.UInteger;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TaxRecord extends UpdatableRecordImpl<TaxRecord> implements Record6<UInteger, String, UInteger, UInteger, Double, UInteger> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>housinggame.tax.id</code>.
     */
    public void setId(UInteger value) {
        set(0, value);
    }

    /**
     * Getter for <code>housinggame.tax.id</code>.
     */
    public UInteger getId() {
        return (UInteger) get(0);
    }

    /**
     * Setter for <code>housinggame.tax.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>housinggame.tax.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>housinggame.tax.minimum_inhabitants</code>.
     */
    public void setMinimumInhabitants(UInteger value) {
        set(2, value);
    }

    /**
     * Getter for <code>housinggame.tax.minimum_inhabitants</code>.
     */
    public UInteger getMinimumInhabitants() {
        return (UInteger) get(2);
    }

    /**
     * Setter for <code>housinggame.tax.maximum_inhabitants</code>.
     */
    public void setMaximumInhabitants(UInteger value) {
        set(3, value);
    }

    /**
     * Getter for <code>housinggame.tax.maximum_inhabitants</code>.
     */
    public UInteger getMaximumInhabitants() {
        return (UInteger) get(3);
    }

    /**
     * Setter for <code>housinggame.tax.tax_cost</code>.
     */
    public void setTaxCost(Double value) {
        set(4, value);
    }

    /**
     * Getter for <code>housinggame.tax.tax_cost</code>.
     */
    public Double getTaxCost() {
        return (Double) get(4);
    }

    /**
     * Setter for <code>housinggame.tax.community_id</code>.
     */
    public void setCommunityId(UInteger value) {
        set(5, value);
    }

    /**
     * Getter for <code>housinggame.tax.community_id</code>.
     */
    public UInteger getCommunityId() {
        return (UInteger) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<UInteger> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<UInteger, String, UInteger, UInteger, Double, UInteger> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<UInteger, String, UInteger, UInteger, Double, UInteger> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<UInteger> field1() {
        return Tax.TAX.ID;
    }

    @Override
    public Field<String> field2() {
        return Tax.TAX.NAME;
    }

    @Override
    public Field<UInteger> field3() {
        return Tax.TAX.MINIMUM_INHABITANTS;
    }

    @Override
    public Field<UInteger> field4() {
        return Tax.TAX.MAXIMUM_INHABITANTS;
    }

    @Override
    public Field<Double> field5() {
        return Tax.TAX.TAX_COST;
    }

    @Override
    public Field<UInteger> field6() {
        return Tax.TAX.COMMUNITY_ID;
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
    public UInteger component3() {
        return getMinimumInhabitants();
    }

    @Override
    public UInteger component4() {
        return getMaximumInhabitants();
    }

    @Override
    public Double component5() {
        return getTaxCost();
    }

    @Override
    public UInteger component6() {
        return getCommunityId();
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
    public UInteger value3() {
        return getMinimumInhabitants();
    }

    @Override
    public UInteger value4() {
        return getMaximumInhabitants();
    }

    @Override
    public Double value5() {
        return getTaxCost();
    }

    @Override
    public UInteger value6() {
        return getCommunityId();
    }

    @Override
    public TaxRecord value1(UInteger value) {
        setId(value);
        return this;
    }

    @Override
    public TaxRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public TaxRecord value3(UInteger value) {
        setMinimumInhabitants(value);
        return this;
    }

    @Override
    public TaxRecord value4(UInteger value) {
        setMaximumInhabitants(value);
        return this;
    }

    @Override
    public TaxRecord value5(Double value) {
        setTaxCost(value);
        return this;
    }

    @Override
    public TaxRecord value6(UInteger value) {
        setCommunityId(value);
        return this;
    }

    @Override
    public TaxRecord values(UInteger value1, String value2, UInteger value3, UInteger value4, Double value5, UInteger value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TaxRecord
     */
    public TaxRecord() {
        super(Tax.TAX);
    }

    /**
     * Create a detached, initialised TaxRecord
     */
    public TaxRecord(UInteger id, String name, UInteger minimumInhabitants, UInteger maximumInhabitants, Double taxCost, UInteger communityId) {
        super(Tax.TAX);

        setId(id);
        setName(name);
        setMinimumInhabitants(minimumInhabitants);
        setMaximumInhabitants(maximumInhabitants);
        setTaxCost(taxCost);
        setCommunityId(communityId);
        resetChangedOnNotNull();
    }
}
