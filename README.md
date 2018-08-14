# Android-MVVM-RxJava2-Dagger2

This repository contains a detailed sample application that uses MVVM as its presentation layer pattern. **The app aims to be extremely flexible to creating variants for automated and manual testing.** Also, the project implements and follows the guidelines presented in Google Sample [MVVM+RXJAVA-android](https://github.com/googlesamples/android-architecture/tree/dev-todo-mvvm-rxjava/).

Essential dependencies are Dagger2 with Dagger-android, RxJava2 with RxAndroid, Room, Retrofit and Espresso. Other noteworthy dependencies would be Mockito, Chrome CustomTabs and Guava.
## App Demo
This app displays latest earthquakes from all around the world. A number of quakes is fetched realtime upon request. If offline, the app displays the most recent loaded quakes.

![content](https://github.com/catalinghita8/android-mvvm-rxjava-dagger2/blob/master/readme_pics/scrolling.gif)
![content](https://github.com/catalinghita8/android-mvvm-rxjava-dagger2/blob/master/readme_pics/forcing_update.gif)
![content](https://github.com/catalinghita8/android-mvvm-rxjava-dagger2/blob/master/readme_pics/open_tab.gif)
## Presentation Layer
MVVM pattern is integrated to facilitate testing and to allow separating the user interface logic from business logic.

As Views were passive in MVP, here the View layer is much more flexibile as an indefinite number of Views can bind to a ViewModel. Also, MVVM enforces a clear separation between Views and their master - ViewModel, as the latter holds no reference to Views. The model layer is completely isolated and centralized through the repository pattern.

![Presentation](https://github.com/catalinghita8/android-mvvm-rxjava-dagger2/blob/master/readme_pics/mvvm_diagram.png)

## Model Layer
The model layer is structured on repository pattern so that the ViewModel has no clue on the origins of the data. 

The repository handles data interactions and transactions from two main data sources - local and remote:
- `QuakesRemoteDataSource` defined by a REST API consumed with [Retrofit](http://square.github.io/retrofit)
- `QuakesLocalDataSource` defined by a SQL database consumed with [Room](https://developer.android.com/topic/libraries/architecture/room)

There are two main use-cases, online and offline. In both use cases, `QuakesLocalDataSource` has priority. In the online use-case if the local data is stale, new data is fetched from the `NewsRemoteDataSource` and the repository data is refreshed. In case of no internet connection,  `QuakesLocalDataSource` is always queried.

Decoupling is also inforced within the Model layer (entirely consisted by `QuakesRepository`). Therefore, lower level components (which are the data sources: `QuakesRemoteDataSource` and `QuakesLocalDatasource`) are decoupled through `QuakesDataSource` interface. Also, through their dependence on the same interface, these data sources are interchangeable.

In this manner, the project respects the DIP (Dependency Inversion Principle) as both low and high level modules depend on abstractions.
### Reactive approach
It is extremely important to note that this project is not essentially a reactive app as it is not capitalizing the enormous potential of a fully reactive approach.
Nevertheless, the app was intended to have a flexible and efficient testing capability, rather than a fully reactive build.

Even in this case, we are able to notice RxJava's benefits when data is being retrieved from the repository through different sources and then is channeled through the ViewModel and finally consumed in Views.
- Data Flow is centralized.
- Threading is much easier, with no need for the dreaded `AsyncTasks`.
- Error handling is straightforward and comfortable.
## Dependency Injection
Dagger2 is used to externalize the creation of dependencies from the classes that use them. Android specific helpers are provided by `Dagger-Android` and the most significant advantage is that they generate a subcomponent for each `Activity` through a new code generator.
Such subcomponent is:
```java
@ActivityScoped
@ContributesAndroidInjector(modules = QuakesModule.class)
abstract QuakesActivity quakesActivity(); 
```
The below diagram illustrates the most significant relations between components and modules. An important note is the fact that the ViewModel is now `@AppScoped` whereas in MVP the Presenter is `@ActivityScoped` - this is mainly due to the fact that in MVVM the ViewModel is a Android Architecture Component so therefore has a greater scope  than Views. You can also get a quick glance on how annotations help us define custom Scopes in order to properly handle classes instantiation.
![Dependecy](https://github.com/catalinghita8/android-mvvm-rxjava-dagger2/blob/master/readme_pics/mvvm_dagger_dependency.png)
_Note: The above diagram might help you understand how Dagger-android works. Also, only essential components/modules/objects are included here, this is suggested by the "…"_
## Testing
The apps' components are extremely easy to test due to DI achieved through Dagger and the project's structure, but as well for the reason that the data flow is centralized with RxJava which results in highly testable pieces of code. 

Unit tests are conducted with the help of Mockito and Instrumentation tests with the help of Espresso. 
## Strong points
- Decoupling level is high.
- Data Flow is centralized through RxJava.
- Possess high flexibility to create variants for automated and manual testing.
- Possess lightweight structure due to MVVM presentation pattern.
- Is scalable and easy to expand.
## Weak points
- Possess high code base - simpler approaches will certainly lower code size
- Possess medium complexity - other approaches might lower complexity and increase efficiency.

# Final notes:
- The app is not a polished ready-to-publish product, it acts as a boilerplate project or as a starting point for android enthusiasts out there.
- Using this project as your starting point and expanding it is also encouraged, as at this point it is very easy to add new modules.
