package nl.tudelft.simulation.housinggame.admin;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickRecord;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.GameversionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GrouproundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasuretypeRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PersonalmeasureRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerroundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioRecord;

public class MaintainPlayMeasure
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("play-measure"))
        {
            data.clearColumns("10%", "GameSession", "10%", "Group", "10%", "GroupRound", "10%", "Player", "10%", "PlayerRound",
                    "10%", "PersonalMeasure");
            data.clearFormColumn("40%", "Edit Properties");
            showGameSession(session, data, 0);
        }

        else if (click.contains("PlayMeasureGameSession"))
        {
            showGameSession(session, data, recordId);
        }

        else if (click.endsWith("PlayMeasureGroup"))
        {
            showGroup(session, data, recordId);
        }

        else if (click.contains("PlayMeasureGroupRound"))
        {
            showPlayGroupRound(session, data, recordId, true, !click.startsWith("view"));
        }

        else if (click.endsWith("PlayMeasurePlayer"))
        {
            showPlayer(session, data, recordId);
        }

        else if (click.contains("PlayMeasurePlayerRound"))
        {
            showPlayPlayerRound(session, data, recordId, false, !click.startsWith("view"));
        }

        else if (click.contains("PersonalMeasure"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.PERSONALMEASURE, "play-measure");
            else if (click.startsWith("delete"))
            {
                PersonalmeasureRecord personalMeasure = SqlUtils.readRecordFromId(data, Tables.PERSONALMEASURE, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(personalMeasure, "play-measure");
                else
                {
                    MeasuretypeRecord measureType =
                            SqlUtils.readRecordFromId(data, Tables.MEASURETYPE, personalMeasure.getMeasuretypeId());
                    data.askDeleteRecord(personalMeasure, "PersonalMeasure", measureType.getName(), "deletePersonalMeasureOk",
                            "play-measure");
                }
                recordId = 0;
            }
            if (!data.isError())
            {
                showPersonalMeasure(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editPersonalMeasure(session, data, 0, true);
            }
        }

        AdminServlet.makeColumnContent(data);
    }

    /*
     * *********************************************************************************************************
     * ****************************************** GAMESESSION **************************************************
     * *********************************************************************************************************
     */

    public static void showGameSession(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("PlayMeasureGameSession", 0, recordId, false, Tables.GAMESESSION, Tables.GAMESESSION.NAME, "name",
                false);
        data.resetColumn(1);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("PlayMeasureGroup", 1, 0, false, Tables.GROUP, Tables.GROUP.NAME, "name",
                    Tables.GROUP.GAMESESSION_ID, false);
        }
    }

    /*
     * *********************************************************************************************************
     * ********************************************** GROUP ****************************************************
     * *********************************************************************************************************
     */

    public static void showGroup(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("PlayMeasureGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayMeasureGroup", 1, recordId, false, Tables.GROUP, Tables.GROUP.NAME, "name",
                Tables.GROUP.GAMESESSION_ID, false);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("PlayMeasureGroupRound", 2, 0, false, Tables.GROUPROUND, Tables.GROUPROUND.ROUND_NUMBER,
                    "round_number", Tables.GROUPROUND.GROUP_ID, false);
        }
    }

    /*
     * *********************************************************************************************************
     * ******************************************** GROUPROUND *************************************************
     * *********************************************************************************************************
     */

    public static void showPlayGroupRound(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("PlayMeasureGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayMeasureGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("PlayMeasureGroupRound", 2, recordId, false, Tables.GROUPROUND, Tables.GROUPROUND.ROUND_NUMBER,
                "round_number", Tables.GROUPROUND.GROUP_ID, false);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            showPlayerColumn(data, "PlayMeasurePlayer", 3, recordId);
        }
    }

    /*
     * *********************************************************************************************************
     * ********************************************** PLAYER ***************************************************
     * *********************************************************************************************************
     */

    public static void showPlayer(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("PlayMeasureGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayMeasureGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("PlayMeasureGroupRound", 2, data.getColumn(2).getSelectedRecordId(), false, Tables.GROUPROUND,
                Tables.GROUPROUND.ROUND_NUMBER, "round_number", Tables.GROUPROUND.GROUP_ID, false);
        showPlayerColumn(data, "PlayMeasurePlayer", 3, recordId);
        data.resetColumn(4);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            showPlayerRoundColumn(data, "PlayMeasurePlayerRound", 4, 0);
        }
    }

    /*
     * *********************************************************************************************************
     * ******************************************* PLAYERROUND *************************************************
     * *********************************************************************************************************
     */

    public static void showPlayPlayerRound(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("PlayMeasureGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayMeasureGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("PlayMeasureGroupRound", 2, data.getColumn(2).getSelectedRecordId(), false, Tables.GROUPROUND,
                Tables.GROUPROUND.ROUND_NUMBER, "round_number", Tables.GROUPROUND.GROUP_ID, false);
        showPlayerColumn(data, "PlayMeasurePlayer", 3, data.getColumn(3).getSelectedRecordId());
        showPlayerRoundColumn(data, "PlayMeasurePlayerRound", 4, recordId);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            showPersonalMeasureColumn(data, "PersonalMeasure", 5, 0);
        }
    }

    /*
     * *********************************************************************************************************
     * ****************************************** PERSONALMEASURE **********************************************
     * *********************************************************************************************************
     */

    public static void showPersonalMeasure(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("PlayMeasureGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayMeasureGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("PlayMeasureGroupRound", 2, data.getColumn(2).getSelectedRecordId(), false, Tables.GROUPROUND,
                Tables.GROUPROUND.ROUND_NUMBER, "round_number", Tables.GROUPROUND.GROUP_ID, false);
        showPlayerColumn(data, "PlayMeasurePlayer", 3, data.getColumn(3).getSelectedRecordId());
        showPlayerRoundColumn(data, "PlayMeasurePlayerRound", 4, data.getColumn(4).getSelectedRecordId());
        showPersonalMeasureColumn(data, "PersonalMeasure", 5, recordId);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editPersonalMeasure(session, data, recordId, editRecord);
        }
    }

    public static void editPersonalMeasure(final HttpSession session, final AdminData data, final int personalMeasureId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        PersonalmeasureRecord personalMeasure =
                personalMeasureId == 0 ? dslContext.newRecord(Tables.PERSONALMEASURE) : dslContext
                        .selectFrom(Tables.PERSONALMEASURE).where(Tables.PERSONALMEASURE.ID.eq(personalMeasureId)).fetchOne();
        int playerRoundId =
                personalMeasureId == 0 ? data.getColumn(4).getSelectedRecordId() : personalMeasure.getPlayerroundId();
        PlayerroundRecord playerRound = SqlUtils.readRecordFromId(data, Tables.PLAYERROUND, playerRoundId);
        GrouproundRecord groupRound = SqlUtils.readRecordFromId(data, Tables.GROUPROUND, playerRound.getGrouproundId());
        GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, groupRound.getGroupId());
        ScenarioRecord scenario = SqlUtils.readRecordFromId(data, Tables.SCENARIO, group.getScenarioId());
        GameversionRecord gameVersion = SqlUtils.readRecordFromId(data, Tables.GAMEVERSION, scenario.getGameversionId());

        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("play-measure", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editPersonalMeasure")
                .setSaveMethod("savePersonalMeasure")
                .setDeleteMethod("deletePersonalMeasure", "Delete", "<br>Note: Adjust the finances of the player "
                        + "<br>when a personal measure is deleted")
                .setRecordNr(personalMeasureId)
                .startForm()
                .addEntry(new TableEntryPickRecord(Tables.PERSONALMEASURE.MEASURETYPE_ID)
                        .setRequired()
                        .setPickTable(data, Tables.MEASURETYPE.join(Tables.MEASURECATEGORY)
                                .on(Tables.MEASURETYPE.MEASURECATEGORY_ID.eq(Tables.MEASURECATEGORY.ID))
                                .and(Tables.MEASURECATEGORY.GAMEVERSION_ID.eq(gameVersion.getId()))
                                .and(Tables.MEASURETYPE.HOUSE_MEASURE.eq((byte) 0)),
                                Tables.MEASURETYPE.ID, Tables.MEASURETYPE.NAME)
                        .setInitialValue(personalMeasure.getMeasuretypeId(), null)
                        .setLabel("Measure type"))
                .addEntry(new TableEntryInt(Tables.PERSONALMEASURE.PLAYERROUND_ID)
                        .setInitialValue(playerRoundId, 0)
                        .setLabel("PlayerRound id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Personal Measure", form);
    }

    /*
     * *********************************************************************************************************
     * ****************************************** SUPPORT METHODS **********************************************
     * *********************************************************************************************************
     */

    public static void showPlayerColumn(final AdminData data, final String columnName, final int columnNr, final int recordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<PlayerRecord> records = dslContext.selectFrom(Tables.PLAYER)
                .where(Tables.PLAYER.GROUP_ID.eq(data.getColumn(1).getSelectedRecordId())).fetch();

        s.append(AdminTable.startTable());
        for (PlayerRecord player : records)
        {
            TableRow tableRow = new TableRow(IdProvider.getId(player), recordId, player.getCode(), "view" + columnName);
            s.append(tableRow.process());
        }
        s.append(AdminTable.endTable());

        data.getColumn(columnNr).setSelectedRecordId(recordId);
        data.getColumn(columnNr).setContent(s.toString());
    }

    public static void showPlayerRoundColumn(final AdminData data, final String columnName, final int columnNr,
            final int recordId)
    {
        // there is maximally ONE playerround record for the selection of a player and a groupround
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<PlayerroundRecord> records = dslContext.selectFrom(Tables.PLAYERROUND)
                .where(Tables.PLAYERROUND.GROUPROUND_ID.eq(data.getColumn(2).getSelectedRecordId())
                        .and(Tables.PLAYERROUND.PLAYER_ID.eq(data.getColumn(3).getSelectedRecordId())))
                .fetch();
        SortedMap<String, PlayerroundRecord> sorted = new TreeMap<>();
        for (PlayerroundRecord record : records)
        {
            PlayerRecord player =
                    dslContext.selectFrom(Tables.PLAYER.where(Tables.PLAYER.ID.eq(record.getPlayerId()))).fetchOne();
            sorted.put(player.getCode(), record);
        }

        s.append(AdminTable.startTable());
        for (String code : sorted.keySet())
        {
            PlayerroundRecord record = sorted.get(code);
            TableRow tableRow = new TableRow(IdProvider.getId(record), recordId, code, "view" + columnName);
            s.append(tableRow.process());
        }
        s.append(AdminTable.endTable());

        data.getColumn(columnNr).setSelectedRecordId(recordId);
        data.getColumn(columnNr).setContent(s.toString());
    }

    public static void showPersonalMeasureColumn(final AdminData data, final String columnName, final int columnNr,
            final int recordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<PersonalmeasureRecord> records = dslContext.selectFrom(Tables.PERSONALMEASURE)
                .where(Tables.PERSONALMEASURE.PLAYERROUND_ID.eq(data.getColumn(columnNr - 1).getSelectedRecordId())).fetch();

        s.append(AdminTable.startTable());
        for (PersonalmeasureRecord record : records)
        {
            MeasuretypeRecord measureType = dslContext
                    .selectFrom(Tables.MEASURETYPE.where(Tables.MEASURETYPE.ID.eq(record.getMeasuretypeId()))).fetchOne();
            TableRow tableRow = new TableRow(IdProvider.getId(record), recordId, measureType.getName(), "view" + columnName);
            tableRow.addButton("Edit", "edit" + columnName);
            s.append(tableRow.process());
        }
        s.append(AdminTable.endTable());
        s.append(AdminTable.finalButton("New Personal Measure", "new" + columnName));

        data.getColumn(columnNr).setSelectedRecordId(recordId);
        data.getColumn(columnNr).setContent(s.toString());
    }

}
