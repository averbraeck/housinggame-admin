package nl.tudelft.simulation.housinggame.admin;

import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import nl.tudelft.simulation.housinggame.common.SqlUtils;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GrouproundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HousegroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerroundRecord;

public class MaintainResults
{

    public static void handleMenu(final HttpServletRequest request, final String click, final int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("results"))
        {
            data.clearColumns("20%", "GameSession", "15%", "Group", "15%", "Player", "50%", "Results");
            data.setFormColumn(null);
            showGameSession(session, data, 0);
        }

        else if (click.contains("GameSession"))
        {
            showGameSession(session, data, recordId);
        }

        else if (click.endsWith("Group"))
        {
            showGroup(session, data, recordId);
        }

        else if (click.endsWith("Player"))
        {
            showPlayer(session, data, recordId);
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
        data.showColumn("ResultsGameSession", 0, recordId, false, Tables.GAMESESSION, Tables.GAMESESSION.NAME, "name", false);
        data.resetColumn(1);
        data.resetColumn(2);
        data.resetColumn(3);
        if (recordId != 0)
        {
            data.showDependentColumn("ResultsGroup", 1, 0, false, Tables.GROUP, Tables.GROUP.NAME, "name",
                    Tables.GROUP.GAMESESSION_ID, false);
            showSessionScores(data, recordId);
            data.getColumn(1).addContent(AdminTable.finalButton("CSV export all groups", "resultExportGroupsCSV"));
            data.getColumn(1).addContent(AdminTable.finalButton("TSV export all groups", "resultExportGroupsTSV"));
        }
    }

    /*
     * *********************************************************************************************************
     * ********************************************** GROUP ****************************************************
     * *********************************************************************************************************
     */

    public static void showGroup(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("ResultsGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("ResultsGroup", 1, recordId, false, Tables.GROUP, Tables.GROUP.NAME, "name",
                Tables.GROUP.GAMESESSION_ID, false);
        data.resetColumn(2);
        data.resetColumn(3);
        if (recordId != 0)
        {
            data.showDependentColumn("ResultsPlayer", 2, 0, false, Tables.PLAYER, Tables.PLAYER.CODE, "code",
                    Tables.PLAYER.GROUP_ID, false);
            showGroupScores(data, recordId);
            data.getColumn(2).addContent(AdminTable.finalButton("CSV export all players", "resultExportPlayersCSV"));
            data.getColumn(2).addContent(AdminTable.finalButton("TSV export all players", "resultExportPlayersTSV"));
        }
        data.getColumn(1).addContent(AdminTable.finalButton("CSV export all groups", "resultExportGroupsCSV"));
        data.getColumn(1).addContent(AdminTable.finalButton("TSV export all groups", "resultExportGroupsTSV"));
    }

    /*
     * *********************************************************************************************************
     * ********************************************** PLAYER ***************************************************
     * *********************************************************************************************************
     */

    public static void showPlayer(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("ResultsGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("ResultsGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        showPlayerColumn(data, "ResultsPlayer", 2, recordId);
        showPlayerScores(data, recordId);
        data.getColumn(1).addContent(AdminTable.finalButton("CSV export all groups", "resultExportGroupsCSV"));
        data.getColumn(1).addContent(AdminTable.finalButton("TSV export all groups", "resultExportGroupsTSV"));
        data.getColumn(2).addContent(AdminTable.finalButton("CSV export all players", "resultExportPlayersCSV"));
        data.getColumn(2).addContent(AdminTable.finalButton("TSV export all players", "resultExportPlayersTSV"));
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
            tableRow.addButton("Edit", "edit" + columnName);
            s.append(tableRow.process());
        }
        s.append(AdminTable.endTable());

        s.append(AdminTable.finalButton("New PlayerRound", "new" + columnName));

        data.getColumn(columnNr).setSelectedRecordId(recordId);
        data.getColumn(columnNr).setContent(s.toString());
    }

    public static void showSessionScores(final AdminData data, final int sessionRecordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);

        List<GroupRecord> groupList = dslContext.selectFrom(Tables.GROUP).where(Tables.GROUP.GAMESESSION_ID.eq(sessionRecordId))
                .fetch().sortAsc(Tables.GROUP.NAME);
        for (GroupRecord group : groupList)
        {
            s.append("        <h3>Group: " + group.getName() + "</h3>\n");
            s.append("\n      <div>\n");
            s.append(AdminTable.startTable());
            s.append("        <table width=\"100%\">\n");
            s.append("          <thead>\n");
            s.append("            <tr>\n");
            s.append("              <td width=\"20%\" align=\"left\">Player</td>\n");
            s.append("              <td width=\"20%\" align=\"center\">Round</td>\n");
            s.append("              <td width=\"20%\" align=\"left\">House</td>\n");
            s.append("              <td width=\"20%\" align=\"center\">Balance</td>\n");
            s.append("              <td width=\"20%\" align=\"center\">Satisf</td>\n");
            s.append("            </tr>\n");
            s.append("          </thead>\n");
            s.append("          <tbody>\n");

            List<PlayerRecord> playerList = dslContext.selectFrom(Tables.PLAYER).where(Tables.PLAYER.GROUP_ID.eq(group.getId()))
                    .fetch().sortAsc(Tables.PLAYER.CODE);
            for (PlayerRecord player : playerList)
            {
                List<PlayerroundRecord> playerRoundList = dslContext.selectFrom(Tables.PLAYERROUND)
                        .where(Tables.PLAYERROUND.PLAYER_ID.eq(player.getId())).fetch().sortAsc(Tables.PLAYERROUND.CREATE_TIME);
                if (!playerRoundList.isEmpty())
                {
                    var playerRound = playerRoundList.get(playerRoundList.size() - 1);
                    GrouproundRecord groupRound =
                            SqlUtils.readRecordFromId(data, Tables.GROUPROUND, playerRound.getGrouproundId());
                    s.append("            <tr>\n");
                    s.append("              <td width=\"20%\" align=\"left\">" + player.getCode() + "</td>\n");
                    s.append("              <td width=\"20%\" align=\"center\">" + groupRound.getRoundNumber() + "</td>\n");
                    int houseGroupId = playerRound.getFinalHousegroupId() != null ? playerRound.getFinalHousegroupId()
                            : playerRound.getStartHousegroupId() != null ? playerRound.getStartHousegroupId() : -1;
                    HousegroupRecord houseGroup =
                            houseGroupId >= 0 ? SqlUtils.readRecordFromId(data, Tables.HOUSEGROUP, houseGroupId) : null;
                    s.append("              <td width=\"20%\" align=\"left\">"
                            + (houseGroup == null ? "-" : houseGroup.getCode()) + "</td>\n");
                    s.append(
                            "              <td width=\"20%\" align=\"center\">" + playerRound.getSpendableIncome() + "</td>\n");
                    s.append("              <td width=\"20%\" align=\"center\">" + playerRound.getSatisfactionTotal()
                            + "</td>\n");
                    s.append("            </tr>\n");
                }
            }
            s.append("          </tbody>\n");
            s.append("        </table>\n");
            s.append("      </div>");
            s.append(AdminTable.endTable());
            s.append("<br>\n");
        }

        data.getColumn(3).setSelectedRecordId(sessionRecordId);
        data.getColumn(3).setContent(s.toString());
    }

    public static void showGroupScores(final AdminData data, final int groupRecordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);

        GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, groupRecordId);
        List<PlayerRecord> playerList = dslContext.selectFrom(Tables.PLAYER).where(Tables.PLAYER.GROUP_ID.eq(group.getId()))
                .fetch().sortAsc(Tables.PLAYER.CODE);
        for (PlayerRecord player : playerList)
        {
            s.append("        <h3>Player: " + player.getCode() + "</h3>\n");
            s.append("\n      <div>\n");
            s.append(AdminTable.startTable());
            s.append("        <table width=\"100%\">\n");
            s.append("          <thead>\n");
            s.append("            <tr>\n");
            s.append("              <td width=\"25%\" align=\"center\">Round</td>\n");
            s.append("              <td width=\"25%\" align=\"left\">House</td>\n");
            s.append("              <td width=\"25%\" align=\"center\">Balance</td>\n");
            s.append("              <td width=\"25%\" align=\"center\">Satisf</td>\n");
            s.append("            </tr>\n");
            s.append("          </thead>\n");
            s.append("          <tbody>\n");

            List<PlayerroundRecord> playerRoundList = dslContext.selectFrom(Tables.PLAYERROUND)
                    .where(Tables.PLAYERROUND.PLAYER_ID.eq(player.getId())).fetch().sortAsc(Tables.PLAYERROUND.CREATE_TIME);
            for (var playerRound : playerRoundList)
            {
                GrouproundRecord groupRound = SqlUtils.readRecordFromId(data, Tables.GROUPROUND, playerRound.getGrouproundId());
                s.append("            <tr>\n");
                s.append("              <td width=\"25%\" align=\"center\">" + groupRound.getRoundNumber() + "</td>\n");
                int houseGroupId = playerRound.getFinalHousegroupId() != null ? playerRound.getFinalHousegroupId()
                        : playerRound.getStartHousegroupId() != null ? playerRound.getStartHousegroupId() : -1;
                HousegroupRecord houseGroup =
                        houseGroupId >= 0 ? SqlUtils.readRecordFromId(data, Tables.HOUSEGROUP, houseGroupId) : null;
                s.append("              <td width=\"25%\" align=\"left\">" + (houseGroup == null ? "-" : houseGroup.getCode())
                        + "</td>\n");
                s.append("              <td width=\"25%\" align=\"center\">" + playerRound.getSpendableIncome() + "</td>\n");
                s.append("              <td width=\"25%\" align=\"center\">" + playerRound.getSatisfactionTotal() + "</td>\n");
                s.append("            </tr>\n");
            }
            s.append("          </tbody>\n");
            s.append("        </table>\n");
            s.append("      </div>");
            s.append(AdminTable.endTable());
            s.append("<br>\n");
        }
        data.getColumn(3).setSelectedRecordId(groupRecordId);
        data.getColumn(3).setContent(s.toString());
    }

    public static void showPlayerScores(final AdminData data, final int playerRecordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);

        PlayerRecord player = SqlUtils.readRecordFromId(data, Tables.PLAYER, playerRecordId);
        GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, player.getGroupId());

        s.append("        <h2>Group: " + group.getName() + "</h2>\n");
        s.append("        <h2>Player: " + player.getCode() + "</h2>\n");

        int incPrevRound = 0;
        int satPrevRound = 0;

        List<PlayerroundRecord> playerRoundList = dslContext.selectFrom(Tables.PLAYERROUND)
                .where(Tables.PLAYERROUND.PLAYER_ID.eq(player.getId())).fetch().sortAsc(Tables.PLAYERROUND.CREATE_TIME);
        for (var pr : playerRoundList)
        {
            GrouproundRecord groupRound = SqlUtils.readRecordFromId(data, Tables.GROUPROUND, pr.getGrouproundId());
            int round = groupRound.getRoundNumber();
            if (round == 0)
            {
                incPrevRound = pr.getSpendableIncome();
                satPrevRound = pr.getSatisfactionTotal();
            }
            else
            {
                s.append("        <h3>Round: " + round + "</h3>\n");
                s.append("\n      <div>\n");
                s.append(AdminTable.startTable());
                s.append("        <table width=\"100%\">\n");
                s.append("          <thead>\n");
                s.append("            <tr>\n");
                s.append("              <td width=\"55%\" align=\"left\">Activity</td>\n");
                s.append("              <td width=\"15%\" align=\"left\">House</td>\n");
                s.append("              <td width=\"15%\" align=\"center\">Balance</td>\n");
                s.append("              <td width=\"15%\" align=\"center\">Satisf</td>\n");
                s.append("            </tr>\n");
                s.append("          </thead>\n");
                s.append("          <tbody>\n");

                HousegroupRecord startHouseGroup = pr.getStartHousegroupId() != null
                        ? SqlUtils.readRecordFromId(data, Tables.HOUSEGROUP, pr.getStartHousegroupId()) : null;
                HousegroupRecord finalHouseGroup = pr.getFinalHousegroupId() != null
                        ? SqlUtils.readRecordFromId(data, Tables.HOUSEGROUP, pr.getFinalHousegroupId()) : null;
                boolean moved = !Objects.equals(pr.getStartHousegroupId(), pr.getFinalHousegroupId());
                reportPlayerLine(s, "Start of round " + round, startHouseGroup, incPrevRound, satPrevRound, false);
                reportPlayerLine(s, "Round income " + round, null, pr.getRoundIncome(), null, false);
                reportPlayerLine(s, "Living costs", null, -pr.getLivingCosts(), null, false);
                if (pr.getPaidDebt() != 0)
                    reportPlayerLine(s, "Debt", null, null, -pr.getSatisfactionDebtPenalty(), false);
                if (moved && startHouseGroup != null)
                {
                    reportPlayerLine(s, "Profit sold house", startHouseGroup, pr.getProfitSoldHouse(), null, false);
                    reportPlayerLine(s, "Penalty for moving", finalHouseGroup, null, -pr.getSatisfactionMovePenalty(), false);
                }
                if (moved)
                {
                    reportPlayerLine(s, "Spent savings for buying house", finalHouseGroup, -pr.getSpentSavingsForBuyingHouse(),
                            null, false);
                }
                if (pr.getMortgagePayment() != 0)
                    reportPlayerLine(s, "Mortgage payment", finalHouseGroup, -pr.getMortgagePayment(), null, false);
                reportPlayerLine(s, "House rating satisfaction", finalHouseGroup, null, pr.getSatisfactionHouseRatingDelta(),
                        false);
                reportPlayerLine(s, "Taxes", finalHouseGroup, -pr.getCostTaxes(), null, false);
                reportPlayerLine(s, "House measures bought", finalHouseGroup, -pr.getCostHouseMeasuresBought(),
                        pr.getSatisfactionHouseMeasures(), true);
                reportPlayerLine(s, "Personal measures bought", finalHouseGroup, -pr.getCostPersonalMeasuresBought(),
                        pr.getSatisfactionPersonalMeasures(), true);
                reportPlayerLine(s, "Pluvial damage", finalHouseGroup, -pr.getCostPluvialDamage(),
                        -pr.getSatisfactionPluvialPenalty(), true);
                reportPlayerLine(s, "Fluvial damage", finalHouseGroup, -pr.getCostFluvialDamage(),
                        -pr.getSatisfactionFluvialPenalty(), true);

                int incCheck = incPrevRound + pr.getRoundIncome() - pr.getLivingCosts() - pr.getMortgagePayment()
                        + pr.getProfitSoldHouse() - pr.getSpentSavingsForBuyingHouse() - pr.getCostTaxes()
                        - pr.getCostHouseMeasuresBought() - pr.getCostPersonalMeasuresBought() - pr.getCostPluvialDamage()
                        - pr.getCostFluvialDamage();
                int satCheck = satPrevRound - pr.getSatisfactionDebtPenalty() + pr.getSatisfactionHouseRatingDelta()
                        - pr.getSatisfactionMovePenalty() + pr.getSatisfactionHouseMeasures()
                        + pr.getSatisfactionPersonalMeasures() - pr.getSatisfactionPluvialPenalty()
                        - pr.getSatisfactionFluvialPenalty();

                reportPlayerLine(s, "End of round " + round, finalHouseGroup, pr.getSpendableIncome(),
                        pr.getSatisfactionTotal(), false);
                if (pr.getSpendableIncome() != incCheck || pr.getSatisfactionTotal() != satCheck)
                    reportPlayerLine(s, "CHECK end of round " + round, finalHouseGroup, incCheck, satCheck, false, "red");

                s.append("          </tbody>\n");
                s.append("        </table>\n");
                s.append("      </div>");
                s.append(AdminTable.endTable());
                s.append("<br>\n");

                incPrevRound = pr.getSpendableIncome();
                satPrevRound = pr.getSatisfactionTotal();
            }
        }

        data.getColumn(3).setSelectedRecordId(playerRecordId);
        data.getColumn(3).setContent(s.toString());
    }

    protected static void reportPlayerLine(final StringBuilder s, final String activity, final HousegroupRecord houseGroup,
            final Integer income, final Integer satisfaction, final boolean nullIsDash)
    {
        reportPlayerLine(s, activity, houseGroup, income, satisfaction, nullIsDash, "black");
    }

    protected static void reportPlayerLine(final StringBuilder s, final String activity, final HousegroupRecord houseGroup,
            final Integer income, final Integer satisfaction, final boolean nullIsDash, final String color)
    {
        s.append("            <tr style=\"color:" + color + ";\">\n");
        s.append("              <td width=\"55%\" align=\"left\">" + activity + "</td>\n");
        s.append("              <td width=\"15%\" align=\"left\">" + (houseGroup == null ? "" : houseGroup.getCode())
                + "</td>\n");
        s.append("              <td width=\"15%\" align=\"center\">" + fmtMoney(income, nullIsDash) + "</td>\n");
        s.append("              <td width=\"15%\" align=\"center\">" + fmtSatisfaction(satisfaction, nullIsDash) + "</td>\n");
        s.append("            </tr>\n");
    }

    protected static String fmtMoney(final Integer nr, final boolean nullIsDash)
    {
        if (nr == null)
            return "";
        if (nr < 0)
            return k(nr);
        if (nr > 0)
            return "+" + k(nr);
        return nullIsDash ? "-" : "0";
    }

    protected static String k(final int nr)
    {
        if (Math.abs(nr) < 1000)
            return Integer.toString(nr);
        else
            return Integer.toString(nr / 1000) + " k";
    }

    protected static String fmtSatisfaction(final Integer nr, final boolean nullIsDash)
    {
        if (nr == null)
            return "";
        if (nr < 0)
            return String.valueOf(nr);
        if (nr > 0)
            return "+" + String.valueOf(nr);
        return nullIsDash ? "-" : "0";
    }
}
