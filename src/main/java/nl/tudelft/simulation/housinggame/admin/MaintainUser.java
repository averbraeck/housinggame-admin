package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickRecordUInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryUInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryBoolean;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.FacilitatorRecord;
import nl.tudelft.simulation.housinggame.data.tables.records.UserRecord;

public class MaintainUser
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("user"))
        {
            data.clearColumns("25%", "User", "25%", "Facilitator");
            data.clearFormColumn("50%", "Edit Properties");
            showUser(session, data, 0, true, false);
        }

        else if (click.contains("User"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.USER, "user");
            else if (click.startsWith("delete"))
            {
                UserRecord user = SqlUtils.readRecordFromId(data, Tables.USER, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(user, "user");
                else
                    data.deleteRecord(user, "User", user.getUsername(), "deleteUserOk", "user");
                recordId = 0;
            }
            if (!data.isError())
            {
                showUser(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editUser(session, data, 0, true);
            }
        }

        else if (click.contains("Facilitator"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.FACILITATOR, "user");
            else if (click.startsWith("delete"))
            {
                FacilitatorRecord facilitator = SqlUtils.readRecordFromId(data, Tables.FACILITATOR, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(facilitator, "user");
                else
                    data.deleteRecord(facilitator, "Facilitator", facilitator.getName(), "deleteFacilitatorOk", "user");
                recordId = 0;
            }
            if (!data.isError())
            {
                showFacilitator(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editFacilitator(session, data, 0, true);
            }
        }

        AdminServlet.makeColumnContent(data);
    }

    /*
     * *********************************************************************************************************
     * *********************************************** USER ****************************************************
     * *********************************************************************************************************
     */

    public static void showUser(final HttpSession session, final AdminData data, final int recordId, final boolean editButton,
            final boolean editRecord)
    {
        data.showColumn("User", 0, recordId, editButton, Tables.USER, Tables.USER.USERNAME, "username", true);
        data.resetColumn(1);
        data.resetFormColumn();
        if (recordId != 0)
        {
            data.showDependentColumn("Facilitator", 1, 0, false, Tables.FACILITATOR, Tables.FACILITATOR.NAME, "name",
                    Tables.FACILITATOR.USER_ID, true);
            editUser(session, data, recordId, editRecord);
        }
    }

    public static void editUser(final HttpSession session, final AdminData data, final int userId, final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        UserRecord user = userId == 0 ? dslContext.newRecord(Tables.USER)
                : dslContext.selectFrom(Tables.USER).where(Tables.USER.ID.eq(UInteger.valueOf(userId))).fetchOne();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("user", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editUser")
                .setSaveMethod("saveUser")
                .setDeleteMethod("deleteUser", "Delete", "<br>Note: User can only be deleted when it is has"
                        + "<br> not been used, and when it has no facilitator(s) or player(s)")
                .setRecordNr(userId)
                .startForm()
                .addEntry(new TableEntryString(Tables.USER.USERNAME)
                        .setRequired()
                        .setInitialValue(user.getUsername(), "")
                        .setLabel("User name")
                        .setMaxChars(16))
                .addEntry(new TableEntryString(Tables.USER.EMAIL)
                        .setRequired(false)
                        .setInitialValue(user.getEmail(), "")
                        .setLabel("Email")
                        .setMaxChars(255))
                .addEntry(new TableEntryString(Tables.USER.PASSWORD)
                        .setRequired()
                        .setInitialValue(user.getPassword(), "")
                        .setLabel("Password")
                        .setMaxChars(32))
                .addEntry(new TableEntryBoolean(Tables.USER.ADMINISTRATOR)
                        .setRequired()
                        .setInitialValue(user.getAdministrator(), Byte.valueOf((byte) 0))
                        .setLabel("Administrator"))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit User", form);
    }

    /*
     * *********************************************************************************************************
     * ******************************************** FACILITATOR ************************************************
     * *********************************************************************************************************
     */

    public static void showFacilitator(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("User", 0, data.getColumn(0).getSelectedRecordId(), editButton, Tables.USER, Tables.USER.USERNAME,
                "username", true);
        data.showDependentColumn("Facilitator", 1, recordId, editButton, Tables.FACILITATOR, Tables.FACILITATOR.NAME, "name",
                Tables.FACILITATOR.USER_ID, true);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editFacilitator(session, data, recordId, editRecord);
        }
    }

    public static void editFacilitator(final HttpSession session, final AdminData data, final int facilitatorId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        FacilitatorRecord facilitator = facilitatorId == 0 ? dslContext.newRecord(Tables.FACILITATOR) : dslContext
                .selectFrom(Tables.FACILITATOR).where(Tables.FACILITATOR.ID.eq(UInteger.valueOf(facilitatorId))).fetchOne();
        UInteger userId =
                facilitatorId == 0 ? UInteger.valueOf(data.getColumn(0).getSelectedRecordId()) : facilitator.getUserId();
        //@formatter:off
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("user", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editFacilitator")
                .setSaveMethod("saveFacilitator")
                .setDeleteMethod("deleteFacilitator", "Delete", "<br>Note: Facilitator can only be deleted when it is has not"
                        + "<br> been used, and when it has no linked gamesessions or groups")
                .setRecordNr(facilitatorId)
                .startForm()
                .addEntry(new TableEntryString(Tables.FACILITATOR.NAME)
                        .setRequired()
                        .setInitialValue(facilitator.getName(), "")
                        .setLabel("Facilitator name")
                        .setMaxChars(16))
                .addEntry(new TableEntryUInt(Tables.FACILITATOR.USER_ID)
                        .setInitialValue(userId, UInteger.valueOf(0))
                        .setLabel("User id")
                        .setHidden(true))
                .addEntry(new TableEntryPickRecordUInt(Tables.FACILITATOR.GAMESESSION_ID)
                        .setPickTable(data, Tables.GAMESESSION, Tables.GAMESESSION.ID, Tables.GAMESESSION.NAME)
                        .setInitialValue(facilitator.getGamesessionId(), UInteger.valueOf(0))
                        .setLabel("Gamesession"))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Facilitator", form);
    }

}
