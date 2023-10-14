package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryBoolean;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryDouble;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickRecordUInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryText;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryUInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.NewseffectsRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.NewsitemRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.RoundRecord;

public class MaintainRound
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("round"))
        {
            data.clearColumns("12%", "GameVersion", "12%", "Scenario", "12%", "Round", "12%", "NewsItem", "12%", "NewsEffects");
            data.clearFormColumn("40%", "Edit Properties");
            showGameVersion(session, data, 0);
        }

        else if (click.contains("RoundGameVersion"))
        {
            showGameVersion(session, data, recordId);
        }

        else if (click.contains("RoundScenario"))
        {
            showScenario(session, data, recordId);
        }

        else if (click.contains("Round"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.ROUND, "round");
            else if (click.startsWith("delete"))
            {
                RoundRecord round = SqlUtils.readRecordFromId(data, Tables.ROUND, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(round, "round");
                else
                    data.deleteRecord(round, "Round", String.valueOf(round.getRoundNumber()), "deleteRoundOk", "round");
                recordId = 0;
            }
            if (!data.isError())
            {
                showRound(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editRound(session, data, 0, true);
            }
        }

        else if (click.contains("NewsItem"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.NEWSITEM, "round");
            else if (click.startsWith("delete"))
            {
                NewsitemRecord newsItem = SqlUtils.readRecordFromId(data, Tables.NEWSITEM, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(newsItem, "round");
                else
                    data.deleteRecord(newsItem, "NewsItem", newsItem.getName(), "deleteNewsItemOk", "round");
                recordId = 0;
            }
            if (!data.isError())
            {
                showNewsItem(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editNewsItem(session, data, 0, true);
            }
        }

        else if (click.contains("NewsEffects"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.NEWSEFFECTS, "round");
            else if (click.startsWith("delete"))
            {
                NewseffectsRecord newsParameters = SqlUtils.readRecordFromId(data, Tables.NEWSEFFECTS, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(newsParameters, "round");
                else
                    data.deleteRecord(newsParameters, "NewsEffects", newsParameters.getName(), "deleteNewsEffectsOk", "round");
                recordId = 0;
            }
            if (!data.isError())
            {
                showNewsEffects(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editNewsEffects(session, data, 0, true);
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
        data.showColumn("RoundGameVersion", 0, recordId, false, Tables.GAMEVERSION, Tables.GAMEVERSION.NAME, "name", false);
        data.resetColumn(1);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("RoundScenario", 1, 0, false, Tables.SCENARIO, Tables.SCENARIO.NAME, "name",
                    Tables.SCENARIO.GAMEVERSION_ID, false);
        }
    }

    /*
     * *********************************************************************************************************
     * ********************************************* SCENARIO **************************************************
     * *********************************************************************************************************
     */

    public static void showScenario(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("RoundGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("RoundScenario", 1, recordId, false, Tables.SCENARIO, Tables.SCENARIO.NAME, "name",
                Tables.SCENARIO.GAMEVERSION_ID, false);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("Round", 2, 0, true, Tables.ROUND, Tables.ROUND.ROUND_NUMBER, "round_number",
                    Tables.ROUND.SCENARIO_ID, true);
        }
    }

    /*
     * *********************************************************************************************************
     * ***************************************** ROUND **********************************************
     * *********************************************************************************************************
     */

    public static void showRound(final HttpSession session, final AdminData data, final int recordId, final boolean editButton,
            final boolean editRecord)
    {
        data.showColumn("RoundGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("RoundScenario", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.SCENARIO,
                Tables.SCENARIO.NAME, "name", Tables.SCENARIO.GAMEVERSION_ID, false);
        data.showDependentColumn("Round", 2, recordId, true, Tables.ROUND, Tables.ROUND.ROUND_NUMBER, "round_number",
                Tables.ROUND.SCENARIO_ID, true);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("NewsItem", 3, 0, true, Tables.NEWSITEM, Tables.NEWSITEM.NAME, "name",
                    Tables.NEWSITEM.ROUND_ID, true);
            editRound(session, data, recordId, editRecord);
        }
    }

    public static void editRound(final HttpSession session, final AdminData data, final int roundId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        RoundRecord round = roundId == 0 ? dslContext.newRecord(Tables.ROUND)
                : dslContext.selectFrom(Tables.ROUND).where(Tables.ROUND.ID.eq(UInteger.valueOf(roundId))).fetchOne();
        UInteger scenarioId = roundId == 0 ? UInteger.valueOf(data.getColumn(1).getSelectedRecordId()) : round.getScenarioId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("round", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editRound")
                .setSaveMethod("saveRound")
                .setDeleteMethod("deleteRound", "Delete", "<br>Note: Round can only be deleted when it "
                        + "<br>has not been used in a game play")
                .setRecordNr(roundId)
                .startForm()
                .addEntry(new TableEntryInt(Tables.ROUND.ROUND_NUMBER)
                        .setRequired()
                        .setInitialValue(round.getRoundNumber(), 1)
                        .setLabel("Round number")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.ROUND.SCENARIO_ID)
                        .setInitialValue(scenarioId, UInteger.valueOf(0))
                        .setLabel("Scenario id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Round", form);
    }

    /*
     * *********************************************************************************************************
     * *********************************************** NEWSITEM ************************************************
     * *********************************************************************************************************
     */

    public static void showNewsItem(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("RoundGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("RoundScenario", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.SCENARIO,
                Tables.SCENARIO.NAME, "name", Tables.SCENARIO.GAMEVERSION_ID, false);
        data.showDependentColumn("Round", 2, data.getColumn(2).getSelectedRecordId(), true, Tables.ROUND,
                Tables.ROUND.ROUND_NUMBER, "round_number", Tables.ROUND.SCENARIO_ID, true);
        data.showDependentColumn("NewsItem", 3, recordId, true, Tables.NEWSITEM, Tables.NEWSITEM.NAME, "name",
                Tables.NEWSITEM.ROUND_ID, true);
        data.resetColumn(4);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("NewsEffects", 4, 0, true, Tables.NEWSEFFECTS, Tables.NEWSEFFECTS.NAME, "name",
                    Tables.NEWSEFFECTS.NEWSITEM_ID, true);
            editNewsItem(session, data, recordId, editRecord);
        }
    }

    public static void editNewsItem(final HttpSession session, final AdminData data, final int newsItemId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        NewsitemRecord newsItem = newsItemId == 0 ? dslContext.newRecord(Tables.NEWSITEM)
                : dslContext.selectFrom(Tables.NEWSITEM).where(Tables.NEWSITEM.ID.eq(UInteger.valueOf(newsItemId))).fetchOne();
        UInteger roundId = newsItemId == 0 ? UInteger.valueOf(data.getColumn(2).getSelectedRecordId()) : newsItem.getRoundId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("round", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editNewsItem")
                .setSaveMethod("saveNewsItem")
                .setDeleteMethod("deleteNewsItem", "Delete", "<br>Note: NewsItem can only be deleted when it "
                        + "<br>has not been used in a round")
                .setRecordNr(newsItemId)
                .startForm()
                .addEntry(new TableEntryString(Tables.NEWSITEM.NAME)
                        .setRequired()
                        .setInitialValue(newsItem.getName(), "")
                        .setLabel("NewsItem name")
                        .setMaxChars(255))
                .addEntry(new TableEntryText(Tables.NEWSITEM.SUMMARY)
                        .setRequired()
                        .setInitialValue(newsItem.getSummary(), "")
                        .setLabel("News Summary"))
                .addEntry(new TableEntryText(Tables.NEWSITEM.CONTENT)
                        .setRequired()
                        .setInitialValue(newsItem.getContent(), "")
                        .setLabel("News Content"))
                .addEntry(new TableEntryUInt(Tables.NEWSITEM.ROUND_ID)
                        .setInitialValue(roundId, UInteger.valueOf(0))
                        .setLabel("Round id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit NewsItem", form);
    }

    /*
     * *********************************************************************************************************
     * ******************************************** NEWSEFFECTS ************************************************
     * *********************************************************************************************************
     */

    public static void showNewsEffects(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("RoundGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("RoundScenario", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.SCENARIO,
                Tables.SCENARIO.NAME, "name", Tables.SCENARIO.GAMEVERSION_ID, false);
        data.showDependentColumn("Round", 2, data.getColumn(2).getSelectedRecordId(), true, Tables.ROUND,
                Tables.ROUND.ROUND_NUMBER, "round_number", Tables.ROUND.SCENARIO_ID, true);
        data.showDependentColumn("NewsItem", 3, data.getColumn(3).getSelectedRecordId(), true, Tables.NEWSITEM,
                Tables.NEWSITEM.NAME, "name", Tables.NEWSITEM.ROUND_ID, true);
        data.showDependentColumn("NewsEffects", 4, recordId, true, Tables.NEWSEFFECTS, Tables.NEWSEFFECTS.NAME, "name",
                Tables.NEWSEFFECTS.NEWSITEM_ID, true);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editNewsEffects(session, data, recordId, editRecord);
        }
    }

    public static void editNewsEffects(final HttpSession session, final AdminData data, final int newsEffectsId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        NewseffectsRecord newsEffects = newsEffectsId == 0 ? dslContext.newRecord(Tables.NEWSEFFECTS) : dslContext
                .selectFrom(Tables.NEWSEFFECTS).where(Tables.NEWSEFFECTS.ID.eq(UInteger.valueOf(newsEffectsId))).fetchOne();
        UInteger newsItemId =
                newsEffectsId == 0 ? UInteger.valueOf(data.getColumn(3).getSelectedRecordId()) : newsEffects.getNewsitemId();
        UInteger gameVersionId = UInteger.valueOf(data.getColumn(0).getSelectedRecordId());
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("round", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editNewsEffects")
                .setSaveMethod("saveNewsEffects")
                .setDeleteMethod("deleteNewsEffects", "Delete", "<br>Note: NewsEffects can only be deleted when it "
                        + "<br>has not been used in a newsitemm/round")
                .setRecordNr(newsEffectsId)
                .startForm()
                .addEntry(new TableEntryString(Tables.NEWSEFFECTS.NAME)
                        .setRequired()
                        .setInitialValue(newsEffects.getName(), "")
                        .setLabel("Effects name")
                        .setMaxChars(45))
                .addEntry(new TableEntryPickRecordUInt(Tables.NEWSEFFECTS.COMMUNITY_ID)
                        .setRequired(false)
                        .setPickTable(data, Tables.COMMUNITY.where(Tables.COMMUNITY.GAMEVERSION_ID.eq(gameVersionId)),
                                Tables.COMMUNITY.ID, Tables.COMMUNITY.NAME)
                        .setInitialValue(newsEffects.getCommunityId(), UInteger.valueOf(0))
                        .setLabel("Community (blank = all)"))
                .addEntry(new TableEntryBoolean(Tables.NEWSEFFECTS.HOUSE_DISCOUNT_EUROS)
                        .setRequired()
                        .setInitialValue(newsEffects.getHouseDiscountEuros(), (byte) 0)
                        .setLabel("Discount Euros?"))
                .addEntry(new TableEntryBoolean(Tables.NEWSEFFECTS.HOUSE_DISCOUNT_PERCENT)
                        .setRequired()
                        .setInitialValue(newsEffects.getHouseDiscountPercent(), (byte) 0)
                        .setLabel("Discount Percent?"))
                .addEntry(new TableEntryUInt(Tables.NEWSEFFECTS.HOUSE_DISCOUNT_YEAR1)
                        .setRequired()
                        .setInitialValue(newsEffects.getHouseDiscountYear1(), UInteger.valueOf(0))
                        .setLabel("Discount Year 1")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.NEWSEFFECTS.HOUSE_DISCOUNT_YEAR2)
                        .setRequired()
                        .setInitialValue(newsEffects.getHouseDiscountYear2(), UInteger.valueOf(0))
                        .setLabel("Discount Year 2")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.NEWSEFFECTS.HOUSE_DISCOUNT_YEAR3)
                        .setRequired()
                        .setInitialValue(newsEffects.getHouseDiscountYear3(), UInteger.valueOf(0))
                        .setLabel("Discount Year 3")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.NEWSEFFECTS.PLUVIAL_PROTECTION_CHANGE)
                        .setRequired()
                        .setInitialValue(newsEffects.getPluvialProtectionChange(), 0)
                        .setLabel("Pluvial prot change"))
                .addEntry(new TableEntryInt(Tables.NEWSEFFECTS.FLUVIAL_PROTECTION_CHANGE)
                        .setRequired()
                        .setInitialValue(newsEffects.getFluvialProtectionChange(), 0)
                        .setLabel("Fluvial prot change"))
                .addEntry(new TableEntryDouble(Tables.NEWSEFFECTS.TAX_CHANGE)
                        .setRequired()
                        .setInitialValue(newsEffects.getTaxChange(), 0.0)
                        .setLabel("Tax change"))
                .addEntry(new TableEntryInt(Tables.NEWSEFFECTS.SATISFACTION_LIVING_BONUS)
                        .setRequired()
                        .setInitialValue(newsEffects.getSatisfactionLivingBonus(), 0)
                        .setLabel("Sat living bonus"))
                .addEntry(new TableEntryInt(Tables.NEWSEFFECTS.SATISFACTION_MOVE_CHANGE)
                        .setRequired()
                        .setInitialValue(newsEffects.getSatisfactionMoveChange(), 0)
                        .setLabel("Sat move change"))
                .addEntry(new TableEntryUInt(Tables.NEWSEFFECTS.NEWSITEM_ID)
                        .setInitialValue(newsItemId, UInteger.valueOf(0))
                        .setLabel("Newsitem id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit NewsEffects", form);
    }

}
