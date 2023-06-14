# SVF API
Documentación oficial de la API del colegio San Vicente Ferrer.

## Tabla de contenidos
A continuación se detallará como se debe consumir la API correctamente.

### Prerrequisitos
Antes de empezar, se debe tener instalado lo siguiente:
- JDK 17
- IntelliJ IDEA

### Instalación
Para poder ejecutar el proyecto, se debe seguir los siguientes pasos:
1. Clonar el repositorio
2. Abrir el proyecto en IntelliJ IDEA
3. Dirigirse a la clase `SvfApiApplication.java` y ejecutarla

### URL de la API
Debido a que esta API se encuentra en desarrollo, se debe utilizar la siguiente URL base para consumir los servicios por el momento:  
**http://localhost:8080/api/v1/**

### Autenticación
Para poder consumir los servicios de esta API, se debe autenticar mediante un token JWT. El token temporal tienen una duración de 5 minutos, y el token permanente tienen una duración de 1 día.
- [Inicio de Sesión](docs/Login.md)

En caso de los estudiantes, si tienen una contraseña por defecto, se les pedirá que la cambien por una nueva, pero primero deberán validar su identidad mediante un código que se les enviará a su número de celular.
- [Validación por SMS](docs/SMSValidation.md)
- [Actualización de contraseña](docs/UpdatePassword.md)


### Endpoints
Este proyecto tiene 3 páginas principales: la matrícula del alumno, la intranet del alumno y la intranet del administrador. Por lo tanto, los endpoints se dividen en 3 grupos para una mayor comprensión.

#### Matrícula
Esta página es la que se encarga de la matrícula del alumno y cuenta con los siguientes endpoints:
- [Información del estudiante](docs/StudentInfo.md)
- [Detalles de la matrícula](docs/EnrollmentDetails.md)
- [Proceso de la matrícula](docs/EnrollmentProcess.md)

### Intranet del administrador
Esta página se encarga del CRUD de los estudiantes y toda la información relacionada con los pagos de pensiones y matrícula, y cuenta con los siguientes endpoints:
- CRUD Estudiantes
  - [Búsqueda general de estudiantes](docs/StudentSearch.md)
  - [Búsqueda de estudiante por código](docs/StudentSearchByCode.md)
  - [Lista de estudiantes](docs/StudentList.md)
  - [Agregar estudiante](docs/AddStudent.md)
  - [Actualizar estudiante](docs/UpdateStudent.md)
  - [Eliminar estudiante](docs/DeleteStudent.md)
- Analíticas y estadísticas
  - [Estadísticas principales](docs/Statistics.md)
  - [Estadísticas de matrícula](docs/EnrollmentStatistics.md)
  - [Estadísticas de pensiones](docs/PensionStatistics.md)
  - [Estadísticas de deuda por mes](docs/DebtStatistics.md)

### Intranet del alumno
Esta página se encarga de mostrar toda la información relacionada con el alumno, y cuenta con los siguientes endpoints:
- [Pensiones del estudiante](docs/StudentPensions.md)

### Manejo de errores
- En desarrollo
