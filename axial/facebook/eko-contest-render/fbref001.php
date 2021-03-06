<?php
require_once 'log4php/Logger.php';
Logger::configure('log4php.properties');

$logger = Logger::getLogger("fbref::render");
$logger->info(print_r($_SERVER['REQUEST_URI'],TRUE));

?><div align="center">
    <table width="520" border="0" cellspacing="0" cellpadding="0" style="background:#423d32; font-family:Arial,Verdana,Sans-serif; font-size:11px;">

        <tr>
            <td width="10"></td>
            <td width="500"><img src="http://axial.imetrical.com/EkoGabarit/images/header-logo.gif"></td>

            <td width="10"></td>
        </tr>


        <tr>
            <td width="10"></td>
            <td width="500"><img src="http://axial.imetrical.com/EkoGabarit/images/header-img.gif"></td>
            <td width="10"></td>
        </tr>

        <tr>
            <td width="10"></td>
            <td width="500" style="background:#ffffff;">

                <table width="500" border="0" cellspacing="0" cellpadding="0" style="background:#ffffff; font-family:Arial,Verdana,Sans-serif; font-size:11px;">
                    <tr>

                        <td width="320" valign="top" align="left" style="padding:10px; background-color:#ffffff; background-image:url('http://www.axialdev.com/emailing/Axialdev/general-hiver/images/maincontent-bg.gif'); background-repeat:no-repeat; background-position:top left;">
                            <span style="color:#666666;"><div id="date" name="Date" type="text">24 décembre 2007</div></span>

                            <h1 style="font-size:26px; font-weight:normal; color:#006699;">
                                <div id="titre" name="Titre de la nouvelle" type="text">
                                    Titre de la nouvelle 2012
                                </div>
                            </h1>

                            <div id="soustitre" name="Sous titre et intro" type="html">
                                <h2 style="font-size:14px; font-weight:bold; color:#006699;"><i>pour</i> <?= $_REQUEST['pid'] ?></h2>

                                <p>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu.</p>
                            </div>


                            <div id="highlight" name="nouvelle Highlight" type="html">
                                <table width="100%" cellspacing="0" cellpadding="0" border="0" style="background:#00b5d9; color:#ffffff; font-family:Arial,Verdana,Sans-serif; font-size:11px;">
                                    <tr>
                                        <td width="80" valign="top" align="right" style="padding:10px; font-weight:bold; font-size:14px;">Titre</td>
                                        <td valign="top" align="left" style="padding:10px 10px 20px 0px;">Texte</td>
                                    </tr>
                                </table>
                            </div>


                            <div id="texte" name="Sous titre" type="html">
                                <p>n enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend te llus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, </p>
                            </div>

                            <div id="listeexemple" name="Liste d'exemple" type="html">
                                <h2 style="font-size:14px; font-weight:bold; color:#006699;">Liste d'exemple</h2>

                                <ul style="margin:0px; padding:0px 0px 0px 15px; list-style-type:none; list-style-image:url('http://www.axialdev.com/emailing/Axialdev/general-hiver/images/puce-bleu.gif'); color:#666666;">
                                    <li><font color="#006699"><b>Brin de jasette</b></font><br>

                                        Nous avons développé pour eux toute l'interface permettant les échanges entre membres (messagerie privée, blogue), nous avons redéfini le visuel su site.
                                    </li>
                                    <li><font color="#006699"><b>Chambre de commerce de Sherbrooke</b></font><br>
                                        Développé en collaboration avec Groupe Itek, le site utilise notre système de gestion de contenu (CMS) : Axone avec le module calendrier.
                                    </li>
                                </ul>
                            </div>

                        </td>

                        <td width="150" valign="top" align="left" style="padding:30px 10px 20px 0px; color:#666666; font-size:10px;">

                            <span style="color:#423d32; font-size:14px; font-weight:bold;">Le service avant tout</span>

                            <p>Toujours dans le but de vous offrir le meilleur service, nous vous invitons à communiquer avec nous via 3 adresses de courriels :</p>

                            <p><a href="mailto:support@axialdev.com" style="color:#006699; text-decoration:none;"><b>support@axialdev.com</b></a><br>
                            pour tous vos problèmes de courriels, problèmes techniques, etc…</p>

                            <p><a href="mailto:ventes@axialdev.com" style="color:#006699; text-decoration:none;"><b>ventes@axialdev.com</b></a><br>
                            pour toutes vos demandes d’informations ou soumissions.</p>


                            <p><a href="mailto:prod@axialdev.com" style="color:#006699; text-decoration:none;"><b>prod@axialdev.com</b></a><br>
                            pour toutes demandes d'ajouts, envoi de votre matériel et pour vos projets en cours.</p>

                            <p>Vous pouvez également nous joindre via 4 numéros de téléphone :</p>

                            <p style="color:#423d32; font-weight:bold;">Sherbrooke : 819-564-2417<br>
                                Gatineau : 819-772-9396<br>
                                Montréal : 514-373-8104<br>

                            Sans-frais : 1 866 52-AXIAL</p>
                            <p>&nbsp;</p>
                            <table width="150" cellspacing="0" cellpadding="0" border="0" style="font-family:Arial,Verdana,Sans-serif; font-size:11px;">
                                <tr>
                                    <td height="20" style="padding:0px 10px; background:#938b78;"><img src="http://axial.imetrical.com/EkoGabarit/images/title-liens.gif"></td>
                                </tr>

                                <tr>
                                    <td valign="top" align="left" style="padding:10px; font-size:10px; background-color:#e8e4db; background-image:url('http://www.axialdev.com/emailing/Axialdev/general-hiver/images/liens-bg.gif'); background-repeat:no-repeat; background-position:top left;">

                                        <div id="liens" name="Liens" type="html">
                                            <ul style="margin:0px 0px 0px 0px; padding:0px 0px 0px 0px; line-height:12px; list-style-type:none;">
                                                <li style="margin:0px 0px 0px 0px; padding:5px 0px 5px 0px; border-bottom:1px solid #aaa18b;">N’oubliez pas de consulter nos présentations interactives pour avoir un aperçu de nos solutions .</li>
                                                <li style="margin:0px 0px 0px 0px; padding:5px 0px 5px 0px;  border-bottom:1px solid #aaa18b;">N’oubliez pas de consulter nos présentations interactives pour avoir un aperçu de nos solutions .</li>
                                                <li style="margin:0px 0px 0px 0px; padding:5px 0px 5px 0px;  border-bottom:1px solid #aaa18b;">N’oubliez pas de consulter nos présentations interactives pour avoir un aperçu de nos solutions .</li>
                                                <li style="margin:0px 0px 0px 0px; padding:5px 0px 5px 0px;  border-bottom:1px solid #aaa18b;">N’oubliez pas de consulter nos présentations interactives pour avoir un aperçu de nos solutions .</li>
                                                <li style="margin:0px 0px 0px 0px; padding:5px 0px 5px 0px;  border-bottom:1px solid #aaa18b;">N’oubliez pas de consulter nos présentations interactives pour avoir un aperçu de nos solutions .</li>

                                                <li style="margin:0px 0px 0px 0px; padding:5px 0px 5px 0px;  border-bottom:1px solid #aaa18b;">N’oubliez pas de consulter nos présentations interactives pour avoir un aperçu de nos solutions .</li>
                                                <li style="margin:0px 0px 0px 0px; padding:5px 0px 5px 0px;  border-bottom:1px solid #aaa18b;">N’oubliez pas de consulter nos présentations interactives pour avoir un aperçu de nos solutions .</li>
                                            </ul>
                                        </div>
                                    </td>
                                </tr>
                            </table>

                        </td>

                    </tr>
                </table>

            </td>
            <td width="10"></td>
        </tr>

        <tr>
            <td width="10"></td>
            <td width="500" height="47" style="color:#cccccc; text-align:right; ">
                <p id="liensaxial"><div number="4">Envoyer à un ami</div>  |  <div lang="fr" >Se désabonner</div></p>

                <p style="font-size:10px;">Téléphones : Gatineau : 819 772-9396     Montréal : 514 373-8104     Sherbrooke : 819 564-2417</p>
            </td>
            <td width="10"></td>
        </tr>

    </table>

</div>

<div style="text-align:center; font-size:11px; color:#666666; font-family:Arial,Verdana,Sans-serif; padding:10px 0px;">Si le courriel ne s'affiche pas correctement, une <div lang="fr" >version en ligne</div> est disponible.</div>
<div>stamp: <fb:application-name /> @ <fb:time t='<?= time() ?>'/></div>
