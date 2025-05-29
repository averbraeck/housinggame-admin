package nl.tudelft.simulation.housinggame.admin;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryBoolean;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryDouble;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryText;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasurecategoryRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasuretypeRecord;

public class MaintainMeasureType
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("measuretype"))
        {
            data.clearColumns("15%", "GameVersion", "15%", "Scenario", "15%", "MeasureCategory", "15%", "MeasureType");
            data.clearFormColumn("40%", "Edit Properties");
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

        else if (click.contains("MeasureCategory"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.MEASURECATEGORY, "measurecategory");
            else if (click.startsWith("delete"))
            {
                MeasurecategoryRecord measureCategory = AdminUtils.readRecordFromId(data, Tables.MEASURECATEGORY, recordId);
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

        else if (click.contains("MeasureType"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.MEASURETYPE, "measuretype");
            else if (click.startsWith("delete"))
            {
                MeasuretypeRecord measureType = AdminUtils.readRecordFromId(data, Tables.MEASURETYPE, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(measureType, "measuretype");
                else
                    data.askDeleteRecord(measureType, "MeasureType", String.valueOf(measureType.getName()),
                            "deleteMeasureTypeOk", "measuretype");
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
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("MeasureTypeScenario", 1, 0, false, Tables.SCENARIO, Tables.SCENARIO.NAME, "name",
                    Tables.SCENARIO.GAMEVERSION_ID, false);
        }
    }

    /*
     * *********************************************************************************************************
     * ******************************************** SCENARIO ***************************************************
     * *********************************************************************************************************
     */

    public static void showScenario(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("MeasureTypeGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("MeasureTypeScenario", 1, recordId, false, Tables.SCENARIO, Tables.SCENARIO.NAME, "name",
                Tables.SCENARIO.GAMEVERSION_ID, false);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("MeasureCategory", 2, 0, true, Tables.MEASURECATEGORY, Tables.MEASURECATEGORY.NAME, "name",
                    Tables.MEASURECATEGORY.SCENARIO_ID, true);
        }
    }

    /*
     * *********************************************************************************************************
     * ***************************************** MEASURECATEGORY ***********************************************
     * *********************************************************************************************************
     */

    public static void showMeasureCategory(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("MeasureTypeGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("MeasureTypeScenario", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.SCENARIO,
                Tables.SCENARIO.NAME, "name", Tables.SCENARIO.GAMEVERSION_ID, false);
        data.showDependentColumn("MeasureCategory", 2, recordId, true, Tables.MEASURECATEGORY, Tables.MEASURECATEGORY.NAME,
                "name", Tables.MEASURECATEGORY.SCENARIO_ID, true);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("MeasureType", 3, 0, true, Tables.MEASURETYPE, Tables.MEASURETYPE.NAME, "name",
                    Tables.MEASURETYPE.MEASURECATEGORY_ID, true);
            editMeasureCategory(session, data, recordId, editRecord);
        }
    }

    public static void editMeasureCategory(final HttpSession session, final AdminData data, final int measureCategoryId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        MeasurecategoryRecord measureCategory =
                measureCategoryId == 0 ? dslContext.newRecord(Tables.MEASURECATEGORY) : dslContext
                        .selectFrom(Tables.MEASURECATEGORY).where(Tables.MEASURECATEGORY.ID.eq(measureCategoryId)).fetchOne();
        int scenarioId = measureCategoryId == 0 ? data.getColumn(1).getSelectedRecordId() : measureCategory.getScenarioId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("measurecategory", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editMeasureCategory")
                .setSaveMethod("saveMeasureCategory")
                .setDeleteMethod("deleteMeasureCategory", "Delete", "<br>Note: Do not delete measurecategory when"
                        + "<br> measure types for this measurecategory exist")
                .setRecordNr(measureCategoryId)
                .startForm()
                .setLabelLength("40%")
                .setFieldLength("60%")
                .addEntry(new TableEntryDouble(Tables.MEASURECATEGORY.SEQUENCE_NR)
                        .setRequired()
                        .setInitialValue(measureCategory.getSequenceNr(), 1.0)
                        .setLabel("Sequence nr")
                        .setMin(0.0))
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
                .addEntry(new TableEntryInt(Tables.MEASURECATEGORY.SCENARIO_ID)
                        .setInitialValue(scenarioId, 0)
                        .setLabel("Scenario id")
                        .setHidden(true))
                .endForm();
        //@formatter:on

        data.getFormColumn().setHeaderForm("Edit MeasureCategory", form);
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
        data.showDependentColumn("MeasureCategory", 2, data.getColumn(2).getSelectedRecordId(), true, Tables.MEASURECATEGORY,
                Tables.MEASURECATEGORY.NAME, "name", Tables.MEASURECATEGORY.SCENARIO_ID, true);
        data.showDependentColumn("MeasureType", 3, recordId, true, Tables.MEASURETYPE, Tables.MEASURETYPE.NAME, "name",
                Tables.MEASURETYPE.MEASURECATEGORY_ID, true);
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
        MeasuretypeRecord measureType = measureTypeId == 0 ? dslContext.newRecord(Tables.MEASURETYPE)
                : dslContext.selectFrom(Tables.MEASURETYPE).where(Tables.MEASURETYPE.ID.eq(measureTypeId)).fetchOne();
        int measureCategoryId =
                measureTypeId == 0 ? data.getColumn(2).getSelectedRecordId() : measureType.getMeasurecategoryId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("measuretype", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editMeasureType")
                .setSaveMethod("saveMeasureType")
                .setDeleteMethod("deleteMeasureType", "Delete", "<br>Note: MeasureType can only be deleted when it "
                        + "<br>has not been used in a scenario")
                .setRecordNr(measureTypeId)
                .startForm()
                .setLabelLength("40%")
                .setFieldLength("60%")
                .addEntry(new TableEntryString(Tables.MEASURETYPE.SHORT_ALIAS)
                        .setRequired()
                        .setInitialValue(measureType.getShortAlias(), "")
                        .setLabel("Short alias")
                        .setMaxChars(24))
                .addEntry(new TableEntryString(Tables.MEASURETYPE.NAME)
                        .setRequired()
                        .setInitialValue(measureType.getName(), "")
                        .setLabel("Name")
                        .setMaxChars(255))
                .addEntry(new TableEntryText(Tables.MEASURETYPE.DESCRIPTION)
                        .setRequired()
                        .setInitialValue(measureType.getDescription(), "")
                        .setLabel("Description"))
                .addEntry(new TableEntryDouble(Tables.MEASURETYPE.CATEGORY_SEQUENCE_NR)
                        .setRequired()
                        .setInitialValue(measureType.getCategorySequenceNr(), 1.0)
                        .setLabel("Category sequence nr")
                        .setMin(0.0))
                .addEntry(new TableEntryDouble(Tables.MEASURETYPE.PRIORITY)
                        .setRequired()
                        .setInitialValue(measureType.getPriority(), 1.0)
                        .setLabel("Priority")
                        .setMin(0.0))
                .addEntry(new TableEntryInt(Tables.MEASURETYPE.COST_ABSOLUTE)
                        .setRequired()
                        .setInitialValue(measureType.getCostAbsolute(), 0)
                        .setLabel("Absolute cost")
                        .setMin(0))
                .addEntry(new TableEntryDouble(Tables.MEASURETYPE.COST_PERCENTAGE_HOUSE)
                        .setRequired()
                        .setInitialValue(measureType.getCostPercentageHouse(), 0.0)
                        .setLabel("Cost as % of house value")
                        .setMin(0.0))
                .addEntry(new TableEntryDouble(Tables.MEASURETYPE.COST_PERCENTAGE_INCOME)
                        .setRequired()
                        .setInitialValue(measureType.getCostPercentageIncome(), 0.0)
                        .setLabel("Cost as % of income")
                        .setMin(0.0))
                .addEntry(new TableEntryInt(Tables.MEASURETYPE.SATISFACTION_DELTA_ONCE)
                        .setRequired()
                        .setInitialValue(measureType.getSatisfactionDeltaOnce(), 0)
                        .setLabel("Satisfaction delta once"))
                .addEntry(new TableEntryInt(Tables.MEASURETYPE.SATISFACTION_DELTA_PERMANENT)
                        .setRequired()
                        .setInitialValue(measureType.getSatisfactionDeltaPermanent(), 0)
                        .setLabel("Satisfaction delta permanent"))
                .addEntry(new TableEntryInt(Tables.MEASURETYPE.FLUVIAL_PROTECTION_DELTA)
                        .setRequired()
                        .setInitialValue(measureType.getFluvialProtectionDelta(), 0)
                        .setLabel("Fluvial protection delta"))
                .addEntry(new TableEntryInt(Tables.MEASURETYPE.PLUVIAL_PROTECTION_DELTA)
                        .setRequired()
                        .setInitialValue(measureType.getPluvialProtectionDelta(), 0)
                        .setLabel("Pluvial protection delta"))
                .addEntry(new TableEntryInt(Tables.MEASURETYPE.HOUSE_VALUE_DELTA_ABS)
                        .setRequired()
                        .setInitialValue(measureType.getHouseValueDeltaAbs(), 0)
                        .setLabel("House value delta abs"))
                .addEntry(new TableEntryDouble(Tables.MEASURETYPE.HOUSE_VALUE_DELTA_PERC)
                        .setRequired()
                        .setInitialValue(measureType.getHouseValueDeltaPerc(), 0.0)
                        .setLabel("House value delta perc"))
                .addEntry(new TableEntryBoolean(Tables.MEASURETYPE.VALID_ONE_ROUND)
                        .setRequired()
                        .setInitialValue(measureType.getValidOneRound(), (byte) 0)
                        .setLabel("Valid for one round only?"))
                .addEntry(new TableEntryBoolean(Tables.MEASURETYPE.VALID_UNTIL_USED)
                        .setRequired()
                        .setInitialValue(measureType.getValidUntilUsed(), (byte) 0)
                        .setLabel("Valid until used?"))
                .addEntry(new TableEntryBoolean(Tables.MEASURETYPE.HOUSE_MEASURE)
                        .setRequired()
                        .setInitialValue(measureType.getHouseMeasure(), (byte) 1)
                        .setLabel("House measure [T] / personal [F]"))
                .addEntry(new TableEntryInt(Tables.MEASURETYPE.MEASURECATEGORY_ID)
                        .setInitialValue(measureCategoryId, 0)
                        .setLabel("Measure category id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit MeasureType", form);
    }

}
