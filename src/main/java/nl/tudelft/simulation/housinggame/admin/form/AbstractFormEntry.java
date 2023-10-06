package nl.tudelft.simulation.housinggame.admin.form;

import org.jooq.Record;
import org.jooq.TableField;

public abstract class AbstractFormEntry<F extends AbstractFormEntry<F, T>, T>
{

    private AdminForm form;

    private final TableField<?, T> tableField;

    private String label;

    private String type;

    private boolean required;

    private boolean readOnly;

    private boolean hidden = false;

    protected String errors; // cumulative error register

    private T initialValue; // to be able to reset the form

    private String lastEnteredValue; // to restore the form after error

    public AbstractFormEntry(final TableField<?, T> tableField)
    {
        this.tableField = tableField;
        this.label = this.tableField.getName();
        this.type = this.tableField.getType().getName().toUpperCase();
        this.required = false;
        this.readOnly = false;
        this.errors = "";
    }

    public AdminForm getForm()
    {
        return this.form;
    }

    public void setForm(final AdminForm form)
    {
        this.form = form;
    }

    public String getLabel()
    {
        return this.label;
    }

    @SuppressWarnings("unchecked")
    public F setLabel(final String label)
    {
        this.label = label;
        return (F) this;
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

    public boolean isRequired()
    {
        return this.required;
    }

    @SuppressWarnings("unchecked")
    public F setRequired(final boolean required)
    {
        this.required = required;
        return (F) this;
    }

    public F setRequired()
    {
        return setRequired(true);
    }

    public boolean isHidden()
    {
        return this.hidden;
    }

    @SuppressWarnings("unchecked")
    public F setHidden(final boolean hidden)
    {
        this.hidden = hidden;
        return (F) this;
    }

    public F setHidden()
    {
        return setHidden(true);
    }

    public TableField<?, ?> getTableField()
    {
        return this.tableField;
    }

    public T getInitialValue()
    {
        return this.initialValue;
    }

    @SuppressWarnings("unchecked")
    public F setInitialValue(final T initialValue, final T valueWhenNull)
    {
        this.initialValue = initialValue != null ? initialValue : valueWhenNull;
        setLastEnteredValue(codeForEdit(this.initialValue));
        return (F) this;
    }

    public String getLastEnteredValue()
    {
        return this.lastEnteredValue;
    }

    public void setLastEnteredValue(final String lastEnteredValue)
    {
        this.lastEnteredValue = lastEnteredValue;
    }

    public boolean isReadOnly()
    {
        return this.readOnly;
    }

    @SuppressWarnings("unchecked")
    public F setReadOnly(final boolean readOnly)
    {
        this.readOnly = readOnly;
        return (F) this;
    }

    @SuppressWarnings("unchecked")
    public F setReadOnly()
    {
        this.readOnly = true;
        return (F) this;
    }

    public String getErrors()
    {
        return this.errors;
    }

    public abstract String codeForEdit(T value);

    public abstract T codeForDatabase(String s);

    protected void addError(final String error)
    {
        this.errors += "<p>Field: '" + getLabel() + "' " + error + "</p>\n";
    }

    protected void validate(final String value)
    {
        this.lastEnteredValue = value;
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
                record.set((TableField<?, T>) this.tableField, codeForDatabase(value));
            }
            catch (Exception exception)
            {
                addError("Exception: " + exception.getMessage());
            }
        }
        return this.errors;
    }

    public abstract String makeHtml();

}
