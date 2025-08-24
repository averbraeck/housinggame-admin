package nl.tudelft.simulation.housinggame.admin;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nl.tudelft.simulation.housinggame.common.SqlUtils;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GrouproundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupstateRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HousegroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerroundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerstateRecord;

public class MaintainPlayerTimeline
{

    public static void handleMenu(final HttpServletRequest request, final HttpServletResponse response, final String click,
            final int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("timeline"))
        {
            data.clearColumns("15%", "GameSession", "15%", "Group", "15%", "Player", "55%", "Timeline");
            data.setFormColumn(null);
            showGameSession(session, data, 0);
        }

        else if (click.endsWith("GameSession"))
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
        data.showColumn("TimelineGameSession", 0, recordId, false, Tables.GAMESESSION, Tables.GAMESESSION.NAME, "name", false);
        data.resetColumn(1);
        data.resetColumn(2);
        data.resetColumn(3);
        if (recordId != 0)
        {
            data.showDependentColumn("TimelineGroup", 1, 0, false, Tables.GROUP, Tables.GROUP.NAME, "name",
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
        data.showColumn("TimelineGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("TimelineGroup", 1, recordId, false, Tables.GROUP, Tables.GROUP.NAME, "name",
                Tables.GROUP.GAMESESSION_ID, false);
        data.resetColumn(2);
        data.resetColumn(3);
        if (recordId != 0)
        {
            data.showDependentColumn("TimelinePlayer", 2, 0, false, Tables.PLAYER, Tables.PLAYER.CODE, "code",
                    Tables.PLAYER.GROUP_ID, false);
        }
    }

    /*
     * *********************************************************************************************************
     * ********************************************** PLAYER ***************************************************
     * *********************************************************************************************************
     */

    public static void showPlayer(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("TimelineGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("TimelineGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        showPlayerColumn(data, "TimelinePlayer", 2, recordId);
        new PlayerTimeline().showTimeline(data, recordId);
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

    /*
     * *********************************************************************************************************
     * ****************************************** PLAYER TIMELINE **********************************************
     * *********************************************************************************************************
     */

    protected static class PlayerTimeline
    {
        private final StringBuilder s = new StringBuilder();

        int incPrevRound = 0;

        int satPrevRound = 0;

        GrouproundRecord groupRound;

        PlayerroundRecord pr;

        public void showTimeline(final AdminData data, final int playerRecordId)
        {
            DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);

            PlayerRecord player = SqlUtils.readRecordFromId(data, Tables.PLAYER, playerRecordId);
            GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, player.getGroupId());

            this.s.append("        <h2>Group: " + group.getName() + "</h2>\n");
            this.s.append("        <h2>Player: " + player.getCode() + "</h2>\n");

            List<PlayerroundRecord> playerRoundList = dslContext.selectFrom(Tables.PLAYERROUND)
                    .where(Tables.PLAYERROUND.PLAYER_ID.eq(player.getId())).fetch().sortAsc(Tables.PLAYERROUND.CREATE_TIME);
            for (var playerRound : playerRoundList)
            {
                this.pr = playerRound;
                this.groupRound = SqlUtils.readRecordFromId(data, Tables.GROUPROUND, this.pr.getGrouproundId());
                int round = this.groupRound.getRoundNumber();
                if (round == 0)
                {
                    this.incPrevRound = this.pr.getSpendableIncome();
                    this.satPrevRound = this.pr.getSatisfactionTotal();
                }
                else
                {
                    this.s.append("        <h3>Round: " + round + "</h3>\n");
                    this.s.append("\n      <div>\n");
                    this.s.append(AdminTable.startTable());
                    this.s.append("        <table width=\"100%\">\n");
                    this.s.append("          <thead>\n");
                    this.s.append("            <tr>\n");
                    this.s.append("              <td width=\"15%\" align=\"left\">Timestamp</td>\n");
                    this.s.append("              <td width=\"20%\" align=\"left\">STATE</td>\n");
                    this.s.append("              <td width=\"25%\" align=\"left\">Activity</td>\n");
                    this.s.append("              <td width=\"10%\" align=\"center\">Value</td>\n");
                    this.s.append("              <td width=\"10%\" align=\"left\">House</td>\n");
                    this.s.append("              <td width=\"10%\" align=\"center\">Budget</td>\n");
                    this.s.append("              <td width=\"10%\" align=\"center\">Satisfation</td>\n");
                    this.s.append("            </tr>\n");
                    this.s.append("          </thead>\n");
                    this.s.append("          <tbody>\n");

                    List<PlayerstateRecord> playerStateList =
                            dslContext.selectFrom(Tables.PLAYERSTATE).where(Tables.PLAYERSTATE.PLAYERROUND_ID.eq(this.pr.getId()))
                                    .fetch().sortAsc(Tables.PLAYERSTATE.TIMESTAMP);
                    List<GroupstateRecord> groupStateList = dslContext.selectFrom(Tables.GROUPSTATE)
                            .where(Tables.GROUPSTATE.GROUPROUND_ID.eq(this.groupRound.getId())).fetch()
                            .sortAsc(Tables.GROUPSTATE.TIMESTAMP);
                    while (!playerStateList.isEmpty() && !groupStateList.isEmpty())
                    {
                        if (playerStateList.isEmpty())
                            reportGroupState(groupStateList.remove(0));
                        else if (playerStateList.isEmpty())
                            reportPlayerState(playerStateList.remove(0));
                        else
                        {
                            var gTime = groupStateList.get(0).getTimestamp();
                            var pTime = playerStateList.get(0).getTimestamp();
                            if (gTime.isBefore(pTime))
                                reportGroupState(groupStateList.remove(0));
                            else
                                reportPlayerState(playerStateList.remove(0));
                        }
                    }

                    this.s.append("          </tbody>\n");
                    this.s.append("        </table>\n");
                    this.s.append("      </div>");
                    this.s.append(AdminTable.endTable());
                    this.s.append("<br>\n");

                    this.incPrevRound = this.pr.getSpendableIncome();
                    this.satPrevRound = this.pr.getSatisfactionTotal();
                }
            }

            data.getColumn(3).setSelectedRecordId(playerRecordId);
            data.getColumn(3).setContent(this.s.toString());
        }

        protected static void reportGroupState(final GroupstateRecord groupState)
        {

        }

        protected static void reportPlayerState(final PlayerstateRecord playerState)
        {

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
            s.append("              <td width=\"15%\" align=\"center\">" + fmtSatisfaction(satisfaction, nullIsDash)
                    + "</td>\n");
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

    /*-
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
     */
}
