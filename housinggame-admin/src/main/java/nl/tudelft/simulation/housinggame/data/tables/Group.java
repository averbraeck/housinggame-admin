/*
 * This file is generated by jOOQ.
 */
package nl.tudelft.simulation.housinggame.data.tables;


import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import nl.tudelft.simulation.housinggame.data.Housinggame;
import nl.tudelft.simulation.housinggame.data.Indexes;
import nl.tudelft.simulation.housinggame.data.Keys;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function7;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row7;
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
public class Group extends TableImpl<GroupRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>housinggame.group</code>
     */
    public static final Group GROUP = new Group();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<GroupRecord> getRecordType() {
        return GroupRecord.class;
    }

    /**
     * The column <code>housinggame.group.id</code>.
     */
    public final TableField<GroupRecord, UInteger> ID = createField(DSL.name("id"), SQLDataType.INTEGERUNSIGNED.nullable(false).identity(true), this, "");

    /**
     * The column <code>housinggame.group.name</code>.
     */
    public final TableField<GroupRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(16).nullable(false), this, "");

    /**
     * The column <code>housinggame.group.password</code>.
     */
    public final TableField<GroupRecord, String> PASSWORD = createField(DSL.name("password"), SQLDataType.VARCHAR(32).nullable(false), this, "");

    /**
     * The column <code>housinggame.group.scenario_id</code>.
     */
    public final TableField<GroupRecord, UInteger> SCENARIO_ID = createField(DSL.name("scenario_id"), SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>housinggame.group.gamesession_id</code>.
     */
    public final TableField<GroupRecord, UInteger> GAMESESSION_ID = createField(DSL.name("gamesession_id"), SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>housinggame.group.round_id</code>.
     */
    public final TableField<GroupRecord, UInteger> ROUND_ID = createField(DSL.name("round_id"), SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>housinggame.group.facilitator_id</code>.
     */
    public final TableField<GroupRecord, UInteger> FACILITATOR_ID = createField(DSL.name("facilitator_id"), SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    private Group(Name alias, Table<GroupRecord> aliased) {
        this(alias, aliased, null);
    }

    private Group(Name alias, Table<GroupRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>housinggame.group</code> table reference
     */
    public Group(String alias) {
        this(DSL.name(alias), GROUP);
    }

    /**
     * Create an aliased <code>housinggame.group</code> table reference
     */
    public Group(Name alias) {
        this(alias, GROUP);
    }

    /**
     * Create a <code>housinggame.group</code> table reference
     */
    public Group() {
        this(DSL.name("group"), null);
    }

    public <O extends Record> Group(Table<O> child, ForeignKey<O, GroupRecord> key) {
        super(child, key, GROUP);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Housinggame.HOUSINGGAME;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.GROUP_FK_GROUP_FACILITATOR1_IDX, Indexes.GROUP_FK_GROUP_GAMESESSION1_IDX, Indexes.GROUP_FK_GROUP_ROUND1_IDX, Indexes.GROUP_FK_GROUP_SCENARIO1_IDX);
    }

    @Override
    public Identity<GroupRecord, UInteger> getIdentity() {
        return (Identity<GroupRecord, UInteger>) super.getIdentity();
    }

    @Override
    public UniqueKey<GroupRecord> getPrimaryKey() {
        return Keys.KEY_GROUP_PRIMARY;
    }

    @Override
    public List<UniqueKey<GroupRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_GROUP_ID_UNIQUE);
    }

    @Override
    public List<ForeignKey<GroupRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FK_GROUP_SCENARIO1, Keys.FK_GROUP_GAMESESSION1, Keys.FK_GROUP_ROUND1, Keys.FK_GROUP_FACILITATOR1);
    }

    private transient Scenario _scenario;
    private transient Gamesession _gamesession;
    private transient Round _round;
    private transient Facilitator _facilitator;

    /**
     * Get the implicit join path to the <code>housinggame.scenario</code>
     * table.
     */
    public Scenario scenario() {
        if (_scenario == null)
            _scenario = new Scenario(this, Keys.FK_GROUP_SCENARIO1);

        return _scenario;
    }

    /**
     * Get the implicit join path to the <code>housinggame.gamesession</code>
     * table.
     */
    public Gamesession gamesession() {
        if (_gamesession == null)
            _gamesession = new Gamesession(this, Keys.FK_GROUP_GAMESESSION1);

        return _gamesession;
    }

    /**
     * Get the implicit join path to the <code>housinggame.round</code> table.
     */
    public Round round() {
        if (_round == null)
            _round = new Round(this, Keys.FK_GROUP_ROUND1);

        return _round;
    }

    /**
     * Get the implicit join path to the <code>housinggame.facilitator</code>
     * table.
     */
    public Facilitator facilitator() {
        if (_facilitator == null)
            _facilitator = new Facilitator(this, Keys.FK_GROUP_FACILITATOR1);

        return _facilitator;
    }

    @Override
    public Group as(String alias) {
        return new Group(DSL.name(alias), this);
    }

    @Override
    public Group as(Name alias) {
        return new Group(alias, this);
    }

    @Override
    public Group as(Table<?> alias) {
        return new Group(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Group rename(String name) {
        return new Group(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Group rename(Name name) {
        return new Group(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Group rename(Table<?> name) {
        return new Group(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<UInteger, String, String, UInteger, UInteger, UInteger, UInteger> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function7<? super UInteger, ? super String, ? super String, ? super UInteger, ? super UInteger, ? super UInteger, ? super UInteger, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function7<? super UInteger, ? super String, ? super String, ? super UInteger, ? super UInteger, ? super UInteger, ? super UInteger, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
