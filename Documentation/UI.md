 ##UI Requirements:  
###Main Windows

####Signup/Login Page
- [ ] Allow users to sign in with Microsoft account, or create a Microsoft account, then proceed to Main Page.

####Main Page (Hard of Hearing)
- [ ] Has 3 buttons (NLP, Video, Physical)
- [ ] Clicking video button starts a video request and takes users to request page
- [ ] Clicking physical button takes users to physical request page
- [ ] Clicking NLP button takes user to the NLP chat window

####Main Page (Interpreter)
- [ ] Has 2 check boxes with buttons
- [ ] Video button toggles the indicator for video interpretation
- [ ] Physical button toggles the indicator for physical interpretation
- [ ] When video indicator is on, then interpreter user will be available to HOH users for video
- [ ] When video indicator is off, then interpreter user will **not** be available to HOH users for video
- [ ] When physical indicator is off, then interpreter user will be available to HOH users for physical interpretation
- [ ] When physical indicator is off, then interpreter user will **not** be available to HOH users for physical interpretation
- [ ] If user closes app. The indicators are still respected, and they be available based on indicators.

####NLP chat page will support Google Voice TTS, keyboard, and scrolling conversation history
- [ ] ???

####Create Requests Page - where Hard of Hearing make request
- [ ] Page shows if HOH users is creating a video or physical request
- [ ] If creating a physical request, then page has a field to set radius from which is look for translators
- [ ] Has subject drop down: Medical, Educational, Confidential, et cetera
- [ ] Has detail text to put more info about the request (150 char max)
- [ ] Has a request now button
- [ ] Error state fragment in case there are no interpreters found
- [ ] On the error state fragment there are three buttons: Change Range (if video), Try Video Chat (if location), and try NLP
- [ ] Clicking on the Change range button goes to Create Request view for physical with the range text box selected
- [ ] Clicking on the Video button takes the user to Create Request view for video
- [ ] Clicking on the NLP button takes the user to the NLP chat page
- [ ] Success state fragment that indicates an interpreter is on the way

####Settings Page
- [ ] Setting page can be accessed from any page.
- [ ] Setting will have a personal photo of the interrupter and the user.
- [ ] Next the photo the settings will have the user first and last name.
- [ ] rofile setting allow to update personal photo and name of user.
- [ ]	Do not disturb setting and Push notifications.
- [ ]	Advanced setting
- [ ]	Location enabled.
- [ ]	Acceptable radios.
- [ ]	Minimum rating.
- [ ]	 Auto timeout.
- [ ]	Change theme (use default vs custom settings)
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
