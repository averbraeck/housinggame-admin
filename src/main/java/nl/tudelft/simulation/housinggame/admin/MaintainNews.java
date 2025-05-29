package nl.tudelft.simulation.housinggame.admin;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryBoolean;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryDouble;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickRecord;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryText;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.NewseffectsRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.NewsitemRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioRecord;

public class MaintainNews
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("news"))
        {
            data.clearColumns("15%", "GameVersion", "15%", "Scenario", "15%", "NewsItem", "15%", "NewsEffects");
            data.clearFormColumn("40%", "Edit Properties");
            showGameVersion(session, data, 0);
        }

        else if (click.contains("NewsGameVersion"))
        {
            showGameVersion(session, data, recordId);
        }

        else if (click.contains("NewsScenario"))
        {
            showScenario(session, data, recordId);
        }

        else if (click.contains("NewsItem"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.NEWSITEM, "news");
            else if (click.startsWith("delete"))
            {
                NewsitemRecord newsItem = AdminUtils.readRecordFromId(data, Tables.NEWSITEM, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(newsItem, "news");
                else
                    data.askDeleteRecord(newsItem, "NewsItem", newsItem.getName(), "deleteNewsItemOk", "news");
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
                recordId = data.saveRecord(request, recordId, Tables.NEWSEFFECTS, "news");
            else if (click.startsWith("delete"))
            {
                NewseffectsRecord newsParameters = AdminUtils.readRecordFromId(data, Tables.NEWSEFFECTS, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(newsParameters, "news");
                else
                    data.askDeleteRecord(newsParameters, "NewsEffects", newsParameters.getName(), "deleteNewsEffectsOk",
                            "news");
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
        data.showColumn("NewsGameVersion", 0, recordId, false, Tables.GAMEVERSION, Tables.GAMEVERSION.NAME, "name", false);
        data.resetColumn(1);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("NewsScenario", 1, 0, false, Tables.SCENARIO, Tables.SCENARIO.NAME, "name",
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
        data.showColumn("NewsGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("NewsScenario", 1, recordId, false, Tables.SCENARIO, Tables.SCENARIO.NAME, "name",
                Tables.SCENARIO.GAMEVERSION_ID, false);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("NewsItem", 2, 0, true, Tables.NEWSITEM, Tables.NEWSITEM.NAME, "name",
                    Tables.NEWSITEM.SCENARIO_ID, true);
        }
    }

    /*
     * *********************************************************************************************************
     * *********************************************** NEWSITEM ************************************************
     * *********************************************************************************************************
     */

    public static void showNewsItem(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("NewsGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("NewsScenario", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.SCENARIO,
                Tables.SCENARIO.NAME, "name", Tables.SCENARIO.GAMEVERSION_ID, false);
        data.showDependentColumn("NewsItem", 2, recordId, true, Tables.NEWSITEM, Tables.NEWSITEM.NAME, "name",
                Tables.NEWSITEM.SCENARIO_ID, true);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("NewsEffects", 3, 0, true, Tables.NEWSEFFECTS, Tables.NEWSEFFECTS.NAME, "name",
                    Tables.NEWSEFFECTS.NEWSITEM_ID, true);
            editNewsItem(session, data, recordId, editRecord);
        }
    }

    public static void editNewsItem(final HttpSession session, final AdminData data, final int newsItemId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        NewsitemRecord newsItem = newsItemId == 0 ? dslContext.newRecord(Tables.NEWSITEM)
                : dslContext.selectFrom(Tables.NEWSITEM).where(Tables.NEWSITEM.ID.eq(newsItemId)).fetchOne();
        int scenarioId = newsItemId == 0 ? data.getColumn(1).getSelectedRecordId() : newsItem.getScenarioId();
        ScenarioRecord scenario = AdminUtils.readRecordFromId(data, Tables.SCENARIO, scenarioId);
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("news", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editNewsItem")
                .setSaveMethod("saveNewsItem")
                .setDeleteMethod("deleteNewsItem", "Delete", "<br>Note: NewsItem can only be deleted when it "
                        + "<br>has not been used.")
                .setRecordNr(newsItemId)
                .startForm()
                .addEntry(new TableEntryString(Tables.NEWSITEM.NAME)
                        .setRequired()
                        .setInitialValue(newsItem.getName(), "")
                        .setLabel("NewsItem name")
                        .setMaxChars(255))
                .addEntry(new TableEntryInt(Tables.NEWSITEM.ROUND_NUMBER)
                        .setRequired()
                        .setInitialValue(newsItem.getRoundNumber(), 0)
                        .setLabel("Round number")
                        .setMin(0)
                        .setMax(scenario.getHighestRoundNumber()))
                .addEntry(new TableEntryText(Tables.NEWSITEM.SUMMARY)
                        .setRequired()
                        .setInitialValue(newsItem.getSummary(), "")
                        .setLabel("News Summary"))
                .addEntry(new TableEntryText(Tables.NEWSITEM.CONTENT)
                        .setRequired()
                        .setInitialValue(newsItem.getContent(), "")
                        .setLabel("News Content"))
                .addEntry(new TableEntryInt(Tables.NEWSITEM.SCENARIO_ID)
                        .setInitialValue(scenarioId, 0)
                        .setLabel("Scenario id")
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
        data.showColumn("NewsGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("NewsScenario", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.SCENARIO,
                Tables.SCENARIO.NAME, "name", Tables.SCENARIO.GAMEVERSION_ID, false);
        data.showDependentColumn("NewsItem", 2, data.getColumn(2).getSelectedRecordId(), true, Tables.NEWSITEM,
                Tables.NEWSITEM.NAME, "name", Tables.NEWSITEM.SCENARIO_ID, true);
        data.showDependentColumn("NewsEffects", 3, recordId, true, Tables.NEWSEFFECTS, Tables.NEWSEFFECTS.NAME, "name",
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
        NewseffectsRecord newsEffects = newsEffectsId == 0 ? dslContext.newRecord(Tables.NEWSEFFECTS)
                : dslContext.selectFrom(Tables.NEWSEFFECTS).where(Tables.NEWSEFFECTS.ID.eq(newsEffectsId)).fetchOne();
        int newsItemId = newsEffectsId == 0 ? data.getColumn(2).getSelectedRecordId() : newsEffects.getNewsitemId();
        int gameVersionId = data.getColumn(0).getSelectedRecordId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("news", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editNewsEffects")
                .setSaveMethod("saveNewsEffects")
                .setDeleteMethod("deleteNewsEffects", "Delete", "<br>Note: NewsEffects can only be deleted when it "
                        + "<br>has not been used in a newsitem/round")
                .setRecordNr(newsEffectsId)
                .startForm()
                .addEntry(new TableEntryString(Tables.NEWSEFFECTS.NAME)
                        .setRequired()
                        .setInitialValue(newsEffects.getName(), "")
                        .setLabel("Effects name")
                        .setMaxChars(45))
                .addEntry(new TableEntryPickRecord(Tables.NEWSEFFECTS.COMMUNITY_ID)
                        .setRequired(false)
                        .setPickTable(data, Tables.COMMUNITY.where(Tables.COMMUNITY.GAMEVERSION_ID.eq(gameVersionId)),
                                Tables.COMMUNITY.ID, Tables.COMMUNITY.NAME)
                        .setInitialValue(newsEffects.getCommunityId(), 0)
                        .setLabel("Affected community (blank = all)"))
                .addEntry(new TableEntryInt(Tables.NEWSEFFECTS.HOUSE_DISCOUNT_ROUND1)
                        .setRequired()
                        .setInitialValue(newsEffects.getHouseDiscountRound1(), 0)
                        .setLabel("Discount Round 1")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.NEWSEFFECTS.HOUSE_DISCOUNT_ROUND2)
                        .setRequired()
                        .setInitialValue(newsEffects.getHouseDiscountRound2(), 0)
                        .setLabel("Discount Round 2")
                        .setMin(0))
                .addEntry(new TableEntryInt(Tables.NEWSEFFECTS.HOUSE_DISCOUNT_ROUND3)
                        .setRequired()
                        .setInitialValue(newsEffects.getHouseDiscountRound3(), 0)
                        .setLabel("Discount Round 3")
                        .setMin(0))
                .addEntry(new TableEntryBoolean(Tables.NEWSEFFECTS.HOUSE_DISCOUNT_EFFECT_FLUVIAL)
                        .setRequired()
                        .setInitialValue(newsEffects.getHouseDiscountEffectFluvial(), (byte) 1)
                        .setLabel("Discount effect fluvial"))
                .addEntry(new TableEntryBoolean(Tables.NEWSEFFECTS.HOUSE_DISCOUNT_EFFECT_PLUVIAL)
                        .setRequired()
                        .setInitialValue(newsEffects.getHouseDiscountEffectPluvial(), (byte) 0)
                        .setLabel("Discount effect pluvial"))
                .addEntry(new TableEntryBoolean(Tables.NEWSEFFECTS.HOUSE_DISCOUNT_COMMUNITY)
                        .setRequired()
                        .setInitialValue(newsEffects.getHouseDiscountCommunity(), (byte) 1)
                        .setLabel("Discount for community?"))
                .addEntry(new TableEntryInt(Tables.NEWSEFFECTS.FLUVIAL_PROTECTION_CHANGE)
                        .setRequired()
                        .setInitialValue(newsEffects.getFluvialProtectionChange(), 0)
                        .setLabel("Fluvial prot change"))
                .addEntry(new TableEntryInt(Tables.NEWSEFFECTS.PLUVIAL_PROTECTION_CHANGE)
                        .setRequired()
                        .setInitialValue(newsEffects.getPluvialProtectionChange(), 0)
                        .setLabel("Pluvial prot change"))
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
                .addEntry(new TableEntryInt(Tables.NEWSEFFECTS.NEWSITEM_ID)
                        .setInitialValue(newsItemId, 0)
                        .setLabel("Newsitem id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit NewsEffects", form);
    }

}
