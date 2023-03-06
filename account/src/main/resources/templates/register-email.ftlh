<!DOCTYPE html>
<html lang="de-AT">
<head>

    <meta charset="utf-8">
    <meta http-equiv="x-ua-compatible" content="ie=edge">
    <title>Email Verifikation</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style type="text/css">
        /**
         * Google webfonts. Recommended to include the .woff version for cross-client compatibility.
         */
        @media screen {
            @font-face {
                font-family: 'Source Sans Pro';
                font-style: normal;
                font-weight: 400;
                src: local('Source Sans Pro Regular'), local('SourceSansPro-Regular'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/ODelI1aHBYDBqgeIAH2zlBM0YzuT7MdOe03otPbuUS0.woff) format('woff');
            }
            @font-face {
                font-family: 'Source Sans Pro';
                font-style: normal;
                font-weight: 700;
                src: local('Source Sans Pro Bold'), local('SourceSansPro-Bold'), url(https://fonts.gstatic.com/s/sourcesanspro/v10/toadOcfmlt9b38dHJxOBGFkQc6VGVFSmCnC_l7QZG60.woff) format('woff');
            }
        }
        /**
         * Avoid browser level font resizing.
         * 1. Windows Mobile
         * 2. iOS / OSX
         */
        body,
        table,
        td,
        a {
            -ms-text-size-adjust: 100%; /* 1 */
            -webkit-text-size-adjust: 100%; /* 2 */
        }
        /**
         * Remove extra space added to tables and cells in Outlook.
         */
        table,
        td {
            mso-table-rspace: 0pt;
            mso-table-lspace: 0pt;
        }
        /**
         * Better fluid images in Internet Explorer.
         */
        img {
            -ms-interpolation-mode: bicubic;
        }
        /**
         * Remove blue links for iOS devices.
         */
        a[x-apple-data-detectors] {
            font-family: inherit !important;
            font-size: inherit !important;
            font-weight: inherit !important;
            line-height: inherit !important;
            color: inherit !important;
            text-decoration: none !important;
        }
        /**
         * Fix centering issues in Android 4.4.
         */
        div[style*="margin: 16px 0;"] {
            margin: 0 !important;
        }
        body {
            width: 100% !important;
            height: 100% !important;
            padding: 0 !important;
            margin: 0 !important;
        }
        /**
         * Collapse table borders to avoid space between cells.
         */
        table {
            border-collapse: collapse !important;
        }
        a {
            color: #1a82e2;
        }
        img {
            height: auto;
            line-height: 100%;
            text-decoration: none;
            border: 0;
            outline: none;
        }
    </style>

</head>
<body style="background-color: #e9ecef;">


<!-- start body -->
<table border="0" cellpadding="0" cellspacing="0" width="100%">

    <!-- start logo -->
    <tr>
        <td align="center" bgcolor="#e9ecef">
            <!--[if (gte mso 9)|(IE)]>
            <table align="center" border="0" cellpadding="0" cellspacing="0" width="600">
                <tr>
                    <td align="center" valign="top" width="600">
            <![endif]-->
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                <tr>
                    <td align="center" valign="top" style="padding: 36px 24px;">
                        <a href="https://collew.netlify.app/" target="_blank" style="display: inline-block;">
                            <img src="data:image/png;base64,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" alt="Logo" border="0" width="48" style="display: block; width: 48px; max-width: 48px; min-width: 48px;">
                        </a>
                    </td>
                </tr>
            </table>
            <!--[if (gte mso 9)|(IE)]>
            </td>
            </tr>
            </table>
            <![endif]-->
        </td>
    </tr>
    <!-- end logo -->

    <!-- start hero -->
    <tr>
        <td align="center" bgcolor="#e9ecef">
            <!--[if (gte mso 9)|(IE)]>
            <table align="center" border="0" cellpadding="0" cellspacing="0" width="600">
                <tr>
                    <td align="center" valign="top" width="600">
            <![endif]-->
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">
                <tr>
                    <td align="left" bgcolor="#ffffff" style="padding: 36px 24px 0; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; border-top: 3px solid #d4dadf;">
                        <h1 style="margin: 0; font-size: 32px; font-weight: 700; letter-spacing: -1px; line-height: 48px;">Verifizieren Sie ihre Email-Addresse:</h1>
                    </td>
                </tr>
            </table>
            <!--[if (gte mso 9)|(IE)]>
            </td>
            </tr>
            </table>
            <![endif]-->
        </td>
    </tr>
    <!-- end hero -->


    <!-- start copy block -->
    <tr>
        <td align="center" bgcolor="#e9ecef">
            <!--[if (gte mso 9)|(IE)]>
            <table align="center" border="0" cellpadding="0" cellspacing="0" width="600">
                <tr>
                    <td align="center" valign="top" width="600">
            <![endif]-->
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">

                <!-- start copy -->
                <tr>
                    <td align="left" bgcolor="#ffffff" style="padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;">
                        <p style="margin: 0;">Hallo ${firstname}, <br> Drücken Sie auf den folgenden Knopf um ihre Email zu bestätigen. Falls Sie keinen Account erstellt haben können Sie diese Email löschen.</p>
                    </td>
                </tr>
                <!-- end copy -->

                <!-- start button -->
                <tr>
                    <td align="left" bgcolor="#ffffff">
                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
                            <tr>
                                <td align="center" bgcolor="#ffffff" style="padding: 12px;">
                                    <table border="0" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <td align="center" bgcolor="#bdd9a0" style="border-radius: 6px;">
                                                <a href="${link}" target="_blank" style="display: inline-block; padding: 16px 36px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; color: #023020; text-decoration: none; border-radius: 6px;">Email Verifizieren</a>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <!-- end button -->

                <!-- start copy -->
                <tr>
                    <td align="left" bgcolor="#ffffff" style="padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px;">
                        <p style="margin: 0;">Falls dies nicht Funktionieren sollte können Sie folgenen Link in Ihren Browser kopieren:</p>
                        <p style="margin: 0;"><a href="${link}" target="_blank">${link}</a></p>
                    </td>
                </tr>
                <!-- end copy -->

                <!-- start copy -->
                <tr>
                    <td align="left" bgcolor="#ffffff" style="padding: 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 16px; line-height: 24px; border-bottom: 3px solid #d4dadf">
                        <p style="margin: 0;">Mit freundlichen Grüßen,<br> ihr CollEW-Team</p>
                    </td>
                </tr>
                <!-- end copy -->

            </table>
            <!--[if (gte mso 9)|(IE)]>
            </td>
            </tr>
            </table>
            <![endif]-->
        </td>
    </tr>
    <!-- end copy block -->

    <!-- start footer -->
    <tr>
        <td align="center" bgcolor="#e9ecef" style="padding: 24px;">
            <!--[if (gte mso 9)|(IE)]>
            <table align="center" border="0" cellpadding="0" cellspacing="0" width="600">
                <tr>
                    <td align="center" valign="top" width="600">
            <![endif]-->
            <table border="0" cellpadding="0" cellspacing="0" width="100%" style="max-width: 600px;">

                <!-- start permission -->
                <tr>
                    <td align="center" bgcolor="#e9ecef" style="padding: 12px 24px; font-family: 'Source Sans Pro', Helvetica, Arial, sans-serif; font-size: 14px; line-height: 20px; color: #666;">
                        <p style="margin: 0;">Sie haben diese E-Mail erhalten weil sie Ihren Account verifizieren wollten. Wenn Sie dies nicht wollen können Sie diese E-Mail ignorieren.</p>
                    </td>
                </tr>
                <!-- end permission -->

            </table>
            <!--[if (gte mso 9)|(IE)]>
            </td>
            </tr>
            </table>
            <![endif]-->
        </td>
    </tr>
    <!-- end footer -->

</table>
<!-- end body -->

</body>
</html>