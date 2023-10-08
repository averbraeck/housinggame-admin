package nl.tudelft.simulation.housinggame.admin.form.table;

import org.jooq.Record;
import org.jooq.TableField;

import nl.tudelft.simulation.housinggame.admin.form.AbstractFormEntry;

public abstract class AbstractTableEntry<F extends AbstractTableEntry<F, T>, T> extends AbstractFormEntry<F, T>
{

    private final TableField<?, T> tableField;

    private String type;

    public AbstractTableEntry(final TableField<?, T> tableField)
    {
        super(tableField.getName());
        this.tableField = tableField;
        this.type = this.tableField.getType().getName().toUpperCase();
        setRequired(false);
        setReadOnly(false);
        this.errors = "";
    }

    public String getType()
    {
        return this.type;
    }

    @SuppressWarnings("unchecked")
    public F setType(final String type)
    {
        this.type = type;
        return (F) this;
    }

    public TableField<?, ?> getTableField()
    {
        return this.tableField;
    }

    protected void validate(final String value)
    {
        setLastEnteredValue(value);
        this.errors = "";
        if ((value == null || value.length() == 0) && (isRequired() || !this.tableField.getDataType().nullable()))
            addError("should not be empty");
    }

    public String setRecordValue(final Record record, final String value)
    {
        validate(value);
        if (this.errors.length() == 0)
        {
            try
            {
                record.set((TableField<?, T>) this.tableField, codeForType(value));
            }
            catch (Exception exception)
            {
                addError("Exception: " + exception.getMessage());
            }
        }
        return this.errors;
    }

}
