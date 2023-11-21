package nl.tudelft.simulation.housinggame.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;

import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryDouble;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryPickRecordUInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryString;
import nl.tudelft.simulation.housinggame.admin.form.table.TableEntryUInt;
import nl.tudelft.simulation.housinggame.admin.form.table.TableForm;
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
            else if (click.startsWith("clone"))
            {
                ScenarioparametersRecord scenarioParameters =
                        SqlUtils.readRecordFromId(data, Tables.SCENARIOPARAMETERS, recordId);
                try
                {
                    SqlUtils.cloneScenarioParameters(data, scenarioParameters);
                }
                catch (Exception e)
                {
                    System.err.println(e.getMessage());
                    ModalWindowUtils.popup(data, "Error cloning Parameters",
                            e.getClass().getSimpleName() + ": " + e.getMessage(), "clickMenu('parameters')");
                    data.setError(true);
                }
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
        TableForm form = new TableForm()
                .setEdit(edit)
                .setCancelMethod("parameters", data.getColumn(0).getSelectedRecordId())
                .setEditMethod("editParameters")
                .setSaveMethod("saveParameters")
                .setDeleteMethod("deleteParameters", "Delete", "<br>Note: Do not delete scenario parameters when"
                        + "<br> game with these parameters has been played")
                .setRecordNr(parametersId)
                .setLabelLength("75%")
                .setFieldLength("25%")
                .startForm()
                .addEntry(new TableEntryString(Tables.SCENARIOPARAMETERS.NAME)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getName(), "")
                        .setLabel("Parameters identifying name")
                        .setMaxChars(255))
                .addEntry(new TableEntryUInt(Tables.SCENARIOPARAMETERS.PLUVIAL_REPAIR_COSTS_PER_DAMAGE_POINT)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getPluvialRepairCostsPerDamagePoint(), UInteger.valueOf(0))
                        .setLabel("Pluvial Repair Costs per damage point")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.SCENARIOPARAMETERS.FLUVIAL_REPAIR_COSTS_PER_DAMAGE_POINT)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getFluvialRepairCostsPerDamagePoint(), UInteger.valueOf(0))
                        .setLabel("Fluvial Repair Costs per damage point")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.SCENARIOPARAMETERS.PLUVIAL_REPAIR_COSTS_FIXED)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getPluvialRepairCostsFixed(), UInteger.valueOf(0))
                        .setLabel("Pluvial Repair Costs Fixed")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.SCENARIOPARAMETERS.FLUVIAL_REPAIR_COSTS_FIXED)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getFluvialRepairCostsFixed(), UInteger.valueOf(0))
                        .setLabel("Fluvial Repair Costs Fixed")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.SCENARIOPARAMETERS.PLUVIAL_SATISFACTION_PENALTY_IF_AREA_FLOODED)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getPluvialSatisfactionPenaltyIfAreaFlooded(), UInteger.valueOf(0))
                        .setLabel("Pluvial Satisfaction Penalty if area flooded")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.SCENARIOPARAMETERS.FLUVIAL_SATISFACTION_PENALTY_IF_AREA_FLOODED)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getFluvialSatisfactionPenaltyIfAreaFlooded(), UInteger.valueOf(0))
                        .setLabel("Fluvial Satisfaction Penalty if area flooded")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.SCENARIOPARAMETERS.PLUVIAL_SATISFACTION_PENALTY_HOUSE_FLOODED_FIXED)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getPluvialSatisfactionPenaltyHouseFloodedFixed(), UInteger.valueOf(0))
                        .setLabel("Pluvial Satisfaction Penalty, fixed if house flooded")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.SCENARIOPARAMETERS.FLUVIAL_SATISFACTION_PENALTY_HOUSE_FLOODED_FIXED)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getFluvialSatisfactionPenaltyHouseFloodedFixed(), UInteger.valueOf(0))
                        .setLabel("Fluvial Satisfaction Penalty, fixed if house flooded")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.SCENARIOPARAMETERS.PLUVIAL_SATISFACTION_PENALTY_PER_DAMAGE_POINT)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getPluvialSatisfactionPenaltyPerDamagePoint(), UInteger.valueOf(0))
                        .setLabel("Pluvial Satisfaction Penalty per damage point")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.SCENARIOPARAMETERS.FLUVIAL_SATISFACTION_PENALTY_PER_DAMAGE_POINT)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getFluvialSatisfactionPenaltyPerDamagePoint(), UInteger.valueOf(0))
                        .setLabel("Fluvial Satisfaction Penalty per damage point")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.SCENARIOPARAMETERS.SATISFACTION_DEBT_PENALTY)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getSatisfactionDebtPenalty(), UInteger.valueOf(0))
                        .setLabel("Satisfaction Debt Penalty")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.SCENARIOPARAMETERS.SATISFACTION_HOUSE_RATING_TOO_LOW_FIXED)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getSatisfactionHouseRatingTooLowFixed(), UInteger.valueOf(0))
                        .setLabel("Satisfaction House Rating Fixed if rating too low")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.SCENARIOPARAMETERS.SATISFACTION_HOUSE_RATING_TOO_LOW_PER_DELTA)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getSatisfactionHouseRatingTooLowPerDelta(), UInteger.valueOf(0))
                        .setLabel("Satisfaction House Rating per delta if rating too low")
                        .setMin(0))
                .addEntry(new TableEntryUInt(Tables.SCENARIOPARAMETERS.SATISFACTION_MOVE_PENALTY)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getSatisfactionMovePenalty(), UInteger.valueOf(0))
                        .setLabel("Satisfaction Move Penalty")
                        .setMin(0))
                .addEntry(new TableEntryDouble(Tables.SCENARIOPARAMETERS.MORTGAGE_PERCENTAGE)
                        .setRequired()
                        .setInitialValue(scenarioParameters.getMortgagePercentage(), 0.0)
                        .setLabel("Mortgage Percentage")
                        .setMin(0.0)
                        .setMax(100.0))
                .addEntry(new TableEntryPickRecordUInt(Tables.SCENARIOPARAMETERS.DEFAULT_LANGUAGE_ID)
                        .setRequired()
                        .setPickTable(data, Tables.LANGUAGE, Tables.LANGUAGE.ID,
                                Tables.LANGUAGE.CODE)
                        .setInitialValue(scenarioParameters.getDefaultLanguageId(), UInteger.valueOf(0))
                        .setLabel("Default language"));
        //@formatter:on

        form.addAddtionalButton("cloneParameters", "Clone Scenario Parameters");
        form.endForm();
        data.getFormColumn().setHeaderForm("Edit Parameters", form);
    }

}
