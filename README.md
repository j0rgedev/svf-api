# SVF API

API design for SVF Enrollment System

## API Reference

## Base URL
/api/v1/student/enrollment/

#### Login validation

```http
  POST /login/${studentcod}
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `cod` | `string` | *Required*. Your student code |

`Body`
```json
{
  "studentCod": "",
  "password": ""
}
```


`Response`
```json
{
  "token": ""
}
```

#### Register student

```http
  POST /register/${studentcod}
```
| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `cod` | `string` | *Required*. Your studentcod |

`Body`
```json
{
  "password": ""
}
```


`Response`
```json
{
  "token": ""
}
```

#### Validation by SMS

```http
  POST /validation 
```

`Body`
```json
{
  "token": "",
  "studentCod": ""
}
```


`Response`
```json
{
  "msg": ""
}
```

#### Enrollment Process

```http
  GET /

```
`Body`
```json
{
  "token": ""
}
```


`Response`
```json
{
  "studentCod": "",
  "currentLevel": "",
  "grade": "",
  "names": "",
  "lastnames": ""
}
```

#### Enrollment details

```http
  GET /details

```

`Body`
```json
{
  "token": ""
}
```


`Response`
```json
{
  "schoolYear": "",
  "mainInfo": "",
  "terms": [
      {
          "title": "",
          "body": ""
      },
      {
          "title": "",
          "body": ""
      },
      {
          "title": "",
          "body": ""
      }
  ],
  "amounts": [
      {
          "level": "",
          "cost": ""
      },
      {
          "level": "",
          "cost": ""
      },
      {
          "level": "",
          "cost": ""
      }
  ]
}
```
#### Enrollment confirmation

```http
  POST /confirmation
```

`Body`
```json
{
  "token": ""
}
```

`Response`
```json
{
  "enrollmentCod": ""
}
```