# RemoteDrawing

RemoteDrawing is a small Android app to educational only.
It uses node.js and socket.io to communicate trough a server.

The client is available on [github here][RemDrawSock] made by [Akhu]

### Install instruction for RemoteDrawingSocket

This will run [node.js] server

  - Download RemoteDrawingSocket and unzip it
  - Download and install [node.js]
  - Open your favorite Terminal and run
  - Navigate to your RemoteDrawingSocket directory with cd command
  - Type `npm install` in that directory
  - Wait till everything is downloaded and installed
  - (Optional) Again, go to the command prompt and type `npm install socket.io`
  - Type `node app.js`. 

  Verify the deployment by navigating to your server address in your preferred browser.

```sh
localhost:3000
```

### Android app

This will launch the Android App 

  - Download  and install [Android Studio]
  - Download and unzip the project
  - In [Android Studio] choose `open an existing Android Studio project`
  - Open `SocketHelper` class and edit the `HOST` string to your own node.js server address
  - Run the app module

#### Dependencies

RemoteDrawing requires [Socket.io-Client][Socket.io-Java] Java to run.

Gradle dependency for Android Studio, in `build.gradle`:

```groovy
compile ('io.socket:socket.io-client:0.8.3') {
  // excluding org.json which is provided by Android
  exclude group: 'org.json', module: 'json'
}
```

### Todos

 - Write Tests
 - Code review
 - Add Code Comments

License
----

MIT

   [node.js]: <https://nodejs.org>
   [RemDrawSock]: <https://github.com/Akhu/RemoteDrawingSocket>
   [Akhu]: <https://github.com/Akhu>
   [Android Studio]: <https://developer.android.com/studio/index.html>
   [Socket.io-Java]: <https://github.com/socketio/socket.io-client-java>
