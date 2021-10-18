# Stash Challenge #


### To run the challenge ###

Project compiled on AndroidStudio ArticFox 2020.3.1 patch 2

### Please in order to protect the base url and api_key add it to the local.properties file, and i removed the strings for the xml in order to protect it. If you don't add it  the project will not run.  ###



baseUrl="https://us-central1-stashandroidchallenge.cloudfunctions.net/api/"
apiKey="88ecd4c66b35da800a96364ceaf3c436" 


### What does the app currently do though? ###
Currently the app has a search field that you can enter something cool and it will fill the screen will a grid of images.

### Tasks ###
1. Convert the existing code base to use RxJava
1. Convert the existing code base to the Model-View-Presenter pattern
1. Add a new feature
1. Create an archive (.tar, .zip, etc.) of the codebase and return it

### So whats the new feature? ###
When a user performs a long press on an image, pop up a dialog that display the pressed image, image title, image artist, and 3 similar images. (Hint: Check out the api folder for models and requests available)

### Extra Credit ###
Create unit test for your Model-View-Presenter changes

### Notes ###
Once you obtain your key you can integrate it by adding it to "api_key" in the string resources file.

If you have any questions don't hesitate to reach out.
