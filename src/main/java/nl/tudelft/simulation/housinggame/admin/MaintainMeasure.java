package nl.tudelft.simulation.housinggame.admin;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryDouble;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickRecord;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryText;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.GamesessionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GrouproundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HouseRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HouseroundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasureRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasuretypeRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioRecord;

public class MaintainMeasure
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("measure"))
        {
            data.clearColumns("12%", "GameSession", "12%", "Group", "12%", "GroupRound", "12%", "HouseRound", "12%", "Measure");
            data.clearFormColumn("40%", "Edit Properties");
            showGameSession(session, data, 0);
        }

        else if (click.contains("MeasureGameSession"))
        {
            showGameSession(session, data, recordId);
        }

        else if (click.endsWith("MeasureGroup"))
        {
            showGroup(session, data, recordId);
        }

        else if (click.contains("MeasureGroupRound"))
        {
            showGroupRound(session, data, recordId);
        }

        else if (click.contains("HouseRound"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.HOUSEROUND, "measure");
            else if (click.startsWith("delete"))
            {
                HouseroundRecord houseRound = SqlUtils.readRecordFromId(data, Tables.HOUSEROUND, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(houseRound, "measure");
                else
                {
                    GrouproundRecord groupRound =
                            SqlUtils.readRecordFromId(data, Tables.GROUPROUND, houseRound.getGrouproundId());
                    HouseRecord house = SqlUtils.readRecordFromId(data, Tables.HOUSE, houseRound.getHouseId());
                    data.askDeleteRecord(houseRound, "HouseRound",
                            "House " + house.getCode() + ", round " + groupRound.getRoundNumber(), "deleteHouseRoundOk",
                            "measure");
                }
                recordId = 0;
            }
            if (!data.isError())
            {
                showHouseRound(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editHouseRound(session, data, 0, true);
            }
        }

        else if (click.contains("Measure"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.MEASURE, "measure");
            else if (click.startsWith("delete"))
            {
                MeasureRecord measure = SqlUtils.readRecordFromId(data, Tables.MEASURE, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(measure, "measure");
                else
                {
                    HouseroundRecord houseRound = SqlUtils.readRecordFromId(data, Tables.HOUSEROUND, measure.getHouseroundId());
                    HouseRecord house = SqlUtils.readRecordFromId(data, Tables.HOUSE, houseRound.getHouseId());
                    MeasuretypeRecord measureType =
                            SqlUtils.readRecordFromId(data, Tables.MEASURETYPE, measure.getMeasuretypeId());
                    GrouproundRecord groupRound =
                            SqlUtils.readRecordFromId(data, Tables.GROUPROUND, houseRound.getGrouproundId());
                    data.askDeleteRecord(measure, "Measure", "Measure " + measureType.getName() + ", house " + house.getCode()
                            + ", round " + groupRound.getRoundNumber(), "deleteMeasureOk", "measure");
                }
                recordId = 0;
            }
            if (!data.isError())
            {
                showMeasure(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editMeasure(session, data, 0, true);
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
        data.showColumn("MeasureGameSession", 0, recordId, false, Tables.GAMESESSION, Tables.GAMESESSION.NAME, "name", false);
        data.resetColumn(1);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("MeasureGroup", 1, 0, false, Tables.GROUP, Tables.GROUP.NAME, "name",
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
        data.showColumn("MeasureGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("MeasureGroup", 1, recordId, false, Tables.GROUP, Tables.GROUP.NAME, "name",
                Tables.GROUP.GAMESESSION_ID, false);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("MeasureGroupRound", 2, 0, false, Tables.GROUPROUND, Tables.GROUPROUND.ROUND_NUMBER,
                    "round_number", Tables.GROUPROUND.GROUP_ID, false);
        }
    }

    /*
     * *********************************************************************************************************
     * ******************************************** GROUPROUND *************************************************
     * *********************************************************************************************************
     */

    public static void showGroupRound(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("MeasureGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("MeasureGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("MeasureGroupRound", 2, recordId, false, Tables.GROUPROUND, Tables.GROUPROUND.ROUND_NUMBER,
                "round_number", Tables.GROUPROUND.GROUP_ID, false);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetFormColumn();
        if (recordId != 0)
        {
            showHouseRoundColumn(data, "HouseRound", 3, 0);
        }
    }

    /*
     * *********************************************************************************************************
     * ******************************************** HOUSEROUND *************************************************
     * *********************************************************************************************************
     */

    public static void showHouseRound(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("MeasureGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("MeasureGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("MeasureGroupRound", 2, data.getColumn(2).getSelectedRecordId(), false, Tables.GROUPROUND,
                Tables.GROUPROUND.ROUND_NUMBER, "round_number", Tables.GROUPROUND.GROUP_ID, false);
        showHouseRoundColumn(data, "HouseRound", 3, recordId);
        data.resetColumn(4);
        data.resetFormColumn();
        if (recordId != 0)
        {
            showMeasureColumn(data, "Measure", 4, 0);
            editHouseRound(session, data, recordId, editRecord);
        }
    }

    public static void editHouseRound(final HttpSession session, final AdminData data, final int houseRoundId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        HouseroundRecord houseRound = houseRoundId == 0 ? dslContext.newRecord(Tables.HOUSEROUND)
                : dslContext.selectFrom(Tables.HOUSEROUND).where(Tables.HOUSEROUND.ID.eq(houseRoundId)).fetchOne();
        int groupRoundId = houseRoundId == 0 ? data.getColumn(2).getSelectedRecordId() : houseRound.getGrouproundId();
        GrouproundRecord groupRound = SqlUtils.readRecordFromId(data, Tables.GROUPROUND, groupRoundId);
        GamesessionRecord gameSession =
                SqlUtils.readRecordFromId(data, Tables.GAMESESSION, data.getColumn(0).getSelectedRecordId());

        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("measure", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editHouseRound")
                .setSaveMethod("saveHouseRound")
                .setDeleteMethod("deleteHouseRound", "Delete", "<br>Note: HouseRound can only be deleted when it "
                        + "<br>has not been used for measures by a player")
                .setRecordNr(houseRoundId)
                .startForm()
                .addEntry(new TableEntryPickRecord(Tables.HOUSEROUND.HOUSE_ID).setPickTable(data,
                        dslContext.fetch("SELECT * from HOUSE INNER JOIN community ON house.community_id = community.id "
                                + "WHERE community.gameversion_id = " + gameSession.getGameversionId()
                                + " AND house.available_round <= " + groupRound.getRoundNumber()),
                                Tables.HOUSE.ID, Tables.HOUSE.CODE)
                        .setInitialValue(houseRound.getHouseId(), 0)
                        .setLabel("House"))
                .addEntry(new TableEntryInt(Tables.HOUSEROUND.HOUSE_PRICE_BOUGHT)
                        .setRequired()
                        .setInitialValue(houseRound.getHousePriceBought(), 0)
                        .setLabel("Undamaged price")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.HOUSEROUND.DAMAGE_REDUCTION)
                        .setRequired()
                        .setInitialValue(houseRound.getDamageReduction(), 0)
                        .setLabel("Damage reducction")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.HOUSEROUND.HOUSE_SATISFACTION)
                        .setRequired()
                        .setInitialValue(houseRound.getHouseSatisfaction(), 0)
                        .setLabel("House satisfaction")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.HOUSEROUND.BID_PRICE)
                        .setInitialValue(houseRound.getBidPrice(), null)
                        .setLabel("Bid price")
                        .setMin(0))
                .addEntry(new TableEntryText(Tables.HOUSEROUND.BID_EXPLANATION)
                        .setInitialValue(houseRound.getBidExplanation(), "")
                        .setLabel("Bid explanation")
                        .setRows(3))
                .addEntry(new TableEntryInt(Tables.HOUSEROUND.GROUPROUND_ID)
                        .setInitialValue(groupRoundId, 0)
                        .setLabel("GroupRound id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit HouseRound", form);
    }

    /*
     * *********************************************************************************************************
     * ********************************************** MEASURE **************************************************
     * *********************************************************************************************************
     */

    public static void showMeasure(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("MeasureGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("MeasureGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("MeasureGroupRound", 2, data.getColumn(2).getSelectedRecordId(), false, Tables.GROUPROUND,
                Tables.GROUPROUND.ROUND_NUMBER, "round_number", Tables.GROUPROUND.GROUP_ID, false);
        showHouseRoundColumn(data, "HouseRound", 3, data.getColumn(3).getSelectedRecordId());
        showMeasureColumn(data, "Measure", 4, recordId);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editMeasure(session, data, recordId, editRecord);
        }
    }

    public static void editMeasure(final HttpSession session, final AdminData data, final int measureId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        MeasureRecord measure = measureId == 0 ? dslContext.newRecord(Tables.MEASURE)
                : dslContext.selectFrom(Tables.MEASURE).where(Tables.MEASURE.ID.eq(measureId)).fetchOne();
        int houseRoundId = measureId == 0 ? data.getColumn(4).getSelectedRecordId() : measure.getPlayerroundId();
        HouseroundRecord houseRound = SqlUtils.readRecordFromId(data, Tables.HOUSEROUND, houseRoundId);
        GrouproundRecord groupRound = SqlUtils.readRecordFromId(data, Tables.GROUPROUND, houseRound.getGrouproundId());
        GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, groupRound.getGroupId());
        ScenarioRecord scenario = SqlUtils.readRecordFromId(data, Tables.SCENARIO, group.getScenarioId());

        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("measure", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editMeasure")
                .setSaveMethod("saveMeasure")
                .setDeleteMethod("deleteMeasure", "Delete", "<br>Note: Adjust the finances of the round "
                        + "<br>when a measure for a player is deleted")
                .setRecordNr(measureId)
                .startForm()
                .addEntry(new TableEntryPickRecord(Tables.MEASURE.MEASURETYPE_ID)
                        .setPickTable(data, Tables.MEASURE.join(Tables.MEASURETYPE)
                                .on(Tables.MEASURE.MEASURETYPE_ID.eq(Tables.MEASURETYPE.ID))
                                .and(Tables.MEASURE.HOUSEROUND_ID.eq(houseRoundId)),
                                Tables.MEASURE.ID, Tables.MEASURETYPE.NAME)
                        .setRequired()
                        .setInitialValue(measure.getMeasuretypeId(), 0)
                        .setLabel("Measure type"))
                .addEntry(new TableEntryPickRecord(Tables.MEASURE.PLAYERROUND_ID)
                        .setPickTable(data, Tables.MEASURE.join(Tables.PLAYERROUND)
                                .on(Tables.MEASURE.PLAYERROUND_ID.eq(Tables.PLAYERROUND.ID))
                                .and(Tables.MEASURE.HOUSEROUND_ID.eq(houseRoundId))
                                .join(Tables.PLAYER).on(Tables.PLAYERROUND.PLAYER_ID.eq(Tables.PLAYER.ID)),
                                Tables.MEASURE.ID, Tables.PLAYER.CODE)
                        .setRequired()
                        .setInitialValue(measure.getMeasuretypeId(), 0)
                        .setLabel("Player"))
                .addEntry(new TableEntryDouble(Tables.MEASURE.VALUE)
                        .setRequired()
                        .setInitialValue(measure.getValue(), 0.0)
                        .setLabel("Value")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.MEASURE.USED_IN_ROUND)
                        .setRequired()
                        .setInitialValue(measure.getUsedInRound(), 0)
                        .setLabel("Used in round")
                        .setMin(0)
                        .setMax(scenario.getHighestRoundNumber()))
                .addEntry(new TableEntryInt(Tables.MEASURE.HOUSEROUND_ID)
                        .setInitialValue(houseRoundId, 0)
                        .setLabel("PlayerRound id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Measure", form);
    }

    /*
     * *********************************************************************************************************
     * ****************************************** SUPPORT METHODS **********************************************
     * *********************************************************************************************************
     */

    public static void showHouseRoundColumn(final AdminData data, final String columnName, final int columnNr,
            final int recordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<HouseroundRecord> records = dslContext.selectFrom(Tables.HOUSEROUND)
                .where(Tables.HOUSEROUND.GROUPROUND_ID.eq(data.getColumn(2).getSelectedRecordId())).fetch();
        SortedMap<String, HouseroundRecord> sorted = new TreeMap<>();
        for (HouseroundRecord record : records)
        {
            HouseRecord house = dslContext.selectFrom(Tables.HOUSE.where(Tables.HOUSE.ID.eq(record.getHouseId()))).fetchOne();
            sorted.put(house.getCode(), record);
        }

        s.append(AdminTable.startTable());
        for (String code : sorted.keySet())
        {
            HouseroundRecord record = sorted.get(code);
            TableRow tableRow = new TableRow(IdProvider.getId(record), recordId, code, "view" + columnName);
            tableRow.addButton("Edit", "edit" + columnName);
            s.append(tableRow.process());
        }
        s.append(AdminTable.endTable());
        if (records.size() == 0)
            s.append(AdminTable.finalButton("New HouseRound", "new" + columnName));

        data.getColumn(columnNr).setSelectedRecordId(recordId);
        data.getColumn(columnNr).setContent(s.toString());
    }

    public static void showMeasureColumn(final AdminData data, final String columnName, final int columnNr, final int recordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<MeasureRecord> records = dslContext.selectFrom(Tables.MEASURE)
                .where(Tables.MEASURE.HOUSEROUND_ID.eq(data.getColumn(columnNr - 1).getSelectedRecordId())).fetch();

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

}
