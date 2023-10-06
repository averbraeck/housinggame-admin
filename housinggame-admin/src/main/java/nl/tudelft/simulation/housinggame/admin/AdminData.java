package nl.tudelft.simulation.housinggame.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.column.FormColumn;
import nl.tudelft.simulation.housinggame.admin.column.TableColumn;
import nl.tudelft.simulation.housinggame.data.tables.records.UserRecord;

public class AdminData
{

    /**
     * the SQL datasource representing the database's connection pool.<br>
     * the datasource is shared among the servlets and stored as a ServletContext attribute.
     */
    private DataSource dataSource;

    /**
     * the name of the admin user logged in to this session. <br>
     * if null, no user is logged in.<br>
     * filled by the UserLoginServlet.<br>
     * used by: server and in servlet.
     */
    private String username;

    /**
     * the id of the admin user logged in to this session.<br>
     * if null, no user is logged in.<br>
     * filled by the UserLoginServlet.<br>
     * used by: server.
     */
    private Integer userId;

    /**
     * the admin User record for the logged in user.<br>
     * this record has the USERNAME to display on the screen.<br>
     * filled by the UserLoginServlet.<br>
     * used by: server and in servlet.<br>
     */
    private UserRecord user;

    private TableColumn[] tableColumns;

    private FormColumn formColumn;

    private String contentHtml = "";

    /* ================================= */
    /* FULLY DYNAMIC INFO IN THE SESSION */
    /* ================================= */

    /**
     * which menu has been chosen, to maintain persistence after a POST. <br>
     */
    private String menuChoice = "";

    /**
     * when 0, do not show popup; when 1: show popup. <br>
     * filled and updated by RoundServlet.
     */
    private int showModalWindow = 0;

    /**
     * client info (dynamic) for popup.
     */
    private String modalWindowHtml = "";

    /**
     * Error
     */
    private boolean error = false;

    /* ******************* */
    /* GETTERS AND SETTERS */
    /* ******************* */

    public DataSource getDataSource()
    {
        return this.dataSource;
    }

    public void setDataSource(final DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(final String username)
    {
        this.username = username;
    }

    public Integer getUserId()
    {
        return this.userId;
    }

    public void setUserId(final Integer userId)
    {
        this.userId = userId;
    }

    public UserRecord getUser()
    {
        return this.user;
    }

    public void setUser(final UserRecord user)
    {
        this.user = user;
    }

    public int getShowModalWindow()
    {
        return this.showModalWindow;
    }

    public void setShowModalWindow(final int showModalWindow)
    {
        this.showModalWindow = showModalWindow;
    }

    public String getMenuChoice()
    {
        return this.menuChoice;
    }

    public void setMenuChoice(final String menuChoice)
    {
        this.menuChoice = menuChoice;
    }

    public String getTopMenu()
    {
        return AdminServlet.getTopMenu(this);
    }

    public String getContentHtml()
    {
        return this.contentHtml;
    }

    public void setContentHtml(final String contentHtml)
    {
        this.contentHtml = contentHtml;
    }

    public String getModalWindowHtml()
    {
        return this.modalWindowHtml;
    }

    public void setModalWindowHtml(final String modalClientWindowHtml)
    {
        this.modalWindowHtml = modalClientWindowHtml;
    }

    public void clearColumns(final String... widthsAndHeaders)
    {
        this.tableColumns = new TableColumn[widthsAndHeaders.length / 2];
        for (int i = 0; i < this.tableColumns.length; i++)
        {
            this.tableColumns[i] = new TableColumn(widthsAndHeaders[2 * i], widthsAndHeaders[2 * i + 1]);
        }
    }

    public void clearFormColumn(final String width, final String defaultHeader)
    {
        this.formColumn = new FormColumn(width, defaultHeader);
    }

    public void resetColumn(final int nr)
    {
        this.tableColumns[nr].setHeader(this.tableColumns[nr].getDefaultHeader());
        this.tableColumns[nr].setContent("");
        this.tableColumns[nr].setSelectedRecordId(0);
    }

    public void resetFormColumn()
    {
        this.formColumn.setHeader(this.formColumn.getDefaultHeader());
        this.formColumn.setForm(null);
        this.formColumn.setHtmlContents("");
    }

    public FormColumn getFormColumn()
    {
        return this.formColumn;
    }

    public void setFormColumn(final FormColumn formColumn)
    {
        this.formColumn = formColumn;
    }

    public int getNrColumns()
    {
        return this.tableColumns.length;
    }

    public TableColumn getColumn(final int nr)
    {
        return this.tableColumns[nr];
    }

    public boolean isError()
    {
        return this.error;
    }

    public void setError(final boolean error)
    {
        this.error = error;
    }

    public <R extends org.jooq.Record, T extends Comparable<? super T>> void showColumn(final String columnName,
            final int columnNr, final int recordId, final boolean editButton, final Table<R> table, final Field<T> sortField,
            final String nameField, final boolean newButton)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(getDataSource(), SQLDialect.MYSQL);
        List<R> records = dslContext.selectFrom(table).fetch().sortAsc(sortField);

        s.append(AdminTable.startTable());
        for (R record : records)
        {
            TableRow tableRow = new TableRow(IdProvider.getId(record), recordId, NameProvider.getName(record, nameField),
                    "view" + columnName);
            if (editButton)
                tableRow.addButton("Edit", "edit" + columnName);
            s.append(tableRow.process());
        }
        s.append(AdminTable.endTable());

        if (newButton)
            s.append(AdminTable.finalButton("New " + columnName, "new" + columnName));

        getColumn(columnNr).setSelectedRecordId(recordId);
        getColumn(columnNr).setContent(s.toString());
    }

    public <R extends org.jooq.Record, T extends Comparable<? super T>> void showDependentColumn(final String columnName,
            final int columnNr, final int recordId, final boolean editButton, final Table<R> table, final Field<T> sortField,
            final String nameField, final TableField<R, UInteger> selectField, final boolean newButton)
    {
        StringBuilder s = new StringBuilder();
        DSLContext dslContext = DSL.using(getDataSource(), SQLDialect.MYSQL);
        List<R> records = dslContext.selectFrom(table)
                .where(selectField.eq(UInteger.valueOf(getColumn(columnNr - 1).getSelectedRecordId()))).fetch()
                .sortAsc(sortField);

        s.append(AdminTable.startTable());
        for (R record : records)
        {
            TableRow tableRow = new TableRow(IdProvider.getId(record), recordId, NameProvider.getName(record, nameField),
                    "view" + columnName);
            if (editButton)
                tableRow.addButton("Edit", "edit" + columnName);
            s.append(tableRow.process());
        }
        s.append(AdminTable.endTable());

        if (newButton)
            s.append(AdminTable.finalButton("New " + columnName, "new" + columnName));

        getColumn(columnNr).setSelectedRecordId(recordId);
        getColumn(columnNr).setContent(s.toString());
    }

    @SuppressWarnings("unchecked")
    public <R extends org.jooq.UpdatableRecord<R>> int saveRecord(final HttpServletRequest request, final int recordId,
            final Table<R> table, final String errorMenu)
    {
        DSLContext dslContext = DSL.using(getDataSource(), SQLDialect.MYSQL);
        R record = recordId == 0 ? dslContext.newRecord(table) : dslContext.selectFrom(table)
                .where(((TableField<R, UInteger>) table.field("id")).eq(UInteger.valueOf(recordId))).fetchOne();
        String errors = getFormColumn().getForm().setFields(record, request, this);
        if (errors.length() > 0)
        {
            System.err.println(errors);
            ModalWindowUtils.popup(this, "Error storing record (1)", errors, "clickMenu('" + errorMenu + "')");
            setError(true);
            return -1;
        }
        else
        {
            try
            {
                record.store();
            }
            catch (Exception exception)
            {
                System.err.println(exception.getMessage());
                System.err.println(record);
                ModalWindowUtils.popup(this, "Error storing record (2)", "<p>" + exception.getMessage() + "</p>",
                        "clickMenu('" + errorMenu + "')");
                setError(true);
                return -1;
            }
        }
        return Integer.valueOf(record.get("id").toString());
    }

    public <R extends org.jooq.UpdatableRecord<R>> void deleteRecord(final R record, final String tableName,
            final String recordName, final String okButtonName, final String errorMenu)
    {
        ModalWindowUtils.make2ButtonModalWindow(this, "Delete " + tableName,
                "<p>Delete " + tableName + " " + recordName + "?</p>", "DELETE",
                "clickRecordId('" + okButtonName + "', " + getId(record) + ")", "Cancel", "clickMenu('" + errorMenu + "')",
                "clickMenu('" + errorMenu + "')");
        setShowModalWindow(1);
    }

    public <R extends org.jooq.UpdatableRecord<R>> void deleteRecordOk(final R record, final String errorMenu)
    {
        try
        {
            record.delete();
        }
        catch (Exception exception)
        {
            ModalWindowUtils.popup(this, "Error deleting record", "<p>" + exception.getMessage() + "</p>",
                    "clickMenu('" + errorMenu + "')");
            setError(true);
        }
    }

    public <R extends org.jooq.UpdatableRecord<R>> int getId(final R record)
    {
        return IdProvider.getId(record);
    }
}
