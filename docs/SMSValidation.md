# Validación por SMS
Este endpoint se utiliza para validar el código de verificación enviado por SMS al número de celular del estudiante.

```http
POST /login/sms-validation
```

`Header`
```json
{
  "Authorization": "Bearer TU_TOKEN_JWT_TEMPORAL_AQUÍ"
}
```

`Body`
```json
{
  "smsCode": ""
}
```

`Response`
```json
{
  "tempToken": ""
}
```
