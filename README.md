About the Project:
This project aims to develop an application to track lost and found items within 
the IIT Bombay campus. As of now, lack of a single point of contact makes it 
difficult to find the lost item given the huge size of the campus and unavailability 
of coherent guidelines . Presence of a centralised application will help 
students, faculty or staff to identify and track their lost belongings. 
Also it will help people who find unidentified items, in returning it to their 
respective owners. The key features of this application will include a forum 
where details of lost items as well as found items could be posted after authorisation.
In our project we have implemented our very own lost and found application 
called "Insti-lost and found " where a user can login using his/her LDAP 
credentials and post images, descriptions as well as map location of lost 
as well as found items in different screens.He also has access to a news feed,
from where he can see the list of recently found/lost items.To claim the item
the person can send an email initiated on claim and contact the post owner.



Installation:
1. Download Android Studio from this link https://developer.android.com/studio . 
Please follow the instructions present in the website for based on your operating
system
2. Clone/Download the Git repository from the Gilink in the AndroidStudioProjects folder
3. Download Android SDK version 25 or above
4. Download the extra jar files from this link and place in the folder InstiLostandFound/libs/
5. Run android Studio
6. For the firebase credentials/login please contact us personally, We have
not made the data public for security reasons.


 Browsing the application:
 Here are the various screens and associated activities for browsing the application :
1. MainActivity : Contains the login screen for the user to login via LDAP
credential
2. Navigation : Here the user can navigate the news feed by clicking on
either lost/found buttons.
3. PostLostItem :The user is redirected to post lost item page. Here he/she
can enter the title of the post, description, geographical location as well
as the image which is optional for the lost item
4. PostFoundItem :The user is redirected to post found item page. Here
he/she can enter the title of the post, description, geographical location
as well as the image which is optional for the found item
5. MypostsFoundRv :Here when user clicks on My posts->Found, he can
browse his own found posts , he can also edit or delete the posts
6. MypostsLostRv :Here when user clicks on My posts->Lost, he can
browse his own lost posts , he can also edit or delete the posts




