michaelmerzin, sean.cherny

323913277, 213716608


=============================
=      File description     =
=============================
World package -
conatain the daynight package and trees package
in addition it contains Avatar, Block, Cow,Energy , Sky and Terrain classes
daynight package -
Night - the class that make the darkness effect over the screen
Sun - the class that make the sun object and  responsible for the sun movement
SunHalo - the class that make the sun halo effect
trees package -
Tree - the class that make the tree object
Avatar - the class that make the avatar object and responsible for the avatar movement and the avatar animation
Block - the class that make the block object
Cow - the class that make the cow object and responsible for the cow movement and the cow animation
Energy - the class that make the text that represent the energy of the avatar
Sky - the class that make the sky object
Terrain - the class that make the terrain. the terrain is made of blocks .
PepseGameManager- the class that responsible for the game logic and the game flow.
SimplexNoise - the class that make the noise that used to generate the terrain
SimplexNoiseOctaved - the class that make the noise that used to generate the terrain

=============================
=          Design           =
=============================
in this project we tried to keep all the classes as simple as possible.
we tried that each class will have only one responsibility.
in addition we keeped the classes not dependent in each other.
for exmaple if we wanted to get the height we need we gave the function the x and the function of
terrain and the function return the heightm instead of be dependent on the terrain object.



=============================
=  Implementation details   =
=============================
Night -It contains a static method create that creates a new night sky and adds it to a GameObjectCollection,
       and a private static method fadeOut that fades out the night sky over a given length of time.
       To create a new night sky, call the create method with the following parameters:
       gameObjects: The GameObjectCollection to add the night sky to.
       Layer: The layer of the night sky in the GameObjectCollection.
       windowDimensions: The dimensions of the window as a Vector2.
       cycleLength: The length of the cycle for the fading out transition.
       This will return the created night sky as a GameObject.
Sun-It contains a static method create that creates a new sun and adds it to a GameObjectCollection,
 and a private static method rotateSun that rotates the sun over a given length of time.
 we didnt add an api .
SunHalo-It contains a static method create that creates a new sun halo and adds it to a GameObjectCollection,
 and a private static method rotateSun that rotates the sun halo over a given length of time.
 we didnt add an api .
 Tree-


=============================
=    Answers to questions   =
=============================

1)


2)
