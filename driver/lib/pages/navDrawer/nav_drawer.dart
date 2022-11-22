import 'package:flutter/material.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:tagyourtaxi_driver/functions/functions.dart';
import 'package:tagyourtaxi_driver/pages/NavigatorPages/about.dart';
import 'package:tagyourtaxi_driver/pages/NavigatorPages/bankdetails.dart';
import 'package:tagyourtaxi_driver/pages/NavigatorPages/driverdetails.dart';
import 'package:tagyourtaxi_driver/pages/NavigatorPages/driverearnings.dart';
import 'package:tagyourtaxi_driver/pages/NavigatorPages/editprofile.dart';
import 'package:tagyourtaxi_driver/pages/NavigatorPages/faq.dart';
import 'package:tagyourtaxi_driver/pages/NavigatorPages/history.dart';
import 'package:tagyourtaxi_driver/pages/NavigatorPages/makecomplaint.dart';
import 'package:tagyourtaxi_driver/pages/NavigatorPages/referral.dart';
import 'package:tagyourtaxi_driver/pages/NavigatorPages/selectlanguage.dart';
import 'package:tagyourtaxi_driver/pages/NavigatorPages/sos.dart';
import 'package:tagyourtaxi_driver/pages/NavigatorPages/updatevehicle.dart';
import 'package:tagyourtaxi_driver/pages/NavigatorPages/walletpage.dart';
import 'package:tagyourtaxi_driver/pages/onTripPage/map_page.dart';
import 'package:tagyourtaxi_driver/pages/vehicleInformations/upload_docs.dart';
import 'package:tagyourtaxi_driver/styles/styles.dart';
import 'package:tagyourtaxi_driver/translation/translation.dart';

import '../NavigatorPages/fleetdetails.dart';
import '../NavigatorPages/managevehicles.dart';

class NavDrawer extends StatefulWidget {
  const NavDrawer({Key? key}) : super(key: key);
  @override
  State<NavDrawer> createState() => _NavDrawerState();
}

class _NavDrawerState extends State<NavDrawer> {
  @override
  Widget build(BuildContext context) {
    var media = MediaQuery.of(context).size;
    return Container(
      color: page,
      width: media.width * 0.8,
      child: Directionality(
        textDirection: (languageDirection == 'rtl')
            ? TextDirection.rtl
            : TextDirection.ltr,
        child: Drawer(
            child: SizedBox(
          width: media.width * 0.7,
          child: SingleChildScrollView(
            child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  SizedBox(
                    height:
                        media.width * 0.05 + MediaQuery.of(context).padding.top,
                  ),
                  SizedBox(
                    width: media.width * 0.7,
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.start,
                      children: [
                        Container(
                          height: media.width * 0.2,
                          width: media.width * 0.2,
                          decoration: BoxDecoration(
                              shape: BoxShape.circle,
                              color: Colors.grey,
                              image: DecorationImage(
                                  image: NetworkImage(
                                      userDetails['profile_picture']
                                          .toString()),
                                  fit: BoxFit.cover)),
                        ),
                        SizedBox(
                          width: media.width * 0.025,
                        ),
                        Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            SizedBox(
                              width: media.width * 0.45,
                              child: Row(
                                mainAxisAlignment:
                                    MainAxisAlignment.spaceBetween,
                                children: [
                                  SizedBox(
                                    width: media.width * 0.3,
                                    child: Text(
                                      userDetails['name'],
                                      style: GoogleFonts.roboto(
                                          fontSize: media.width * eighteen,
                                          color: textColor,
                                          fontWeight: FontWeight.w600),
                                      maxLines: 1,
                                    ),
                                  ),
                                  //edit profile
                                  InkWell(
                                    onTap: () async {
                                      var val = await Navigator.push(
                                          context,
                                          MaterialPageRoute(
                                              builder: (context) =>
                                                  const EditProfile()));
                                      if (val != null) {
                                        if (val) {
                                          setState(() {});
                                        }
                                      }
                                    },
                                    child: Icon(
                                      Icons.edit,
                                      size: media.width * eighteen,
                                    ),
                                  )
                                ],
                              ),
                            ),
                            SizedBox(
                              height: media.width * 0.01,
                            ),
                            SizedBox(
                              width: media.width * 0.45,
                              child: Text(
                                userDetails['email'],
                                style: GoogleFonts.roboto(
                                    fontSize: media.width * fourteen,
                                    color: textColor),
                                maxLines: 1,
                              ),
                            )
                          ],
                        )
                      ],
                    ),
                  ),
                  Container(
                    padding: EdgeInsets.only(top: media.width * 0.02),
                    width: media.width * 0.7,
                    child: Column(
                      children: [
                        //history
                        InkWell(
                          onTap: () {
                            Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) => const History()));
                          },
                          child: Container(
                            padding: EdgeInsets.all(media.width * 0.025),
                            child: Row(
                              children: [
                                Image.asset(
                                  'assets/images/history.png',
                                  fit: BoxFit.contain,
                                  width: media.width * 0.075,
                                ),
                                SizedBox(
                                  width: media.width * 0.025,
                                ),
                                SizedBox(
                                  width: media.width * 0.55,
                                  child: Text(
                                    languages[choosenLanguage]
                                        ['text_enable_history'],
                                    overflow: TextOverflow.ellipsis,
                                    style: GoogleFonts.roboto(
                                        fontSize: media.width * sixteen,
                                        color: textColor),
                                  ),
                                )
                              ],
                            ),
                          ),
                        ),

                        //wallet
                        userDetails['owner_id'] == null
                            ? InkWell(
                                onTap: () {
                                  Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                          builder: (context) =>
                                              const WalletPage()));
                                },
                                child: Container(
                                  padding: EdgeInsets.all(media.width * 0.025),
                                  child: Row(
                                    children: [
                                      Image.asset(
                                        'assets/images/walletImage.png',
                                        fit: BoxFit.contain,
                                        width: media.width * 0.075,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.025,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.55,
                                        child: Text(
                                          languages[choosenLanguage]
                                              ['text_enable_wallet'],
                                          overflow: TextOverflow.ellipsis,
                                          style: GoogleFonts.roboto(
                                              fontSize: media.width * sixteen,
                                              color: textColor),
                                        ),
                                      )
                                    ],
                                  ),
                                ),
                              )
                            : Container(),

                        //referral
                        userDetails['owner_id'] == null &&
                                userDetails['role'] == 'driver'
                            ? InkWell(
                                onTap: () {
                                  Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                          builder: (context) =>
                                              const ReferralPage()));
                                },
                                child: Container(
                                  padding: EdgeInsets.all(media.width * 0.025),
                                  child: Row(
                                    children: [
                                      Image.asset(
                                        'assets/images/referral.png',
                                        fit: BoxFit.contain,
                                        width: media.width * 0.075,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.025,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.55,
                                        child: Text(
                                          languages[choosenLanguage]
                                              ['text_enable_referal'],
                                          overflow: TextOverflow.ellipsis,
                                          style: GoogleFonts.roboto(
                                              fontSize: media.width * sixteen,
                                              color: textColor),
                                        ),
                                      )
                                    ],
                                  ),
                                ),
                              )
                            : Container(),

                        //manage vehicle

                        userDetails['role'] == 'owner'
                            ? InkWell(
                                onTap: () {
                                  Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                          builder: (context) =>
                                              const ManageVehicles()));
                                },
                                child: Container(
                                  padding: EdgeInsets.all(media.width * 0.025),
                                  child: Row(
                                    children: [
                                      Image.asset(
                                        'assets/images/updateVehicleInfo.png',
                                        fit: BoxFit.contain,
                                        width: media.width * 0.075,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.025,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.55,
                                        child: Text(
                                          languages[choosenLanguage]
                                              ['text_manage_vehicle'],
                                          overflow: TextOverflow.ellipsis,
                                          style: GoogleFonts.roboto(
                                              fontSize: media.width * sixteen,
                                              color: textColor),
                                        ),
                                      )
                                    ],
                                  ),
                                ),
                              )
                            : Container(),

                        //manage Driver

                        userDetails['role'] == 'owner'
                            ? InkWell(
                                onTap: () {
                                  Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                          builder: (context) =>
                                              const DriverList()));
                                },
                                child: Container(
                                  padding: EdgeInsets.all(media.width * 0.025),
                                  child: Row(
                                    children: [
                                      Image.asset(
                                        'assets/images/managedriver.png',
                                        fit: BoxFit.contain,
                                        width: media.width * 0.075,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.025,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.55,
                                        child: Text(
                                          languages[choosenLanguage]
                                              ['text_manage_drivers'],
                                          overflow: TextOverflow.ellipsis,
                                          style: GoogleFonts.roboto(
                                              fontSize: media.width * sixteen,
                                              color: textColor),
                                        ),
                                      )
                                    ],
                                  ),
                                ),
                              )
                            : Container(),
                        //update vehicles

                        userDetails['owner_id'] == null &&
                                userDetails['role'] == 'driver'
                            ? InkWell(
                                onTap: () {
                                  Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                          builder: (context) =>
                                              const UpdateVehicle()));
                                },
                                child: Container(
                                  padding: EdgeInsets.all(media.width * 0.025),
                                  child: Row(
                                    children: [
                                      Image.asset(
                                        'assets/images/updateVehicleInfo.png',
                                        fit: BoxFit.contain,
                                        width: media.width * 0.075,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.025,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.55,
                                        child: Text(
                                          languages[choosenLanguage]
                                              ['text_updateVehicle'],
                                          overflow: TextOverflow.ellipsis,
                                          style: GoogleFonts.roboto(
                                              fontSize: media.width * sixteen,
                                              color: textColor),
                                        ),
                                      )
                                    ],
                                  ),
                                ),
                              )
                            : Container(),
//fleet details
                        userDetails['owner_id'] != null &&
                                userDetails['role'] == 'driver'
                            ? InkWell(
                                onTap: () {
                                  Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                          builder: (context) =>
                                              const FleetDetails()));
                                },
                                child: Container(
                                  padding: EdgeInsets.all(media.width * 0.025),
                                  child: Row(
                                    children: [
                                      Image.asset(
                                        'assets/images/updateVehicleInfo.png',
                                        fit: BoxFit.contain,
                                        width: media.width * 0.075,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.025,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.55,
                                        child: Text(
                                          languages[choosenLanguage]
                                              ['text_fleet_details'],
                                          overflow: TextOverflow.ellipsis,
                                          style: GoogleFonts.roboto(
                                              fontSize: media.width * sixteen,
                                              color: textColor),
                                        ),
                                      )
                                    ],
                                  ),
                                ),
                              )
                            : Container(),
                        //earnings
                        userDetails['owner_id'] == null
                            ? InkWell(
                                onTap: () {
                                  Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                          builder: (context) =>
                                              const DriverEarnings()));
                                },
                                child: Container(
                                  padding: EdgeInsets.all(media.width * 0.025),
                                  child: Row(
                                    children: [
                                      Image.asset(
                                        'assets/images/Earnings.png',
                                        fit: BoxFit.contain,
                                        width: media.width * 0.075,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.025,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.55,
                                        child: Text(
                                          languages[choosenLanguage]
                                              ['text_earnings'],
                                          overflow: TextOverflow.ellipsis,
                                          style: GoogleFonts.roboto(
                                              fontSize: media.width * sixteen,
                                              color: textColor),
                                        ),
                                      )
                                    ],
                                  ),
                                ),
                              )
                            : Container(),

                        //documents
                        InkWell(
                          onTap: () async {
                            var nav = await Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) => Docs(
                                          fromPage: '2',
                                        )));
                            if (nav) {
                              setState(() {});
                            }
                          },
                          child: Container(
                            padding: EdgeInsets.all(media.width * 0.025),
                            child: Row(
                              children: [
                                Image.asset(
                                  'assets/images/manageDocuments.png',
                                  fit: BoxFit.contain,
                                  width: media.width * 0.075,
                                ),
                                SizedBox(
                                  width: media.width * 0.025,
                                ),
                                SizedBox(
                                  width: media.width * 0.55,
                                  child: Text(
                                    languages[choosenLanguage]
                                        ['text_manage_docs'],
                                    overflow: TextOverflow.ellipsis,
                                    style: GoogleFonts.roboto(
                                        fontSize: media.width * sixteen,
                                        color: textColor),
                                  ),
                                )
                              ],
                            ),
                          ),
                        ),

                        //faq
                        InkWell(
                          onTap: () {
                            Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) => const Faq()));
                          },
                          child: Container(
                            padding: EdgeInsets.all(media.width * 0.025),
                            child: Row(
                              children: [
                                Image.asset(
                                  'assets/images/faq.png',
                                  fit: BoxFit.contain,
                                  width: media.width * 0.075,
                                ),
                                SizedBox(
                                  width: media.width * 0.025,
                                ),
                                SizedBox(
                                  width: media.width * 0.55,
                                  child: Text(
                                    languages[choosenLanguage]['text_faq'],
                                    overflow: TextOverflow.ellipsis,
                                    style: GoogleFonts.roboto(
                                        fontSize: media.width * sixteen,
                                        color: textColor),
                                  ),
                                )
                              ],
                            ),
                          ),
                        ),

                        //sos
                        userDetails['role'] != 'owner'
                            ? InkWell(
                                onTap: () async {
                                  var nav = await Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                          builder: (context) => const Sos()));

                                  if (nav) {
                                    setState(() {});
                                  }
                                },
                                child: Container(
                                  padding: EdgeInsets.all(media.width * 0.025),
                                  child: Row(
                                    children: [
                                      Image.asset(
                                        'assets/images/sos.png',
                                        fit: BoxFit.contain,
                                        width: media.width * 0.075,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.025,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.55,
                                        child: Text(
                                          languages[choosenLanguage]
                                              ['text_sos'],
                                          overflow: TextOverflow.ellipsis,
                                          style: GoogleFonts.roboto(
                                              fontSize: media.width * sixteen,
                                              color: textColor),
                                        ),
                                      )
                                    ],
                                  ),
                                ),
                              )
                            : Container(),

                        //language
                        InkWell(
                          onTap: () async {
                            var nav = await Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) =>
                                        const SelectLanguage()));
                            if (nav) {
                              setState(() {});
                            }
                          },
                          child: Container(
                            padding: EdgeInsets.all(media.width * 0.025),
                            child: Row(
                              children: [
                                Image.asset(
                                  'assets/images/changeLanguage.png',
                                  fit: BoxFit.contain,
                                  width: media.width * 0.075,
                                ),
                                SizedBox(
                                  width: media.width * 0.025,
                                ),
                                SizedBox(
                                  width: media.width * 0.55,
                                  child: Text(
                                    languages[choosenLanguage]
                                        ['text_change_language'],
                                    overflow: TextOverflow.ellipsis,
                                    style: GoogleFonts.roboto(
                                        fontSize: media.width * sixteen,
                                        color: textColor),
                                  ),
                                )
                              ],
                            ),
                          ),
                        ),

                        //bank details
                        userDetails['owner_id'] == null
                            ? InkWell(
                                onTap: () {
                                  Navigator.push(
                                      context,
                                      MaterialPageRoute(
                                          builder: (context) =>
                                              const BankDetails()));
                                },
                                child: Container(
                                  padding: EdgeInsets.all(media.width * 0.025),
                                  child: Row(
                                    children: [
                                      Icon(Icons.account_balance,
                                          size: media.width * 0.075),
                                      SizedBox(
                                        width: media.width * 0.025,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.55,
                                        child: Text(
                                          languages[choosenLanguage]
                                              ['text_updateBank'],
                                          overflow: TextOverflow.ellipsis,
                                          style: GoogleFonts.roboto(
                                              fontSize: media.width * sixteen,
                                              color: textColor),
                                        ),
                                      )
                                    ],
                                  ),
                                ),
                              )
                            : Container(),
                        //make complaints
                        InkWell(
                          onTap: () async {
                            var nav = await Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) => MakeComplaint(
                                          fromPage: 0,
                                        )));
                            if (nav) {
                              setState(() {});
                            }
                          },
                          child: Container(
                            padding: EdgeInsets.all(media.width * 0.025),
                            child: Row(
                              children: [
                                Image.asset(
                                  'assets/images/makecomplaint.png',
                                  fit: BoxFit.contain,
                                  width: media.width * 0.075,
                                ),
                                SizedBox(
                                  width: media.width * 0.025,
                                ),
                                SizedBox(
                                  width: media.width * 0.55,
                                  child: Text(
                                    languages[choosenLanguage]
                                        ['text_make_complaints'],
                                    overflow: TextOverflow.ellipsis,
                                    style: GoogleFonts.roboto(
                                        fontSize: media.width * sixteen,
                                        color: textColor),
                                  ),
                                )
                              ],
                            ),
                          ),
                        ),
                        //about
                        InkWell(
                          onTap: () {
                            Navigator.push(
                                context,
                                MaterialPageRoute(
                                    builder: (context) => const About()));
                          },
                          child: Container(
                            padding: EdgeInsets.all(media.width * 0.025),
                            child: Row(
                              children: [
                                Icon(Icons.info_outline,
                                    size: media.width * 0.075),
                                SizedBox(
                                  width: media.width * 0.025,
                                ),
                                SizedBox(
                                  width: media.width * 0.55,
                                  child: Text(
                                    languages[choosenLanguage]['text_about'],
                                    overflow: TextOverflow.ellipsis,
                                    style: GoogleFonts.roboto(
                                        fontSize: media.width * sixteen,
                                        color: textColor),
                                  ),
                                )
                              ],
                            ),
                          ),
                        ),

                        //delete account
                        userDetails['owner_id'] == null
                            ? InkWell(
                                onTap: () {
                                  setState(() {
                                    deleteAccount = true;
                                  });
                                  valueNotifierHome.incrementNotifier();
                                  Navigator.pop(context);
                                },
                                child: Container(
                                  padding: EdgeInsets.all(media.width * 0.025),
                                  child: Row(
                                    children: [
                                      Icon(
                                        Icons.delete_forever,
                                        size: media.width * 0.075,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.025,
                                      ),
                                      SizedBox(
                                        width: media.width * 0.55,
                                        child: Text(
                                          languages[choosenLanguage]
                                              ['text_delete_account'],
                                          overflow: TextOverflow.ellipsis,
                                          style: GoogleFonts.roboto(
                                              fontSize: media.width * sixteen,
                                              color: textColor),
                                        ),
                                      )
                                    ],
                                  ),
                                ),
                              )
                            : Container(),

                        //logout
                        InkWell(
                          onTap: () {
                            setState(() {
                              logout = true;
                            });
                            valueNotifierHome.incrementNotifier();
                            Navigator.pop(context);
                          },
                          child: Container(
                            padding: EdgeInsets.all(media.width * 0.025),
                            child: Row(
                              children: [
                                Image.asset(
                                  'assets/images/logout.png',
                                  fit: BoxFit.contain,
                                  width: media.width * 0.075,
                                ),
                                SizedBox(
                                  width: media.width * 0.025,
                                ),
                                SizedBox(
                                  width: media.width * 0.55,
                                  child: Text(
                                    languages[choosenLanguage]['text_logout'],
                                    overflow: TextOverflow.ellipsis,
                                    style: GoogleFonts.roboto(
                                        fontSize: media.width * sixteen,
                                        color: textColor),
                                  ),
                                )
                              ],
                            ),
                          ),
                        )
                      ],
                    ),
                  )
                ]),
          ),
        )),
      ),
    );
  }
}
