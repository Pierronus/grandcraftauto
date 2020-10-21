<?php
$timer = file_get_contents('http://api.serveurs-minecraft.com/api.php?Latence_Ping&ip=51.91.170.102&port=27110');
$favicon = file_get_contents('http://api.serveurs-minecraft.com/api.php?Favicon_Ping&ip=51.91.170.102&port=27110');
$maxplayer = file_get_contents('http://api.serveurs-minecraft.com/api.php?Joueurs_Maximum_Ping&ip=51.91.170.102&port=27110');
$playeronline = file_get_contents('http://api.serveurs-minecraft.com/api.php?Joueurs_En_Ligne_Ping&ip=51.91.170.102&port=27110');
?>

<!DOCTYPE html>
<html>
    <meta charset="UTF-8">
    <title>Grandcraftauto - Accueil</title>
    <link rel="stylesheet" href="gca.css">
    <head>
      <div class="topnav">
        <a class="active" href="gca.html">Accueil</a>
        <a href="https://grandcraftauto.mtxserv.com/forum">Forum</a>
        <a href="about.html">A propos</a> 
      </div>
      <div>
      <header class="header"></header>
      </div>
    </head>
<div class="hero-image">
    <div class="hero-text">
      <h2>Bienvenue sur</h2>
      <h1 style="text-decoration: underline; color: #933d40; font-weight: bold;">Grandcraftauto</h2>
    </div>
    
  <body>
    <div class="enligne">
      <h2 align=center style="color: white; font-family: Arial"><?php echo $playeronline ?> / <?php echo $maxplayer ?></h2>
      <h3 align=center style="color: white; font-family: Arial"> joueurs connectés</h3>

    </div>
  </body>