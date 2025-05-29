package nl.tudelft.simulation.housinggame.admin;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickRecord;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryText;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.LabelRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.LanguageRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.LanguagegroupRecord;

public class MaintainLanguage
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("language"))
        {
            data.clearColumns("15%", "Language", "15%", "Language group", "15%", "Label");
            data.clearFormColumn("55%", "Edit Properties");
            showLanguage(session, data, 0, true, false);
        }

        else if (click.endsWith("Language") || click.endsWith("LanguageOk"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.LANGUAGE, "language");
            else if (click.startsWith("delete"))
            {
                LanguageRecord language = AdminUtils.readRecordFromId(data, Tables.LANGUAGE, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(language, "language");
                else
                    data.askDeleteRecord(language, "Language", language.getCode(), "deleteLanguageOk", "language");
                recordId = 0;
            }
            if (!data.isError())
            {
                showLanguage(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editLanguage(session, data, 0, true);
            }
        }

        else if (click.contains("LanguageGroup"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.LANGUAGEGROUP, "language");
            else if (click.startsWith("delete"))
            {
                LanguagegroupRecord languages = AdminUtils.readRecordFromId(data, Tables.LANGUAGEGROUP, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(languages, "language");
                else
                    data.askDeleteRecord(languages, "LanguageGroup", languages.getName(), "deleteLanguageGroupOk", "language");
                recordId = 0;
            }
            if (!data.isError())
            {
                showLanguageGroup(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editLanguageGroup(session, data, 0, true);
            }
        }

        else if (click.contains("Label"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.LABEL, "label");
            else if (click.startsWith("delete"))
            {
                LabelRecord label = AdminUtils.readRecordFromId(data, Tables.LABEL, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(label, "label");
                else
                    data.askDeleteRecord(label, "Label", label.getKey(), "deleteLabelOk", "label");
                recordId = 0;
            }
            if (!data.isError())
            {
                showLabel(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editLabel(session, data, 0, true);
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
        data.showColumn("LanguageGroup", 1, 0, false, Tables.LANGUAGEGROUP, Tables.LANGUAGEGROUP.NAME, "name", false);
        data.resetColumn(2);
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
                : dslContext.selectFrom(Tables.LANGUAGE).where(Tables.LANGUAGE.ID.eq(languageId)).fetchOne();
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
     * ******************************************** LANGUAGEGROUP **********************************************
     * *********************************************************************************************************
     */

    public static void showLanguageGroup(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("Language", 0, 0, false, Tables.LANGUAGE, Tables.LANGUAGE.CODE, "code", false);
        data.showColumn("LanguageGroup", 1, recordId, editButton, Tables.LANGUAGEGROUP, Tables.LANGUAGEGROUP.NAME, "name",
                true);
        data.resetColumn(2);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("Label", 2, 0, false, Tables.LABEL, Tables.LABEL.KEY, "key", Tables.LABEL.LANGUAGEGROUP_ID,
                    true);
            editLanguageGroup(session, data, recordId, editRecord);
        }
    }

    public static void editLanguageGroup(final HttpSession session, final AdminData data, final int languagesId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        LanguagegroupRecord languages = languagesId == 0 ? dslContext.newRecord(Tables.LANGUAGEGROUP)
                : dslContext.selectFrom(Tables.LANGUAGEGROUP).where(Tables.LANGUAGEGROUP.ID.eq(languagesId)).fetchOne();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("language", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editLanguageGroup")
                .setSaveMethod("saveLanguageGroup")
                .setDeleteMethod("deleteLanguageGroup", "Delete", "<br>Note: Do not delete language group when"
                        + "<br> it has been used in a game version")
                .setRecordNr(languagesId)
                .startForm()
                .addEntry(new TableEntryString(Tables.LANGUAGEGROUP.NAME)
                        .setRequired()
                        .setInitialValue(languages.getName(), "")
                        .setLabel("Group name")
                        .setMaxChars(45))
                .addEntry(new TableEntryPickRecord(Tables.LANGUAGEGROUP.LANGUAGE_ID1)
                        .setRequired()
                        .setPickTable(data, Tables.LANGUAGE, Tables.LANGUAGE.ID,
                                Tables.LANGUAGE.CODE)
                        .setInitialValue(languages.getLanguageId1(), 0)
                        .setLabel("Language 1"))
                .addEntry(new TableEntryPickRecord(Tables.LANGUAGEGROUP.LANGUAGE_ID2)
                        .setRequired(false)
                        .setPickTable(data, Tables.LANGUAGE, Tables.LANGUAGE.ID,
                                Tables.LANGUAGE.CODE)
                        .setInitialValue(languages.getLanguageId2(), 0)
                        .setLabel("Language 2"))
                .addEntry(new TableEntryPickRecord(Tables.LANGUAGEGROUP.LANGUAGE_ID3)
                        .setRequired(false)
                        .setPickTable(data, Tables.LANGUAGE, Tables.LANGUAGE.ID,
                                Tables.LANGUAGE.CODE)
                        .setInitialValue(languages.getLanguageId3(), 0)
                        .setLabel("Language 3"))
                .addEntry(new TableEntryPickRecord(Tables.LANGUAGEGROUP.LANGUAGE_ID4)
                        .setRequired(false)
                        .setPickTable(data, Tables.LANGUAGE, Tables.LANGUAGE.ID,
                                Tables.LANGUAGE.CODE)
                        .setInitialValue(languages.getLanguageId4(), 0)
                        .setLabel("Language 4"))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit LanguageGroup", form);
    }

    /*
     * *********************************************************************************************************
     * ************************************************ LABEL **************************************************
     * *********************************************************************************************************
     */

    public static void showLabel(final HttpSession session, final AdminData data, final int recordId, final boolean editButton,
            final boolean editRecord)
    {
        data.showColumn("Language", 0, 0, false, Tables.LANGUAGE, Tables.LANGUAGE.CODE, "code", false);
        data.showColumn("LanguageGroup", 1, data.getColumn(1).getSelectedRecordId(), false, Tables.LANGUAGEGROUP,
                Tables.LANGUAGEGROUP.NAME, "name", false);
        data.showDependentColumn("Label", 2, recordId, editButton, Tables.LABEL, Tables.LABEL.KEY, "key",
                Tables.LABEL.LANGUAGEGROUP_ID, true);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editLabel(session, data, recordId, editRecord);
        }
    }

    public static void editLabel(final HttpSession session, final AdminData data, final int labelId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        LabelRecord label = labelId == 0 ? dslContext.newRecord(Tables.LABEL)
                : dslContext.selectFrom(Tables.LABEL).where(Tables.LABEL.ID.eq(labelId)).fetchOne();
        int languagesId = labelId == 0 ? data.getColumn(1).getSelectedRecordId() : label.getLanguagegroupId();
        LanguagegroupRecord languages = AdminUtils.readRecordFromId(data, Tables.LANGUAGEGROUP, languagesId);
        String[] language = new String[4];
        language[0] = languages.getLanguageId1() != null && languages.getLanguageId1() != 0
                ? AdminUtils.readRecordFromId(data, Tables.LANGUAGE, languages.getLanguageId1()).getCode() : "EN";
        language[1] = languages.getLanguageId2() != null && languages.getLanguageId2() != 0
                ? AdminUtils.readRecordFromId(data, Tables.LANGUAGE, languages.getLanguageId2()).getCode() : "language 2";
        language[2] = languages.getLanguageId3() != null && languages.getLanguageId3() != 0
                ? AdminUtils.readRecordFromId(data, Tables.LANGUAGE, languages.getLanguageId3()).getCode() : "language 3";
        language[3] = languages.getLanguageId4() != null && languages.getLanguageId4() != 0
                ? AdminUtils.readRecordFromId(data, Tables.LANGUAGE, languages.getLanguageId4()).getCode() : "language 4";
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("label", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editLabel")
                .setSaveMethod("saveLabel")
                .setDeleteMethod("deleteLabel", "Delete", "<br>Note: Label can only be deleted when it is has not"
                        + "<br> been used, and when it has no roles, rounds, groups, params")
                .setRecordNr(labelId)
                .startForm()
                .addEntry(new TableEntryString(Tables.LABEL.KEY)
                        .setRequired()
                        .setInitialValue(label.getKey(), "")
                        .setLabel("Label key")
                        .setMaxChars(45))
                .addEntry(new TableEntryText(Tables.LABEL.VALUE1)
                        .setRequired()
                        .setInitialValue(label.getValue1(), "")
                        .setLabel("Text in " + language[0])
                        .setRows(5))
                .addEntry(new TableEntryText(Tables.LABEL.VALUE2)
                        .setInitialValue(label.getValue2(), "")
                        .setLabel("Text in " + language[1])
                        .setRows(5))
                .addEntry(new TableEntryText(Tables.LABEL.VALUE3)
                        .setInitialValue(label.getValue3(), "")
                        .setLabel("Text in " + language[2])
                        .setRows(5))
                .addEntry(new TableEntryText(Tables.LABEL.VALUE4)
                        .setInitialValue(label.getValue4(), "")
                        .setLabel("Text in " + language[3])
                        .setRows(5))
                .addEntry(new TableEntryInt(Tables.LABEL.LANGUAGEGROUP_ID)
                        .setInitialValue(languagesId, 0)
                        .setLabel("LanguageGroup id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Label", form);
    }

}
