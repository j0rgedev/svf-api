# Actualizar contraseña
Este endpoint se utiliza para actualizar la contraseña del estudiante.

```http
PUT /login/updatepassword/
```
`Header`
```json
{
  "Authorization": "Bearer TU_TOKEN_JWT_TEMPORAL_AQUI"
}
```

`Body`
```json
{
  "newPassword": ""
}
```


`Response`
```json
{
  "accessToken": ""
}
```