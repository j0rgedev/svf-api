# SVF API
Documentación de la API del colegio San Vicente Ferrer.

## Tabla de contenidos
A continuación, se detallará como se debe consumir esta API correctamente.

### Pre-requisitos
Antes de empezar, se debe tener instalado lo siguiente:
- JDK 17
- IntelliJ IDEA

### URL de la API
Debido a que esta API se encuentra en desarrollo, se debe utilizar la siguiente URL base para consumir los servicios por el momento:
http://localhost:8080/api/v1/

### Autenticación
Para poder consumir los servicios de esta API, se debe autenticar mediante un token JWT. Los token temporales tienen una duración de 5 minutos, y los token permanentes tienen una duración de 1 día.
- [Inicio de Sesión](docs/Login.md)

En caso de los estudiantes, si tienen una contraseña por defecto, se les pedirá que la cambien por una nueva, pero primero deberán validar su identidad mediante un código que se les enviará a su número de celular.
- [Validación por SMS](docs/SMSValidation.md)
- [Actualización de contraseña](docs/UpdatePassword.md)


### Endpoints
Este proyecto tiene 3 páginas principales: la matrícula del alumno, el intranet del alumno y el intranet del administrador. Por lo tanto, los endpoints se dividen en 3 grupos para una mayor comprensión.

#### Matrícula
- [Información del estudiante](docs/StudentInfo.md)
- [Detalles de la matrícula](docs/EnrollmentDetails.md)
- [Proceso de la matrícula](docs/EnrollmentProcess.md)

### Intranet del administrador
- En desarrollo

### Intranet del alumno
- En desarrollo