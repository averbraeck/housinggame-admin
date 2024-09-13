<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="ISO-8859-1">
    <title>Housing Game Administration</title>

    <!--  favicon -->
    <link rel="shortcut icon" href="/housinggame-admin/favicon.ico" type="image/x-icon">

    <link rel="stylesheet" type="text/css" href="/housinggame-admin/css/admin.css" />
    <script src="/housinggame-admin/js/admin.js"></script>

    <style>
    table, th, td {
      border: 1px solid gray;
      border-spacing: 0px;
      border-collapse: collapse;
      padding: 5px;
      vertical-align: top;
    }
    
    body {
      line-height: 1.2;
    }
    </style>

  </head>

  <body onload="initPage()">
    <div class="hg-page">
      <div class="hg-header">
        <span class="hg-game-heading">Housing Game</span>
        <span class="hg-slogan">Game Administration</span>
      </div>
      <div class="hg-header-right">
        <img src="images/tudelft.png" />
        <p><a href="/housinggame-admin">LOGOUT</a></p>
        <span style="font-size: 12px; padding-left: 20px; position:relative; top:-4px; color:black;">v1.5.2</span>
      </div>
      <div class="hg-header-game-user">
        <p>&nbsp;</p>
        <p>User:&nbsp;&nbsp;&nbsp; ${adminData.getUser().getUsername()}</p>
      </div>

      <div class="hg-body">
      
        <div class="hg-admin-menu">
          ${adminData.getTopMenu1()}
        </div>
        <div class="hg-admin-menu" style="margin-bottom: 20px;">
          ${adminData.getTopMenu2()}
        </div>
        <div class="hg-admin" id="hg-admin">
          ${adminData.getContentHtml()}
        </div>
        
      </div> <!-- hg-body -->
      
    </div> <!-- hg-page -->
    
    <!-- modal window for the client information within an order -->
    
    ${adminData.getModalWindowHtml()}

    <form id="clickForm" action="/housinggame-admin/admin" method="POST" style="display:none;">
      <input id="click" type="hidden" name="click" value="tobefilled" />
      <input id="recordNr" type="hidden" name="recordNr" value="0" />
    </form>

  </body>

</html>