package nl.tudelft.simulation.housinggame.admin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import nl.tudelft.simulation.housinggame.common.CumulativeNewsEffects;
import nl.tudelft.simulation.housinggame.common.SqlUtils;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.CommunityRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GrouproundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupstateRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HouseRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HousegroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HousemeasureRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasuretypeRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PersonalmeasureRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerroundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerstateRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.TaxRecord;

public class MaintainPlayerTimeline
{

    public static void handleMenu(final HttpServletRequest request, final HttpServletResponse response, final String click,
            final int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("timeline"))
        {
            data.clearColumns("15%", "GameSession", "10%", "Group", "10%", "Player", "65%", "Timeline");
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

        HousegroupRecord startHouseGroup;

        HousegroupRecord finalHouseGroup;

        boolean moved;

        AdminData data;

        int paidTax;

        public void showTimeline(final AdminData data, final int playerRecordId)
        {
            this.data = data;
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

                    this.startHouseGroup = this.pr.getStartHousegroupId() != null
                            ? SqlUtils.readRecordFromId(data, Tables.HOUSEGROUP, this.pr.getStartHousegroupId()) : null;
                    this.finalHouseGroup = this.pr.getFinalHousegroupId() != null
                            ? SqlUtils.readRecordFromId(data, Tables.HOUSEGROUP, this.pr.getFinalHousegroupId()) : null;
                    this.moved = !Objects.equals(this.pr.getStartHousegroupId(), this.pr.getFinalHousegroupId());

                    List<PlayerstateRecord> playerStateList = dslContext.selectFrom(Tables.PLAYERSTATE)
                            .where(Tables.PLAYERSTATE.PLAYERROUND_ID.eq(this.pr.getId())).fetch()
                            .sortAsc(Tables.PLAYERSTATE.TIMESTAMP);
                    List<GroupstateRecord> groupStateList = dslContext.selectFrom(Tables.GROUPSTATE)
                            .where(Tables.GROUPSTATE.GROUPROUND_ID.eq(this.groupRound.getId())).fetch()
                            .sortAsc(Tables.GROUPSTATE.TIMESTAMP);
                    LocalDateTime time = null;
                    while (!playerStateList.isEmpty() || !groupStateList.isEmpty())
                    {
                        if (playerStateList.isEmpty())
                        {
                            time = groupStateList.get(0).getTimestamp();
                            reportGroupState(groupStateList.remove(0));
                        }
                        else if (groupStateList.isEmpty())
                        {
                            time = playerStateList.get(0).getTimestamp();
                            reportPlayerState(playerStateList.remove(0));
                        }
                        else
                        {
                            var gTime = groupStateList.get(0).getTimestamp();
                            var pTime = playerStateList.get(0).getTimestamp();
                            if (gTime.isBefore(pTime))
                            {
                                time = groupStateList.get(0).getTimestamp();
                                reportGroupState(groupStateList.remove(0));
                            }
                            else
                            {
                                time = playerStateList.get(0).getTimestamp();
                                reportPlayerState(playerStateList.remove(0));
                            }
                        }
                    }
                    reportLastPlayerState(time);

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

        protected void reportGroupState(final GroupstateRecord groupState)
        {
            switch (groupState.getGroupState())
            {
                case "NEW_ROUND" -> reportPlayerLine(groupState.getTimestamp(), "F:" + groupState.getGroupState(),
                        "Facilitator starts new round", "Round " + String.valueOf(this.groupRound.getRoundNumber()), null, null,
                        null, true);
                case "ROLLED_DICE" -> {
                    reportPlayerLine(groupState.getTimestamp(), "F:" + groupState.getGroupState(), "Rolled dice", "F="
                            + this.groupRound.getFluvialFloodIntensity() + ", P=" + this.groupRound.getPluvialFloodIntensity(),
                            null, null, null, true);
                }

                default -> reportPlayerLine(groupState.getTimestamp(), "F:" + groupState.getGroupState(), "FACILITATOR", "",
                        null, null, null, true);
            }
        }

        protected void reportPlayerState(final PlayerstateRecord playerState)
        {
            switch (playerState.getPlayerState())
            {
                case "READ_BUDGET" -> {
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "Player starts new round",
                            "Round " + String.valueOf(this.groupRound.getRoundNumber()), this.startHouseGroup,
                            this.incPrevRound, this.satPrevRound, false);
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "Round income", "", null,
                            this.pr.getRoundIncome(), null, true);
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "Living costs", "", null,
                            -this.pr.getLivingCosts(), null, true);
                    if (this.pr.getPaidDebt() != 0)
                        reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(),
                                "Debt satisfaction change", "", null, null, -this.pr.getSatisfactionDebtPenalty(), false);
                    // valid personal measures

                    // valid house measures

                }
                case "STAY_HOUSE_WAIT" -> {
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(),
                            "Stayed in house, total mortgage", k(this.pr.getMortgageHouseEnd()), this.finalHouseGroup, null,
                            null, true);
                }
                case "STAYED_HOUSE" -> {
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "Mortgage start of round",
                            k(this.pr.getMortgageLeftStart()), this.finalHouseGroup, null, null, true);
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "Mortgage payment", "",
                            this.finalHouseGroup, -this.pr.getMortgagePayment(), null, true);
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "Mortgage end of round",
                            k(this.pr.getMortgageLeftEnd()), this.finalHouseGroup, null, null, true);
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(),
                            "House rating satisfaction", "", this.finalHouseGroup, null,
                            this.pr.getSatisfactionHouseRatingDelta(), true);
                }
                case "SELL_HOUSE_WAIT" -> {
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(),
                            "Sold house, total mortgage", k(this.pr.getMortgageHouseStart()), this.startHouseGroup, null, null,
                            true);
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(),
                            "Sold house, left mortgage", k(this.pr.getMortgageLeftStart()), this.startHouseGroup, null, null,
                            true);
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "Profit sold house", "",
                            this.startHouseGroup, this.pr.getProfitSoldHouse(), null, false);
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "Penalty for moving", "",
                            this.finalHouseGroup, null, -this.pr.getSatisfactionMovePenalty(), false);
                }
                case "BUY_HOUSE_WAIT" -> {
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(),
                            "Bought house, total mortgage", k(this.pr.getMortgageHouseEnd()), this.finalHouseGroup, null, null,
                            true);
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(),
                            "Spent savings for buying house", "", this.finalHouseGroup,
                            -this.pr.getSpentSavingsForBuyingHouse(), null, false);
                }
                case "BOUGHT_HOUSE" -> {
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "Mortgage start of round",
                            k(this.pr.getMortgageLeftStart()), this.finalHouseGroup, null, null, true);
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "Mortgage payment", "",
                            this.finalHouseGroup, -this.pr.getMortgagePayment(), null, true);
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "Mortgage end of round",
                            k(this.pr.getMortgageLeftEnd()), this.finalHouseGroup, null, null, true);
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(),
                            "House rating satisfaction", "", this.finalHouseGroup, null,
                            this.pr.getSatisfactionHouseRatingDelta(), true);
                }
                case "VIEW_TAXES" -> {
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "Paid taxes ", "",
                            this.finalHouseGroup, -this.pr.getCostTaxes(), null, true);
                    String taxes = calcTaxes();
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "Tax calc: " + taxes,
                            k(this.paidTax), this.finalHouseGroup, null, null, true);
                }
                case "VIEW_IMPROVEMENTS" -> {
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "House measures bought",
                            "", this.finalHouseGroup, -this.pr.getCostHouseMeasuresBought(),
                            this.pr.getSatisfactionHouseMeasures(), true);
                    reportHouseMeasuresBought(playerState);
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(),
                            "Personal measures bought", "", null, -this.pr.getCostPersonalMeasuresBought(),
                            this.pr.getSatisfactionPersonalMeasures(), true);
                    reportPersonalMeasuresBought(playerState);
                }
                case "VIEW_DAMAGE" -> {
                    String protP = "B:" + this.finalHouseGroup.getPluvialBaseProtection() + ", H:"
                            + this.finalHouseGroup.getPluvialHouseProtection();
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "Pluvial damage", protP,
                            this.finalHouseGroup, -this.pr.getCostPluvialDamage(), -this.pr.getSatisfactionPluvialPenalty(),
                            true);
                    String protF = "B:" + this.finalHouseGroup.getFluvialBaseProtection() + ", H:"
                            + this.finalHouseGroup.getFluvialHouseProtection();
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "Fluvial damage", protF,
                            this.finalHouseGroup, -this.pr.getCostFluvialDamage(), -this.pr.getSatisfactionFluvialPenalty(),
                            true);
                }
                case "VIEW_SUMMARY" -> {
                    reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(),
                            "End of round " + this.groupRound.getRoundNumber(), "", this.finalHouseGroup,
                            this.pr.getSpendableIncome(), this.pr.getSatisfactionTotal(), false);
                }

                default -> reportPlayerLine(playerState.getTimestamp(), "P:" + playerState.getPlayerState(), "PLAYER", "", null,
                        null, null, true);
            }
        }

        protected void reportLastPlayerState(final LocalDateTime time)
        {
            int incCheck = this.incPrevRound + this.pr.getRoundIncome() - this.pr.getLivingCosts()
                    - this.pr.getMortgagePayment() + this.pr.getProfitSoldHouse() - this.pr.getSpentSavingsForBuyingHouse()
                    - this.pr.getCostTaxes() - this.pr.getCostHouseMeasuresBought() - this.pr.getCostPersonalMeasuresBought()
                    - this.pr.getCostPluvialDamage() - this.pr.getCostFluvialDamage();
            int satCheck = this.satPrevRound - this.pr.getSatisfactionDebtPenalty() + this.pr.getSatisfactionHouseRatingDelta()
                    - this.pr.getSatisfactionMovePenalty() + this.pr.getSatisfactionHouseMeasures()
                    + this.pr.getSatisfactionPersonalMeasures() - this.pr.getSatisfactionPluvialPenalty()
                    - this.pr.getSatisfactionFluvialPenalty();
            if (this.pr.getSpendableIncome() != incCheck || this.pr.getSatisfactionTotal() != satCheck)
                reportPlayerLine(time, "P:VIEW_SUMMARY", "CHECK end of round " + this.groupRound.getRoundNumber(), "",
                        this.finalHouseGroup, incCheck, satCheck, false, "red");
        }

        protected void reportPlayerLine(final LocalDateTime timestamp, final String state, final String activity,
                final String value, final HousegroupRecord houseGroup, final Integer income, final Integer satisfaction,
                final boolean nullIsDash)
        {
            reportPlayerLine(timestamp, state, activity, value, houseGroup, income, satisfaction, nullIsDash, "black");
        }

        protected void reportPlayerLine(final LocalDateTime timestamp, final String state, final String activity,
                final String value, final HousegroupRecord houseGroup, final Integer income, final Integer satisfaction,
                final boolean nullIsDash, final String color)
        {
            this.s.append("            <tr style=\"color:" + color + ";\">\n");
            this.s.append("              <td width=\"15%\" align=\"left\">"
                    + timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "</td>\n");
            this.s.append("              <td width=\"20%\" align=\"left\">" + (state == null ? "" : state) + "</td>\n");
            this.s.append("              <td width=\"25%\" align=\"left\">" + (activity == null ? "" : activity) + "</td>\n");
            this.s.append("              <td width=\"10%\" align=\"left\">" + (value == null ? "" : value) + "</td>\n");
            this.s.append("              <td width=\"10%\" align=\"left\">" + (houseGroup == null ? "" : houseGroup.getCode())
                    + "</td>\n");
            this.s.append("              <td width=\"10%\" align=\"center\">" + fmtMoney(income, nullIsDash) + "</td>\n");
            this.s.append("              <td width=\"10%\" align=\"center\">" + fmtSatisfaction(satisfaction, nullIsDash)
                    + "</td>\n");
            this.s.append("            </tr>\n");
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

        protected String calcTaxes()
        {
            // get the focal player's data
            DSLContext dslContext = DSL.using(this.data.getDataSource(), SQLDialect.MYSQL);
            HousegroupRecord houseGroup =
                    AdminUtils.readRecordFromId(this.data, Tables.HOUSEGROUP, this.pr.getFinalHousegroupId());
            HouseRecord house = AdminUtils.readRecordFromId(this.data, Tables.HOUSE, houseGroup.getHouseId());
            CommunityRecord community = AdminUtils.readRecordFromId(this.data, Tables.COMMUNITY, house.getCommunityId());

            // count the number of players in this community
            int nrInCommunity = 0;
            List<PlayerroundRecord> players = dslContext.selectFrom(Tables.PLAYERROUND)
                    .where(Tables.PLAYERROUND.GROUPROUND_ID.eq(this.groupRound.getId())).fetch();
            for (var playerRound : players)
            {
                if (playerRound.getFinalHousegroupId() != null)
                {
                    HousegroupRecord playerHouseGroup =
                            AdminUtils.readRecordFromId(this.data, Tables.HOUSEGROUP, playerRound.getFinalHousegroupId());
                    HouseRecord playerHouse =
                            AdminUtils.readRecordFromId(this.data, Tables.HOUSE, playerHouseGroup.getHouseId());
                    if (playerHouse.getCommunityId().equals(community.getId()))
                        nrInCommunity += 1;
                }
            }

            // tax measures?
            GroupRecord group = AdminUtils.readRecordFromId(this.data, Tables.GROUP, this.groupRound.getGroupId());
            ScenarioRecord scenario = AdminUtils.readRecordFromId(this.data, Tables.SCENARIO, group.getScenarioId());
            var cumulativeNewsEffects = CumulativeNewsEffects.readCumulativeNewsEffects(this.data.getDataSource(), scenario,
                    this.groupRound.getRoundNumber());

            // get the tax brackets
            List<TaxRecord> taxList =
                    dslContext.selectFrom(Tables.TAX).where(Tables.TAX.COMMUNITY_ID.eq(community.getId())).fetch();

            // tax change based on measures
            int txc = (int) cumulativeNewsEffects.get(community.getId()).getTaxChange();
            int tax = 0;
            for (TaxRecord taxRecord : taxList)
            {
                if (nrInCommunity >= taxRecord.getMinimumInhabitants() && nrInCommunity <= taxRecord.getMaximumInhabitants())
                {
                    tax = taxRecord.getTaxCost().intValue();
                    break;
                }
            }

            // return result
            this.paidTax = tax + txc;
            return "nr=" + nrInCommunity + ", tax=" + k(tax) + ", measure=" + k(txc);
        }

        protected void reportPersonalMeasuresBought(final PlayerstateRecord playerState)
        {
            DSLContext dslContext = DSL.using(this.data.getDataSource(), SQLDialect.MYSQL);
            List<PersonalmeasureRecord> measures = dslContext.selectFrom(Tables.PERSONALMEASURE)
                    .where(Tables.PERSONALMEASURE.PLAYERROUND_ID.eq(this.pr.getId())).fetch();
            for (var measure : measures)
            {
                MeasuretypeRecord mt = AdminUtils.readRecordFromId(this.data, Tables.MEASURETYPE, measure.getMeasuretypeId());
                String md1 = "";
                if (mt.getSatisfactionDeltaOnce() != 0)
                    md1 += " sat:1x=" + mt.getSatisfactionDeltaOnce();
                if (mt.getSatisfactionDeltaPermanent() != 0)
                    md1 += " sat:perm=" + mt.getSatisfactionDeltaPermanent();
                if (mt.getValidOneRound() != 0)
                    md1 += " valid:1x";
                if (mt.getCostAbsolute() > 0)
                    md1 += " cost=" + k(mt.getCostAbsolute());
                if (mt.getCostPercentageIncome() > 0)
                    md1 += " cost:" + mt.getCostPercentageIncome() + "%inc="
                            + k((int) (0.01 * mt.getCostPercentageIncome() * this.pr.getRoundIncome()));
                if (mt.getCostPercentageHouse() > 0)
                    md1 += " cost:" + mt.getCostPercentageHouse() + "%hs="
                            + k((int) (0.01 * mt.getCostPercentageHouse() * this.finalHouseGroup.getMarketValue()));
                String md2 = "";
                if (mt.getPluvialProtectionDelta() > 0 || mt.getFluvialProtectionDelta() > 0)
                    md2 += "F=" + mt.getFluvialProtectionDelta() + ", P=" + mt.getPluvialProtectionDelta();
                reportPlayerLine(playerState.getTimestamp(), "PM:" + mt.getShortAlias(), md1, md2, null, null, null, true);
            }
        }

        protected void reportHouseMeasuresBought(final PlayerstateRecord playerState)
        {
            if (this.finalHouseGroup == null)
                return;
            DSLContext dslContext = DSL.using(this.data.getDataSource(), SQLDialect.MYSQL);
            List<HousemeasureRecord> measures = dslContext.selectFrom(Tables.HOUSEMEASURE)
                    .where(Tables.HOUSEMEASURE.HOUSEGROUP_ID.eq(this.finalHouseGroup.getId())).fetch();
            for (var measure : measures)
            {
                if (measure.getBoughtInRound() == this.groupRound.getRoundNumber())
                {
                    MeasuretypeRecord mt =
                            AdminUtils.readRecordFromId(this.data, Tables.MEASURETYPE, measure.getMeasuretypeId());
                    String md1 = "";
                    if (mt.getSatisfactionDeltaOnce() != 0)
                        md1 += " sat:1x=" + mt.getSatisfactionDeltaOnce();
                    if (mt.getSatisfactionDeltaPermanent() != 0)
                        md1 += " sat:perm=" + mt.getSatisfactionDeltaPermanent();
                    if (mt.getValidOneRound() != 0)
                        md1 += " valid:1x";
                    if (mt.getCostAbsolute() > 0)
                        md1 += " cost=" + k(mt.getCostAbsolute());
                    if (mt.getCostPercentageIncome() > 0)
                        md1 += " cost:" + mt.getCostPercentageIncome() + "%inc="
                                + k((int) (0.01 * mt.getCostPercentageIncome() * this.pr.getRoundIncome()));
                    if (mt.getCostPercentageHouse() > 0)
                        md1 += " cost:" + mt.getCostPercentageHouse() + "%hs="
                                + k((int) (0.01 * mt.getCostPercentageHouse() * this.finalHouseGroup.getMarketValue()));
                    String md2 = "";
                    if (mt.getPluvialProtectionDelta() > 0 || mt.getFluvialProtectionDelta() > 0)
                        md2 += "F=" + mt.getFluvialProtectionDelta() + ", P=" + mt.getPluvialProtectionDelta();
                    reportPlayerLine(playerState.getTimestamp(), "HM:" + mt.getShortAlias(), md1, md2, null, null, null, true);
                }
            }
        }

    }

}
