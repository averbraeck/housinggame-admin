package nl.tudelft.simulation.housinggame.admin;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryBoolean;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickRecord;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryText;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.GroupRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.GrouproundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.PlayerroundRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.QuestionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.QuestionscoreRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioRecord;

public class MaintainPlayQuestion
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("play-question"))
        {
            data.clearColumns("10%", "GameSession", "10%", "Group", "10%", "GroupRound", "10%", "Player", "10%", "PlayerRound",
                    "10%", "Question");
            data.clearFormColumn("40%", "Edit Properties");
            showGameSession(session, data, 0);
        }

        else if (click.contains("PlayQuestionGameSession"))
        {
            showGameSession(session, data, recordId);
        }

        else if (click.endsWith("PlayQuestionGroup"))
        {
            showGroup(session, data, recordId);
        }

        else if (click.contains("PlayQuestionGroupRound"))
        {
            showPlayGroupRound(session, data, recordId, true, !click.startsWith("view"));
        }

        else if (click.endsWith("PlayQuestionPlayer"))
        {
            showPlayer(session, data, recordId);
        }

        else if (click.contains("PlayQuestionPlayerRound"))
        {
            showPlayPlayerRound(session, data, recordId, false, !click.startsWith("view"));
        }

        else if (click.contains("PlayQuestion"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.QUESTIONSCORE, "play-question");
            else if (click.startsWith("delete"))
            {
                QuestionscoreRecord questionScore = AdminUtils.readRecordFromId(data, Tables.QUESTIONSCORE, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(questionScore, "play-question");
                else
                {
                    QuestionRecord question = AdminUtils.readRecordFromId(data, Tables.QUESTION, questionScore.getQuestionId());
                    data.askDeleteRecord(questionScore, "PlayQuestion", question.getName(), "deletePlayQuestionOk",
                            "play-question");
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
        data.showColumn("PlayQuestionGameSession", 0, recordId, false, Tables.GAMESESSION, Tables.GAMESESSION.NAME, "name",
                false);
        data.resetColumn(1);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("PlayQuestionGroup", 1, 0, false, Tables.GROUP, Tables.GROUP.NAME, "name",
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
        data.showColumn("PlayQuestionGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayQuestionGroup", 1, recordId, false, Tables.GROUP, Tables.GROUP.NAME, "name",
                Tables.GROUP.GAMESESSION_ID, false);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("PlayQuestionGroupRound", 2, 0, false, Tables.GROUPROUND, Tables.GROUPROUND.ROUND_NUMBER,
                    "round_number", Tables.GROUPROUND.GROUP_ID, false);
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
        data.showColumn("PlayQuestionGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayQuestionGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("PlayQuestionGroupRound", 2, recordId, false, Tables.GROUPROUND,
                Tables.GROUPROUND.ROUND_NUMBER, "round_number", Tables.GROUPROUND.GROUP_ID, false);
        data.resetColumn(3);
        data.resetColumn(4);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            showPlayerColumn(data, "PlayQuestionPlayer", 3, recordId);
        }
    }

    /*
     * *********************************************************************************************************
     * ********************************************** PLAYER ***************************************************
     * *********************************************************************************************************
     */

    public static void showPlayer(final HttpSession session, final AdminData data, final int recordId)
    {
        data.showColumn("PlayQuestionGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayQuestionGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("PlayQuestionGroupRound", 2, data.getColumn(2).getSelectedRecordId(), false, Tables.GROUPROUND,
                Tables.GROUPROUND.ROUND_NUMBER, "round_number", Tables.GROUPROUND.GROUP_ID, false);
        showPlayerColumn(data, "PlayQuestionPlayer", 3, recordId);
        data.resetColumn(4);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            showPlayerRoundColumn(data, "PlayQuestionPlayerRound", 4, 0);
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
        data.showColumn("PlayQuestionGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayQuestionGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("PlayQuestionGroupRound", 2, data.getColumn(2).getSelectedRecordId(), false, Tables.GROUPROUND,
                Tables.GROUPROUND.ROUND_NUMBER, "round_number", Tables.GROUPROUND.GROUP_ID, false);
        showPlayerColumn(data, "PlayQuestionPlayer", 3, data.getColumn(3).getSelectedRecordId());
        showPlayerRoundColumn(data, "PlayQuestionPlayerRound", 4, recordId);
        data.resetColumn(5);
        data.resetFormColumn();
        if (recordId != 0)
        {
            showQuestionScoreColumn(data, "PlayQuestion", 5, 0);
        }
    }

    /*
     * *********************************************************************************************************
     * ********************************************* QUESTION **************************************************
     * *********************************************************************************************************
     */

    public static void showPlayQuestion(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("PlayQuestionGameSession", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMESESSION,
                Tables.GAMESESSION.NAME, "name", false);
        data.showDependentColumn("PlayQuestionGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.GROUP,
                Tables.GROUP.NAME, "name", Tables.GROUP.GAMESESSION_ID, false);
        data.showDependentColumn("PlayQuestionGroupRound", 2, data.getColumn(2).getSelectedRecordId(), false, Tables.GROUPROUND,
                Tables.GROUPROUND.ROUND_NUMBER, "round_number", Tables.GROUPROUND.GROUP_ID, false);
        showPlayerColumn(data, "PlayQuestionPlayer", 3, data.getColumn(3).getSelectedRecordId());
        showPlayerRoundColumn(data, "PlayQuestionPlayerRound", 4, data.getColumn(4).getSelectedRecordId());
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
        QuestionscoreRecord questionScore = questionScoreId == 0 ? dslContext.newRecord(Tables.QUESTIONSCORE)
                : dslContext.selectFrom(Tables.QUESTIONSCORE).where(Tables.QUESTIONSCORE.ID.eq(questionScoreId)).fetchOne();
        int playerRoundId = questionScoreId == 0 ? data.getColumn(4).getSelectedRecordId() : questionScore.getPlayerroundId();
        PlayerroundRecord playerRound = AdminUtils.readRecordFromId(data, Tables.PLAYERROUND, playerRoundId);
        GrouproundRecord groupRound = AdminUtils.readRecordFromId(data, Tables.GROUPROUND, playerRound.getGrouproundId());
        GroupRecord group = AdminUtils.readRecordFromId(data, Tables.GROUP, groupRound.getGroupId());
        ScenarioRecord scenario = AdminUtils.readRecordFromId(data, Tables.SCENARIO, group.getScenarioId());

        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("play-question", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editPlayQuestion")
                .setSaveMethod("savePlayQuestion")
                .setDeleteMethod("deletePlayQuestion", "Delete", "<br>Note: Adjust the finances of the round "
                        + "<br>when a measure for a player is deleted")
                .setRecordNr(questionScoreId)
                .startForm()
                .addEntry(new TableEntryPickRecord(Tables.QUESTIONSCORE.QUESTION_ID)
                        .setRequired()
                        .setPickTable(data, Tables.QUESTION.where
                                (Tables.QUESTION.SCENARIO_ID.eq(scenario.getId())),
                                Tables.QUESTION.ID, Tables.QUESTION.NAME)
                        .setInitialValue(questionScore.getQuestionId(), null)
                        .setLabel("Question"))
                .addEntry(new TableEntryText(Tables.QUESTIONSCORE.ANSWER)
                        .setRequired()
                        .setInitialValue(questionScore.getAnswer(), "")
                        .setLabel("Answer")
                        .setRows(1))
                .addEntry(new TableEntryBoolean(Tables.QUESTIONSCORE.LATE_ANSWER)
                        .setRequired()
                        .setInitialValue(questionScore.getLateAnswer(), (byte) 0)
                        .setLabel("Late answer"))
                .addEntry(new TableEntryInt(Tables.QUESTIONSCORE.PLAYERROUND_ID)
                        .setInitialValue(playerRoundId, 0)
                        .setLabel("PlayerRound id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Question", form);
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
            s.append(tableRow.process());
        }
        s.append(AdminTable.endTable());

        data.getColumn(columnNr).setSelectedRecordId(recordId);
        data.getColumn(columnNr).setContent(s.toString());
    }

    public static void showQuestionScoreColumn(final AdminData data, final String columnName, final int columnNr,
            final int recordId)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        List<QuestionscoreRecord> records = dslContext.selectFrom(Tables.QUESTIONSCORE)
                .where(Tables.QUESTIONSCORE.PLAYERROUND_ID.eq(data.getColumn(columnNr - 1).getSelectedRecordId())).fetch();

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

}
