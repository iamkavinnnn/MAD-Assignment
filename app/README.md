# Movie Lookup App

COMP2008 Mobile Application Development, Assignment Part A 
Group 12: Kavin Shanmugam and Sachin Senaratne

An Android app (Kotlin, Jetpack Compose, MVVM) for keeping a movie watchlist and logging what you have watched.

---

## App description

Users: Casual film fans who currently keep "movies to watch" in notes apps, screenshots or group chats, and forget what they have already seen or whether they liked it.

Core task: Save a movie to a watchlist, then log a viewing of it: the date watched, a rating out of 5 and a short note.

Data: Two related Room tables. `watchlist_items` holds one row per saved movie. `viewings` holds one row per time a movie was watched, linked by `movieId` (one movie, many viewings). Each movie's status, viewing count and average rating are calculated from its viewings. DataStore saves the watchlist sort order. Movie information comes from the TMDB API.

Out of scope: ser accounts and cloud sync. Showing where a movie can be streamed.

---

## Build steps

**Requirements**
- Android Studio: Any Version after Android Studio Quail 2 | 2026.1.2 Patch 1
- JDK: 9.5.0
- An emulator or phone running Android 7.0 (API 24) or newer
- A free TMDB API key (This will be given from us and do not have to be created by whoever is marking this)

**Steps**
1. Extract the ZIP and open the project folder in Android Studio.
2. Get a TMDB API key: 
   - Included in this project already.
   - If you want to create your own key. create an account at https://www.themoviedb.org, then go to **Settings -> API** and request a Developer key. Copy the short **API Key**(Do not copy the long "Read Access Token"). 
3. Open `local.properties` in the project root, and add this line with no quotes or spaces:
   ```
   TMDB_API_KEY=your_api_key_here
   ```
   This file is not committed or submitted, so the key stays private.
4. Click **File -> Sync Project with Gradle Files**.
5. Click **Build -> Rebuild Project**.

---

## Run steps

1. Start an emulator, or connect a phone with USB debugging turned on.
2. Select the **app** run configuration and press **Run**.
3. The app opens on the **Trending** tab. Use the bottom bar to switch between **Trending**, **Search** and **Watchlist**.

---

## Demonstrating the network features

| What to show | How |
|---|---|
| Loading and success | Open the app. Trending shows a spinner, then a grid of movies from TMDB. |
| Empty state | On **Search**, type `zzqxv` and tap **Search**. The app shows "No movies found". |
| Error and Retry | Turn on aeroplane mode, then tap **Retry** on Trending, or search for anything. An error message and **Retry** button appear. Turn the network back on and tap **Retry**. |
| Offline use | Save a movie and log a viewing. Turn on aeroplane mode. **Watchlist** still works, logging and editing viewings still works, and opening a saved movie shows its details with an "offline" note. |
| Bad data | Search for an obscure title. Movies with no poster show a placeholder, and missing dates show as "Unknown", with no crash. |
| Saved data | Add movies and viewings, change the sort order, then close the app completely and reopen it. Everything is still there in the same order. |
| Rotation | Start filling in **Log Viewing**, rotate the device, and the typed values are kept. |

## Demonstrating the device feature 

The app shares a movie with another app using Android's share sheet.

1. Open any movie and tap "Share with a friend".
2. Pick an app, such as Messages or Gmail. The message says "Watch this with me: [title] ([year]) ...".
3. **Cancel:** open the share sheet again and press Back without choosing an app. You return to the app with nothing changed.

Sharing needs no permissions. If no app can receive the share, a short message appears instead of crashing.

---

## External Data and libraries Used

- Movie data from [TMDB](https://www.themoviedb.org/). This product uses the TMDB API but is not endorsed or certified by TMDB.
- Libraries: Jetpack Compose, Navigation Compose, Room, DataStore, Retrofit, kotlinx.serialization, Coil.