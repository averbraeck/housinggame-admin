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
            // Language - LanguageGroup (non-heriarchic) - Label
            case "language":
            case "viewLanguage":
            case "editLanguage":
            case "saveLanguage":
            case "deleteLanguage":
            case "deleteLanguageOk":
            case "newLanguage":

            case "viewLanguageGroup":
            case "editLanguageGroup":
            case "saveLanguageGroup":
            case "deleteLanguageGroup":
            case "deleteLanguageGroupOk":
            case "newLanguageGroup":

            case "viewLabel":
            case "editLabel":
            case "saveLabel":
            case "deleteLabel":
            case "deleteLabelOk":
            case "newLabel":
                data.setMenuChoice("language");
                MaintainLanguage.handleMenu(request, click, recordNr);
                break;

            // ScenarioParameters
            case "parameters":
            case "viewParameters":
            case "editParameters":
            case "saveParameters":
            case "deleteParameters":
            case "deleteParametersOk":
            case "newParameters":
            case "cloneParameters":
                data.setMenuChoice("parameters");
                MaintainParameters.handleMenu(request, click, recordNr);
                break;

            // GameVersion - Label
            case "label":
            case "viewLabelGameVersion":
            case "editLabelGameVersion":
            case "saveLabelGameVersion":
            case "deleteLabelGameVersion":
            case "deleteLabelGameVersionOk":
            case "newLabelGameVersion":

            // GameVersion - Scenario
            case "scenario":
            case "viewGameVersion":
            case "editGameVersion":
            case "saveGameVersion":
            case "deleteGameVersion":
            case "deleteGameVersionOk":
            case "newGameVersion":
            case "cloneGameVersion":
            case "destroyGameVersion":
            case "destroyGameVersionOk":

            case "viewScenario":
            case "editScenario":
            case "saveScenario":
            case "deleteScenario":
            case "deleteScenarioOk":
            case "newScenario":
            case "cloneScenario":
            case "destroyScenario":
            case "destroyScenarioOk":
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

            case "viewNewsEffects":
            case "editNewsEffects":
            case "saveNewsEffects":
            case "deleteNewsEffects":
            case "deleteNewsEffectsOk":
            case "newNewsEffects":
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

            // (GameVersion) - MeasureType
            case "measuretype":
            case "viewMeasureTypeGameVersion":

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

            // User
            case "user":
            case "viewUser":
            case "editUser":
            case "saveUser":
            case "deleteUser":
            case "deleteUserOk":
            case "newUser":
                data.setMenuChoice("user");
                MaintainUser.handleMenu(request, click, recordNr);
                break;

            // (GameVersion) - GameSession - Group - Player
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

            // (GameSession) - (Group) - GroupRound - (Player) - PlayerRound - Measure/QuestionScore
            case "play":
            case "viewPlayGameSession":
            case "viewPlayGroup":

            case "viewPlayGroupRound":
            case "editPlayGroupRound":
            case "savePlayGroupRound":
            case "deletePlayGroupRound":
            case "deletePlayGroupRoundOk":
            case "newPlayGroupRound":

            case "viewPlayBid":
            case "editPlayBid":
            case "savePlayBid":
            case "deletePlayBid":
            case "deletePlayBidOk":
            case "newPlayBid":

            case "viewPlayPlayer":

            case "viewPlayPlayerRound":
            case "editPlayPlayerRound":
            case "savePlayPlayerRound":
            case "deletePlayPlayerRound":
            case "deletePlayPlayerRoundOk":
            case "newPlayPlayerRound":

            case "viewPlayMeasure":
            case "editPlayMeasure":
            case "savePlayMeasure":
            case "deletePlayMeasure":
            case "deletePlayMeasureOk":
            case "newPlayMeasure":

            case "viewPlayQuestion":
            case "editPlayQuestion":
            case "savePlayQuestion":
            case "deletePlayQuestion":
            case "deletePlayQuestionOk":
            case "newPlayQuestion":
                data.setMenuChoice("play");
                MaintainPlay.handleMenu(request, click, recordNr);
                break;

            /*-

            // (GameSession) - (Group) - Result
            case "play-result":
            case "viewPlayResultGameSession":
            case "viewPlayResultGroup":
            case "viewPlayResultResult":
                data.setMenuChoice("play-result");
                MaintainPlayResult.handleMenu(request, click, recordNr);
                break;

            */

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
        topmenu(data, s, "language", "Language"); // Language - LanguageGroup (non-heriarchic) - Label
        topmenu(data, s, "parameters", "Parameters"); // ScenarioParameters
        topmenu(data, s, "scenario", "Scenario"); // GameVersion - Scenario
        topmenu(data, s, "welfaretype", "Welfare"); // (GameVersion) - (Scenario) - WelfareType
        topmenu(data, s, "measuretype", "Measure"); // (GameVersion) - MeasureType
        topmenu(data, s, "community", "Community"); // (GameVersion) - Community - Tax
        topmenu(data, s, "house", "House"); // (GameVersion) - (Community) - House - InitialHouseMeasure
        topmenu(data, s, "round", "Round"); // (GameVersion) - (Scenario) - Round - NewsItem
        topmenu(data, s, "question", "Question"); // (GameVersion) - (Scenario) - Question
        topmenu(data, s, "user", "User"); // User
        topmenu(data, s, "gamesession", "Session"); // (GameVersion) - GameSession - Group - Player
        topmenu(data, s, "play", "Play"); // (GameSession) - (Group) - GroupRound - (Player) - PlayerRound - Measure/Question
        topmenu(data, s, "result", "Result"); // (GameSession) - (Group) - Result
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
