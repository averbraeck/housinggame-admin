package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.admin.form.table.FormEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.FormEntryUInt;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.WelfaretypeRecord;

public class MaintainWelfareType
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("welfaretype"))
        {
            data.clearColumns("15%", "GameVersion", "15%", "Scenario", "15%", "WelfareType");
            data.clearFormColumn("55%", "Edit Properties");
            showGameVersion(session, data, 0);
        }

        else if (click.contains("WelfareTypeGameVersion"))
        {
            showGameVersion(session, data, recordId);
        }

        else if (click.contains("WelfareTypeScenario"))
        {
            showScenario(session, data, recordId);
        }

        else if (click.contains("WelfareType"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.WELFARETYPE, "welfaretype");
            else if (click.startsWith("delete"))
            {
                WelfaretypeRecord welfareType = SqlUtils.readRecordFromId(data, Tables.WELFARETYPE, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(welfareType, "welfaretype");
                else
                    data.deleteRecord(welfareType, "WelfareType", String.valueOf(welfareType.getName()), "deleteWelfareTypeOk",
                            "welfaretype");
                recordId = 0;
            }
            if (!data.isError())
            {
                showWelfareType(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editWelfareType(session, data, 0, true);
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
        data.showColumn("WelfareTypeGameVersion", 0, recordId, false, Tables.GAMEVERSION, Tables.GAMEVERSION.NAME, "name",
                false);
        data.resetColumn(1);
        data.resetColumn(2);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("WelfareTypeScenario", 1, 0, false, Tables.SCENARIO, Tables.SCENARIO.NAME, "name",
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
        data.showColumn("WelfareTypeGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("WelfareTypeScenario", 1, recordId, false, Tables.SCENARIO, Tables.SCENARIO.NAME, "name",
                Tables.SCENARIO.GAMEVERSION_ID, false);
        data.resetColumn(2);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("WelfareType", 2, 0, true, Tables.WELFARETYPE, Tables.WELFARETYPE.NAME, "name",
                    Tables.WELFARETYPE.SCENARIO_ID, true);
        }
    }

    /*
     * *********************************************************************************************************
     * ***************************************** WELFARETYPE **********************************************
     * *********************************************************************************************************
     */

    public static void showWelfareType(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("WelfareTypeGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("WelfareTypeScenario", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.SCENARIO,
                Tables.SCENARIO.NAME, "name", Tables.SCENARIO.GAMEVERSION_ID, false);
        data.showDependentColumn("WelfareType", 2, 0, true, Tables.WELFARETYPE, Tables.WELFARETYPE.NAME, "name",
                Tables.WELFARETYPE.SCENARIO_ID, true);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editWelfareType(session, data, recordId, editRecord);
        }
    }

    public static void editWelfareType(final HttpSession session, final AdminData data, final int welfareTypeId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        WelfaretypeRecord welfareType = welfareTypeId == 0 ? dslContext.newRecord(Tables.WELFARETYPE) : dslContext
                .selectFrom(Tables.WELFARETYPE).where(Tables.WELFARETYPE.ID.eq(UInteger.valueOf(welfareTypeId))).fetchOne();
        UInteger scenarioId =
                welfareTypeId == 0 ? UInteger.valueOf(data.getColumn(1).getSelectedRecordId()) : welfareType.getScenarioId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("welfaretype", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editWelfareType")
                .setSaveMethod("saveWelfareType")
                .setDeleteMethod("deleteWelfareType", "Delete", "<br>Note: Do not delete welfare type when"
                        + "<br> game with this welfare type has been played")
                .setRecordNr(welfareTypeId)
                .startForm()
                .addEntry(new FormEntryString(Tables.WELFARETYPE.NAME)
                        .setRequired()
                        .setInitialValue(welfareType.getName(), "")
                        .setLabel("WelfareType identifying name")
                        .setMaxChars(255))
                .addEntry(new FormEntryUInt(Tables.WELFARETYPE.INITIAL_SATISFACTION)
                        .setRequired()
                        .setInitialValue(welfareType.getInitialSatisfaction(), UInteger.valueOf(0))
                        .setLabel("Initial Satisfaction")
                        .setMin(0))
                .addEntry(new FormEntryUInt(Tables.WELFARETYPE.INITIAL_MONEY)
                        .setRequired()
                        .setInitialValue(welfareType.getInitialMoney(), UInteger.valueOf(0))
                        .setLabel("Initial Money")
                        .setMin(0))
                .addEntry(new FormEntryUInt(Tables.WELFARETYPE.MAXIMUM_MORTGAGE)
                        .setRequired()
                        .setInitialValue(welfareType.getMaximumMortgage(), UInteger.valueOf(0))
                        .setLabel("Maximum Mortgage")
                        .setMin(0))
                .addEntry(new FormEntryUInt(Tables.WELFARETYPE.LIVING_COSTS)
                        .setRequired()
                        .setInitialValue(welfareType.getLivingCosts(), UInteger.valueOf(0))
                        .setLabel("Living Costs")
                        .setMin(0))
                .addEntry(new FormEntryUInt(Tables.WELFARETYPE.INCOME)
                        .setRequired()
                        .setInitialValue(welfareType.getIncome(), UInteger.valueOf(0))
                        .setLabel("Income")
                        .setMin(0))
                .addEntry(new FormEntryUInt(Tables.WELFARETYPE.SATISFACTION_COST_PER_POINT)
                        .setRequired()
                        .setInitialValue(welfareType.getSatisfactionCostPerPoint(), UInteger.valueOf(0))
                        .setLabel("Satisfaction Cost per Point")
                        .setMin(0))
                .addEntry(new FormEntryUInt(Tables.WELFARETYPE.PREFERRED_HOUSE_RATING)
                        .setRequired()
                        .setInitialValue(welfareType.getPreferredHouseRating(), UInteger.valueOf(0))
                        .setLabel("Preferred House Rating")
                        .setMin(0))
                .addEntry(new FormEntryUInt(Tables.WELFARETYPE.SCENARIO_ID)
                        .setInitialValue(scenarioId, UInteger.valueOf(0))
                        .setLabel("Scenario id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit WelfareType", form);
    }

}
