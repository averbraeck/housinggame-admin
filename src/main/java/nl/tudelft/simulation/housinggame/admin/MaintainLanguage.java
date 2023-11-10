package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickRecordUInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.LanguageRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.LanguagesRecord;

public class MaintainLanguage
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("language"))
        {
            data.clearColumns("25%", "Language", "25%", "Language group");
            data.clearFormColumn("50%", "Edit Properties");
            showLanguage(session, data, 0, true, false);
        }

        else if (click.endsWith("Language") || click.endsWith("LanguageOk"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.LANGUAGE, "language");
            else if (click.startsWith("delete"))
            {
                LanguageRecord language = SqlUtils.readRecordFromId(data, Tables.LANGUAGE, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(language, "language");
                else
                    data.deleteRecord(language, "Language", language.getCode(), "deleteLanguageOk", "language");
                recordId = 0;
            }
            if (!data.isError())
            {
                showLanguage(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editLanguage(session, data, 0, true);
            }
        }

        else if (click.contains("Languages"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.LANGUAGES, "language");
            else if (click.startsWith("delete"))
            {
                LanguagesRecord languages = SqlUtils.readRecordFromId(data, Tables.LANGUAGES, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(languages, "language");
                else
                    data.deleteRecord(languages, "Languages", languages.getName(), "deleteLanguagesOk", "language");
                recordId = 0;
            }
            if (!data.isError())
            {
                showLanguages(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editLanguages(session, data, 0, true);
            }
        }

        AdminServlet.makeColumnContent(data);
    }

    /*
     * *********************************************************************************************************
     * ********************************************** LANGUAGE *************************************************
     * *********************************************************************************************************
     */

    public static void showLanguage(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("Language", 0, recordId, editButton, Tables.LANGUAGE, Tables.LANGUAGE.CODE, "code", true);
        data.showColumn("Languages", 1, 0, false, Tables.LANGUAGES, Tables.LANGUAGES.NAME, "name", false);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editLanguage(session, data, recordId, editRecord);
        }
    }

    public static void editLanguage(final HttpSession session, final AdminData data, final int languageId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        LanguageRecord language = languageId == 0 ? dslContext.newRecord(Tables.LANGUAGE)
                : dslContext.selectFrom(Tables.LANGUAGE).where(Tables.LANGUAGE.ID.eq(UInteger.valueOf(languageId))).fetchOne();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("language", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editLanguage")
                .setSaveMethod("saveLanguage")
                .setDeleteMethod("deleteLanguage", "Delete", "<br>Note: Do not delete scenario language when"
                        + "<br> language has been used in scenario or game version")
                .setRecordNr(languageId)
                .startForm()
                .addEntry(new TableEntryString(Tables.LANGUAGE.CODE)
                        .setRequired()
                        .setInitialValue(language.getCode(), "")
                        .setLabel("Language code (ISO2)")
                        .setMaxChars(2))
                .addEntry(new TableEntryString(Tables.LANGUAGE.NAME)
                        .setRequired()
                        .setInitialValue(language.getName(), "")
                        .setLabel("Language name")
                        .setMaxChars(45))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Language", form);
    }

    /*
     * *********************************************************************************************************
     * ********************************************** LANGUAGES ************************************************
     * *********************************************************************************************************
     */

    public static void showLanguages(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("Language", 0, 0, false, Tables.LANGUAGE, Tables.LANGUAGE.CODE, "code", false);
        data.showColumn("Languages", 1, recordId, editButton, Tables.LANGUAGES, Tables.LANGUAGES.NAME, "name", true);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editLanguages(session, data, recordId, editRecord);
        }
    }

    public static void editLanguages(final HttpSession session, final AdminData data, final int languagesId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        LanguagesRecord languages = languagesId == 0 ? dslContext.newRecord(Tables.LANGUAGES)
                : dslContext.selectFrom(Tables.LANGUAGES).where(Tables.LANGUAGES.ID.eq(UInteger.valueOf(languagesId))).fetchOne();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("language", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editLanguages")
                .setSaveMethod("saveLanguages")
                .setDeleteMethod("deleteLanguages", "Delete", "<br>Note: Do not delete language group when"
                        + "<br> it has been used in a game version")
                .setRecordNr(languagesId)
                .startForm()
                .addEntry(new TableEntryString(Tables.LANGUAGES.NAME)
                        .setRequired()
                        .setInitialValue(languages.getName(), "")
                        .setLabel("Language code (ISO2)")
                        .setMaxChars(45))
                .addEntry(new TableEntryPickRecordUInt(Tables.LANGUAGES.LANGUAGE_ID1)
                        .setRequired()
                        .setPickTable(data, Tables.LANGUAGE, Tables.LANGUAGE.ID,
                                Tables.LANGUAGE.CODE)
                        .setInitialValue(languages.getLanguageId1(), UInteger.valueOf(0))
                        .setLabel("Language 1"))
                .addEntry(new TableEntryPickRecordUInt(Tables.LANGUAGES.LANGUAGE_ID2)
                        .setRequired(false)
                        .setPickTable(data, Tables.LANGUAGE, Tables.LANGUAGE.ID,
                                Tables.LANGUAGE.CODE)
                        .setInitialValue(languages.getLanguageId2(), UInteger.valueOf(0))
                        .setLabel("Language 2"))
                .addEntry(new TableEntryPickRecordUInt(Tables.LANGUAGES.LANGUAGE_ID3)
                        .setRequired(false)
                        .setPickTable(data, Tables.LANGUAGE, Tables.LANGUAGE.ID,
                                Tables.LANGUAGE.CODE)
                        .setInitialValue(languages.getLanguageId3(), UInteger.valueOf(0))
                        .setLabel("Language 3"))
                .addEntry(new TableEntryPickRecordUInt(Tables.LANGUAGES.LANGUAGE_ID4)
                        .setRequired(false)
                        .setPickTable(data, Tables.LANGUAGE, Tables.LANGUAGE.ID,
                                Tables.LANGUAGE.CODE)
                        .setInitialValue(languages.getLanguageId4(), UInteger.valueOf(0))
                        .setLabel("Language 4"))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Language", form);
    }

}
