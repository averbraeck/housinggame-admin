package nl.tudelft.simulation.housinggame.admin.form.table;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.AdminData;

public class TableEntryPickRecordUInt extends AbstractTableEntry<TableEntryPickRecordUInt, UInteger>
{

    /** Entries alphabetically sorted on pick name. */
    private SortedMap<String, UInteger> records = new TreeMap<>();

    public TableEntryPickRecordUInt(final TableField<?, UInteger> tableField)
    {
        super(tableField);
    }

    @Override
    public String codeForEdit(final UInteger value)
    {
        if (value != null)
            return value.toString();
        return "0";
    }

    @Override
    public UInteger codeForType(final String s)
    {
        if (s == null || s.length() == 0 || s.equals("null") || s.equals("0"))
            return null;
        return UInteger.valueOf(s);
    }

    public TableEntryPickRecordUInt setPickTable(final AdminData data, final Table<?> table, final TableField<?, UInteger> id,
            final TableField<?, ?> name)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<? extends Record> tableRecords = dslContext.selectFrom(table).fetch();
        if (!isRequired())
            this.records.put("", null);
        for (Record record : tableRecords)
        {
            this.records.put(record.get(name).toString(), record.get(id));
        }
        return this;
    }

    public TableEntryPickRecordUInt setPickTable(final AdminData data, final Table<?> table, final TableField<?, UInteger> id,
            final TableField<?, String> name, final Condition condition)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<? extends Record> tableRecords = dslContext.selectFrom(table).where(condition).fetch();
        if (!isRequired())
            this.records.put("", null);
        for (Record record : tableRecords)
        {
            this.records.put(record.get(name), record.get(id));
        }
        return this;
    }

    @Override
    public String makeHtml()
    {
        StringBuilder s = new StringBuilder();
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
            s.append("\" style=\"pointer-events: none;\">\n");
        else
            s.append("\">\n");
        for (String name : this.records.keySet())
        {
            UInteger id = this.records.get(name);
            s.append("        <option value=\"");
            s.append(id);
            s.append("\"");
            // System.out.println(getLastEnteredValue());
            if (codeForEdit(id).equals(getLastEnteredValue()))
            {
                s.append(" selected");
            }
            s.append(">");
            s.append(name);
            s.append("</option>\n");
        }
        s.append("        </select>\n");
        s.append("      </td>\n");
        s.append("    </tr>\n");
        return s.toString();
    }

}
