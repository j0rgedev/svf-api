# SVF API

API design for the Enrollment system

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
  "studentcod": "",
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
  "studentcod": ""
}
```


`Response`
```json
{
  "msg": ""
}
```

#### Process enrollment

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
  "studentcod": "",
  "currentlevel": "",
  "grade": ""
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
  "schoolyear": "",
  "main_info": "",
  "tandc": [
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
  "enrollmentcod": ""
}
```