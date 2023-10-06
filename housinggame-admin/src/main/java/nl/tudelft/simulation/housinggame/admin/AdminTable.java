package nl.tudelft.simulation.housinggame.admin;

public class AdminTable
{

    public static String startTable()
    {
        return "      <div class=\"hg-admin-line-table\">";
    }

    public static String endTable()
    {
        return "      </div>\n"; // hg-admin-line-table
    }

    public static String finalButton(String text, String method)
    {
        StringBuilder s = new StringBuilder();
        s.append("      <div class=\"hg-admin-table-button\">");
        s.append("<a href=\"#\" onClick=\"clickRecordId('");
        s.append(method);
        s.append("',0); return false;\">");
        s.append(text);
        s.append("</a>");
        s.append("</div>\n");
        return s.toString();
    }
}
