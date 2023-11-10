package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickRecordUInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryText;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryUInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.GameversionRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.LabelRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.LanguagesRecord;

public class MaintainLabel
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("label"))
        {
            data.clearColumns("25%", "GameVersion", "25%", "Label");
            data.clearFormColumn("50%", "Edit Properties");
            showGameVersion(session, data, 0, true, false);
        }

        else if (click.contains("LabelGameVersion"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.GAMEVERSION, "label");
            else if (click.startsWith("delete"))
            {
                GameversionRecord gameVersion = SqlUtils.readRecordFromId(data, Tables.GAMEVERSION, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(gameVersion, "label");
                else
                    data.deleteRecord(gameVersion, "GameVersion", gameVersion.getName(), "deleteLabelGameVersionOk", "label");
                recordId = 0;
            }
            if (!data.isError())
            {
                showGameVersion(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editGameVersion(session, data, 0, true);
            }
        }

        else if (click.contains("Label"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.LABEL, "label");
            else if (click.startsWith("delete"))
            {
                LabelRecord label = SqlUtils.readRecordFromId(data, Tables.LABEL, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(label, "label");
                else
                    data.deleteRecord(label, "Label", label.getKey(), "deleteLabelOk", "label");
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
     * ****************************************** GAMEVERSION **************************************************
     * *********************************************************************************************************
     */

    public static void showGameVersion(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("LabelGameVersion", 0, recordId, editButton, Tables.GAMEVERSION, Tables.GAMEVERSION.NAME, "name", true);
        data.resetColumn(1);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("Label", 1, 0, false, Tables.LABEL, Tables.LABEL.KEY, "key", Tables.LABEL.GAMEVERSION_ID,
                    true);
            editGameVersion(session, data, recordId, editRecord);
        }
    }

    public static void editGameVersion(final HttpSession session, final AdminData data, final int gameVersionId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        GameversionRecord gameVersion = gameVersionId == 0 ? dslContext.newRecord(Tables.GAMEVERSION) : dslContext
                .selectFrom(Tables.GAMEVERSION).where(Tables.GAMEVERSION.ID.eq(UInteger.valueOf(gameVersionId))).fetchOne();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("label", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editLabelGameVersion")
                .setSaveMethod("saveLabelGameVersion")
                .setDeleteMethod("deleteLabelGameVersion", "Delete", "<br>Note: Game Version can only be deleted when it is has"
                        + "<br> not been used, and when it has no associated labels or scenarios")
                .setRecordNr(gameVersionId)
                .startForm()
                .addEntry(new TableEntryString(Tables.GAMEVERSION.NAME)
                        .setRequired()
                        .setInitialValue(gameVersion.getName(), "")
                        .setLabel("Game Version name")
                        .setMaxChars(255))
                .addEntry(new TableEntryPickRecordUInt(Tables.GAMEVERSION.LANGUAGES_ID)
                        .setRequired()
                        .setPickTable(data, Tables.LANGUAGES, Tables.LANGUAGES.ID,
                                Tables.LANGUAGES.NAME)
                        .setInitialValue(gameVersion.getLanguagesId(), UInteger.valueOf(0))
                        .setLabel("Game languages"))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Game Version", form);
    }

    /*
     * *********************************************************************************************************
     * ************************************************ LABEL **************************************************
     * *********************************************************************************************************
     */

    public static void showLabel(final HttpSession session, final AdminData data, final int recordId, final boolean editButton,
            final boolean editRecord)
    {
        data.showColumn("GameVersion", 0, data.getColumn(0).getSelectedRecordId(), editButton, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", true);
        data.showDependentColumn("Label", 1, recordId, editButton, Tables.LABEL, Tables.LABEL.KEY, "key",
                Tables.LABEL.GAMEVERSION_ID, true);
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
                : dslContext.selectFrom(Tables.LABEL).where(Tables.LABEL.ID.eq(UInteger.valueOf(labelId))).fetchOne();
        UInteger gameVersionId =
                labelId == 0 ? UInteger.valueOf(data.getColumn(0).getSelectedRecordId()) : label.getGameversionId();
        GameversionRecord gameVersion = SqlUtils.readRecordFromId(data, Tables.GAMEVERSION, gameVersionId);
        LanguagesRecord languages = SqlUtils.readRecordFromId(data, Tables.LANGUAGES, gameVersion.getLanguagesId());
        String[] language = new String[4];
        language[0] = languages.getLanguageId1() != null && languages.getLanguageId1().intValue() != 0
                ? SqlUtils.readRecordFromId(data, Tables.LANGUAGE, languages.getLanguageId1()).getCode() : "EN";
        language[1] = languages.getLanguageId2() != null && languages.getLanguageId2().intValue() != 0
                ? SqlUtils.readRecordFromId(data, Tables.LANGUAGE, languages.getLanguageId2()).getCode() : "language 2";
        language[2] = languages.getLanguageId3() != null && languages.getLanguageId3().intValue() != 0
                ? SqlUtils.readRecordFromId(data, Tables.LANGUAGE, languages.getLanguageId3()).getCode() : "language 3";
        language[3] = languages.getLanguageId4() != null && languages.getLanguageId4().intValue() != 0
                ? SqlUtils.readRecordFromId(data, Tables.LANGUAGE, languages.getLanguageId4()).getCode() : "language 4";
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
                .addEntry(new TableEntryUInt(Tables.LABEL.GAMEVERSION_ID)
                        .setInitialValue(gameVersionId, UInteger.valueOf(0))
                        .setLabel("GameVersion id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Label", form);
    }

}
