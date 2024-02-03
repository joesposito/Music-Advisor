
# Music Advisor

A command line program that connects to Spotify's API to deliver up-to-date data to users in a friendly fashion.

This application utilizes Spotify's OAuth2 authorization code flow to obtain user access and display a variety of data by making API calls using Java 11's HttpClient library.

In addition I configured a MySQL database to persist users' refresh tokens and access codes, so that the number of authorizations, and thus API calls, can be reduced.

Users can sign in using a login feature and their data will be imported, no longer necessitating a repetition of the authorization process. 

The app also works without a database, users will just have to reauthenticate every time.

Below you can find a video demonstration:

![](https://github.com/joesposito/MusicAdvisor/blob/master/MusicAdvisorGIF.gif)

## Features
- menu - provides a menu of the options
- login - user can login by typing "login 'your username'"
- auth - user can authorize the application to retrieve Spotify data
- featured (auth required) - displays a list of featured playlists
- new (auth required) - displays a list of albums that are new releases
- categories (auth required) - displays a list of categories
- playlists (auth required) - user can display a list of playlists for a category by typing "playlists 'your category here'"
    - for a complete list of categories, use the "categories" command
- next - displays the next page of active content being shown
- prev - displays the previous page of active content being shown

## How To
To use the program, download the files and go into the SpotifyConfig.java class and enter your Spotify developer "Client ID" and "Client Secret." For more information on requesting these, see https://developer.spotify.com/.

## MySQL Connectivity
You can also go connect this program to a MySQL database to store refresh tokens that are used to get access tokens. Along with this, you will be able to eliminate the necessity of having to authorize Spotify during every startup.

To do this, go to DBConfig.java and enter in your database details.
## Run Locally

Clone the project

```bash
  git clone https://github.com/joesposito/MusicAdvisor
```

Go to the project directory

```bash
  cd my-project
```

Run the program

```bash
  gradlew build
```

