package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.form.AdminForm;
import nl.tudelft.simulation.housinggame.admin.form.FormEntryDouble;
import nl.tudelft.simulation.housinggame.admin.form.FormEntryString;
import nl.tudelft.simulation.housinggame.data.Tables;
import nl.tudelft.simulation.housinggame.data.tables.records.ScenarioparametersRecord;

public class MaintainParameters
{

    public static void handleMenu(final HttpServletRequest request, final String click, int recordId)
    {
        HttpSession session = request.getSession();
        AdminData data = SessionUtils.getData(session);

        if (click.equals("parameters"))
        {
            data.clearColumns("30%", "Parameters");
            data.clearFormColumn("70%", "Edit Properties");
            showScenarioParameters(session, data, 0, true, false);
        }

        else if (click.contains("Parameters"))
        {
            if (click.startsWith("save"))
                recordId = data.saveRecord(request, recordId, Tables.SCENARIOPARAMETERS, "parameters");
            else if (click.startsWith("delete"))
            {
                ScenarioparametersRecord scenarioParameters =
                        SqlUtils.readRecordFromId(data, Tables.SCENARIOPARAMETERS, recordId);
                if (click.endsWith("Ok"))
                    data.deleteRecordOk(scenarioParameters, "parameters");
                else
                    data.deleteRecord(scenarioParameters, "Parameters", scenarioParameters.getName(), "deleteParametersOk",
                            "parameters");
                recordId = 0;
            }
            if (!data.isError())
            {
                showScenarioParameters(session, data, recordId, true, !click.startsWith("view"));
                if (click.startsWith("new"))
                    editScenarioParameters(session, data, 0, true);
            }
        }

        AdminServlet.makeColumnContent(data);
    }

    /*
     * *********************************************************************************************************
     * *************************************** SCENARIOPARAMETERS **********************************************
     * *********************************************************************************************************
     */

    public static void showScenarioParameters(final HttpSession session, final AdminData data, final int recordId,
            final boolean editButton, final boolean editRecord)
    {
        data.showColumn("Parameters", 0, recordId, editButton, Tables.SCENARIOPARAMETERS, Tables.SCENARIOPARAMETERS.NAME,
                "name", true);
        data.resetFormColumn();
        if (recordId != 0)
        {
            editScenarioParameters(session, data, recordId, editRecord);
        }
    }

    public static void editScenarioParameters(final HttpSession session, final AdminData data, final int parametersId,
            final boolean edit)
    {
        DSLContext dslContext = DSL.using(data.getDataSource(), SQLDialect.MYSQL);
        ScenarioparametersRecord scenarioParameters = parametersId == 0 ? dslContext.newRecord(Tables.SCENARIOPARAMETERS)
                : dslContext.selectFrom(Tables.SCENARIOPARAMETERS)
                        .where(Tables.SCENARIOPARAMETERS.ID.eq(UInteger.valueOf(parametersId))).fetchOne();
        //@formatter:off
        AdminForm form = new AdminForm()
                .setEdit(edit)
                .setCancelMethod("parameters", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editParameters")
                .setSaveMethod("saveParameters")
                .setDeleteMethod("deleteParameters", "Delete", "<br>Note: Do not delete scenario parameters when"
                        + "<br> game with these parameters has been played")
                .setRecordNr(parametersId)
                .startForm()
                .addEntry(new FormEntryString(Tables.SCENARIOPARAMETERS.NAME)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getName(), "")
                        .setLabel("Parameters identifying name")
                        .setMaxChars(255))
                .addEntry(new FormEntryDouble(Tables.SCENARIOPARAMETERS.PLUVIAL_REPAIR_COSTS)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getPluvialRepairCosts(), 0.0)
                        .setLabel("Pluvial Repair Costs")
                        .setMin(0.0))
                .addEntry(new FormEntryDouble(Tables.SCENARIOPARAMETERS.PLUVIAL_SATISFACTION_PENALTY)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getPluvialSatisfactionPenalty(), 0.0)
                        .setLabel("Pluvial Satisfaction Penalty")
                        .setMin(0.0))
                .addEntry(new FormEntryDouble(Tables.SCENARIOPARAMETERS.FLUVIAL_REPAIR_COSTS)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getFluvialRepairCosts(), 0.0)
                        .setLabel("Fluvial Repair Costs")
                        .setMin(0.0))
                .addEntry(new FormEntryDouble(Tables.SCENARIOPARAMETERS.FLUVIAL_SATISFACTION_PENALTY)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getFluvialSatisfactionPenalty(), 0.0)
                        .setLabel("Fluvial Satisfaction Penalty")
                        .setMin(0.0))
                .addEntry(new FormEntryDouble(Tables.SCENARIOPARAMETERS.SATISFACTION_DEBT_PENALTY)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getSatisfactionDebtPenalty(), 0.0)
                        .setLabel("Satisfaction Debt Penalty")
                        .setMin(0.0))
                .addEntry(new FormEntryDouble(Tables.SCENARIOPARAMETERS.SATISFACTION_HOUSE_RATING_CHANGE)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getSatisfactionHouseRatingChange(), 0.0)
                        .setLabel("Satisfaction House Rating")
                        .setMin(0.0))
                .addEntry(new FormEntryDouble(Tables.SCENARIOPARAMETERS.SATISFACTION_MOVE_PENALTY)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getSatisfactionMovePenalty(), 0.0)
                        .setLabel("Satisfaction Move Penalty")
                        .setMin(0.0))
                .addEntry(new FormEntryDouble(Tables.SCENARIOPARAMETERS.FLOOD_REPAIR_COST)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getFloodRepairCost(), 0.0)
                        .setLabel("Flood Repair Cost")
                        .setMin(0.0))
                .addEntry(new FormEntryDouble(Tables.SCENARIOPARAMETERS.FLOOD_SATISFACTION_PENALTY)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getFloodSatisfactionPenalty(), 0.0)
                        .setLabel("Flood Satisfaction Penalty")
                        .setMin(0.0))
                .addEntry(new FormEntryDouble(Tables.SCENARIOPARAMETERS.MORTGAGE_PERCENTAGE)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getMortgagePercentage(), 0.0)
                        .setLabel("Mortgage Percentage")
                        .setMin(0.0)
                        .setMax(100.0))
                .endForm();
        //@formatter:on
        data.getFormColumn().setHeaderForm("Edit Parameters", form);
    }

}
