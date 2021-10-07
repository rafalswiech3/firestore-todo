# Firestore TODO Android Application

Android application that allows users to manage TODO list. 

Application allows users to add, edit and delete TODOs.

TODOs are displayed on the screen as list.

Data is stored in Firestore Database.

Application displays buttons to retry when error occoured while loading data.

Data is automatically updated when other user edit remote data.

To add TODO user has to click "+" floating button on main screen. User must fill Title (max 30 characters) and Description (max 200 characters). Image URL is optional.

To edit / show TODO click on item on the list.

Te delete TODO long click on item.

# Used tools and technologies:

- Android Studio
- Android SDK
- MVVM Architecture Design Pattern
- Firebase Firestore SDK
- Hilt - Dependency Injection
- RecyclerView - Display data on the list
- Paging 3 - Load paginated data into RecyclerView list
- Glide 4 - Loading images
- Navigation Component - Navigating between fragments
- Parcelable - Send objects between fragments

# Screenshots:

![Screenshot_20211007-201225](https://user-images.githubusercontent.com/23174038/136440552-f3284205-d44d-4162-b6a7-1932a8ced307.png)
