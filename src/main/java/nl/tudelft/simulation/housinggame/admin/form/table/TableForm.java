package nl.tudelft.simulation.housinggame.admin.form.table;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.jooq.Record;

import nl.tudelft.simulation.housinggame.admin.AdminData;

public class TableForm
{

    private StringBuilder s;

    private int recordNr;

    private String cancelMethod = "";

    private int cancelRecordNr = 0;

    private String saveMethod = "";

    private String saveText = "Save";

    private String editMethod = "";

    private String deleteMethod = "";

    private String deleteButton = "Delete";

    private String deleteText = "";

    private List<String> additionalButtons = new ArrayList<>();

    private List<String> additionalMethods = new ArrayList<>();

    List<AbstractTableEntry<?, ?>> entries = new ArrayList<>();

    private boolean multipart;

    private boolean edit;

    public TableForm()
    {
        this.s = new StringBuilder();
    }

    public TableForm startMultipartForm()
    {
        this.multipart = true;
        this.s.append("<div class=\"hg-form\">\n");
        this.s.append("  <form id=\"editForm\" action=\"/housinggame-admin/admin\" ");
        this.s.append("method=\"POST\" enctype=\"multipart/form-data\">\n");
        this.s.append("    <input id=\"editClick\" type=\"hidden\" name=\"editClick\" value=\"tobefilled\" />\n");
        this.s.append("    <input id=\"editRecordNr\" type=\"hidden\" name=\"editRecordNr\" value=\"0\" />\n");
        buttonRow();
        this.s.append("    <fieldset");
        if (isEdit())
            this.s.append(">\n");
        else
            this.s.append(" disabled=\"disabled\">\n");
        this.s.append("    <table width=\"100%\">\n");
        return this;
    }

    // Fieldset trick for read only:
    // https://stackoverflow.com/questions/3507958/how-can-i-make-an-entire-html-form-readonly
    public TableForm startForm()
    {
        this.multipart = false;
        this.s.append("<div class=\"hg-form\">\n");
        this.s.append("  <form id=\"editForm\" action=\"/housinggame-admin/admin\" method=\"POST\" >\n");
        this.s.append("    <input id=\"editClick\" type=\"hidden\" name=\"editClick\" value=\"tobefilled\" />\n");
        this.s.append("    <input id=\"editRecordNr\" type=\"hidden\" name=\"editRecordNr\" value=\"0\" />\n");
        buttonRow();
        this.s.append("    <fieldset");
        if (isEdit())
            this.s.append(">\n");
        else
            this.s.append(" disabled=\"disabled\">\n");
        this.s.append("    <table width=\"100%\">\n");
        return this;
    }

    public TableForm endForm()
    {
        this.s.append("    </table>\n");
        this.s.append("    </fieldset>\n");
        buttonRow();
        this.s.append("  </form>\n");
        this.s.append("</div>\n");
        return this;
    }

    private void buttonRow()
    {
        this.s.append("    <div class=\"hg-admin-form-buttons\">\n");
        this.s.append("      <span class=\"hg-admin-form-button\" /><a href=\"#\" onClick=\"submitEditForm('");
        this.s.append(this.cancelMethod);
        this.s.append("', ");
        if (this.cancelRecordNr > 0)
            this.s.append(this.cancelRecordNr);
        else
            this.s.append(this.recordNr);
        this.s.append("); return false;\">Cancel</a></span>");
        if (this.edit && this.saveMethod.length() > 0)
        {
            this.s.append("      <span class=\"hg-admin-form-button\" /><a href=\"#\" onClick=\"submitEditForm('");
            this.s.append(this.saveMethod);
            this.s.append("', ");
            this.s.append(this.recordNr);
            this.s.append("); return false;\">");
            this.s.append(this.saveText);
            this.s.append("</a></span>");
        }
        if (!this.edit && this.editMethod.length() > 0)
        {
            this.s.append("      <span class=\"hg-admin-form-button\" /><a href=\"#\" onClick=\"submitEditForm('");
            this.s.append(this.editMethod);
            this.s.append("', ");
            this.s.append(this.recordNr);
            this.s.append("); return false;\">Edit</a></span>");
        }
        if (this.edit && this.recordNr > 0 && this.deleteMethod.length() > 0)
        {
            this.s.append("      <span class=\"hg-admin-form-button\" /><a href=\"#\" onClick=\"submitEditForm('");
            this.s.append(this.deleteMethod);
            this.s.append("', ");
            this.s.append(this.recordNr);
            this.s.append("); return false;\">");
            this.s.append(this.deleteButton);
            this.s.append("</a></span>");
            if (this.deleteText.length() > 0)
                this.s.append("<i>&nbsp; &nbsp; " + this.deleteText + "</i>");
        }
        this.s.append("    </div>\n");

        for (int i = 0; i < this.additionalButtons.size(); i++)
        {
            this.s.append("<br/>      <span class=\"hg-admin-form-button\" /><a href=\"#\" onClick=\"submitEditForm('");
            this.s.append(this.additionalMethods.get(i));
            this.s.append("', ");
            this.s.append(this.recordNr);
            this.s.append("); return false;\">");
            this.s.append(this.additionalButtons.get(i));
            this.s.append("</a></span>\n");
        }
    }

    public TableForm addEntry(AbstractTableEntry<?, ?> entry)
    {
        this.entries.add(entry);
        entry.setForm(this);
        this.s.append(entry.makeHtml());
        return this;
    }

    public TableForm setCancelMethod(String cancelMethod)
    {
        this.cancelMethod = cancelMethod;
        return this;
    }

    public TableForm setCancelMethod(String cancelMethod, int cancelRecordNr)
    {
        this.cancelMethod = cancelMethod;
        this.cancelRecordNr = cancelRecordNr;
        return this;
    }

    public int getCancelRecordNr()
    {
        return this.cancelRecordNr;
    }

    public TableForm setSaveMethod(String saveMethod)
    {
        this.saveMethod = saveMethod;
        return this;
    }

    public TableForm setSaveMethod(String saveMethod, String saveText)
    {
        this.saveMethod = saveMethod;
        this.saveText = saveText;
        return this;
    }

    public TableForm setEditMethod(String editMethod)
    {
        this.editMethod = editMethod;
        return this;
    }

    public TableForm setDeleteMethod(String deleteeMethod)
    {
        this.deleteMethod = deleteeMethod;
        this.deleteButton = "Delete";
        this.deleteText = "";
        return this;
    }

    public TableForm setDeleteMethod(String deleteMethod, String deleteButton)
    {
        this.deleteMethod = deleteMethod;
        this.deleteButton = deleteButton;
        this.deleteText = "";
        return this;
    }

    public TableForm setDeleteMethod(String deleteMethod, String deleteButton, String deleteText)
    {
        this.deleteMethod = deleteMethod;
        this.deleteButton = deleteButton;
        this.deleteText = deleteText;
        return this;
    }

    public TableForm addAddtionalButton(String method, String buttonText)
    {
        this.additionalButtons.add(buttonText);
        this.additionalMethods.add(method);
        return this;
    }

    public TableForm setRecordNr(int recordNr)
    {
        this.recordNr = recordNr;
        return this;
    }

    public boolean isMultipart()
    {
        return this.multipart;
    }

    public boolean isEdit()
    {
        return this.edit;
    }

    public TableForm setEdit(boolean edit)
    {
        this.edit = edit;
        return this;
    }

    public String process()
    {
        return this.s.toString();
    }

    // for multipart: https://stackoverflow.com/questions/2422468/how-to-upload-files-to-server-using-jsp-servlet
    public String setFields(Record record, HttpServletRequest request, AdminData data)
    {
        String errors = "";
        for (AbstractTableEntry<?, ?> entry : this.entries)
        {
            if (isMultipart() && entry instanceof FormEntryImage)
            {
                try
                {
                    FormEntryImage imageEntry = (FormEntryImage) entry;
                    Part filePart = request.getPart(imageEntry.getTableField().getName());
                    String reset = request.getParameter(entry.getTableField().getName() + "_reset");
                    boolean delete = reset != null && reset.equals("delete");
                    if (delete)
                    {
                        errors += imageEntry.setRecordValue(record, (byte[]) null);
                    }
                    else if (filePart != null && filePart.getSubmittedFileName() != null
                            && filePart.getSubmittedFileName().length() > 0)
                    {
                        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                        imageEntry.setFilename(fileName);
                        try (InputStream fileContent = filePart.getInputStream())
                        {
                            byte[] image = fileContent.readAllBytes();
                            errors += imageEntry.setRecordValue(record, image);
                        }
                    }
                }
                catch (ServletException | IOException exception)
                {
                    errors += "<p>Exception: " + exception.getMessage() + "</p>\n";
                }
            }
            else
            {
                String value = request.getParameter(entry.getTableField().getName());
                errors += entry.setRecordValue(record, value);
            }
        }
        return errors;
    }

}
