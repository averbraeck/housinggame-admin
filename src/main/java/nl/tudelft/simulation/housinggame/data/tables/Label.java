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
import nl.tudelft.simulation.housinggame.data.tables.records.LabelRecord;

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
public class Label extends TableImpl<LabelRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>housinggame.label</code>
     */
    public static final Label LABEL = new Label();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<LabelRecord> getRecordType() {
        return LabelRecord.class;
    }

    /**
     * The column <code>housinggame.label.id</code>.
     */
    public final TableField<LabelRecord, UInteger> ID = createField(DSL.name("id"), SQLDataType.INTEGERUNSIGNED.nullable(false).identity(true), this, "");

    /**
     * The column <code>housinggame.label.key</code>.
     */
    public final TableField<LabelRecord, String> KEY = createField(DSL.name("key"), SQLDataType.VARCHAR(45).nullable(false), this, "");

    /**
     * The column <code>housinggame.label.value1</code>.
     */
    public final TableField<LabelRecord, String> VALUE1 = createField(DSL.name("value1"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>housinggame.label.value2</code>.
     */
    public final TableField<LabelRecord, String> VALUE2 = createField(DSL.name("value2"), SQLDataType.CLOB.defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.CLOB)), this, "");

    /**
     * The column <code>housinggame.label.value3</code>.
     */
    public final TableField<LabelRecord, String> VALUE3 = createField(DSL.name("value3"), SQLDataType.CLOB.defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.CLOB)), this, "");

    /**
     * The column <code>housinggame.label.value4</code>.
     */
    public final TableField<LabelRecord, String> VALUE4 = createField(DSL.name("value4"), SQLDataType.CLOB.defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.CLOB)), this, "");

    /**
     * The column <code>housinggame.label.languages_id</code>.
     */
    public final TableField<LabelRecord, UInteger> LANGUAGES_ID = createField(DSL.name("languages_id"), SQLDataType.INTEGERUNSIGNED.nullable(false), this, "");

    private Label(Name alias, Table<LabelRecord> aliased) {
        this(alias, aliased, null);
    }

    private Label(Name alias, Table<LabelRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>housinggame.label</code> table reference
     */
    public Label(String alias) {
        this(DSL.name(alias), LABEL);
    }

    /**
     * Create an aliased <code>housinggame.label</code> table reference
     */
    public Label(Name alias) {
        this(alias, LABEL);
    }

    /**
     * Create a <code>housinggame.label</code> table reference
     */
    public Label() {
        this(DSL.name("label"), null);
    }

    public <O extends Record> Label(Table<O> child, ForeignKey<O, LabelRecord> key) {
        super(child, key, LABEL);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Housinggame.HOUSINGGAME;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.LABEL_FK_LABEL_LANGUAGES1_IDX);
    }

    @Override
    public Identity<LabelRecord, UInteger> getIdentity() {
        return (Identity<LabelRecord, UInteger>) super.getIdentity();
    }

    @Override
    public UniqueKey<LabelRecord> getPrimaryKey() {
        return Keys.KEY_LABEL_PRIMARY;
    }

    @Override
    public List<UniqueKey<LabelRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_LABEL_ID_UNIQUE, Keys.KEY_LABEL_KEY_UNIQUE);
    }

    @Override
    public List<ForeignKey<LabelRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FK_LABEL_LANGUAGES1);
    }

    private transient Languages _languages;

    /**
     * Get the implicit join path to the <code>housinggame.languages</code>
     * table.
     */
    public Languages languages() {
        if (_languages == null)
            _languages = new Languages(this, Keys.FK_LABEL_LANGUAGES1);

        return _languages;
    }

    @Override
    public Label as(String alias) {
        return new Label(DSL.name(alias), this);
    }

    @Override
    public Label as(Name alias) {
        return new Label(alias, this);
    }

    @Override
    public Label as(Table<?> alias) {
        return new Label(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Label rename(String name) {
        return new Label(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Label rename(Name name) {
        return new Label(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Label rename(Table<?> name) {
        return new Label(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row7<UInteger, String, String, String, String, String, UInteger> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function7<? super UInteger, ? super String, ? super String, ? super String, ? super String, ? super String, ? super UInteger, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function7<? super UInteger, ? super String, ? super String, ? super String, ? super String, ? super String, ? super UInteger, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
