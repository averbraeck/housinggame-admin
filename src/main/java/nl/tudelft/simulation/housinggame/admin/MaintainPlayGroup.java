package nl.tudelft.simulation.housinggame.admin;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryDateTime;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickList;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryText;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.common.GroupState;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GrouproundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupstateRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioparametersRecord;

public class MaintainPlayGroup
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("play-group"))
        {
            data.clearColumns("15%", "GameSession", "15%", "Group", "15%", "GroupRound", "15%", "GroupState");
            data.clearFormColumn("40%", "Edit Properties");
            showGameSession(session, data, 0);
        }

        else if (click.contains("PlayGroupGameSession"))
        {
            showGameSession(session, data, recordId);
        }

        else if (click.endsWith("PlayGroupGroup"))
        {
            showGroup(session, data, recordId);
        }

        else if (click.contains("PlayGroupGroupRound"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.GROUPROUND, "play-group");
            else if (click.startsWith("delete"))
            {
                GrouproundRecord groupRound = AdminUtils.readRecordFromId(data, Tables.GROUPROUND, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(groupRound, "play-group");
                else
                {
                    data.askDeleteRecord(groupRound, "PlayGroupGroupRound", String.valueOf(groupRound.getRoundNumber()),
                            "deletePlayGroupGroupRoundOk", "play-group");
                }
                recordId = 0;
            }
            if (!data.isError())
            {
                showPlayGroupRound(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editPlayGroupRound(session, data, 0, true);
            }
        }

        else if (click.contains("PlayGroupState"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.GROUPSTATE, "play-group");
            else if (click.startsWith("delete"))
            {
                GroupstateRecord groupState = AdminUtils.readRecordFromId(data, Tables.GROUPSTATE, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(groupState, "play-group");
                else
                {
                    GrouproundRecord groupRound =
                            AdminUtils.readRecordFromId(data, Tables.GROUPROUND, groupState.getGrouproundId());
                    data.askDeleteRecord(groupState, "PlayGroupState", "Round " + groupRound.getRoundNumber(),
                            "deletePlayGroupStateOk", "play-group");
                }
                recordId = 0;
            }
            if (!data.isError())
            {
                showPlayGroupState(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editPlayGroupState(session, data, 0, true);
            }
        }

        else if (click.contains("destroyGamePlayGroup"))
        {
            GroupRecord group = AdminUtils.readRecordFromId(data, Tables.GROUP, data.getColumn(1).getSelectedRecordId());
            if (click.endsWith("Ok"))
            {
                AdminUtils.destroyGamePlay(data, data.getColumn(1).getSelectedRecordId());
                showGroup(session, data, 0);
            }
            else
            {
                data.askDestroyRecord(group, "GamePlay for Group", group.getName(), "destroyGamePlayGroupOk", "play-group");
            }
            recordId = 0;
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
        data.showColumn("PlayGroupGameSession", 0, recordId, false, Tables.GAMESESSION, Tables.GAMESESSION.NAME, "name", false);
        data.resetColumn(1);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("PlayGroupGroup", 1, 0, false, Tables.GROUP, Tables.GROUP.NAME, "name",
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
        data.showColumn("PlayGroupGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayGroupGroup", 1, recordId, false, Tables.GROUP, Tables.GROUP.NAME, "name",
                Tables.GROUP.GAMESESSION_ID, false);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("PlayGroupGroupRound", 2, 0, true, Tables.GROUPROUND, Tables.GROUPROUND.ROUND_NUMBER,
                    "round_number", Tables.GROUPROUND.GROUP_ID, true, "GroupRound");
            data.getColumn(1).addContent(AdminTable.finalButton("DEL GAMEPLAY", "destroyGamePlayGroup"));
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
        data.showColumn("PlayGroupGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayGroupGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("PlayGroupGroupRound", 2, recordId, true, Tables.GROUPROUND, Tables.GROUPROUND.ROUND_NUMBER,
                "round_number", Tables.GROUPROUND.GROUP_ID, true, "GroupRound");
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            showGroupStateColumn(data, "PlayGroupState", 3, 0);
            editPlayGroupRound(session, data, recordId, editRecord);
        }
    }

    public static void editPlayGroupRound(final HttpSession session, final AdminData data, final int groupRoundId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        GrouproundRecord groupRound = groupRoundId == 0 ? dslContext.newRecord(Tables.GROUPROUND)
                : dslContext.selectFrom(Tables.GROUPROUND).where(Tables.GROUPROUND.ID.eq(groupRoundId)).fetchOne();
        int groupId = groupRoundId == 0 ? data.getColumn(1).getSelectedRecordId() : groupRound.getGroupId();
        GroupRecord group = AdminUtils.readRecordFromId(data, Tables.GROUP, groupId);
        ScenarioRecord scenario = AdminUtils.readRecordFromId(data, Tables.SCENARIO, group.getScenarioId());
        ScenarioparametersRecord parameters =
                AdminUtils.readRecordFromId(data, Tables.SCENARIOPARAMETERS, scenario.getScenarioparametersId());
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("play-group", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editPlayGroupGroupRound")
                .setSaveMethod("savePlayGroupGroupRound")
                .setDeleteMethod("deletePlayGroupGroupRound", "Delete", "<br>Note: GroupRound can only be deleted when it "
                        + "<br>has not been used for play by a player")
                .setRecordNr(groupRoundId)
                .startForm()
                .addEntry(new TableEntryInt(Tables.GROUPROUND.ROUND_NUMBER)
                        .setRequired()
                        .setInitialValue(groupRound.getRoundNumber(), 0)
                        .setLabel("Round number")
                        .setMin(0)
                        .setMax(scenario.getHighestRoundNumber()))
                .addEntry(new TableEntryPickList(Tables.GROUPROUND.GROUP_STATE)
                        .setRequired()
                        .setPickListEntries(GroupState.class)
                        .setInitialValue(groupRound.getGroupState(), "")
                        .setLabel("Round State"))
                .addEntry(new TableEntryInt(Tables.GROUPROUND.FLUVIAL_FLOOD_INTENSITY)
                        .setRequired()
                        .setInitialValue(groupRound.getFluvialFloodIntensity(), 0)
                        .setLabel("Fluvial flood intensity")
                        .setMin(0)
                        .setMax(parameters.getHighestFluvialScore().intValue()))
                .addEntry(new TableEntryInt(Tables.GROUPROUND.PLUVIAL_FLOOD_INTENSITY)
                        .setRequired()
                        .setInitialValue(groupRound.getPluvialFloodIntensity(), 0)
                        .setLabel("Pluvial flood intensity")
                        .setMin(0)
                        .setMax(parameters.getHighestPluvialScore().intValue()))
                .addEntry(new TableEntryInt(Tables.GROUPROUND.GROUP_ID)
                        .setInitialValue(groupId, 0)
                        .setLabel("Group id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit GroupRound", form);
    }

    /*
     * *********************************************************************************************************
     * ******************************************** GROUPSTATE *************************************************
     * *********************************************************************************************************
     */

    public static void showPlayGroupState(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("PlayGroupGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayGroupGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("PlayGroupGroupRound", 2, data.getColumn(2).getSelectedRecordId(), true, Tables.GROUPROUND,
                Tables.GROUPROUND.ROUND_NUMBER, "round_number", Tables.GROUPROUND.GROUP_ID, true, "GroupRound");
        showGroupStateColumn(data, "PlayGroupState", 3, recordId);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editPlayGroupState(session, data, recordId, editRecord);
        }
    }

    public static void editPlayGroupState(final HttpSession session, final AdminData data, final int groupStateId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        GroupstateRecord groupState = groupStateId == 0 ? dslContext.newRecord(Tables.GROUPSTATE)
                : dslContext.selectFrom(Tables.GROUPSTATE).where(Tables.GROUPSTATE.ID.eq(groupStateId)).fetchOne();
        int groupRoundId = groupStateId == 0 ? data.getColumn(2).getSelectedRecordId() : groupState.getGrouproundId();

        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("play-group", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editPlayGroupState")
                .setSaveMethod("savePlayGroupState")
                .setDeleteMethod("deletePlayGroupState", "Delete", "<br>Be careful with deleting a group state "
                        + "<br>when the group round contains this state")
                .setRecordNr(groupStateId)
                .startForm()
                .addEntry(new TableEntryDateTime(Tables.GROUPSTATE.TIMESTAMP)
                        .setRequired()
                        .setInitialValue(groupState.getTimestamp(), LocalDateTime.now())
                        .setLabel("Timestamp"))
                .addEntry(new TableEntryPickList(Tables.GROUPSTATE.GROUP_STATE)
                        .setRequired()
                        .setPickListEntries(GroupState.class)
                        .setInitialValue(groupState.getGroupState(), "")
                        .setLabel("Group State"))
                .addEntry(new TableEntryText(Tables.GROUPSTATE.CONTENT)
                        .setRequired(false)
                        .setInitialValue(groupState.getContent(), "")
                        .setLabel("Content")
                        .setRows(10))
                .addEntry(new TableEntryInt(Tables.GROUPSTATE.GROUPROUND_ID)
                        .setInitialValue(groupRoundId, 0)
                        .setLabel("GroupRound id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit GroupState", form);
    }

    /*
     * *********************************************************************************************************
     * ****************************************** SUPPORT METHODS **********************************************
     * *********************************************************************************************************
     */

    public static void showGroupStateColumn(final AdminData data, final String columnName, final int columnNr,
            final int recordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<GroupstateRecord> records = dslContext.selectFrom(Tables.GROUPSTATE)
                .where(Tables.GROUPSTATE.GROUPROUND_ID.eq(data.getColumn(columnNr - 1).getSelectedRecordId())).fetch()
                .sortAsc(Tables.GROUPSTATE.TIMESTAMP);

        s.append(AdminTable.startTable());
        for (GroupstateRecord record : records)
        {
            TableRow tableRow = new TableRow(IdProvider.getId(record), recordId, record.getGroupState(), "view" + columnName);
            tableRow.addButton("Edit", "edit" + columnName);
            s.append(tableRow.process());
        }
        s.append(AdminTable.endTable());
        s.append(AdminTable.finalButton("New GroupState", "new" + columnName));

        data.getColumn(columnNr).setSelectedRecordId(recordId);
        data.getColumn(columnNr).setContent(s.toString());
    }

}
