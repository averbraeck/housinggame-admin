package nl.tudelft.simulation.housinggame.admin;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryBoolean;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.MovingreasonRecord;

public class MaintainMovingReason
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("movingreason"))
        {
            data.clearColumns("20%", "GameVersion", "20%", "MovingReason");
            data.clearFormColumn("60%", "Edit Properties");
            showGameVersion(session, data, 0);
        }

        else if (click.contains("MovingReasonGameVersion"))
        {
            showGameVersion(session, data, recordId);
        }

        else if (click.contains("MovingReason"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.MOVINGREASON, "movingreason");
            else if (click.startsWith("delete"))
            {
                MovingreasonRecord movingReason = AdminUtils.readRecordFromId(data, Tables.MOVINGREASON, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(movingReason, "movingreason");
                else
                    data.askDeleteRecord(movingReason, "MovingReason", String.valueOf(movingReason.getKey()),
                            "deleteMovingReasonOk", "movingreason");
                recordId = 0;
            }
            if (!data.isError())
            {
                showMovingReason(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editMovingReason(session, data, 0, true);
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
        data.showColumn("MovingReasonGameVersion", 0, recordId, false, Tables.GAMEVERSION, Tables.GAMEVERSION.NAME, "name",
                false);
        data.resetColumn(1);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("MovingReason", 1, 0, true, Tables.MOVINGREASON, Tables.MOVINGREASON.KEY, "key",
                    Tables.MOVINGREASON.GAMEVERSION_ID, true);
        }
    }

    /*
     * *********************************************************************************************************
     * ******************************************** MOVINGREASON ***********************************************
     * *********************************************************************************************************
     */

    public static void showMovingReason(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("MovingReasonGameVersion", 0, data.getColumn(0).getSelectedRecordId(), false, Tables.GAMEVERSION,
                Tables.GAMEVERSION.NAME, "name", false);
        data.showDependentColumn("MovingReason", 1, recordId, true, Tables.MOVINGREASON, Tables.MOVINGREASON.KEY, "key",
                Tables.MOVINGREASON.GAMEVERSION_ID, true);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editMovingReason(session, data, recordId, editRecord);
        }
    }

    public static void editMovingReason(final HttpSession session, final AdminData data, final int movingReasonId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        MovingreasonRecord movingReason = movingReasonId == 0 ? dslContext.newRecord(Tables.MOVINGREASON)
                : dslContext.selectFrom(Tables.MOVINGREASON).where(Tables.MOVINGREASON.ID.eq(movingReasonId)).fetchOne();
        int gameVersionId = movingReasonId == 0 ? data.getColumn(0).getSelectedRecordId() : movingReason.getGameversionId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("movingreason", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editMovingReason")
                .setSaveMethod("saveMovingReason")
                .setDeleteMethod("deleteMovingReason", "Delete", "<br>Note: MovingReason can only be deleted when it "
                        + "<br>has not been used during gameplay")
                .setRecordNr(movingReasonId)
                .startForm()
                .addEntry(new TableEntryString(Tables.MOVINGREASON.KEY)
                        .setRequired()
                        .setInitialValue(movingReason.getKey(), "")
                        .setLabel("Key")
                        .setMaxChars(24))
                .addEntry(new TableEntryInt(Tables.MOVINGREASON.SEQUENCE_NUMBER)
                        .setRequired()
                        .setInitialValue(movingReason.getSequenceNumber(), 0)
                        .setLabel("Sequence nr")
                        .setMin(0))
                .addEntry(new TableEntryString(Tables.MOVINGREASON.REASON_TEXT)
                        .setRequired()
                        .setInitialValue(movingReason.getReasonText(), "")
                        .setLabel("Reason text")
                        .setMaxChars(64))
                .addEntry(new TableEntryBoolean(Tables.MOVINGREASON.IS_OTHER)
                        .setRequired()
                        .setInitialValue(movingReason.getIsOther(), (byte) 0)
                        .setLabel("Other alternative?"))
                .addEntry(new TableEntryInt(Tables.MOVINGREASON.GAMEVERSION_ID)
                        .setInitialValue(gameVersionId, 0)
                        .setLabel("Scenario id")
                        .setHidden(true))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit MovingReason", form);
    }

}
