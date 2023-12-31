package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryText;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.QuestionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.QuestionitemRecord;

public class MaintainQuestion
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("question"))
        {
            data.clearColumns("12%", "GameVersion", "12%", "Scenario", "12%", "Question", "12%", "QuestionItem");
            data.clearFormColumn("52%", "Edit Properties");
            showGameVersion(session, data, 0);
        }

        else if (click.contains("QuestionGameVersion"))
        {
            showGameVersion(session, data, recordId);
        }

        else if (click.contains("QuestionScenario"))
        {
            showScenario(session, data, recordId);
        }

        else if (click.endsWith("Question") || click.endsWith("QuestionOk"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.QUESTION, "question");
            else if (click.startsWith("delete"))
            {
                QuestionRecord question = SqlUtils.readRecordFromId(data, Tables.QUESTION, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(question, "question");
                else
                    data.askDeleteRecord(question, "Question", String.valueOf(question.getName()), "deleteQuestionOk",
                            "question");
                recordId = 0;
            }
            if (!data.isError())
            {
                showQuestion(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editQuestion(session, data, 0, true);
            }
        }

        else if (click.contains("QuestionItem"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.QUESTIONITEM, "question");
            else if (click.startsWith("delete"))
            {
                QuestionitemRecord questionItem = SqlUtils.readRecordFromId(data, Tables.QUESTIONITEM, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(questionItem, "question");
                else
                    data.askDeleteRecord(questionItem, "QuestionItem", String.valueOf(questionItem.getCode()),
                            "deleteQuestionItemOk", "question");
                recordId = 0;
            }
            if (!data.isError())
            {
                showQuestionItem(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editQuestionItem(session, data, 0, true);
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
        data.showColumn("QuestionGameVersion", 0, recordId, false, Tables.GAMEVERSION, Tables.GAMEVERSION.NAME, "name", false);
        data.resetColumn(1);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("QuestionScenario", 1, 0, false, Tables.SCENARIO, Tables.SCENARIO.NAME, "name",
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
        data.showColumn("QuestionGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("QuestionScenario", 1, recordId, false, Tables.SCENARIO, Tables.SCENARIO.NAME, "name",
                Tables.SCENARIO.GAMEVERSION_ID, false);
        data.resetColumn(2);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("Question", 2, 0, true, Tables.QUESTION, Tables.QUESTION.NAME, "name",
                    Tables.QUESTION.SCENARIO_ID, true);
        }
    }

    /*
     * *********************************************************************************************************
     * ********************************************* QUESTION **************************************************
     * *********************************************************************************************************
     */

    public static void showQuestion(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("QuestionGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("QuestionScenario", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.SCENARIO,
                Tables.SCENARIO.NAME, "name", Tables.SCENARIO.GAMEVERSION_ID, false);
        data.showDependentColumn("Question", 2, recordId, true, Tables.QUESTION, Tables.QUESTION.NAME, "name",
                Tables.QUESTION.SCENARIO_ID, true);
        data.resetColumn(3);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("QuestionItem", 3, 0, true, Tables.QUESTIONITEM, Tables.QUESTIONITEM.CODE, "code",
                    Tables.QUESTIONITEM.QUESTION_ID, true);
            editQuestion(session, data, recordId, editRecord);
        }
    }

    public static void editQuestion(final HttpSession session, final AdminData data, final int questionId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        QuestionRecord question = questionId == 0 ? dslContext.newRecord(Tables.QUESTION)
                : dslContext.selectFrom(Tables.QUESTION).where(Tables.QUESTION.ID.eq(questionId)).fetchOne();
        int scenarioId = questionId == 0 ? data.getColumn(1).getSelectedRecordId() : question.getScenarioId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("question", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editQuestion")
                .setSaveMethod("saveQuestion")
                .setDeleteMethod("deleteQuestion", "Delete", "<br>Note: Question can only be deleted when it "
                        + "<br>has not been used in a scenario")
                .setRecordNr(questionId)
                .startForm()
                .addEntry(new TableEntryInt(Tables.QUESTION.QUESTION_NUMBER)
                        .setRequired()
                        .setInitialValue(question.getQuestionNumber(), 1)
                        .setLabel("Question number"))
                .addEntry(new TableEntryString(Tables.QUESTION.TYPE)
                        .setRequired()
                        .setInitialValue(question.getType(), "")
                        .setLabel("Type (SELECT, INTEGER, STRING or TEXT)")
                        .setMaxChars(12))
                .addEntry(new TableEntryString(Tables.QUESTION.NAME)
                        .setRequired()
                        .setInitialValue(question.getName(), "")
                        .setLabel("Name")
                        .setMaxChars(80))
                .addEntry(new TableEntryText(Tables.QUESTION.DESCRIPTION)
                        .setInitialValue(question.getDescription(), "")
                        .setLabel("Description"))
                .addEntry(new TableEntryInt(Tables.QUESTION.MIN_VALUE)
                        .setInitialValue(question.getMinValue(), 1)
                        .setLabel("Minimum value (for INT)"))
                .addEntry(new TableEntryInt(Tables.QUESTION.MAX_VALUE)
                        .setInitialValue(question.getMaxValue(), 1)
                        .setLabel("Maximum value (for INT)"))
                .addEntry(new TableEntryInt(Tables.QUESTION.MAX_LENGTH)
                        .setInitialValue(question.getMaxLength(), 80)
                        .setLabel("Maximum length (for STR)"))
                .addEntry(new TableEntryInt(Tables.QUESTION.SCENARIO_ID)
                        .setInitialValue(scenarioId, 0)
                        .setLabel("Scenario id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Question", form);
    }

    /*
     * *********************************************************************************************************
     * ******************************************* QUESTIONITEM ************************************************
     * *********************************************************************************************************
     */

    public static void showQuestionItem(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("QuestionGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("QuestionScenario", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.SCENARIO,
                Tables.SCENARIO.NAME, "name", Tables.SCENARIO.GAMEVERSION_ID, false);
        data.showDependentColumn("Question", 2, data.getColumn(2).getSelectedRecordId(), true, Tables.QUESTION,
                Tables.QUESTION.NAME, "name", Tables.QUESTION.SCENARIO_ID, true);
        data.showDependentColumn("QuestionItem", 3, recordId, true, Tables.QUESTIONITEM, Tables.QUESTIONITEM.CODE, "code",
                Tables.QUESTIONITEM.QUESTION_ID, true);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editQuestionItem(session, data, recordId, editRecord);
        }
    }

    public static void editQuestionItem(final HttpSession session, final AdminData data, final int questionItemId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        QuestionitemRecord questionItem = questionItemId == 0 ? dslContext.newRecord(Tables.QUESTIONITEM)
                : dslContext.selectFrom(Tables.QUESTIONITEM).where(Tables.QUESTIONITEM.ID.eq(questionItemId)).fetchOne();
        int questionId = questionItemId == 0 ? data.getColumn(2).getSelectedRecordId() : questionItem.getQuestionId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("question", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editQuestionItem")
                .setSaveMethod("saveQuestionItem")
                .setDeleteMethod("deleteQuestionItem", "Delete", "<br>Note: QuestionItem can only be deleted when it "
                        + "<br>has not been used in a question")
                .setRecordNr(questionItemId)
                .startForm()
                .addEntry(new TableEntryString(Tables.QUESTIONITEM.CODE)
                        .setRequired()
                        .setInitialValue(questionItem.getCode(), "")
                        .setLabel("Code")
                        .setMaxChars(8))
                .addEntry(new TableEntryString(Tables.QUESTIONITEM.NAME)
                        .setRequired()
                        .setInitialValue(questionItem.getName(), "")
                        .setLabel("Name")
                        .setMaxChars(255))
                .addEntry(new TableEntryInt(Tables.QUESTIONITEM.QUESTION_ID)
                        .setInitialValue(questionId, 0)
                        .setLabel("Question id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit QuestionItem", form);
    }

}
