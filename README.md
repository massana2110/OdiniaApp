# OdiniaApp
Odinia Application for Android 

## Implementación del aplicativo en un entorno de desarrollo.

En primer lugar, para el desarrollo del mismo se utilizo Android Studio en su versión 3.6, por lo que se recomienda
utilizar dicha versión para no tener problemas de compatibilidad. El siguiente paso es la clonacion de este repositorio,
ya se encuentran aqui todos los recursos graficos necesarios por lo que no se necesita descargar nada mas.

Para el sistema backend se opto por utilizar el servicio de Firebase por parte de Google ya que cumple con los alcances
y objetivos del proyecto. Para tener acceso a la consola de Firebase se debe enviar un correo a la direccion: 
david.massana.10@gmail.com para asi proceder a agregar su usuario al proyecto. Como maximo se puede tener hasta 6 cuentas.

Pero si solo se desea testear el aplicativo aqui listamos usuarios de prueba (username - email):

- User Test Facebook: user_fawncre_facebook@tfbnw.net 
- Test User: test@gmail.com

## Estructura del Proyecto

Aqui se detallan todos los archivos (Clases .kt, archivos xml, carpetas) utilizados.

Se ha agrupado por paquetes: viewmodel y views/ui. Dentro de viewmodel se encuentran todos los viewmodel implementados
y en la carpeta views/ui todo lo visual dividido en activities y fragments. Se busco seguir el patrón de arquitectura mvvm.

Para los recursos como drawables, layouts, mipmap, cada uno esta en sus carpetas respectivas por lo que si se desea modificar
uno de estos archivos o agregar uno asegurarse de guardarlo en su lugar correspondiente para mantener la estructura.
