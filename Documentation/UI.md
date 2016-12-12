 ##UI Requirements:  
###Main Windows

####Signup/Login Page
- [x] Allow users to sign in with Email account and password, or create an ASL Buddy account with Email and password, then proceed to Main Page.
- [ ] Query the database and match the hashed password and salt function with the client-side input.  New hash password and salt function when signing up.

####Main Page (Hard of Hearing)
- [x] Has 3 buttons (Hearing Tool, Video, Physical)
- [x] Clicking video button takes the user to CreateRequest page, passing video param
- [x] Clicking physical button takes users to CreateRequest page, passing physical as param
- [x] Clicking Hearing Tool button takes user to the Hearing tool chat window

####Main Page (Interpreter)
- [x] Has 2 check boxes with buttons
- [x] Video button toggles the indicator for video interpretation
- [x] Physical button toggles the indicator for physical interpretation
- [x] When video indicator is on, then interpreter user will be available to HOH users for video
- [x] When video indicator is off, then interpreter user will **not** be available to HOH users for video
- [x] When physical indicator is off, then interpreter user will be available to HOH users for physical interpretation
- [x] When physical indicator is off, then interpreter user will **not** be available to HOH users for physical interpretation
- [x] If user closes app. The indicators are still respected, and they be available based on indicators.
- [x] Skype status is displayed, whether or not this interpreter has linked their Skype account with their ASL buddy account.

####Hearing Tool chat page will support Google Voice TTS, keyboard, and scrolling conversation history
- [x] Will support recording Audio through device's recorder.
- [x] Will translate that recorded audio into text using the Google API.
- [x] Will accept typed responses from the HOH user to communicate back.
- [x] Will speak the typed responses from HOH user through the device's speakers.
- [x] Will support scrolling conversation history.

####Create Requests Page - where Hard of Hearing make request
- [x] Page shows if HOH users is creating a video or physical request
- [x] If creating a physical request, then page has a field to set radius from which is look for translators
- [x] Has subject drop down: Medical, Educational, Confidential, et cetera
- [x] Has detail text to put more info about the request (150 char max)
- [x] Has a request now button
- [x] Error state fragment in case there are no interpreters found
- [x] On the error state fragment there are three buttons: Change Range (if video), Try Video Chat (if location), and try Hearing Tool
- [x] Clicking on the Change range button goes to Create Request view for physical with the range text box selected
- [x] Clicking on the Video button takes the user to Create Request view for video
- [x] Clicking on the Hearing Tool button takes the user to the Hearing Tool chat page
- [ ] Success state fragment that indicates an interpreter is on the way.
~~- [ ] Skype status is displayed, whether or not this interpreter has linked their Skype account with their ASL buddy account.~~

####Settings Page
- [ ] Setting page can be accessed from any page.
- [ ] Setting will have a personal photo of the interrupter and the user.
- [ ] Next the photo the settings will have the user first and last name.
- [ ] profile setting allow to update personal photo and name of user.
- [ ] Do not disturb setting and Push notifications.
- [ ] Location enabled.
- [ ] Acceptable radius.
- [ ] Minimum rating.
- [ ] Auto timeout.
- [ ] Configure Skype from settings page by verifying they have Skype installed, verifying they are signed into Skype, and storing their Skype username.
- [ ] Change theme (use default vs custom settings)
- [ ]	Captions > language , text size , white/black
- [ ]	About > app version, send feedback, help(Q&A) and developer info

####View Request Page - This is for translators.
- [ ] Clicking location or physical notification takes user to this page
- [ ] Shows HOH profile pic
- [ ] Shows HOH user name
- [ ] Shows Type of request: Physical/Video
- [ ] If location request, then shows location
- [ ] If video request, then does NOT show location
- [ ] Shows request subject (medical, confidential, educational, et cetera)
- [ ] Shows request details. Limited to 150 chars.
- [ ] Page has accept and deny buttons
- [ ] If video request, then clicking ACCEPT takes user to video chat with HOH user
- [ ] If physical request, then clicking ACCEPT takes user to video chat with HOH user
- [ ] Click ACCEPT also notifies HOH users that request is accepted
- [ ] Clicking DENY dismisses the notification and HOH user is notified of rejection?

###3 push notifications:

####Request for translation notification
- [ ] notification has icon for app
- [ ] When translator gets a physical or video request, then they get this request notification
- [ ] Notification has short text that tells translator they have a pending request.
- [ ] Clicking notification starts app and takes translator to View Request page for the request.

####Video request accepted for Hard of Hearing user  
- [ ] notification has icon for app
- [ ] notification lets user know that interpreter is ready for video
- [ ] Clicking notification takes user to video chat
- [ ] Dismissing notification?

####Physical Interpreter on the way (no buttons necessary
- [ ] Notification has icon for app
- [ ] Notification lets user know that interpreter is on their way
- [ ] Clicking notification takes user to???
- [ ] Dismissing notification?
