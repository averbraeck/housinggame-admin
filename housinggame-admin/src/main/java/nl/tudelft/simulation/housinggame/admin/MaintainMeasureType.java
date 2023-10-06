package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.form.AdminForm;
import nl.tudelft.simulation.housinggame.admin.form.FormEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.FormEntryString;
import nl.tudelft.simulation.housinggame.admin.form.FormEntryText;
import nl.tudelft.simulation.housinggame.admin.form.FormEntryUInt;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasuretypeRecord;

public class MaintainMeasureType
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("measuretype"))
        {
            data.clearColumns("15%", "GameVersion", "15%", "Scenario", "15%", "MeasureType");
            data.clearFormColumn("55%", "Edit Properties");
            showGameVersion(session, data, 0);
        }

        else if (click.contains("MeasureTypeGameVersion"))
        {
            showGameVersion(session, data, recordId);
        }

        else if (click.contains("MeasureTypeScenario"))
        {
            showScenario(session, data, recordId);
        }

        else if (click.contains("MeasureType"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.MEASURETYPE, "measuretype");
            else if (click.startsWith("delete"))
            {
                MeasuretypeRecord measureType = SqlUtils.readRecordFromId(data, Tables.MEASURETYPE, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(measureType, "measuretype");
                else
                    data.deleteRecord(measureType, "MeasureType", String.valueOf(measureType.getName()), "deleteMeasureTypeOk",
                            "measuretype");
                recordId = 0;
            }
            if (!data.isError())
            {
                showMeasureType(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editMeasureType(session, data, 0, true);
            }
        }

        AdminServlet.makeColumnContent(data);
    }

    /*
     * *********************************************************************************************************
     * ****************************************** GAMEVERSION **************************************************
     * *********************************************************************************************************
     */

    public static void showGameVersion(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("MeasureTypeGameVersion", 0, recordId, false, Tables.GAMEVERSION, Tables.GAMEVERSION.NAME, "name",
                false);
        data.resetColumn(1);
        data.resetColumn(2);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("MeasureTypeScenario", 1, 0, false, Tables.SCENARIO, Tables.SCENARIO.NAME, "name",
                    Tables.SCENARIO.GAMEVERSION_ID, false);
        }
    }

    /*
     * *********************************************************************************************************
     * ********************************************* SCENARIO **************************************************
     * *********************************************************************************************************
     */

    public static void showScenario(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("MeasureTypeGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("MeasureTypeScenario", 1, recordId, false, Tables.SCENARIO, Tables.SCENARIO.NAME, "name",
                Tables.SCENARIO.GAMEVERSION_ID, false);
        data.resetColumn(2);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("MeasureType", 2, 0, true, Tables.MEASURETYPE, Tables.MEASURETYPE.NAME, "name",
                    Tables.MEASURETYPE.SCENARIO_ID, true);
        }
    }

    /*
     * *********************************************************************************************************
     * ***************************************** MEASURETYPE **********************************************
     * *********************************************************************************************************
     */

    public static void showMeasureType(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("MeasureTypeGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("MeasureTypeScenario", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.SCENARIO,
                Tables.SCENARIO.NAME, "name", Tables.SCENARIO.GAMEVERSION_ID, false);
        data.showDependentColumn("MeasureType", 2, 0, true, Tables.MEASURETYPE, Tables.MEASURETYPE.NAME, "name",
                Tables.MEASURETYPE.SCENARIO_ID, true);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editMeasureType(session, data, recordId, editRecord);
        }
    }

    public static void editMeasureType(final HttpSession session, final AdminData data, final int measureTypeId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        MeasuretypeRecord measureType = measureTypeId == 0 ? dslContext.newRecord(Tables.MEASURETYPE) : dslContext
                .selectFrom(Tables.MEASURETYPE).where(Tables.MEASURETYPE.ID.eq(UInteger.valueOf(measureTypeId))).fetchOne();
        UInteger scenarioId =
                measureTypeId == 0 ? UInteger.valueOf(data.getColumn(1).getSelectedRecordId()) : measureType.getScenarioId();
        //@formatter:off
        AdminForm form = new AdminForm()
                .setEdit(edit)
                .setCancelMethod("measuretype", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editMeasureType")
                .setSaveMethod("saveMeasureType")
                .setDeleteMethod("deleteMeasureType", "Delete", "<br>Note: MeasureType can only be deleted when it "
                        + "<br>has not been used in a scenario")
                .setRecordNr(measureTypeId)
                .startForm()
                .addEntry(new FormEntryString(Tables.MEASURETYPE.NAME)
                        .setRequired()
                        .setInitialValue(measureType.getName(), "")
                        .setLabel("Name")
                        .setMaxChars(255))
                .addEntry(new FormEntryText(Tables.MEASURETYPE.DESCRIPTION)
                        .setRequired()
                        .setInitialValue(measureType.getDescription(), "")
                        .setLabel("Description"))
                .addEntry(new FormEntryUInt(Tables.MEASURETYPE.PRICE)
                        .setRequired()
                        .setInitialValue(measureType.getPrice(), UInteger.valueOf(0))
                        .setLabel("Price")
                        .setMin(0))
                .addEntry(new FormEntryInt(Tables.MEASURETYPE.SATISFACTION)
                        .setRequired()
                        .setInitialValue(measureType.getSatisfaction(), 0)
                        .setLabel("Satisfaction")
                        .setMin(0))
                .addEntry(new FormEntryInt(Tables.MEASURETYPE.PLUVIAL_PROTECTION_LEVEL)
                        .setRequired()
                        .setInitialValue(measureType.getPluvialProtectionLevel(), 0)
                        .setLabel("Pluvial protection level")
                        .setMin(0))
                .addEntry(new FormEntryInt(Tables.MEASURETYPE.FLUVIAL_PROTECTION_LEVEL)
                        .setRequired()
                        .setInitialValue(measureType.getFluvialProtectionLevel(), 0)
                        .setLabel("Fluvial protection level")
                        .setMin(0))
                .addEntry(new FormEntryUInt(Tables.MEASURETYPE.SCENARIO_ID)
                        .setInitialValue(scenarioId, UInteger.valueOf(0))
                        .setLabel("Scenario id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit MeasureType", form);
    }

}
