package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.admin.form.table.FormEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.table.FormEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.FormEntryText;
import nl.tudelft.simulation.housinggame.admin.form.table.FormEntryUInt;
import nl.tudelft.simulation.housinggame.data.Tables;
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
            data.clearColumns("15%", "GameVersion", "15%", "Scenario", "15%", "Round", "15", "NewsItem");
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
                .addEntry(new FormEntryInt(Tables.ROUND.ROUND_NUMBER)
                        .setRequired()
                        .setInitialValue(round.getRoundNumber(), 1)
                        .setLabel("Round number")
                        .setMin(0))
                .addEntry(new FormEntryUInt(Tables.ROUND.SCENARIO_ID)
                        .setInitialValue(scenarioId, UInteger.valueOf(0))
                        .setLabel("Scenario id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Round", form);
    }

    /*
     * *********************************************************************************************************
     * ***************************************** NEWSITEM **********************************************
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
        data.resetFormColumn();
        if (recordId != 0)
        {
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
                .addEntry(new FormEntryString(Tables.NEWSITEM.NAME)
                        .setRequired()
                        .setInitialValue(newsItem.getName(), "")
                        .setLabel("NewsItem name")
                        .setMaxChars(255))
                .addEntry(new FormEntryText(Tables.NEWSITEM.CONTENT)
                        .setRequired()
                        .setInitialValue(newsItem.getContent(), "")
                        .setLabel("News Content"))
                .addEntry(new FormEntryUInt(Tables.NEWSITEM.ROUND_ID)
                        .setInitialValue(roundId, UInteger.valueOf(0))
                        .setLabel("Round id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit NewsItem", form);
    }

}
