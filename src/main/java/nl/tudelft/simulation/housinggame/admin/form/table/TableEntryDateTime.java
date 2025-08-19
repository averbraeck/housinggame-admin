package nl.tudelft.simulation.housinggame.admin.form.table;

import java.time.LocalDateTime;

import org.jooq.TableField;

public class TableEntryDateTime extends AbstractTableEntry<TableEntryDateTime, LocalDateTime>
{

    public TableEntryDateTime(final TableField<?, LocalDateTime> tableField)
    {
        super(tableField);
    }

    @Override
    public String codeForEdit(final LocalDateTime value)
    {
        if (value == null)
            return "";
        return value.toString();
    }

    @Override
    public LocalDateTime codeForType(final String s)
    {
        if (s != null && s.length() > 0)
        {
            return LocalDateTime.parse(s);
        }
        return null;
    }

    @Override
    protected void validate(final String value)
    {
        super.validate(value);
        if (value != null && value.length() > 0)
        {
            try
            {
                LocalDateTime.parse(value);
            }
            catch (Exception exception)
            {
                addError("Exception: " + exception.getMessage());
            }
        }
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
        s.append("<input type=\"datetime-local\" ");
        if (isRequired())
            s.append("required name=\"");
        else
            s.append("name=\"");
        s.append(getTableField().getName());
        s.append("\" value=\"");
        s.append(getLastEnteredValue() == null ? "" : getLastEnteredValue());
        if (isReadOnly())
            s.append("\" readonly");
        else
            s.append("\"");

        if (!getTableField().getDataType().nullable())
        {
            s.append(">");
        }
        else
        {
            s.append(" oninput=\"fieldEdited('" + getTableField().getName() + "', this)\">\n");
            s.append("&nbsp;&nbsp;<input type=\"checkbox\" name=\"");
            s.append(getTableField().getName() + "-null\" value=\"null\"");
            s.append(getLastEnteredValue() == null ? " checked" : "");
            s.append(" onchange=\"nullToggle('" + getTableField().getName() + "', this)\">\n");
            s.append("<span class=\"null-badge\">NULL</span>\n");
        }

        s.append("</td>\n");
        s.append("    </tr>\n");

        return s.toString();
    }

}
