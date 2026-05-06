# BIBLIOTECA
## Sprint1 (4/05)

### US1: Carga de datos desde fichero a base de datos

Modificar la aplicación para que, cuando se abra, se cargue el fichero `empresa.dat` en base de datos en vez de en memoria.

Cuando arranque la aplicación, se deben cargar los datos existentes en el fichero binario `ficheroDatos/empresa.dat` en la base de datos, en vez de cargarlos en RAM.

Si el trabajador ya existe en la base de datos, la aplicación intentará actualizar sus datos.

### Tareas

| Nº | Tarea | Descripción |
|---:|-------|-------------|
| 1 | Crear base de datos | Crear la base de datos `empresa` en MySQL. |
| 2 | Crear paquete `config` | Crear el paquete `config` con la configuración de MySQL. |
| 3 | Crear paquete `dao` | Crear el paquete `dao` con la clase `AccesoTrabajador.java`. |
| 4 | Crear métodos DAO | Implementar métodos como insertar, borrar y actualizar trabajadores. |
| 5 | Crear paquete `modelo` | Crear o utilizar el paquete `modelo` con la clase `Trabajador`. |
| 6 | Método insertar lista | Crear el método `insertar(ArrayList<Trabajador> trabajadores)` en `AccesoTrabajador`. |
| 7 | Control de duplicados | El método deberá recorrer la lista objeto por objeto, intentando insertar cada trabajador. Si ya existe en la base de datos, se actualizarán sus datos. |

---

### US2: Modificar la aplicación para trabajar con base de datos

Modificar la aplicación ya existente para que realice las operaciones directamente contra la base de datos.

| Operación | Descripción |
|----------|-------------|
| Alta | Insertar trabajadores en la base de datos. |
| Borrado | Eliminar trabajadores de la base de datos. |

---

### Alta de trabajadores

| Requisito | Descripción |
|----------|-------------|
| Eliminar campo `id` | El `id` no debe pedirse en el `JDialog` de alta, ya que será autoincremental en la base de datos. |
| DNI obligatorio | El DNI debe ser obligatorio. |
| Validación de DNI | El DNI debe ser válido: 8 dígitos + letra, y la letra debe corresponder con el número del DNI. |
| Aviso de error | Si el DNI no es válido, el programa debe avisar al usuario. |
| Nombre obligatorio | El nombre debe ser obligatorio. |
| Apellidos obligatorios | Los apellidos deben ser obligatorios. |
| Teléfono obligatorio | El teléfono debe ser obligatorio. |
| Validación de teléfono | El teléfono debe ser un número válido. |
| Carga de puestos | Opcional: obtener de la base de datos los diferentes puestos existentes y cargarlos en el `JComboBox`, en vez de cargar los items del combo box de forma estática. |

---

### Borrado de trabajadores

| Requisito | Descripción |
|----------|-------------|
| Borrado por tabla | Opcional: en vez de pedir un `id` para borrar el trabajador, mostrar todos los trabajadores en una `JTable`. |
| Selección de trabajador | El usuario debe poder posicionarse en una fila de la tabla. |
| Botón borrar | Al hacer clic en borrar, se debe eliminar el trabajador seleccionado. |
| Recarga automática | Después del borrado, la tabla debe recargarse automáticamente para que no aparezca el trabajador eliminado. |


## Sprint2: Listado Trabajadores y Modificar trabajador (8/05)

### US3: Modificar Listado (para que ataque a bbdd)
- OPCIONAL: Aplicar ordenación a la tabla, filtrado de datos (buscar)

### US4: Implementar trabajador (Completo). Carga automática en el JDialog de los datos de la fila del listado seleccionada para que el usuario pueda modificar los campos que requiera.

- OPCIONAL: Poder modificar los datos desde la propia tabla (Ojo el puesto es un ComboBox)


## Sprint3: Buscar trabajador (13/05)

### US5: Buscar trabajador (por id o por DNI)

Opcional: Poder buscar trabajadores (usando filtros) por varios campos (el resultado no será un único trabajador, sino una tabla)


## Sprint4: Exportación CSV y JSON

### US6: Exportación CSV y JSON


## Práctica GUI- Entrega final (18/05)
