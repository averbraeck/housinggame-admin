package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryDouble;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.CommunityRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.TaxRecord;

public class MaintainCommunity
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("community"))
        {
            data.clearColumns("15%", "GameVersion", "15%", "Community", "15%", "Tax");
            data.clearFormColumn("55%", "Edit Properties");
            showGameVersion(session, data, 0);
        }

        else if (click.contains("CommunityGameVersion"))
        {
            showGameVersion(session, data, recordId);
        }

        else if (click.contains("Community"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.COMMUNITY, "community");
            else if (click.startsWith("delete"))
            {
                CommunityRecord community = SqlUtils.readRecordFromId(data, Tables.COMMUNITY, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(community, "community");
                else
                    data.askDeleteRecord(community, "Community", String.valueOf(community.getName()), "deleteCommunityOk",
                            "community");
                recordId = 0;
            }
            if (!data.isError())
            {
                showCommunity(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editCommunity(session, data, 0, true);
            }
        }

        else if (click.contains("Tax"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.TAX, "community");
            else if (click.startsWith("delete"))
            {
                TaxRecord tax = SqlUtils.readRecordFromId(data, Tables.TAX, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(tax, "community");
                else
                    data.askDeleteRecord(tax, "Tax", tax.getName(), "deleteTaxOk", "community");
                recordId = 0;
            }
            if (!data.isError())
            {
                showTax(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editTax(session, data, 0, true);
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
        data.showColumn("CommunityGameVersion", 0, recordId, false, Tables.GAMEVERSION, Tables.GAMEVERSION.NAME, "name", false);
        data.resetColumn(1);
        data.resetColumn(2);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("Community", 1, 0, true, Tables.COMMUNITY, Tables.COMMUNITY.NAME, "name",
                    Tables.COMMUNITY.GAMEVERSION_ID, true);
        }
    }

    /*
     * *********************************************************************************************************
     * ********************************************* COMMUNITY *************************************************
     * *********************************************************************************************************
     */

    public static void showCommunity(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("CommunityGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("Community", 1, recordId, true, Tables.COMMUNITY, Tables.COMMUNITY.NAME, "name",
                Tables.COMMUNITY.GAMEVERSION_ID, true);
        data.resetColumn(2);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("Tax", 2, 0, true, Tables.TAX, Tables.TAX.NAME, "name", Tables.TAX.COMMUNITY_ID, true);
            editCommunity(session, data, recordId, editRecord);
        }
    }

    public static void editCommunity(final HttpSession session, final AdminData data, final int communityId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        CommunityRecord community = communityId == 0 ? dslContext.newRecord(Tables.COMMUNITY)
                : dslContext.selectFrom(Tables.COMMUNITY).where(Tables.COMMUNITY.ID.eq(communityId)).fetchOne();
        int gameVersionId = communityId == 0 ? data.getColumn(0).getSelectedRecordId() : community.getGameversionId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("community", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editCommunity")
                .setSaveMethod("saveCommunity")
                .setDeleteMethod("deleteCommunity", "Delete", "<br>Note: Community can only be deleted when it "
                        + "<br>has not been used in a game play")
                .setRecordNr(communityId)
                .startForm()
                .addEntry(new TableEntryString(Tables.COMMUNITY.NAME)
                        .setRequired()
                        .setInitialValue(community.getName(), "")
                        .setLabel("Community name")
                        .setMaxChars(45))
                .addEntry(new TableEntryInt(Tables.COMMUNITY.CAPACITY)
                        .setRequired()
                        .setInitialValue(community.getCapacity(), 0)
                        .setLabel("Capacity")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.COMMUNITY.PROTECTION_RAIN_FLOOD)
                        .setRequired()
                        .setInitialValue(community.getProtectionRainFlood(), 0)
                        .setLabel("Protection rain flood")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.COMMUNITY.PROTECTION_RIVER_FLOOD)
                        .setRequired()
                        .setInitialValue(community.getProtectionRiverFlood(), 0)
                        .setLabel("Protection river flood")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.COMMUNITY.GAMEVERSION_ID)
                        .setInitialValue(gameVersionId, 0)
                        .setLabel("Scenario id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Community", form);
    }

    /*
     * *********************************************************************************************************
     * ************************************************* TAX ***************************************************
     * *********************************************************************************************************
     */

    public static void showTax(final HttpSession session, final AdminData data, final int recordId, final boolean editButton,
            final boolean editRecord)
    {
        data.showColumn("CommunityGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("Community", 1, data.getColumn(1).getSelectedRecordId(), true, Tables.COMMUNITY,
                Tables.COMMUNITY.NAME, "name", Tables.COMMUNITY.GAMEVERSION_ID, true);
        data.showDependentColumn("Tax", 2, recordId, true, Tables.TAX, Tables.TAX.NAME, "name", Tables.TAX.COMMUNITY_ID, true);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editTax(session, data, recordId, editRecord);
        }
    }

    public static void editTax(final HttpSession session, final AdminData data, final int taxId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        TaxRecord tax = taxId == 0 ? dslContext.newRecord(Tables.TAX)
                : dslContext.selectFrom(Tables.TAX).where(Tables.TAX.ID.eq(taxId)).fetchOne();
        int communityId = taxId == 0 ? data.getColumn(1).getSelectedRecordId() : tax.getCommunityId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("community", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editTax")
                .setSaveMethod("saveTax")
                .setDeleteMethod("deleteTax", "Delete", "<br>Note: Tax can only be deleted when it "
                        + "<br>has not been used in a community")
                .setRecordNr(taxId)
                .startForm()
                .addEntry(new TableEntryString(Tables.TAX.NAME)
                        .setRequired()
                        .setInitialValue(tax.getName(), "")
                        .setLabel("Tax name")
                        .setMaxChars(45))
                .addEntry(new TableEntryInt(Tables.TAX.MINIMUM_INHABITANTS)
                        .setRequired()
                        .setInitialValue(tax.getMinimumInhabitants(), 0)
                        .setLabel("Minimum inhabitants")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.TAX.MAXIMUM_INHABITANTS)
                        .setRequired()
                        .setInitialValue(tax.getMaximumInhabitants(), 0)
                        .setLabel("Maximum inhabitants")
                        .setMin(0))
                .addEntry(new TableEntryDouble(Tables.TAX.TAX_COST)
                        .setRequired()
                        .setInitialValue(tax.getTaxCost(), 0.0)
                        .setLabel("Tax cost")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.TAX.COMMUNITY_ID)
                        .setInitialValue(communityId, 0)
                        .setLabel("Community id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Tax", form);
    }

}
