package nl.tudelft.simulation.housinggame.admin.form;

import java.time.LocalDateTime;

public class FormEntryDateTime extends AbstractFormEntry<FormEntryDateTime, LocalDateTime>
{

    public FormEntryDateTime(final String label, final String name)
    {
        super(label, name);
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
            s.append(getName());
            s.append("\" value=\"");
            s.append(getLastEnteredValue() == null ? "" : getLastEnteredValue());
            s.append("\" />\n");
            return s.toString();
        }

        s.append("    <tr>\n");
        s.append("      <td width=\"" + getForm().getLabelLength() + "\">");
        s.append(getLabel());
        if (isRequired())
            s.append(" *");
        s.append("      </td>");
        s.append("      <td width=\"" + getForm().getFieldLength() + "\">");
        s.append("<input type=\"datetime-local\" ");
        if (isRequired())
            s.append("required name=\"");
        else
            s.append("name=\"");
        s.append(getName());
        s.append("\" value=\"");
        s.append(getLastEnteredValue() == null ? "" : getLastEnteredValue());
        if (isReadOnly())
            s.append("\" readonly />");
        else
            s.append("\" />");
        s.append("</td>\n");
        s.append("    </tr>\n");

        return s.toString();
    }

}
