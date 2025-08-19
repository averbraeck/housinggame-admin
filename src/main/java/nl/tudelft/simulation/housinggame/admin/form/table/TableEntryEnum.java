package nl.tudelft.simulation.housinggame.admin.form.table;

import org.jooq.EnumType;
import org.jooq.TableField;

public class TableEntryEnum<T extends EnumType> extends AbstractTableEntry<TableEntryEnum<T>, T>
{

    private T[] pickListEntries;

    public TableEntryEnum(final TableField<?, T> tableField)
    {
        super(tableField);
    }

    @Override
    public String codeForEdit(final T value)
    {
        if (value == null)
            return "";
        return value.toString();
    }

    @Override
    public T codeForType(final String s)
    {
        for (T entry : this.pickListEntries)
        {
            if (entry.getLiteral().equals(s))
                return entry;
        }
        return null;
    }

    public T[] getPickListEntries()
    {
        return this.pickListEntries;
    }

    public TableEntryEnum<T> setPickListEntries(final T[] pickListEntries)
    {
        this.pickListEntries = pickListEntries;
        return this;
    }

    @Override
    public String makeHtml()
    {
        StringBuilder s = new StringBuilder();

        if (isHidden())
        {
            s.append("    <input type=\"hidden\" name=\"");
            s.append(getTableField().getName());
            s.append("\" value=\"");
            s.append(getLastEnteredValue() == null ? "" : getLastEnteredValue());
            s.append("\" />\n");
            return s.toString();
        }

        s.append("    <tr>\n");
        String labelLength = getForm() == null ? "25%" : getForm().getLabelLength();
        s.append("      <td width=\"" + labelLength + "\">");
        s.append(getLabel());
        if (isRequired())
            s.append(" *");
        s.append("      </td>");
        String fieldLength = getForm() == null ? "75%" : getForm().getFieldLength();
        s.append("      <td width=\"" + fieldLength + "\">");
        s.append("        <select ");
        if (isRequired())
            s.append(" required name=\"");
        else
            s.append(" name=\"");
        s.append(getTableField().getName());
        if (isReadOnly())
            s.append("\" readonly");
        else
            s.append("\"");

        if (!getTableField().getDataType().nullable())
        {
            s.append(">\n");
        }
        else
        {
            s.append(" onchange=\"fieldEdited('" + getTableField().getName() + "', this)\">\n");
        }

        for (T entry : getPickListEntries())
        {
            s.append("        <option value=\"");
            s.append(entry.getLiteral());
            s.append("\"");
            if (entry.getLiteral().equals(getLastEnteredValue()))
            {
                s.append(" selected");
            }
            s.append(">");
            s.append(entry.getLiteral());
            s.append("</option>\n");
        }
        s.append("        </select>\n");

        if (getTableField().getDataType().nullable())
        {
            s.append("&nbsp;&nbsp;<input type=\"checkbox\" name=\"");
            s.append(getTableField().getName() + "-null\" value=\"null\"");
            s.append(getLastEnteredValue() == null ? " checked" : "");
            s.append(" onchange=\"nullToggle('" + getTableField().getName() + "', this)\">\n");
            s.append("<span class=\"null-badge\">NULL</span>\n");
        }

        s.append("      </td>\n");
        s.append("    </tr>\n");
        return s.toString();
    }

}
