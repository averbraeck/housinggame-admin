package nl.tudelft.simulation.housinggame.admin;

import java.time.LocalDateTime;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryDateTime;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickList;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickRecord;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryText;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.common.GroupState;
import nl.tudelft.simulation.housinggame.common.PlayerState;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GrouproundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerroundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerstateRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.WelfaretypeRecord;

public class MaintainPlayPlayer
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("play-player"))
        {
            data.clearColumns("10%", "GameSession", "10%", "Group", "10%", "GroupRound", "10%", "Player", "10%", "PlayerRound",
                    "10%", "PlayerState");
            data.clearFormColumn("40%", "Edit Properties");
            showGameSession(session, data, 0);
        }

        else if (click.contains("PlayGameSession"))
        {
            showGameSession(session, data, recordId);
        }

        else if (click.endsWith("PlayGroup"))
        {
            showGroup(session, data, recordId);
        }

        else if (click.contains("PlayGroupRound"))
        {
            showPlayGroupRound(session, data, recordId);
        }

        else if (click.endsWith("PlayPlayer"))
        {
            showPlayer(session, data, recordId);
        }

        else if (click.contains("PlayPlayerRound"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.PLAYERROUND, "play-player");
            else if (click.startsWith("delete"))
            {
                PlayerroundRecord playerRound = SqlUtils.readRecordFromId(data, Tables.PLAYERROUND, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(playerRound, "play-player");
                else
                {
                    GrouproundRecord groupRound =
                            SqlUtils.readRecordFromId(data, Tables.GROUPROUND, playerRound.getGrouproundId());
                    PlayerRecord player = SqlUtils.readRecordFromId(data, Tables.PLAYER, playerRound.getPlayerId());
                    data.askDeleteRecord(playerRound, "PlayPlayerRound",
                            "Player " + player.getCode() + ", round " + groupRound.getRoundNumber(), "deletePlayPlayerRoundOk",
                            "play-player");
                }
                recordId = 0;
            }
            if (!data.isError())
            {
                showPlayPlayerRound(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editPlayPlayerRound(session, data, 0, true);
            }
        }

        else if (click.contains("PlayerState"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.PLAYERSTATE, "play-player");
            else if (click.startsWith("delete"))
            {
                PlayerstateRecord playerState = SqlUtils.readRecordFromId(data, Tables.PLAYERSTATE, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(playerState, "play-player");
                else
                {
                    PlayerroundRecord playerRound =
                            SqlUtils.readRecordFromId(data, Tables.PLAYERROUND, playerState.getPlayerroundId());
                    PlayerRecord player = SqlUtils.readRecordFromId(data, Tables.PLAYER, playerRound.getPlayerId());
                    GrouproundRecord groupRound =
                            SqlUtils.readRecordFromId(data, Tables.GROUPROUND, playerRound.getGrouproundId());
                    data.askDeleteRecord(playerState, "PlayerState", "Player " + player.getCode() + ", round "
                            + groupRound.getRoundNumber() + ", state " + playerState.getPlayerState(), "deletePlayerStateOk",
                            "play-player");
                }
                recordId = 0;
            }
            if (!data.isError())
            {
                showPlayerState(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editPlayerState(session, data, 0, true);
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
        data.showColumn("PlayGameSession", 0, recordId, false, Tables.GAMESESSION, Tables.GAMESESSION.NAME, "name", false);
        data.resetColumn(1);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("PlayGroup", 1, 0, false, Tables.GROUP, Tables.GROUP.NAME, "name",
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
        data.showColumn("PlayGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayGroup", 1, recordId, false, Tables.GROUP, Tables.GROUP.NAME, "name",
                Tables.GROUP.GAMESESSION_ID, false);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("PlayGroupRound", 2, 0, false, Tables.GROUPROUND, Tables.GROUPROUND.ROUND_NUMBER,
                    "round_number", Tables.GROUPROUND.GROUP_ID, false);
        }
    }

    /*
     * *********************************************************************************************************
     * ******************************************** GROUPROUND *************************************************
     * *********************************************************************************************************
     */

    public static void showPlayGroupRound(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("PlayGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("PlayGroupRound", 2, recordId, false, Tables.GROUPROUND, Tables.GROUPROUND.ROUND_NUMBER,
                "round_number", Tables.GROUPROUND.GROUP_ID, false);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            showPlayerColumn(data, "PlayPlayer", 3, 0);
        }
    }

    /*
     * *********************************************************************************************************
     * ********************************************** PLAYER ***************************************************
     * *********************************************************************************************************
     */

    public static void showPlayer(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("PlayGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("PlayGroupRound", 2, data.getColumn(2).getSelectedRecordId(), false, Tables.GROUPROUND,
                Tables.GROUPROUND.ROUND_NUMBER, "round_number", Tables.GROUPROUND.GROUP_ID, false);
        showPlayerColumn(data, "PlayPlayer", 3, recordId);
        data.resetColumn(4);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            showPlayerRoundColumn(data, "PlayPlayerRound", 4, 0);
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
        data.showColumn("PlayGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("PlayGroupRound", 2, data.getColumn(2).getSelectedRecordId(), false, Tables.GROUPROUND,
                Tables.GROUPROUND.ROUND_NUMBER, "round_number", Tables.GROUPROUND.GROUP_ID, false);
        showPlayerColumn(data, "PlayPlayer", 3, data.getColumn(3).getSelectedRecordId());
        showPlayerRoundColumn(data, "PlayPlayerRound", 4, recordId);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("PlayPlayerState", 5, 0, true, Tables.PLAYERSTATE, Tables.PLAYERSTATE.TIMESTAMP, "player_state",
                    Tables.PLAYERSTATE.PLAYERROUND_ID, true, "PlayerState");
            editPlayPlayerRound(session, data, recordId, editRecord);
        }
    }

    public static void editPlayPlayerRound(final HttpSession session, final AdminData data, final int playerRoundId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        PlayerroundRecord playerRound = playerRoundId == 0 ? dslContext.newRecord(Tables.PLAYERROUND)
                : dslContext.selectFrom(Tables.PLAYERROUND).where(Tables.PLAYERROUND.ID.eq(playerRoundId)).fetchOne();
        int groupRoundId = playerRoundId == 0 ? data.getColumn(2).getSelectedRecordId() : playerRound.getGrouproundId();
        GrouproundRecord groupRound = SqlUtils.readRecordFromId(data, Tables.GROUPROUND, groupRoundId);
        GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, groupRound.getGroupId());
        PlayerRecord player = SqlUtils.readRecordFromId(data, Tables.PLAYER, data.getColumn(3).getSelectedRecordId());
        WelfaretypeRecord welfareType = SqlUtils.readRecordFromId(data, Tables.WELFARETYPE, player.getWelfaretypeId());

        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("play-player", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editPlayPlayerRound")
                .setSaveMethod("savePlayPlayerRound")
                .setDeleteMethod("deletePlayPlayerRound", "Delete", "<br>Note: PlayerRound can only be deleted when it "
                        + "<br>has not been used for measures by a player")
                .setRecordNr(playerRoundId)
                .setLabelLength("50%")
                .startForm()
                .addEntry(new TableEntryDateTime(Tables.PLAYERROUND.CREATE_TIME)
                        .setInitialValue(playerRound.getCreateTime(), null)
                        .setLabel("Create time")
                        .setReadOnly())
                .addEntry(new TableEntryPickRecord(Tables.PLAYERROUND.PLAYER_ID)
                        .setRequired()
                        .setPickTable(data, Tables.PLAYER.where(Tables.PLAYER.ID.eq(player.getId())),
                                Tables.PLAYER.ID, Tables.PLAYER.CODE)
                        .setInitialValue(playerRound.getPlayerId(), 0)
                        .setLabel("Player code")
                        .setReadOnly())
                .addEntry(new TableEntryPickList(Tables.PLAYERROUND.PLAYER_STATE)
                        .setRequired()
                        .setPickListEntries(PlayerState.class)
                        .setInitialValue(playerRound.getPlayerState(), "")
                        .setLabel("Player State"))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.ROUND_INCOME)
                        .setRequired()
                        .setInitialValue(playerRound.getRoundIncome(), welfareType.getRoundIncome())
                        .setLabel("Round income")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.LIVING_COSTS)
                        .setRequired()
                        .setInitialValue(playerRound.getLivingCosts(), welfareType.getLivingCosts())
                        .setLabel("Living costs")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.PAID_DEBT)
                        .setRequired()
                        .setInitialValue(playerRound.getPaidDebt(), 0)
                        .setLabel("Paid debt")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.MORTGAGE_PAYMENT)
                        .setRequired()
                        .setInitialValue(playerRound.getMortgagePayment(), 0)
                        .setLabel("Paid mortgage")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.PROFIT_SOLD_HOUSE)
                        .setRequired()
                        .setInitialValue(playerRound.getProfitSoldHouse(), 0)
                        .setLabel("Profit sold house"))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.SPENT_SAVINGS_FOR_BUYING_HOUSE)
                        .setRequired()
                        .setInitialValue(playerRound.getSpentSavingsForBuyingHouse(), 0)
                        .setLabel("Spent savings used for house")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.COST_TAXES)
                        .setRequired()
                        .setInitialValue(playerRound.getCostTaxes(), 0)
                        .setLabel("Cost taxes")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.COST_MEASURES_BOUGHT)
                        .setRequired()
                        .setInitialValue(playerRound.getCostMeasuresBought(), 0)
                        .setLabel("Cost measures bought")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.COST_SATISFACTION_BOUGHT)
                        .setRequired()
                        .setInitialValue(playerRound.getCostSatisfactionBought(), 0)
                        .setLabel("Cost satisfaction bought")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.COST_FLUVIAL_DAMAGE)
                        .setRequired()
                        .setInitialValue(playerRound.getCostFluvialDamage(), 0)
                        .setLabel("Cost fluvial damage")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.COST_PLUVIAL_DAMAGE)
                        .setRequired()
                        .setInitialValue(playerRound.getCostPluvialDamage(), 0)
                        .setLabel("Cost pluvial damage")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.SPENDABLE_INCOME)
                        .setRequired()
                        .setInitialValue(playerRound.getSpendableIncome(), 0)
                        .setLabel("Spendable income"))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.SATISFACTION_MOVE_PENALTY)
                        .setRequired()
                        .setInitialValue(playerRound.getSatisfactionMovePenalty(), 0)
                        .setLabel("Satisfaction move penalty")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.SATISFACTION_HOUSE_RATING_DELTA)
                        .setRequired()
                        .setInitialValue(playerRound.getSatisfactionHouseRatingDelta(), 0)
                        .setLabel("Satisfaction house rating delta"))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.SATISFACTION_HOUSE_MEASURES)
                        .setRequired()
                        .setInitialValue(playerRound.getSatisfactionHouseMeasures(), 0)
                        .setLabel("Satisfaction house measures")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.SATISFACTION_BOUGHT)
                        .setRequired()
                        .setInitialValue(playerRound.getSatisfactionBought(), 0)
                        .setLabel("Satisfaction bought")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.SATISFACTION_FLUVIAL_PENALTY)
                        .setRequired()
                        .setInitialValue(playerRound.getSatisfactionFluvialPenalty(), 0)
                        .setLabel("Satisfaction fluvial damage")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.SATISFACTION_PLUVIAL_PENALTY)
                        .setRequired()
                        .setInitialValue(playerRound.getSatisfactionPluvialPenalty(), 0)
                        .setLabel("Satisfaction pluvial damage")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.SATISFACTION_DEBT_PENALTY)
                        .setRequired()
                        .setInitialValue(playerRound.getSatisfactionDebtPenalty(), 0)
                        .setLabel("Satisfaction debt penalty")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.PERSONAL_SATISFACTION)
                        .setRequired()
                        .setInitialValue(playerRound.getPersonalSatisfaction(), 0)
                        .setLabel("Personal satisfaction"))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.PREFERRED_HOUSE_RATING)
                        .setRequired()
                        .setInitialValue(playerRound.getPreferredHouseRating(), welfareType.getPreferredHouseRating())
                        .setLabel("Preferred house rating")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.MAXIMUM_MORTGAGE)
                        .setRequired()
                        .setInitialValue(playerRound.getMaximumMortgage(), 0)
                        .setLabel("Maximum mortgage")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.MORTGAGE_HOUSE_START)
                        .setRequired()
                        .setInitialValue(playerRound.getMortgageHouseStart(), 0)
                        .setLabel("Full mortgage start of round")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.MORTGAGE_LEFT_START)
                        .setRequired()
                        .setInitialValue(playerRound.getMortgageLeftStart(), 0)
                        .setLabel("Mortgage left start of round")
                        .setMin(0))
                .addEntry(new TableEntryPickRecord(Tables.PLAYERROUND.START_HOUSEGROUP_ID)
                        .setPickTable(data, Tables.HOUSEGROUP.join(Tables.HOUSE)
                                .on(Tables.HOUSEGROUP.HOUSE_ID.eq(Tables.HOUSE.ID))
                                .and(Tables.HOUSEGROUP.GROUP_ID.eq(group.getId())),
                                Tables.HOUSEGROUP.ID, Tables.HOUSE.CODE)
                        .setInitialValue(playerRound.getStartHousegroupId(), 0)
                        .setLabel("House start of round"))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.HOUSE_PRICE_SOLD)
                        .setRequired()
                        .setInitialValue(playerRound.getHousePriceSold(), 0)
                        .setLabel("House Price sold")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.HOUSE_PRICE_BOUGHT)
                        .setRequired()
                        .setInitialValue(playerRound.getHousePriceBought(), 0)
                        .setLabel("House Price bought")
                        .setMin(0))
                .addEntry(new TableEntryPickRecord(Tables.PLAYERROUND.FINAL_HOUSEGROUP_ID)
                        .setPickTable(data, Tables.HOUSEGROUP.join(Tables.HOUSE)
                                .on(Tables.HOUSEGROUP.HOUSE_ID.eq(Tables.HOUSE.ID))
                                .and(Tables.HOUSEGROUP.GROUP_ID.eq(group.getId())),
                                Tables.HOUSEGROUP.ID, Tables.HOUSE.CODE)
                        .setInitialValue(playerRound.getFinalHousegroupId(), 0)
                        .setLabel("House end of round"))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.MORTGAGE_HOUSE_END)
                        .setRequired()
                        .setInitialValue(playerRound.getMortgageHouseEnd(), 0)
                        .setLabel("Full mortgage end of round")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.MORTGAGE_LEFT_END)
                        .setRequired()
                        .setInitialValue(playerRound.getMortgageLeftEnd(), 0)
                        .setLabel("Mortgage left end of round")
                        .setMin(0))
                .addEntry(new TableEntryPickRecord(Tables.PLAYERROUND.MOVINGREASON_ID)
                        .setPickTable(data, Tables.MOVINGREASON,
                                Tables.MOVINGREASON.ID, Tables.MOVINGREASON.KEY)
                        .setInitialValue(playerRound.getMovingreasonId(), null)
                        .setRequired(false)
                        .setLabel("Moving reason"))
                .addEntry(new TableEntryString(Tables.PLAYERROUND.MOVING_REASON_OTHER)
                        .setInitialValue(playerRound.getMovingReasonOther(), "")
                        .setLabel("Moving reason other")
                        .setMaxChars(45))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.FLUVIAL_BASE_PROTECTION)
                        .setRequired()
                        .setInitialValue(playerRound.getFluvialBaseProtection(), 0)
                        .setLabel("Fluvial base protection")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.PLUVIAL_BASE_PROTECTION)
                        .setRequired()
                        .setInitialValue(playerRound.getPluvialBaseProtection(), 0)
                        .setLabel("Pluvial base protection")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.FLUVIAL_COMMUNITY_DELTA)
                        .setRequired()
                        .setInitialValue(playerRound.getFluvialCommunityDelta(), 0)
                        .setLabel("Fluvial community delta"))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.PLUVIAL_COMMUNITY_DELTA)
                        .setRequired()
                        .setInitialValue(playerRound.getPluvialCommunityDelta(), 0)
                        .setLabel("Pluvial community delta"))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.FLUVIAL_HOUSE_DELTA)
                        .setRequired()
                        .setInitialValue(playerRound.getFluvialHouseDelta(), 0)
                        .setLabel("Fluvial house delta"))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.PLUVIAL_HOUSE_DELTA)
                        .setRequired()
                        .setInitialValue(playerRound.getPluvialHouseDelta(), 0)
                        .setLabel("Pluvial house delta"))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.GROUPROUND_ID)
                        .setInitialValue(groupRoundId, 0)
                        .setLabel("GroupRound id")
                        .setHidden(true))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.ACTIVE_TRANSACTION_ID)
                        .setInitialValue(playerRound.getActiveTransactionId(), 0)
                        .setLabel("Transaction id")
                        .setReadOnly(false))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit PlayerRound", form);
    }

    /*
     * *********************************************************************************************************
     * ******************************************** PLAYERSTATE ************************************************
     * *********************************************************************************************************
     */

    public static void showPlayerState(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("PlayGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("PlayGroupRound", 2, data.getColumn(2).getSelectedRecordId(), false, Tables.GROUPROUND,
                Tables.GROUPROUND.ROUND_NUMBER, "round_number", Tables.GROUPROUND.GROUP_ID, false);
        showPlayerColumn(data, "PlayPlayer", 3, data.getColumn(3).getSelectedRecordId());
        showPlayerRoundColumn(data, "PlayPlayerRound", 4, data.getColumn(4).getSelectedRecordId());
        data.showDependentColumn("PlayPlayerState", 5, recordId, true, Tables.PLAYERSTATE, Tables.PLAYERSTATE.TIMESTAMP, "player_state",
                Tables.PLAYERSTATE.PLAYERROUND_ID, true, "PlayerState");
        data.resetFormColumn();
        if (recordId != 0)
        {
            editPlayerState(session, data, recordId, editRecord);
        }
    }

    public static void editPlayerState(final HttpSession session, final AdminData data, final int playerStateId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        PlayerstateRecord playerState = playerStateId == 0 ? dslContext.newRecord(Tables.PLAYERSTATE)
                : dslContext.selectFrom(Tables.PLAYERSTATE).where(Tables.PLAYERSTATE.ID.eq(playerStateId)).fetchOne();
        int playerRoundId = playerStateId == 0 ? data.getColumn(4).getSelectedRecordId() : playerState.getPlayerroundId();

        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("play-player", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editPlayPlayerState")
                .setSaveMethod("savePlayPlayerState")
                .setDeleteMethod("deletePlayPlayerState", "Delete", "<br>Be careful with deleting a player state "
                        + "<br>when the player round contains this state")
                .setRecordNr(playerStateId)
                .startForm()
                .addEntry(new TableEntryDateTime(Tables.PLAYERSTATE.TIMESTAMP)
                        .setRequired()
                        .setInitialValue(playerState.getTimestamp(), LocalDateTime.now())
                        .setLabel("Timestamp"))
                .addEntry(new TableEntryPickList(Tables.PLAYERSTATE.PLAYER_STATE)
                        .setRequired()
                        .setPickListEntries(PlayerState.class)
                        .setInitialValue(playerState.getPlayerState(), "")
                        .setLabel("Player State"))
                .addEntry(new TableEntryText(Tables.PLAYERSTATE.CONTENT)
                        .setRequired(false)
                        .setInitialValue(playerState.getContent(), "")
                        .setLabel("Content")
                        .setRows(10))
                .addEntry(new TableEntryInt(Tables.PLAYERSTATE.PLAYERROUND_ID)
                        .setInitialValue(playerRoundId, 0)
                        .setLabel("PlayerRound id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit PlayerState", form);
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

}
