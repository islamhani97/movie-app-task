## Objective

Android application that previews a list of movies, shows detailed information, supports
searching, and implements caching for performance.

## Functional Requirements

### Movie List (Home Screen)

Fetch movies from TMDB API and display each movie with:

- Title
- Poster image
- Release year

### Movie Details

Open a detail screen when a movie is tapped to show:

- Title
- Backdrop image
- Poster image
- Release year
- Genre(s)
- Rating
- Overview

### Caching

Cache retrieved movie data locally (Room DB), when the app startup load cached data immediately if available while fetch
fresh data in the background. Once the data arrives the UI updated with it.

### Search Feature

Search bar on movie list screen, that enables the user to search movies by title, and show results dynamically while
typing.

### Technology Stack

- Kotlin programming language.
- Clean Architecture with separation of concerns (Domain, Data, Presentation).
- MVI pattern for state management.
- Coroutines & Flow.
- Jetpack Compose for UI.
- Networking: Retrofit
- Efficient caching strategy (paging 3 remote mediator with room).
- Dependency Injection: Hilt
- Unit tests for critical layers (UseCases, ViewModels).
- Version Control:Use Git Flow branching model (feature branches, main, develop).
