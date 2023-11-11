package nl.tudelft.simulation.housinggame.admin;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryDouble;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickRecordUInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryText;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryUInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.BidRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GamesessionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GameversionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GrouproundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HouseRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasureRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasuretypeRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerroundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.QuestionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.QuestionscoreRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.RoundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.WelfaretypeRecord;

public class MaintainPlay
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        System.out.println(click);

        if (click.equals("play"))
        {
            data.clearColumns("10%", "GameSession", "10%", "Group", "10%", "GroupRound", "10%", "Player", "10%", "PlayerRound",
                    "10%", "Measure");
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
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.GROUPROUND, "play");
            else if (click.startsWith("delete"))
            {
                GrouproundRecord groupRound = SqlUtils.readRecordFromId(data, Tables.GROUPROUND, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(groupRound, "play");
                else
                {
                    DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
                    RoundRecord round =
                            dslContext.selectFrom(Tables.ROUND.where(Tables.ROUND.ID.eq(groupRound.getRoundId()))).fetchOne();
                    data.deleteRecord(groupRound, "PlayGroupRound", String.valueOf(round.getRoundNumber()),
                            "deletePlayGroupRoundOk", "play");
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

        else if (click.contains("PlayBid"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.BID, "play");
            else if (click.startsWith("delete"))
            {
                BidRecord bid = SqlUtils.readRecordFromId(data, Tables.BID, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(bid, "play");
                else
                {
                    GrouproundRecord groupRound = SqlUtils.readRecordFromId(data, Tables.GROUPROUND, bid.getGrouproundId());
                    RoundRecord round = SqlUtils.readRecordFromId(data, Tables.ROUND, groupRound.getRoundId());
                    HouseRecord house = SqlUtils.readRecordFromId(data, Tables.HOUSE, bid.getHouseId());
                    data.deleteRecord(bid, "PlayBid",
                            "Round " + String.valueOf(round.getRoundNumber()) + ", House " + house.getAddress(),
                            "deletePlayBidOk", "play");
                }
                recordId = 0;
            }
            if (!data.isError())
            {
                showPlayBid(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editPlayBid(session, data, 0, true);
            }
        }

        else if (click.endsWith("PlayPlayer"))
        {
            showPlayer(session, data, recordId);
        }

        else if (click.contains("PlayPlayerRound"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.PLAYERROUND, "play");
            else if (click.startsWith("delete"))
            {
                PlayerroundRecord playerRound = SqlUtils.readRecordFromId(data, Tables.PLAYERROUND, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(playerRound, "play");
                else
                {
                    GrouproundRecord groupRound =
                            SqlUtils.readRecordFromId(data, Tables.GROUPROUND, playerRound.getGrouproundId());
                    RoundRecord round = SqlUtils.readRecordFromId(data, Tables.ROUND, groupRound.getRoundId());
                    PlayerRecord player = SqlUtils.readRecordFromId(data, Tables.PLAYER, playerRound.getPlayerId());
                    data.deleteRecord(playerRound, "PlayPlayerRound",
                            "Player " + player.getCode() + ", round " + round.getRoundNumber(), "deletePlayPlayerRoundOk",
                            "play");
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

        else if (click.contains("PlayMeasure"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.MEASURE, "play");
            else if (click.startsWith("delete"))
            {
                MeasureRecord measure = SqlUtils.readRecordFromId(data, Tables.MEASURE, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(measure, "play");
                else
                {
                    MeasuretypeRecord measureType =
                            SqlUtils.readRecordFromId(data, Tables.MEASURETYPE, measure.getMeasuretypeId());
                    data.deleteRecord(measure, "PlayMeasure", measureType.getName(), "deletePlayMeasureOk", "play");
                }
                recordId = 0;
            }
            if (!data.isError())
            {
                showPlayMeasure(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editPlayMeasure(session, data, 0, true);
            }
        }

        else if (click.contains("PlayQuestion"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.QUESTIONSCORE, "play");
            else if (click.startsWith("delete"))
            {
                QuestionscoreRecord questionScore = SqlUtils.readRecordFromId(data, Tables.QUESTIONSCORE, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(questionScore, "play");
                else
                {
                    QuestionRecord question = SqlUtils.readRecordFromId(data, Tables.QUESTION, questionScore.getQuestionId());
                    data.deleteRecord(questionScore, "PlayQuestion", question.getName(), "deletePlayQuestionOk", "play");
                }
                recordId = 0;
            }
            if (!data.isError())
            {
                showPlayQuestion(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editPlayQuestion(session, data, 0, true);
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
            showGroupRoundColumn(data, "PlayGroupRound", 2, 0);
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
        data.showColumn("PlayGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        showGroupRoundColumn(data, "PlayGroupRound", 2, recordId);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("PlayPlayer", 3, 0, false, Tables.PLAYER, Tables.PLAYER.CODE, "code",
                    Tables.PLAYER.GROUP_ID, false, 1);
            editPlayGroupRound(session, data, recordId, editRecord);
        }
    }

    public static void editPlayGroupRound(final HttpSession session, final AdminData data, final int groupRoundId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        GrouproundRecord groupRound = groupRoundId == 0 ? dslContext.newRecord(Tables.GROUPROUND) : dslContext
                .selectFrom(Tables.GROUPROUND).where(Tables.GROUPROUND.ID.eq(UInteger.valueOf(groupRoundId))).fetchOne();
        UInteger groupId =
                groupRoundId == 0 ? UInteger.valueOf(data.getColumn(1).getSelectedRecordId()) : groupRound.getGroupId();
        GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, groupId);
        ScenarioRecord scenario = SqlUtils.readRecordFromId(data, Tables.SCENARIO, group.getScenarioId());
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("play", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editPlayGroupRound")
                .setSaveMethod("savePlayGroupRound")
                .setDeleteMethod("deletePlayGroupRound", "Delete", "<br>Note: GroupRound can only be deleted when it "
                        + "<br>has not been used for play by a player")
                .setRecordNr(groupRoundId)
                .startForm()
                .addEntry(new TableEntryPickRecordUInt(Tables.GROUPROUND.ROUND_ID)
                        .setRequired()
                        .setPickTable(data, Tables.ROUND.where(Tables.ROUND.SCENARIO_ID.eq(scenario.getId())),
                                Tables.ROUND.ID, Tables.ROUND.ROUND_NUMBER)
                        .setInitialValue(groupRound.getRoundId(), UInteger.valueOf(0))
                        .setLabel("Round number"))
                .addEntry(new TableEntryInt(Tables.GROUPROUND.PLUVIAL_FLOOD_INTENSITY)
                        .setRequired()
                        .setInitialValue(groupRound.getPluvialFloodIntensity(), 0)
                        .setLabel("Pluvial flood intensity")
                        .setMin(0)
                        .setMax(10))
                .addEntry(new TableEntryInt(Tables.GROUPROUND.FLUVIAL_FLOOD_INTENSITY)
                        .setRequired()
                        .setInitialValue(groupRound.getFluvialFloodIntensity(), 0)
                        .setLabel("Fluvial flood intensity")
                        .setMin(0)
                        .setMax(10))
                .addEntry(new TableEntryUInt(Tables.GROUPROUND.GROUP_ID)
                        .setInitialValue(groupId, UInteger.valueOf(0))
                        .setLabel("Group id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit GroupRound", form);
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
        showGroupRoundColumn(data, "PlayGroupRound", 2, data.getColumn(2).getSelectedRecordId());
        data.showDependentColumn("PlayPlayer", 3, recordId, false, Tables.PLAYER, Tables.PLAYER.CODE, "code",
                Tables.PLAYER.GROUP_ID, false, 1);
        data.getColumn(3).setHeader("Player");
        data.resetColumn(4);
        data.getColumn(4).setHeader("PlayerRound");
        data.resetColumn(5);
        data.getColumn(5).setHeader("Measure");
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
        showGroupRoundColumn(data, "PlayGroupRound", 2, data.getColumn(2).getSelectedRecordId());
        data.showDependentColumn("PlayPlayer", 3, data.getColumn(3).getSelectedRecordId(), false, Tables.PLAYER,
                Tables.PLAYER.CODE, "code", Tables.PLAYER.GROUP_ID, false, 1);
        showPlayerRoundColumn(data, "PlayPlayerRound", 4, recordId);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            showMeasureColumn(data, "PlayMeasure", 5, 0);
            editPlayPlayerRound(session, data, recordId, editRecord);
        }
    }

    public static void editPlayPlayerRound(final HttpSession session, final AdminData data, final int playerRoundId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        PlayerroundRecord playerRound = playerRoundId == 0 ? dslContext.newRecord(Tables.PLAYERROUND) : dslContext
                .selectFrom(Tables.PLAYERROUND).where(Tables.PLAYERROUND.ID.eq(UInteger.valueOf(playerRoundId))).fetchOne();
        UInteger groupRoundId =
                playerRoundId == 0 ? UInteger.valueOf(data.getColumn(2).getSelectedRecordId()) : playerRound.getGrouproundId();
        GrouproundRecord groupRound = SqlUtils.readRecordFromId(data, Tables.GROUPROUND, groupRoundId);
        GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, groupRound.getGroupId());
        GamesessionRecord gameSession = SqlUtils.readRecordFromId(data, Tables.GAMESESSION, group.getGamesessionId());
        GameversionRecord gameVersion = SqlUtils.readRecordFromId(data, Tables.GAMEVERSION, gameSession.getGameversionId());
        PlayerRecord player = SqlUtils.readRecordFromId(data, Tables.PLAYER, data.getColumn(3).getSelectedRecordId());
        WelfaretypeRecord welfareType = SqlUtils.readRecordFromId(data, Tables.WELFARETYPE, player.getWelfaretypeId());

        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("play", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editPlayPlayerRound")
                .setSaveMethod("savePlayPlayerRound")
                .setDeleteMethod("deletePlayPlayerRound", "Delete", "<br>Note: PlayerRound can only be deleted when it "
                        + "<br>has not been used for measures by a player")
                .setRecordNr(playerRoundId)
                .startForm()
                .addEntry(new TableEntryPickRecordUInt(Tables.PLAYERROUND.PLAYER_ID)
                        .setRequired()
                        .setPickTable(data, Tables.PLAYER.where(Tables.PLAYER.ID.eq(player.getId())),
                                Tables.PLAYER.ID, Tables.PLAYER.CODE)
                        .setInitialValue(playerRound.getPlayerId(), UInteger.valueOf(0))
                        .setLabel("Player code")
                        .setReadOnly())
                .addEntry(new TableEntryPickRecordUInt(Tables.PLAYERROUND.HOUSE_ID)
                        .setPickTable(data, Tables.HOUSE.join(Tables.COMMUNITY)
                                .on(Tables.HOUSE.COMMUNITY_ID.eq(Tables.COMMUNITY.ID))
                                .and(Tables.COMMUNITY.GAMEVERSION_ID.eq(gameVersion.getId())),
                                Tables.HOUSE.ID, Tables.HOUSE.ADDRESS)
                        .setInitialValue(playerRound.getHouseId(), UInteger.valueOf(0))
                        .setLabel("House address"))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.SATISFACTION)
                        .setRequired()
                        .setInitialValue(playerRound.getSatisfaction(), UInteger.valueOf(0))
                        .setLabel("Satisfaction")
                        .setMin(0)
                        .setMax(100))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.SAVING)
                        .setRequired()
                        .setInitialValue(playerRound.getSaving(), UInteger.valueOf(0))
                        .setLabel("Savings")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.MORTGAGE)
                        .setRequired()
                        .setInitialValue(playerRound.getMortgage(), UInteger.valueOf(0))
                        .setLabel("Mortgage")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.LIVING_COSTS)
                        .setRequired()
                        .setInitialValue(playerRound.getLivingCosts(), welfareType.getLivingCosts())
                        .setLabel("Living costs")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.INCOME)
                        .setRequired()
                        .setInitialValue(playerRound.getIncome(), welfareType.getIncome())
                        .setLabel("Income")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.DEBT)
                        .setRequired()
                        .setInitialValue(playerRound.getDebt(), UInteger.valueOf(0))
                        .setLabel("Debt")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.PLAYERROUND.CURRENT_WEALTH)
                        .setRequired()
                        .setInitialValue(playerRound.getCurrentWealth(), 0)
                        .setLabel("Current wealth"))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.PREFERRED_HOUSE_RATING)
                        .setRequired()
                        .setInitialValue(playerRound.getPreferredHouseRating(), welfareType.getPreferredHouseRating())
                        .setLabel("Pref Rating")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.SATISFACTION_COST_PER_POINT)
                        .setRequired()
                        .setInitialValue(playerRound.getSatisfactionCostPerPoint(), welfareType.getSatisfactionCostPerPoint())
                        .setLabel("Sat.Cost/Point")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.HOUSE_PRICE_SOLD)
                        .setRequired()
                        .setInitialValue(playerRound.getHousePriceSold(), UInteger.valueOf(0))
                        .setLabel("House Price Sold")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.HOUSE_PRICE_BOUGHT)
                        .setRequired()
                        .setInitialValue(playerRound.getHousePriceBought(), UInteger.valueOf(0))
                        .setLabel("Houce Price Bought")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.SPENT_SAVINGS_FOR_BUYING_HOUSE)
                        .setRequired()
                        .setInitialValue(playerRound.getSpentSavingsForBuyingHouse(), UInteger.valueOf(0))
                        .setLabel("Savings for house")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.PAID_OFF_DEBT)
                        .setRequired()
                        .setInitialValue(playerRound.getPaidOffDebt(), UInteger.valueOf(0))
                        .setLabel("Paid off debt")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.MEASURE_BOUGHT)
                        .setRequired()
                        .setInitialValue(playerRound.getMeasureBought(), UInteger.valueOf(0))
                        .setLabel("Measures bought")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.PLUVIAL_DAMAGE)
                        .setRequired()
                        .setInitialValue(playerRound.getPluvialDamage(), UInteger.valueOf(0))
                        .setLabel("Pluvial damage")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.FLUVIAL_DAMAGE)
                        .setRequired()
                        .setInitialValue(playerRound.getFluvialDamage(), UInteger.valueOf(0))
                        .setLabel("Fluvial damage")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.REPAIRED_DAMAGE)
                        .setRequired()
                        .setInitialValue(playerRound.getRepairedDamage(), UInteger.valueOf(0))
                        .setLabel("Repaired damage")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.SATISFACTION_POINT_BOUGHT)
                        .setRequired()
                        .setInitialValue(playerRound.getSatisfactionPointBought(), UInteger.valueOf(0))
                        .setLabel("Sat.point bought")
                        .setMin(0))
                .addEntry(new TableEntryText(Tables.PLAYERROUND.MOVING_REASON)
                        .setInitialValue(playerRound.getMovingReason(), "")
                        .setLabel("Moving reason")
                        .setRows(3))
                .addEntry(new TableEntryUInt(Tables.PLAYERROUND.GROUPROUND_ID)
                        .setInitialValue(groupRoundId, UInteger.valueOf(0))
                        .setLabel("GroupRound id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit PlayerRound", form);
    }

    /*
     * *********************************************************************************************************
     * ********************************************* MEASURE ***************************************************
     * *********************************************************************************************************
     */

    public static void showPlayMeasure(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("PlayGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        showGroupRoundColumn(data, "PlayGroupRound", 2, data.getColumn(2).getSelectedRecordId());
        data.showDependentColumn("PlayPlayer", 3, data.getColumn(3).getSelectedRecordId(), false, Tables.PLAYER,
                Tables.PLAYER.CODE, "code", Tables.PLAYER.GROUP_ID, false, 1);
        showPlayerRoundColumn(data, "PlayPlayerRound", 4, data.getColumn(4).getSelectedRecordId());
        data.getColumn(5).setHeader("Measure");
        showMeasureColumn(data, "PlayMeasure", 5, recordId);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editPlayMeasure(session, data, recordId, editRecord);
        }
    }

    public static void editPlayMeasure(final HttpSession session, final AdminData data, final int measureId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        MeasureRecord measure = measureId == 0 ? dslContext.newRecord(Tables.MEASURE)
                : dslContext.selectFrom(Tables.MEASURE).where(Tables.MEASURE.ID.eq(UInteger.valueOf(measureId))).fetchOne();
        UInteger playerRoundId =
                measureId == 0 ? UInteger.valueOf(data.getColumn(4).getSelectedRecordId()) : measure.getPlayerroundId();
        GamesessionRecord gameSession =
                SqlUtils.readRecordFromId(data, Tables.GAMESESSION, data.getColumn(0).getSelectedRecordId());
        GameversionRecord gameVersion = SqlUtils.readRecordFromId(data, Tables.GAMEVERSION, gameSession.getGameversionId());

        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("play", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editPlayMeasure")
                .setSaveMethod("savePlayMeasure")
                .setDeleteMethod("deletePlayMeasure", "Delete", "<br>Note: Adjust the finances of the round "
                        + "<br>when a measure for a player is deleted")
                .setRecordNr(measureId)
                .startForm()
                .addEntry(new TableEntryPickRecordUInt(Tables.MEASURE.MEASURETYPE_ID)
                        .setRequired()
                        .setPickTable(data, Tables.MEASURETYPE.where
                                (Tables.MEASURETYPE.GAMEVERSION_ID.eq(gameVersion.getId())),
                                Tables.MEASURETYPE.ID, Tables.MEASURETYPE.NAME)
                        .setInitialValue(measure.getMeasuretypeId(), null)
                        .setLabel("Measuretype"))
                .addEntry(new TableEntryDouble(Tables.MEASURE.VALUE)
                        .setRequired()
                        .setInitialValue(measure.getValue(), 0.0)
                        .setLabel("Value")
                        .setMin(0.0))
                .addEntry(new TableEntryUInt(Tables.MEASURE.PLAYERROUND_ID)
                        .setInitialValue(playerRoundId, UInteger.valueOf(0))
                        .setLabel("PlayerRound id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Measure", form);
    }

    /*
     * *********************************************************************************************************
     * ********************************************* QUESTION **************************************************
     * *********************************************************************************************************
     */

    public static void showPlayQuestion(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("PlayGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        showGroupRoundColumn(data, "PlayGroupRound", 2, data.getColumn(2).getSelectedRecordId());
        data.showDependentColumn("PlayPlayer", 3, data.getColumn(3).getSelectedRecordId(), false, Tables.PLAYER,
                Tables.PLAYER.CODE, "code", Tables.PLAYER.GROUP_ID, false, 1);
        showPlayerRoundColumn(data, "PlayPlayerRound", 4, data.getColumn(4).getSelectedRecordId());
        data.getColumn(5).setHeader("Question");
        showQuestionScoreColumn(data, "PlayQuestion", 5, recordId);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editPlayQuestion(session, data, recordId, editRecord);
        }
    }

    public static void editPlayQuestion(final HttpSession session, final AdminData data, final int questionScoreId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        QuestionscoreRecord questionScore =
                questionScoreId == 0 ? dslContext.newRecord(Tables.QUESTIONSCORE) : dslContext.selectFrom(Tables.QUESTIONSCORE)
                        .where(Tables.QUESTIONSCORE.ID.eq(UInteger.valueOf(questionScoreId))).fetchOne();
        UInteger playerRoundId = questionScoreId == 0 ? UInteger.valueOf(data.getColumn(4).getSelectedRecordId())
                : questionScore.getPlayerroundId();
        PlayerroundRecord playerRound = SqlUtils.readRecordFromId(data, Tables.PLAYERROUND, playerRoundId);
        GrouproundRecord groupRound = SqlUtils.readRecordFromId(data, Tables.GROUPROUND, playerRound.getGrouproundId());
        GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, groupRound.getGroupId());
        ScenarioRecord scenario = SqlUtils.readRecordFromId(data, Tables.SCENARIO, group.getScenarioId());

        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("play", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editPlayQuestion")
                .setSaveMethod("savePlayQuestion")
                .setDeleteMethod("deletePlayQuestion", "Delete", "<br>Note: Adjust the finances of the round "
                        + "<br>when a measure for a player is deleted")
                .setRecordNr(questionScoreId)
                .startForm()
                .addEntry(new TableEntryPickRecordUInt(Tables.QUESTIONSCORE.QUESTION_ID)
                        .setRequired()
                        .setPickTable(data, Tables.QUESTION.where
                                (Tables.QUESTION.SCENARIO_ID.eq(scenario.getId())),
                                Tables.QUESTION.ID, Tables.QUESTION.NAME)
                        .setInitialValue(questionScore.getQuestionId(), null)
                        .setLabel("Question"))
                .addEntry(new TableEntryInt(Tables.QUESTIONSCORE.SCORE)
                        .setRequired()
                        .setInitialValue(questionScore.getScore(), 0)
                        .setLabel("Score")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.QUESTIONSCORE.PLAYERROUND_ID)
                        .setInitialValue(playerRoundId, UInteger.valueOf(0))
                        .setLabel("PlayerRound id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Question", form);
    }

    /*
     * *********************************************************************************************************
     * ************************************************ BID ****************************************************
     * *********************************************************************************************************
     */

    public static void showPlayBid(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("PlayGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        showGroupRoundColumn(data, "PlayGroupRound", 2, data.getColumn(2).getSelectedRecordId());
        showBidColumn(data, "PlayBid", 3, recordId);

        data.getColumn(3).setHeader("Bid");
        data.resetColumn(4);
        data.getColumn(4).setHeader("");
        data.resetColumn(5);
        data.getColumn(5).setHeader("");
        data.resetFormColumn();

        if (recordId != 0)
        {
            editPlayBid(session, data, recordId, editRecord);
        }
    }

    public static void editPlayBid(final HttpSession session, final AdminData data, final int bidId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        BidRecord bid = bidId == 0 ? dslContext.newRecord(Tables.BID)
                : dslContext.selectFrom(Tables.BID).where(Tables.BID.ID.eq(UInteger.valueOf(bidId))).fetchOne();
        UInteger groupRoundId = bidId == 0 ? UInteger.valueOf(data.getColumn(2).getSelectedRecordId()) : bid.getGrouproundId();
        GrouproundRecord groupRound = SqlUtils.readRecordFromId(data, Tables.GROUPROUND, groupRoundId);
        GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, groupRound.getGroupId());
        GamesessionRecord gameSession = SqlUtils.readRecordFromId(data, Tables.GAMESESSION, group.getGamesessionId());
        GameversionRecord gameVersion = SqlUtils.readRecordFromId(data, Tables.GAMEVERSION, gameSession.getGameversionId());

        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("play", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editPlayBid")
                .setSaveMethod("savePlayBid")
                .setDeleteMethod("deletePlayBid", "Delete", "<br>Note: you can remove a bid, but player "
                        + "<br>finances are not automatically adapted")
                .setRecordNr(bidId)
                .startForm()
                .addEntry(new TableEntryPickRecordUInt(Tables.BID.HOUSE_ID)
                .setRequired()
                        .setPickTable(data, Tables.HOUSE.join(Tables.COMMUNITY)
                                .on(Tables.HOUSE.COMMUNITY_ID.eq(Tables.COMMUNITY.ID))
                                .and(Tables.COMMUNITY.GAMEVERSION_ID.eq(gameVersion.getId())),
                                Tables.HOUSE.ID, Tables.HOUSE.ADDRESS)
                        .setInitialValue(bid.getHouseId(), UInteger.valueOf(0))
                        .setLabel("House address"))
                .addEntry(new TableEntryInt(Tables.BID.PRICE)
                        .setRequired()
                        .setInitialValue(bid.getPrice(), 0)
                        .setLabel("Bid price")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.BID.GROUPROUND_ID)
                        .setInitialValue(groupRoundId, UInteger.valueOf(0))
                        .setLabel("GroupRound id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Bid", form);
    }

    /*
     * *********************************************************************************************************
     * ****************************************** SUPPORT METHODS **********************************************
     * *********************************************************************************************************
     */

    public static void showGroupRoundColumn(final AdminData data, final String columnName, final int columnNr,
            final int recordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<GrouproundRecord> records = dslContext.selectFrom(Tables.GROUPROUND)
                .where(Tables.GROUPROUND.GROUP_ID.eq(UInteger.valueOf(data.getColumn(columnNr - 1).getSelectedRecordId())))
                .fetch();
        SortedMap<Integer, GrouproundRecord> sorted = new TreeMap<>();
        for (GrouproundRecord record : records)
        {
            RoundRecord round = dslContext.selectFrom(Tables.ROUND.where(Tables.ROUND.ID.eq(record.getRoundId()))).fetchOne();
            sorted.put(round.getRoundNumber().intValue(), record);
        }

        s.append(AdminTable.startTable());
        for (int roundNr : sorted.keySet())
        {
            GrouproundRecord record = sorted.get(roundNr);
            TableRow tableRow =
                    new TableRow(IdProvider.getId(record), recordId, "Round " + Integer.toString(roundNr), "view" + columnName);
            tableRow.addButton("Edit", "edit" + columnName);
            s.append(tableRow.process());
        }
        s.append(AdminTable.endTable());
        s.append(AdminTable.finalButton("New GroupRound", "new" + columnName));

        if (recordId != 0)
        {
            s.append(AdminTable.finalButton("Players", "viewPlayPlayer"));
            s.append(AdminTable.finalButton("Bids", "viewPlayBid"));
        }

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
                .where(Tables.PLAYERROUND.GROUPROUND_ID.eq(UInteger.valueOf(data.getColumn(2).getSelectedRecordId()))
                        .and(Tables.PLAYERROUND.PLAYER_ID.eq(UInteger.valueOf(data.getColumn(3).getSelectedRecordId()))))
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
        if (records.size() == 0)
            s.append(AdminTable.finalButton("New PlayerRound", "new" + columnName));
        else if (recordId != 0)
        {
            s.append(AdminTable.finalButton("Measures", "viewPlayMeasure"));
            s.append(AdminTable.finalButton("Questions", "viewPlayQuestion"));
        }

        data.getColumn(columnNr).setSelectedRecordId(recordId);
        data.getColumn(columnNr).setContent(s.toString());
    }

    public static void showMeasureColumn(final AdminData data, final String columnName, final int columnNr, final int recordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<MeasureRecord> records = dslContext.selectFrom(Tables.MEASURE)
                .where(Tables.MEASURE.PLAYERROUND_ID.eq(UInteger.valueOf(data.getColumn(columnNr - 1).getSelectedRecordId())))
                .fetch();

        s.append(AdminTable.startTable());
        for (MeasureRecord record : records)
        {
            MeasuretypeRecord measureType = dslContext
                    .selectFrom(Tables.MEASURETYPE.where(Tables.MEASURETYPE.ID.eq(record.getMeasuretypeId()))).fetchOne();
            TableRow tableRow = new TableRow(IdProvider.getId(record), recordId, measureType.getName(), "view" + columnName);
            tableRow.addButton("Edit", "edit" + columnName);
            s.append(tableRow.process());
        }
        s.append(AdminTable.endTable());
        s.append(AdminTable.finalButton("New Measure", "new" + columnName));

        data.getColumn(columnNr).setSelectedRecordId(recordId);
        data.getColumn(columnNr).setContent(s.toString());
    }

    public static void showQuestionScoreColumn(final AdminData data, final String columnName, final int columnNr,
            final int recordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<QuestionscoreRecord> records = dslContext.selectFrom(Tables.QUESTIONSCORE).where(
                Tables.QUESTIONSCORE.PLAYERROUND_ID.eq(UInteger.valueOf(data.getColumn(columnNr - 1).getSelectedRecordId())))
                .fetch();

        s.append(AdminTable.startTable());
        for (QuestionscoreRecord record : records)
        {
            QuestionRecord question =
                    dslContext.selectFrom(Tables.QUESTION.where(Tables.QUESTION.ID.eq(record.getQuestionId()))).fetchOne();
            TableRow tableRow = new TableRow(IdProvider.getId(record), recordId, question.getName(), "view" + columnName);
            tableRow.addButton("Edit", "edit" + columnName);
            s.append(tableRow.process());
        }
        s.append(AdminTable.endTable());
        s.append(AdminTable.finalButton("New Q-Score", "new" + columnName));

        data.getColumn(columnNr).setSelectedRecordId(recordId);
        data.getColumn(columnNr).setContent(s.toString());
    }

    public static void showBidColumn(final AdminData data, final String columnName, final int columnNr, final int recordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<BidRecord> records = dslContext.selectFrom(Tables.BID)
                .where(Tables.BID.GROUPROUND_ID.eq(UInteger.valueOf(data.getColumn(columnNr - 1).getSelectedRecordId())))
                .fetch();

        s.append(AdminTable.startTable());
        for (BidRecord bid : records)
        {
            HouseRecord house = SqlUtils.readRecordFromId(data, Tables.HOUSE, bid.getHouseId());
            TableRow tableRow =
                    new TableRow(IdProvider.getId(bid), recordId, "House " + house.getAddress(), "view" + columnName);
            tableRow.addButton("Edit", "edit" + columnName);
            s.append(tableRow.process());
        }
        s.append(AdminTable.endTable());
        s.append(AdminTable.finalButton("New Bid", "new" + columnName));

        data.getColumn(columnNr).setSelectedRecordId(recordId);
        data.getColumn(columnNr).setContent(s.toString());
    }

}
