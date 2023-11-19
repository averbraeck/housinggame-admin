package nl.tudelft.simulation.housinggame.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.form.FormEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.FormEntryPickRecordUInt;
import nl.tudelft.simulation.housinggame.admin.form.FormEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryDate;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryDateTime;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickRecordUInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryUInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.GamesessionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.WelfaretypeRecord;

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

        else if (click.equals("generateGroups"))
        {
            generateGroups(request, data);
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
            data.getColumn(2).addContent(AdminTable.finalButton("Generate Groups + Players", "generateGroupsParams"));
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
                .addEntry(new TableEntryString(Tables.GAMESESSION.NAME)
                        .setRequired()
                        .setInitialValue(gameSession.getName(), "")
                        .setLabel("GameSession name")
                        .setMaxChars(32))
                .addEntry(new TableEntryString(Tables.GAMESESSION.PASSWORD)
                        .setRequired()
                        .setInitialValue(gameSession.getPassword(), "")
                        .setLabel("Password")
                        .setMaxChars(32))
                .addEntry(new TableEntryString(Tables.GAMESESSION.LOCATION)
                        .setRequired(false)
                        .setInitialValue(gameSession.getLocation(), "")
                        .setLabel("Location")
                        .setMaxChars(32))
                .addEntry(new TableEntryDate(Tables.GAMESESSION.DATE)
                        .setRequired(false)
                        .setInitialValue(gameSession.getDate(), null)
                        .setLabel("Date"))
                .addEntry(new TableEntryDateTime(Tables.GAMESESSION.START_TIME)
                        .setRequired(false)
                        .setInitialValue(gameSession.getStartTime(), null)
                        .setLabel("Start time login"))
                .addEntry(new TableEntryDateTime(Tables.GAMESESSION.END_TIME)
                        .setRequired(false)
                        .setInitialValue(gameSession.getEndTime(), null)
                        .setLabel("End time login"))
                .addEntry(new TableEntryUInt(Tables.GAMESESSION.GAMEVERSION_ID)
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
        UInteger gameVersionId = UInteger.valueOf(data.getColumn(0).getSelectedRecordId());
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
                .addEntry(new TableEntryString(Tables.GROUP.NAME)
                        .setRequired()
                        .setInitialValue(group.getName(), "")
                        .setLabel("Group name")
                        .setMaxChars(32))
                .addEntry(new TableEntryString(Tables.GROUP.PASSWORD)
                        .setRequired()
                        .setInitialValue(group.getPassword(), "")
                        .setLabel("Password")
                        .setMaxChars(32))
                .addEntry(new TableEntryUInt(Tables.GROUP.GAMESESSION_ID)
                        .setInitialValue(sessionId, UInteger.valueOf(0))
                        .setLabel("GameSession id")
                        .setHidden(true))
                .addEntry(new TableEntryPickRecordUInt(Tables.GROUP.SCENARIO_ID)
                        .setRequired()
                        .setPickTable(data, Tables.SCENARIO.where(Tables.SCENARIO.GAMEVERSION_ID.eq(gameVersionId)),
                                Tables.SCENARIO.ID, Tables.SCENARIO.NAME)
                        .setInitialValue(group.getScenarioId(), UInteger.valueOf(0))
                        .setLabel("Scenario to play"))
                .addEntry(new TableEntryPickRecordUInt(Tables.GROUP.FACILITATOR_ID)
                        .setRequired(false)
                        .setPickTable(data, Tables.USER, Tables.USER.ID, Tables.USER.USERNAME)
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
        GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, groupId);
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
                .addEntry(new TableEntryString(Tables.PLAYER.CODE)
                        .setRequired()
                        .setInitialValue(player.getCode(), "")
                        .setLabel("Player name")
                        .setMaxChars(16))
                .addEntry(new TableEntryUInt(Tables.PLAYER.GROUP_ID)
                        .setInitialValue(groupId, UInteger.valueOf(0))
                        .setLabel("Group id")
                        .setHidden(true))
                .addEntry(new TableEntryPickRecordUInt(Tables.PLAYER.WELFARETYPE_ID)
                        .setRequired()
                        .setPickTable(data, Tables.WELFARETYPE.where(Tables.WELFARETYPE.SCENARIO_ID.eq(group.getScenarioId())),
                                Tables.WELFARETYPE.ID, Tables.WELFARETYPE.NAME)
                        .setInitialValue(player.getWelfaretypeId(), UInteger.valueOf(0))
                        .setLabel("Player welfare type"))
                .addEntry(new TableEntryPickRecordUInt(Tables.PLAYER.USER_ID)
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
        UInteger gameVersionId = UInteger.valueOf(data.getColumn(0).getSelectedRecordId());

        StringBuilder s = new StringBuilder();
        s.append("<p>Note: The group numbers will be generated at the places of the #-sign in the group name, \n"
                + "user name and and password. The user number will be generated at the place of the % sign \n"
                + "in the user name. The welfare types will be randomized and rotated among the players.</p>\n");

        s.append("<div class=\"hg-form\">\n");
        s.append("  <form id=\"editForm\" action=\"/housinggame-admin/admin\" method=\"POST\" >\n");
        s.append("    <input id=\"editClick\" type=\"hidden\" name=\"editClick\" value=\"tobefilled\" />\n");
        s.append("    <input id=\"editRecordNr\" type=\"hidden\" name=\"editRecordNr\" value=\"0\" />\n");
        s.append(buttonRow(data));
        s.append("    <fieldset>\n");
        s.append("     <table width=\"100%\">\n");

        s.append(new FormEntryString("Group names (with #)", "groupname").setMaxChars(32).setRequired()
                .setInitialValue("Table#", "Table#").makeHtml());
        s.append(new FormEntryInt("Group start number", "groupstartnr").setMin(1).setRequired().setInitialValue(1, 1)
                .makeHtml());
        s.append(new FormEntryInt("Number of groups", "nrgroups").setMin(1).setRequired().setInitialValue(6, 1).makeHtml());
        s.append(new FormEntryString("Group password (# allowed)", "password").setMaxChars(32).setRequired().makeHtml());
        s.append(new FormEntryPickRecordUInt("Scenario to play", "scenarioId")
                .setRequired().setPickTable(data, Tables.SCENARIO.where(Tables.SCENARIO.GAMEVERSION_ID.eq(gameVersionId)),
                        Tables.SCENARIO.ID, Tables.SCENARIO.NAME)
                .setInitialValue(UInteger.valueOf(0), UInteger.valueOf(0)).makeHtml());
        s.append(new FormEntryString("Player names (with # and %)", "playername").setMaxChars(16).setRequired()
                .setInitialValue("t#p%", "t#p%").makeHtml());
        s.append(new FormEntryInt("Player start number", "playerstartnr").setMin(1).setRequired().setInitialValue(1, 1)
                .makeHtml());
        s.append(new FormEntryInt("Nr of players / group", "nrplayers").setMin(1).setRequired().setInitialValue(8, 1)
                .makeHtml());

        s.append("     </table>\n");
        s.append("    </fieldset>\n");
        s.append(buttonRow(data));
        s.append("  </form>\n");
        s.append("</div>\n");

        data.getFormColumn().setHtmlContents(s.toString());
        data.getFormColumn().setHeader("Generate Player Batch");
    }

    private static String buttonRow(final AdminData data)
    {
        StringBuilder s = new StringBuilder();
        s.append("    <div class=\"hg-admin-form-buttons\">\n");

        s.append("      <span class=\"hg-admin-form-button\" /><a href=\"#\" onClick=\"submitEditForm('");
        s.append("showGroups");
        s.append("', ");
        s.append(data.getColumn(0).getSelectedRecordId());
        s.append("); return false;\">Cancel</a></span>\n");

        s.append("      <span class=\"hg-admin-form-button\" /><a href=\"#\" onClick=\"submitEditForm('");
        s.append("generateGroups");
        s.append("', ");
        s.append(data.getColumn(1).getSelectedRecordId());
        s.append("); return false;\">");
        s.append("Generate");
        s.append("</a></span>\n");

        s.append("    </div>\n");
        return s.toString();
    }

    public static void generateGroups(final HttpServletRequest request, final AdminData data)
    {
        int gameSessionId = data.getColumn(1).getSelectedRecordId();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        String groupName = request.getParameter("groupname");
        String sGroupStartNr = request.getParameter("groupstartnr");
        String sNrGroups = request.getParameter("nrgroups");
        String password = request.getParameter("password");
        String playerName = request.getParameter("playername");
        String sPlayerStartNr = request.getParameter("playerstartnr");
        String sNrPlayers = request.getParameter("nrplayers");
        String sScenarioId = request.getParameter("scenarioId");

        // System.out.println("groupName = " + groupName);
        // System.out.println("sGroupStartNr = " + sGroupStartNr);
        // System.out.println("sNrGroups = " + sNrGroups);
        // System.out.println("password = " + password);
        // System.out.println("playerName = " + playerName);
        // System.out.println("sPlayerStartNr = " + sPlayerStartNr);
        // System.out.println("sNrPlayers = " + sNrPlayers);
        // System.out.println("sScenarioId = " + sScenarioId);

        // check validity
        if (!groupName.contains("#"))
        {
            ModalWindowUtils.popup(data, "Error in group name", "<p>No # sign in group name</p>",
                    "clickRecordId('showGameSession'," + gameSessionId + ")");
            return;
        }
        if (playerName.indexOf('#') != playerName.lastIndexOf('#'))
        {
            ModalWindowUtils.popup(data, "Error in group name", "<p>Multiple # sign in group name</p>",
                    "clickRecordId('showGameSession'," + gameSessionId + ")");
            return;
        }
        if (!playerName.contains("%"))
        {
            ModalWindowUtils.popup(data, "Error in player name", "<p>No % sign in player name</p>",
                    "clickRecordId('showGameSession'," + gameSessionId + ")");
            return;
        }
        if (playerName.indexOf('%') != playerName.lastIndexOf('%'))
        {
            ModalWindowUtils.popup(data, "Error in player name", "<p>Multiple % sign in player name</p>",
                    "clickRecordId('showGameSession'," + gameSessionId + ")");
            return;
        }
        if (playerName.indexOf('#') != playerName.lastIndexOf('#'))
        {
            ModalWindowUtils.popup(data, "Error in player name", "<p>Multiple # sign in player name</p>",
                    "clickRecordId('showGameSession'," + gameSessionId + ")");
            return;
        }
        if (password.indexOf('#') != password.lastIndexOf('#'))
        {
            ModalWindowUtils.popup(data, "Error in password", "<p>Multiple # sign in password</p>",
                    "clickRecordId('showGameSession'," + gameSessionId + ")");
            return;
        }
        try
        {
            Integer.parseInt(sGroupStartNr);
            Integer.parseInt(sNrGroups);
            Integer.parseInt(sPlayerStartNr);
            Integer.parseInt(sNrPlayers);
            Integer.parseInt(sScenarioId);
        }
        catch (NumberFormatException nfe)
        {
            ModalWindowUtils.popup(data, "Error in numeric values", "<p>startNr / nr wrong</p>",
                    "clickRecordId('showGameSession'," + gameSessionId + ")");
            return;
        }

        int groupStartNr = Integer.parseInt(sGroupStartNr);
        int nrGroups = Integer.parseInt(sNrGroups);
        int playerStartNr = Integer.parseInt(sPlayerStartNr);
        int nrPlayers = Integer.parseInt(sNrPlayers);
        UInteger scenarioId = UInteger.valueOf(sScenarioId);

        Random random = new Random();
        SortedMap<Double, UInteger> randomRecords = new TreeMap<>();
        List<WelfaretypeRecord> tableRecords =
                dslContext.selectFrom(Tables.WELFARETYPE).where(Tables.WELFARETYPE.SCENARIO_ID.eq(scenarioId)).fetch();
        for (WelfaretypeRecord record : tableRecords)
            randomRecords.put(random.nextDouble(), record.getId());
        List<UInteger> welfareTypeRecords = new ArrayList<>(randomRecords.values());

        // make the groups
        for (int i = groupStartNr; i < groupStartNr + nrGroups; i++)
        {
            GroupRecord group = dslContext.newRecord(Tables.GROUP);
            String gnr = "" + i;
            String gname = groupName.replaceFirst("#", gnr);
            String pwd;
            if (password.length() == 0)
            {
                pwd = "" + new Random().nextInt(1, 1000);
            }
            else
            {
                pwd = password.replaceFirst("#", gnr);
            }

            group.setName(gname);
            group.setPassword(pwd);
            group.setGamesessionId(UInteger.valueOf(gameSessionId));
            group.setScenarioId(scenarioId);
            UInteger groupId;
            try
            {
                group.store();
                groupId = group.getId();
            }
            catch (DataAccessException exception)
            {
                ModalWindowUtils.popup(data, "Error storing group record", "<p>" + exception.getMessage() + "</p>",
                        "clickRecordId('showGameSession'," + gameSessionId + ")");
                return;
            }

            // System.out.println("ScenarioId group " + gname + " = " + scenarioId);

            // make the players in the group
            for (int j = playerStartNr; j < playerStartNr + nrPlayers; j++)
            {
                PlayerRecord player = dslContext.newRecord(Tables.PLAYER);
                String pnr = "" + j;
                String pname = playerName.replaceFirst("#", gnr).replaceFirst("\\%", pnr);
                UInteger welfareTypeId = welfareTypeRecords.get(j % welfareTypeRecords.size());
                // System.out.println("welfareTypeId player " + pname + " = " + welfareTypeId);

                player.setCode(pname);
                player.setGroupId(groupId);
                player.setWelfaretypeId(welfareTypeId);
                try
                {
                    player.store();
                }
                catch (DataAccessException exception)
                {
                    ModalWindowUtils.popup(data, "Error storing player record", "<p>" + exception.getMessage() + "</p>",
                            "clickRecordId('showGameSession'," + gameSessionId + ")");
                    return;
                }
            }
        }

        showGameSession(request.getSession(), data, gameSessionId, true, true);
    }
}
