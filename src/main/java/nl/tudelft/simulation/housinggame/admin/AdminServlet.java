package nl.tudelft.simulation.housinggame.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/admin")
@MultipartConfig
public class AdminServlet extends HttpServlet
{

    /** */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException
    {
        HttpSession session = request.getSession();

        AdminData data = SessionUtils.getData(session);
        if (data == null)
        {
            response.sendRedirect("/housinggame-admin/login");
            return;
        }

        String click = "";
        if (request.getParameter("click") != null)
            click = request.getParameter("click").toString();
        else if (request.getParameter("editClick") != null)
            click = request.getParameter("editClick").toString();

        int recordNr = 0;
        if (request.getParameter("recordNr") != null)
            recordNr = Integer.parseInt(request.getParameter("recordNr"));
        else if (request.getParameter("editRecordNr") != null)
            recordNr = Integer.parseInt(request.getParameter("editRecordNr"));

        data.setShowModalWindow(0);
        data.setModalWindowHtml("");

        switch (click)
        {
            // ScenarioParameters
            case "parameters":
            case "viewParameters":
            case "editParameters":
            case "saveParameters":
            case "deleteParameters":
            case "deleteParametersOk":
            case "newParameters":
                data.setMenuChoice("parameters");
                MaintainParameters.handleMenu(request, click, recordNr);
                break;

            // GameVersion - Scenario
            case "scenario":
            case "viewGameVersion":
            case "editGameVersion":
            case "saveGameVersion":
            case "deleteGameVersion":
            case "deleteGameVersionOk":
            case "newGameVersion":

            case "viewScenario":
            case "editScenario":
            case "saveScenario":
            case "deleteScenario":
            case "deleteScenarioOk":
            case "newScenario":
                data.setMenuChoice("scenario");
                MaintainScenario.handleMenu(request, click, recordNr);
                break;

            // (GameVersion) - (Scenario) - Round - NewsItem
            case "round":
            case "viewRoundGameVersion":
            case "viewRoundScenario":

            case "viewRound":
            case "editRound":
            case "saveRound":
            case "deleteRound":
            case "deleteRoundOk":
            case "newRound":

            case "viewNewsItem":
            case "editNewsItem":
            case "saveNewsItem":
            case "deleteNewsItem":
            case "deleteNewsItemOk":
            case "newNewsItem":
                data.setMenuChoice("round");
                MaintainRound.handleMenu(request, click, recordNr);
                break;

            // (GameVersion) - (Scenario) - WelfareType
            case "welfaretype":
            case "viewWelfareTypeGameVersion":
            case "viewWelfareTypeScenario":

            case "viewWelfareType":
            case "editWelfareType":
            case "saveWelfareType":
            case "deleteWelfareType":
            case "deleteWelfareTypeOk":
            case "newWelfareType":
                data.setMenuChoice("welfaretype");
                MaintainWelfareType.handleMenu(request, click, recordNr);
                break;

            // (GameVersion) - (Scenario) - MeasureType
            case "measuretype":
            case "viewMeasureTypeGameVersion":
            case "viewMeasureTypeScenario":

            case "viewMeasureType":
            case "editMeasureType":
            case "saveMeasureType":
            case "deleteMeasureType":
            case "deleteMeasureTypeOk":
            case "newMeasureType":
                data.setMenuChoice("measuretype");
                MaintainMeasureType.handleMenu(request, click, recordNr);
                break;

            // (GameVersion) - (Scenario) - Question
            case "question":
            case "viewQuestionGameVersion":
            case "viewQuestionScenario":

            case "viewQuestion":
            case "editQuestion":
            case "saveQuestion":
            case "deleteQuestion":
            case "deleteQuestionOk":
            case "newQuestion":
                data.setMenuChoice("question");
                MaintainQuestion.handleMenu(request, click, recordNr);
                break;

            // (GameVersion) - Community - Tax
            case "community":
            case "viewCommunityGameVersion":

            case "viewCommunity":
            case "editCommunity":
            case "saveCommunity":
            case "deleteCommunity":
            case "deleteCommunityOk":
            case "newCommunity":

            case "viewTax":
            case "editTax":
            case "saveTax":
            case "deleteTax":
            case "deleteTaxOk":
            case "newTax":
                data.setMenuChoice("community");
                MaintainCommunity.handleMenu(request, click, recordNr);
                break;

            // (GameVersion) - (Community) - House - InitialHouseMeasure
            case "house":
            case "viewHouseGameVersion":
            case "viewHouseCommunity":

            case "viewHouse":
            case "editHouse":
            case "saveHouse":
            case "deleteHouse":
            case "deleteHouseOk":
            case "newHouse":

            case "viewInitialHouseMeasure":
            case "editInitialHouseMeasure":
            case "saveInitialHouseMeasure":
            case "deleteInitialHouseMeasure":
            case "deleteInitialHouseMeasureOk":
            case "newInitialHouseMeasure":
                data.setMenuChoice("house");
                MaintainHouse.handleMenu(request, click, recordNr);
                break;

            // User - Facilitator
            case "user":
            case "viewUser":
            case "editUser":
            case "saveUser":
            case "deleteUser":
            case "deleteUserOk":
            case "newUser":

            case "viewFacilitator":
            case "editFacilitator":
            case "saveFacilitator":
            case "deleteFacilitator":
            case "deleteFacilitatorOk":
            case "newFacilitator":
                data.setMenuChoice("user");
                MaintainUser.handleMenu(request, click, recordNr);
                break;

            // (GameVersion) - (Scenario) - GameSession - Group - Player
            case "gamesession":
            case "viewGameSessionGameVersion":

            case "viewGameSession":
            case "editGameSession":
            case "saveGameSession":
            case "deleteGameSession":
            case "deleteGameSessionOk":
            case "newGameSession":

            case "viewGroup":
            case "editGroup":
            case "saveGroup":
            case "deleteGroup":
            case "deleteGroupOk":
            case "newGroup":
            case "generateGroups":
            case "generateGroupsParams":

            case "viewPlayer":
            case "editPlayer":
            case "savePlayer":
            case "deletePlayer":
            case "deletePlayerOk":
            case "newPlayer":
                data.setMenuChoice("gamesession");
                MaintainGameSession.handleMenu(request, click, recordNr);
                break;

            default:
                break;
        }

        response.sendRedirect("jsp/admin/admin.jsp");
    }

    public static void makeColumnContent(final AdminData data)
    {
        StringBuilder s = new StringBuilder();
        s.append("<table width=\"100%\">\n");
        s.append("  <tr>");
        for (int i = 0; i < data.getNrColumns(); i++)
        {
            s.append("    <td width=\"");
            s.append(data.getColumn(i).getWidth());
            s.append("\">\n");
            s.append("      <div class=\"hg-admin-line-header\">");
            s.append(data.getColumn(i).getHeader());
            s.append("</div>\n");
            s.append(data.getColumn(i).getContent());
            s.append("    </td>\n");
        }
        if (data.getFormColumn() != null)
        {
            s.append("    <td width=\"");
            s.append(data.getFormColumn().getWidth());
            s.append("\">\n");
            s.append("      <div class=\"hg-admin-line-header\">");
            s.append(data.getFormColumn().getHeader());
            s.append("</div>\n");
            s.append(data.getFormColumn().getContent());
            s.append("    </td>\n");
        }
        s.append("  </tr>");
        s.append("</table>\n");
        data.setContentHtml(s.toString());
    }

    public static String getTopMenu(final AdminData data)
    {
        StringBuilder s = new StringBuilder();
        topmenu(data, s, "parameters", "Parameters"); // ScenarioParameters
        topmenu(data, s, "scenario", "Scenario"); // GameVersion - Scenario
        topmenu(data, s, "welfaretype", "WelfareType"); // (GameVersion) - (Scenario) - WelfareType
        topmenu(data, s, "measuretype", "MeasureType"); // (GameVersion) - (Scenario) - MeasureType
        topmenu(data, s, "community", "Community"); // (GameVersion) - Community - Tax
        topmenu(data, s, "house", "House"); // (GameVersion) - (Community) - House - InitialHouseMeasure
        topmenu(data, s, "round", "Round"); // (GameVersion) - (Scenario) - Round - NewsItem
        topmenu(data, s, "question", "Question"); // (GameVersion) - (Scenario) - Question
        topmenu(data, s, "user", "User"); // User - Facilitator
        topmenu(data, s, "gamesession", "GameSession"); // (GameVersion) - GameSession - Group - Player
        topmenu(data, s, "groupround", "GroupRound"); // (GameSession) - (Group) - GroupRound
        topmenu(data, s, "gameplay", "GamePlay"); // (GameSession) - (Player) - PlayerRound - Measure
        topmenu(data, s, "result", "Result");
        return s.toString();
    }

    private static final String bn = "          <div class=\"hg-admin-menu-button\"";

    private static final String br = "          <div class=\"hg-admin-menu-button-red\"";

    private static void topmenu(final AdminData data, final StringBuilder s, final String key, final String text)
    {
        s.append(key.equals(data.getMenuChoice()) ? br : bn);
        s.append(" onclick=\"clickMenu('");
        s.append(key);
        s.append("')\">");
        s.append(text);
        s.append("</div>\n");
    }
}
