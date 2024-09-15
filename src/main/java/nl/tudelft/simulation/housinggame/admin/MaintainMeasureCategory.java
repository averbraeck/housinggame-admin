package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryText;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasurecategoryRecord;

public class MaintainMeasureCategory
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("measurecategory"))
        {
            data.clearColumns("30%", "MeasureCategory");
            data.clearFormColumn("70%", "Edit Properties");
            showMeasureCategory(session, data, 0, true, false);
        }

        else if (click.contains("MeasureCategory"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.MEASURECATEGORY, "measurecategory");
            else if (click.startsWith("delete"))
            {
                MeasurecategoryRecord measureCategory = SqlUtils.readRecordFromId(data, Tables.MEASURECATEGORY, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(measureCategory, "measurecategory");
                else
                    data.askDeleteRecord(measureCategory, "MeasureCategory", measureCategory.getName(),
                            "deleteMeasureCategoryOk", "measurecategory");
                recordId = 0;
            }
            if (!data.isError())
            {
                showMeasureCategory(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editMeasureCategory(session, data, 0, true);
            }
        }

        AdminServlet.makeColumnContent(data);
    }

    /*
     * *********************************************************************************************************
     * ***************************************** MEASURECATEGORY ***********************************************
     * *********************************************************************************************************
     */

    public static void showMeasureCategory(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("MeasureCategory", 0, recordId, editButton, Tables.MEASURECATEGORY, Tables.MEASURECATEGORY.NAME, "name",
                true);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editMeasureCategory(session, data, recordId, editRecord);
        }
    }

    public static void editMeasureCategory(final HttpSession session, final AdminData data, final int measurecategoryId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        MeasurecategoryRecord measureCategory =
                measurecategoryId == 0 ? dslContext.newRecord(Tables.MEASURECATEGORY) : dslContext
                        .selectFrom(Tables.MEASURECATEGORY).where(Tables.MEASURECATEGORY.ID.eq(measurecategoryId)).fetchOne();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("measurecategory", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editMeasureCategory")
                .setSaveMethod("saveMeasureCategory")
                .setDeleteMethod("deleteMeasureCategory", "Delete", "<br>Note: Do not delete measurecategory when"
                        + "<br> measure types for this measurecategory exist")
                .setRecordNr(measurecategoryId)
                .startForm()
                .addEntry(new TableEntryString(Tables.MEASURECATEGORY.NAME)
                        .setRequired()
                        .setInitialValue(measureCategory.getName(), "")
                        .setLabel("Short name (24 chars)")
                        .setMaxChars(24))
                .addEntry(new TableEntryString(Tables.MEASURECATEGORY.DESCRIPTION)
                        .setRequired()
                        .setInitialValue(measureCategory.getDescription(), "")
                        .setLabel("Long name (255 chars)")
                        .setMaxChars(255))
                .addEntry(new TableEntryText(Tables.MEASURECATEGORY.EXPLANATION)
                        .setRequired(false)
                        .setInitialValue(measureCategory.getExplanation(), "")
                        .setLabel("Explanation")
                        .setRows(10))
                .endForm();
        //@formatter:on

        data.getFormColumn().setHeaderForm("Edit MeasureCategory", form);
    }

}
