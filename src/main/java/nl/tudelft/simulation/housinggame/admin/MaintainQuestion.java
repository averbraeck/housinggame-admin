package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.admin.form.table.FormEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.FormEntryText;
import nl.tudelft.simulation.housinggame.admin.form.table.FormEntryUInt;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.QuestionRecord;

public class MaintainQuestion
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("question"))
        {
            data.clearColumns("15%", "GameVersion", "15%", "Scenario", "15%", "Question");
            data.clearFormColumn("55%", "Edit Properties");
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

        else if (click.contains("Question"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.QUESTION, "question");
            else if (click.startsWith("delete"))
            {
                QuestionRecord question = SqlUtils.readRecordFromId(data, Tables.QUESTION, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(question, "question");
                else
                    data.deleteRecord(question, "Question", String.valueOf(question.getName()), "deleteQuestionOk", "question");
                recordId = 0;
            }
            if (!data.isError())
            {
                showQuestion(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editQuestion(session, data, 0, true);
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
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("Question", 2, 0, false, Tables.QUESTION, Tables.QUESTION.NAME, "name",
                    Tables.QUESTION.SCENARIO_ID, true);
        }
    }

    /*
     * *********************************************************************************************************
     * ***************************************** QUESTION **********************************************
     * *********************************************************************************************************
     */

    public static void showQuestion(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("QuestionGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("QuestionScenario", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.SCENARIO,
                Tables.SCENARIO.NAME, "name", Tables.SCENARIO.GAMEVERSION_ID, false);
        data.showDependentColumn("Question", 2, 0, false, Tables.QUESTION, Tables.QUESTION.NAME, "name",
                Tables.QUESTION.SCENARIO_ID, true);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editQuestion(session, data, recordId, editRecord);
        }
    }

    public static void editQuestion(final HttpSession session, final AdminData data, final int questionId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        QuestionRecord question = questionId == 0 ? dslContext.newRecord(Tables.QUESTION)
                : dslContext.selectFrom(Tables.QUESTION).where(Tables.QUESTION.ID.eq(UInteger.valueOf(questionId))).fetchOne();
        UInteger scenarioId =
                questionId == 0 ? UInteger.valueOf(data.getColumn(1).getSelectedRecordId()) : question.getScenarioId();
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
                .addEntry(new FormEntryUInt(Tables.QUESTION.QUESTION_NUMBER)
                        .setRequired()
                        .setInitialValue(question.getQuestionNumber(), UInteger.valueOf(1))
                        .setLabel("Question number"))
                .addEntry(new FormEntryString(Tables.QUESTION.NAME)
                        .setRequired()
                        .setInitialValue(question.getName(), "")
                        .setLabel("Name")
                        .setMaxChars(45))
                .addEntry(new FormEntryText(Tables.QUESTION.DESCRIPTION)
                        .setRequired()
                        .setInitialValue(question.getDescription(), "")
                        .setLabel("Description"))
                .addEntry(new FormEntryUInt(Tables.QUESTION.SCENARIO_ID)
                        .setInitialValue(scenarioId, UInteger.valueOf(0))
                        .setLabel("Scenario id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Question", form);
    }

}
