# Inicio de Sesión
Este endpoint se utiliza para iniciar sesión en la aplicación.

```http
POST /login
```

`Body`
```json
{
  "userCode": "",
  "password": ""
}
```

`Response`
```json
{
  "accessToken": ""
}
```

`Response (contraseña por defecto)`
```json
{
  "redirectUrl": ""
}
```