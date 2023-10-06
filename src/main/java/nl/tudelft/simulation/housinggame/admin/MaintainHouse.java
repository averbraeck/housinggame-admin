package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.form.AdminForm;
import nl.tudelft.simulation.housinggame.admin.form.FormEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.FormEntryPickRecordUInt;
import nl.tudelft.simulation.housinggame.admin.form.FormEntryString;
import nl.tudelft.simulation.housinggame.admin.form.FormEntryText;
import nl.tudelft.simulation.housinggame.admin.form.FormEntryUInt;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.HouseRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.InitialhousemeasureRecord;

public class MaintainHouse
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("house"))
        {
            data.clearColumns("12%", "GameVersion", "12%", "Community", "12%", "House", "14%", "HouseMeasures");
            data.clearFormColumn("50%", "Edit Properties");
            showGameVersion(session, data, 0);
        }

        else if (click.contains("HouseGameVersion"))
        {
            showGameVersion(session, data, recordId);
        }

        else if (click.contains("HouseCommunity"))
        {
            showCommunity(session, data, recordId);
        }

        else if (click.endsWith("House") || click.endsWith("HouseOk"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.HOUSE, "house");
            else if (click.startsWith("delete"))
            {
                HouseRecord house = SqlUtils.readRecordFromId(data, Tables.HOUSE, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(house, "house");
                else
                    data.deleteRecord(house, "House", house.getAddress(), "deleteHouseOk", "house");
                recordId = 0;
            }
            if (!data.isError())
            {
                showHouse(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editHouse(session, data, 0, true);
            }
        }

        else if (click.contains("InitialHouseMeasure"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.INITIALHOUSEMEASURE, "house");
            else if (click.startsWith("delete"))
            {
                InitialhousemeasureRecord initialHouseMeasure =
                        SqlUtils.readRecordFromId(data, Tables.INITIALHOUSEMEASURE, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(initialHouseMeasure, "house");
                else
                    data.deleteRecord(initialHouseMeasure, "InitialHouseMeasure", initialHouseMeasure.getName(),
                            "deleteInitialHouseMeasureOk", "house");
                recordId = 0;
            }
            if (!data.isError())
            {
                showInitialHouseMeasure(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editInitialHouseMeasure(session, data, 0, true);
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
        data.showColumn("HouseGameVersion", 0, recordId, false, Tables.GAMEVERSION, Tables.GAMEVERSION.NAME, "name", false);
        data.resetColumn(1);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("HouseCommunity", 1, 0, false, Tables.COMMUNITY, Tables.COMMUNITY.NAME, "name",
                    Tables.COMMUNITY.GAMEVERSION_ID, false);
        }
    }

    /*
     * *********************************************************************************************************
     * ********************************************* COMMUNITY *************************************************
     * *********************************************************************************************************
     */

    public static void showCommunity(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("HouseGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("HouseCommunity", 1, recordId, false, Tables.COMMUNITY, Tables.COMMUNITY.NAME, "name",
                Tables.COMMUNITY.GAMEVERSION_ID, false);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("House", 2, 0, true, Tables.HOUSE, Tables.HOUSE.ADDRESS, "address", Tables.HOUSE.COMMUNITY_ID,
                    true);
        }
    }

    /*
     * *********************************************************************************************************
     * ************************************************* HOUSE ***************************************************
     * *********************************************************************************************************
     */

    public static void showHouse(final HttpSession session, final AdminData data, final int recordId, final boolean editButton,
            final boolean editRecord)
    {
        data.showColumn("HouseGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("HouseCommunity", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.COMMUNITY,
                Tables.COMMUNITY.NAME, "name", Tables.COMMUNITY.GAMEVERSION_ID, false);
        data.showDependentColumn("House", 2, recordId, true, Tables.HOUSE, Tables.HOUSE.ADDRESS, "address",
                Tables.HOUSE.COMMUNITY_ID, true);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("InitialHouseMeasure", 3, 0, true, Tables.INITIALHOUSEMEASURE,
                    Tables.INITIALHOUSEMEASURE.NAME, "name", Tables.INITIALHOUSEMEASURE.HOUSE_ID, true);
            editHouse(session, data, recordId, editRecord);
        }
    }

    public static void editHouse(final HttpSession session, final AdminData data, final int houseId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        HouseRecord house = houseId == 0 ? dslContext.newRecord(Tables.HOUSE)
                : dslContext.selectFrom(Tables.HOUSE).where(Tables.HOUSE.ID.eq(UInteger.valueOf(houseId))).fetchOne();
        UInteger communityId =
                houseId == 0 ? UInteger.valueOf(data.getColumn(1).getSelectedRecordId()) : house.getCommunityId();
        //@formatter:off
        AdminForm form = new AdminForm()
                .setEdit(edit)
                .setCancelMethod("house", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editHouse")
                .setSaveMethod("saveHouse")
                .setDeleteMethod("deleteHouse", "Delete", "<br>Note: House can only be deleted when it "
                        + "<br>has not been used in a community")
                .setRecordNr(houseId)
                .startForm()
                .addEntry(new FormEntryString(Tables.HOUSE.ADDRESS)
                        .setRequired()
                        .setInitialValue(house.getAddress(), "")
                        .setLabel("House address (eg U01)")
                        .setMaxChars(45))
                .addEntry(new FormEntryUInt(Tables.HOUSE.PRICE)
                        .setRequired()
                        .setInitialValue(house.getPrice(), UInteger.valueOf(0))
                        .setLabel("Price")
                        .setMin(0))
                .addEntry(new FormEntryUInt(Tables.HOUSE.RATING)
                        .setRequired()
                        .setInitialValue(house.getRating(), UInteger.valueOf(0))
                        .setLabel("Rating")
                        .setMin(0))
                .addEntry(new FormEntryUInt(Tables.HOUSE.AVAILABLE_ROUND)
                        .setRequired()
                        .setInitialValue(house.getAvailableRound(), UInteger.valueOf(0))
                        .setLabel("Available from round")
                        .setMin(1))
                .addEntry(new FormEntryText(Tables.HOUSE.DESCRIPTION)
                        .setRequired()
                        .setInitialValue(house.getDescription(), "")
                        .setLabel("Description"))
                .addEntry(new FormEntryInt(Tables.HOUSE.INITIAL_PLUVIAL_PROTECTION)
                        .setRequired()
                        .setInitialValue(house.getInitialPluvialProtection(), 0)
                        .setLabel("Initial pluvial protection")
                        .setMin(0))
                .addEntry(new FormEntryInt(Tables.HOUSE.INITIAL_FLUVIAL_PROTECTION)
                        .setRequired()
                        .setInitialValue(house.getInitialFluvialProtection(), 0)
                        .setLabel("Initial fluvial protection")
                        .setMin(0))
                .addEntry(new FormEntryUInt(Tables.HOUSE.COMMUNITY_ID)
                        .setInitialValue(communityId, UInteger.valueOf(0))
                        .setLabel("Community id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit House", form);
    }

    /*
     * *********************************************************************************************************
     * ***************************************** INITIALHOUSEMEASURE *******************************************
     * *********************************************************************************************************
     */

    public static void showInitialHouseMeasure(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("HouseGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("HouseCommunity", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.COMMUNITY,
                Tables.COMMUNITY.NAME, "name", Tables.COMMUNITY.GAMEVERSION_ID, false);
        data.showDependentColumn("House", 2, data.getColumn(2).getSelectedRecordId(), true, Tables.HOUSE, Tables.HOUSE.ADDRESS,
                "address", Tables.HOUSE.COMMUNITY_ID, true);
        data.showDependentColumn("InitialHouseMeasure", 3, recordId, true, Tables.INITIALHOUSEMEASURE,
                Tables.INITIALHOUSEMEASURE.NAME, "name", Tables.INITIALHOUSEMEASURE.HOUSE_ID, true);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editInitialHouseMeasure(session, data, recordId, editRecord);
        }
    }

    public static void editInitialHouseMeasure(final HttpSession session, final AdminData data, final int initialHouseMeasureId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        InitialhousemeasureRecord initialHouseMeasure = initialHouseMeasureId == 0
                ? dslContext.newRecord(Tables.INITIALHOUSEMEASURE) : dslContext.selectFrom(Tables.INITIALHOUSEMEASURE)
                        .where(Tables.INITIALHOUSEMEASURE.ID.eq(UInteger.valueOf(initialHouseMeasureId))).fetchOne();
        UInteger houseId = initialHouseMeasureId == 0 ? UInteger.valueOf(data.getColumn(2).getSelectedRecordId())
                : initialHouseMeasure.getHouseId();
        //@formatter:off
        AdminForm form = new AdminForm()
                .setEdit(edit)
                .setCancelMethod("house", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editInitialHouseMeasure")
                .setSaveMethod("saveInitialHouseMeasure")
                .setDeleteMethod("deleteInitialHouseMeasure", "Delete", "<br>Note: InitialHouseMeasure can only be deleted when it "
                        + "<br>has not been used in a house")
                .setRecordNr(initialHouseMeasureId)
                .startForm()
                .addEntry(new FormEntryString(Tables.INITIALHOUSEMEASURE.NAME)
                        .setRequired()
                        .setInitialValue(initialHouseMeasure.getName(), "")
                        .setLabel("Measure name")
                        .setMaxChars(45))
                .addEntry(new FormEntryUInt(Tables.INITIALHOUSEMEASURE.ROUND)
                        .setRequired()
                        .setInitialValue(initialHouseMeasure.getRound(), UInteger.valueOf(0))
                        .setLabel("Effective in round")
                        .setMin(1))
                .addEntry(new FormEntryPickRecordUInt(Tables.INITIALHOUSEMEASURE.MEASURETYPE_ID)
                        .setPickTable(data, Tables.MEASURETYPE, Tables.MEASURETYPE.ID, Tables.MEASURETYPE.NAME)
                        .setInitialValue(initialHouseMeasure.getMeasuretypeId(), UInteger.valueOf(0))
                        .setLabel("Measure type"))
                .addEntry(new FormEntryUInt(Tables.INITIALHOUSEMEASURE.HOUSE_ID)
                        .setInitialValue(houseId, UInteger.valueOf(0))
                        .setLabel("House id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit InitialHouseMeasure", form);
    }

}
