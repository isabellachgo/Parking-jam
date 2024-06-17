# parking-jam

## Table of Contents

* [Introduccion](#introduccion)
* [Organización del proyecto](#organización-del-proyecto)
* [Cómo Jugar](#cómo-jugar)
* [Autores](#autores)

## Introduccion

En este proyecto se ha desarrollado un videojuego llamado parking-jam. La este videojuego consiste en conseguir que un coche de color rojo consiga salir de un parking en el que se encuentra atrapado.

## Organización del proyecto

El proyecto esta divivido en varias carpetas que derivan de una carpeta inicial 'src':

- Dentro de la carpeta 'src' se encuentra la carpeta 'main', que contiene otras carpetas que contienen toda la funcionalidad del videojuego, y otra carpeta 'test' que contiene todos los tests con los que hemos probado que todo el videojuego funcione correctamente.

- Dentro de la carpeta 'main' en 'src' hay 3 subcarpetas, la primera llamada 'gamesSaved' donde se guardaran los archivos .txt que toda la informacion de cada partida que haya sido guardada por un jugador en el videojuego para posteriormente poder ser cargada. La segunda carpeta llamada 'java' contiene otras tres carpetas con toda la implementacion y funcionalidad del videojuego. Y la tercera llamada 'resources' que contiene todos los archivos externos utilizados para el desarrollo del videojuego, como las imagenes de los objetos usados en la interfaz o el estado inicial de los niveles del juego. 

- La carpeta 'java' (dentro de src/main) se divide en 3 subcarpetas que contienen la funcionalidad del videojuego, como el proyecto está desarrollado siguiendo la arquitectura MVC, estas 3 subcarpetas separan las funcionalidades 'model', 'view' y 'controller', en la carpeta 'model' se encuentran todas las clases .java que con toda la base lógica del proyecto, la carpeta 'view' contiene todas las clases .java con las interfaces que verá el jugador y con las que interactuará para jugar al juego, y la carpeta 'controller' contiene una clase .java es la que se encarga de recibir la informacion sobre las interacciones que el jugador realiza con las interfaces y utiliza los métodos y clases de la carpeta 'model' para que el sistema reaccione correctamente a las interacciones del jugador.

## Cómo Jugar

- Para jugar tan solo se deberá ejecutar el archivo ejecutable del proyecto situado en el directorio raíz.
- Al inicio tendrás que crear una nueva partida y al crearla ya podrás acceder a los niveles del juego, que irás desbloqueando cada vez que logres pasarte uno. Una vez logres pasarte un nivel podrás volver a intentarlo para buscar obtener una mejor puntuación. En todo momento podras volver al menu de niveles, al de partidas, cerrar el juego o guardar partida clickando los botones del menu desplegable.
- Para guardar partida solo tendrás que hacer click en el boton 'save game' del menu desplegable, y cuando quieras volver a cargar la partida, en el menu de partidas, al hacer click en el boton load game, se te mostrarán las partidas guardadas y podrás elegir la partida que guardaste para continuar donde lo dejaste.

## Autores

- Sonia Gallego Trapero
- Lucas Coronel Naranjo
- Isabella Chaves Gómez
- Raúl López Soto


