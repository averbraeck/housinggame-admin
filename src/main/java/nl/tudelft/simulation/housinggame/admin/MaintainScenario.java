package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickRecord;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryText;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.GameversionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioRecord;

public class MaintainScenario
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("scenario"))
        {
            data.clearColumns("25%", "GameVersion", "25%", "Scenario");
            data.clearFormColumn("50%", "Edit Properties");
            showGameVersion(session, data, 0, true, false);
        }

        else if (click.contains("GameVersion"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.GAMEVERSION, "scenario");
            else if (click.startsWith("delete"))
            {
                GameversionRecord gameVersion = AdminUtils.readRecordFromId(data, Tables.GAMEVERSION, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(gameVersion, "scenario");
                else
                    data.askDeleteRecord(gameVersion, "GameVersion", gameVersion.getName(), "deleteGameVersionOk", "scenario");
                recordId = 0;
            }
            else if (click.startsWith("clone"))
            {
                GameversionRecord gameVersion = AdminUtils.readRecordFromId(data, Tables.GAMEVERSION, recordId);
                try
                {
                    AdminUtils.cloneGameVersion(data, gameVersion);
                }
                catch (Exception e)
                {
                    System.err.println(e.getMessage());
                    ModalWindowUtils.popup(data, "Error cloning GameVersion",
                            e.getClass().getSimpleName() + ": " + e.getMessage(), "clickMenu('scenario')");
                    data.setError(true);
                }
            }
            else if (click.startsWith("destroy"))
            {
                GameversionRecord gameVersion = AdminUtils.readRecordFromId(data, Tables.GAMEVERSION, recordId);
                try
                {
                    if (click.endsWith("Ok"))
                        AdminUtils.destroyGameVersion(data, gameVersion);
                    else
                        data.askDestroyRecord(gameVersion, "GameVersion", gameVersion.getName(), "destroyGameVersionOk",
                                "scenario");
                    recordId = 0;
                }
                catch (Exception e)
                {
                    System.err.println(e.getMessage());
                    ModalWindowUtils.popup(data, "Error destroying GameVersion",
                            e.getClass().getSimpleName() + ": " + e.getMessage(), "clickMenu('scenario')");
                    data.setError(true);
                }
            }
            if (!data.isError())
            {
                showGameVersion(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editGameVersion(session, data, 0, true);
            }
        }

        else if (click.contains("Scenario"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.SCENARIO, "scenario");
            else if (click.startsWith("delete"))
            {
                ScenarioRecord scenario = AdminUtils.readRecordFromId(data, Tables.SCENARIO, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(scenario, "scenario");
                else
                    data.askDeleteRecord(scenario, "Scenario", scenario.getName(), "deleteScenarioOk", "scenario");
                recordId = 0;
            }
            else if (click.startsWith("clone"))
            {
                ScenarioRecord scenario = AdminUtils.readRecordFromId(data, Tables.SCENARIO, recordId);
                try
                {
                    AdminUtils.cloneScenario(data, scenario);
                }
                catch (Exception e)
                {
                    System.err.println(e.getMessage());
                    ModalWindowUtils.popup(data, "Error cloning Scenario", e.getClass().getSimpleName() + ": " + e.getMessage(),
                            "clickMenu('scenario')");
                    data.setError(true);
                }
            }
            else if (click.startsWith("destroy"))
            {
                ScenarioRecord scenario = AdminUtils.readRecordFromId(data, Tables.SCENARIO, recordId);
                try
                {
                    if (click.endsWith("Ok"))
                        AdminUtils.destroyScenario(data, scenario);
                    else
                        data.askDestroyRecord(scenario, "Scenario", scenario.getName(), "destroyScenarioOk", "scenario");
                    recordId = 0;
                }
                catch (Exception e)
                {
                    System.err.println(e.getMessage());
                    ModalWindowUtils.popup(data, "Error destroying Scenario",
                            e.getClass().getSimpleName() + ": " + e.getMessage(), "clickMenu('scenario')");
                    data.setError(true);
                }
            }
            if (!data.isError())
            {
                showScenario(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editScenario(session, data, 0, true);
            }
        }

        AdminServlet.makeColumnContent(data);
    }

    /*
     * *********************************************************************************************************
     * ****************************************** GAMEVERSION **************************************************
     * *********************************************************************************************************
     */

    public static void showGameVersion(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("GameVersion", 0, recordId, editButton, Tables.GAMEVERSION, Tables.GAMEVERSION.NAME, "name", true);
        data.resetColumn(1);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("Scenario", 1, 0, true, Tables.SCENARIO, Tables.SCENARIO.NAME, "name",
                    Tables.SCENARIO.GAMEVERSION_ID, true);
            editGameVersion(session, data, recordId, editRecord);
        }
    }

    public static void editGameVersion(final HttpSession session, final AdminData data, final int gameVersionId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        GameversionRecord gameVersion = gameVersionId == 0 ? dslContext.newRecord(Tables.GAMEVERSION)
                : dslContext.selectFrom(Tables.GAMEVERSION).where(Tables.GAMEVERSION.ID.eq(gameVersionId)).fetchOne();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("scenario", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editGameVersion")
                .setSaveMethod("saveGameVersion")
                .setDeleteMethod("deleteGameVersion", "Delete", "<br>Note: Game Version can only be deleted when it is has"
                        + "<br> not been used, and when it has no associated scenarios")
                .setRecordNr(gameVersionId)
                .startForm()
                .addEntry(new TableEntryString(Tables.GAMEVERSION.NAME)
                        .setRequired()
                        .setInitialValue(gameVersion.getName(), "")
                        .setLabel("Game Version name")
                        .setMaxChars(255))
                .addEntry(new TableEntryText(Tables.GAMEVERSION.DESCRIPTION)
                        .setRequired(false)
                        .setInitialValue(gameVersion.getDescription(), "")
                        .setLabel("Game Description")
                        .setRows(10))
                .addEntry(new TableEntryPickRecord(Tables.GAMEVERSION.LANGUAGEGROUP_ID)
                        .setRequired()
                        .setPickTable(data, Tables.LANGUAGEGROUP, Tables.LANGUAGEGROUP.ID,
                                Tables.LANGUAGEGROUP.NAME)
                        .setInitialValue(gameVersion.getLanguagegroupId(), 0)
                        .setLabel("Game languages"));
        //@formatter:on
        form.addAddtionalButton("cloneGameVersion", "Clone Entire GameVersion");
        form.addAddtionalButton("destroyGameVersion", "Destroy Entire GameVersion");
        form.endForm();
        data.getFormColumn().setHeaderForm("Edit Game Version", form);
    }

    /*
     * *********************************************************************************************************
     * ********************************************* SCENARIO **************************************************
     * *********************************************************************************************************
     */

    public static void showScenario(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("GameVersion", 0, data.getColumn(0).getSelectedRecordId(), editButton, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", true);
        data.showDependentColumn("Scenario", 1, recordId, editButton, Tables.SCENARIO, Tables.SCENARIO.NAME, "name",
                Tables.SCENARIO.GAMEVERSION_ID, true);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editScenario(session, data, recordId, editRecord);
        }
    }

    public static void editScenario(final HttpSession session, final AdminData data, final int scenarioId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        ScenarioRecord scenario = scenarioId == 0 ? dslContext.newRecord(Tables.SCENARIO)
                : dslContext.selectFrom(Tables.SCENARIO).where(Tables.SCENARIO.ID.eq(scenarioId)).fetchOne();
        int gameVersionId = scenarioId == 0 ? data.getColumn(0).getSelectedRecordId() : scenario.getGameversionId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("scenario", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editScenario")
                .setSaveMethod("saveScenario")
                .setDeleteMethod("deleteScenario", "Delete", "<br>Note: Scenario can only be deleted when it is has not"
                        + "<br> been used, and when it has no roles, rounds, groups, params")
                .setRecordNr(scenarioId)
                .startForm()
                .addEntry(new TableEntryString(Tables.SCENARIO.NAME)
                        .setRequired()
                        .setInitialValue(scenario.getName(), "")
                        .setLabel("Scenario name")
                        .setMaxChars(45))
                .addEntry(new TableEntryText(Tables.SCENARIO.DESCRIPTION)
                        .setRequired(false)
                        .setInitialValue(scenario.getDescription(), "")
                        .setLabel("Scenario description")
                        .setRows(10))
                .addEntry(new TableEntryInt(Tables.SCENARIO.INFORMATION_AMOUNT)
                        .setRequired()
                        .setInitialValue(scenario.getInformationAmount(), 0)
                        .setLabel("Information amount")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.SCENARIO.GAMEVERSION_ID)
                        .setInitialValue(gameVersionId, 0)
                        .setLabel("GameVersion id")
                        .setHidden(true))
                .addEntry(new TableEntryInt(Tables.SCENARIO.MINIMUM_PLAYERS)
                        .setRequired()
                        .setInitialValue(scenario.getMinimumPlayers(), 0)
                        .setLabel("Minimum # of players")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.SCENARIO.MAXIMUM_PLAYERS)
                        .setRequired()
                        .setInitialValue(scenario.getMaximumPlayers(), 0)
                        .setLabel("Maximum # of players")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.SCENARIO.HIGHEST_ROUND_NUMBER)
                        .setRequired()
                        .setInitialValue(scenario.getHighestRoundNumber(), 0)
                        .setLabel("Highest round number")
                        .setMin(0))
                .addEntry(new TableEntryPickRecord(Tables.SCENARIO.SCENARIOPARAMETERS_ID)
                        .setRequired()
                        .setPickTable(data, Tables.SCENARIOPARAMETERS, Tables.SCENARIOPARAMETERS.ID,
                                Tables.SCENARIOPARAMETERS.NAME)
                        .setInitialValue(scenario.getScenarioparametersId(), 0)
                        .setLabel("Scenario parameters"));
        //@formatter:on
        form.addAddtionalButton("cloneScenario", "Clone Entire Scenario");
        form.addAddtionalButton("destroyScenario", "Destroy Entire Scenario");
        form.endForm();
        data.getFormColumn().setHeaderForm("Edit Scenario", form);
    }

}
