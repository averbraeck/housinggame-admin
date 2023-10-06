/*
 * This file is generated by jOOQ.
 */
package nl.tudelft.simulation.housinggame.data.tables;


import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import nl.tudelft.simulation.housinggame.data.Housinggame;
import nl.tudelft.simulation.housinggame.data.Keys;
import nl.tudelft.simulation.housinggame.data.tables.records.GameversionRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function2;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row2;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;
import org.jooq.types.UInteger;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Gameversion extends TableImpl<GameversionRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>housinggame.gameversion</code>
     */
    public static final Gameversion GAMEVERSION = new Gameversion();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<GameversionRecord> getRecordType() {
        return GameversionRecord.class;
    }

    /**
     * The column <code>housinggame.gameversion.id</code>.
     */
    public final TableField<GameversionRecord, UInteger> ID = createField(DSL.name("id"), SQLDataType.INTEGERUNSIGNED.nullable(false).identity(true), this, "");

    /**
     * The column <code>housinggame.gameversion.name</code>.
     */
    public final TableField<GameversionRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    private Gameversion(Name alias, Table<GameversionRecord> aliased) {
        this(alias, aliased, null);
    }

    private Gameversion(Name alias, Table<GameversionRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>housinggame.gameversion</code> table reference
     */
    public Gameversion(String alias) {
        this(DSL.name(alias), GAMEVERSION);
    }

    /**
     * Create an aliased <code>housinggame.gameversion</code> table reference
     */
    public Gameversion(Name alias) {
        this(alias, GAMEVERSION);
    }

    /**
     * Create a <code>housinggame.gameversion</code> table reference
     */
    public Gameversion() {
        this(DSL.name("gameversion"), null);
    }

    public <O extends Record> Gameversion(Table<O> child, ForeignKey<O, GameversionRecord> key) {
        super(child, key, GAMEVERSION);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Housinggame.HOUSINGGAME;
    }

    @Override
    public Identity<GameversionRecord, UInteger> getIdentity() {
        return (Identity<GameversionRecord, UInteger>) super.getIdentity();
    }

    @Override
    public UniqueKey<GameversionRecord> getPrimaryKey() {
        return Keys.KEY_GAMEVERSION_PRIMARY;
    }

    @Override
    public List<UniqueKey<GameversionRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_GAMEVERSION_ID_UNIQUE, Keys.KEY_GAMEVERSION_NAME_UNIQUE);
    }

    @Override
    public Gameversion as(String alias) {
        return new Gameversion(DSL.name(alias), this);
    }

    @Override
    public Gameversion as(Name alias) {
        return new Gameversion(alias, this);
    }

    @Override
    public Gameversion as(Table<?> alias) {
        return new Gameversion(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Gameversion rename(String name) {
        return new Gameversion(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Gameversion rename(Name name) {
        return new Gameversion(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Gameversion rename(Table<?> name) {
        return new Gameversion(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row2 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row2<UInteger, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function2<? super UInteger, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function2<? super UInteger, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
