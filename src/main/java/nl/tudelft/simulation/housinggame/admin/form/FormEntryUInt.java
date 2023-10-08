package nl.tudelft.simulation.housinggame.admin.form;

import org.jooq.types.UInteger;

public class FormEntryUInt extends AbstractFormEntry<FormEntryUInt, UInteger>
{

    int min;

    int max;

    int step;

    String pattern;

    public FormEntryUInt(final String label, final String name)
    {
        super(label, name);
        this.min = -Integer.MAX_VALUE;
        this.max = Integer.MAX_VALUE;
        this.step = 1;
        this.pattern = "\\d+";
    }

    public int getMin()
    {
        return this.min;
    }

    public FormEntryUInt setMin(final int min)
    {
        this.min = min;
        return this;
    }

    public int getMax()
    {
        return this.max;
    }

    public FormEntryUInt setMax(final int max)
    {
        this.max = max;
        return this;
    }

    public int getStep()
    {
        return this.step;
    }

    public FormEntryUInt setStep(final int step)
    {
        this.step = step;
        return this;
    }

    public String getPattern()
    {
        return this.pattern;
    }

    public FormEntryUInt setPattern(final String pattern)
    {
        this.pattern = pattern;
        return this;
    }

    @Override
    public String codeForEdit(final UInteger value)
    {
        if (value == null)
            return "";
        return value.toString();
    }

    @Override
    public UInteger codeForType(final String s)
    {
        return UInteger.valueOf(s);
    }

    @Override
    protected void validate(final String value)
    {
        super.validate(value);
        try
        {
            int v = Integer.valueOf(value);
            if (v < getMin())
                addError("Value lower than minimum " + getMin());
            if (v > getMax())
                addError("Value larger than maximum " + getMin());
            // TODO: step, pattern
        }
        catch (Exception exception)
        {
            addError("Exception: " + exception.getMessage());
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
        s.append("      <td width=\"25%\">");
        s.append(getLabel());
        if (isRequired())
            s.append(" *");
        s.append("      </td>");
        s.append("      <td width=\"75%\">");
        s.append("<input type=\"number\" min=\"");
        s.append(getMin());
        s.append("\" max=\"");
        s.append(getMax());
        s.append("\" step=\"");
        s.append(getStep());
        s.append("\" pattern=\"");
        s.append(getPattern());
        if (isRequired())
            s.append("\" required name=\"");
        else
            s.append("\" name=\"");
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