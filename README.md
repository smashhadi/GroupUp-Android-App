# Social Networking Android Application -  GroupUp

## Introduction
GroupUp is an android application that lets users create groups with other users based on personal interests that show up as tags. Each user is expected to be associated with certain tags which reflect their interests or simply things that they wish to work on. Users can create groups with users they already know or find new acquaintances that share their interest or goal. Once a user is a member of one or more groups, the posts from those groups appear on the user’s homepage which updates the user of recent developments or plans. Members can comment on any post in the group. Users can create a new group or join existing groups.

## Architecture

### Purpose
This section gives an overview of the overall architecture being used in different parts of this project. It begins by providing a Development View of the overall system using UML component diagrams, this enables the reader to have an understanding for both the server-side and client-side architecture of the system. Then the architecture used on the client-side (GroupUp Android app) is detailed and an overview of the publisher-subscriber architecture that supports push notifications on the GroupUp app is presented. Finally, the non-functional properties that our choice of architecture enables us to support is discussed.

### Development View
The development (or implementation) view for the GroupUp application serves the purpose of giving a high-level view of the different components of the system from a developer’s or project manager’s point of view. This section describes the major components of the system, their purpose, and also uses component diagrams to show how these components are integrated with one another. The component diagram itself is decomposed into two diagrams. One diagram shows the components involved on the server side, while the other diagram shows the main components of the GroupUp Android application.

### Server-side View

#### Components:
##### Firebase Auth:
This component represents the authentication service. This component registers a new user, authenticates an already existing user and provides access to the rest of the application.
##### Cloud Firestore DB:
Could Firestore is a scalable, realtime NoSQL database that stores data from across all GroupUp clients. It keeps the data across all GroupUp clients in sync and provides offline support through the Firebase Firestore API.
##### Firebase Storage:
Cloud Firestore is not adequate for storing media files. The Firebase Storage component is therefore used to store actual media files, whereas the location paths for these files is stored in Cloud Firestore DB.
##### Notification Publisher:
The GroupUp Server is a deployed on Firebase Cloud as a Firebase Function implemented in Node.js to handle push notifications. It listens for changes to data on Cloud Firestore DB and sends notifications using Firebase Cloud Messaging (FCM) based on specific events.
##### GroupUp Client:
This component represents the Android client and is explained in the following section.

#### Connectors:
##### Authentication:
The authentication interface (provided by firebase.auth.FirebaseAuth) sends login and new registration requests from GroupUp Client to Firebase Auth.
##### Firestore API:
Interface provided by Firebase to interact with Cloud Firestore DB and the real-time services it provides.
##### Firebase Messaging Service:
Interface to receive notifications on GroupUp client from FCM.
##### FirebaseStorage:
Interface provided by Firebase API to upload/download files.
##### Firebase Admin API:
Interface provided with Firebase Admin SDK to integrate backend service with Firebase Cloud Messaging.

### Client-side View
#### Components:
##### User Profile:
This component is responsible for sending login and registration processes. It is also responsible for handling user’s basic information.
##### Notification Receiver:
This component receives notification intended for the client and relays them to appropriate components.
##### Group Management:
This component is responsible for handling different groups the user is subscribed to  and displaying group invites for the user.
##### Group Posts:
This component is responsible to managing posts and comments within a group.

### User Interface Architecture
For a number of views, the application needed to be able to handle real-time updates from the source of truth. Making sure all clients see the correct information that is held in Cloud Firestore presented a number of challenges:

1. Database updates need to be propagated to all active clients.
2. The client application needs to update its views whenever the database has been updated.
3. Firestore’s API returns its own data types as query responses. These need to be converted into data classes before they can be displayed in the view.

To tackle these challenges GroupUP uses an Event-Listener model combined with a  Model-View-ViewModel (MVVM) architecture and makes use of the Observer pattern.

The client application backend maintains a model of a data. It also listens to the database for updates and updates its model accordingly. The viewmodel observes this data model and transforms it into data types useable by the frontend activity. The Activity in turn listens to the transformed dataset and updates its Views accordingly. The activity also controls the lifecycle of the viewmodel.

Using this approach offers a few advantages. First of all, the application’s dynamic views are now updated in real-time without any need for user input. This approach also reduces the coupling between the application classes and the Cloud Firestore API since all the transformations happen within the ViewModel. This makes it significantly easier to change out the actual database used in the future.

### Push Notifications Architectural Style:
Publish-Subscribe architectural style is used to facilitate one-to-many communication between users in the application. It allows decoupling of business logic from the application and prevents any unauthorized communication between users in the application as the backend server is responsible for conveying messages from one user to another. Push notifications require a backend server for sending messages to devices. 

The application implements pub-sub architecture for sending group invite push notifications. Every registered user of the app is considered a subscriber of group invite notifications.

Push notifications are sent to users based on device tokens. When a user first registers on a device, his/her FirebaseInstanceId device token is stored in the database with other information pertaining to the user. This device token is retained in the database till the user signs out from the device. On successful log-in, device token field in the database is updated to current device.

GroupUp server code is implemented using Firebase Cloud Functions and Firebase Cloud Messaging Service. The Admin FCM API listens for a write event on a Firestore database collection. By default, the user who creates the group is added as a subscriber to the group updates, but other users who are invited to the group are considered pending members. The GroupUp server code queries for the device tokens of these pending members and send the group invite message with the name of the group they are invited to join. These messages are received by the Firebase Cloud Messaging service set up in the application. This service not only receives the message but also builds custom view of the notification visible in the system tray of the device. When a user clicks on the notification, the application pops up to the foreground and the user can then accept or decline. If a user accepts the invites, he/she is subscribed to the group’s updates. 

## Non-Functional Properties:
The main non-functional properties of the systems are authentication, security and consistency.
### Authentication:
GroupUP uses Firebase Authentication Service provided by Firebase to authenticate users. The user provides email/password credentials to setup and to access his account. The app puts a constraint on the minimum length of the user’s password (the password should have at least 6 letters) to keep the passwords complex. More constraints such as inclusion on a number and/or special characters can be enforced easily on the existing system.   
### Security:
The first aspect of security is to keep the user’s account secure and thereby to keep user’s passwords secure.Firebase Authentication saves the user’s password as hash-codes. The passwords aren’t stored anywhere else in the database. Thus, the passwords are inaccessible to everyone, even to the developers.If a user forgets his password, he can request for a link to reset his password. This link will be sent to his registered email address. The app also makes sure that once the account is set up, the user cannot change his email. This makes resetting the password secure.
The second aspect is to secure the communication with the database. All the data to and from the Firestore are encrypted and sent over HTTPS connections. This keeps data transactions safe.
### Consistency:
GroupUP supports real-time updates for posts and comments. The app uses MVVM architecture to receive real-time updates without the need for refreshing the app.

## Design
### Purpose
The purpose of this section is to describe the design of the GroupUp application by providing a Logical View. It also explains how the GroupUp android application makes use of the MVVM pattern to achieve dynamic UI.
### Model-View-ViewModel
As mentioned, GroupUP uses a Model-View-ViewModel (MVVM) architecture with the observer pattern for dynamic user interfaces. Every page which handles and displays dynamic information is backed by a ViewModel. These ViewModels are implemented by subclassing from “android.arch.lifecycle.ViewModel” as shown in the UML diagram below. Doing so allows instantiating the ViewModel for a given activity using the factory methods provided by “android.arch.lifecycle.ViewModelProviders”. The lifecycle of a ViewModel is tied to that of its activity which owns the View.

Within the ViewModels, the data classes are packaged as instances of “android.arch.lifecycle.LiveData<T>”. This class is observable - it provides an “observe” method which can accept an “Observer”. Said observer is notified whenever the value held by the LiveData instance changes.

The two subclasses of “LiveData<T>”, FirebaseQueryLiveData and FirebaseDocumentLiveData use the listener pattern to watch the external database for updates. FirebaseQueryLiveData uses an event listener to see if the result of a given query has changed. Similarly, FirebaseDocumentLiveData watches a single document. In this way, if the watched information is changed, the data held by instances of these classes changes accordingly and any observers are notified. The values held by these classes are Firebase internals types, e.g. query snapshots and document snapshots. This means that the data needs to be transformed before it can be consumed by the view.

This is done in the ViewModels. They transform the data given to them into types ready for consumption at the frontend. For example, a query snapshot of the “Groups” collection will be transformed by the ViewModel into a “List<Group>”. This would be packaged into a “LiveData<List<Group>>” so that it can be observed the user interface code. The frontend will add an observer to this LiveData instance to watch for changes and update the UI accordingly.

### Logical View
This section gives the logical view of the system. It describes the static relationships between classes that make up each component in our system through UML class diagrams and the interactions among them ithrough  UML sequence diagrams.
#### UML Class Diagrams
The class diagrams for GroupUp android application are broken down into different parts so that it’s easier to understand. A short explanation is provided with each part as required.
User Profile
The classes shown in this section are responsible for providing the functionality of signup, login, setting up and editing a user’s profile information. UserProfileFragment inherits from Fragment class (provided by Android SDK). Each activity in Android can host multiple fragments. In the GroupUp application, fragments are used to make the NavigationActivity (the main activity of the application) into a multi-pane UI activity. 
Main Navigation
This class diagram shows the “main activity” represented by NavigationActivity of the application. This activity has tabs for Feed, Groups, Search, User Profile and Notification, each of which is represented by its Fragment class, e.g. SearchFragment represents the Search tab on NavigationActivity. Each of these Fragments are responsible for providing separate functionalities provided by their corresponding tabs.

Feed Tab: shows posts from different posts the user is subscribed to.

Groups Tab: shows the groups the user is subscribed to.

Search Tab: provides functionality to search for other users who are interested in similar topics by searching on tags.

UserProfile Tab: shows the user’s profile page.

Notification Tab: shows notifications received by the user.

Group Management
The classes shown in this section of the class diagram are responsible for providing the functionality related to groups such as creating a new group, adding a new member to a group, leave a group and the  option to accept or reject an invitation to join a group.

Posts & Comments
The classes shown by this section of the class diagram represent the functionality of adding posts to a group and adding comments to a post.

## Support for future changes:
### Support for more notifications:
In future, the app might need to support notifications for new posts and comments. Since the observer pattern is used to achieve Pub-Sub models, we can attach more observers, for posts and comments easily, with the database and handle notifications. The same model class which is being used for the invite push notifications, Notification.java, can be reused to handle the notifications on the client side.

### Support for events:
The app can have an option to post events within a group. The purpose of the app is to make groups based on interests, hence, the users of a group might be interested in knowing about events related to the common interest of the group. A support for “Interested In” (user to show that he is interested in an event) can also be implemented.

To start with, a new collection named Events needs to be added to the database, which will contain metadata for the event, the groupID of the group the event is related with and a list of userIDs of the users interested in this event.

To support “Interested In” functionality, a new java model for Events can be created. The usual flow in this case would be to first fetch groupIDs from the Groups collection for the current user’s groups, and then using those groupIDs and the current user,s userID, fetch events from the Events collections. Since the groupIDs in MainViewModel.java keeps track of the current user’s groups in real time, fetching events for the current user does not require querying the database to get the groups the user is a member of. A new listener, on the Events collection, can be added for the groups enlisted in groupIDs (this will fetch all the events the user is interested with and are in one of the user’s subscribed groups in real-time). This provides decoupling between the database and the database listeners.

Each of the activities supported by a group(comments and posts) a separate ViewModel is used. Here, the separation of concerns makes it easy to add another ViewModel for the Events which can be used to fetch events for a group in real-time.     

### Support for scalability:
Firestore scales automatically as the number of connections increases. No data sharding is required to support scaling.

## Glossary
This section provides some definitions and explanations for the terms and concepts used in this document and project.

Activity
The Activity class is a component of an android app. An android app that has multiple screens comprises of multiple activities. Each of these activities inherits from the Activity class to implement some functionality and draws its own user interface.

Fragment
A Fragment represents a portion of the user interface of a FragmentActivity. Multiple Fragments can be combined in a single activity to build a multi-pane UI. Fragments can also be reused in multiple activities. Each fragment can define its own behavior but its lifecycle is directly affected by the host activity’s lifecycle.

Adapter
An Adapter for any view on the UI acts as a bridge between that view and the data that is required to create it. E.g. A PostAdapter contains a list of all posts and is responsible for creating a view on the UI for each post in the list.


*Created as course project for ECE651- Foundations of Software Engineering in a team of 5*
