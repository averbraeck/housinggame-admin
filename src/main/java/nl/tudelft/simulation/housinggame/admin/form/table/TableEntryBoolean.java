package nl.tudelft.simulation.housinggame.admin.form.table;

import org.jooq.Record;
import org.jooq.TableField;

public class TableEntryBoolean extends AbstractTableEntry<TableEntryBoolean, Byte>
{

    // assumes boolean is coded as TINYINT
    public TableEntryBoolean(final TableField<?, Byte> tableField)
    {
        super(tableField);
    }

    @Override
    public String codeForEdit(final Byte value)
    {
        if (value == null)
            return "0";
        return value.byteValue() == 1 ? "1" : "0";
    }

    @Override
    public Byte codeForType(final String s)
    {
        return "1".equals(s) ? (byte) 1 : (byte) 0;
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
            s.append(getLastEnteredValue() == null ? "0" : getLastEnteredValue());
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
        s.append("<input type=\"checkbox\" name=\"");
        s.append(getTableField().getName());
        s.append("\" ");
        s.append(getLastEnteredValue() == null || "0".equals(getLastEnteredValue()) ? "" : "checked");
        s.append(" value=\"1\"");
        if (isReadOnly())
            s.append(" readonly");

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

    @Override
    public String setRecordValue(final Record record, final String value)
    {
        String v = value == null ? "0" : value; // field is NOT returned when not ticked...
        return super.setRecordValue(record, v);
    }

}
