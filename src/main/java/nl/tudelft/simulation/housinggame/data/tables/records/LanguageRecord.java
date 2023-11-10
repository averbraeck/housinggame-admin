/*
 * This file is generated by jOOQ.
 */
package nl.tudelft.simulation.housinggame.data.tables.records;


import nl.tudelft.simulation.housinggame.data.tables.Language;

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
public class LanguageRecord extends UpdatableRecordImpl<LanguageRecord> implements Record6<UInteger, String, String, String, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>housinggame.language.id</code>.
     */
    public void setId(UInteger value) {
        set(0, value);
    }

    /**
     * Getter for <code>housinggame.language.id</code>.
     */
    public UInteger getId() {
        return (UInteger) get(0);
    }

    /**
     * Setter for <code>housinggame.language.language1</code>.
     */
    public void setLanguage1(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>housinggame.language.language1</code>.
     */
    public String getLanguage1() {
        return (String) get(1);
    }

    /**
     * Setter for <code>housinggame.language.language2</code>.
     */
    public void setLanguage2(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>housinggame.language.language2</code>.
     */
    public String getLanguage2() {
        return (String) get(2);
    }

    /**
     * Setter for <code>housinggame.language.language3</code>.
     */
    public void setLanguage3(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>housinggame.language.language3</code>.
     */
    public String getLanguage3() {
        return (String) get(3);
    }

    /**
     * Setter for <code>housinggame.language.language4</code>.
     */
    public void setLanguage4(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>housinggame.language.language4</code>.
     */
    public String getLanguage4() {
        return (String) get(4);
    }

    /**
     * Setter for <code>housinggame.language.lock</code>.
     */
    public void setLock(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>housinggame.language.lock</code>.
     */
    public String getLock() {
        return (String) get(5);
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
    public Row6<UInteger, String, String, String, String, String> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<UInteger, String, String, String, String, String> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<UInteger> field1() {
        return Language.LANGUAGE.ID;
    }

    @Override
    public Field<String> field2() {
        return Language.LANGUAGE.LANGUAGE1;
    }

    @Override
    public Field<String> field3() {
        return Language.LANGUAGE.LANGUAGE2;
    }

    @Override
    public Field<String> field4() {
        return Language.LANGUAGE.LANGUAGE3;
    }

    @Override
    public Field<String> field5() {
        return Language.LANGUAGE.LANGUAGE4;
    }

    @Override
    public Field<String> field6() {
        return Language.LANGUAGE.LOCK;
    }

    @Override
    public UInteger component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getLanguage1();
    }

    @Override
    public String component3() {
        return getLanguage2();
    }

    @Override
    public String component4() {
        return getLanguage3();
    }

    @Override
    public String component5() {
        return getLanguage4();
    }

    @Override
    public String component6() {
        return getLock();
    }

    @Override
    public UInteger value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getLanguage1();
    }

    @Override
    public String value3() {
        return getLanguage2();
    }

    @Override
    public String value4() {
        return getLanguage3();
    }

    @Override
    public String value5() {
        return getLanguage4();
    }

    @Override
    public String value6() {
        return getLock();
    }

    @Override
    public LanguageRecord value1(UInteger value) {
        setId(value);
        return this;
    }

    @Override
    public LanguageRecord value2(String value) {
        setLanguage1(value);
        return this;
    }

    @Override
    public LanguageRecord value3(String value) {
        setLanguage2(value);
        return this;
    }

    @Override
    public LanguageRecord value4(String value) {
        setLanguage3(value);
        return this;
    }

    @Override
    public LanguageRecord value5(String value) {
        setLanguage4(value);
        return this;
    }

    @Override
    public LanguageRecord value6(String value) {
        setLock(value);
        return this;
    }

    @Override
    public LanguageRecord values(UInteger value1, String value2, String value3, String value4, String value5, String value6) {
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
     * Create a detached LanguageRecord
     */
    public LanguageRecord() {
        super(Language.LANGUAGE);
    }

    /**
     * Create a detached, initialised LanguageRecord
     */
    public LanguageRecord(UInteger id, String language1, String language2, String language3, String language4, String lock) {
        super(Language.LANGUAGE);

        setId(id);
        setLanguage1(language1);
        setLanguage2(language2);
        setLanguage3(language3);
        setLanguage4(language4);
        setLock(lock);
        resetChangedOnNotNull();
    }
}