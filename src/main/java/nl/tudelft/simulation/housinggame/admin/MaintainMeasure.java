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
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickList;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickRecord;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.common.HouseGroupStatus;
import nl.tudelft.simulation.housinggame.common.TransactionStatus;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HouseRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HousegroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.HousetransactionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasureRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.MeasuretypeRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerroundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioRecord;

public class MaintainMeasure
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("measure"))
        {
            data.clearColumns("12%", "GameSession", "12%", "Group", "12%", "HouseGroup", "12%", "Measure", "12%",
                    "HouseTransaction");
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

        else if (click.contains("HouseGroup"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.HOUSEGROUP, "measure");
            else if (click.startsWith("delete"))
            {
                HousegroupRecord houseGroup = SqlUtils.readRecordFromId(data, Tables.HOUSEGROUP, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(houseGroup, "measure");
                else
                {
                    GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, houseGroup.getGroupId());
                    HouseRecord house = SqlUtils.readRecordFromId(data, Tables.HOUSE, houseGroup.getHouseId());
                    data.askDeleteRecord(houseGroup, "HouseGroup", "House " + house.getCode() + ", group " + group.getName(),
                            "deleteHouseGroupOk", "measure");
                }
                recordId = 0;
            }
            if (!data.isError())
            {
                showHouseGroup(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editHouseGroup(session, data, 0, true);
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
                    HousegroupRecord houseGroup = SqlUtils.readRecordFromId(data, Tables.HOUSEGROUP, measure.getHousegroupId());
                    HouseRecord house = SqlUtils.readRecordFromId(data, Tables.HOUSE, houseGroup.getHouseId());
                    MeasuretypeRecord measureType =
                            SqlUtils.readRecordFromId(data, Tables.MEASURETYPE, measure.getMeasuretypeId());
                    GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, houseGroup.getGroupId());
                    data.askDeleteRecord(measure, "Measure",
                            "Measure " + measureType.getName() + ", house " + house.getCode() + ", group " + group.getName(),
                            "deleteMeasureOk", "measure");
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

        else if (click.contains("HouseTransaction"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.HOUSETRANSACTION, "measure");
            else if (click.startsWith("delete"))
            {
                HousetransactionRecord transaction = SqlUtils.readRecordFromId(data, Tables.HOUSETRANSACTION, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(transaction, "measure");
                else
                {
                    HousegroupRecord houseGroup =
                            SqlUtils.readRecordFromId(data, Tables.HOUSEGROUP, transaction.getHousegroupId());
                    HouseRecord house = SqlUtils.readRecordFromId(data, Tables.HOUSE, houseGroup.getHouseId());
                    GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, houseGroup.getGroupId());
                    PlayerroundRecord playerRound =
                            SqlUtils.readRecordFromId(data, Tables.PLAYERROUND, transaction.getHousegroupId());
                    PlayerRecord player = SqlUtils.readRecordFromId(data, Tables.PLAYER, playerRound.getPlayerId());
                    data.askDeleteRecord(transaction, "HouseHouseTransaction", "HouseTransaction house " + house.getCode()
                            + ", group " + group.getName() + ", player " + player.getCode(), "deleteHouseTransactionOk",
                            "measure");
                }
                recordId = 0;
            }
            if (!data.isError())
            {
                showHouseTransaction(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editHouseTransaction(session, data, 0, true);
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
            showHouseGroupColumn(data, "HouseGroup", 2, 0);
        }
    }

    /*
     * *********************************************************************************************************
     * ******************************************** HOUSEGROUP *************************************************
     * *********************************************************************************************************
     */

    public static void showHouseGroup(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("MeasureGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("MeasureGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        showHouseGroupColumn(data, "HouseGroup", 2, recordId);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetFormColumn();
        if (recordId != 0)
        {
            showMeasureColumn(data, "Measure", 3, 0);
            showHouseTransactionColumn(data, "HouseTransaction", 4, 0);
            editHouseGroup(session, data, recordId, editRecord);
        }
    }

    public static void editHouseGroup(final HttpSession session, final AdminData data, final int houseGroupId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        HousegroupRecord houseGroup = houseGroupId == 0 ? dslContext.newRecord(Tables.HOUSEGROUP)
                : dslContext.selectFrom(Tables.HOUSEGROUP).where(Tables.HOUSEGROUP.ID.eq(houseGroupId)).fetchOne();
        int groupId = houseGroupId == 0 ? data.getColumn(1).getSelectedRecordId() : houseGroup.getGroupId();

        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("measure", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editHouseGroup")
                .setSaveMethod("saveHouseGroup")
                .setDeleteMethod("deleteHouseGroup", "Delete", "<br>Note: HouseGroup can only be deleted when it "
                        + "<br>has not been used for measures by a player")
                .setRecordNr(houseGroupId)
                .setLabelLength("50%")
                .setFieldLength("50%")
                .startForm()
                .addEntry(new TableEntryString(Tables.HOUSEGROUP.CODE)
                        .setRequired()
                        .setInitialValue(houseGroup.getCode(), "")
                        .setLabel("House code")
                        .setReadOnly())
                .addEntry(new TableEntryString(Tables.HOUSEGROUP.ADDRESS)
                        .setRequired()
                        .setInitialValue(houseGroup.getAddress(), "")
                        .setLabel("House address")
                        .setReadOnly())
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.RATING)
                        .setRequired()
                        .setInitialValue(houseGroup.getRating(), 0)
                        .setLabel("House rating")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.ORIGINAL_PRICE)
                        .setRequired()
                        .setInitialValue(houseGroup.getOriginalPrice(), 0)
                        .setLabel("Original price")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.DAMAGE_REDUCTION)
                        .setRequired()
                        .setInitialValue(houseGroup.getDamageReduction(), 0)
                        .setLabel("Damage reduction")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.MARKET_VALUE)
                        .setRequired()
                        .setInitialValue(houseGroup.getMarketValue(), 0)
                        .setLabel("Market value")
                        .setMin(0))
                .addEntry(new TableEntryPickRecord(Tables.HOUSEGROUP.OWNER_ID)
                        .setPickTable(data, Tables.PLAYER.join(Tables.GROUP)
                                .on(Tables.PLAYER.GROUP_ID.eq(groupId)),
                                Tables.PLAYER.ID, Tables.PLAYER.CODE)
                        .setRequired()
                        .setInitialValue(houseGroup.getOwnerId(), null) // no 'new' function, so can safely retrieve
                        .setLabel("Owner (player)"))
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.LAST_SOLD_PRICE)
                        .setRequired()
                        .setInitialValue(houseGroup.getLastSoldPrice(), 0)
                        .setLabel("Last sold price")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.HOUSE_SATISFACTION)
                        .setRequired()
                        .setInitialValue(houseGroup.getHouseSatisfaction(), 0)
                        .setLabel("House satisfaction"))
                .addEntry(new TableEntryPickList(Tables.HOUSEGROUP.STATUS)
                        .setPickListEntries(HouseGroupStatus.values())
                        .setRequired()
                        .setInitialValue(houseGroup.getStatus(), "")
                        .setLabel("Status"))
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.FLUVIAL_BASE_PROTECTION)
                        .setRequired()
                        .setInitialValue(houseGroup.getFluvialBaseProtection(), 0)
                        .setLabel("Fluvial base prot")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.PLUVIAL_BASE_PROTECTION)
                        .setRequired()
                        .setInitialValue(houseGroup.getPluvialBaseProtection(), 0)
                        .setLabel("Pluvial base prot")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.FLUVIAL_HOUSE_PROTECTION)
                        .setRequired()
                        .setInitialValue(houseGroup.getFluvialHouseProtection(), 0)
                        .setLabel("Fluvial house prot")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.PLUVIAL_HOUSE_PROTECTION)
                        .setRequired()
                        .setInitialValue(houseGroup.getPluvialHouseProtection(), 0)
                        .setLabel("Pluvial house prot")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.LAST_ROUND_COMM_FLUVIAL)
                        .setInitialValue(houseGroup.getLastRoundCommFluvial(), 0)
                        .setLabel("Last round fluvial community flood")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.LAST_ROUND_COMM_PLUVIAL)
                        .setInitialValue(houseGroup.getLastRoundCommPluvial(), 0)
                        .setLabel("Last round pluvial community flood")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.LAST_ROUND_HOUSE_FLUVIAL)
                        .setInitialValue(houseGroup.getLastRoundHouseFluvial(), 0)
                        .setLabel("Last round fluvial house flood")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.LAST_ROUND_HOUSE_PLUVIAL)
                        .setInitialValue(houseGroup.getLastRoundHousePluvial(), 0)
                        .setLabel("Last round pluvial house flood")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.GROUP_ID)
                        .setInitialValue(groupId, 0)
                        .setLabel("Group id")
                        .setHidden(true))
                .addEntry(new TableEntryInt(Tables.HOUSEGROUP.HOUSE_ID)
                        .setInitialValue(houseGroup.getHouseId(), null) // no 'new' function, so can safely retrieve
                        .setLabel("House id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit HouseGroup", form);
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
        showHouseGroupColumn(data, "HouseGroup", 2, data.getColumn(2).getSelectedRecordId());
        showMeasureColumn(data, "Measure", 3, recordId);
        showHouseTransactionColumn(data, "HouseTransaction", 4, 0);
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
        int houseGroupId = measureId == 0 ? data.getColumn(2).getSelectedRecordId() : measure.getHousegroupId();
        HousegroupRecord houseGroup = SqlUtils.readRecordFromId(data, Tables.HOUSEGROUP, houseGroupId);
        GroupRecord group = SqlUtils.readRecordFromId(data, Tables.GROUP, houseGroup.getGroupId());
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
                            .setPickTable(data, Tables.MEASURETYPE
                                .where(Tables.MEASURETYPE.GAMEVERSION_ID.eq(scenario.getGameversionId())),
                                Tables.MEASURETYPE.ID, Tables.MEASURETYPE.NAME)
                        .setRequired()
                        .setInitialValue(measure.getMeasuretypeId(), 0)
                        .setLabel("Measure type"))
                .addEntry(new TableEntryInt(Tables.MEASURE.ROUND_NUMBER)
                        .setRequired()
                        .setInitialValue(measure.getRoundNumber(), 0)
                        .setLabel("Round number")
                        .setMin(0)
                        .setMax(scenario.getHighestRoundNumber()))
                .addEntry(new TableEntryInt(Tables.MEASURE.CONSUMED_IN_ROUND)
                        .setRequired()
                        .setInitialValue(measure.getConsumedInRound(), 0)
                        .setLabel("Used in round")
                        .setMin(0)
                        .setMax(scenario.getHighestRoundNumber()))
                .addEntry(new TableEntryInt(Tables.MEASURE.HOUSEGROUP_ID)
                        .setInitialValue(houseGroupId, 0)
                        .setLabel("Housegroup id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Measure", form);
    }

    /*
     * *********************************************************************************************************
     * ***************************************** HOUSETRANSACTION **********************************************
     * *********************************************************************************************************
     */

    public static void showHouseTransaction(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("MeasureGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("MeasureGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        showHouseGroupColumn(data, "HouseGroup", 2, data.getColumn(2).getSelectedRecordId());
        showMeasureColumn(data, "Measure", 3, 0);
        showHouseTransactionColumn(data, "HouseTransaction", 4, recordId);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editHouseTransaction(session, data, recordId, editRecord);
        }
    }

    public static void editHouseTransaction(final HttpSession session, final AdminData data, final int transactionId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        HousetransactionRecord transaction = transactionId == 0 ? dslContext.newRecord(Tables.HOUSETRANSACTION)
                : dslContext.selectFrom(Tables.HOUSETRANSACTION).where(Tables.HOUSETRANSACTION.ID.eq(transactionId)).fetchOne();
        int houseGroupId = transactionId == 0 ? data.getColumn(3).getSelectedRecordId() : transaction.getHousegroupId();

        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("measure", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editHouseTransaction")
                .setSaveMethod("saveHouseTransaction")
                .setDeleteMethod("deleteHouseTransaction", "Delete", "<br>Note: Adjust the finances of the round "
                        + "<br>when a measure for a player is deleted")
                .setRecordNr(transactionId)
                .startForm()
                .addEntry(new TableEntryInt(Tables.HOUSETRANSACTION.PRICE)
                        .setRequired()
                        .setInitialValue(transaction.getPrice(), 0)
                        .setLabel("Price")
                        .setMin(0))
                .addEntry(new TableEntryString(Tables.HOUSETRANSACTION.COMMENT)
                        .setInitialValue(transaction.getComment(), "")
                        .setLabel("Comment")
                        .setMaxChars(45))
                .addEntry(new TableEntryPickList(Tables.HOUSETRANSACTION.TRANSACTION_STATUS)
                        .setPickListEntries(TransactionStatus.values())
                        .setInitialValue(transaction.getTransactionStatus(), "")
                        .setLabel("Status"))
                .addEntry(new TableEntryInt(Tables.HOUSETRANSACTION.PLAYERROUND_ID)
                        .setInitialValue(transaction.getPlayerroundId(), 0)
                        .setLabel("Playerround id")
                        .setHidden(true))
                .addEntry(new TableEntryInt(Tables.HOUSETRANSACTION.GROUPROUND_ID)
                        .setInitialValue(transaction.getGrouproundId(), 0)
                        .setLabel("Groupround id")
                        .setHidden(true))
                .addEntry(new TableEntryInt(Tables.HOUSETRANSACTION.HOUSEGROUP_ID)
                        .setInitialValue(houseGroupId, 0)
                        .setLabel("Housegroup id")
                        .setHidden(true))
                .addEntry(new TableEntryInt(Tables.HOUSETRANSACTION.ID)
                        .setInitialValue(transaction.getId(), 0)
                        .setLabel("Transaction id")
                        .setReadOnly()
                        .setNoWrite())
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit HouseTransaction", form);
    }

    /*
     * *********************************************************************************************************
     * ****************************************** SUPPORT METHODS **********************************************
     * *********************************************************************************************************
     */

    public static void showHouseGroupColumn(final AdminData data, final String columnName, final int columnNr,
            final int recordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<HousegroupRecord> records = dslContext.selectFrom(Tables.HOUSEGROUP)
                .where(Tables.HOUSEGROUP.GROUP_ID.eq(data.getColumn(1).getSelectedRecordId())).fetch();
        SortedMap<String, HousegroupRecord> sorted = new TreeMap<>();
        for (HousegroupRecord record : records)
        {
            HouseRecord house = dslContext.selectFrom(Tables.HOUSE.where(Tables.HOUSE.ID.eq(record.getHouseId()))).fetchOne();
            sorted.put(house.getCode(), record);
        }

        s.append(AdminTable.startTable());
        for (String code : sorted.keySet())
        {
            HousegroupRecord record = sorted.get(code);
            TableRow tableRow = new TableRow(IdProvider.getId(record), recordId, code, "view" + columnName);
            tableRow.addButton("Edit", "edit" + columnName);
            s.append(tableRow.process());
        }
        s.append(AdminTable.endTable());
        if (records.size() == 0)
            s.append(AdminTable.finalButton("New HouseGroup", "new" + columnName));

        data.getColumn(columnNr).setSelectedRecordId(recordId);
        data.getColumn(columnNr).setContent(s.toString());
    }

    public static void showMeasureColumn(final AdminData data, final String columnName, final int columnNr, final int recordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<MeasureRecord> records = dslContext.selectFrom(Tables.MEASURE)
                .where(Tables.MEASURE.HOUSEGROUP_ID.eq(data.getColumn(2).getSelectedRecordId())).fetch();

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

    public static void showHouseTransactionColumn(final AdminData data, final String columnName, final int columnNr,
            final int recordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<HousetransactionRecord> records = dslContext.selectFrom(Tables.HOUSETRANSACTION)
                .where(Tables.HOUSETRANSACTION.HOUSEGROUP_ID.eq(data.getColumn(2).getSelectedRecordId())).fetch();

        s.append(AdminTable.startTable());
        for (HousetransactionRecord record : records)
        {
            PlayerroundRecord playerRound = dslContext
                    .selectFrom(Tables.PLAYERROUND.where(Tables.PLAYERROUND.ID.eq(record.getPlayerroundId()))).fetchOne();
            PlayerRecord player = SqlUtils.readRecordFromId(data, Tables.PLAYER, playerRound.getPlayerId());
            TableRow tableRow = new TableRow(IdProvider.getId(record), recordId, player.getCode(), "view" + columnName);
            tableRow.addButton("Edit", "edit" + columnName);
            s.append(tableRow.process());
        }
        s.append(AdminTable.endTable());

        data.getColumn(columnNr).setSelectedRecordId(recordId);
        data.getColumn(columnNr).setContent(s.toString());
    }

}
