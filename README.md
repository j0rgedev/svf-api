# SVF API

Este es el diseño de la API para el sistema de matriculas

## API Reference

## Base URL
/api/v1/alumno/matricula/

#### Validar inicio de sesión

```http
  POST /login/${cod}

```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `cod` | `string` | *Requerido*. Tu código de alumno |

`Cuerpo`
```json
{
  "codigo_alumno": "",
  "contraseña": ""
}
```


`Respuesta`
```json
{
  "token": ""
}
```

#### Registrar alumno

```http
  POST /registrar/${cod}

```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `cod` | `string` | *Requerido*. Tu código de alumno |

`Cuerpo`
```json
{
  "contraseña": ""
}
```


`Respuesta`
```json
{
  "token": ""
}
```

#### Validacion por sms

```http
  POST /validacion

```

`Cuerpo`
```json
{
  "token": "",
  "codigo": ""
}
```


`Respuesta`
```json
{
  "sms": ""
}
```

#### Proceso matricula 1er paso

```http
  GET /

```
`Cuerpo`
```json
{
  "token": ""
}
```


`Respuesta`
```json
{
  "codigo": "",
  "nivel": "",
  "grado": ""
}
```

#### Detalles de matricula

```http
  GET /details

```

`Cuerpo`
```json
{
  "token": ""
}
```


`Respuesta`
```json
{
  "año_escolar": "",
  "info_principal": "",
  "tyc": [
      {
          "titulo": "",
          "cuerpo": ""
      },
      {
          "titulo": "",
          "cuerpo": ""
      },
      {
          "titulo": "",
          "cuerpo": ""
      }
  ],
  "montos": [
      {
          "nivel": "",
          "costo": ""
      },
      {
          "nivel": "",
          "costo": ""
      },
      {
          "nivel": "",
          "costo": ""
      }
  ]
}
```
#### Confirmacion de matricula

```http
  POST /confirmacion

```

`Cuerpo`
```json
{
  "token": ""
}
```


`Respuesta`
```json
{
  "nro_matricula": ""
}
```