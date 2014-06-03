Animal Book Game Version 0.5.0

Download Eclipse + ADT Bundle:
http://developer.android.com/sdk/index.html

Extract contents to program directory.
Run eclipse and select Github directory for workbench.

Go to Help>Install New Software...
Then enter the following in the "Work with: " field:
http://dist.springsource.com/release/TOOLS/gradle
Install the software

Pull animalBook-Game from Github into Github directory. DO NOT pull "local.properties" file. You will get an error.
Clone URL:
https://github.com/tgco/animalBook.git

Go to eclipse, select import (By right clicking package explorer window), select import gradle project, select animalBook folder in Github, the click on "Build Model".

This will take a while.

The animalBook_Game-core contains all of the main code.

To run android, launch from animalBook_Game-android, to launch desktop, launch animalBook_Game-desktop, etc.

Every time you add/edit new resources, make sure to refresh each folder for the changes to show.

Make sure to add the libraries to build path in android and core.

For more information or if you run into errors, refer to this site:
https://github.com/libgdx/libgdx/wiki/Setting-up-your-Development-Environment-%28Eclipse%2C-Intellij-IDEA%2C-NetBeans%29