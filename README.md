# Cajero Cliente-Servidor
### Por Daniel Rodriguez Muñoz - 2ºDAM

---
Este programa funciona leyendo y modificando un CSV que actúa como base de datos y que 
almacena la información de los usuarios: 
- Nombre
- Correo (que actúa como id)
- contraseña 
- Saldo (el cual se modifica con cada transacción)
- Saldo maximo (el maximo de dinero que se puede sacar en una transacción)

---

El logger.log ubicado en resources sirve para llevar un historial de los eventos sucedidos 
en el servidor.

users.csv es, como se ha dicho antes, la "base de datos"

Los clientes se conectan al servidor, quien les releva al ClientManager, un hilo encargado de, 
como su nombre indica, gestionar a cada cliente.

El cliente puede crear una nueva cuenta o logearse desde una ya existente, siempre y cuando 
introduzca la contraseña correcta, claro.

Una vez logeado, el cliente podrá solicitar una de las siguientes transacciones:
- Sacar dinero
- Meter dinero
- Ver cuanto dinero tiene guardado
- salir

---

He intentado hacer el código lo más autoexplicativo posible, aunque de todas formas añadiré documentación 
mañana.