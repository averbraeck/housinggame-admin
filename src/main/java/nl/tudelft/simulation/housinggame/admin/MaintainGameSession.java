package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.admin.form.table.FormEntryDate;
import nl.tudelft.simulation.housinggame.admin.form.table.FormEntryPickRecordUInt;
import nl.tudelft.simulation.housinggame.admin.form.table.FormEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.FormEntryUInt;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.GamesessionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerRecord;

public class MaintainGameSession
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("gamesession"))
        {
            data.clearColumns("15%", "GameVersion", "15%", "GameSession", "15%", "Group", "15%", "Player");
            data.clearFormColumn("40%", "Edit Properties");
            showGameVersion(session, data, 0);
        }

        else if (click.contains("GameSessionGameVersion"))
        {
            showGameVersion(session, data, recordId);
        }

        else if (click.contains("GameSession"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.GAMESESSION, "gamesession");
            else if (click.startsWith("delete"))
            {
                GamesessionRecord gameSession = SqlUtils.readRecordFromId(data, Tables.GAMESESSION, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(gameSession, "gamesession");
                else
                    data.deleteRecord(gameSession, "GameSession", String.valueOf(gameSession.getName()), "deleteGameSessionOk",
                            "gamesession");
                recordId = 0;
            }
            if (!data.isError())
            {
                showGameSession(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editGameSession(session, data, 0, true);
            }
        }

        else if (click.equals("generateGroupsParams"))
        {
            generateGroupsParams(session, data, recordId);
        }

        else if (click.contains("Group"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.GROUP, "gamesession");
            else if (click.startsWith("delete"))
            {
                GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(group, "gamesession");
                else
                    data.deleteRecord(group, "Group", group.getName(), "deleteGroupOk", "gamesession");
                recordId = 0;
            }
            if (!data.isError())
            {
                showGroup(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editGroup(session, data, 0, true);
            }
        }

        else if (click.contains("Player"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.PLAYER, "gamesession");
            else if (click.startsWith("delete"))
            {
                PlayerRecord player = SqlUtils.readRecordFromId(data, Tables.PLAYER, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(player, "gamesession");
                else
                    data.deleteRecord(player, "Player", player.getCode(), "deletePlayerOk", "gamesession");
                recordId = 0;
            }
            if (!data.isError())
            {
                showPlayer(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editPlayer(session, data, 0, true);
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
        data.showColumn("GameSessionGameVersion", 0, recordId, false, Tables.GAMEVERSION, Tables.GAMEVERSION.NAME, "name",
                false);
        data.resetColumn(1);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("GameSession", 1, 0, true, Tables.GAMESESSION, Tables.GAMESESSION.NAME, "name",
                    Tables.GAMESESSION.GAMEVERSION_ID, true);
        }
    }

    /*
     * *********************************************************************************************************
     * ***************************************** GAMESESSION **********************************************
     * *********************************************************************************************************
     */

    public static void showGameSession(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("GameSessionGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("GameSession", 1, recordId, true, Tables.GAMESESSION, Tables.GAMESESSION.NAME, "name",
                Tables.GAMESESSION.GAMEVERSION_ID, true);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("Group", 2, 0, true, Tables.GROUP, Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID,
                    true);
            editGameSession(session, data, recordId, editRecord);
        }
        if (editButton && recordId != 0)
        {
            data.getColumn(2).addContent(AdminTable.finalButton("Generate Greups + Players", "generateGroupsParams"));
        }
    }

    public static void editGameSession(final HttpSession session, final AdminData data, final int gameSessionId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        GamesessionRecord gameSession = gameSessionId == 0 ? dslContext.newRecord(Tables.GAMESESSION) : dslContext
                .selectFrom(Tables.GAMESESSION).where(Tables.GAMESESSION.ID.eq(UInteger.valueOf(gameSessionId))).fetchOne();
        UInteger gameVersionId =
                gameSessionId == 0 ? UInteger.valueOf(data.getColumn(0).getSelectedRecordId()) : gameSession.getGameversionId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("gamesession", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editGameSession")
                .setSaveMethod("saveGameSession")
                .setDeleteMethod("deleteGameSession", "Delete", "<br>Note: GameSession can only be deleted when it "
                        + "<br>has not been used in a game play")
                .setRecordNr(gameSessionId)
                .startForm()
                .addEntry(new FormEntryString(Tables.GAMESESSION.NAME)
                        .setRequired()
                        .setInitialValue(gameSession.getName(), "")
                        .setLabel("GameSession name")
                        .setMaxChars(16))
                .addEntry(new FormEntryString(Tables.GAMESESSION.PASSWORD)
                        .setRequired()
                        .setInitialValue(gameSession.getPassword(), "")
                        .setLabel("Password")
                        .setMaxChars(32))
                .addEntry(new FormEntryString(Tables.GAMESESSION.LOCATION)
                        .setRequired(false)
                        .setInitialValue(gameSession.getLocation(), "")
                        .setLabel("Location")
                        .setMaxChars(32))
                .addEntry(new FormEntryDate(Tables.GAMESESSION.DATE)
                        .setRequired(false)
                        .setInitialValue(gameSession.getDate(), null)
                        .setLabel("Date"))
                .addEntry(new FormEntryUInt(Tables.GAMESESSION.GAMEVERSION_ID)
                        .setInitialValue(gameVersionId, UInteger.valueOf(0))
                        .setLabel("Game version id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit GameSession", form);
    }

    /*
     * *********************************************************************************************************
     * *********************************************** GROUP ***************************************************
     * *********************************************************************************************************
     */

    public static void showGroup(final HttpSession session, final AdminData data, final int recordId, final boolean editButton,
            final boolean editRecord)
    {
        data.showColumn("GameSessionGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("GameSession", 1, data.getColumn(1).getSelectedRecordId(), true, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", Tables.GAMESESSION.GAMEVERSION_ID, true);
        data.showDependentColumn("Group", 2, recordId, true, Tables.GROUP, Tables.GROUP.NAME, "name",
                Tables.GROUP.GAMESESSION_ID, true);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("Player", 3, 0, true, Tables.PLAYER, Tables.PLAYER.CODE, "code", Tables.PLAYER.GROUP_ID,
                    true);
            editGroup(session, data, recordId, editRecord);
        }
    }

    public static void editGroup(final HttpSession session, final AdminData data, final int groupId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        GroupRecord group = groupId == 0 ? dslContext.newRecord(Tables.GROUP)
                : dslContext.selectFrom(Tables.GROUP).where(Tables.GROUP.ID.eq(UInteger.valueOf(groupId))).fetchOne();
        UInteger sessionId =
                groupId == 0 ? UInteger.valueOf(data.getColumn(1).getSelectedRecordId()) : group.getGamesessionId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("gamesession", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editGroup")
                .setSaveMethod("saveGroup")
                .setDeleteMethod("deleteGroup", "Delete", "<br>Note: Group can only be deleted when it "
                        + "<br>has not been used in a session")
                .setRecordNr(groupId)
                .startForm()
                .addEntry(new FormEntryString(Tables.GROUP.NAME)
                        .setRequired()
                        .setInitialValue(group.getName(), "")
                        .setLabel("Group name")
                        .setMaxChars(16))
                .addEntry(new FormEntryString(Tables.GROUP.PASSWORD)
                        .setRequired()
                        .setInitialValue(group.getPassword(), "")
                        .setLabel("Password")
                        .setMaxChars(32))
                .addEntry(new FormEntryUInt(Tables.GROUP.GAMESESSION_ID)
                        .setInitialValue(sessionId, UInteger.valueOf(0))
                        .setLabel("GameSession id")
                        .setHidden(true))
                .addEntry(new FormEntryPickRecordUInt(Tables.GROUP.SCENARIO_ID)
                        .setRequired()
                        .setPickTable(data, Tables.SCENARIO, Tables.SCENARIO.ID, Tables.SCENARIO.NAME)
                        .setInitialValue(group.getScenarioId(), UInteger.valueOf(0))
                        .setLabel("Scenario to play"))
                .addEntry(new FormEntryPickRecordUInt(Tables.GROUP.CURRENT_ROUND_ID)
                        .setRequired()
                        .setPickTable(data, Tables.ROUND, Tables.ROUND.ID, Tables.ROUND.ROUND_NUMBER)
                        .setInitialValue(group.getCurrentRoundId(), UInteger.valueOf(0))
                        .setLabel("Current (initial) round"))
                .addEntry(new FormEntryPickRecordUInt(Tables.GROUP.FACILITATOR_ID)
                        .setRequired(false)
                        .setPickTable(data, Tables.FACILITATOR, Tables.FACILITATOR.ID, Tables.FACILITATOR.NAME)
                        .setInitialValue(group.getFacilitatorId(), UInteger.valueOf(0))
                        .setLabel("Group facilitator"))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Group", form);
    }

    /*
     * *********************************************************************************************************
     * *********************************************** PLAYER **************************************************
     * *********************************************************************************************************
     */

    public static void showPlayer(final HttpSession session, final AdminData data, final int recordId, final boolean editButton,
            final boolean editRecord)
    {
        data.showColumn("GameSessionGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("GameSession", 1, data.getColumn(1).getSelectedRecordId(), true, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", Tables.GAMESESSION.GAMEVERSION_ID, true);
        data.showDependentColumn("Group", 2, data.getColumn(2).getSelectedRecordId(), true, Tables.GROUP, Tables.GROUP.NAME,
                "name", Tables.GROUP.GAMESESSION_ID, true);
        data.showDependentColumn("Player", 3, recordId, true, Tables.PLAYER, Tables.PLAYER.CODE, "code", Tables.PLAYER.GROUP_ID,
                true);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editPlayer(session, data, recordId, editRecord);
        }
    }

    public static void editPlayer(final HttpSession session, final AdminData data, final int playerId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        PlayerRecord player = playerId == 0 ? dslContext.newRecord(Tables.PLAYER)
                : dslContext.selectFrom(Tables.PLAYER).where(Tables.PLAYER.ID.eq(UInteger.valueOf(playerId))).fetchOne();
        UInteger groupId = playerId == 0 ? UInteger.valueOf(data.getColumn(2).getSelectedRecordId()) : player.getGroupId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("gamesession", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editPlayer")
                .setSaveMethod("savePlayer")
                .setDeleteMethod("deletePlayer", "Delete", "<br>Note: Player can only be deleted when it "
                        + "<br>has not been used in a game play")
                .setRecordNr(playerId)
                .startForm()
                .addEntry(new FormEntryString(Tables.PLAYER.CODE)
                        .setRequired()
                        .setInitialValue(player.getCode(), "")
                        .setLabel("Player name")
                        .setMaxChars(16))
                .addEntry(new FormEntryUInt(Tables.PLAYER.GROUP_ID)
                        .setInitialValue(groupId, UInteger.valueOf(0))
                        .setLabel("Group id")
                        .setHidden(true))
                .addEntry(new FormEntryPickRecordUInt(Tables.PLAYER.WELFARETYPE_ID)
                        .setRequired()
                        .setPickTable(data, Tables.WELFARETYPE, Tables.WELFARETYPE.ID, Tables.WELFARETYPE.NAME)
                        .setInitialValue(player.getWelfaretypeId(), UInteger.valueOf(0))
                        .setLabel("Player welfare type"))
                .addEntry(new FormEntryPickRecordUInt(Tables.PLAYER.USER_ID)
                        .setRequired(false)
                        .setPickTable(data, Tables.USER, Tables.USER.ID, Tables.USER.USERNAME)
                        .setInitialValue(player.getUserId(), UInteger.valueOf(0))
                        .setLabel("User (can be blank)"))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Player", form);
    }

    /*
     * *********************************************************************************************************
     * ****************************************** GENERATE GROUPS **********************************************
     * *********************************************************************************************************
     */

    public static void generateGroupsParams(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("GameSessionGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("GameSession", 1, data.getColumn(1).getSelectedRecordId(), true, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", Tables.GAMESESSION.GAMEVERSION_ID, true);
        data.showDependentColumn("Group", 2, recordId, true, Tables.GROUP, Tables.GROUP.NAME, "name",
                Tables.GROUP.GAMESESSION_ID, true);
        data.resetColumn(3);
        data.resetFormColumn();

        StringBuilder s = new StringBuilder();
        s.append("<p>Note: The group numbers will be generated at the places of the #-sign in the group name, \n"
                + "user name and and password. The user number will be generated at the place of the % sign \n"
                + "in the user name. The code will be randomly generated.</p>\n");

        s.append("<div class=\"tg-form\">\n");
        s.append("  <form id=\"editForm\" action=\"/housinggame-admin/admin\" method=\"POST\" >\n");
        s.append("    <input id=\"editClick\" type=\"hidden\" name=\"editClick\" value=\"tobefilled\" />\n");
        s.append("    <input id=\"editRecordNr\" type=\"hidden\" name=\"editRecordNr\" value=\"0\" />\n");
        s.append(buttonRow(data));
        s.append("    <fieldset>\n");
        s.append("     <table width=\"100%\">\n");

        s.append(makeStringField("Group names (with #)", true, "groupname", ""));
        s.append(makeIntField("Group start number", true, "groupstartnr", 1));
        s.append(makeIntField("Number of groups", true, "nrgroups", 6));
        s.append(makeStringField("Group password", false, "password", ""));
        s.append(makePickField("Group scenario", false, "scenario", ""));
        s.append(makeStringField("Player names (with # and %)", true, "playername", ""));
        s.append(makeIntField("Player start number", true, "playerstartnr", 1));
        s.append(makeIntField("Number of players", true, "nrplayers", 8));

        s.append("     </table>\n");
        s.append("    </fieldset>\n");
        s.append(buttonRow(data));
        s.append("  </form>\n");
        s.append("</div>\n");

        data.getFormColumn().setHtmlContents(s.toString());
        data.getFormColumn().setHeader("Generate User Batch");
    }

    private static String makeStringField(final String label, final boolean required, final String name,
            final String initialValue)
    {
        StringBuilder s = new StringBuilder();
        s.append("    <tr>\n");
        s.append("      <td width=\"40%\">");
        s.append(label);
        s.append("      </td>");
        s.append("      <td width=\"60%\">");
        s.append("<input type=\"text\" style=\"width:97%;\" ");
        if (required)
            s.append("required name=\"");
        else
            s.append("name=\"");
        s.append(name);
        s.append("\" value=\"");
        s.append(initialValue);
        s.append("\" />");
        s.append("</td>\n");
        s.append("    </tr>\n");
        return s.toString();
    }

    private static String makeIntField(final String label, final boolean required, final String name, final int initialValue)
    {
        StringBuilder s = new StringBuilder();
        s.append("    <tr>\n");
        s.append("      <td width=\"40%\">");
        s.append(label);
        s.append("      </td>");
        s.append("      <td width=\"60%\">");
        s.append("<input type=\"number\" style=\"width:97%;\" ");
        if (required)
            s.append("required name=\"");
        else
            s.append("name=\"");
        s.append(name);
        s.append("\" value=\"");
        s.append(initialValue);
        s.append("\" />");
        s.append("</td>\n");
        s.append("    </tr>\n");
        return s.toString();
    }

    private static String buttonRow(final AdminData data)
    {
        StringBuilder s = new StringBuilder();
        s.append("    <div class=\"tg-admin-form-buttons\">\n");

        s.append("      <span class=\"tg-admin-form-button\" /><a href=\"#\" onClick=\"submitEditForm('");
        s.append("showUsers");
        s.append("', ");
        s.append(data.getColumn(0).getSelectedRecordId());
        s.append("); return false;\">Cancel</a></span>\n");

        s.append("      <span class=\"tg-admin-form-button\" /><a href=\"#\" onClick=\"submitEditForm('");
        s.append("generateUsers");
        s.append("', ");
        s.append(data.getColumn(0).getSelectedRecordId());
        s.append("); return false;\">");
        s.append("Generate");
        s.append("</a></span>\n");

        s.append("    </div>\n");
        return s.toString();
    }

}
